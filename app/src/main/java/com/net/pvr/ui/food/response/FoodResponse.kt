package com.net.pvr.ui.food.response

import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import java.io.Serializable

data class FoodResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
) : Serializable {
    data class Output(
        val aqt: Int,
        val banners: ArrayList<Banners>,
        val bestsellers: ArrayList<Bestseller>,
        val c: String,
        val cat: ArrayList<Cat>,
        val cn: String,
        val fimg: String,
        val h1: String,
        val h2: String,
        val ins: Boolean,
        val mfl: ArrayList<Mfl>,
        val na: String,
        val nams: String,
        val pastfoods: String,
        val ph: ArrayList<HomeResponse.Ph>,
        val pu: ArrayList<BookingResponse.Output.Pu>,
        val ra: Boolean,
        val st: String,
        val typ: String
    ) : Serializable {
        data class Bestseller(
            val bestseller: Boolean,
            val cid: Int,
            val ctn: String,
            val dp: Int,
            val mi: String,
            val mid: Int,
            val miw: String,
            var qt: Int,
            val nm: String,
            val op: Int,
            val r: List<R>,
            val veg: Boolean
        ) : Serializable {
            data class R(
                val ct: String,
                val dis: String,
                val dp: Int,
                val fout: String,
                val foutimg: Any,
                val h: String,
                val ho: String,
                val i: String,
                val id: Int,
                val iw: String,
                val masterItemId: Int,
                val op: Int,
                var qt: Int,
                val sf: Boolean,
                val wt: String,
                val en: String,
                val fa: String,
                val si: String,
                val veg: Boolean
            ) : Serializable
        }


        data class Cat(
            val i: String,
            val name: String,
            val priority: Int
        ) : Serializable

        data class Mfl(
            val bestseller: Boolean,
            val cid: Int,
            val ctn: String,
            val dp: Int,
            val mi: String,
            val mid: Int,
            val miw: String,
            val nm: String,
            val op: Int,
            var qt: Int,
            val r: ArrayList<Bestseller.R>,
            val veg: Boolean
        ) : Serializable


        data class Banners(
            val categoryId: Int,
            val name: String,
            val categoryName: String,
            val itemId: String,
            val url: String,
            val type: String,
            val i: String,
            var priority: Int,
            var validto: Long,
            var validfrom: Long
        ) : Serializable
    }
}