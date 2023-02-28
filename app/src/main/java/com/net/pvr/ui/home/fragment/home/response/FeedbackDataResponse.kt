package com.net.pvr.ui.home.fragment.home.response

data class FeedbackDataResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
){
    data class Output(
        val dsc: String,
        val ratings: Ratings,
        val tags: String,
        val title: String
    )

    data class Ratings(
        val L1: String,
        val L2: String,
        val L3: String,
        val L4: String,
        val L5: String
    )
}