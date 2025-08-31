package com.github.nanachi357.exchanges.bybit.model.response.public_api
data class Klines(
    val symbol: String? = null,
    val category: String? = null,
    val list: List<List<String>>? = null
)
