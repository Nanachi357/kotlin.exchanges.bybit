package com.github.nanachi357.exchanges.bybit.model.response.balance

data class SingleBalanceResponse(
    val accountType: String? = null,
    val bizType: Int? = null,
    val accountId: String? = null,
    val memberId: String? = null,
    val balance: SingleBalance? = null
)
