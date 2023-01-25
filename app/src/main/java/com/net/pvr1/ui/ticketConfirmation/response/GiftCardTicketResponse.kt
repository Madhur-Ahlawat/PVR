package com.net.pvr1.ui.ticketConfirmation.response

data class GiftCardTicketResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
){
    data class Output(
        val a: String,
        val c: String,
        val ch: String,
        val d: String,
        val da: String,
        val dc: String,
        val dcn: String,
        val dis: String,
        val dm: String,
        val fn: List<Fn>,
        val id: String,
        val imsg: String,
        val m: String,
        val oid: String,
        val p: String,
        val pm: String,
        val rem: String,
        val rm: String,
        val rn: String,
        val rnm: String,
        val t: String,
        val ta: String,
        val title: String,
        val total: String,
        val tp: String
    )
}