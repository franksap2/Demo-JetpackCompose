package com.franksap2.finances.ui.stockdetail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.franksap2.finances.R

@Composable
fun StockDetailContainer(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {

    Column(
        modifier = modifier
            .padding(
                horizontal = 28.dp
            )
    ) {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = title,
            style = MaterialTheme.typography.h4
        )

        Divider(modifier = Modifier.padding(vertical = 12.dp))

        content()
    }

}

@Preview(showBackground = true)
@Composable
fun StockDetailContainerPreview() {
    StockDetailContainer(title = stringResource(id = R.string.analytis_title)) {
        Text(text = "Content")
    }
}