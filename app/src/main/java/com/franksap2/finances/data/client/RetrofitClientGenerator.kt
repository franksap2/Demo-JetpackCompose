package com.franksap2.finances.data.client

interface RetrofitClientGenerator {

    fun <T> getApi(`class`: Class<T>) : T

}