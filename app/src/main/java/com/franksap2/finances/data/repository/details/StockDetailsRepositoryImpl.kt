package com.franksap2.finances.data.repository.details

import android.util.Log
import androidx.compose.ui.util.fastForEach
import com.franksap2.finances.data.client.RetrofitClientGenerator
import com.franksap2.finances.data.repository.details.api.RecommendationApi
import com.franksap2.finances.data.repository.details.dominmodel.StockProfile
import com.franksap2.finances.data.repository.details.dominmodel.StockRecommendations
import com.franksap2.finances.data.repository.ticker.DateResolver
import com.franksap2.finances.di.qualifiers.FinnhubClient
import com.franksap2.finances.di.qualifiers.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StockDetailsRepositoryImpl @Inject constructor(
    @param:IODispatcher private val dispatcher: CoroutineDispatcher,
    @param:FinnhubClient private val generator: RetrofitClientGenerator
) : StockDetailsRepository {

    private val api by lazy { generator.getApi(RecommendationApi::class.java) }

    override suspend fun getStockRecommendations(ticker: String) = flow {

        try {
            val result = api.getRecommendation(ticker)

            var averageBuyRating = 0f
            var averageHoldRating = 0f
            var averageSellRating = 0f

            result.fastForEach {
                averageBuyRating += it.buyRating + it.strongBuy
                averageHoldRating += it.holdRating + it.strongSell
                averageSellRating += it.sellRating
            }

            val totalRatings = (averageBuyRating +
                    averageHoldRating +
                    averageSellRating).takeIf { it > 0f } ?: 1f

            averageBuyRating /= totalRatings
            averageHoldRating /= totalRatings
            averageSellRating /= totalRatings

            emit(
                StockRecommendations(
                    averageBuyRating,
                    averageHoldRating,
                    averageSellRating,
                    result.size
                )
            )
        } catch (e: Exception) {
            Log.e("", "error", e)
        }

    }.flowOn(dispatcher)

    override suspend fun getStockProfile(ticker: String) = flow {
        try {
            val result = api.getProfile(ticker)

            emit(
                StockProfile(
                    result.name,
                    result.ticker,
                    result.logo,
                    result.web
                )
            )

        } catch (e: Exception) {
            Log.e("", "error", e)
        }
    }.flowOn(dispatcher)
}