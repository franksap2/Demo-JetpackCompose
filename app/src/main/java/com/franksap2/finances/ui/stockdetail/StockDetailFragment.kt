package com.franksap2.finances.ui.stockdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.franksap2.finances.data.repository.details.dominmodel.StockProfile
import com.franksap2.finances.data.repository.details.dominmodel.StockRecommendations
import com.franksap2.finances.ui.stockdetail.viewmodel.StockDetailAssistedFactory
import com.franksap2.finances.ui.stockdetail.viewmodel.StockDetailState
import com.franksap2.finances.ui.stockdetail.viewmodel.StockDetailViewModel
import com.franksap2.finances.ui.theme.FinancesTheme
import com.franksap2.finances.utils.createComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StockDetailFragment : Fragment() {

    private val args by navArgs<StockDetailFragmentArgs>()

    @Inject
    lateinit var factory: StockDetailAssistedFactory

    private val ticker by lazy { args.stockSticker.uppercase() }

    private val viewModel: StockDetailViewModel by viewModels {
        StockDetailViewModel.provideFactory(
            factory,
            ticker
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = createComposeView {

        val state by viewModel.state.collectAsState()

        FinancesTheme(isNegative = state.stockChartData?.isNegative ?: false) {
            Surface {
                activity?.window?.statusBarColor = MaterialTheme.colors.primaryVariant.toArgb()
                StockDetail(
                    ticker,
                    state,
                    { findNavController().popBackStack() }
                ) {
                    viewModel.requestStockCharData(it)
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DefaultPreview() {
    FinancesTheme(isNegative = false) {
        StockDetail(
            "AAPL",
            StockDetailState(
                profile = StockProfile("Apple", "AAPL", "", ""),
                stockRecommendations = StockRecommendations()
            ),
            {}
        ) {}
    }
}