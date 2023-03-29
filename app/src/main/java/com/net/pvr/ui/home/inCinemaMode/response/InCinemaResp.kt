package com.net.pvr.ui.home.inCinemaMode.response

import com.net.pvr.ui.home.fragment.home.response.HomeResponse

data class InCinemaResp(
    val audi: String,
    val cinemaname: String,
    val format: String,
    val genre: Any,
    val inCinemaFoodResp: MutableList<InCinemaFoodResp> = mutableListOf(),
    val incinemaTypes: MutableList<IncinemaType> = mutableListOf(),
    val lang: String,
    val mcensor: String,
    val placeholders: ArrayList<HomeResponse.Ph>,
    val popups: ArrayList<HomeResponse.Ph>,
    val mname: String,
    val showData: Any,
    val showtime: String,
    val seats: MutableList<String> = mutableListOf()
)