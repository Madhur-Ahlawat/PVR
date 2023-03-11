package com.net.pvr.ui.payment.response

data class CaptchaResponse(
    val apk_package_name: String,
    val challenge_ts: String,
    val success: Boolean
)