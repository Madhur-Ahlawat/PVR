package com.net.pvr1.ui.myBookings.response

import java.io.Serializable

data class FoodTicketResponse(
    val code: Int,
    val minversion: String,
    val msg: String,
    val output: Output,
    val result: String,
    val version: String
) : Serializable {
    data class Output(
        val c: ArrayList<C>,
        val p: ArrayList<C>,
        val pb: ArrayList<Any>,
        val rm: Any
    ) : Serializable {
        data class C(
            val amount: String,
            val audi: String,
            val bd: String,
            val bi: String,
            val bio: String,
            val bnd: String,
            val bt: Boolean,
            val bund: String,
            val bvt1: String,
            val bvt2: String,
            val bvt3: String,
            val c: String,
            val ca: List<Any>,
            val ca_a: Boolean,
            val ca_d: String,
            val ca_msg: String,
            val ca_r: String,
            val cancelledseat: List<Any>,
            val cancelledseats: String,
            val cardUsedMsg: Any,
            val cbi: String,
            val cc: String,
            val cen: String,
            val cid: String,
            val cityId: Any,
            val cityName: Any,
            val conv: String,
            val ct: String,
            val cv: Boolean,
            val cvpv: String,
            val cvpvt: String,
            val dit: Boolean,
            val dt: Any,
            val eticket: Boolean,
            val extraAmt: Any,
            val extraAmtMsg: Any,
            val f: List<P.F>,
            val fa: Boolean,
            val fc: Int,
            val fcmsg: String,
            val feed: Any,
            val fl: String,
            val fmsg: String,
            val fmt: String,
            val food: List<Any>,
            val fs: String,
            val fst: Int,
            val ft: String,
            val g: List<Any>,
            val hy: Boolean,
            val hym: String,
            val im: String,
            val imh: String,
            val is_only_fd: Boolean,
            val `it`: String,
            val lg: String,
            val lngt: String,
            val ltd: String,
            val m: String,
            val mc: String,
            val md: String,
            val mdt: Any,
            val mf: List<Any>,
            val mn: String,
            val modification: Boolean,
            val ms: String,
            val parking: Boolean,
            val parkbooking: Boolean,
            val nf: Boolean,
            val ptext: String,
            val part: String,
            val partialCancellationAllowed: Boolean,
            val pe: Any,
            val pf: Boolean,
            val ph: List<Any>,
            val preDetails: List<Any>,
            val pttr: Any,
            val pu: List<Any>,
            val qr: String,
            val qrc: String,
            val resch: String,
            val s_ca: String,
            val seat: List<Any>,
            val seatChangeAllowed: Boolean,
            val seats: String,
            val showChangeAllowed: Boolean,
            val sp: Any,
            val st: String,
            val sto: Any,
            val t: String,
            val tax: String,
            val tim: String,
            val tp: Any,
            val tu: String,
            val tv: Any,
            val typ: String,
            val vban: Any,
            val voucherMsg: Any,
            val vs: List<Any>,
            val vt: String
        ) : Serializable

        data class P(
            val amount: String,
            val audi: String,
            val bd: String,
            val bi: String,
            val bio: String,
            val bnd: String,
            val bt: Boolean,
            val bund: String,
            val bvt1: String,
            val bvt2: String,
            val bvt3: String,
            val c: String,
            val ca: List<Any>,
            val ca_a: Boolean,
            val ca_d: String,
            val ca_msg: String,
            val ca_r: String,
            val cancelledseat: List<Any>,
            val cancelledseats: String,
            val cardUsedMsg: Any,
            val cbi: String,
            val cc: String,
            val cen: String,
            val cid: String,
            val cityId: Any,
            val cityName: String,
            val conv: String,
            val ct: String,
            val cv: Boolean,
            val cvpv: String,
            val cvpvt: String,
            val dit: Boolean,
            val dt: String,
            val eticket: Boolean,
            val extraAmt: Any,
            val extraAmtMsg: Any,
            val f: List<F>,
            val fa: Boolean,
            val fc: Int,
            val fcmsg: String,
            val feed: Any,
            val fl: String,
            val fmsg: String,
            val fmt: String,
            val food: List<Any>,
            val fs: String,
            val fst: Int,
            val ft: String,
            val g: List<Any>,
            val hy: Boolean,
            val hym: String,
            val im: String,
            val imh: String,
            val is_only_fd: Boolean,
            val `it`: String,
            val lg: String,
            val lngt: String,
            val ltd: String,
            val m: String,
            val mc: String,
            val md: String,
            val mdt: Any,
            val mf: List<Any>,
            val mn: String,
            val modification: Boolean,
            val ms: String,
            val nf: Boolean,
            val part: String,
            val partialCancellationAllowed: Boolean,
            val pe: Any,
            val pf: Boolean,
            val ph: List<Any>,
            val preDetails: List<Any>,
            val pttr: Any,
            val pu: List<Any>,
            val qr: String,
            val qrc: String,
            val resch: String,
            val s_ca: String,
            val seat: List<Seat>,
            val seatChangeAllowed: Boolean,
            val seats: String,
            val showChangeAllowed: Boolean,
            val sp: Any,
            val st: String,
            val sto: String,
            val t: String,
            val tax: String,
            val tim: String,
            val tp: Any,
            val tu: String,
            val tv: Any,
            val typ: String,
            val vban: Any,
            val voucherMsg: Any,
            val vs: List<Any>,
            val vt: String
        ) : Serializable {
            data class Seat(
                val c: Int,
                val cp: String,
                val `it`: List<Any>,
                val n: String,
                val v: String
            ) : Serializable
            data class F(
                val c: Int,
                val cp: String,
                val `it`: List<Any>,
                val n: String,
                val v: String
            ):Serializable
        }
    }
}