package com.franksap2.finances.data.repository.info.api

import com.franksap2.finances.data.repository.info.model.StockInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface StockInfoApi {

    @GET("/v1/meta/symbols/{ticker}/company")
    suspend fun getTickerInfo(
        @Path("ticker")
        ticker: String
    ): StockInfoResponse

}