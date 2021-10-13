package com.franksap2.finances.ui.stockdetail.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franksap2.finances.R
import com.franksap2.finances.data.repository.info.domainmodel.StockInfo
import com.franksap2.finances.utils.encode
import java.net.URLEncoder

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StockInformation(
    modifier: Modifier = Modifier,
    ticker: String,
    data: StockInfo?
) {
    AnimatedVisibility(
        visible = data != null,
        enter = fadeIn(animationSpec = tween(1000))
    ) {
        StockDetailContainer(
            modifier = modifier.animateContentSize(),
            title = stringResource(id = R.string.stock_info, ticker)
        ) {
            Text(
                fontWeight = FontWeight.Light,
                text = data?.description.orEmpty()
            )

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = stringResource(id = R.string.company_ceo),
                fontWeight = FontWeight.Bold
            )

            Text(
                fontWeight = FontWeight.Light,
                text = data?.ceo.orEmpty()
            )

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = stringResource(id = R.string.address),
                fontWeight = FontWeight.Bold
            )

            val uriHandler = LocalUriHandler.current
            val url = stringResource(R.string.google_maps_query, data?.address.orEmpty().encode())

            Text(
                modifier = Modifier.clickable {
                    uriHandler.openUri(url)
                },
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Light,
                text = data?.address.orEmpty(),
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Preview
@Composable
fun StockInformationPreview() {
    StockInformation(
        ticker = "AAPL",
        data = StockInfo(
            "Tim Cook",
            "Cupertino",
            "Apple Inc is designs, manufactures and markets mobile communication and media devices and personal computers"
        )
    )
}