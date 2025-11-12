package com.sesac.domain.local.usecase.home

data class HomeUseCase(
    val getAllBannersUseCase: GetAllBannersUseCase,
    val getAllDogCafeUseCase: GetAllDogCafeUseCase,
    val getAllTravelDestinationUseCase: GetAllTravelDestinationUseCase,
    val getAllWalkPathUseCase: GetAllWalkPathUseCase,
)