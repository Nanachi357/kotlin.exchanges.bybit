package com.github.nanachi357.exchanges.bybit.model.response.order

data class Orders(
    val category: String? = null,
    val list: List<BybitOrder>? = null,
    val nextPageCursor: String? = null
)
