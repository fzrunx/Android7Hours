package com.sesac.monitor.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.common.ui_state.MonitorUiState
import com.sesac.common.usecase.webrtc.WebRTCUseCase
import com.sesac.domain.model.Pet
import com.sesac.domain.model.User
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.ResponseUiState
import com.sesac.domain.usecase.pet.PetUseCase
import com.sesac.domain.usecase.session.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.webrtc.VideoTrack
import javax.inject.Inject

@HiltViewModel
class MonitorViewModel @Inject constructor (
    private val petUseCase: PetUseCase,
    private val sessionUseCase: SessionUseCase,
    private val webRTCUseCase: WebRTCUseCase
) : ViewModel() {

    val _activeTab = MutableStateFlow<String>("")
    val activeTab get() = _activeTab.asStateFlow()

    private val _uiState = MutableStateFlow<MonitorUiState>(MonitorUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _monitoredPet = MutableStateFlow<ResponseUiState<Pet>>(ResponseUiState.Idle)
    val monitoredPet = _monitoredPet.asStateFlow()

    private val _monitorablePets = MutableStateFlow<ResponseUiState<List<Pet>>>(ResponseUiState.Idle) // NEW STATE
    val monitorablePets = _monitorablePets.asStateFlow() // NEW EXPOSED STATE

    private val _remoteVideoTrack = MutableStateFlow<VideoTrack?>(null)
    val remoteVideoTrack = _remoteVideoTrack.asStateFlow()

    private var currentUser: User? = null

    init {
        checkUserRole()
    }

    fun selectTab(selectedTab: String) {
        viewModelScope.launch {
            _activeTab.value = selectedTab
        }
    }

    /**
     * 현재 로그인된 사용자의 역할을 확인하고, 역할에 맞는 초기 화면 상태를 설정합니다.
     */
    fun checkUserRole() {
        viewModelScope.launch {
            _uiState.value = MonitorUiState.Loading
            currentUser = sessionUseCase.getUserInfo().first()

            if (currentUser == null) {
                _uiState.value = MonitorUiState.Error("사용자 정보를 가져올 수 없습니다. 다시 로그인해주세요.")
                return@launch
            }

            if (currentUser?.isPet == true) {
                // 사용자가 '펫'인 경우
                _uiState.value = MonitorUiState.PetScreen
            } else {
                // 사용자가 '주인'인 경우, 등록된 펫 목록을 가져옵니다.
                val token = sessionUseCase.getAccessToken().first()
                if (token.isNullOrEmpty()) {
                    _uiState.value = MonitorUiState.Error("인증 정보가 없습니다.")
                    return@launch
                }
                petUseCase.getUserPetsUseCase(token).collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            val monitorablePets = result.resultData.filter { it.linkedUser != null }
                            _uiState.value = MonitorUiState.OwnerScreen(monitorablePets)
                        }
                        is AuthResult.NetworkError -> {
                            _uiState.value = MonitorUiState.Error(result.exception.message ?: "펫 목록 로드 실패")
                        }
                        else -> { // Loading
                            _uiState.value = MonitorUiState.Loading
                        }
                    }
                }
            }
        }
    }

    fun getMonitorablePets() { // NEW FUNCTION
        viewModelScope.launch {
            _monitorablePets.value = ResponseUiState.Loading
            val token = sessionUseCase.getAccessToken().first()
            if (token.isNullOrEmpty()) {
                _monitorablePets.value = ResponseUiState.Error("로그인이 필요합니다.")
                return@launch
            }

            petUseCase.getUserPetsUseCase(token).collectLatest { result ->
                when (result) {
                    is AuthResult.Success -> {
                        val monitorableList = result.resultData.filter { it.linkedUser != null }
                        _monitorablePets.value = ResponseUiState.Success("모니터링 가능한 펫 목록을 가져왔습니다.", monitorableList)
                    }
                    is AuthResult.NetworkError -> {
                        _monitorablePets.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                    }
                    else -> {
                        _monitorablePets.value = ResponseUiState.Error("펫 목록을 가져오는데 실패했습니다.")
                    }
                }
            }
        }
    }

    fun startMonitoringPetLocation(petId: Int) {
        viewModelScope.launch {
            _monitoredPet.value = ResponseUiState.Loading
            while (true) {
                petUseCase.getPetInfoUseCase(petId).collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            val pet = result.resultData // getPetInfoUseCase returns List<Pet>
                            if (pet.lastLocation != null) {
                                _monitoredPet.value = ResponseUiState.Success("위치를 가져왔습니다.", pet)
                            } else {
                                _monitoredPet.value = ResponseUiState.Error("해당 펫을 찾을 수 없습니다.")
                            }
                        }
                        is AuthResult.NetworkError -> {
                            _monitoredPet.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                        }
                        else -> {
                           // Loading is handled outside, other states are ignored for now
                        }
                    }
                }
                delay(10000) // 10초마다 갱신
            }
        }
    }

    /**
     * 주인(Owner)이 펫 모니터링을 시작할 때 호출합니다.
     */
    fun startCall(pet: Pet) {
        viewModelScope.launch {
            Log.d("MonitorViewModel", "Starting call to pet: ${pet.name}")
            // TODO: WebRTC 'Offer' 생성 및 시그널링 서버로 전송 로직 구현
            _uiState.value = MonitorUiState.Calling(pet)
        }
    }

    /**
     * 펫(Pet)이 스트리밍을 준비할 때 호출합니다.
     */
    fun prepareStreaming() {
        viewModelScope.launch {
            Log.d("MonitorViewModel", "Pet is preparing for streaming...")
            // TODO: WebRTC 로컬 미디어(카메라) 시작 및 시그널링 서버 연결 대기 로직 구현
            _uiState.value = MonitorUiState.Streaming
        }
    }

    /**
     * 통화를 종료하거나 취소합니다.
     */
    fun endCall() {
        viewModelScope.launch {
            Log.d("MonitorViewModel", "Ending call.")
            // TODO: WebRTC 세션 종료 및 시그널링 서버에 통화 종료 메시지 전송 로직 구현
            webRTCUseCase.closeSession()
            // 역할에 따라 이전 화면으로 돌아갑니다.
            checkUserRole()
        }
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel이 소멸될 때 WebRTC 세션을 반드시 종료합니다.
        webRTCUseCase.closeSession()
    }
}