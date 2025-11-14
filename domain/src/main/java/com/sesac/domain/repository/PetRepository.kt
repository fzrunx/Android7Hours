package com.sesac.domain.repository

import com.sesac.domain.model.Pet
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun getPets(): Flow<AuthResult<List<Pet>>>
    suspend fun getUserPets(userId: Int): Flow<AuthResult<List<Pet>>>
    suspend fun postUserPet(pet: Pet): Flow<AuthResult<Unit>>
}