package com.net.pvr.ui.home.fragment.more.offer.offerDetails.response

data class OfferDetailsResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val btn: String,
        val c: String,
        val d: String,
        val i: String,
        val i2: String,
        val id: String,
        val l: String,
        val pid: String,
        val promo: String,
        val qt: String,
        val rurl: String,
        val t: String,
        val vf: String,
        val von: String,
        val vt: String
    ):java.io.Serializable
}