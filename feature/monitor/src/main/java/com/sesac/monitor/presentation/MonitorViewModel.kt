package com.sesac.monitor.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.Coord
import com.sesac.domain.usecase.monitor.MonitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonitorViewModel @Inject constructor (
    private val monitorUseCase: MonitorUseCase,
): ViewModel() {
    val _activeTab = MutableStateFlow<String>("")
    val _coordList = MutableStateFlow<List<Coord?>>(emptyList())
    val _coordRandom = MutableStateFlow<Coord?>(Coord())
    val activeTab get() = _activeTab.asStateFlow()
    val latLngList get() = _coordList.asStateFlow()
    val latLngRandom get() = _coordRandom.asStateFlow()

    init {
        viewModelScope.launch {
            monitorUseCase.getRandomDummyLatLngUseCase().collect { _coordRandom.value = it }
        }
    }

    fun selecteTab(selectedTab: String) {
        viewModelScope.launch {
            _activeTab.value = selectedTab
        }
    }

    suspend fun getLatLngRandom() {
        viewModelScope.launch {
            monitorUseCase.getRandomDummyLatLngUseCase().collect { _coordRandom.value = it }
        }
    }

    suspend fun getLatLngList() {
        viewModelScope.launch {
            monitorUseCase.getAllDummyLatLngUseCase().collect { _coordList.value = it }
        }
    }
}