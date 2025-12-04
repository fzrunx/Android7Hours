package com.sesac.data.source.api

import com.sesac.data.dto.BreedDTO
import com.sesac.data.dto.PetDTO
import com.sesac.data.dto.InvitationCodeResponseDTO
import com.sesac.data.dto.PetLocationRequestDTO
import com.sesac.data.dto.PetLocationResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody // Added import for RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PetsApi {
    @GET("pets/")
    suspend fun getUserPets(@Header("Authorization") token: String,): List<PetDTO>

    @Multipart
    @POST("pets/")
    suspend fun postPet(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part?,
        @Part("pet") pet: RequestBody // Modified to send PetDTO as RequestBody
    ): Unit

    @GET("pets/{id}/")
    suspend fun getPetInfo(@Path("id") petId: Int): PetDTO

    @Multipart
    @PATCH("pets/{id}/")
    suspend fun updatePet(
        @Header("Authorization") token: String,
        @Path("id") petId: Int,
        @Part image: MultipartBody.Part?,
        @Part("pet") pet: RequestBody // Modified to send PetDTO as RequestBody
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
