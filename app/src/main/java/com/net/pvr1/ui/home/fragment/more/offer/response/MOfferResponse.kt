package com.net.pvr1.ui.home.fragment.more.offer.response

data class MOfferResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val offers: ArrayList<Offer>,
        val ph: ArrayList<Ph>,
        val pu: List<Pu>
    ):java.io.Serializable{
        data class Ph(
            val buttonText: String,
            val cinemas: String,
            val cities: String,
            val formats: String,
            val i: String,
            val id: Int,
            val location: String,
            val movies: String,
            val name: String,
            val platform: String,
            val priority: Int,
            val redirectView: String,
            val redirect_url: String,
            val screen: String,
            val text: String,
            val trailerUrl: String,
            val type: String
        ):java.io.Serializable

        data class Pu(
            val buttonText: String,
            val cinemas: String,
            val cities: String,
            val formats: String,
            val i: String,
            val id: Int,
            val location: String,
            val movies: String,
            val name: String,
            val platform: String,
            val priority: Int,
            val redirectView: String,
            val redirect_url: String,
            val screen: String,
            val text: String,
            val trailerUrl: String,
            val type: String
        ):java.io.Serializable

        data class Offer(
            val btn: String,
            val c: String,
            val catp: String,
            val description: String,
            val detailText: String,
            val howAvail: String,
            val i: String,
            val i1: String,
            val i2: String,
            val icon: String,
            val id: Int,
            val image: String,
            val oc: String,
            val ot: String,
            val pid: String,
            val priority: Int,
            val promo: String,
            val qt: String,
            val rurl: String,
            val source: String,
            val t: String,
            val tnc: String,
            val vf: String,
            val von: String,
            val vt: String
        ):java.io.Serializable
    }
}