package com.net.pvr.ui.home.fragment.more.eVoucher.details.response

data class SaveEVoucherResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val bookType: String,
        val id: String,
        val qt: List<Qt>,
        val ta: String
    ):java.io.Serializable{
        data class Qt(
            val count: Int,
            val name: String,
            val value: Int
        )
    }
}