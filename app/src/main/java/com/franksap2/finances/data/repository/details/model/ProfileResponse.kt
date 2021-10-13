package com.franksap2.finances.data.repository.details.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("weburl")
    val web: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("ticker")
    val ticker: String
)