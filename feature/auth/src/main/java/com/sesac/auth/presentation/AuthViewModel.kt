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

            val userToPost = Auth(
//                id = -1, // Server will generate
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
                            val loginResponse = LoginResponse(result.resultData.access, result.resultData.refresh, result.resultData.user)
                            sessionUseCase.saveSession(loginResponse)
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

    fun resetUiState() {
        _joinUiState.value = JoinUiState.Idle
    }
}