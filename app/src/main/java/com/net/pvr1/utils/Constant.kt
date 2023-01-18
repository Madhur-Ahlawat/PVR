package com.net.pvr1.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Color
import android.location.*
import android.net.Uri
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.provider.Settings
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.ScaleXSpan
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.net.pvr1.R
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.home.fragment.more.response.ProfileResponse
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.ui.ticketConfirmation.TicketConfirmationActivity
import okhttp3.internal.and
import java.net.MalformedURLException
import java.net.URL
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class Constant {

    companion object {
        const val platform = "ANDROID"
        const val version = "11.3"
        const val status = "success"
        const val SUCCESS_CODE = 10001
        var latitude: Double? = 0.0
        var longitude: Double? = 0.0

        const val donation = "https://www.pvrcinemas.com/pvrstatic/donation/tnc.html"
        const val pvrCare = " https://www.pvrcinemas.com/pvrstatic/pvr-care/index.html"
        const val merchandise = "https://pvr.macmerise.com/?user_agent=pvr"
        const val termsCondition = "https://www.pvrcinemas.com/pvrstatic/tnc.html"
        const val termsUse = "https://www.pvrcinemas.com/pvrstatic/pvr-terms.html"
        const val privacy = "https://www.pvrcinemas.com/pvrstatic/pvr-privacy.html"

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
        var DISCOUNT = 0.0
        var BOOKING_ID = "0"
        var CINEMA_ID = "0"
        var OfferDialogImage = "0"
        var SELECTED_SEAT = 0
        var SESSION_ID = "0"
        var PRIVILEGEPOINT = "0"
        var PRIVILEGEVOUCHER = "0"
        var FOODENABLE = 0
        const val CITY = "Delhi-NCR"
        const val CITY_CC = "City-Name"
        const val LATITUDE = "lat"
        const val LONGITUDE = "lang"
        var BOOK_TYPE = "BOOKING"
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
        const val LETTER_SPACING = 1
        var PrivilegeHomeResponseConst: PrivilegeHomeResponse.Output? = null
        var ProfileResponseConst: ProfileResponse.Output? = null
        var PlaceHolder: HomeResponse.Output? = null
        var DECIFORMAT = DecimalFormat("0.000")

        // DisCount for payment

        var discount_val = "0.0"
        var isPromoCode = ""
        var discount_txt = ""


        // Payment Ids
        var CREDIT_CARD = "116"
        var DEBIT_CARD = "117"
        var NET_BANKING = "118"
        var PAYTMPOSTPAID = "124"
        var PAYTM_WALLET = "102"
        var CRED = "125"
        var ZAGGLE = "126"
        var UPI = "119"
        var PHONE_PE = "113"
        var GEIFT_CARD = "105"
        var AIRTEL = "110"
        var PRICE_DESK = "101"
        var PAYTM = "102"
        var MOBIKWIK = "103"
        var OXYGEN = "104"
        var DC_CARD = "106"
        var OFFER_OPTION = "00000"
        var BIN_OPTION = "000000"
        var PAYTM_DEBIT_CARD = "117"
        var PAYTM_CREDIT_CARD = "116"
        var PAYTM_NETBANKING = "118"

        var NETBANKING = "109"
        var TEJ = "109"
        var TEZUPI = "111"
        var PAYTMUPI = "119"
        var EPAY_LATTER = "115"
        var PAYPAL = "114"


        var KOTAK_SATURDAY = "o101"
        var KOTAK_WEEKEND = "130"
        var KOTAK_CREDIT_CARD = "125"


        // OFFERS
        var PROMOCODE = "O104"
        var ACCENTIVE = "o111"
        var M_COUPON = "O102"
        var STAR_PASS = "O105"
        var HYATT = "O107"
        var GYFTR = "O108"

        fun isPackageInstalled(packageManager: PackageManager): Boolean {
            return try {
                packageManager.getPackageInfo("com.dreamplug.androidapp.dev", 0)
                true
            } catch (e: NameNotFoundException) {
                e.printStackTrace()
                println("NameNotFoundException---" + e.message)
                false
            }
        }

        fun onShareClick(context: Context, shareUrl: String, shareMessage: String) {
            /*System.out.println("ShareUrl"+shareURL+"fkfg"+shareMessage);*/
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "$shareMessage $shareUrl")
            sendIntent.type = "text/plain"
            context.startActivity(sendIntent)
        }

        fun getDid(): String {
            return ""
        }

        @Throws(Exception::class)
        fun getHash(text: String): String {
            val mdText = MessageDigest.getInstance("SHA-512")
            val byteData = mdText.digest(text.toByteArray())
            val sb = StringBuffer()
            for (i in byteData.indices) {
                sb.append(((byteData[i] and 0xff) + 0x100).toString(16).substring(1))
            }
            return sb.toString()
        }

        fun getDate(oldformat: String, newformat: String, olddate: String): String? {
            return try {
                val newDateString: String
                val sdf = SimpleDateFormat(oldformat)
                val sdf1 = SimpleDateFormat(newformat)
                var d: Date? = null
                val d1: Date? = null
                d = try {
                    sdf.parse(olddate)
                } catch (e: ParseException) {
                    e.printStackTrace()
                    sdf1.parse(olddate)
                }
                sdf.applyPattern(newformat)
                newDateString = sdf.format(d)
                newDateString
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                ""
            }
        }


    }

    fun getDeviceId(context: Activity): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }


    fun spannableText(
        activityContext: Activity, stringBuilder: StringBuilder?, tvCensorLang: TextView
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


    fun openMap(context: Context, lat: String, lang: String) {
        val strUri = "http://maps.google.com/maps?q=loc:$lat,$lang (Label which you want)"
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
                activity.currentFocus!!.windowToken, 0
            )
        }
    }


    fun printTicket(activity: Activity) {
        val intent = Intent(
            activity, TicketConfirmationActivity::class.java
        )
        activity.startActivity(intent)
        activity.finish()
    }

    fun shareData(activity: Activity, title: String, shareUrl: String) {
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
                            Html.fromHtml(tv.text.toString()), tv, maxLine, expandText, viewMore
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
                            Html.fromHtml(tv.text.toString()), tv, maxLine, expandText, viewMore
                        ), TextView.BufferType.SPANNABLE
                    )
                } else {
                    val lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                    val text = tv.text.subSequence(0, lineEndIndex).toString() + "" + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextViewResizable(
                            Html.fromHtml(tv.text.toString()),
                            tv,
                            lineEndIndex,
                            expandText,
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
        strSpanned: Spanned, tv: TextView, maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : MySpannable(false) {
                override fun onClick(widget: View) {
                    super.onClick(widget)
                    if (viewMore) {
                        tv.layoutParams = tv.layoutParams
                        tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                        tv.invalidate()
                        makeTextViewResizable(tv, -1, "", false)
                    } else {
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


    fun getAppVersion(activity: Activity): String? {
        val packageInfo: PackageInfo
        try {
            packageInfo = activity.packageManager.getPackageInfo(activity.packageName, 0)
            if (packageInfo != null) return packageInfo.versionName
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    fun applyLetterSpacing(pcTextView: TextView, text: String, letterspacing: Int) {
        val builder = StringBuilder()
        for (i in 0 until text.length) {
            builder.append(text[i])
            if (i + 1 < text.length) {
                builder.append("\u00A0")
            }
        }
        val finalText = SpannableString(builder.toString())
        val letterSpacingBuf = if (letterspacing == -1) 0.1f else (letterspacing + 1).toFloat()
        if (builder.toString().length > 1) {
            var i = 1
            while (i < builder.toString().length) {
                finalText.setSpan(
                    ScaleXSpan(letterSpacingBuf / 10), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                i += 2
            }
        }
        pcTextView.text = finalText
    }

    //location Mange
    private val requestLocation = 1
    private var locationManager: LocationManager? = null


    //Manage Location
    fun enableLocation(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestLocation)
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) != true) {
            onGPS(activity)
        } else {
            getLocation(activity)
        }
    }

    //location
    private fun onGPS(activity: Activity) {
        val dialog = OptionDialog(activity,
            R.mipmap.ic_launcher,
            R.string.app_name,
            "Enable GPS",
            positiveBtnText = R.string.ok,
            negativeBtnText = R.string.no,
            positiveClick = {
                activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            },
            negativeClick = {})
        dialog.show()
    }

    private fun getLocation(activity: Activity) {
        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestLocation
            )
        } else {
            locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val criteria = Criteria()
            val bestProvider = locationManager?.getBestProvider(criteria, false)
            val location: Location? =
                locationManager?.getLastKnownLocation(bestProvider.toString())

            if (location == null) {
                Toast.makeText(activity, "Location Not found", Toast.LENGTH_LONG).show()
            } else {
                val geocoder = Geocoder(activity)
                try {
                    val user = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    latitude = user?.get(0)?.latitude as Double
                    longitude = user[0]?.longitude as Double
                    val geocoder = Geocoder(activity, Locale.getDefault())
                    val addresses: List<Address>? = geocoder.getFromLocation(latitude!!, longitude!!, 1)
                    val cityName: String? = addresses?.get(0)?.getAddressLine(0)
                    val stateName: String? = addresses?.get(0)?.getAddressLine(1)
                    val countryName: String? = addresses?.get(0)?.getAddressLine(2)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun locationServicesEnabled(context: Context): Boolean {
        var locationMode = 0
        var isAvailable = false
        try {
            locationMode =
                Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        isAvailable = locationMode != Settings.Secure.LOCATION_MODE_OFF
        val coarsePermissionCheck = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val finePermissionCheck = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return isAvailable && (coarsePermissionCheck || finePermissionCheck)
    }

    @SuppressLint("SimpleDateFormat")
    fun changeDateFormat(date: String?): String? {
        return if (date != null && date != "") {
            try {
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val d = sdf.parse(date)
                val displayFormat = SimpleDateFormat("dd MMM, yyyy")
                d?.let { displayFormat.format(it) }
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        } else {
            ""
        }
    }

//    shimer
    fun getData(textView: TextView?, textViewSecond: TextView?) {
        try {
            val titles = arrayOf(
                "“You shoot me in a dream, you better wake up and apologize.”<br><i><small>(Reservoir Dogs)</i></small>",
                "“I will have my vengeance, in this life or the next.”<br><i><small>(Gladiator)</i></small>",
                "“I’m the king of the world!”<br><i><small>(Titanic)</i></small>",
                "“Hasta la vista baby.”<br><i><small>(Terminator)</i></small>",
                "“I must break you.”<br><i><small>(Rocky IV)</i></small>",
                "“They may take our lives but they’ll never take our freedom.”<br><i><small>(Braveheart)</i></small>",
                "“If you are good at something, never do it for free.”<br><i><small>(The Dark Knight)</i></small>",
                "“Do I really look like a guy with a plan?”<br><i><small>(The Dark Knight)</i></small>",
                "“Frankly my dear, I don’t give a damn.”<br><i><small>(Gone With The Wind)</i></small>",
                "“I’m gonna make him an offer he can’t refuse.”<br><i><small>(The Godfather)</i></small>",
                "“This life’s hard, man, but it’s harder if you are stupid.”<br><i><small>(The Friends Of Eddie Coyle)</i></small>",
                "“You mustn’t be afraid to dream a little bigger, darling.”<br><i><small>(Inception)</i></small>",
                "“Don’t let anybody tell you that you can’t do something.”<br><i><small>(The Pursuit Of Happiness)</i></small>",
                "“It was Beauty killed the Beast.”<br><i><small>(The King-Kong)</i></small>",
                "“They call it a Royale with cheese.”<br><i><small>(Pulp Fiction)</i></small>",
                "“Magic Mirror on the wall, who is the fairest one of all?”<br><i><small>(Snow White And The 7 Dwarves)</i></small>",
                "“My Precious.”<br><i><small>(The Lord Of The Rings)</i></small>",
                "“Help me, Obi-Wan Kenobi. You're my only hope.”<br><i><small>(StarWars, 1997)</i></small>",
                "“Just keep swimming.”<br><i><small>(Finding Nemo)</i></small>",
                "“I am big! It's the pictures that got small.”<br><i><small>(Sunset Boulevard)</i></small>",
                "“Keep your friends close but your enemies closer.”<br><i><small>(The Godfather, part II)</i></small>",
                "“The greatest trick the devil ever pulled was convincing the world he didn't exist.”<br><i><small>(The Usual Suspects)</i></small>",
                "“Show me the money!”<br><i><small>(Jerry Maguire)</i></small>",
                "“Carpe diem. Seize the day, boys.”<br><i><small>(Dead Poets Society)</i></small>",
                "“We don’t read and write poetry because it’s cute, we read and write poetry because we are members of the human race. And the human race is filled with passion.”<br><i><small>(Dead Poets Society)</i></small>",
                "“There’s no crying in baseball.”<br><i><small>(A League Of Their Own)</i></small>",
                "“You had me at hello.”<br><i><small>(Jerry Maguire)</i></small>",
                "“To infinity and beyond.”<br><i><small>(Toy Story)</i></small>",
                "“Why so serious?”<br><i><small>(The Dark Knight)</i></small>"
            )
            val r = Random()
            val randomNumber = r.nextInt(titles.size)
            textView?.text = Html.fromHtml(titles[randomNumber])
            if (textViewSecond != null && textViewSecond.text.toString()
                    .equals(textView?.text.toString(),ignoreCase = true)
            ) {
                val randomNumber1 = r.nextInt(titles.size)
                textView?.text = Html.fromHtml(titles[randomNumber1])
                textViewSecond.text = Html.fromHtml(titles[randomNumber1])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //AppBar Hide
    fun appBarHide(activity: Activity){
        activity.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            statusBarColor = Color.TRANSPARENT
        }
    }

    fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    interface PCBackStackActivity {
        companion object {
            const val LOYALITY_SPECIFICATION_ACTIVITY = "loyalityspecificationactivity"
            const val LOYALITY_NONMEMBER_ACTIVITY = "loyalitynonmemberactivity"
            const val LOYALITY_UNLIMITED_ACTIVITY = "loyalityunlimited"
            const val MAGAZINE_ACTIVITY = "magazineactivity"
            const val SOUND_LIKE_PLANE_ACTIVITY = "soundlikeplan"
            const val THEATER_ACTIVITY = "theatreActivity"
            const val ALERT_ACTIVITY = "alertActivity"
            const val THEATER_DETAILS = "theaterdetailsActivity"
            const val PCSCAN_ACTIVITY = "PCCouponScan"
            const val GRAB_ACTIVITY = "GrabABiteActivity"
            const val VKAAO_PAGE = "PCTheatreDemandActivity"
            const val PCPRE_ACTIVITY = "PCPreBookActivity"
            const val MYTICKET_ACTIVITY = "myTicketActivity"
            const val PREFERENCES_ACTIVITY = "preferencesActivity"
            const val GIFT_CARD_ACTIVITY = "giftCardActivity"
            const val OFFER_ACTIVITY = "offerActivity"
            const val LANDING_ACTIVITY = "landingActivity"
            const val SPLASH_ACTIVITY = "splashActivity"
            const val OPEN_ACTIVITY_NAME = "activity_name"
            const val SOCIAL_INFORMATION = "social_information"
            const val ORDER_SNACKS = "ordersancks"
            const val ORDER_SUMMARY = "ordersummary"
            const val IN_SEAT_DINING = "inSeatDining"
        }
    }
}