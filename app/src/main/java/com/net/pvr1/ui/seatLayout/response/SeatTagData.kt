package com.net.pvr1.ui.seatLayout.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class SeatTagData(

    @SerializedName("b")
    @Expose
    var b: String? = null,

    @SerializedName("c")
    @Expose
    internal var c: String? = null,

    @SerializedName("s")
    @Expose
    var s: Int? = null,

    @SerializedName("sn")
    @Expose
    var sn: String? = null,

    @SerializedName("st")
    @Expose
    var st: Int? = null,
    @SerializedName("hc")
    @Expose
    var hc: Boolean = false,

    @SerializedName("bu")
    @Expose
    var bu: Boolean = false,

    @SerializedName("cos")
    @Expose
    var cos: Boolean = false,

    @SerializedName("area")
    @Expose
    var area: String? = null
) : Serializable

