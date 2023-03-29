package com.net.pvr.ui.home.inCinemaMode.response

import com.net.pvr.ui.home.fragment.home.response.HomeResponse

data class BookingItem(
    val audi: String,
    val cinemaname: String,
    val format: String,
    val genre: Any,
    val inCinemaFoodResp: List<InCinemaFoodResp>,
    val incinemaTypes: MutableList<IncinemaType>,
    val lang: String,
    val ccode: String,
    val mcensor: String,
    val mname: String,
    val movieImage: String,
    val placeholders: ArrayList<HomeResponse.Ph>,
    val popups: List<Any>,
    val seats: List<String>,
    val showData: List<ShowData>,
    val showtime: String
)