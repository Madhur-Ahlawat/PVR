package com.net.pvr1.ui.giftCard.response

import java.io.Serializable

data class SaveGiftCardCount(
     var recipient_email: String,

    val recipient_mobile: String,

    val recipient_name: String,

    val personal_message: String,

    val del_address: String,

    val is_self: String,

    val city: String,

    val pincode: String,
     var  gift_cards:ArrayList<GiftCards>
): Serializable
