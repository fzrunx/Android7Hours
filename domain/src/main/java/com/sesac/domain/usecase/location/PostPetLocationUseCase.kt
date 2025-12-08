package com.sesac.domain.usecase.location

import com.sesac.domain.model.PetLocation
import com.sesac.domain.repository.LocationRepository
import javax.inject.Inject

class PostPetLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(token: String, location: PetLocation) = locationRepository.postPetLocation(token, location)
}