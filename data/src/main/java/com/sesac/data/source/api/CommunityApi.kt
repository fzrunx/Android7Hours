package com.sesac.data.source.api

import retrofit2.http.GET

interface CommunityApi {
    @GET("places/")
    suspend fun getPlaces()
}