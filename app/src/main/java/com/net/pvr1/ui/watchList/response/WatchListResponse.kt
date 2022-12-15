package com.net.pvr1.ui.watchList.response

data class WatchListResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: ArrayList<Output>,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val active: Boolean,
        val cinemaCodes: String,
        val city: String,
        val emailNotification: Boolean,
        val movieData: MovieData,
        val movieName: String,
        val moviecode: String,
        val pushNotification: Boolean,
        val sentNotification: Boolean,
        val sentNotificationDate: Any,
        val smsNotification: Boolean,
        val whatsappNotification: Boolean
    ):java.io.Serializable{
        data class MovieData(
            val caption: String,
            val censor: String,
            val date_caption: String,
            val desc: String,
            val genre: String,
            val image: String,
            val language: String,
            val like_count: String,
            val liked: Boolean,
            val masterMovieId: String,
            val mih: String,
            val miv: String,
            val movieId: String,
            val name: String,
            val openingDate: Long,
            val othergenres: String,
            val otherlanguages: String,
            val prebook: Boolean,
            val prebookId: String,
            val release: Boolean,
            val trs: List<Tr>,
            val ul: Boolean,
            val videoUrl: String,
            val webimage: String,
            val wib: String,
            val wit: String
        ):java.io.Serializable{
            data class Tr(
                val d: String,
                val id: String,
                val t: String,
                val ty: String,
                val u: String
            )
        }
    }
}