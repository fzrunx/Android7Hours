package com.sesac.domain.usecase.location

import com.sesac.domain.model.Coord
import com.sesac.domain.repository.LocationRepository
import com.sesac.domain.result.LocationFlowResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val externalScope: CoroutineScope
) {
    operator fun invoke(): SharedFlow<LocationFlowResult<Coord>> = locationRepository
        .getCurrentCoord()
        .shareIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )
}