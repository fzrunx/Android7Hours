package com.sesac.android7hours.di

import com.sesac.domain.repository.MonitorRepository
import com.sesac.domain.usecase.monitor.GetAllDummyLatLngUseCase
import com.sesac.domain.usecase.monitor.GetRandomDummyLatLngUseCase
import com.sesac.domain.usecase.monitor.MonitorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object MonitorUseCaseModule {
    @Provides
    @ActivityRetainedScoped
    fun provideMonitorUseCase(repository: MonitorRepository): MonitorUseCase {
        return MonitorUseCase(
            getAllDummyLatLngUseCase = GetAllDummyLatLngUseCase(repository),
            getRandomDummyLatLngUseCase = GetRandomDummyLatLngUseCase(repository),
        )
    }
}