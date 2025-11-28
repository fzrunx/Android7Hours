package com.sesac.domain.usecase.location

data class LocationUseCase(
    val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    val postPetLocationUseCase: PostPetLocationUseCase,
)