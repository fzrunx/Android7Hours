package com.sesac.data.repository

import android.util.Log
import com.sesac.data.mapper.toBreedsList
import com.sesac.data.mapper.toPet
import com.sesac.data.mapper.toPetDTO
import com.sesac.data.mapper.toPetList
import com.sesac.data.source.api.PetsApi
import com.sesac.domain.model.Breed
import com.sesac.domain.model.Pet
import com.sesac.domain.repository.PetRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petsApi: PetsApi,
): PetRepository {
    override suspend fun getUserPets(token: String): Flow<AuthResult<List<Pet>>> = flow {
        emit(AuthResult.Loading)
        val result = petsApi.getUserPets("Bearer $token").toPetList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-PetRepositoryImpl", "getPets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun postUserPet(token: String, pet: Pet): Flow<AuthResult<Unit>> = flow {
        emit(AuthResult.Loading)
        val result = petsApi.postPet("Bearer $token", pet.toPetDTO())
        Log.d("TAG-PetRepositoryImpl", "result : $result")
        emit(AuthResult.Success(Unit))
    }.catch {
        Log.d("TAG-PetRepositoryImpl", "postUserPets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun getPetInfo(petId: Int): Flow<AuthResult<List<Pet>>> = flow {
        emit(AuthResult.Loading)
        val result = petsApi.getPetInfo(petId).toPetList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-PetRepositoryImpl", "getUserPets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun updatePet(token: String, petId: Int, pet: Pet): Flow<AuthResult<Pet>> = flow {
        emit(AuthResult.Loading)
        val result = petsApi.updatePet("Bearer $token", petId, pet.toPetDTO()).toPet()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-PetRepostioryImpl", "updatePets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun deletePet(token: String, petId: Int): Flow<AuthResult<Unit>> = flow {
        emit(AuthResult.Loading)
        petsApi.deletePet("Bearer $token", petId)
        emit(AuthResult.Success(Unit))
    }.catch {
        Log.d("TAG-PetRepostioryImpl", "deletePets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun getBreeds(): Flow<AuthResult<List<Breed>>> = flow {
        emit(AuthResult.Loading)
        val result = petsApi.getBreeds().toBreedsList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-PetRepostioryImpl", "getBreeds(): error: $it")
        emit(AuthResult.NetworkError(it))
    }
}