package com.net.pvr1.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.ui.ticketConfirmation.TicketConfirmationActivity
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

        const val IS_LOGIN = "is_login"
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
        var PRIVILEGEPOINT = "0"
        var PRIVILEGEVOUCHER = "0"
        var CINEMA_ID = "0"
        const val CITY = "Delhi-NCR"
        const val CITY_CC = "City-Name"
        const val LATITUDE = "lat"
        const val LONGITUDE= "lang"
        const val BOOK_TYPE= "BOOKING"
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

        // DisCount for payment

        var discount_val = "0.0"
        var discount_txt = ""


        // Payment Ids
        var CREDIT_CARD = "116"
        var DEBIT_CARD = "117"
        var NET_BANKING = "118"
        var PAYTMPOSTPAID = "124"
        var PAYTM_WALLET = "102"
        var ZAGGLE = "126"
        var UPI = "119"
        var PHONE_PE = "113"
        var GEIFT_CARD = "105"
        var AIRTEL = "110"

        // OFFERS

        var PROMOCODE = "O104"
        var ACCENTIVE = "o111"
        var M_COUPON = "O102"
        var STAR_PASS = "O105"
        var HYATT = "O107"
        var GYFTR = "O108"

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



    fun openMap(context: Context,lat:String,lang:String){
        val strUri =
            "http://maps.google.com/maps?q=loc:$lat,$lang (Label which you want)"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        context.startActivity(intent)
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

    fun isLocationServicesAvailable(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
        var isAvailable = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode =
                    Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
            }
            isAvailable = locationMode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            locationProviders = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            isAvailable = !TextUtils.isEmpty(locationProviders)
        }
        val coarsePermissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val finePermissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return isAvailable && (coarsePermissionCheck || finePermissionCheck)
    }

    fun printTicket(activity: Activity) {
        val intent = Intent(
            activity,
            TicketConfirmationActivity::class.java
        )
        activity.startActivity(intent)
        activity.finish()
    }

    fun shareData(activity: Activity,title:String,shareUrl:String){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl)
        activity.startActivity(Intent.createChooser(shareIntent, "Share via"))
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
                        .toString() + "... " + expandText
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
                    val text = tv.text.subSequence(0, lineEndIndex - expandText.length - 15)
                        .toString() + "... " + expandText
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
                    val text = tv.text.subSequence(0, lineEndIndex).toString() + "" + expandText
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


    //spannable textView

    //spannable textView
    private fun addClickablePartTextViewResizable(
        strSpanned: Spanned, tv: TextView,
        maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : MySpannable(false) {
                override fun onClick(widget: View) {
                    super.onClick(widget)
                    if (viewMore){
                        tv.layoutParams = tv.layoutParams
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, -1, "", false)
                    }else{
                        tv.layoutParams = tv.layoutParams
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, 5, ".. Read More", true)
                    }
                }

            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }

    interface FilterType {
        companion object {
            const val LANG_FILTER = "LANG_FILTER"
            const val GENERE_FILTER = "GENERE_FILTER"
            const val FORMAT_FILTER = "FORMAT_FILTER"
            const val PRICE_FILTER = "PRICE_FILTER"
            const val SHOWTIME_FILTER = "SHOWTIME_FILTER"
            const val ACCESSABILITY_FILTER = "ACCESSABILITY_FILTER"
            const val CINEMA_FORMAT = "CINEMA_FORMAT"
            const val SPECIAL_SHOW = "SPECIAL_SHOW"
        }
    }

    interface SharedPreference {
        companion object {
            const val WALK_THROUGH = "walk"
            const val USER_NAME = "user_name"
            const val USER_TOKEN = "user_token"
            const val CHECK_BOX = "status"
            const val FACEBOOK_USER_ID = "facebook_user_id"
            const val USER_GENDER = "user_gender"
            const val USER_AGE = "user_age"
            const val AUTH_TOKEN = "auth_token"
            const val EXPIRY_TIME = "expiry_time"
            const val SESSION_ID = "session_id"
            const val USER_ID = "user_id"
            const val SHOW_PRESS = "press"
            const val USER_IMAGE = "user_image"
            const val USER_NUMBER = "user_number"
            const val API_CALL = "api_call"
            const val API_CALL2 = "api_call2"
            const val BOOKING_FIRST_INDEX = "booking_first_index"
            const val LOYALITY_POINT = "loyality_point"
            const val pcities = "pcities"
            const val SUBSCRIPTION_STATUS = "subscription_status"
            const val NOT_SUBSCRIBED = "NOT SUBSCRIBED"
            const val ACTIVE = "ACTIVE"
            const val INACTIVE = "INACTIVE"
            const val IS_LOGIN = "is_login"
            const val IS_FOOD = "is_food"
            const val IS_WHATSAPP = "is_whatsapp"
            const val CORPORATE_USER = "corporate_user"
            const val DOB = "dob"
            const val GENDER = "gender"
            const val LOYALITY_STATUS = "loyality_status"
            const val LOYALITY_CARD = "loyality_card"
            const val LOYALITY_CANCEL = "loyality_cancel"
            const val USER_EMAIL = "user_email"
            const val SAVED_ADDRESS = "saved_address"
            const val SAVED_LATITUDE = "lat"
            const val SAVED_LONGITUDE = "lng"
            const val ADDRESS_STATUS = "address_status"
            const val PREFLANG = "preflang"
            const val USER_CARD = "user_card"
            const val IS_REF = "is_ref"
            const val IS_HM = "is_hm"
            const val IS_HF = "is_hf"
            const val IS_WF = "is_wf"
            const val IS_HL = "is_HL"
            const val IS_LY = "is_LY"
            const val LSM = "lsm"
            const val FEEDBACK = "feedback"
            const val CINEMA_FEEDBACK = "cinema_feedback"
            const val LS = "ls"
            const val RM = "rm"
            const val DONATION = "donation"
            const val SVM = "svm"
            const val Pvr_tc = "Pvr_tc"
            const val Pvr_New_Tag = "Pvr_New_Tag"
            const val PS = "ps"
            const val MLIB = "mlib"
            const val RB = "rb"
            const val RBC = "rbc"
            const val NT = "nt"
            const val NTBT = "ntbt"
            const val AR = "app_rate"
            const val AU = "au"
            const val SUBS_OPEN = "subs_open"
            const val USEOFFER = "useroffer"
            const val APPLYUSEOFFER = "applyuseroffer"
            const val SELECTED_CITY_ID = "selectedCityId"
            const val SELECTED_CITY_NAME = "selectedCityName"
            const val TEMP_SELECTED_CITY_NAME = "tempSelectedCityName"
            const val TEMP_SELECTED_CITY_ID = "tempSelectedCityId"
            const val LOCATION_CHANGED = "locationChange"
            const val IMAGE_COUNT = "count"
            const val REDEEMED_COUNT = "redeemed_count"
            const val REDEEMED_SET = "redeemed_set"
            const val GCM_TOKEN = "gcm_token"
            const val PAYMENT_OPTION = "payment"
            const val LOGIN_TYPE = "loginType"
            const val LAT = "lat"
            const val LNG = "lng"
            const val doClear = "doclear"
            const val IS_ONBOARDING = "is_onboarding"
            const val Promo_Bin_Series = "promoBinSeries"
            const val Has_Bin_Series = "hasBinSeries"
            const val PROMOCODE = "promocode"
        }
    }

    fun getLoyaltyQr(mobile_no: String, size: String): String {
        return "https://chart.googleapis.com/chart?cht=qr&chl=$mobile_no&chld=H%7C1&chs=$size"
    }

}