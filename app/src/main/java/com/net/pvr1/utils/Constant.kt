package com.net.pvr1.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.provider.Settings
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import java.net.MalformedURLException
import java.net.URL
import java.text.DecimalFormat


@Suppress("DEPRECATION")
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
        const val PREFS_TOKEN_FILE = "prefs_token_file"
        const val USER_NAME = "user_name"
        const val USER_EMAIL = "user_email"
        const val USER_TOKEN = "user_token"
        const val USER_DOB = "user_dob"
        const val USER_MO_NUMBER = "user_mo"
        const val USER_ID = "user_id"
        const val SEAT_AVAILABEL = 1
        var TRANSACTION_ID = "0"
        var OfferDialogImage = "0"
        var BOOKING_ID = "0"
        var SESSION_ID = "0"
        var CINEMA_ID = "0"
        var BOOK_TYPE = "BOOKING"
        var TICKET_BOOKING_DETAILS = ""
        var CITY = "Delhi-NCR"
        const val ON_BOARDING_CLICK = false
        const val SEAT_BOOKED = 2
        const val SEAT_SELECTED = 3
        const val HATCHBACK = 5
        const val SEAT_SOCIAL_DISTANCING = 4
        const val SEAT_SELECTED_HATCHBACK = 1137
        const val SEAT_SELECTED_SUV = 1136
        const val SEAT_SELECTED_BIKE = 1138
        const val SUV = 6
        const val BIKE = 7
        const val BIKE_SEAT_BOOKED = 8
        var PrivilegeHomeResponseConst:PrivilegeHomeResponse.Output? =null
        var PlaceHolder:HomeResponse.Output? = null
        var DECIFORMAT = DecimalFormat("0.000")

    }

    fun getDeviceId(context: Activity): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
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
    ): SpannableStringBuilder{
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : MySpannable(false) {
                override fun onClick(widget: View) {
                    if (viewMore) {
                        tv.layoutParams = tv.layoutParams
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.setTextColor(Color.parseColor("#000000"))
                        tv.invalidate()
                        makeTextViewResizable(tv, -1, "..read less", false)
                    } else {
                        tv.layoutParams = tv.layoutParams
                        tv.setTextColor(Color.parseColor("#000000"))
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, 4, "..read more", true)
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
                    tv.setTextColor(Color.parseColor("#000000"));

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
                    tv.setTextColor(Color.parseColor("#000000"));

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
                    tv.setTextColor(Color.parseColor("#000000"));

                }
            }
        })
    }

    fun extractYoutubeId(s: String): String? {
        return try {
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

    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.toInt()
    }

    fun removeTrailingZeroFormatter(d: Float): String? {
        val df = DecimalFormat("#0.00")
        df.format(d.toDouble())
        return d.toString()
    }

    fun removeTrailingZeroFormater(d: Float): String? {
        return if (d == d.toLong().toFloat()) String.format("%d", d.toLong()) else {
            val df = DecimalFormat("#0.00")
            df.format(d.toDouble())
        }
    }

    var youtubeVideoCode: String? = null

    fun getVideoCode(youtubeUrl: String) {
        val videoCode: Array<String>
        try {
            videoCode = if (youtubeUrl.contains("v=")) {
                youtubeUrl.split("v=").toTypedArray()
            } else {
                youtubeUrl.split("list=").toTypedArray()
            }
            youtubeVideoCode = videoCode[1]
        } catch (e: ArrayIndexOutOfBoundsException) {

        }
    }

    fun openMap(context: Context,lat:String,lang:String){
        val strUri =
            "http://maps.google.com/maps?q=loc:$lat,$lang (Label which you want)"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        context.startActivity(intent)
    }

    fun getURLForResource(resourceId: Int): String? {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + android.R::class.java.getPackage().name + "/" + resourceId)
            .toString()
    }



    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }
}