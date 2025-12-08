package com.sesac.android7hours.di

import com.sesac.domain.repository.PetRepository
import com.sesac.domain.usecase.pet.DeletePetUseCase
import com.sesac.domain.usecase.pet.GetUserPetsUseCase
import com.sesac.domain.usecase.pet.GetBreedsUseCase
import com.sesac.domain.usecase.pet.GetPetInfoUseCase
import com.sesac.domain.usecase.pet.PetUseCase
import com.sesac.domain.usecase.pet.PostUserPetUseCase
import com.sesac.domain.usecase.pet.UpdatePetUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object PetUseCaseModule {
    @Provides
    @ActivityRetainedScoped
    fun providePetUseCase(repository: PetRepository): PetUseCase {
        return PetUseCase(
            getUserPetsUseCase = GetUserPetsUseCase(repository),
            postUserPetUseCase = PostUserPetUseCase(repository),
            getPetInfoUseCase = GetPetInfoUseCase(repository),
            updatePetUseCase = UpdatePetUseCase(repository),
            deletePetUseCase = DeletePetUseCase(repository),
            getBreedsUseCase = GetBreedsUseCase(repository),
        )
    }
}