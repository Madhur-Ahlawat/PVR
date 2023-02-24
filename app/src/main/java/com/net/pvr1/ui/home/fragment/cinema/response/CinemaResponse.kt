package com.net.pvr1.ui.home.fragment.cinema.response

import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import java.io.Serializable

data class CinemaResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
) : Serializable {
    data class Output(
        val c: List<C>,
        val ph: ArrayList<HomeResponse.Ph>,
        val pu: ArrayList<Pu>
    ) : Serializable {
        data class C(
            val ad: String,
            val cId: Int,
            val childs: List<Child>,
            val d: String,
            val e: String,
            val fo: Boolean,
            val hc: Boolean,
            val imob: String,
            val iwb: List<String>,
            val iwt: String,
            val l: String,
            val lang: String,
            val lat: String,
            val m: List<M>,
            val mc: String,
            val newCinemaText: String,
            val me: Boolean,
            val mo: String,
            val n: String,
            val ns: List<Any>,
            val par: Boolean,
            val sc: Int
        ) : Serializable {
            data class Child(
                val ccid: String,
                val ccn: String
            ) : Serializable

            data class M(
                val i: String,
                val m: String,
                val n: String
            ) : Serializable
        }
        data class Pu(
            val buttonText: String,
            val cities: String,
            val i: String,
            val id: Int,
            val location: String,
            val name: String,
            val platform: String,
            val priority: Int,
            val redirect_url: String,
            val redirectView: String,
            val screen: String,
            val trailerUrl: String,
            val type: String
        ) : Serializable

    }
}