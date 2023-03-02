package com.net.pvr.utils

import android.content.Context
import com.scottyab.rootbeer.RootBeer

object Util {
    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi / 160f)
    }

    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.toInt()
    }

    fun isRooted(context: Context?): Boolean? {
        return try {
            val rootBeer = RootBeer(context)
//            return rootBeer.isRooted
            false
        } catch (e: Exception) {
            false
        }
    }
}