package com.sesac.domain.usecase.pet

import com.sesac.domain.repository.PetRepository
import javax.inject.Inject

class DeletePetUseCase @Inject constructor(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke(token: String, petId: Int) = petRepository.deletePet(token, petId)
}