package com.net.pvr.ui.food.old.reponse

data class OldFoodResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
):java.io.Serializable{
    data class Output(
        val aqt: Int,
        val c: String,
        val cat: List<String>,
        val cn: String,
        val fimg: String,
        val h1: String,
        val h2: String,
        val ins: Boolean,
        val na: String,
        val nams: String,
        val r: ArrayList<R>,
        val ra: Boolean,
        val st: String,
        val typ: String
    ):java.io.Serializable{
        data class R(
            val ct: String,
            val dis: String,
            val dp: Int,
            val fout: String,
            val foutimg: Any,
            val h: String,
            val ho: String,
            val i: String,
            val id: String,
            val iw: String,
            val op: Int,
            var qt: Int,
            val sf: Boolean,
            val si: String,
            val veg: Boolean
        )
    }
}