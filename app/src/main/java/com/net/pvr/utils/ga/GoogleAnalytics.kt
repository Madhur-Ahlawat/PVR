package com.net.pvr.utils.ga

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.gms.analytics.HitBuilders.EventBuilder
import com.google.android.gms.analytics.Tracker
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.ui.giftCard.response.GiftCardListResponse
import com.net.pvr.ui.home.fragment.home.response.HomeResponse


class GoogleAnalytics {
    companion object{
        private var mFirebaseAnalytics: FirebaseAnalytics? = null

        fun hitEvent(context: Context, eventName: String, bundle: Bundle) {
            try {
                println("eventName--->$eventName")
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                mFirebaseAnalytics?.logEvent(eventName.trim(), bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
        // Ecommerce Event Booking
        fun hitItemListEvent(context: Context, product: String, mv: List<HomeResponse.Mv>) {
           val itemArray = ArrayList<Bundle>()
            for (data in mv.indices) {
                val itemMovie = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_ID, mv[data].mcc)
                    putString(FirebaseAnalytics.Param.ITEM_NAME, mv[data].t)
                    putString(FirebaseAnalytics.Param.ITEM_CATEGORY,product)
                    putString(FirebaseAnalytics.Param.ITEM_BRAND, "PVR Cinema")
                }
                val itemMovieWithIndex = Bundle(itemMovie).apply {
                    putLong(FirebaseAnalytics.Param.INDEX, data.toLong())
                }
                itemArray.add(itemMovieWithIndex)
            }
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_ID,"")
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME,product)
            bundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS,itemArray)
            mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST,bundle)

        }
        //GiftCard
        fun hitItemListEventGC(context: Context, product: String, giftCards: ArrayList<GiftCardListResponse.Output.GiftCard>) {
           val itemArray = ArrayList<Bundle>()
            for (data in giftCards.indices) {
                val itemMovie = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_ID, giftCards[data].pkGiftId.toString())
                    putString(FirebaseAnalytics.Param.ITEM_NAME, giftCards[data].alias)
                    putString(FirebaseAnalytics.Param.ITEM_CATEGORY,product)
                    putString(FirebaseAnalytics.Param.ITEM_BRAND, "PVR Cinema")
                }
                val itemMovieWithIndex = Bundle(itemMovie).apply {
                    putLong(FirebaseAnalytics.Param.INDEX, data.toLong())
                }
                itemArray.add(itemMovieWithIndex)
            }

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_ID,"")
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME,product)
            bundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS,itemArray)
            mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST,bundle)

        }

        fun hitViewItemEvent(context: Context, product: String,id:String,name:String) {
            val itemMovie = Bundle()
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            itemMovie.putString(FirebaseAnalytics.Param.ITEM_LIST_ID,id)
            itemMovie.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME,name)
            itemMovie.putString(FirebaseAnalytics.Param.ITEM_CATEGORY,product)
            mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.VIEW_ITEM,itemMovie)
        }

        fun hitAddCartEvent(context: Context, id: String, price: String,product: String,quantity:Int) {

                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)

            val addToWishlistParams = Bundle()
            addToWishlistParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR")
            addToWishlistParams.putDouble(FirebaseAnalytics.Param.VALUE, price.toDouble())
            addToWishlistParams.putDouble(FirebaseAnalytics.Param.PRICE, price.toDouble())
            addToWishlistParams.putInt(FirebaseAnalytics.Param.QUANTITY, quantity)
            addToWishlistParams.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, product)
            addToWishlistParams.putString(FirebaseAnalytics.Param.ITEM_ID, id)
            addToWishlistParams.putString(FirebaseAnalytics.Param.ITEM_BRAND, "PVR CINEMA")


            mFirebaseAnalytics?.logEvent(
                FirebaseAnalytics.Event.ADD_TO_CART,
                addToWishlistParams
            )


        }

        fun hitBeginCheckOutEvent(context: Context, id: String, price: String,product: String,quantity:Int) {
            ////
            val addToWishlistParams = Bundle()
            addToWishlistParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR")
            addToWishlistParams.putDouble(FirebaseAnalytics.Param.VALUE, price.toDouble())
            addToWishlistParams.putDouble(FirebaseAnalytics.Param.PRICE, price.toDouble())
            addToWishlistParams.putInt(FirebaseAnalytics.Param.QUANTITY, quantity)
            addToWishlistParams.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, product)
            addToWishlistParams.putString(FirebaseAnalytics.Param.ITEM_ID, id)
            addToWishlistParams.putString(FirebaseAnalytics.Param.ITEM_BRAND, "PVR CINEMA")
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            mFirebaseAnalytics?.logEvent(
                FirebaseAnalytics.Event.BEGIN_CHECKOUT,
                addToWishlistParams
            )

        }

        fun hitPurchaseEvent(context: Context, id: String, price: String,product: String,quantity: Int) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val addPaymentParams = Bundle()
            addPaymentParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR")
            addPaymentParams.putDouble(FirebaseAnalytics.Param.VALUE, price.toDouble())
            addPaymentParams.putDouble(FirebaseAnalytics.Param.PRICE, price.toDouble())
            addPaymentParams.putInt(FirebaseAnalytics.Param.QUANTITY, quantity)
            addPaymentParams.putString(FirebaseAnalytics.Param.ITEM_ID, id)
            addPaymentParams.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, product)
            mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.PURCHASE, addPaymentParams)
        }

        fun hitPaymentInfo(context: Context, id: String, price: String,product: String,paymentType:String) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val addPaymentParams = Bundle()
            addPaymentParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR")
            addPaymentParams.putDouble(FirebaseAnalytics.Param.VALUE, price.toDouble())
            addPaymentParams.putDouble(FirebaseAnalytics.Param.PRICE, price.toDouble())
            addPaymentParams.putString(FirebaseAnalytics.Param.COUPON, "")
            addPaymentParams.putString(FirebaseAnalytics.Param.PAYMENT_TYPE, paymentType)
            addPaymentParams.putString(FirebaseAnalytics.Param.ITEM_ID, id)
            addPaymentParams.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, product)
            mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, addPaymentParams)

        }

        fun hitRefundEvent(context: Context, eventName: String, bundle: Bundle) {
            try {
                println("eventName--->$eventName")
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                mFirebaseAnalytics?.logEvent(eventName.trim(), bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

    }

}