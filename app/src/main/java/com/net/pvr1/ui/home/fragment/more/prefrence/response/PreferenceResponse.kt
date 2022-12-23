package com.net.pvr1.ui.home.fragment.more.prefrence.response

data class PreferenceResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val cd: String,
        val doa: String,
        val dob: String,
        val em: String,
        val g: String,
        val genre: Genre,
        val im: String,
        val lang: Lang,
        val ms: String,
        val params: List<Any>,
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
        data class Genre(
            val liked: List<Liked>,
            val other: List<Other>
        ):java.io.Serializable{
            data class Liked(
                val id: String,
                val na: String,
                val txt: String
            ):java.io.Serializable
            data class Other(
                val id: String,
                val na: String,
                val txt: String
            ):java.io.Serializable
        }
        data class Lang(
            val liked: List<Genre.Liked>,
            val other: List<Genre.Other>
        ):java.io.Serializable

        data class Pon(
            val Other: List<Genre.Other>,
            val SavedCards: List<Any>,
            val Wallets: List<Wallet>
        ):java.io.Serializable

        data class Schedule(
            val liked: List<Any>,
            val other: List<Genre.Other>
        ):java.io.Serializable


        data class Theater(
            val liked: List<Genre.Liked>,
            val other: List<Genre.Other>
        ):java.io.Serializable

        data class Wallet(
            val id: String,
            val na: String,
            val txt: String
        ):java.io.Serializable
    }
}