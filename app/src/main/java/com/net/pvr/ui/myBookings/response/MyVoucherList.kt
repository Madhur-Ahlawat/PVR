package com.net.pvr.ui.myBookings.response

data class MyVoucherList(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val ev: ArrayList<Any>
    )
}