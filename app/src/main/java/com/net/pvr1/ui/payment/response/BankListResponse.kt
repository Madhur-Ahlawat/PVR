package com.net.pvr1.ui.payment.response

data class BankListResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Map<String,String>,
    val result: String,
    val version: Any
)
