package com.sesac.common.ui_state

import com.sesac.domain.model.Pet

// ViewModel에서 화면의 모든 상태를 정의합니다.
sealed interface MonitorUiState {
    data object Loading : MonitorUiState
    data class OwnerScreen(val pets: List<Pet>) : MonitorUiState
    data object PetScreen : MonitorUiState
    data class Calling(val pet: Pet) : MonitorUiState
    data class Viewing(val pet: Pet) : MonitorUiState
    data object Streaming : MonitorUiState
    data class Error(val message: String) : MonitorUiState
}