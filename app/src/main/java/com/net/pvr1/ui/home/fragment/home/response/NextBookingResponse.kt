package com.net.pvr1.ui.home.fragment.home.response

data class NextBookingResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Any,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val a: Boolean,
        val audi: String,
        val bvt: String,
        val bvtt: String,
        val cla: String,
        val cln: String,
        val cn: String,
        val d: String,
        val mn: String,
        val pos: String,
        val qr: String,
        val qrt: String,
        val sd: String,
        val seat: String,
        val st: String,
        val tim: String,
        val type: String
    ):java.io.Serializable
}