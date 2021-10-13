package com.franksap2.finances.data.repository.ticker

import com.franksap2.finances.data.repository.ticker.domainmodel.StockChartData
import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector
import kotlinx.coroutines.flow.Flow

interface TickerRepository {

    suspend fun getStockChartData(ticker: String, timeSelector: TimeSelector): Flow<StockChartData>

}