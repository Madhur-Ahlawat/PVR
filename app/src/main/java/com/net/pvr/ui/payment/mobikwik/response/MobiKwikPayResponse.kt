package com.net.pvr.ui.payment.mobikwik.response

data class MobiKwikPayResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
){
    data class Output(
        val messagecode: String,
        val status: String,
        val statuscode: String,
        val statusdescription: String
    )
}