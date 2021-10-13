package com.franksap2.finances.ui.stockdetail.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import android.graphics.Rect as LegacyRect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franksap2.finances.utils.formatToPercentOneDecimal
import com.franksap2.finances.utils.orZero


@Composable
fun PercentProgressBar(
    modifier: Modifier = Modifier,
    percent: Float?,
    color: Color,
    leftText: String,
) {

    val animatedValue = remember { Animatable(0f) }
    LaunchedEffect(percent) {
        animatedValue.animateTo(percent.orZero(), animationSpec = tween(2000))
    }


    val textPaint = remember {
        Paint().apply {
            this.color = color
        }.asFrameworkPaint().apply {
            textSize = 32f
            textAlign = android.graphics.Paint.Align.CENTER
        }
    }

    val percentText = (100 * percent.orZero()).formatToPercentOneDecimal()
    val textBounds = remember(percent) {
        LegacyRect().also {
            textPaint.getTextBounds(
                percentText,
                0,
                percentText.length,
                it
            )
        }
    }

    "prueba".lowercase()

    Row {

        Text(
            text = leftText,
            style = MaterialTheme.typography.caption
        )

        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(15.dp)
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
        ) {

            val targetSize = size.width * animatedValue.value

            drawIntoCanvas {

                it.withSaveLayer(Rect(Offset.Zero, size), Paint()) {

                    val barHeight = 4.dp.toPx()
                    val barOffset = Offset(0f, size.height / 2f - barHeight / 2f)
                    val cornerRadius = CornerRadius(12f, 12f)

                    drawRoundRect(
                        color = color.copy(0.2f),
                        size = Size(size.width, barHeight),
                        topLeft = barOffset,
                        cornerRadius = cornerRadius
                    )
                    drawRoundRect(
                        color,
                        size = Size(targetSize, barHeight),
                        topLeft = barOffset,
                        cornerRadius = cornerRadius
                    )

                    val textPadding = 5.dp.toPx()

                    val startTextPosition = (targetSize + textBounds.centerX() + textPadding)
                        .coerceAtMost(size.width - textBounds.width() / 2f)

                    val boxWidth = textBounds.width().toFloat() + 4 * textPadding
                    val maskOffsetX = targetSize.coerceAtMost(size.width - textBounds.width())

                    val maskOffset = Offset(maskOffsetX, 0f)
                    drawRect(
                        brush = Brush.horizontalGradient(
                            startX = maskOffset.x - textPadding,
                            endX = maskOffset.x + boxWidth + textPadding,
                            colorStops = arrayOf(0.4f to Color.White, 0.9f to Color.Transparent)
                        ),
                        blendMode = BlendMode.DstOut,
                        size = Size(boxWidth, size.height),
                        topLeft = maskOffset
                    )

                    val textAlignment = ((textPaint.descent() + textPaint.ascent()) / 2)
                    it.nativeCanvas.drawText(
                        percentText,
                        startTextPosition,
                        (size.height / 2f) - textAlignment,
                        textPaint
                    )
                }
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun PercentProgressBarPreview() {
    PercentProgressBar(percent = 1f, color = Color.Blue, leftText = "Hold")
}