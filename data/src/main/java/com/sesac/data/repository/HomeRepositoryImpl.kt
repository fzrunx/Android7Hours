package com.sesac.data.repository

import com.sesac.data.source.api.AuthApi
import com.sesac.data.source.local.datasource.MockHome
import com.sesac.domain.model.BannerData
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
}

