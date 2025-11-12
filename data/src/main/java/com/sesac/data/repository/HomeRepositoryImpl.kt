package com.sesac.data.repository

import com.sesac.data.source.api.AuthApi
import com.sesac.data.source.local.datasource.MockCommunity
import com.sesac.data.source.local.datasource.MockHome
import com.sesac.domain.local.model.BannerData
import com.sesac.domain.local.model.Community
import com.sesac.domain.local.model.DogCafe
import com.sesac.domain.local.model.TravelDestination
import com.sesac.domain.local.model.WalkPath
import com.sesac.domain.local.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
): HomeRepository {
    override suspend fun getAllBanners(): Flow<List<BannerData?>> = flow {
        emit(MockHome.bannerDataList)
    }

    override suspend fun getAllDogCafe(): Flow<List<DogCafe?>> = flow {
        emit(MockHome.dogCafeList)
    }

    override suspend fun getAllWalkPath(): Flow<List<WalkPath?>> = flow {
        emit(MockHome.walkPathList)
    }

    override suspend fun getAllTravelDestination(): Flow<List<TravelDestination?>> = flow {
        emit(MockHome.travelDestinationList)
    }

    override suspend fun getAllCommunity(): Flow<List<Community?>> = flow {
        emit(MockCommunity.postList)
    }
}

