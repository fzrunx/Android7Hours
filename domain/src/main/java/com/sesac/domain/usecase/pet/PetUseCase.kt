package com.sesac.domain.usecase.pet

data class PetUseCase(
    val getUserPetsUseCase: GetUserPetsUseCase,
    val getPetInfoUseCase: GetPetInfoUseCase,
    val postUserPetUseCase: PostUserPetUseCase,
    val getBreedsUseCase: GetBreedsUseCase,
)