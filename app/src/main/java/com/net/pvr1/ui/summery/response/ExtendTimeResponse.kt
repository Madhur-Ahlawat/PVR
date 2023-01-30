package com.net.pvr1.ui.summery.response

data class ExtendTimeResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val at: Int,
        val et: Int
    ):java.io.Serializable
}