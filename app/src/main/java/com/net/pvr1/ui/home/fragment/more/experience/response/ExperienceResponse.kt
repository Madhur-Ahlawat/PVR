package com.net.pvr1.ui.home.fragment.more.experience.response

data class ExperienceResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val formats: ArrayList<Format>
    ):java.io.Serializable{
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
            val type: String
        )
    }
}