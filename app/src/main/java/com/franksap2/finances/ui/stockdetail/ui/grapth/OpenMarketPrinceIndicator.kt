package com.franksap2.finances.ui.stockdetail.ui.grapth

import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import com.franksap2.finances.data.repository.ticker.domainmodel.StockChartData
import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector


@Composable
fun OpenMarketPriceIndicator(
    modifier: Modifier,
    data: StockChartData,
    textIndicatorHeight: Float,
) {

    val graphColor: Color = MaterialTheme.colors.primary

    Canvas(modifier) {

        val sessionPrice = if (data.timeSelector == TimeSelector.DAY) {
            data.previousSessionPrice
        } else {
            data.charData.first().closedPrice
        }

        val yPoint = calculateYPoint(
            size.height,
            data.lowestPrice,
            data.highestPrice,
            textIndicatorHeight,
            sessionPrice
        )

        drawLine(
            graphColor,
            start = Offset(0f, yPoint),
            end = Offset(size.width, yPoint),
            strokeWidth = 4f,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(1f, 14f), 0f)
        )
    }
}
