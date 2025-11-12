package com.sesac.domain.local.usecase.home

import com.sesac.domain.local.repository.HomeRepository
import javax.inject.Inject

class GetAllBannersUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() = homeRepository.getAllBanners()
}
