package com.sesac.domain.usecase.place

import com.sesac.domain.model.Place
import com.sesac.domain.repository.PlaceRepository
import com.sesac.domain.result.AuthResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPlaceUseCase @Inject constructor (
    private val repository: PlaceRepository
    ) {
        suspend operator fun invoke(
            categoryId: Int? = null,
            latitude: Double? = null,
            longitude: Double? = null,
            radius: Int? = null
        ): Flow<AuthResult<List<Place>>> {
            return repository.getPlaces(categoryId, latitude, longitude, radius)
        }

    }