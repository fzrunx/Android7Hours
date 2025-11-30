package com.sesac.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.result.AuthUiState
import com.sesac.domain.usecase.session.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val sessionUseCase: SessionUseCase
) : ViewModel() {

    val uiState: StateFlow<AuthUiState> = combine(
        sessionUseCase.getAccessToken(),
        sessionUseCase.getUserInfo()
    ) { token, user ->
        Log.d("CommonViewModel", "Combining user info: $user")
        if (token != null && user != null) {
            AuthUiState(
                isLoggedIn = true,
                token = token,
                user = user,
//                id = user.id,
//                nickname = user.nickname,
//                fullName = user.fullName,
//                email = user.email,
//                profileImageUrl = user.profileImageUrl,
            )
        } else {
            AuthUiState().reset()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AuthUiState()
    )

    fun onLogout() {
        viewModelScope.launch {
            uiState.value.reset()
            sessionUseCase.clearSession()
        }
    }
}
