package com.sesac.android7hours.di

import com.sesac.domain.local.repository.MonitorRepository
import com.sesac.domain.local.usecase.monitor.GetAllDummyLatLngUseCase
import com.sesac.domain.local.usecase.monitor.GetRandomDummyLatLngUseCase
import com.sesac.domain.local.usecase.monitor.MonitorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MonitorUseCaseModule {
    @Provides
    @Singleton
    fun provideMonitorUseCase(repository: MonitorRepository): MonitorUseCase {
        return MonitorUseCase(
            getAllDummyLatLngUseCase = GetAllDummyLatLngUseCase(repository),
            getRandomDummyLatLngUseCase = GetRandomDummyLatLngUseCase(repository),
        )
    }
}