package com.net.pvr.ui.home.inCinemaMode.response

data class InCinemaFoodResp(
    var isExpanded:Boolean = false,
    val bookingId: String,
    val foods: List<Food>,
    val totalPrice: String
)