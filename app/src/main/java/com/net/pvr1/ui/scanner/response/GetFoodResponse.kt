package com.net.pvr1.ui.scanner.response

data class GetFoodResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: List<Output>,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val fout: String,
        val foutimg: Any
    ):java.io.Serializable
}