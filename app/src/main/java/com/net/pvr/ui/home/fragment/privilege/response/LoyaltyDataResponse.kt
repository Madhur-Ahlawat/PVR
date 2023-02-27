package com.net.pvr.ui.home.fragment.privilege.response

import java.util.*
import kotlin.collections.ArrayList

data class LoyaltyDataResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
){
    data class Output(
        val ct: String,
        val ex: String,
        val his: ArrayList<Hi>,
        val pt: String,
        val qr: String,
        val qrcard: String,
        val st: String,
        val stf: String,
        val subscription: Subscription,
        val sync_time: String,
        val vou: List<Vou>
    )

    data class Hi(
        val b: Boolean,
        val boxOfficeValue: String,
        val cinema: String,
        val concessionsValue: String,
        val dd: String,
        val l1: String,
        val l2: String,
        val pointsEarned: String,
        val pointsRedeemed: String,
        val spent_type: String,
        val transactionId: Int,
        val transactionTime: String,
        val transaction_POStransactionId: Int,
        val transaction_cardEntry: String,
        val transaction_cardNumber: String,
        val transaction_id: Int,
        val transaction_workstationID: String,
        val ty: String
    )

    data class Subscription(
        val can_redeem: Int,
        val cd: String,
        val expiryDate: String,
        val qr: String,
        val redeemed: Int,
        val subsription_status: String
    )
    data class Vou(
        val acp: String,
        val cp: String,
        val nm: String,
        val tp: String,
        val vouchers: ArrayList<Voucher>
    )

    data class Voucher(
        val TransScheme_strCtrlMsr: String,
        val amt: Int,
        val canRedeemCount: Int,
        val cd: String,
        val cin: String,
        val ex: String,
        val exf: String,
        val expiryDate: String,
        val expDate: Date = com.net.pvr.utils.Constant.getPrivilegeDate("MMM dd, yyyyy",exf)!!,
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
        val voucher_qr: String,
        val voucher_type: String
    )
}