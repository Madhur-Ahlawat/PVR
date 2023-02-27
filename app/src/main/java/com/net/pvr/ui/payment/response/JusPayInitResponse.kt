package com.net.pvr.ui.payment.response

data class JusPayInitResponse(
    val code: Int,
    val minversion: String,
    val msg: String,
    val output: Output,
    val result: String,
    val version: String
){
    data class Output(
        val client_auth_token: String,
        val client_id: String,
        val customer_id: String,
        val list_cards: List<Any>,
        val merchant_id: String
    )
}