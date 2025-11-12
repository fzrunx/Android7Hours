package com.sesac.android7hours.di

import com.sesac.domain.remote.repository.SessionRepository
import com.sesac.domain.remote.usecase.session.ClearSessionUseCase
import com.sesac.domain.remote.usecase.session.GetAccessTokenUseCase
import com.sesac.domain.remote.usecase.session.GetRefreshTokenUseCase
import com.sesac.domain.remote.usecase.session.GetUserInfoUseCase
import com.sesac.domain.remote.usecase.session.SaveSessionUseCase
import com.sesac.domain.remote.usecase.session.SessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionUseCaseModule {

    @Provides
    @Singleton
    fun provideSessionUseCase(repository: SessionRepository): SessionUseCase {
        return SessionUseCase(
            getAccessToken = GetAccessTokenUseCase(repository),
            getRefreshToken = GetRefreshTokenUseCase(repository),
            getUserInfo = GetUserInfoUseCase(repository),
            saveSession = SaveSessionUseCase(repository),
            clearSession = ClearSessionUseCase(repository)
        )
    }
}
