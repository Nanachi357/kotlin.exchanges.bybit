package com.github.nanachi357.exchanges.bybit.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.runBlocking
import com.google.gson.Gson

import com.github.nanachi357.exchanges.bybit.model.response.DefaultResponse
import com.github.nanachi357.exchanges.bybit.model.response.public_api.*

import com.github.nanachi357.exchanges.bybit.utils.ApiUtils

object PublicApi {
    
    private const val BASE = "https://api.bybit.com"
    private const val SYMBOLS = "/v5/market/instruments-info"
    private const val FEES = "/v5/account/fee-rate"
    private const val CURRENCIES = "/v5/asset/coin/query-info"
    private const val KLINES = "/v5/market/kline"

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson()
        }
        install(WebSockets)
    }

    fun symbols(): Symbols? {
        return sendRequest<Symbols>(HttpMethod.Get, SYMBOLS, query = mapOf("category" to "spot"))?.result
    }

    fun klines(coin: String): Klines? {
        val query = mapOf(
            "category" to "spot",
            "symbol" to coin + "USDT",
            "interval" to "60",
            "limit" to "24"
        )
        return sendRequest<Klines>(HttpMethod.Get, KLINES, query = query)?.result
    }

    fun currencies(): Currencies? {
        val headers = ApiUtils.genGetSign(emptyMap())
        return sendRequest<Currencies>(HttpMethod.Get, CURRENCIES, headers = headers)?.result
    }

    fun fees(symbol: String): Fees? {
        val query = mapOf("category" to "spot", "symbol" to symbol)
        val headers = ApiUtils.genGetSign(query)
        return sendRequest<Fees>(HttpMethod.Get, FEES, headers = headers, query = query)?.result
    }

    private inline fun <reified T> sendRequest(
        httpMethod: HttpMethod,
        prefix: String = "",
        path: String = "",
        body: Any? = null,
        query: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap()
    ): DefaultResponse<T>? {
        return runBlocking {
            runCatching {
                client.request {
                    method = httpMethod
                    url(BASE + prefix + path)
                    contentType(ContentType.Application.Json)
                    if (body != null) setBody(body)
                    headers { headers.onEach { append(it.key, it.value) } }
                    url { query.onEach { parameters.append(it.key, it.value) } }
                }.body<DefaultResponse<T>>()
            }.getOrNull()
        }
    }
}
