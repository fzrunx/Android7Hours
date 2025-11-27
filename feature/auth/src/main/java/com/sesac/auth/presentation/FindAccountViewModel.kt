package com.sesac.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindAccountViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<FindUiState>(FindUiState.Idle)
    val uiState = _uiState.asStateFlow()

    // [변경] 아이디 찾기 -> 이메일 찾기
    // 전화번호가 없는 유저는 찾을 수 없으므로, 서버에서 "등록된 전화번호가 없습니다" 에러를 내려줘야 함
    fun findEmail(name: String, phoneNumber: String) {
        viewModelScope.launch {
            _uiState.value = FindUiState.Loading
            try {
                // TODO: 실제 API 호출 (서버에 name, phoneNumber 전송)
                delay(1000)

                // 성공 시: 마스킹 된 이메일 반환 (예: se***@gmail.com)
                _uiState.value = FindUiState.SuccessId("sesac***@gmail.com")
            } catch (e: Exception) {
                _uiState.value = FindUiState.Error("일치하는 정보가 없습니다.")
            }
        }
    }

    // [변경] 비밀번호 재설정 (아이디 파라미터 삭제 -> 이름과 이메일로 확인)
    fun resetPassword(name: String, email: String) {
        viewModelScope.launch {
            _uiState.value = FindUiState.Loading
            try {
                // TODO: 실제 API 호출 (서버에 name, email 전송)
                // 서버는 해당 이메일 유저의 이름이 맞는지 확인 후 메일 발송
                delay(1000)

                _uiState.value = FindUiState.SuccessReset("입력하신 이메일로 재설정 링크를 보냈습니다.")
            } catch (e: Exception) {
                _uiState.value = FindUiState.Error("가입된 정보가 없거나 이름이 일치하지 않습니다.")
            }
        }
    }

    fun resetState() {
        _uiState.value = FindUiState.Idle
    }
}

// State는 기존과 동일하게 유지해도 됨 (의미만 통하면 됨)
sealed interface FindUiState {
    data object Idle : FindUiState
    data object Loading : FindUiState
    data class SuccessId(val userId: String) : FindUiState // 여기서 userId는 찾은 이메일이 됨
    data class SuccessReset(val message: String) : FindUiState
    data class Error(val message: String) : FindUiState
}