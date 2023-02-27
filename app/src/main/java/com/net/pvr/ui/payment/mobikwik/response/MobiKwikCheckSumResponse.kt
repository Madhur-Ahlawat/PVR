package com.net.pvr.ui.payment.mobikwik.response

data class MobiKwikCheckSumResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
){
    data class Output(
        val amount: String,
        val bookingid: String,
        val callingurl: String,
        val checksum: String,
        val merchantid: String,
        val merchantname: String,
        val redirecturl: String
    )
}