package com.net.pvr.ui.home.inCinemaMode

data class Order(var orderId:String="DD457HHFGSKHJGK",var foodItems:MutableList<FoodItem> = mutableListOf(FoodItem(),FoodItem()),var orderValue:Int=400)
