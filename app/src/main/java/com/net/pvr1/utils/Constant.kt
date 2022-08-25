package com.net.pvr1.utils

import android.app.Activity
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import com.net.pvr1.R
import java.net.MalformedURLException
import java.net.URL


class Constant {

    companion object {
        const val platform = "ANDROID"
        const val version = "11.3"
        const val status = "success"
        const val SUCCESS_CODE = 10001
        const val pvrCare = "https://www.pvrcinemas.com/pvrstatic/pvr-care/"
        const val merchandise = "https://pvr.macmerise.com/?user_agent=pvr"
        var select_pos = 0

        const val IS_LOGIN = "is_login"
        const val CITY_NAME = "city_name"
        var DISPLAY = 1

        const val SUCCESS_RESULT = 1
        const val RECEVIER = "$PACKAGE_NAME.RECEVIER"
        const val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"
        const val ADDRESS = "$PACKAGE_NAME.ADDRESS"
        const val LOCAITY = "$PACKAGE_NAME.LOCAITY"
        const val COUNTRY = "$PACKAGE_NAME.COUNTRY"
        const val DISTRICT = "$PACKAGE_NAME.DISTRICT"
        const val POST_CODE = "$PACKAGE_NAME.POST_CODE"
        const val STATE = "$PACKAGE_NAME.STATE"
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

    private fun addClickablePartTextViewResizable(
        strSpanned: Spanned, tv: TextView,
        maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : MySpannable(false) {
                override fun onClick(widget: View) {
                    if (viewMore) {
                        tv.layoutParams = tv.layoutParams
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, -1, "See Less", false)
                    } else {
                        tv.layoutParams = tv.layoutParams
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, 4, ".. See More", true)
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }

    fun makeTextViewResizable(tv: TextView, maxLine: Int, expandText: String, viewMore: Boolean) {
        if (tv.tag == null) {
            tv.tag = tv.text
        }
        val vto = tv.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val obs = tv.viewTreeObserver
                obs.removeGlobalOnLayoutListener(this)
                if (maxLine == 0) {
                    val lineEndIndex = tv.layout.getLineEnd(0)
                    val text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextViewResizable(
                            Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                            viewMore
                        ), TextView.BufferType.SPANNABLE
                    )
                } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                    val lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                    val text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextViewResizable(
                            Html.fromHtml(tv.text.toString()), tv, maxLine, expandText,
                            viewMore
                        ), TextView.BufferType.SPANNABLE
                    )
                } else {
                    val lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                    val text = tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextViewResizable(
                            Html.fromHtml(tv.text.toString()), tv, lineEndIndex, expandText,
                            viewMore
                        ), TextView.BufferType.SPANNABLE
                    )
                }
            }
        })
    }
    fun extractYoutubeId(s: String): String? {
        return try {
            println("queryurl---$s")
            var query: String? = null
            try {
                query = URL(s).query
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            val param = query!!.split("&").toTypedArray()
            var id: String? = null
            for (row in param) {
                val param1 = row.split("=").toTypedArray()
                if (param1[0] == "v") {
                    id = param1[1]
                }
            }
            id
        } catch (e: Exception) {
            ""
        }
    }
}

