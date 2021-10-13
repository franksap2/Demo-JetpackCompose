package com.franksap2.finances.data.repository.info.domainmodel

import com.google.gson.annotations.SerializedName

data class StockInfo (
    val ceo: String,
    val address: String,
    val description: String
)