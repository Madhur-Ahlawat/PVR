package com.net.pvr.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scottyab.rootbeer.RootBeer


object Util {
    fun reorderActivityToFront(context: Context, cls: Class<*>) {
        val intent = Intent(context, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        context.startActivity(intent)
    }
    fun appInstalledOrNot(context: Context, uri: String): Boolean {
        val pm: PackageManager = context.getPackageManager()
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }
    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi / 160f)
    }
    fun getCurrentItemRecyclerView(recyclerView: RecyclerView): Int {
        return (recyclerView.layoutManager!! as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
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