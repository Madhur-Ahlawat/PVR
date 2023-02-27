package com.net.pvr.ui.bookingSession.response

import java.io.Serializable

data class BookingTheatreResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):Serializable{
    data class Output(
        val m: List<M>
    ):Serializable{
        data class M(
            val i: String,
            val m: String,
            val n: String
        ):Serializable
    }
}