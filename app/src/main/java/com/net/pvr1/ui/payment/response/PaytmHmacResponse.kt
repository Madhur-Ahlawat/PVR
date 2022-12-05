package com.net.pvr1.ui.payment.response

data class PaytmHmacResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val amount: String,
        val bookid: String,
        val callingurl: String,
        val cardlist: List<Any>,
        val channelId: String,
        val currency: String,
        val deepLink: String,
        val forwardurl: String,
        val hmackey: String,
        val mcc: String,
        val mid: String,
        val nblist: Nblist,
        val subcriptionid: String,
        val timer: Int,
        val txndate: Any,
        val vpa: String,
        val vpaname: String,
        val website: String
    ):java.io.Serializable{
        class Nblist
    }
}