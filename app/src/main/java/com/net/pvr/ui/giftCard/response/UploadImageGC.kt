package com.net.pvr.ui.giftCard.response

data class UploadImageGC(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
){
    data class Output(
        val url: String,
        val id: String,
    )
}