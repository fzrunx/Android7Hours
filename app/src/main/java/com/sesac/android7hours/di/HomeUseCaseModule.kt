package com.sesac.android7hours.di

import com.sesac.domain.local.repository.HomeRepository
import com.sesac.domain.local.usecase.home.GetAllBannersUseCase
import com.sesac.domain.local.usecase.home.GetAllDogCafeUseCase
import com.sesac.domain.local.usecase.home.GetAllTravelDestinationUseCase
import com.sesac.domain.local.usecase.home.GetAllWalkPathUseCase
import com.sesac.domain.local.usecase.home.HomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeUseCaseModule {
    @Provides
    @Singleton
    fun provideHomeUseCase(repository: HomeRepository): HomeUseCase {
        return HomeUseCase(
            getAllBannersUseCase = GetAllBannersUseCase(repository),
            getAllDogCafeUseCase = GetAllDogCafeUseCase(repository),
            getAllTravelDestinationUseCase = GetAllTravelDestinationUseCase(repository),
            getAllWalkPathUseCase = GetAllWalkPathUseCase(repository),
        )
    }
}