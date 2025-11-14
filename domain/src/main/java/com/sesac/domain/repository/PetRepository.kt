package com.sesac.domain.repository

import com.sesac.domain.model.Breed
import com.sesac.domain.model.Pet
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun getUserPets(token: String): Flow<AuthResult<List<Pet>>>
    suspend fun getPetInfo(petId: Int): Flow<AuthResult<List<Pet>>>
    suspend fun postUserPet(token: String, pet: Pet): Flow<AuthResult<Unit>>
    suspend fun getBreeds(): Flow<AuthResult<List<Breed>>>
}