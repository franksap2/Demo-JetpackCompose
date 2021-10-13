package com.franksap2.finances.data.repository.ticker.model

import com.google.gson.annotations.SerializedName

data class TickerResponse(
    @SerializedName("c")
    val currentPrice : Float,
    @SerializedName("h")
    val highestPrice : Float,
    @SerializedName("l")
    val lowestPrice : Float,
    @SerializedName("o")
    val openPrice : Float,
    @SerializedName("pc")
    val previousClosePrice : Float,
    @SerializedName("t")
    val timeStamp : Long
)