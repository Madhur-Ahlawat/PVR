package com.net.pvr.ui.seatLayout.response

data class ReserveSeatResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val bookingid: String,
        val fc: String,
        val nf: String
    )
}