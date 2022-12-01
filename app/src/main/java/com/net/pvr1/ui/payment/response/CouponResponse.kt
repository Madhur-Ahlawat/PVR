package com.net.pvr1.ui.payment.response

data class CouponResponse(
    val code: Int,
    val msg: String,
    val output: ArrayList<Output>,
    val result: String
) : java.io.Serializable {
    data class Output(
        val acp: String,
        val cp: String,
        val nm: String,
        val tp: String,
        val vouchers: List<Voucher>
    ) : java.io.Serializable {
        data class Voucher(
            val TransScheme_strCtrlMsr: String,
            val amt: Int,
            val canRedeemCount: Int,
            val cd: String,
            val cin: String,
            val display_value: Boolean,
            val ex: String,
            val exf: String,
            val expiryDate: String,
            val image_path: String,
            val info: String,
            val itd: String,
            val itn: String,
            val new_voucher_type: String,
            val nm: String,
            val qr: String,
            val redeemedCount: Int,
            val sc: Int,
            val scheme_id: String,
            val st: String,
            val stcp: String,
            val tp: String,
            val tpt: String,
            val type: String,
            val usd: String,
            val voucher_history: List<Any>,
            val voucher_qr: String,
            val voucher_type: String
        ) : java.io.Serializable
    }
}