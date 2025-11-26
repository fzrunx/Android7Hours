package com.sesac.data.source.api

import com.sesac.data.dto.BreedDTO
import com.sesac.data.dto.PetDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PetsApi {
    @GET("pets/")
    suspend fun getUserPets(@Header("Authorization") token: String,): List<PetDTO>

    @GET("pets/{id}")
    suspend fun getPetInfo(@Path("id") petId: Int): List<PetDTO>

    @POST("pets/")
    suspend fun postPet(
        @Header("Authorization") token: String,
        @Body pet: PetDTO
    ): Unit

    @GET("pets/breeds")
    suspend fun getBreeds(): List<BreedDTO>
}