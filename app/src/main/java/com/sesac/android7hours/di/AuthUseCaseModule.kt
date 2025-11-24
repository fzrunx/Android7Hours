package com.sesac.android7hours.di

import com.sesac.domain.repository.AuthRepository
import com.sesac.domain.usecase.auth.AuthUseCase
import com.sesac.domain.usecase.auth.LoginUseCase
import com.sesac.domain.usecase.auth.LoginWithKakaoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object AuthUseCaseModule {
    @Provides
    @ActivityRetainedScoped
    fun provideAuthUseCase(repository: AuthRepository): AuthUseCase {
        return AuthUseCase(
            login = LoginUseCase(repository),
            loginWithKakao = LoginWithKakaoUseCase(repository)
        )
    }

}
