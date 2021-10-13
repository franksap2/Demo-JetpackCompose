package com.franksap2.finances.data.repository.ticker.model

import com.google.gson.annotations.SerializedName

data class ChartResponse(
    @SerializedName("c")
    val closedPrice : List<Float>?,
    @SerializedName("h")
    val highestPrice : List<Float>,
    @SerializedName("l")
    val lowestPrice : List<Float>,
    @SerializedName("o")
    val openPrice : List<Float>,
    @SerializedName("t")
    val timeStamp : List<Long>
)