package com.sesac.domain.repository

import com.sesac.domain.model.Breed
import com.sesac.domain.model.Pet
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun getUserPets(token: String): Flow<AuthResult<List<Pet>>>
    suspend fun postUserPet(token: String, pet: Pet): Flow<AuthResult<Unit>>
    suspend fun getPetInfo(petId: Int): Flow<AuthResult<List<Pet>>>
    suspend fun updatePet(token: String, petId: Int, pet: Pet): Flow<AuthResult<Pet>>
    suspend fun deletePet(token: String, petId: Int): Flow<AuthResult<Unit>>
    suspend fun getBreeds(): Flow<AuthResult<List<Breed>>>
}