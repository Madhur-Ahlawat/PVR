package com.net.pvr1.ui.home.fragment.privilege.response

data class PassportPlanResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
){
    data class Output(
        val scheme: ArrayList<Scheme>,
        val selectedScheme: Any
    )
    data class Scheme(
        val cinema: String,
        val city: String,
        val cprice: String,
        val discription: Any,
        val duration: String,
        val expirydays: String,
        val info: String,
        val itemcode: String,
        val maxtrycount: String,
        val price: String,
        val retrymsgone: String,
        val retrymsgtwo: String,
        val schemeid: String,
        val schemename: String,
        val subsplan: String,
        val text: String,
        val vouchercount: String
    )
}