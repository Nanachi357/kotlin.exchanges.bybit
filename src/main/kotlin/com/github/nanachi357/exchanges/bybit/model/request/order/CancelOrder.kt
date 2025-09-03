package com.github.nanachi357.exchanges.bybit.model.request.order

data class CancelOrder(
    val category: String = "spot",
    val symbol: String,
    val orderId: String
)
