package com.net.pvr.ui.search.searchHome.response

import java.io.Serializable

data class HomeSearchResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
) : Serializable {
    data class Output(
        val lr: List<Any>,
        var m: ArrayList<M>,
        val t: ArrayList<T>
    ) : Serializable {
        data class T(
            val ad: String,
            val c: String,
            val desc: String,
            val distance: Int,
            val genre: String,
            val id: String,
            val im: String,
            val iwt: String,
            val l: String,
            val lat: String,
            val length: String,
            val lng: String,
            val mid: String,
            val n: String,
            val n2: String,
            val t: String
        ) : Serializable
        data class M(
            val ad: String,
            val c: String,
            val desc: String,
            val distance: Int,
            val genre: String,
            val id: String,
            val im: String,
            val iwt: String,
            val l: String,
            val lat: String,
            val length: String,
            val lng: String,
            val mid: String,
            val n: String,
            val n2: String,
            val t: String
        ) : Serializable
    }
}