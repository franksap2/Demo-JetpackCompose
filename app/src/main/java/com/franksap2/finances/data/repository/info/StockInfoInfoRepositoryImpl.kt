package com.franksap2.finances.data.repository.info

import android.util.Log
import com.franksap2.finances.data.client.RetrofitClientGenerator
import com.franksap2.finances.data.repository.info.api.StockInfoApi
import com.franksap2.finances.data.repository.ticker.domainmodel.StockChartData
import com.franksap2.finances.data.repository.info.domainmodel.StockInfo
import com.franksap2.finances.di.qualifiers.IODispatcher
import com.franksap2.finances.di.qualifiers.PolygonClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StockInfoInfoRepositoryImpl @Inject constructor(
    @param:IODispatcher private val dispatcher: CoroutineDispatcher,
    @param:PolygonClient private val generator: RetrofitClientGenerator
) : StockInfoRepository {

    private val api by lazy { generator.getApi(StockInfoApi::class.java) }

    override fun getStockInfo(ticker: String) = flow {

        try {
            val result = api.getTickerInfo(ticker)
            emit(
                StockInfo(
                    result.ceo,
                    result.address,
                    result.description
                )
            )

        } catch (e: Exception) {
            Log.e("", "error", e)
        }
    }.flowOn(dispatcher)

}