package com.franksap2.finances.ui.stocklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.franksap2.finances.R
import com.franksap2.finances.ui.theme.FinancesTheme
import com.franksap2.finances.utils.createComposeView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = createComposeView {
        FinancesTheme(isNegative = false) {
            StockList(::navigate)
        }
    }

    private fun navigate(ticker: String) {
        val action = StockListFragmentDirections.actionStockListToStockDetail(
            if (ticker.isEmpty()) "MSFT" else ticker
        )
        findNavController().navigate(action)
    }
}

@Composable
fun StockList(onClick: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(10.dp),
        Arrangement.spacedBy(10.dp)
    ) {

        var inputField by rememberSaveable { mutableStateOf("") }

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = 5.dp
        ) {
            TextField(
                value = inputField,
                onValueChange = { inputField = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = MaterialTheme.colors.onSurface,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        stringResource(id = R.string.stock_finder_hint),
                        color = Color.LightGray
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                    )
                }
            )

        }
        Button(
            onClick = { onClick(inputField) }
        ) {
            Text(
                stringResource(R.string.search_action)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockListPreview() {
    StockList {}
}