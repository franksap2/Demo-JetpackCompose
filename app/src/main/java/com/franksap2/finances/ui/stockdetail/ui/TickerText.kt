package com.franksap2.finances.ui.stockdetail.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.franksap2.finances.R
import com.franksap2.finances.utils.toSafeFloat
import com.google.android.material.math.MathUtils
import kotlinx.coroutines.CoroutineScope


@Composable
fun TickerText(
    modifier: Modifier = Modifier,
    newValue: Float
) {

    var currentValue by remember { mutableStateOf(newValue) }
    val scrollAnimation = remember { Animatable(0f) }
    val animationData = remember { ArrayList<TickerInfo>() }

    if (!scrollAnimation.isRunning) {
        currentValue = newValue
    }

    LaunchedEffect(currentValue) {
        scrollAnimation.snapTo(0f)
        scrollAnimation.animateTo(1f, tween(300))
        animationData.clearAnimationState()
        currentValue = newValue
    }

    val value = if (scrollAnimation.isRunning) scrollAnimation.value else 0f
    animationData.buildTickerAnimInfo(currentValue, value)


    Row(modifier.animateContentSize()) {

        CarrouselText(
            currentValue,
            animationData
        )

        Text(
            fontSize = 38.sp,
            text = "\$"
        )
    }
}

@Composable
private fun CarrouselText(currentValue: Float, animationData: ArrayList<TickerInfo>) {

    val currentValueAsString = currentValue.toString()
    val tickerTextSize = with(LocalDensity.current) { 42.dp.toPx() }
    val padding = with(LocalDensity.current) { 10.dp.toPx() }.toInt()
    val spacing = with(LocalDensity.current) { 1.dp.toPx() }.toInt()
    val widthSpacing = with(LocalDensity.current) { 10.dp.toPx() }.toInt()
    val textColor = MaterialTheme.colors.onBackground

    val paint = remember {
        Paint().apply {
            color = textColor
        }.asFrameworkPaint().apply {
            textSize = tickerTextSize
            textAlign = android.graphics.Paint.Align.LEFT
        }
    }

    val bounds = android.graphics.Rect()
    paint.getTextBounds(currentValueAsString, 0, currentValueAsString.length, bounds)
    val textSizes = FloatArray(currentValueAsString.length)
    paint.getTextWidths(currentValueAsString, 0, currentValueAsString.length, textSizes)

    Canvas(
        modifier = Modifier
            .animateContentSize()
            .layout { measurable, _ ->
                val placeable = measurable.measure(
                    Constraints.fixed(
                        bounds.width(),
                        bounds.height()
                    )
                )
                layout(bounds.width(), (bounds.height()) + padding + spacing) {
                    placeable.place(
                        0,
                        padding
                    )
                }
            }
    ) {

        drawIntoCanvas {

            var previousPosition = size.height
            var previousWidth = 0f

            if (animationData.isEmpty()) return@Canvas

            animationData.forEachIndexed { index, tickerInfo ->

                if (tickerInfo.to.isDigit()) {
                    it.save()

                    val translateTo = (size.height + padding + spacing) * (tickerInfo.currentValue)
                    it.translate(0f, translateTo)

                    repeat(10) { j ->
                        it.nativeCanvas.drawText(
                            j.toString(),
                            previousWidth,
                            previousPosition,
                            paint
                        )
                        previousPosition -= size.height + padding + spacing
                    }

                    it.restore()

                    previousPosition = size.height
                    previousWidth += textSizes[index] / 2 + widthSpacing


                } else {

                    it.nativeCanvas.drawText(
                        tickerInfo.to.toString(),
                        previousWidth,
                        previousPosition,
                        paint
                    )

                    previousWidth += textSizes[index]
                }
            }

        }
    }
}

private fun ArrayList<TickerInfo>.buildTickerAnimInfo(to: Float, value: Float) {

    val targetSize = to.toString().length
    if (targetSize < size) {
        repeat(size - targetSize) {
            removeLast()
        }
    }

    to.toString().forEachIndexed { index, c ->
        val current = getOrNull(index)

        current?.let {
            if (current.to != c) {
                current.from = current.to
                current.to = c
            }

            current.currentValue = MathUtils.lerp(current.from.toSafeFloat(), current.to.toSafeFloat(), value)

        } ?: run {
            val newInfo = TickerInfo('0', c)
            newInfo.currentValue = MathUtils.lerp(0f, c.toSafeFloat(), value)
            add(newInfo)
        }
    }
}

private fun ArrayList<TickerInfo>.clearAnimationState() {
    forEach {
        it.from = it.to
        it.currentValue = it.to.toSafeFloat()
    }
}


private data class TickerInfo(var from: Char, var to: Char, var currentValue: Float = 0f)