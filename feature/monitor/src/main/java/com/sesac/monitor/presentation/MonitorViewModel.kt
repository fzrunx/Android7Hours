package com.sesac.monitor.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.LatLngPoint
import com.sesac.domain.usecase.GetAllDummyLatLngUseCase
import com.sesac.domain.usecase.GetRandomDummyLatLngUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonitorViewModel @Inject constructor (
    private val getAllDummyLatLngUseCase: GetAllDummyLatLngUseCase,
    private val getRandomDummyLatLngUseCase: GetRandomDummyLatLngUseCase,
): ViewModel() {
    val _activeTab = MutableStateFlow<String>("")
    val _latLngPointList = MutableStateFlow<List<LatLngPoint?>>(emptyList())
    val _latLngPointRandom = MutableStateFlow<LatLngPoint?>(LatLngPoint())
    val activeTab get() = _activeTab.asStateFlow()
    val latLngList get() = _latLngPointList.asStateFlow()
    val latLngRandom get() = _latLngPointRandom.asStateFlow()

    init {
        viewModelScope.launch {
            getRandomDummyLatLngUseCase().collect { _latLngPointRandom.value = it }
        }
    }

    fun selecteTab(selectedTab: String) {
        viewModelScope.launch {
            _activeTab.value = selectedTab
        }
    }

    suspend fun getLatLngRandom() {
        viewModelScope.launch {
            getRandomDummyLatLngUseCase().collect { _latLngPointRandom.value = it }
        }
    }

    suspend fun getLatLngList() {
        viewModelScope.launch {
            getAllDummyLatLngUseCase().collect { _latLngPointList.value = it }
        }
    }
}