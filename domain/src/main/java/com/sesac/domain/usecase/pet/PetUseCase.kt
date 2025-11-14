package com.sesac.domain.usecase.pet

data class PetUseCase(
    val getAllPetsUseCase: GetAllPetsUseCase,
    val getUserPetsUseCase: GetUserPetsUseCase,
    val postUserPetUseCase: PostUserPetUseCase,
)