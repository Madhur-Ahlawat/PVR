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
        val bal: String,
        val industry_type: String,
        val paytm_website: String,
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
        val bookingid: String,
        val cust_id: String,
        val nblist: Map<String,String>,
        val subcriptionid: String,
        val timer: Int,
        val txndate: Any,
        val vpa: String,
        val vpaname: String,
        val p: Boolean,
        val creditCardOnly: Boolean,
        val di: String,
        val txt: String,
        val bin: String,
        val website: String
    ):java.io.Serializable{
        data class Nblist(
            val ANDB: String,
            val AXIS: String,
            val BHARAT: String,
            val BOB: String,
            val CANARA: String,
            val CBI: String,
            val CITIUB: String,
            val CORP: String,
            val COSMOS: String,
            val CSB: String,
            val DCB: String,
            val DENA: String,
            val GPPB: String,
            val HDFC: String,
            val ICICI: String,
            val IDBI: String,
            val IDFC: String,
            val INDS: String,
            val IOB: String,
            val JKB: String,
            val KTKB: String,
            val LVB: String,
            val NKMB: String,
            val OBPRF: String,
            val PNB: String,
            val PSB: String,
            val RATN: String,
            val SBI: String,
            val SIB: String,
            val STB: String,
            val UBI: String,
            val UCO: String,
            val UNI: String,
            val VJYA: String,
            val YES: String
        )
    }
}