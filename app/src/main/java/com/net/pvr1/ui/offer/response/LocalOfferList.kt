package com.net.pvr1.ui.offer.response

import java.io.Serializable


data class LocalOfferList (
    var cat: String,
    var list: ArrayList<MOfferResponse.Output.Offer>
):Serializable

