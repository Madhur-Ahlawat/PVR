package com.net.pvr.ui.giftCard.response

data class ActiveGCResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
){
    data class Output(
        val gca: ArrayList<Gca>,
        val gci: ArrayList<Gca>,
        val limit: String
    )

    data class Gca(
        val al: String,
        val c: String,
        val ccf: String,
        val ch: String,
        val ci: String,
        val gi: String,
        val cia: Boolean,
        val cim: String,
        val msg: String,
        val cn: Any,
        val ctg: Boolean,
        val customImageApprove: Boolean,
        val d: String,
        val dn: String,
        val gcn: String,
        val id: String,
        val r: String,
        val t: Any,
        val ta: String,
        val tan: String,
        val tn: String,
        val type: String,
        val up: Boolean
    )
}