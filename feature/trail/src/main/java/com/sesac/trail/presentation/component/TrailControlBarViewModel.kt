package com.sesac.trail.presentation.component

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class TrailState {
    Idle, Running, Paused, Stopped
}

class TrailControlBarViewModel : ViewModel() {
    private val _trailState = MutableStateFlow(TrailState.Idle)
    val trailState = _trailState.asStateFlow()

    fun start() = setState(TrailState.Running)
    fun pause() = setState(TrailState.Paused)
    fun resume() = setState(TrailState.Running)
    fun stop() = setState(TrailState.Stopped)
    fun reset() = setState(TrailState.Idle)

    private fun setState(newState: TrailState) {
        _trailState.value = newState
    }
}