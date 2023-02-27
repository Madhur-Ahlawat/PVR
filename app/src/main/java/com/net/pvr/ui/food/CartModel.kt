package com.net.pvr.ui.food

data class CartModel (
    val id: Int?,
    val title: String?,
    val image: String?,
    var quantity: Int,
    val price: Int,
    val veg: Boolean,
    val ho: String,
    val mid: String,
):java.io.Serializable