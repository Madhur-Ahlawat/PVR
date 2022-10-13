package com.net.pvr1.ui.summery.response

data class AddFoodResponse(
    val code: Int,
    val minversion: String,
    val msg: String,
    val output: Output,
    val result: String,
    val version: String
):java.io.Serializable{
    class Output{

    }
}