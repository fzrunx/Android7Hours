package com.sesac.data.repository

import android.util.Log
import com.sesac.data.mapper.toBreedsList
import com.sesac.data.mapper.toPet
import com.sesac.data.mapper.toPetList
import com.sesac.data.source.api.PetsApi
import com.sesac.domain.model.Breed
import com.sesac.domain.model.Pet
import com.sesac.domain.repository.PetRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petsApi: PetsApi
): PetRepository {
    override suspend fun getUserPets(token: String): Flow<AuthResult<List<Pet>>> = flow {
        emit(AuthResult.Loading)
        val result = petsApi.getUserPets("Bearer $token").toPetList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.e("TAG-PetRepositoryImpl", "getPets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun postUserPet(token: String, image: MultipartBody.Part?, pet: Pet): Flow<AuthResult<Unit>> = flow {
        emit(AuthResult.Loading)

        val params = mutableMapOf<String, RequestBody>()
        params["name"] = pet.name.toRequestBody("text/plain".toMediaTypeOrNull())
        pet.gender?.let { params["gender"] = it.toRequestBody("text/plain".toMediaTypeOrNull()) }
        pet.birthday?.let { params["birthday"] = it.toRequestBody("text/plain".toMediaTypeOrNull()) }
        params["neutering"] = pet.neutering.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        pet.breed?.let { params["breed"] = it.toRequestBody("text/plain".toMediaTypeOrNull()) }

        val result = petsApi.postPet("Bearer $token", image, params)
        Log.d("TAG-PetRepositoryImpl", "result : $result")
        emit(AuthResult.Success(Unit))
    }.catch {
        Log.e("TAG-PetRepositoryImpl", "postUserPets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun getPetInfo(petId: Int): Flow<AuthResult<Pet>> = flow {
        emit(AuthResult.Loading)
        val result = petsApi.getPetInfo(petId).toPet()
        emit(AuthResult.Success(result))
    }.catch {
        Log.e("TAG-PetRepositoryImpl", "getPetInfo(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun updatePet(token: String, petId: Int, image: MultipartBody.Part?, pet: Pet): Flow<AuthResult<Pet>> = flow {
        emit(AuthResult.Loading)

        val params = mutableMapOf<String, RequestBody>()
        params["name"] = pet.name.toRequestBody("text/plain".toMediaTypeOrNull())
        pet.gender?.let { params["gender"] = it.toRequestBody("text/plain".toMediaTypeOrNull()) }
        pet.birthday?.let { params["birthday"] = it.toRequestBody("text/plain".toMediaTypeOrNull()) }
        params["neutering"] = pet.neutering.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        pet.breed?.let { params["breed"] = it.toRequestBody("text/plain".toMediaTypeOrNull()) }

        val result = petsApi.updatePet("Bearer $token", petId, image, params).toPet()
        emit(AuthResult.Success(result))
    }.catch {
        Log.e("TAG-PetRepostioryImpl", "updatePets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun deletePet(token: String, petId: Int): Flow<AuthResult<Unit>> = flow {
        emit(AuthResult.Loading)
        petsApi.deletePet("Bearer $token", petId)
        emit(AuthResult.Success(Unit))
    }.catch {
        Log.e("TAG-PetRepostioryImpl", "deletePets(): error: $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun getBreeds(): Flow<AuthResult<List<Breed>>> = flow {
        emit(AuthResult.Loading)
        val result = petsApi.getBreeds().toBreedsList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.e("TAG-PetRepostioryImpl", "getBreeds(): error: $it")
        emit(AuthResult.NetworkError(it))
    }
}