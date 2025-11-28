package com.sesac.data.source.api

import com.sesac.data.dto.PlaceDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceApi {

    /**
     * 전체 장소 불러오기 (필터링)
     */
    @GET("places/")
    suspend fun getPlaces(
        @Query("category_id") categoryId: Int?,
        @Query("lat") latitude: Double?,
        @Query("lng") longitude: Double?,
        @Query("radius") radius: Int?
    ): List<PlaceDTO>



//    /**
//     * 카테고리별 장소 조회
//     */
//    @GET("places/")
//    suspend fun getPlacesByCategory(
//        @Query("category") categoryId: Int
//    ): List<PlaceDTO>
}