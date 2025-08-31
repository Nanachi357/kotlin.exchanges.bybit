package com.github.nanachi357.exchanges.bybit.model.response.public_api
data class LotSizeFilter(
    val basePrecision: String? = null,
    val quotePrecision: String? = null,
    val minOrderQty: String? = null,
    val maxOrderQty: String? = null,
    val minOrderAmt: String? = null,
    val maxOrderAmt: String? = null
)
