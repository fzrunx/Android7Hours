package com.sesac.domain.usecase

import com.sesac.domain.repository.HomeRepository
import javax.inject.Inject


class GetAllBannersUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() = homeRepository.getAllBanners()
}

class GetAllDogCafeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() = homeRepository.getAllDogCafe()
}

class GetAllTravelDestinationUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() = homeRepository.getAllTravelDestination()
}

class GetAllWalkPathUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() = homeRepository.getAllWalkPath()
}