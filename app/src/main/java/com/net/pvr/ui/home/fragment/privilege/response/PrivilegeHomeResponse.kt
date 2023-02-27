package com.net.pvr.ui.home.fragment.privilege.response

data class PrivilegeHomeResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val faq: String,
        val ls: String,
        val passcancel: Boolean,
        val passport: Boolean,
        var passportbuy: Boolean = false,

        val pcheck: String,
        val pcities: String,
        val pdays: String,
        val pinfo: ArrayList<Pinfo>,
        val pkotakurl: String,
        val pt: String,
        val st: ArrayList<St>,
        val ulm: String,
        val vou: String,
        val wt: ArrayList<Wt>
    ):java.io.Serializable{
        data class Pinfo(
            val content: String,
            val ph: String,
            val pi: String,
            val plogo: String,
            val pname: String,
            val priority: Int,
            val psh: String,
            val ptype: String,
            val visits: String
        ):java.io.Serializable
        data class St(
            val pi: String,
            val priority: Int,
            val text: String
        ):java.io.Serializable
        data class Wt(
            val pi: String,
            val priority: Int,
            val text: String
        ):java.io.Serializable
    }
}