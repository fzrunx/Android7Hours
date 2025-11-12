package com.sesac.android7hours

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.remote.usecase.session.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val isLoggedIn: Boolean = false,
    val nickname: String? = null // Add other user info if needed
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionUseCase: SessionUseCase
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = combine(
        sessionUseCase.getAccessToken(),
        sessionUseCase.getUserInfo()
    ) { token, userInfo ->
        if (token != null && userInfo != null) {
            MainUiState(isLoggedIn = true, nickname = userInfo.nickname)
        } else {
            MainUiState(isLoggedIn = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainUiState()
    )

    fun onLogout() {
        viewModelScope.launch {
            sessionUseCase.clearSession()
        }
    }
}
