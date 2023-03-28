package com.net.pvr.ui.home.inCinemaMode.response

data class InCinemaHomeResponse(
    val code: Int,
    val minversion: Int,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Int
)