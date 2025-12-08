package com.sesac.common.model

sealed class UiEvent {
    data class ToastEvent(val message: String): UiEvent()
}
