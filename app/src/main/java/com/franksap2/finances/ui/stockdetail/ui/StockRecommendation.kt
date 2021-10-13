package com.franksap2.finances.ui.stockdetail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franksap2.finances.R
import com.franksap2.finances.data.repository.details.dominmodel.StockRecommendations

import com.franksap2.finances.ui.theme.FinancesTheme
import com.franksap2.finances.ui.theme.Green500
import com.franksap2.finances.ui.theme.Red500
import com.franksap2.finances.utils.formatToPercentWithoutDecimals
import com.franksap2.finances.utils.orZero

@Composable
fun StockRecommendation(
    modifier: Modifier = Modifier,
    data: StockRecommendations?,
) {

    StockDetailContainer(
        modifier = modifier,
        title = stringResource(id = R.string.analytis_title)
    ) {

        Row {
            TextCircleBackground(
                modifier = Modifier.align(Alignment.CenterVertically),
                data = data
            )

            Column(
                modifier = Modifier
                    .height(90.dp)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                PercentProgressBar(
                    percent = data?.buyRating,
                    color = Green500,
                    leftText = stringResource(id = R.string.buy)
                )

                PercentProgressBar(
                    percent = data?.holdRating,
                    color = MaterialTheme.colors.onBackground,
                    leftText = stringResource(id = R.string.hold)
                )

                PercentProgressBar(
                    percent = data?.sellRating,
                    color = Red500,
                    leftText = stringResource(id = R.string.sell)
                )
            }
        }
    }


}

@Composable
fun TextCircleBackground(
    modifier: Modifier = Modifier,
    data: StockRecommendations?,
) {

    val primary = MaterialTheme.colors.primary
    val backgroundColor = primary.copy(0.2f)

    Box(
        modifier = modifier
            .padding(10.dp)
            .background(backgroundColor, CircleShape)
            .size(100.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        color = primary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    val percent = data?.buyRating.orZero() * 100
                    append(percent.formatToPercentWithoutDecimals())
                }

                withStyle(
                    SpanStyle(
                        fontSize = MaterialTheme.typography.caption.fontSize,
                        color = primary
                    )
                ) {
                    append(
                        stringResource(R.string.analyst_rating, data?.totalRatings.orZero())
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StockRecommendationsPreview() {
    FinancesTheme(isNegative = false) {
        StockRecommendation(
            data = StockRecommendations(
                0.5f,
                0.1f,
                0.0f,
                20
            )
        )
    }

}