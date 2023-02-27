package com.net.pvr.ui.splash.response

data class SplashResponse(
    val code: Int,
    val minversion: String,
    val msg: String,
    val output: Output,
    val result: String,
    val version: String
):java.io.Serializable{
    data class Output(
        val ar: String,
        val au: String,
        val cc: String,
        val cmp: String,
        val donation: String,
        val fs: String,
        val hb: String,
        val hf: String,
        val hl: String,
        val hm: String,
        val ls: String,
        val lsm: String,
        val lus: String,
        val ly: String,
        val md: String,
        val mlib: String,
        val nt: String,
        val ntb: String,
        val ntbn: String,
        val ps: String,
        val pvr_tc: String,
        val rb: String,
        val rbc: String,
        val rf: String,
        val rm: String,
        val sp: String,
        val svm: String,
        val wf: String
    ):java.io.Serializable
}