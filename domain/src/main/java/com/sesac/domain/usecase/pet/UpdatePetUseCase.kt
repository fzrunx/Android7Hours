package com.sesac.domain.usecase.pet

import com.sesac.domain.model.Pet
import com.sesac.domain.repository.PetRepository
import javax.inject.Inject

class UpdatePetUseCase @Inject constructor(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke(token: String, petId: Int, pet: Pet) = petRepository.updatePet(token, petId, pet)
}