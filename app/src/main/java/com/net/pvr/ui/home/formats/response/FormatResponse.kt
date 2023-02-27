package com.net.pvr.ui.home.formats.response

import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import java.io.Serializable

data class FormatResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
) : Serializable {
    data class Output(
        val banner: Any,
        val cinemas: Cinemas,
        val ph: ArrayList<Ph>,
        val pu: ArrayList<Any>
    ) : Serializable {
        data class Ph(
            val id: String,
            val name: String,
            val screen: String,
            val platform: String,
            val cities: String,
            val cinemas: String,
            val movies: String,
            val trailerUrl: String,
            val type: String,
            val buttonText: String,
            val location: String,
            val redirectView: String,
            val text: String,
            val priority: String,
            val formats: String,
            val redirect_url: String,
            val i: String
        ) : Serializable
        data class Cinemas(
            val ci: List<Any>,
            val m: ArrayList<HomeResponse.Mv>,
            val type: Any
        ):Serializable
    }
}