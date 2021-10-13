package com.franksap2.finances.data.repository.details.api

import com.franksap2.finances.data.repository.details.model.ProfileResponse
import com.franksap2.finances.data.repository.details.model.RecommendationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendationApi {

    @GET("/api/v1/stock/recommendation")
    suspend fun getRecommendation(
        @Query("symbol")
        ticker: String
    ): List<RecommendationResponse>

    @GET("/api/v1/stock/profile2")
    suspend fun getProfile(
        @Query("symbol")
        ticker: String
    ): ProfileResponse
}