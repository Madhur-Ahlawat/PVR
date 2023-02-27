package com.net.pvr.ui.giftCard.response

import java.io.Serializable

data class GiftCards(
    var d: String,

    var c: String,

    val type: String,

    val alias: String
): Serializable
