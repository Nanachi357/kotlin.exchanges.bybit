package com.github.nanachi357.exchanges.bybit.model.response.deposit

data class Deposits(
    val rows: List<DepositTransaction>? = null,
    val nextPageCursor: String? = null,
)
