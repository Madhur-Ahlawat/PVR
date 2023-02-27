package com.net.pvr.ui.food.response

data class CancelTransResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: String,
    val result: String,
    val version: Any
)