package com.net.pvr1.ui.payment.response

data class LoyaltyVocherApply(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
){
    data class Output(
        val am: String,
        val di: String,
        val ft: String,
        val `it`: String,
        val msg: String,
        val p: String,
        val txt: String
    )
}