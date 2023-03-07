package com.net.pvr.ui.home.fragment.more.eVoucher.model

data class HowItWorkModel(
    val title: String,
    val category: ArrayList<Category>
) : java.io.Serializable {
    data class Category(
        val title: String,
        val image: Int
    ) : java.io.Serializable
}