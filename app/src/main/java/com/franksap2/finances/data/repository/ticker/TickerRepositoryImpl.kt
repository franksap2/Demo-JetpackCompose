package com.franksap2.finances.data.repository.ticker

import android.util.Log
import com.franksap2.finances.data.client.RetrofitClientGenerator
import com.franksap2.finances.data.repository.ticker.domainmodel.CharData
import com.franksap2.finances.data.repository.ticker.domainmodel.StockChartData
import com.franksap2.finances.data.repository.ticker.api.TickerApi
import com.franksap2.finances.data.repository.ticker.model.ChartResponse
import com.franksap2.finances.data.repository.ticker.model.TickerResponse
import com.franksap2.finances.di.qualifiers.FinnhubClient
import com.franksap2.finances.di.qualifiers.IODispatcher
import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TickerRepositoryImpl @Inject constructor(
    @param:IODispatcher private val dispatcher: CoroutineDispatcher,
    @param:FinnhubClient private val generator: RetrofitClientGenerator,
    private val dateResolver: DateResolver
) : TickerRepository {

    private companion object {
        const val AFTER_MARKET = -2
        const val PRE_MARKET = 1
        const val MARKET = 0
    }

    private val api by lazy { generator.getApi(TickerApi::class.java) }

    override suspend fun getStockChartData(ticker: String, timeSelector: TimeSelector) = flow {

        val query = dateResolver.toQueryCalendar(timeSelector)
        val toDate = dateResolver.getToDate(query.first.timeInMillis, timeSelector)

        try {
            val result = requestChartDataAndCurrentPrice(query, toDate, timeSelector, ticker)
            emit(result)
        } catch (e: Exception) {
            Log.e("", "error", e)
        }

    }.flowOn(dispatcher)

    private suspend fun requestChartDataAndCurrentPrice(
        query: Pair<Calendar, String>,
        toDate: Calendar,
        timeSelector: TimeSelector,
        ticker: String
    ): StockChartData {
        val result = api.getChartData(
            query.first.timeInMillis / 1000,
            toDate.timeInMillis / 1000L,
            query.second,
            ticker
        )
        val tickerPrice = api.getTickerPrice(ticker)
        return mapToDomain(result, tickerPrice, timeSelector, query.first)
    }

    private fun mapToDomain(
        chartResponse: ChartResponse,
        tickerPrice: TickerResponse,
        timeSelector: TimeSelector,
        from: Calendar
    ): StockChartData {

        val data = ArrayList<CharData>()
        val sections = HashMap<Int, IntArray>()
        var previousSection = -1
        val sectionList = ArrayList<Int>()
        chartResponse.closedPrice?.size?.let {
            for (i in 0 until it) {

                val closedPrice = chartResponse.closedPrice[i]
                val openPrice = chartResponse.openPrice[i]
                val lowestPrice = chartResponse.lowestPrice[i]
                val highestPrice = chartResponse.highestPrice[i]
                val timeStamp = chartResponse.timeStamp[i] * 1000


                if (timeSelector == TimeSelector.WEEK) {
                    val day = dateResolver.getDayFromTimeStamp(timeStamp)

                    if (day == previousSection || previousSection == -1) {
                        sectionList.add(i)
                        sections[day] = intArrayOf(sectionList.first(), sectionList.last())
                    } else {
                        sectionList.clear()
                    }
                    previousSection = day

                } else if (timeSelector == TimeSelector.DAY) {

                    val type = when {

                        dateResolver.isAfterMarket(timeStamp) -> AFTER_MARKET

                        dateResolver.isPreMarket(timeStamp) -> PRE_MARKET

                        else -> MARKET
                    }

                    if (type == previousSection || previousSection == -1) {
                        sectionList.add(i)
                        sections[type] = intArrayOf(sectionList.first(), sectionList.last())
                    } else {
                        sectionList.clear()
                    }

                    previousSection = type
                }

                data.add(
                    CharData(
                        closedPrice = closedPrice,
                        openPrice = openPrice,
                        highestPrice = highestPrice,
                        lowestPrice = lowestPrice,
                        timeStamp = timeStamp,
                        graphSection = previousSection
                    )
                )
            }

            if (dateResolver.isMarketOpen(from)) {
                data.add(
                    CharData(
                        closedPrice = tickerPrice.currentPrice,
                        openPrice = tickerPrice.openPrice,
                        highestPrice = tickerPrice.highestPrice,
                        lowestPrice = tickerPrice.lowestPrice,
                        timeStamp = tickerPrice.timeStamp * 1000L,
                        graphSection = previousSection
                    )
                )
            }
        }


        val highest = data.maxByOrNull { it.closedPrice }?.closedPrice ?: 0f
        val lowest = data.minByOrNull { it.closedPrice }?.closedPrice ?: 0f

        val reamingTime = if (timeSelector == TimeSelector.DAY) {
            dateResolver.remainTime(from, data.last().timeStamp)
        } else {
            1f
        }

        val sessionPrice = if (timeSelector == TimeSelector.DAY) {
            tickerPrice.previousClosePrice
        } else {
            data.first().closedPrice
        }
        return StockChartData(
            highestPrice = highest,
            lowestPrice = lowest,
            openPrice = tickerPrice.openPrice,
            previousSessionPrice = tickerPrice.previousClosePrice,
            currentPrice = tickerPrice.currentPrice,
            charData = data,
            timeSelector = timeSelector,
            isNegative = tickerPrice.currentPrice < sessionPrice,
            remainTime = reamingTime,
            graphSections = sections
        )
    }


}
