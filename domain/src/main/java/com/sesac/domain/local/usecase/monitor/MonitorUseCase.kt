package com.sesac.domain.local.usecase.monitor

import com.sesac.domain.local.repository.MonitorRepository
import javax.inject.Inject

data class MonitorUseCase(
    val getAllDummyLatLngUseCase: GetAllDummyLatLngUseCase,
    val getRandomDummyLatLngUseCase: GetRandomDummyLatLngUseCase,
)