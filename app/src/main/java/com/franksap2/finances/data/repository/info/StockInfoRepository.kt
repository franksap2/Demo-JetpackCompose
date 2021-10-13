package com.franksap2.finances.data.repository.info

import com.franksap2.finances.data.repository.ticker.domainmodel.StockChartData
import com.franksap2.finances.data.repository.info.domainmodel.StockInfo
import kotlinx.coroutines.flow.Flow

interface StockInfoRepository {

    fun getStockInfo(ticker: String): Flow<StockInfo>

}