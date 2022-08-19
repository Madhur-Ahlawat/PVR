package com.net.pvr1.ui.selectCity.response

import java.io.Serializable

data class SelectCityResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
): Serializable {
    data class Output(
        val cc: Cc,
        val nb: ArrayList<Nb>,
        val nc: ArrayList<Nc>,
        val ot: ArrayList<Ot>,
        val pc: ArrayList<Pc>
    ): Serializable {

        data class Cc(
            val image: String,
            val lat: String,
            val lng: String,
            val name: String,
            val parent: String,
            val popular: Boolean,
            val priority: Int,
            val radius: Int,
            val region: String,
            val subcities: String
        ) : Serializable

        data class Nb(
            val image: String,
            val lat: String,
            val lng: String,
            val name: String,
            val parent: String,
            val popular: Boolean,
            val priority: Int,
            val radius: Int,
            val region: String,
            val subcities: String
        ) : Serializable

        data class Nc(
            val image: String,
            val lat: String,
            val lng: String,
            val name: String,
            val parent: String,
            val popular: Boolean,
            val priority: Int,
            val radius: Int,
            val region: String,
            val subcities: String
        )

        data class Ot(
            val image: String,
            val lat: String,
            val lng: String,
            val name: String,
            val parent: String,
            val popular: Boolean,
            val priority: Int,
            val radius: Int,
            val region: String,
            val subcities: String
        ) : Serializable

        data class Pc(
            val image: String,
            val lat: String,
            val lng: String,
            val name: String,
            val parent: String,
            val popular: Boolean,
            val priority: Int,
            val radius: Int,
            val region: String,
            val subcities: String
        ) : Serializable
    }
}