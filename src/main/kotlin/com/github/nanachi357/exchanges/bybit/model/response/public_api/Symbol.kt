package com.github.nanachi357.exchanges.bybit.model.response.public_api
data class Symbol(
    val symbol: String? = null,
    val baseCoin: String? = null,
    val quoteCoin: String? = null,
    val innovation: String? = null,
    val status: String? = null,
    val marginTrading: String? = null,
    val stTag: String? = null,
    val lotSizeFilter: LotSizeFilter? = null,
)
