package com.sesac.domain.usecase.monitor

data class MonitorUseCase(
    val getAllDummyLatLngUseCase: GetAllDummyLatLngUseCase,
    val getRandomDummyLatLngUseCase: GetRandomDummyLatLngUseCase,
)