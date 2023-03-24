package com.net.pvr.ui.home.inCinemaMode.response

data class GetBookingResponse(
    val code: Int,
    val minversion: Int,
    val msg: String,
    val output: BookingItem,
    val result: String,
    val version: Int
)