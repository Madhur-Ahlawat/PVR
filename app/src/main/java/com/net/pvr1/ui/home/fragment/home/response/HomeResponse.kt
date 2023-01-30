package com.net.pvr1.ui.home.fragment.home.response

import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import java.io.Serializable

data class HomeResponse(
    val code: Int,
    val minversion: String,
    val msg: String,
    val output: Output,
    val result: String,
    val version: String
):Serializable{
    data class Output(
        val cnt_down: String,
        val cp: List<Cp>,
        val cur_date: Any,
        val ex_date: Any,
        val m: List<Any>,
        val mf: List<String>,
        val mfi: List<Mfi>,
        val mgener: ArrayList<String>,
        val mlng: ArrayList<String>,
        val mspecial: List<Any>,
        val msps: List<Any>,
        val mv: List<Mv>,
        val ph: ArrayList<Ph>,
        val pu: ArrayList<Pu>,
        val rm: Rm,
        val vban: Any
    ):Serializable
    data class Cp(
        val c: String,
        val ce: String,
        val fmts: List<Any>,
        val grs: List<Any>,
        val i: String,
        val id: String,
        val imf: String,
        val l: String,
        val lc: String,
        val lng: String,
        val mcc: String,
        val mf: Boolean,
        val mfs: List<Any>,
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
        val trs: ArrayList<MovieDetailsResponse.Trs>,
        val ul: Boolean,
        val vka: Boolean,
        val wib: String,
        val wit: String
    ):Serializable
    data class Mfi(
        val name: String,
        val url: String,
        val nurl: String
    ):Serializable
    data class Mv(
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
        val trs: ArrayList<MovieDetailsResponse.Trs>,
        val ul: Boolean,
        val vka: Boolean,
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
        val redirectView: String,
        val redirect_url: String,
        val screen: String,
        val text: String,
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
        val redirectView: String,
        val redirect_url: String,
        val screen: String,
        val text: String,
        val trailerUrl: String,
        val type: String
    ):Serializable
    data class Rm(
        val c: String,
        val ce: String,
        val fmts: ArrayList<String>,
        val grs: List<String>,
        val i: String,
        val id: String,
        val imf: String,
        val l: String,
        val lc: String,
        val lng: String,
        val mcc: String,
        val mf: Boolean,
        val mfs: ArrayList<String>,
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
        val trs: ArrayList<MovieDetailsResponse.Trs>,
        val ul: Boolean,
        val vka: Boolean,
        val wib: String,
        val wit: String
    ):Serializable
    data class Tr(
        val d: String,
        val id: String,
        val t: String,
        val ty: String,
        val u: String
    ):Serializable
}