package com.sesac.data.source.api

import androidx.room.Query
import com.sesac.data.dto.PetDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PetsApi {
    @GET("pets/")
    suspend fun getPets(): List<PetDTO>

    @GET("pets/{id}")
    suspend fun getUserPets(@Path("id") userId: Int): List<PetDTO>

    @POST("pets/")
    suspend fun postPet(@Body pet: PetDTO): Unit
}