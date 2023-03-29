package com.net.pvr.ui.home.inCinemaMode.response

import com.net.pvr.ui.home.fragment.home.response.HomeResponse

data class Output(
    val bookingIdList: MutableList<String>,
    val count: Int,
    val `data`: List<Data>,
    val inCinemaResp: InCinemaResp
)