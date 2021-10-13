package com.franksap2.finances.data.repository.info.model

import com.google.gson.annotations.SerializedName

data class StockInfoResponse(
    @SerializedName("ceo")
    val ceo: String,
    @SerializedName("hq_address")
    val address: String,
    @SerializedName("description")
    val description: String
)