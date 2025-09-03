package com.github.nanachi357.exchanges.bybit.model.response.deposit

import java.math.BigDecimal

data class DepositTransaction(
    val coin: String? = null,
    val chain: String? = null,
    val amount: BigDecimal? = null,
    val txID: String? = null,
    val status: Int? = null,
    val toAddress: String? = null,
    val tag: String? = null,
    val depositFee: BigDecimal? = null,
    val successAt: String? = null,
    val confirmations: String? = null,
    val txIndex: String? = null,
    val blockHash: String? = null,
    val batchReleaseLimit: String? = null,
    val depositType: String? = null
)
