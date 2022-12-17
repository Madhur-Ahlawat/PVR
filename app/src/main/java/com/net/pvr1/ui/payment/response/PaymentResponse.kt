package com.net.pvr1.ui.payment.response

data class PaymentResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
) : java.io.Serializable {
    data class Output(
        val binoffers: List<Binoffer>,
        val ca_a: Boolean,
        val dc: Boolean,
        val dcinfo: String,
        val gateway: ArrayList<Gateway>,
        val offers: ArrayList<Offer>
    ) : java.io.Serializable {
        data class Binoffer(
            val c: String,
            val ca_a: Boolean,
            val ca_t: String,
            val id: String,
            val imageUrl: String,
            val name: String,
            val promomap: Any,
            val pty: String,
            val scheme: String,
            val tc: String
        ) : java.io.Serializable

        data class Gateway(
            val c: String,
            val ca_a: Boolean,
            val ca_t: String,
            val id: String,
            val imageUrl: String,
            val name: String,
            val promomap: ArrayList<com.net.pvr1.ui.payment.response.Promomap>,
            val pty: String,
            val scheme: String,
            val tc: String
        ) : java.io.Serializable

        data class Offer(
            val c: String,
            val ca_a: Boolean,
            val ca_t: String,
            val id: String,
            val imageUrl: String,
            val name: String,
            val promomap: List<Promomap>,
            val pty: String,
            val scheme: String,
            val tc: String
        ) : java.io.Serializable

        data class Promomap(
            val key: String,
            val value: String
        ) : java.io.Serializable
    }
}