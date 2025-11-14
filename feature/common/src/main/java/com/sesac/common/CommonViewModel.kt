package com.sesac.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.CommonAuthUiState
import com.sesac.domain.usecase.session.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val sessionUseCase: SessionUseCase
) : ViewModel() {

    val uiState: StateFlow<CommonAuthUiState> = combine(
        sessionUseCase.getAccessToken(),
        sessionUseCase.getUserInfo()
    ) { token, user ->
        if (token != null && user != null) {
            CommonAuthUiState(
                isLoggedIn = true,
                token = token,
                id = user.id,
                nickname = user.nickname,
                fullName = user.fullName,
                email = user.email,
            )
        } else {
            CommonAuthUiState(isLoggedIn = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CommonAuthUiState()
    )

    fun onLogout() {
        viewModelScope.launch {
            sessionUseCase.clearSession()
        }
    }
}
