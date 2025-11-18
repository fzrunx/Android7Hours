package com.sesac.domain.model

sealed class UiEvent {
    data class ToastEvent(val message: String): UiEvent()
}
