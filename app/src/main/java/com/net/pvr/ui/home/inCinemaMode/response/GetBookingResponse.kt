package com.net.pvr.ui.home.inCinemaMode.response

data class GetBookingResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: BookingItem,
    val result: String,
    val version: Any
)