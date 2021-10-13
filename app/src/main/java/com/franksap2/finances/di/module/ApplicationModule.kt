package com.franksap2.finances.di.module

import com.franksap2.finances.data.client.FinnhubClientGeneratorImpl
import com.franksap2.finances.data.client.RetrofitClientGenerator
import com.franksap2.finances.data.client.PolygonClientGeneratorImpl
import com.franksap2.finances.di.qualifiers.FinnhubClient
import com.franksap2.finances.di.qualifiers.IODispatcher
import com.franksap2.finances.di.qualifiers.PolygonClient
import com.franksap2.finances.di.qualifiers.UiDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import java.util.TimeZone

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @IODispatcher
    @Provides
    fun provideIODispatcher() = Dispatchers.IO

    @UiDispatcher
    @Provides
    fun provideMainDispatcher() = Dispatchers.Main

    @PolygonClient
    @Provides
    fun polygonClientGenerator(): RetrofitClientGenerator {
        return PolygonClientGeneratorImpl()
    }

    @FinnhubClient
    @Provides
    fun finnhubClientGenerator(): RetrofitClientGenerator {
        return FinnhubClientGeneratorImpl()
    }

    @Provides
    fun provideTimesZone(): TimeZone = TimeZone.getTimeZone("GMT-4")
}