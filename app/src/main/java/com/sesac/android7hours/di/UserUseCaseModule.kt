package com.sesac.android7hours.di

import com.sesac.domain.repository.UserRepository
import com.sesac.domain.usecase.user.DeleteUserUseCase
import com.sesac.domain.usecase.user.GetUsersUseCase
import com.sesac.domain.usecase.user.PostUserUseCase
import com.sesac.domain.usecase.user.UpdateProfileUseCase
import com.sesac.domain.usecase.user.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object UserUseCaseModule {
    @Provides
    @ActivityRetainedScoped
    fun provideUserUseCase(repository: UserRepository): UserUseCase {
        return UserUseCase(
            getAllUsers = GetUsersUseCase(repository),
            postUser = PostUserUseCase(repository),
            deleteUserUseCase = DeleteUserUseCase(repository),
            updateProfile = UpdateProfileUseCase(repository),
        )
    }

}
