package com.net.pvr.ui.home.inCinemaMode.response

data class InCinemaFoodResp(
    val bookingId: String,
    val foods: List<Food>,
    val totalPrice: String
)