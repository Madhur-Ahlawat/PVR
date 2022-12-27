package com.net.pvr1.ui.home.fragment.more.response

data class ProfileResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val cd: String,
        val doa: Any,
        val dob: String,
        val em: String,
        val g: String,
        val genre: Genre,
        val im: String,
        val lang: Lang,
        val ms: String,
        val params: List<String>,
        val percentage: Int,
        val ph: String,
        val po: List<Any>,
        val pon: Pon,
        val pp: String,
        val schedule: Schedule,
        val theater: Theater,
        val un: String,
        val wl: List<Any>
    ):java.io.Serializable{
        class Genre
        class Schedule
        class Theater
        class Lang
        class Pon
    }
}