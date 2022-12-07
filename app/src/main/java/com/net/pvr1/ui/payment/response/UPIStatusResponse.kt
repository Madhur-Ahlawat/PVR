package com.net.pvr1.ui.payment.response

data class UPIStatusResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
) : java.io.Serializable {
    data class Output(
        val p: String
    ) : java.io.Serializable
}