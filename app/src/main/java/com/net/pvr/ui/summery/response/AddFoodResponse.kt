package com.net.pvr.ui.summery.response

data class AddFoodResponse(
    val code: Int,
    val minversion: String,
    val msg: String,
    val output: Output,
    val result: String,
    val version: String
):java.io.Serializable{
    class Output(val bi:String,val tid:String, val pktransid:String){


    }
}