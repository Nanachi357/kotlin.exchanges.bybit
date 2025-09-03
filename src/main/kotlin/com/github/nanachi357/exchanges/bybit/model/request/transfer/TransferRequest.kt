package com.github.nanachi357.exchanges.bybit.model.request.transfer

data class TransferRequest(
    val transferId: String,
    val coin: String,
    val amount: String,
    val fromMemberId: Long,
    val toMemberId: Long,
    val fromAccountType: String = "FUND",
    val toAccountType: String = "FUND"
)
