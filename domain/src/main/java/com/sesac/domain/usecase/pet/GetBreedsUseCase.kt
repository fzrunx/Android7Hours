package com.sesac.domain.usecase.pet

import com.sesac.domain.repository.PetRepository
import javax.inject.Inject

class GetBreedsUseCase @Inject constructor(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke() = petRepository.getBreeds()
}
