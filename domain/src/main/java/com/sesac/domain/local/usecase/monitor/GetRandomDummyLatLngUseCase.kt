package com.sesac.domain.local.usecase.monitor

import com.sesac.domain.local.repository.MonitorRepository
import javax.inject.Inject

class GetRandomDummyLatLngUseCase @Inject constructor(
    private val monitorRepository: MonitorRepository
) {
    suspend operator fun invoke() = monitorRepository.getRandomDummyLatLng()
}