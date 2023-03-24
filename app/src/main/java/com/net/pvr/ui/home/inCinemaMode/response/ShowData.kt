package com.net.pvr.ui.home.inCinemaMode.response

data class ShowData(
    val Cinema_code: String,
    val before_interval_duration: Int,
    val before_movie_part: Int,
    val cinema_ip: String,
    val cinema_name: String,
    val date: String,
    val first_half_duration: Int,
    val interval_duration: Int,
    val intervel_arrival: String,
    val movie_end_datetime: String,
    val movie_name: String,
    val region: String,
    val screen_number: String,
    val second_half_duration: Int,
    val server_uuid: String,
    val show_date_time: String,
    val show_uuid: String
)