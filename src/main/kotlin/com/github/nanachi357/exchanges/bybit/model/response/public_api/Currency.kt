package com.github.nanachi357.exchanges.bybit.model.response.public_api
data class Currency(
    val name: String? = null,
    val coin: String? = null,
    val remainAmount: String? = null,
    val chains: List<Chain>? = null
)
