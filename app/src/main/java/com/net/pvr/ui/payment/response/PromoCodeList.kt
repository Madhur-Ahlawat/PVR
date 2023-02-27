package com.net.pvr.ui.payment.response

data class PromoCodeList(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: ArrayList<Output>,
    val result: String,
    val version: Any
){
    data class Output(
        val category: String,
        val image: String,
        val promocode: String,
        val title: String,
        val tnc: String
    )
}