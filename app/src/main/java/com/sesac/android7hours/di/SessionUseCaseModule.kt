package com.sesac.android7hours.di

import com.sesac.domain.repository.SessionRepository
import com.sesac.domain.usecase.session.ClearSessionUseCase
import com.sesac.domain.usecase.session.GetAccessTokenUseCase
import com.sesac.domain.usecase.session.GetRefreshTokenUseCase
import com.sesac.domain.usecase.session.GetUserInfoUseCase
import com.sesac.domain.usecase.session.SaveSessionUseCase
import com.sesac.domain.usecase.session.SaveUserUseCase
import com.sesac.domain.usecase.session.SessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object SessionUseCaseModule {

    @Provides
    @ActivityRetainedScoped
    fun provideSessionUseCase(repository: SessionRepository): SessionUseCase {
        return SessionUseCase(
            getAccessToken = GetAccessTokenUseCase(repository),
            getRefreshToken = GetRefreshTokenUseCase(repository),
            getUserInfo = GetUserInfoUseCase(repository),
            saveSession = SaveSessionUseCase(repository),
            saveUser = SaveUserUseCase(repository),
            clearSession = ClearSessionUseCase(repository)
        )
    }
}
