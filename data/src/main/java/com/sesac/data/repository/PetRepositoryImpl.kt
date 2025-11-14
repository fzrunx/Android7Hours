package com.sesac.data.repository

import android.util.Log
import com.sesac.data.mapper.toPetDTO
import com.sesac.data.mapper.toPetList
import com.sesac.data.source.api.PetsApi
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
    override suspend fun getPets(): Flow<AuthResult<List<Pet>>> = flow {
        emit(AuthResult.Loading)
        val result = petsApi.getPets().toPetList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-PetRepostioryImpl", "getPets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun getUserPets(userId: Int): Flow<AuthResult<List<Pet>>> =flow {
        emit(AuthResult.Loading)
        val result = petsApi.getUserPets(userId).toPetList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-PetRepostioryImpl", "getUserPets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun postUserPet(pet: Pet): Flow<AuthResult<Unit>> =flow {
        emit(AuthResult.Loading)
        petsApi.postPet(pet.toPetDTO())
        emit(AuthResult.Success(Unit))
    }.catch {
        Log.d("TAG-PetRepostioryImpl", "postUserPets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }
}