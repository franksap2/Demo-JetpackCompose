package com.franksap2.finances.ui.stockdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.franksap2.finances.R
import com.franksap2.finances.data.repository.ticker.domainmodel.StockChartData
import com.franksap2.finances.data.repository.details.dominmodel.StockProfile
import com.franksap2.finances.data.repository.details.dominmodel.StockRecommendations
import com.franksap2.finances.ui.stockdetail.ui.grapth.Chart
import com.franksap2.finances.ui.stockdetail.ui.GainsText
import com.franksap2.finances.ui.stockdetail.ui.GraphTimeSelector
import com.franksap2.finances.ui.stockdetail.ui.StockRecommendation
import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector
import com.franksap2.finances.ui.stockdetail.ui.StockInformation
import com.franksap2.finances.ui.stockdetail.ui.TickerText
import com.franksap2.finances.ui.stockdetail.viewmodel.StockDetailState
import com.franksap2.finances.ui.theme.FinancesTheme
import com.franksap2.finances.utils.formatToDecimals
import com.franksap2.finances.utils.orZero

@Composable
fun StockDetail(
    ticker: String,
    state: StockDetailState,
    onBackPressed: () -> Unit,
    onTimeSelectorClicked: (TimeSelector) -> Unit,
) {

    val (currency, setCurrency) = remember { mutableStateOf(-1f) }

    val (data, profile, recommendationData, stockInfo) = state

    Column {

        TopAppBar(
            title = { Text(text = ticker) },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            navigationIcon = {
                IconButton(onBackPressed) {
                    Image(painterResource(R.drawable.ic_back), null)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState(), currency == -1f)
                .padding(bottom = 50.dp)

        ) {

            StockDetailHeader(profile)

            val tickerAmount = if (currency == -1f) data?.currentPrice else currency

            TickerText(
                Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                tickerAmount.formatToDecimals().toFloat()
            )

            val sessionPrice = data?.let {
                if (data.timeSelector == TimeSelector.DAY) {
                    it.previousSessionPrice
                } else {
                    it.charData.first().closedPrice
                }
            }.orZero()

            val currentPrice = data?.currentPrice.orZero()

            GainsText(
                modifier = Modifier.padding(start = 20.dp),
                previousSessionClosedPrice = sessionPrice,
                currentPrice = currentPrice
            )

            StockDetailChart(data, state.showLoading, setCurrency)


            GraphTimeSelector(
                modifier = Modifier.padding(horizontal = 20.dp),
                onItemSelected = onTimeSelectorClicked
            )

            StockInformation(
                Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                ticker = ticker,
                data = stockInfo
            )

            StockRecommendation(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                data = recommendationData
            )

        }
    }
}


@Composable
fun StockDetailHeader(profile: StockProfile?) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .animateContentSize()
    ) {
        profile?.let {

            val handler = LocalUriHandler.current

            if (it.logo.isNotBlank()) {
                Image(
                    modifier = Modifier
                        .clickable { handler.openUri(it.web) }
                        .padding(end = 8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.onBackground.copy(0.1f))
                        .size(42.dp)
                        .padding(8.dp),
                    painter = rememberImagePainter(it.logo),
                    contentDescription = "",
                    contentScale = ContentScale.Inside
                )
            }

            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                style = MaterialTheme.typography.h5,
                text = it.companyName
            )
        }
    }
}

@Composable
fun StockDetailChart(
    data: StockChartData?,
    showLoading: Boolean,
    setCurrency: (Float) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(300.dp)
    ) {

        val alphaChart = animateFloatAsState(targetValue = if (showLoading) 0f else 1f)
        if (showLoading) {
            CircularProgressIndicator(
                Modifier.align(Alignment.Center)
            )
        }

        data?.let {
            Chart(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(alphaChart.value),
                data = data
            ) { selected ->
                setCurrency(selected?.closedPrice ?: -1f)
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun StockDetailPreview() {
    FinancesTheme(isNegative = false) {
        StockDetail(
            "AAPL",
            StockDetailState(
                profile = StockProfile("Apple", "AAPL", "", ""),
                stockRecommendations = StockRecommendations()
            ), {}
        ) {}
    }
}