package com.sesac.android7hours.di

import com.sesac.domain.repository.PetRepository
import com.sesac.domain.usecase.pet.GetAllPetsUseCase
import com.sesac.domain.usecase.pet.GetUserPetsUseCase
import com.sesac.domain.usecase.pet.PetUseCase
import com.sesac.domain.usecase.pet.PostUserPetUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PetUseCaseModule {
    @Provides
    @Singleton
    fun providePetUseCase(repository: PetRepository): PetUseCase {
        return PetUseCase(
            getAllPetsUseCase = GetAllPetsUseCase(repository),
            getUserPetsUseCase = GetUserPetsUseCase(repository),
            postUserPetUseCase = PostUserPetUseCase(repository),
        )
    }
}