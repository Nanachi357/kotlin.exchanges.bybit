package com.github.nanachi357.exchanges.bybit.model.account

import com.github.nanachi357.exchanges.bybit.model.account.sub.*

data class Account(
    val info: Info,
    val status: Status,
    val api: Api,
    val security: Security
)
