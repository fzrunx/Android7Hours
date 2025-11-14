package com.sesac.android7hours.di

import com.sesac.domain.repository.MonitorRepository
import com.sesac.domain.usecase.monitor.GetAllDummyLatLngUseCase
import com.sesac.domain.usecase.monitor.GetRandomDummyLatLngUseCase
import com.sesac.domain.usecase.monitor.MonitorUseCase
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