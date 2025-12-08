package com.sesac.domain.usecase.pet

import com.sesac.domain.model.Pet
import com.sesac.domain.repository.PetRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UpdatePetUseCase @Inject constructor(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke(token: String, petId: Int, image: MultipartBody.Part?, pet: Pet) = petRepository.updatePet(token, petId, image, pet)
}