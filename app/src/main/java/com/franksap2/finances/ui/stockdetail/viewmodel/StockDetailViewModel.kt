package com.franksap2.finances.ui.stockdetail.viewmodel


import androidx.lifecycle.*
import com.franksap2.finances.data.repository.ticker.domainmodel.StockChartData
import com.franksap2.finances.data.repository.details.StockDetailsRepository
import com.franksap2.finances.data.repository.details.dominmodel.StockProfile
import com.franksap2.finances.data.repository.details.dominmodel.StockRecommendations
import com.franksap2.finances.data.repository.ticker.TickerRepository
import com.franksap2.finances.data.repository.details.dominmodel.TimeSelector
import com.franksap2.finances.data.repository.info.StockInfoRepository
import com.franksap2.finances.data.repository.info.domainmodel.StockInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class StockDetailViewModel @AssistedInject constructor(
    @Assisted private val ticker: String,
    private val tickerRepository: TickerRepository,
    private val stockDetailsRepository: StockDetailsRepository,
    private val stockInfoRepository: StockInfoRepository
) : ViewModel() {


    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            stockDetailAssistedFactory: StockDetailAssistedFactory,
            ticker: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return stockDetailAssistedFactory.create(ticker) as T
            }
        }
    }


    private val _state = MutableStateFlow(StockDetailState())

    private val showLoading = MutableStateFlow(false)
    private val stockChartData = MutableStateFlow<StockChartData?>(null)
    private val stockProfile = MutableStateFlow<StockProfile?>(null)
    private val stockInfo = MutableStateFlow<StockInfo?>(null)
    private val stockRecommendations = MutableStateFlow<StockRecommendations?>(null)

    val state: StateFlow<StockDetailState>
        get() = _state

    private var job: Job? = null

    init {
        requestStockCharData(TimeSelector.DAY)
        requestRecommendations()
        requestStockProfile()
        requestStockInfo()

        viewModelScope.launch {
            combine(
                showLoading,
                stockChartData,
                stockProfile,
                stockRecommendations,
                stockInfo
            ) { showLoading, stockChart, stockProfile, stockRecommendations, stockInfo ->
                return@combine StockDetailState(stockChart, stockProfile, stockRecommendations, stockInfo, showLoading)
            }.collect {
                _state.value = it
            }
        }
    }

    private fun requestRecommendations() {
        viewModelScope.launch {
            stockDetailsRepository.getStockRecommendations(ticker).collect {
                stockRecommendations.value = it
            }
        }
    }

    private fun requestStockProfile() {
        viewModelScope.launch {
            stockDetailsRepository.getStockProfile(ticker).collect {
                stockProfile.value = it
            }
        }
    }

    private fun requestStockInfo() {
        viewModelScope.launch {
            stockInfoRepository.getStockInfo(ticker).collect {
                stockInfo.value = it
            }
        }
    }

    fun requestStockCharData(timeSelector: TimeSelector) {
        showLoading.value = true
        job?.cancel()
        job = viewModelScope.launch {
            tickerRepository.getStockChartData(ticker, timeSelector).collect {
                showLoading.value = false
                stockChartData.value = it
            }
        }
    }
}

data class StockDetailState(
    val stockChartData: StockChartData? = null,
    val profile: StockProfile? = null,
    val stockRecommendations: StockRecommendations? = null,
    val stockInfo: StockInfo? = null,
    val showLoading: Boolean = false,
)



