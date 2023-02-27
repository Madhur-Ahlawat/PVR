package com.net.pvr.ui.myBookings.response

import java.io.Serializable

data class ParkingResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):Serializable{
    data class Output(
        val url: String,
        val date: String,
        val location_id: String,
        val time: String,
        val duration: String,
        val booking_id: String,
        val hmac: String,
        val partner_name: String
    )
}