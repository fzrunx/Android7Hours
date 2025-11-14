package com.sesac.domain.usecase.monitor

import com.sesac.domain.repository.MonitorRepository
import javax.inject.Inject

class GetRandomDummyLatLngUseCase @Inject constructor(
    private val monitorRepository: MonitorRepository
) {
    suspend operator fun invoke() = monitorRepository.getRandomDummyLatLng()
}