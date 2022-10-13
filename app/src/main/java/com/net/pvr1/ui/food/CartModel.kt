package com.net.pvr1.ui.food

data class CartModel (
    val id: Int?,
    val title: String?,
    val image: String?,
    var quantity: Int,
    val price: Int,
    val veg: Boolean,
):java.io.Serializable