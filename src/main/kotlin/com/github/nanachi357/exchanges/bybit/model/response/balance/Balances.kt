package com.github.nanachi357.exchanges.bybit.model.response.balance

data class Balances(
    val memberId: String? = null,
    val accountType: String? = null,
    val balance: List<SingleBalance>? = null,
)
