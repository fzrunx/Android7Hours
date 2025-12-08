package com.sesac.domain.repository

import com.sesac.domain.model.Place
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface PlaceRepository  {

    suspend fun getPlaces(
        categoryId: Int?,
        latitude: Double?,
        longitude: Double?,
        radius: Int?
    ): Flow<AuthResult<List<Place>>>



//    suspend fun getPlacesByCategory(categoryId: Int): Flow<AuthResult<List<Place>>>
}