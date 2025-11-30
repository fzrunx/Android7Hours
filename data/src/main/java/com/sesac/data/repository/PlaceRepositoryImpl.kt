package com.sesac.data.repository

import android.util.Log
import com.sesac.data.mapper.PlaceMapper.toDomain
import com.sesac.data.source.api.PlaceApi
import com.sesac.domain.model.Place
import com.sesac.domain.repository.PlaceRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeApi: PlaceApi
) : PlaceRepository {

    override suspend fun getPlaces(
        categoryId: Int?,
        latitude: Double?,
        longitude: Double?,
        radius: Int?
    ): Flow<AuthResult<List<Place>>> = flow {
        emit(AuthResult.Loading)
        val response = placeApi.getPlaces(
            categoryId = categoryId,
            latitude = latitude,
            longitude = longitude,
            radius = radius
        )
        Log.d("PlaceRepository", "API Response Received: ${response.size} items")

        val domainModel = response.toDomain()
        Log.d("PlaceRepository", "Domain Model Mapped: ${domainModel.size} items")

        emit(AuthResult.Success(domainModel))
    }.catch {
        Log.e("PlaceRepository", "Error in getPlaces", it)
        emit(AuthResult.NetworkError(it))
    }



//    override suspend fun getPlacesByCategory(categoryId: Int): Flow<AuthResult<List<Place>>> = flow {
//        emit(AuthResult.Loading)
//        val response = placeApi.getPlacesByCategory(categoryId)
//        val domainModel = response.toDomain()
//        emit(AuthResult.Success(domainModel))
//    }.catch {
//        emit(AuthResult.NetworkError(it))
//    }
}