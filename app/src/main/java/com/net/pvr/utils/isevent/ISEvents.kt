package com.net.pvr.utils.isevent

import android.app.Activity
import com.evergage.android.Evergage
import com.evergage.android.Screen
import com.evergage.android.promote.*
import com.net.pvr.ui.ticketConfirmation.response.TicketBookedResponse
import org.json.JSONObject

class ISEvents {
    fun bookBtn(context: Activity, masterMovieId: String?) {
        // Evergage track screen view
        val screen: Screen? = Evergage.getInstance().getScreenForActivity(context)
        if (screen != null) {

            val jsonObject = JSONObject()
            try {
                jsonObject.put("type", ItemType.Product)
                jsonObject.put("_id", masterMovieId)
                val item: Item? = Item.fromJSONString(jsonObject.toString())
                screen.viewItem(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun movieDetail(context: Activity, masterMovieId: String?) {
        // Evergage track screen view
        val screen: Screen? = Evergage.getInstance().getScreenForActivity(context)
        if (screen != null) {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("type", ItemType.Product)
                jsonObject.put("_id", masterMovieId)
                val item: Item? = Item.fromJSONString(jsonObject.toString()) // fromJSONObject(jsonObject,cityId);
                screen.viewItemDetail(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addToCArt(context: Activity, id: String, price: String, type: String, size: Int) {
        // Evergage track screen view
        val screen = Evergage.getInstance().getScreenForActivity(context)
        if (screen != null) {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("type", type)
                jsonObject.put("_id", id)
                screen.viewTag(Tag.fromJSONObject(jsonObject, id!!))
                val product = Product(id)
                val category = Category.fromJSONObject(
                    jsonObject,
                    id
                )
                val categories: MutableList<Category?> = ArrayList()
                categories.add(category)
                product.price = java.lang.Double.valueOf(price)
                product.categories = categories
                val lineItem = LineItem(product, size)
                screen.addToCart(lineItem)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun confirmationOrder(
        context: Activity,
        bookingId: String,
        amount: List<TicketBookedResponse.F>,
        movieId: String,
        amt: String,
        food: List<TicketBookedResponse.Food>
    ) {
        // Evergage track screen view
        val screen = Evergage.getInstance().getScreenForActivity(context)

        if (screen != null) {
            try {
                val lineItems: MutableList<LineItem> = java.util.ArrayList()
                for (i in amount.indices) {
                    if (amount[i].n == ("Tickets")) {
                        val jsonObject = JSONObject()
                        jsonObject.put("type", ItemType.Product)
                        jsonObject.put("_id", movieId) //TODO movieId
                        jsonObject.put("price", amount[i].v.toDouble() / amount[i].c) //TODO price
                        val item = Item.fromJSONString(jsonObject.toString())
                        val lineItem = LineItem(
                            item!!, amount[i].c
                        )
                        lineItems.add(lineItem)
                    }
                }
                if (food != null && food.isNotEmpty()) {
                    for (i in food.indices) {
                        val jsonObject = JSONObject()
                        jsonObject.put("type", ItemType.Product)
                        jsonObject.put("_id", food[i].ho) //TODO movieId
                        jsonObject.put("price", food[i].dp / 100) //TODO price
                        val item = Item.fromJSONString(jsonObject.toString())
                        val lineItem = LineItem(
                            item!!, food[i].qt
                        )
                        lineItems.add(lineItem)
                    }
                }
                val order = Order(bookingId, lineItems, amt.toDouble())
                screen.purchase(order)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }


}