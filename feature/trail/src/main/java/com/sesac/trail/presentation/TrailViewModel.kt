package com.sesac.trail.presentation

import androidx.lifecycle.ViewModel
import com.sesac.trail.presentation.ui.WalkMode
import com.sesac.trail.presentation.ui.WalkPathUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// 2. ViewModel
class TrailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WalkPathUiState())
    val uiState = _uiState.asStateFlow()

    // React의 모드 변경 로직
    fun onModeChange(newMode: WalkMode) {
        _uiState.update { currentState ->
            val nextMode = if (currentState.activeMode == newMode) {
                WalkMode.NONE // 같은 버튼을 다시 누르면 모드 해제
            } else {
                newMode
            }

            // 모드 변경 시 녹화 중이면 중지
            if (nextMode != WalkMode.RECORD && currentState.isRecording) {
                currentState.copy(
                    activeMode = nextMode,
                    isRecording = false,
                    recordingTime = 0
                )
            } else {
                currentState.copy(activeMode = nextMode)
            }
        }
    }

    // React의 녹화 토글 로직
    fun onToggleRecording() {
        _uiState.update {
            if (it.isRecording) {
                // TODO: 타이머 중지 로직
                it.copy(isRecording = false, recordingTime = 0)
            } else {
                // TODO: 타이머 시작 로직
                it.copy(isRecording = true)
            }
        }
    }

    // '따라가기' 시작
    fun startFollowingPath() {
        _uiState.update {
            it.copy(
                isFollowingPath = true,
                activeMode = WalkMode.FOLLOW
            )
        }
    }
}