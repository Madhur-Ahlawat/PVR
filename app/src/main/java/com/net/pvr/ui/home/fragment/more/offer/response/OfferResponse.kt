package com.net.pvr.ui.home.fragment.more.offer.response

import java.io.Serializable

data class OfferResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):Serializable{
    data class Output(val offer: ArrayList<Offer>):Serializable
    data class Offer(
        val c: String,
        val description: String,
        val detailText: String,
        val howAvail: String,
        val i: String,
        val i1: String,
        val i2: String,
        val icon: String,
        val id: Int,
        val image: String,
        val imageUrl: String,
        val offerName: String,
        val oc: String,
        val ot: String,
        val priority: Int,
        val qt: String,
        val rurl: String,
        val otherLinkRedirectUrl: String,
        val source: String,
        val t: String,
        val tnc: String,
        val vf: String,
        val von: String,
        val vt: String
    ):Serializable
}