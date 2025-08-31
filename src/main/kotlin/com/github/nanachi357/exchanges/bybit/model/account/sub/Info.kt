package com.github.nanachi357.exchanges.bybit.model.account.sub

data class Info(
    val uid: String,
    val email: String,
    val platform: String,
    val note: String? = null
)
