package com.sesac.monitor.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.Pet
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
import javax.inject.Inject

@HiltViewModel
class MonitorViewModel @Inject constructor (
    private val petUseCase: PetUseCase,
    private val sessionUseCase: SessionUseCase, // NEW INJECTION
): ViewModel() {
    val _activeTab = MutableStateFlow<String>("")
    val activeTab get() = _activeTab.asStateFlow()

    private val _monitoredPet = MutableStateFlow<ResponseUiState<Pet>>(ResponseUiState.Idle)
    val monitoredPet = _monitoredPet.asStateFlow()

    private val _monitorablePets = MutableStateFlow<ResponseUiState<List<Pet>>>(ResponseUiState.Idle) // NEW STATE
    val monitorablePets = _monitorablePets.asStateFlow() // NEW EXPOSED STATE

    fun selectTab(selectedTab: String) {
        viewModelScope.launch {
            _activeTab.value = selectedTab
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

    fun startMonitoringPet(petId: Int) {
        viewModelScope.launch {
            _monitoredPet.value = ResponseUiState.Loading
            while (true) {
                petUseCase.getPetInfoUseCase(petId).collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            val pet = result.resultData.firstOrNull() // getPetInfoUseCase returns List<Pet>
                            if (pet != null) {
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
}