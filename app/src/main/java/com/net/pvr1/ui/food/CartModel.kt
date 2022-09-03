package com.net.pvr1.ui.food

data class CartModel (
    val id: String?,
    val title: String?,
    val image: String?,
    var quantity: Int,
    val price: Int,
    val veg: Boolean,
)