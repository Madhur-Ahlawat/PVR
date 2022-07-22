package com.net.pvr1.utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context?.toast(@SuppressLint("SupportAnnotationUsage") @StringRes resId: String?) {
    resId ?: return
    this ?: return
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}
fun Context?.printLog(@SuppressLint("SupportAnnotationUsage") @StringRes log: String?) {
    Log.d(TAG, log.toString())

}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

