package com.franksap2.finances.ui.stockdetail.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franksap2.finances.ui.theme.Shapes
import com.franksap2.finances.utils.formatToCurrency
import com.franksap2.finances.utils.formatToPercent


private const val animationTime = 700


@Composable
fun GainsText(
    modifier: Modifier = Modifier,
    previousSessionClosedPrice: Float,
    currentPrice: Float,
) {

    val gains = currentPrice - previousSessionClosedPrice
    val percentGains = (100 * gains / previousSessionClosedPrice).takeIf { !it.isNaN() } ?: 0f

    val textColor = MaterialTheme.colors.primary
    val backgroundColor = textColor.copy(0.4f)

    Row(modifier) {

        CarouselContent(
            modifier = Modifier
                .padding(end = 8.dp)
                .background(backgroundColor, Shapes.small)
                .padding(horizontal = 10.dp, vertical = 5.dp),
            targetState = percentGains,
        ) { value ->
            Text(
                color = textColor,
                text = value.formatToPercent()
            )
        }

        CarouselContent(
            modifier = Modifier.align(Alignment.CenterVertically),
            targetState = gains
        ) { value ->
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                color = textColor,
                text = value.formatToCurrency()
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T : Comparable<T>> CarouselContent(
    modifier: Modifier = Modifier,
    targetState: T,
    content: @Composable AnimatedVisibilityScope.(targetState: T) -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = targetState,
        transitionSpec = {

            val transition = if (targetState > initialState) {
                slideInVertically(
                    { height -> -height },
                    tween(animationTime)
                ) + fadeIn(animationSpec = tween(animationTime)) with
                        slideOutVertically({ height -> height }, tween(animationTime)) + fadeOut(
                    animationSpec = tween(
                        animationTime
                    )
                )
            } else {
                slideInVertically(
                    { height -> height },
                    tween(animationTime)
                ) + fadeIn(animationSpec = tween(animationTime)) with
                        slideOutVertically({ height -> -height }, tween(animationTime)) + fadeOut(
                    animationSpec = tween(
                        animationTime
                    )
                )
            }

            transition.using(SizeTransform(clip = true))
        },
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun GainsTextPreview() {

    GainsText(previousSessionClosedPrice = 10f, currentPrice = 12f)

}

