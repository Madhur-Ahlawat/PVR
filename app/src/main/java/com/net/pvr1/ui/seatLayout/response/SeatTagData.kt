package com.net.pvr1.ui.seatLayout.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SeatTagData {
    /**
     *
     * @return
     * The b
     */
    /**
     *
     * @param b
     * The b
     */
    @SerializedName("b")
    @Expose
    var b: String? = null

    @SerializedName("c")
    @Expose
    private var c: String? = null

    /**
     *
     * @return
     * The s
     */
    /**
     *
     * @param s
     * The s
     */
    @SerializedName("s")
    @Expose
    var s: Int? = null

    /**
     *
     * @return
     * The sn
     */
    /**
     *
     * @param sn
     * The sn
     */
    @SerializedName("sn")
    @Expose
    var sn: String? = null

    @SerializedName("st")
    @Expose
    var st: Int? = null

    /**
     *
     * @return
     * The hc
     */
    /**
     *
     * @param hc
     * The hc
     */
    @SerializedName("hc")
    @Expose
    var hc = false

    /**
     *
     * @return
     * The bu
     */
    /**
     *
     * @param bu
     * The bu
     */
    @SerializedName("bu")
    @Expose
    var bu = false

    @SerializedName("cos")
    @Expose
    var cos = false

    @SerializedName("area")
    @Expose
    var area: String? = null

    /**
     *
     * @return
     * The c
     */
    fun getC(): Any? {
        return c
    }

    /**
     *
     * @param c
     * The c
     */
    fun setC(c: String?) {
        this.c = c
    }

}
