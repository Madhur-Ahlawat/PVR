package com.net.pvr1.utils

import android.app.Activity
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.net.pvr1.R


class Constant {

    companion object {
        const val platform = "ANDROID"
        const val version = "11.3"
        const val status = "success"
        const val SUCCESS_CODE = 10001
        const val pvrCare = "https://www.pvrcinemas.com/pvrstatic/pvr-care/"
        const val merchandise = "https://pvr.macmerise.com/?user_agent=pvr"

        const val IS_LOGIN = "is_login"
        var DISPLAY = 1
    }


    fun spannableText(
        activityContext: Activity,
        stringBuilder: StringBuilder?,
        tvCensorLang: TextView
    ) {
        val ss = SpannableString(stringBuilder)
        ss.setSpan(
            ForegroundColorSpan(activityContext.resources.getColor(R.color.yellow)),
            0,
            1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            ForegroundColorSpan(activityContext.resources.getColor(R.color.gray)),
            2,
            ss.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvCensorLang.text = ss
        tvCensorLang.movementMethod = LinkMovementMethod.getInstance()
    }
}

