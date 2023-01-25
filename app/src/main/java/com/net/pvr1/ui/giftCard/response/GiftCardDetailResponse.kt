package com.net.pvr1.ui.giftCard.response

data class GiftCardDetailResponse(
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
        val cdm: String,
        val ch: String,
        val da: String,
        val dc: String,
        val dcn: String,
        val dis: String,
        val dm: String,
        val edm: String,
        val em: String,
        val fn: List<Fn>,
        val img: String,
        val imsg: String,
        val kd: String,
        val kpd: String,
        val m: String,
        val p: String,
        val pm: String,
        val pp: Any,
        val pv: Boolean,
        val q: String,
        val rn: String,
        val sd: Boolean,
        val t: String,
        val ta: String,
        val tan: String,
        val title: String,
        val tp: String
    )
    data class Fn(
        val c: Int,
        val cp: String,
        val `it`: List<Any>,
        val n: String,
        val v: String
    )
}