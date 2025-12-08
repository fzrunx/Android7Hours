package com.sesac.domain.usecase.home

import com.sesac.domain.repository.HomeRepository
import javax.inject.Inject

class GetAllBannersUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() = homeRepository.getAllBanners()
}
