package com.net.pvr1.ui.login.response

import java.io.Serializable

data class LoginResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):Serializable{
    data class Output(
        val nu: String,
        val otp_require: String
    ):Serializable
}