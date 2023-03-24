package com.net.pvr.ui.home.inCinemaMode.response

data class BookingItem(
    val audi: String,
    val cinemaname: String,
    val format: String,
    val genre: String,
    val inCinemaFoodResp: MutableList<InCinemaFoodResp>,
    val incinemaTypes: MutableList<IncinemaType>,
    val lang: String,
    val mcensor: String,
    val mname: String,
    val showData: ShowData,
    val showtime: String,
    val seats: MutableList<String> = mutableListOf()
)