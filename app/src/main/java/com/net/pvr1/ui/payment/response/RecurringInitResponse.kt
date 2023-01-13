package com.net.pvr1.ui.payment.response

data class RecurringInitResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
) : java.io.Serializable {
    data class Output(
        val token: String,
        val subscriptionid: String,
        val msg: String,
        val tf: String,
        val ts: String,
        val createdAt: String

    ) : java.io.Serializable
}