package com.net.pvr1.ui.seatLayout.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Seat {
    /**
     *
     * @return
     * The PriceCode
     */
    /**
     *
     * @param PriceCode
     * The PriceCode
     */
    @SerializedName("priceCode")
    @Expose
    var priceCode: String? = null

    /**
     *
     * @return
     * The SeatBookingId
     */
    /**
     *
     * @param SeatBookingId
     * The SeatBookingId
     */
    @SerializedName("seatBookingId")
    @Expose
    var seatBookingId: String? = null

}
