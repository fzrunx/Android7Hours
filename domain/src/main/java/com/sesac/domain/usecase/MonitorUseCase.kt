package com.sesac.domain.usecase

import com.sesac.domain.repository.MonitorRepository
import javax.inject.Inject

class GetAllDummyLatLngUseCase @Inject constructor(
    private val monitorRepository: MonitorRepository
) {
    suspend operator fun invoke() = monitorRepository.getAllDummyLatLng()
}

class GetRandomDummyLatLngUseCase @Inject constructor(
    private val monitorRepository: MonitorRepository
) {
    suspend operator fun invoke() = monitorRepository.getRandomDummyLatLng()
}