package com.sesac.auth.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.auth.utils.ValidationUtils
import com.sesac.domain.remote.model.LoginRequest
import com.sesac.domain.remote.model.UserAPI
import com.sesac.domain.remote.result.AuthResult
import com.sesac.domain.remote.usecase.auth.AuthUseCase
import com.sesac.domain.remote.usecase.session.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import kotlinx.coroutines.delay

sealed interface JoinUiState {
    object Idle : JoinUiState
    object Loading : JoinUiState
    data class Success(val message: String) : JoinUiState
    data class Error(val message: String) : JoinUiState
}

data class JoinFormState(
    val email: String = "",
    val isEmailValid: Boolean = true,
    val password: String = "",
    val isPasswordValid: Boolean = true,
    val passwordConfirm: String = "",
    val doPasswordsMatch: Boolean = true,
    val name: String = "",
    val nickname: String = "",
    val phone: String = "",
    val agreeAll: Boolean = false,
    val agreeAge: Boolean = false,
    val agreeTerms: Boolean = false,
    val agreePrivacy: Boolean = false,
    val showValidationErrors: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val sessionUseCase: SessionUseCase
) : ViewModel() {

    private val _joinFormState = MutableStateFlow(JoinFormState())
    val joinFormState = _joinFormState.asStateFlow()

    private val _joinUiState = MutableStateFlow<JoinUiState>(JoinUiState.Idle)
    val joinUiState = _joinUiState.asStateFlow()

    // State for Login Screen
    val loginEmail = mutableStateOf("")
    val loginPassword = mutableStateOf("")

    fun onEmailChange(email: String) {
        val isValid = if (email.isNotEmpty()) ValidationUtils.isValidEmail(email) else true
        _joinFormState.update { it.copy(email = email, isEmailValid = isValid) }
    }

    fun onPasswordChange(password: String) {
        val isPasswordValid = if (password.isNotEmpty()) ValidationUtils.isValidPassword(password) else true
        val doPasswordsMatch = password == _joinFormState.value.passwordConfirm
        _joinFormState.update { it.copy(password = password, isPasswordValid = isPasswordValid, doPasswordsMatch = doPasswordsMatch) }
    }

    fun onPasswordConfirmChange(passwordConfirm: String) {
        val doPasswordsMatch = _joinFormState.value.password == passwordConfirm
        _joinFormState.update { it.copy(passwordConfirm = passwordConfirm, doPasswordsMatch = doPasswordsMatch) }
    }

    fun onNameChange(name: String) {
        _joinFormState.update { it.copy(name = name) }
    }

    fun onNicknameChange(nickname: String) {
        _joinFormState.update { it.copy(nickname = nickname) }
    }

    fun onPhoneChange(phone: String) {
        _joinFormState.update { it.copy(phone = phone) }
    }

    fun onAgreeAllChange(isChecked: Boolean) {
        _joinFormState.update {
            it.copy(
                agreeAll = isChecked,
                agreeAge = isChecked,
                agreeTerms = isChecked,
                agreePrivacy = isChecked
            )
        }
    }

    private fun updateAgreeAll() {
        _joinFormState.update {
            val allChecked = it.agreeAge && it.agreeTerms && it.agreePrivacy
            it.copy(agreeAll = allChecked)
        }
    }

    fun onAgreeAgeChange(isChecked: Boolean) {
        _joinFormState.update { it.copy(agreeAge = isChecked) }
        updateAgreeAll()
    }

    fun onAgreeTermsChange(isChecked: Boolean) {
        _joinFormState.update { it.copy(agreeTerms = isChecked) }
        updateAgreeAll()
    }

    fun onAgreePrivacyChange(isChecked: Boolean) {
        _joinFormState.update { it.copy(agreePrivacy = isChecked) }
        updateAgreeAll()
    }

    private fun validateForm(): Boolean {
        val form = _joinFormState.value
        return form.email.isNotBlank() &&
                form.isEmailValid &&
                form.password.isNotBlank() &&
                form.isPasswordValid &&
                form.passwordConfirm.isNotBlank() &&
                form.doPasswordsMatch &&
                form.name.isNotBlank() &&
                form.nickname.isNotBlank() &&
                form.agreeAge && form.agreeTerms && form.agreePrivacy
    }

    fun onJoinClick() {
        _joinFormState.update { it.copy(showValidationErrors = true) }
        if (!validateForm()) {
            return
        }

        viewModelScope.launch {
            _joinUiState.value = JoinUiState.Loading
            val form = _joinFormState.value

            val userToPost = UserAPI(
                id = 0, // Server will generate
                username = form.email,
                email = form.email,
                fullName = form.name,
                nickname = form.nickname,
                password = form.password,
                passwordVerification = form.passwordConfirm
            )

            authUseCase.postUser(userToPost)
                .collect { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            _joinUiState.value = JoinUiState.Success("Join successful!")
                        }
                        is AuthResult.NetworkError -> {
                            _joinUiState.value = JoinUiState.Error(result.exception.message ?: "Network Error")
                        }
                        is AuthResult.Loading -> {
                            _joinUiState.value = JoinUiState.Loading
                        }
                        else -> {
                            // Not handled state
                        }
                    }
                }
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _joinUiState.value = JoinUiState.Loading

            val loginRequest = LoginRequest(
                email = loginEmail.value,
                password = loginPassword.value
            )

            authUseCase.login(loginRequest)
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            sessionUseCase.saveSession(
                                accessToken = result.resultData.access,
                                refreshToken = result.resultData.refresh,
                                userInfo = result.resultData.user
                            )
                            _joinUiState.value = JoinUiState.Success("Login successful!")
                        }
                        is AuthResult.NetworkError -> {
                            _joinUiState.value = JoinUiState.Error(result.exception.message ?: "Network Error")
                        }
                        is AuthResult.Loading -> {
                            _joinUiState.value = JoinUiState.Loading
                        }
                        else -> {
                            // Not handled state
                        }
                    }
                }
        }
    }
    fun onKakaoLoginSuccess(accessToken: String) {
        // 1. 뷰모델이 토큰을 받았는지 로그로 확인
        Log.d("AuthViewModel", "Received Kakao Access Token: $accessToken")

        // 2. UI 상태를 '로딩'으로 변경 (이메일 로그인과 동일)
        _joinUiState.value = JoinUiState.Loading

        viewModelScope.launch {
            try {
                // --- (TODO: 3. Retrofit + Repository 로직) ---
                // 이 accessToken을 GeoDjango 서버로 전송합니다.
                // val response = authUseCase.sendKakaoToken(accessToken)

                // if (response is AuthResult.Success) {
                //    ... (서버가 준 7Hours 전용 토큰 저장)
                //    sessionUseCase.saveSession(...)
                //    _joinUiState.value = JoinUiState.Success("Login successful!")
                // } else {
                //    _joinUiState.value = JoinUiState.Error("서버 로그인 실패")
                // }
                // ---


                // --- [임시 테스트 코드] ---
                // 지금은 서버가 없으므로, 1초 뒤 강제로 '성공' 상태로 만듭니다.
                delay(1000)
                Log.d("AuthViewModel", "Simulating Kakao login success...")
                _joinUiState.value = JoinUiState.Success("Login successful!") // [수정] Success 상태 통일
                // ---

            } catch (e: Exception) {
                _joinUiState.value = JoinUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    // ---

    fun resetUiState() {
        _joinUiState.value = JoinUiState.Idle
    }
}

