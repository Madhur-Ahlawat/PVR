package com.net.pvr.ui.home.inCinemaMode.response

import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeHomeResponse

data class InCinemaResp(
    val audi: String,
    val bookingId: String,
    val ccode: String,
    val cinemaname: String,
    val format: String,
    val genre: Any,
    val inCinemaFoodResp: MutableList<InCinemaFoodResp>,
    val incinemaTypes: List<IncinemaType>,
    val lang: String,
    val mcensor: String,
    val mname: String,
    val movieImage: String,
    val placeholders: ArrayList<Placeholder>,
    val popups: List<Popup>,
    val seats: List<String>,
    val showData: List<ShowData>,
    val showtime: String,
    val st: ArrayList<PrivilegeHomeResponse.Output.St>
)