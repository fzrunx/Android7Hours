package com.sesac.data.source.api

import com.sesac.data.dto.BreedDTO
import com.sesac.data.dto.PetDTO
import com.sesac.data.dto.InvitationCodeResponseDTO
import com.sesac.data.dto.PetLocationRequestDTO
import com.sesac.data.dto.PetLocationResponseDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface PetsApi {
    @GET("pets/")
    suspend fun getUserPets(@Header("Authorization") token: String,): List<PetDTO>

    @POST("pets/")
    suspend fun postPet(
        @Header("Authorization") token: String,
        @Body pet: PetDTO
    ): Unit

    @GET("pets/{id}/")
    suspend fun getPetInfo(@Path("id") petId: Int): List<PetDTO>

    @PATCH("pets/{id}/")
    suspend fun updatePet(
        @Header("Authorization") token: String,
        @Path("id") petId: Int,
        @Body pet: PetDTO
    ): PetDTO

    @DELETE("pets/{id}/")
    suspend fun deletePet(
        @Header("Authorization") token: String,
        @Path("id") petId: Int,
    ): Unit

    @GET("pets/breeds/")
    suspend fun getBreeds(): List<BreedDTO>

    // New API for generating invitation code
    @POST("pets/invitations/")
    suspend fun postInvitationCode(
        @Header("Authorization") token: String
    ): InvitationCodeResponseDTO

    // New API for posting pet location
    @POST("pets/locations/")
    suspend fun postPetLocation(
        @Header("Authorization") token: String,
        @Body location: PetLocationRequestDTO
    ): PetLocationResponseDTO
}