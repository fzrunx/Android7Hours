package com.sesac.domain.repository

import com.sesac.domain.model.Breed
import com.sesac.domain.model.Pet
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface PetRepository {
    suspend fun getUserPets(token: String): Flow<AuthResult<List<Pet>>>
    suspend fun postUserPet(token: String, image: MultipartBody.Part?, pet: Pet): Flow<AuthResult<Unit>>
    suspend fun getPetInfo(petId: Int): Flow<AuthResult<Pet>>
    suspend fun updatePet(token: String, petId: Int, image: MultipartBody.Part?, pet: Pet): Flow<AuthResult<Pet>>
    suspend fun deletePet(token: String, petId: Int): Flow<AuthResult<Unit>>
    suspend fun getBreeds(): Flow<AuthResult<List<Breed>>>
}