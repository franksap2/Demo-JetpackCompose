package com.franksap2.finances.data.client

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import okio.ByteString
import javax.inject.Inject

class FinnhubWebSocketClient @Inject constructor() : WebSocketListener() {

    private companion object {
        const val URL = "wss://ws.finnhub.io?token=${FinnhubClientGeneratorImpl.TOKEN}"
    }

    private val request by lazy { Request.Builder().url(URL).build() }
    private val client by lazy { OkHttpClient() }

    fun startSocket() {
        client.newWebSocket(request, this)
    }

    fun closeSocket() {
        client.dispatcher.executorService.shutdown()
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        val gson = Gson()
        val values = gson.toJson("{\" type \":\" subscribe \",\" symbol \":\" AAPL \"}")

        val json = JsonObject()
        json.addProperty("type", "subscribe")
        json.addProperty("symbol", "AAPL")

        webSocket.send(json.toString())
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("Test", text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        Log.d("Test", "ALHO")
    }
}