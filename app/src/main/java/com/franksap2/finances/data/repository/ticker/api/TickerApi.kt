package com.franksap2.finances.data.repository.ticker.api

import com.franksap2.finances.data.repository.ticker.model.ChartResponse
import com.franksap2.finances.data.repository.ticker.model.TickerResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TickerApi {

    @GET("/api/v1/quote")
    suspend fun getTickerPrice(
        @Query("symbol")
        ticker: String
    ): TickerResponse

    @GET("/api/v1/stock/candle")
    suspend fun getChartData(
        @Query("from")
        from: Long,
        @Query("to")
        to: Long,
        @Query("resolution")
        resolution: String,
        @Query("symbol")
        ticker: String
    ): ChartResponse


}