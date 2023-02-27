package com.net.pvr.ui.login.otpVerify.response

import java.io.Serializable

data class ResisterResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):Serializable{
    data class Output(
        val cid: String,
        val cn: String,
        val doa: Any,
        val dob: String,
        val em: String,
        val gd: String,
        val id: String,
        val im: String,
        val ls: String,
        val ms: String,
        val ph: String,
        val pp: String,
        val token: String,
        val un: String,
        val wopt: String
    ):Serializable
}