package com.github.nanachi357.exchanges.bybit.model.response.public_api
data class FeeRate(
    val symbol: String? = null,
    val takerFeeRate: String? = null,
    val makerFeeRate: String? = null,
)
