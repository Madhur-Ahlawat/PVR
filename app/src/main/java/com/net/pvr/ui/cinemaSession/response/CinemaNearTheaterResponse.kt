package com.net.pvr.ui.cinemaSession.response

import java.io.Serializable

data class CinemaNearTheaterResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):Serializable{
    data class Output(
        val c: List<C>
    ):Serializable{
        data class C(
            val ad: String,
            val cId: Int,
            val d: String,
            val imob: String,
            val iwb: List<String>,
            val iwt: String,
            val lang: String,
            val lat: String,
            val mc: String,
            val n: String,
            val sc: Int
        )
    }
}