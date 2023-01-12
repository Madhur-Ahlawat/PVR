package com.net.pvr1.ui.cinemaSession.cinemaDetails.response

data class CinemaDetailsResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val AQI_IN: Any,
        val AQI_OUT: Any,
        val add: String,
        val chf: String,
        val cn: String,
        val csc: String,
        val dis: String,
        val hc: String,
        val hsc: String,
        val lat: String,
        val lng: String,
        val pi: List<Any>,
        val rsc: String
    )
}