package com.net.pvr1.ui.seatLayout.request

data class ReserveSeatRequest(
    val cinemacode: String,
    val seats: ArrayList<Seat>,
    val sessionid: String,
    val transid: String
) {

    data class Seat(
        val priceCode: String,
        val seatBookingId: String
    )

}