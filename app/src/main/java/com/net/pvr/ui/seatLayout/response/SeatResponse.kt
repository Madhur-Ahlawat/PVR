package com.net.pvr.ui.seatLayout.response

import java.io.Serializable

data class SeatResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):Serializable{
    data class Output(
        val at: Int,
        val bm: String,
        val bu: Boolean,
        val ca_a: Boolean,
        val ca_bm: String,
        val ca_tm: String,
        val cinemaCode: String,
        val cn: String,
        val dit: Boolean,
        val et: String,
        val hc: Boolean,
        val hcm: String,
        val im: String,
        val imh: String,
        val iw: String,
        val mn: String,
        val priceList:Map<String, PriceList.Price>,
        val rows: ArrayList<Row>,
        val sd: Boolean,
        val showId: Int,
        val st: String,
        val tnc: String,
        val transId: Any
    ):Serializable{
        data class PriceList(
            val `0000000006`: Price,
            val `0000000007`: Price,
            val `0000000008`: Price
        ):Serializable{
            data class Price(
                val description: String,
                val price: String,
                val priceCode: String
            ):Serializable
        }

        data class Row(
            val n: String,
            val s: List<S>,
            val t: String,
            val c: String="",
        ):Serializable{

            data class S(
                val b: String,
                val bu: Boolean,
                val c: String,
                val cos: Boolean,
                val displaynumber: String,
                val en: Boolean,
                val hc: Boolean,
                var s: Int,
                val sn: String,
                val st: Int
            ):Serializable
        }
    }
}