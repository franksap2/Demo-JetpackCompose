package com.franksap2.finances.data.repository.details.dominmodel

import com.google.gson.annotations.SerializedName

data class StockRecommendations(
    val buyRating: Float = 0f,
    val holdRating: Float = 0f,
    val sellRating: Float = 0f,
    val totalRatings: Int = 0
)

data class StockProfile(
    val companyName: String,
    val ticker : String,
    val logo: String,
    val web: String
)