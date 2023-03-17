package com.net.pvr.ui.myBookings.response

import java.io.Serializable

data class GiftCardResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):Serializable{
    data class Output(
        val gc: List<Gc>
    ):Serializable{
        data class Gc(
            val c: String,
            val d: String,
            val dn: String,
            val id: String,
            val ci: String,
            val gi: String,
            val r: String,
            val t: Any,
            val ta: String,
            val tan: String,
            val tn: String
        ):Serializable
    }
}