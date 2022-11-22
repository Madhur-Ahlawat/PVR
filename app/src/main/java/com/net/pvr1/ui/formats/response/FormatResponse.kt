package com.net.pvr1.ui.formats.response

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
            val m: List<M>,
            val type: Any
        ) : Serializable {
            data class M(
                val c: String,
                val ce: String,
                val fmts: List<String>,
                val grs: List<String>,
                val i: String,
                val id: String,
                val imf: String,
                val l: String,
                val lc: String,
                val lng: String,
                val mcc: String,
                val mf: Boolean,
                val mfs: List<String>,
                val mih: String,
                val miv: String,
                val mlength: String,
                val mopeningdate: String,
                val mtrailerurl: String,
                val mty: String,
                val n: String,
                val othergenres: String,
                val otherlanguages: String,
                val rt: String,
                val rtt: String,
                val sapi: String,
                val sco: Int,
                val sh: String,
                val surl: String,
                val t: String,
                val tag: String,
                val trs: List<Tr>,
                val ul: Boolean,
                val vka: Boolean,
                val wib: String,
                val wit: String
            ) : Serializable {
                data class Tr(
                    val d: String,
                    val id: String,
                    val t: String,
                    val ty: String,
                    val u: String
                ) : Serializable
            }
        }
    }
}