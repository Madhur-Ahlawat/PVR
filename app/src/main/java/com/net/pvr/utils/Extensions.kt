package com.net.pvr.utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.net.pvr.ui.payment.PaymentActivity
import java.util.ArrayList

fun Context?.toast(@SuppressLint("SupportAnnotationUsage") @StringRes resId: String?) {
    resId ?: return
    this ?: return
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}
fun printLog(@SuppressLint("SupportAnnotationUsage") @StringRes log: String?) {
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

fun Context.launchActivity(cls: Class<*>, flags: Int) {
    val intent = Intent(this, cls).apply {
        addFlags(flags)
    }
    this.startActivity(intent)
    Constant.discount_val = "0.0"
    PaymentActivity.subsId = ""
    PaymentActivity.subsToken = ""
    PaymentActivity.createdAt = ""
    PaymentActivity.isPromoCodeApplied = false
    PaymentActivity.offerList = ArrayList()
}
fun Context.launchGiftActivity(cls: Class<*>, flags: Int) {
    val intent = Intent(this, cls).apply {
        addFlags(flags)
    }
    this.startActivity(intent)

}

fun Context.launchPaymentActivity(cls: Class<*>, flags: Int,amt:String,from:String) {
    val intent = Intent(this, cls).apply {
        putExtra("paidAmount",amt)
        putExtra("from",from)
        addFlags(flags)
    }
    this.startActivity(intent)


}

fun Context.launchPrivilegeActivity(cls: Class<*>, flags: Int,date:String,amt:String,id:String,from:String) {
    val intent = Intent(this, cls).apply {
        putExtra("id",id)
        putExtra("date",date)
        putExtra("amt",amt)
        putExtra("from",from)
        addFlags(flags)
        Constant.discount_val = "0.0"
        PaymentActivity.subsId = ""
        PaymentActivity.subsToken = ""
        PaymentActivity.createdAt = ""
        PaymentActivity.isPromoCodeApplied = false
        PaymentActivity.offerList = ArrayList()
    }
    this.startActivity(intent)

}

