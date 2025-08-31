package com.github.nanachi357.exchanges.bybit.model.response.public_api
data class Chain(
    val chainType: String? = null,
    val confirmation: String? = null,
    val withdrawFee: String? = null,
    val depositMin: String? = null,
    val withdrawMin: String? = null,
    val chain: String? = null,
    val chainDeposit: String? = null,
    val chainWithdraw: String? = null,
    val minAccuracy: String? = null,
    val withdrawPercentageFee: String? = null,
)
