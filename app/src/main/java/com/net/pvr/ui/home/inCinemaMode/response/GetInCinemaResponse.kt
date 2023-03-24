package com.net.pvr.ui.home.inCinemaMode.response

data class GetInCinemaResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
)