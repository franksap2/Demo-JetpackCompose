package com.franksap2.finances.ui.stockdetail.ui.grapth

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.franksap2.finances.data.repository.ticker.domainmodel.CharData
import com.franksap2.finances.data.repository.ticker.domainmodel.StockChartData
import com.franksap2.finances.utils.formatToChartDay

@Composable
fun DragIndicator(
    modifier: Modifier,
    data: StockChartData,
    lineColor: Color,
    textIndicatorHeight: Float,
    dragIndicatorRadius: Float,
    textPaint: NativePaint,
    textBounds: Rect,
    currentSelection: ((CharData?) -> Unit)?,
) {

    val indicatorColor: Color = MaterialTheme.colors.primary
    val indicatorBgColor = MaterialTheme.colors.background

    val haptic = LocalHapticFeedback.current
    var dragPosition by remember { mutableStateOf(0f) }
    var isUserDragging by remember { mutableStateOf(false) }
    val previousCharDataInvokedOnDrag = remember { mutableStateOf(0) }

    Canvas(
        modifier.pointerInput(Unit) {

            detectDragGesturesAfterLongPress(
                onDragStart = {
                    dragPosition = it.x
                    previousCharDataInvokedOnDrag.value = -1
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onDragEnd = {
                    isUserDragging = false
                    currentSelection?.invoke(null)
                },
                onDrag = { _, offset ->
                    isUserDragging = true
                    dragPosition += offset.x
                }
            )
        }
    ) {

        drawIntoCanvas {
            if (isUserDragging) {
                drawDragPriceIndicator(
                    it,
                    dragPosition,
                    data,
                    textPaint,
                    textBounds,
                    indicatorColor,
                    lineColor,
                    currentSelection,
                    previousCharDataInvokedOnDrag,
                    textIndicatorHeight,
                    dragIndicatorRadius,
                    indicatorBgColor
                )
            }
        }
    }
}


private fun DrawScope.drawDragPriceIndicator(
    canvas: Canvas,
    dragPosition: Float,
    data: StockChartData,
    textPaint: NativePaint,
    textBounds: Rect,
    indicatorColor: Color,
    lineColor: Color,
    currentSelection: ((CharData?) -> Unit)?,
    previousPosition: MutableState<Int>,
    textIndicatorHeight: Float,
    dragIndicatorRadius: Float,
    indicatorBgColor: Color,
) {

    val width = size.width * data.remainTime
    val draggedPercent = (dragPosition.coerceAtMost(width)) / width
    val distance = (data.charData.lastIndex * draggedPercent).toInt()
    val pointX = (width / data.charData.size) * distance

    data.charData.getOrNull(distance)?.let {

        val yPoint = calculateYPoint(
            size.height,
            data.lowestPrice,
            data.highestPrice,
            textIndicatorHeight,
            it.closedPrice
        )

        drawCircle(indicatorBgColor, radius = dragIndicatorRadius, center = Offset((pointX), yPoint))
        drawCircle(indicatorColor, radius = 10f, center = Offset((pointX), yPoint))

        drawLine(
            lineColor,
            start = Offset(pointX, textBounds.height().toFloat() + 5.dp.toPx()),
            end = Offset(pointX, size.height - 20f),
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )



        if (previousPosition.value != distance) {
            previousPosition.value = distance
            currentSelection?.invoke(it)
        }

        val dateText = it.timeStamp.formatToChartDay(data.timeSelector)
        val textSize = textPaint.measureText(dateText) / 2f

        val textPostX = pointX.coerceIn(textSize, size.width - textSize)
        canvas.nativeCanvas.drawText(
            dateText,
            textPostX,
            textBounds.height().toFloat(),
            textPaint
        )
    }
}
