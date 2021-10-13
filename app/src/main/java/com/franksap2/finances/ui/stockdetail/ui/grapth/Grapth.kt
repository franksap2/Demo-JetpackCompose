package com.franksap2.finances.ui.stockdetail.ui.grapth

import android.graphics.Rect
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.franksap2.finances.R
import com.franksap2.finances.data.repository.ticker.domainmodel.CharData
import com.franksap2.finances.data.repository.ticker.domainmodel.StockChartData
import com.google.android.material.math.MathUtils


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Chart(
    modifier: Modifier = Modifier,
    data: StockChartData,
    lineColor: Color = colorResource(id = R.color.indicator_line),
    textColor: Color = colorResource(id = R.color.text_graph_color),
    dragIndicatorRadius: Float = 13.5f,
    currentSelection: ((CharData?) -> Unit),
) {

    val (textPaint, textBounds) = rememberPaintState(textColor, data.highestPrice.toString())
    var currentChartDataSelected by remember { mutableStateOf<CharData?>(null) }
    val textIndicatorHeight = remember { textBounds.height().toFloat() + dragIndicatorRadius }

    OpenMarketPriceIndicator(modifier, data, textIndicatorHeight)

    LineGraph(modifier, data, textIndicatorHeight, currentChartDataSelected)

    DragIndicator(
        modifier = modifier,
        data = data,
        lineColor = lineColor,
        textIndicatorHeight = textIndicatorHeight,
        dragIndicatorRadius = dragIndicatorRadius,
        textPaint = textPaint,
        textBounds = textBounds,
        currentSelection = {
            currentSelection(it)
            currentChartDataSelected = it
        }
    )

}


internal fun calculateYPoint(
    height: Float,
    lowest: Float,
    highest: Float,
    textHeight: Float,
    sessionPrice: Float,
): Float {

    val percent = (1 - ((sessionPrice - lowest) / (highest - lowest))).coerceIn(0f, 1f)

    return MathUtils.lerp(textHeight, height - textHeight, percent)
}

@Composable
private fun rememberPaintState(
    textColor: Color,
    highestPrice: String,
) = remember {

    val textPaint = Paint().apply {
        color = textColor
    }.asFrameworkPaint().apply {
        textSize = 35.sp.value
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val textBounds = Rect().also {
        textPaint.getTextBounds(
            highestPrice,
            0,
            highestPrice.length,
            it
        )
    }

    textPaint to textBounds
}