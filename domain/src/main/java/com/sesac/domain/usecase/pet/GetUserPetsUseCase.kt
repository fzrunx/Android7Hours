package com.sesac.domain.usecase.pet

import com.sesac.domain.repository.PetRepository
import javax.inject.Inject

class GetUserPetsUseCase @Inject constructor(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke(token: String) = petRepository.getUserPets(token = token)
}