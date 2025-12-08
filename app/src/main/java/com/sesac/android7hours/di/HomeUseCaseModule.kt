package com.sesac.android7hours.di

import com.sesac.domain.repository.HomeRepository
import com.sesac.domain.usecase.home.GetAllBannersUseCase
import com.sesac.domain.usecase.home.HomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object HomeUseCaseModule {
    @Provides
    @ActivityRetainedScoped
    fun provideHomeUseCase(repository: HomeRepository): HomeUseCase {
        return HomeUseCase(
            getAllBannersUseCase = GetAllBannersUseCase(repository),
        )
    }
}