package com.sesac.domain.usecase.pet

import com.sesac.domain.model.Pet
import com.sesac.domain.repository.PetRepository
import javax.inject.Inject

class PostUserPetUseCase @Inject constructor(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke(token: String, pet: Pet) = petRepository.postUserPet(token, pet)
}