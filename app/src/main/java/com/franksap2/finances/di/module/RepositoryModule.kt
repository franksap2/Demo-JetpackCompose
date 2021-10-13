package com.franksap2.finances.di.module

import com.franksap2.finances.data.repository.info.StockInfoRepository
import com.franksap2.finances.data.repository.info.StockInfoInfoRepositoryImpl
import com.franksap2.finances.data.repository.details.StockDetailsRepository
import com.franksap2.finances.data.repository.details.StockDetailsRepositoryImpl
import com.franksap2.finances.data.repository.ticker.TickerRepository
import com.franksap2.finances.data.repository.ticker.TickerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun stockRepository(
        stockInfoRepositoryImpl: StockInfoInfoRepositoryImpl
    ): StockInfoRepository

    @Binds
    abstract fun tickerRepository(
        tickerRepositoryImpl: TickerRepositoryImpl
    ): TickerRepository

    @Binds
    abstract fun stockDetailsRepository(
        stockDetailsRepositoryImpl: StockDetailsRepositoryImpl
    ): StockDetailsRepository

}
