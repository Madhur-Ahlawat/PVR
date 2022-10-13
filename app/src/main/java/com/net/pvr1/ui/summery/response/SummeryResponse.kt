package com.net.pvr1.ui.summery.response

data class SummeryResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val a: String,
        val audi: String,
        val bd: String,
        val bi: String,
        val bnd: String,
        val bund: String,
        val bvt1: String,
        val bvt2: String,
        val bvt3: String,
        val c: String,
        val ca: List<Any>,
        val ca_a: Boolean,
        val ca_rf1: CaRf1,
        val ca_rf2: CaRf1,
        val ca_tm: String,
        val cc: String,
        val cen: String,
        val corp: Boolean,
        val corpde: Any,
        val cs: Boolean,
        val cv: Boolean,
        val dit: Boolean,
        val don: Int,
        val don_stext: String,
        val don_text: String,
        val donation: String,
        val dt: Any,
        val extraAmt: Any,
        val extraAmtMsg: Any,
        val f: List<F>,
        val fc: Int,
        val ffo: Boolean,
        val fmt: String,
        val food: List<Any>,
        val fs: String,
        val ft: String,
        val im: String,
        val imh: String,
        val ins: Int,
        val inst: String,
        val instxt: Any,
        val is_only_fd: Boolean,
        val `it`: String,
        val lg: String,
        val m: String,
        val mc: String,
        val md: String,
        val mdt: Any,
        val mn: String,
        val ms: String,
        val pe: String,
        val pmsg: String,
        val pp: Any,
        val preDetails: List<Any>,
        val pttr: Any,
        val rd: String,
        val seat: List<Seat>,
        val seats: String,
        val sp: Any,
        val st: String,
        val t: String,
        val tid: String,
        val tim: String,
        val typ: String,
        val vs: List<Any>,
        val vt: String
    ):java.io.Serializable{
        data class Seat(
            val c: Int,
            val cp: String,
            val `it`: List<Any>,
            val n: String,
            val v: String
        ):java.io.Serializable
        data class F(
            val c: Int,
            val cp: String,
            val `it`: List<It>,
            val n: String,
            val v: String
        ):java.io.Serializable{
            data class It(
                val c: Int,
                val cp: String,
                val `it`: List<Any>,
                val n: String,
                val v: String
            ):java.io.Serializable
        }
        data class CaRf1(
            val c: Int,
            val cp: String,
            val `it`: List<Any>,
            val n: String,
            val v: String
        ):java.io.Serializable
    }
}