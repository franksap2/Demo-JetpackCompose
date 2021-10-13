package com.franksap2.finances.data.client

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class FinnhubClientGeneratorImpl @Inject constructor() : RetrofitClientGenerator {

    companion object {
        const val BASE_URL = "https://finnhub.io/"
        const val TOKEN = "c3vsf92ad3iaf2se2vtg"
    }

    private val retrofit by lazy {
        Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(buildClient())
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    private fun buildClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor())
            .addInterceptor(logging)
            .build()
    }

    private fun headerInterceptor() = Interceptor {
        val request = it.request().newBuilder()
        request.addHeader("X-Finnhub-Token", TOKEN)
        it.proceed(request.build())
    }

    override fun <T> getApi(`class`: Class<T>): T = retrofit.create(`class`)


}