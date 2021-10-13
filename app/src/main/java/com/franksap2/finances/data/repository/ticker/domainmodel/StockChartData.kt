package com.franksap2.finances.data.repository.ticker.domainmodel

import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector

data class StockChartData(
    val highestPrice: Float,
    val lowestPrice: Float,
    val previousSessionPrice : Float,
    val openPrice : Float,
    val currentPrice : Float,
    val timeSelector: TimeSelector,
    val charData: List<CharData>,
    val isNegative :Boolean,
    val remainTime : Float,
    val graphSections : HashMap<Int, IntArray>
)

data class CharData(
    val openPrice: Float,
    val closedPrice: Float,
    val highestPrice: Float,
    val lowestPrice: Float,
    val timeStamp : Long,
    val graphSection : Int
)