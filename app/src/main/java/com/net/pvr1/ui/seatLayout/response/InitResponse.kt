package com.net.pvr1.ui.seatLayout.response

data class InitResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val at: String,
        val et: String,
        val transid: String
    )
}