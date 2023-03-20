package com.net.pvr.ui.home.fragment.more.eVoucher.response

data class VoucherListResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
) : java.io.Serializable {
    data class Output(
        val ev: ArrayList<Ev>) : java.io.Serializable {
        data class Ev(
            val binDiscountId: String,
            val binDiscountName: String,
            var quantity: Int,
            val descNTnc: String,
            val endDate: Long,
            val imageUrl1: String,
            val imageUrl2: String,
            val imageUrl3: String,
            val pkBinDiscountId: Int,
            val sellAllowedCPValue: String,
            val sellAllowedCount: Int,
            val sellAllowedValue: Int,
            val shortDesc: String,
            val startDate: Long,
            val tncUrl: String,
            val voucherCategory: String
        ) : java.io.Serializable
    }
}