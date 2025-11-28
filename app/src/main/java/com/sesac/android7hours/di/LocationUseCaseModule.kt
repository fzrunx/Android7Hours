package com.sesac.android7hours.di

import com.sesac.domain.repository.LocationRepository
import com.sesac.domain.usecase.location.GetCurrentLocationUseCase
import com.sesac.domain.usecase.location.LocationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object LocationUseCaseModule {
    @Provides
    @Singleton
    fun provideLocationUseCaseModule(
        repository: LocationRepository,
        @ApplicationScope externalScope: CoroutineScope,
    ): LocationUseCase {
        return LocationUseCase(
            getCurrentLocationUseCase = GetCurrentLocationUseCase(repository, externalScope),
        )
    }
}