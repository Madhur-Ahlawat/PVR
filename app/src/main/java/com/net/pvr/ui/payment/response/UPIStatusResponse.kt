package com.net.pvr.ui.payment.response

data class UPIStatusResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
) : java.io.Serializable {
    data class Output(
        val p: String,
        val state: String,
        val msg: String,
        val mode: String,
        val banner_txt: String,
        val icon: String
    ) : java.io.Serializable
}