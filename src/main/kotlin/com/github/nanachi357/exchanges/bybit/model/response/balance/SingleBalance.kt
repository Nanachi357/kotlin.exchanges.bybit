package com.github.nanachi357.exchanges.bybit.model.response.balance

import java.math.BigDecimal

data class SingleBalance(
    val coin: String? = null,
    val walletBalance: BigDecimal? = null,
    val transferBalance: BigDecimal? = null,
    val bonus: String? = null,
    val transferSafeAmount: String? = null,
)
