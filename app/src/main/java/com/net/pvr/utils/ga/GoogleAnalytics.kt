package com.net.pvr.utils.ga

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
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
        // Ecommerce Event
        fun hitItemListEvent(context: Context, product: String, mv: List<HomeResponse.Mv>) {
            for (data in mv.indices) {
                val itemMovie = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_123")
                    putString(FirebaseAnalytics.Param.ITEM_NAME, "jeggings")
                    putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "pants")
                    putString(FirebaseAnalytics.Param.ITEM_VARIANT, "black")
                    putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
                    putDouble(FirebaseAnalytics.Param.PRICE, 9.99)
                }
                val itemMovieWithIndex = Bundle(itemMovie).apply {
                    putLong(FirebaseAnalytics.Param.INDEX, 1)
                }
               val data = arrayOf(itemMovieWithIndex)
            }
//            mFirebaseAnalytics = FirebaseAnalytics()
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_ID,"")
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME,"")
            bundle.putString(FirebaseAnalytics.Param.ITEMS,"")

//            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST) {
//                param(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001")
//                param(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products")
//                param(
//                    FirebaseAnalytics.Param.ITEMS,
//                    arrayOf(itemJeggingsWithIndex, itemBootsWithIndex, itemSocksWithIndex)
//                )
//            }

        }

        fun hitViewItemEvent(context: Context, eventName: String, bundle: Bundle) {
            try {
                println("eventName--->$eventName")
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                mFirebaseAnalytics?.logEvent(eventName.trim(), bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

        fun hitAddCartEvent(context: Context, eventName: String, bundle: Bundle) {
            try {
                println("eventName--->$eventName")
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                mFirebaseAnalytics?.logEvent(eventName.trim(), bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

        fun hitBeginCheckOutEvent(context: Context, eventName: String, bundle: Bundle) {
            try {
                println("eventName--->$eventName")
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                mFirebaseAnalytics?.logEvent(eventName.trim(), bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

        fun hitPurchaseEvent(context: Context, eventName: String, bundle: Bundle) {
            try {
                println("eventName--->$eventName")
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                mFirebaseAnalytics?.logEvent(eventName.trim(), bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

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