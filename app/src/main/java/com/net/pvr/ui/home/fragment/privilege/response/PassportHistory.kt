package com.net.pvr.ui.home.fragment.privilege.response

data class PassportHistory(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val can_redeem: Int,
        val cd: String,
        val renewDate: String="",
        val purchaseDate: String="",
        val purchaseAmount: String="",
        val expiryDate: String,
        val history: ArrayList<History>,
        val passCancelBtn: Boolean,
        val qr: String,
        val redeemed: Int,
        val subsription_status: String
    )
    data class History(
        val complexName: String,
        val redeemedDate: String,
        val status: String,
        val transRefId: Int
    )
}