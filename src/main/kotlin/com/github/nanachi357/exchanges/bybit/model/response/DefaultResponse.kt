package com.github.nanachi357.exchanges.bybit.model.response

data class DefaultResponse<T>(
    val retCode: Int? = null,
    val retMsg: String? = null,
    val result: T? = null,
    val retExtInfo: Any? = null,
    val time: Long? = null
)
