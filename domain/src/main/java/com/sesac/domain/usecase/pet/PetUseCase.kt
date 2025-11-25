package com.sesac.domain.usecase.pet

data class PetUseCase(
    val getUserPetsUseCase: GetUserPetsUseCase,
    val postUserPetUseCase: PostUserPetUseCase,
    val getPetInfoUseCase: GetPetInfoUseCase,
    val updatePetUseCase: UpdatePetUseCase,
    val deletePetUseCase: DeletePetUseCase,
    val getBreedsUseCase: GetBreedsUseCase,
)