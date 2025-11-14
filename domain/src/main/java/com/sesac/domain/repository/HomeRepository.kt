package com.sesac.domain.repository

import com.sesac.domain.model.BannerData
import com.sesac.domain.model.Community
import com.sesac.domain.model.DogCafe
import com.sesac.domain.model.TravelDestination
import com.sesac.domain.model.WalkPath
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getAllBanners(): Flow<List<BannerData?>>
    suspend fun getAllDogCafe(): Flow<List<DogCafe?>>
    suspend fun getAllTravelDestination(): Flow<List<TravelDestination?>>
    suspend fun getAllWalkPath(): Flow<List<WalkPath?>>
    suspend fun getAllCommunity(): Flow<List<Community?>>
}