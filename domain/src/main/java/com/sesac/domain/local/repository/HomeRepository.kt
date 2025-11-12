package com.sesac.domain.local.repository

import com.sesac.domain.local.model.BannerData
import com.sesac.domain.local.model.Community
import com.sesac.domain.local.model.DogCafe
import com.sesac.domain.local.model.TravelDestination
import com.sesac.domain.local.model.WalkPath
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getAllBanners(): Flow<List<BannerData?>>
    suspend fun getAllDogCafe(): Flow<List<DogCafe?>>
    suspend fun getAllTravelDestination(): Flow<List<TravelDestination?>>
    suspend fun getAllWalkPath(): Flow<List<WalkPath?>>
    suspend fun getAllCommunity(): Flow<List<Community?>>
}