package com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response

import java.io.Serializable

data class BookingRetrievalResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
) : Serializable {
    data class Output(
        val c: ArrayList<C>,
        val ph: ArrayList<Any>,
        val pu: ArrayList<Any>
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
            val me: Boolean,
            val mo: String,
            val n: String,
            val ns: List<Any>,
            val par: Boolean,
            val sc: Int,
            var itemSelectedOrNot: Boolean = false
        ) : Serializable

        data class Child(
            val ccid: String,
            val ccn: String
        ) : Serializable

        data class M(
            val i: String,
            val m: String,
            val n: String
        ) : java.io.Serializable
    }
}