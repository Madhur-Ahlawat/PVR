package com.net.pvr1.ui.seatLayout.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Price {
    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("priceCode")
    @Expose
    var priceCode: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("c")
    @Expose
    var c: String? = null

}