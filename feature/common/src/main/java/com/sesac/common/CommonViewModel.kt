package com.sesac.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.local.model.CommonUiState
import com.sesac.domain.remote.usecase.session.SessionUseCase
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

    val uiState: StateFlow<CommonUiState> = combine(
        sessionUseCase.getAccessToken(),
        sessionUseCase.getUserInfo()
    ) { token, userInfo ->
        if (token != null && userInfo != null) {
            CommonUiState(isLoggedIn = true, nickname = userInfo.nickname, fullName = userInfo.fullName)
        } else {
            CommonUiState(isLoggedIn = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CommonUiState()
    )

    fun onLogout() {
        viewModelScope.launch {
            sessionUseCase.clearSession()
        }
    }
}
