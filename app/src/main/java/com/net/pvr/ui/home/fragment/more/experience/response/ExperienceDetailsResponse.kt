package com.net.pvr.ui.home.fragment.more.experience.response

import com.net.pvr.ui.home.fragment.home.response.HomeResponse

data class ExperienceDetailsResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val book: Boolean,
        val format: Format,
        val ph: ArrayList<HomeResponse.Ph>,
        val pu: ArrayList<Any>
    ):java.io.Serializable
    data class Format(
        val cities: String,
        val description: String,
        val id: Int,
        val imageUrl: String,
        val logoUrl: String,
        val name: String,
        val priority: Int,
        val rurl: String,
        val text: String,
        val type: String):java.io.Serializable

}