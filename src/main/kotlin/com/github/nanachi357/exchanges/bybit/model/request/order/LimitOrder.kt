package com.github.nanachi357.exchanges.bybit.model.request.order

import com.github.nanachi357.exchanges.bybit.model.enums.OrderSide

data class LimitOrder(
    val category: String = "spot",
    val symbol: String,
    val side: OrderSide,
    val orderType: String = "Limit",
    val qty: String,
    val price: String
)
