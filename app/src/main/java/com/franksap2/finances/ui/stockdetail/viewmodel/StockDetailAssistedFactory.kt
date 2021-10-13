package com.franksap2.finances.ui.stockdetail.viewmodel

@dagger.assisted.AssistedFactory
interface StockDetailAssistedFactory {
    fun create(ticker: String): StockDetailViewModel
}