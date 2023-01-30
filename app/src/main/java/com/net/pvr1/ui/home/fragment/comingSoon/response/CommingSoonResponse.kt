package com.net.pvr1.ui.home.fragment.comingSoon.response

import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import java.io.Serializable

data class CommingSoonResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):Serializable{
    data class Output(
        val genre: ArrayList<String>,
        val language: ArrayList<String>,
        val movies: ArrayList<Movy>,
        val ph: ArrayList<Ph>,
        val pu: ArrayList<Pu>
    ):Serializable{
        data class Movy(
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
            val trs: ArrayList<MovieDetailsResponse.Trs>,
            val ul: Boolean,
            val videoUrl: String,
            val webimage: String,
            val wib: String,
            val wit: String
        ):Serializable
        data class Ph(
            val buttonText: String,
            val cities: String,
            val i: String,
            val id: Int,
            val location: String,
            val name: String,
            val platform: String,
            val priority: Int,
            val redirect_url: String,
            val screen: String,
            val trailerUrl: String,
            val type: String
        ):Serializable
        data class Pu(
            val buttonText: String,
            val cities: String,
            val i: String,
            val id: Int,
            val location: String,
            val name: String,
            val platform: String,
            val priority: Int,
            val redirect_url: String,
            val redirectView: String,
            val screen: String,
            val trailerUrl: String,
            val type: String
        ):Serializable
    }

}