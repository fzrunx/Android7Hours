package com.sesac.auth.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.auth.utils.ValidationUtils
import com.sesac.domain.model.Auth
import com.sesac.domain.model.JoinFormState
import com.sesac.domain.model.LoginRequest
import com.sesac.domain.model.LoginResponse
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.JoinUiState
import com.sesac.domain.usecase.auth.AuthUseCase
import com.sesac.domain.usecase.session.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import com.sesac.auth.utils.validate
import kotlinx.coroutines.delay

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

    // ... (onEmailChange ~ onJoinClick 함수는 변경 없음) ...

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
        val validationResult = _joinFormState.value.validate()

        // 디버깅용: 유효하지 않은 필드 로그
        if (!validationResult.isValid) {
            Log.d("Validation", "Invalid fields: ${validationResult.invalidFields}")
        }

        return validationResult.isValid
    }

//    fun onSubmit() {
//        _joinFormState.update { it.copy(showValidationErrors = true) }
//
//        if (validateForm()) {
//            // 폼 제출 로직
//            submitJoinForm()
//        }
//    }

    fun onJoinClick() {
        _joinFormState.update { it.copy(showValidationErrors = true) }
        if (!validateForm()) {
            return
        }

        viewModelScope.launch {
            _joinUiState.value = JoinUiState.Loading
            val form = _joinFormState.value

            authUseCase.postUser(form.toAuth())
                .collectLatest { result ->
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
                            // [참고] result.resultData가 이미 LoginResponse 타입이므로
                            // val loginResponse = LoginResponse(...) 코드는 생략 가능합니다.
                            sessionUseCase.saveSession(result.resultData)
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

    // --- [수정] onKakaoLoginSuccess 함수 ---
    fun onKakaoLoginSuccess(accessToken: String) {
        Log.d("AuthViewModel", "Received Kakao Access Token: $accessToken")
        _joinUiState.value = JoinUiState.Loading

        viewModelScope.launch {
            // [수정] 이제 AuthUseCase에 실제로 존재하는 함수를 호출합니다
            // (물론, 3단계에 걸쳐 이 함수를 Domain, Data 계층에 만들어야 합니다)
            authUseCase.loginWithKakao(accessToken)
                .collectLatest { result ->
                    when (result) {
                        // [수정] onLoginClick과 동일한 로직을 사용합니다
                        is AuthResult.Success -> {
                            sessionUseCase.saveSession(result.resultData)
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
    // ---

    fun resetUiState() {
        _joinUiState.value = JoinUiState.Idle
    }

}