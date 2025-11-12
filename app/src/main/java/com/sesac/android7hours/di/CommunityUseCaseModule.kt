package com.sesac.android7hours.di

import com.sesac.domain.local.repository.CommunityRepository
import com.sesac.domain.local.usecase.community.CommunityUseCase
import com.sesac.domain.local.usecase.community.GetAllCommunityUseCase
import com.sesac.domain.local.usecase.community.GetCommunityDetailUseCase
import com.sesac.domain.local.usecase.community.GetCommunitySearchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommunityUseCaseModule {
    @Provides
    @Singleton
    fun provideCommunityUseCase(repository: CommunityRepository): CommunityUseCase {
        return CommunityUseCase(
            getAllCommunityUseCase = GetAllCommunityUseCase(repository),
            getCommunityDetailUseCase = GetCommunityDetailUseCase(repository),
            getCommunitySearchUseCase = GetCommunitySearchUseCase(repository),
        )
    }
}