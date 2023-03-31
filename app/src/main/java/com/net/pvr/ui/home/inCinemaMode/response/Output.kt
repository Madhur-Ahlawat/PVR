package com.net.pvr.ui.home.inCinemaMode.response

data class Output(
    val bookingIdList: List<String>,
    val count: Int,
    val `data`: List<Data>,
    val inCinemaResp: InCinemaResp
)