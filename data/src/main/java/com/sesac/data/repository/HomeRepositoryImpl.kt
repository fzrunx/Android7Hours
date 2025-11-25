package com.sesac.data.repository

import com.sesac.data.source.remote.api.AuthApi
import com.sesac.data.source.local.datasource.MockCommunity
import com.sesac.data.source.local.datasource.MockHome
import com.sesac.domain.model.BannerData
import com.sesac.domain.model.Community
import com.sesac.domain.model.DogCafe
import com.sesac.domain.model.TravelDestination
import com.sesac.domain.model.WalkPath
import com.sesac.domain.repository.HomeRepository
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

