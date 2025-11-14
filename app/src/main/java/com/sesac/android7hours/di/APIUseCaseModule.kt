package com.sesac.android7hours.di

import com.sesac.domain.remote.repository.AuthRepository
import com.sesac.domain.remote.usecase.auth.AuthUseCase
import com.sesac.domain.remote.usecase.auth.DeleteUserUseCase
import com.sesac.domain.remote.usecase.auth.GetAllUsersUseCase
import com.sesac.domain.remote.usecase.auth.LoginUseCase
import com.sesac.domain.remote.usecase.auth.PostUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIUseCaseModule {
    @Provides
    @Singleton
    fun provideAuthUseCase(repository: AuthRepository): AuthUseCase {
        return AuthUseCase(
            getAllUsers = GetAllUsersUseCase(repository),
            postUser = PostUserUseCase(repository),
            deleteUserUseCase = DeleteUserUseCase(repository),
            login = LoginUseCase(repository),
        )
    }

}
