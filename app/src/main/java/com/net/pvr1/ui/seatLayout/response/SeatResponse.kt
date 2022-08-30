package com.net.pvr1.ui.seatLayout.response

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
        val et: Int,
        val hc: Boolean,
        val hcm: String,
        val im: String,
        val imh: String,
        val iw: String,
        val mn: String,
        val priceList: PriceList,
        val rows: List<Row>,
        val sd: Boolean,
        val showId: Int,
        val st: String,
        val tnc: String,
        val transId: Any
    ):Serializable{
        data class PriceList(
            val `0000000006`: X0000000006,
            val `0000000007`: X0000000006,
            val `0000000008`: X0000000006
        ):Serializable{
            data class X0000000006(
                val description: String,
                val price: String,
                val priceCode: String
            ):Serializable
        }

        data class Row(
            val n: String,
            val s: List<S>,
            val t: String
        ):Serializable{

            data class S(
                val b: String,
                val bu: Boolean,
                val c: String,
                val cos: Boolean,
                val displaynumber: String,
                val en: Boolean,
                val hc: Boolean,
                val s: Int,
                val sn: String,
                val st: Int
            ):Serializable
        }
    }
}