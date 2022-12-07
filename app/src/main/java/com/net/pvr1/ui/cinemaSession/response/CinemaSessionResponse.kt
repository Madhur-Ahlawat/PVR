package com.net.pvr1.ui.cinemaSession.response

import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import java.io.Serializable

data class CinemaSessionResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
) : Serializable {
    data class Output(
        val addr: String,
        val bd: List<Bd>,
        val btnc: String,
        val cd: String,
        val cf: Boolean,
        val childs: List<Child>,
        val cn: String,
        val d: String,
        val em: String,
        val fmts: List<String>,
        val fo: Boolean,
        val gfd: String,
        val gfe: Boolean,
        val hc: Boolean,
        val id: String,
        val img: List<String>,
        val imob: String,
        val ina: String,
        val lang: String,
        val lat: String,
        val like: Boolean,
        val lngs: List<String>,
        val mc: Boolean,
        val me: Boolean,
        val msc: Int,
        val oua: String,
        val par: Boolean,
        val ph: String,
        val phd: List<HomeResponse.Ph>,
        val pu: List<HomeResponse.Ph>,
        val s: String
    ) : Serializable {
        data class Bd(
            val d: String,
            val dt: String,
            val showdate: Long,
            val wd: String,
            val wdf: String
        ) : Serializable
    }
    data class Child(
        val at: String,
        val ccid: String,
        val ccn: String,
        val fmt: List<String>,
        val hc: Boolean,
        val i: Any,
        val mvs: ArrayList<Mv>,
        val sc: Int
    ) : Serializable {
        data class Mv(
            val adlt: Boolean,
            val amcode: String,
            val genres: List<String>,
            val mcensor: String,
            val mih: String,
            val miv: String,
            val ml: ArrayList<Ml>,
            val mlength: String,
            val mn: String
        ) : Serializable {
            data class Ml(
                val lk: String,
                val lng: String,
                val s: ArrayList<S>,
                val tx: Any
            ) : Serializable {
                data class S(
                    val adl: Boolean,
                    val `as`: Int,
                    val at: String,
                    val ba: Boolean,
                    val cc: String,
                    val comm: String,
                    val et: String,
                    val hc: Boolean,
                    val mc: String,
                    val mdc: Boolean,
                    val mds: Boolean,
                    val mdt: Any,
                    val mn: Any,
                    val pg: String,
                    val prs: ArrayList<Pr>,
                    val sd: Long,
                    val sh: String,
                    val sid: Int,
                    val sn: Int,
                    val ss: Int,
                    val st: String,
                    val ts: Int,
                    val txt: String
                ) : Serializable {
                    data class Pr(
                        val ar: String,
                        val `as`: Int,
                        val bp: String,
                        val bv: String,
                        val n: String,
                        val p: String,
                        val st: Int,
                        val ts: Int,
                        val tt: String
                    ) : Serializable
                }
            }
        }
    }
}