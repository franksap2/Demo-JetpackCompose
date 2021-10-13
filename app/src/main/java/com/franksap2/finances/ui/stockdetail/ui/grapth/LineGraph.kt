package com.franksap2.finances.ui.stockdetail.ui.grapth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.util.fastForEachIndexed
import com.franksap2.finances.data.repository.ticker.domainmodel.CharData
import com.franksap2.finances.data.repository.ticker.domainmodel.StockChartData
import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector


@Composable
fun LineGraph(
    modifier: Modifier,
    data: StockChartData,
    textIndicatorHeight: Float,
    currentChartDataSelected: CharData?,
) {

    val revealGraph = remember(data.timeSelector) { Animatable(0f) }
    val path = remember(data.timeSelector) { Path() }
    val gradientPath = remember(data.timeSelector) { Path() }
    val graphColor: Color = MaterialTheme.colors.primary

    LaunchedEffect(data.timeSelector) {
        revealGraph.animateTo(
            1f,
            tween(3000)
        )
    }

    Canvas(modifier) {
        if (!revealGraph.isRunning) {
            path.buildLineGraph(size, data, gradientPath, textIndicatorHeight)
        }

        drawIntoCanvas {

            it.withSaveLayer(Rect(Offset.Zero, size), Paint()) {

                drawPath(
                    path = path,
                    graphColor,
                    style = Stroke(5f)
                )

                drawPath(
                    path = gradientPath,
                    brush = Brush.verticalGradient(
                        listOf(graphColor, Color.Transparent)
                    )
                )

                drawRect(
                    Color.White,
                    Offset(size.width * data.remainTime * revealGraph.value, 0f),
                    size,
                    blendMode = BlendMode.DstOut
                )

                currentChartDataSelected?.let {
                    if (data.timeSelector == TimeSelector.WEEK || data.timeSelector == TimeSelector.DAY) {
                        drawHighLightMarketDay(currentChartDataSelected.graphSection, data)
                    }
                }

            }
        }

    }
}

private fun Path.buildLineGraph(
    size: Size,
    data: StockChartData,
    gradientPath: Path,
    textHeight: Float,
) {
    reset()
    gradientPath.reset()

    val with = size.width * data.remainTime
    val height = size.height
    val count = data.charData.size
    val stepCandleSize = with / count

    var prevX = 0f

    val highest = data.highestPrice
    val lowest = data.lowestPrice
    var firstPointY = 0f

    data.charData.fastForEachIndexed { index, value ->

        val yPoint = calculateYPoint(
            height,
            lowest,
            highest,
            textHeight,
            value.closedPrice
        )

        if (index == 0) {
            firstPointY = yPoint
            moveTo(prevX, yPoint)
            gradientPath.moveTo(prevX, yPoint)
        }

        lineTo(prevX, yPoint)
        moveTo(prevX, yPoint)

        gradientPath.lineTo(prevX, yPoint)

        prevX += stepCandleSize
    }

    close()

    gradientPath.apply {
        lineTo(prevX, height)

        lineTo(0f, height)

        lineTo(0f, firstPointY)
        close()
    }
}


private fun DrawScope.drawHighLightMarketDay(
    graphSection: Int,
    data: StockChartData,
) {

    data.graphSections[graphSection]?.let {
        val width = size.width * data.remainTime
        val chartDataSize = data.charData.lastIndex.toFloat()

        val from = width * (it[0] / chartDataSize)
        val to = width * (it[1] / chartDataSize)
        val color = Color(1f, 1f, 1f, 0.5f)

        drawRect(
            color,
            Offset(0f, 0f),
            Size(from, size.height),
            blendMode = BlendMode.DstOut
        )

        drawRect(
            color,
            Offset(to, 0f),
            Size(width - to, size.height),
            blendMode = BlendMode.DstOut
        )
    }

}