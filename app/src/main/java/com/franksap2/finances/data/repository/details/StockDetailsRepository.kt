package com.franksap2.finances.data.repository.details

import com.franksap2.finances.data.repository.details.dominmodel.StockProfile
import com.franksap2.finances.data.repository.details.dominmodel.StockRecommendations
import kotlinx.coroutines.flow.Flow

interface StockDetailsRepository {

    suspend fun getStockRecommendations(ticker: String): Flow<StockRecommendations>

    suspend fun getStockProfile(ticker: String): Flow<StockProfile>

}