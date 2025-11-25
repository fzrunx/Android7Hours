package com.sesac.data.source.remote.api

import retrofit2.http.GET

interface CommunityApi {
    @GET("places/")
    suspend fun getPlaces()
}