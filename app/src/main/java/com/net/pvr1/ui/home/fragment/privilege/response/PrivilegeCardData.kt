package com.net.pvr1.ui.home.fragment.privilege.response

data class PrivilegeCardData(
    var cardImg: String? = null,
    var type: String? = null,
    var lock: Boolean? = false,
    var name: String? = "",
    var qr_img: String? = "",
    var points: String? = "",
    var output: LoyaltyDataResponse.Output? = null
)
