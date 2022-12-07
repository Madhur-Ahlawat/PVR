package com.net.pvr1.ui.payment.response

data class PhonepeHmacResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
){
    data class Output(
        val api: String,
        val bi: Any,
        val bs: String,
        val ch: String,
        val url: String
    )
}