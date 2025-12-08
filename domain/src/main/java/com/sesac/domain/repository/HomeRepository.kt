package com.sesac.domain.repository

import com.sesac.domain.model.BannerData
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getAllBanners(): Flow<List<BannerData?>>
}