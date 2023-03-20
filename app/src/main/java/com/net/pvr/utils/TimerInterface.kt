package com.net.pvr.utils

import android.app.Activity

interface TimerInterface {
    fun update(
        context: Activity,
        string: String,
        contextString: String,
        cinemaID: String,
        sessionID: String,
        paymentType: String,
        bookingid: String
    )

}