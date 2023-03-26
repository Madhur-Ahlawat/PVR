package com.net.pvr.ui.payment.webView

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.net.UrlQuerySanitizer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr.R
import com.net.pvr.databinding.ActivityPaymentWebBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.payment.mobikwik.response.MobiKwikCheckSumResponse
import com.net.pvr.ui.payment.paytmpostpaid.PaytmPostPaidActivity
import com.net.pvr.ui.payment.paytmpostpaid.viewModel.PaytmPostPaidViewModel
import com.net.pvr.ui.payment.response.PaytmHmacResponse
import com.net.pvr.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class PaytmWebActivity : AppCompatActivity() {
    private var binding: ActivityPaymentWebBinding? = null
    private val authViewModel: PaytmPostPaidViewModel by viewModels()
    private var loader: LoaderDialog? = null
    @Inject
    lateinit var preferences: PreferenceManager
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentWebBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include3?.imageView58?.setOnClickListener {
            onBackPressed()
        }
        binding?.include3?.textView108?.text = intent.getStringExtra("title")
        if (intent.getStringExtra("pTypeId") == "110") {
            authViewModel.airtelPay(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
        }else if (intent.getStringExtra("title") == "Paytm"){
            authViewModel.walletPay(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
        }else if (intent.getStringExtra("title") == "FreeCharge"){
            authViewModel.freechargeAddMoney(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE,intent.getStringExtra("paidAmount").toString()
            )
            freechargeAddMoney()
        }else if (intent.getStringExtra("pTypeId") == Constant.MOBIKWIK){
            authViewModel.mobikwikCheckSum(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE,
                intent.getStringExtra("mobile").toString(),
                intent.getStringExtra("otp").toString()
            )
        }else{
            authViewModel.postPaidPay(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
        }
        mobikwikPay()
        getToken()
        mobikwikCheckSum()
        getWalletToken()
        getAirtelToken()

    }



    private fun getToken() {
        authViewModel.liveDatapayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        postRequestInWebView(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }
    private fun mobikwikCheckSum() {
        authViewModel.mobikwikCheckSumpayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        postRequestInWebViewMobiKwik(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }
    private fun getWalletToken() {
        authViewModel.liveWalletpayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        postRequestInWebView(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun getAirtelToken() {
        authViewModel.airtelPayHmacOldScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        postFormOpenWebView(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }
    private fun freechargeAddMoney() {
        authViewModel.freechargeAddMoneypayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        postFreechargeWebView(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    //    <div  style="display: none;">
    // <form name="airtelform" id="airtelform" action="${AIRTEL_URL}" method="POST">
    //  <input type="hidden" readonly="readonly" name="MID" value="" />
    //  <input type="hidden" readonly="readonly" name="TXN_REF_NO" value="${BookingPaymentVO.payVO.bookingid}" />
    //  <input type="hidden" readonly="readonly" name="SU" value="" />
    //  <input type="hidden" readonly="readonly" name="FU" value="" />
    //  <input type="hidden" readonly="readonly" name="AMT" value="" />
    //  <input type="hidden" readonly="readonly" name="DATE" value="" />
    //  <input type="hidden" readonly="readonly" name="CUR" value="INR" />
    //  <input type="hidden" readonly="readonly" name="CUST_MOBILE" value="${loginVO.mobile}" />
    //  <input type="hidden" readonly="readonly" name=CUST_EMAIL value="${loginVO.email}" />
    // <!-- <input type="hidden" readonly="readonly" name="service" value="NB" />  -->
    //  <input type="hidden" readonly="readonly" name="service" value="WT" />
    //  <input type="hidden" readonly="readonly" name="HASH" value="" />
    // </form>
    //</div>
    private fun postFormOpenWebView(data: PaytmHmacResponse.Output) {
        if (null != data) {
            var urlParams = "MID=" + nullCheck(data.mid)
            urlParams = urlParams + "&TXN_REF_NO=" + nullCheck(data.bookid)
            urlParams = urlParams + "&SU=" + nullCheck(data.forwardurl)
            urlParams = urlParams + "&FU=" + nullCheck(data.forwardurl)
            urlParams = urlParams + "&AMT=" + nullCheck(data.amount)
            urlParams = urlParams + "&DATE=" + nullCheck(data.txndate.toString())
            urlParams = urlParams + "&CUR=" + nullCheck(data.currency)
            urlParams = "$urlParams&CUST_MOBILE=" + nullCheck(
                preferences.geMobileNumber()
            )
            urlParams = "$urlParams&CUST_EMAIL=" + nullCheck(
                preferences.getEmail()
            )
            urlParams = urlParams + "&service=" + nullCheck("WT")
            urlParams = urlParams + "&HASH=" + nullCheck(data.hmackey)
            val url: String = data.callingurl
            postWebView(url, urlParams)
        }
    }

    private fun postFreechargeWebView(data: PaytmHmacResponse.Output) {
        if (null != data) {
            var urlParams = "amount=" + data.amt
            urlParams = urlParams + "&callbackUrl=" + data.cbk
            urlParams = urlParams + "&channel=" + data.pt
            urlParams = urlParams + "&checksum=" + data.chk
            urlParams = urlParams + "&loginToken=" + data.token
            urlParams = urlParams + "&merchantId=" + data.mid
            urlParams = urlParams + "&metadata=" + data.mta
            urlParams = urlParams + "&HASH=" + nullCheck(data.hmackey)
            val url: String = data.fcurl
            postWebView(url, urlParams)
        }
    }

    private fun nullCheck(`val`: String?): String? {
        return `val` ?: ""
    }

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    private fun postRequestInWebView(data: PaytmHmacResponse.Output) {
        var urlParams = "REQUEST_TYPE=" + "DEFAULT"
        urlParams = urlParams + "&MID=" + data.mid
        urlParams = urlParams + "&ORDER_ID=" + data.bookingid
        urlParams = urlParams + "&CUST_ID=" + data.cust_id
        urlParams = urlParams + "&TXN_AMOUNT=" + data.amount
        urlParams = "$urlParams&CHANNEL_ID=WEB"
        urlParams = urlParams + "&INDUSTRY_TYPE_ID=" + data.industry_type
        urlParams = urlParams + "&WEBSITE=" + data.paytm_website
        var hmac = ""
        try {/*
               hmac = new String(data.output.hmackey.getBytes(), "UTF-8");
            Pvrlog.write("hmac encoded",hmac);*/hmac =
                URLEncoder.encode(data.hmackey, "utf-8")
            //data.output.hmackey.stringByAddingPercentEncodingWithAllowedCharacters(.URLHostAllowedCharacterSet())
        } catch (e: java.lang.Exception) {
            Log.e("utf8", "conversion", e)
        }
        urlParams = "$urlParams&CHECKSUMHASH=$hmac"
        urlParams = urlParams + "&CALLBACK_URL=" + data.forwardurl
        val url: String = data.callingurl
        val webSettings: WebSettings? = binding?.webView?.settings
        webSettings?.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webSettings?.domStorageEnabled = true
        CookieSyncManager.createInstance(this)
        CookieSyncManager.getInstance().startSync()
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(binding?.webView, true)
        }
        postWebView(url, urlParams)
        printLog("data--->${url}--->${data}")

    }

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    private fun postRequestInWebViewMobiKwik(data: MobiKwikCheckSumResponse.Output) {
        var postData = ""
        try {
            postData = "checksum=" + URLEncoder.encode(
                data.checksum,
                "UTF-8"
            ) + "&redirecturl=" + data.redirecturl.replace(
                "http://demoapi.pvrcinemas.com//",
                "http://demoapi.pvrcinemas.com/"
            ) + "&otp=" + URLEncoder.encode(
                intent.getStringExtra("otp"),
                "UTF-8"
            ) + "&mid=" + URLEncoder.encode(
                data.merchantid,
                "UTF-8"
            ) + "&merchantname=" + URLEncoder.encode(
                data.merchantname,
                "UTF-8"
            ) + "&orderid=" + URLEncoder.encode(
                data.bookingid,
                "UTF-8"
            ) + "&cell=" + URLEncoder.encode(intent.getStringExtra("mobile"), "UTF-8") + "&amount=" + URLEncoder.encode(
                data.amount,
                "UTF-8"
            )
            val webSettings: WebSettings = binding?.webView?.settings!!
            val webview = binding?.webView
            webSettings.javaScriptEnabled = true
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//            webview.addJavascriptInterface(
//                com.net.pvr.ui.paymentgateway.mobikwik.PcMobikwikAddMoneyActivity.MyJavaScriptInterface(
//                    this
//                ), "ResponseViewer"
//            )
//            setContentView(webview)
            webview?.requestFocus(View.FOCUS_DOWN)
            webview?.postUrl(data.callingurl, postData.toByteArray())
            webview?.webChromeClient = object : WebChromeClient() {}

            webview?.webViewClient = object : WebViewClient() {
                @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
               override fun onReceivedError(
                    view: WebView,
                    errorCode: Int,
                    description: String,
                    failingUrl: String
                ) {
                    println("$errorCode,$description,$failingUrl")
                }



                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    println("onPageFinished---$url")
                    try {
                        try {
                            if (url.contains("mobimobresp")) {
                                val sanitzer = UrlQuerySanitizer(url)
                                val value = sanitzer.getValue("result")
                                if (value.equals("fail", ignoreCase = true)) {
                                    val sanitzer1 = UrlQuerySanitizer(url)
                                    val value1 = sanitzer1.getValue("errorStatus")
                                        .replace("\\+".toRegex(), " ")
                                    val dialog = OptionDialog(this@PaytmWebActivity,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        "$value1",
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                            launchActivity(
                                                HomeActivity::class.java,
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            )
                                        },
                                        negativeClick = {

                                        })
                                    dialog.show()
                                } else if (value.equals("success", ignoreCase = true)) {
                                    authViewModel.mobikwikPay(
                                        preferences.getUserId(),
                                        Constant.BOOKING_ID,
                                        Constant.TRANSACTION_ID,
                                        Constant.BOOK_TYPE, intent.getStringExtra("mobile").toString(),intent.getStringExtra("otp").toString(),
                                        Constant.CINEMA_ID
                                    )
                                }
                            } else {

                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    } catch (e1: java.lang.Exception) {
                        e1.printStackTrace()
                    }
                }
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }

    private fun postWebView(url: String?, data: String?) {
        data?.let { pp(url, it) }
    }

    @SuppressLint("ObsoleteSdkInt", "SetJavaScriptEnabled")
    fun pp(url: String?, data: String) {

        val webSettings = binding?.webView?.settings
        webSettings?.javaScriptEnabled = true
        webSettings?.domStorageEnabled = true
        binding?.webView?.settings?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        binding?.webView?.webViewClient = object : WebViewClient() {


            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                printLog("shouldOverrideUrlLoading--->${url}")

                return if (url.contains("mobimobresp") || url.contains("paytmexmobresp") || url.contains("recurring")) {
                    true
                } else {
                    super.shouldOverrideUrlLoading(view, url)
                }

            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                printLog("failUrl--->${url}")
                try {
                    try {
                        if (url.contains("paytmexmobresp") || url.contains("recurring")) {
                            //                            Pvrlog.write2("==host match=true=", url)
                            val sanitizer = UrlQuerySanitizer(url)
                            val value = sanitizer.getValue("result")
                            if (value == "fail") {
                                val sanitizer1 = UrlQuerySanitizer(url)
                                val value1 =
                                    sanitizer1.getValue("errorStatus").replace("_".toRegex(), " ")
                                        .replace("\\+".toRegex(), " ")
                                if (Constant.BOOK_TYPE.equals(
                                        "GIFTCARD", ignoreCase = true
                                    ) || Constant.BOOK_TYPE.equals(
                                        "LOYALTYUNLIMITED", ignoreCase = true
                                    ) || Constant.BOOK_TYPE.equals("CINE_MOVIE_PASS", ignoreCase = true)
                                ) {

                                    val dialog = OptionDialog(this@PaytmWebActivity,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                          "$value1",
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                            launchActivity(
                                                HomeActivity::class.java,
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            )
                                        },
                                        negativeClick = {

                                        })
                                    dialog.show()

                                } else {

                                    val dialog = OptionDialog(this@PaytmWebActivity,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        "$value1",
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {

                                        },
                                        negativeClick = {

                                        })
                                    dialog.show()
                                }
                            } else if (value.equals("success", ignoreCase = true)) {
                               Constant().printTicket(this@PaytmWebActivity)
                            }
                        } else if (url.contains("mobimobresp")){
                            val sanitzer = UrlQuerySanitizer(url)
                            val value = sanitzer.getValue("result")
                            val value1 = sanitzer.getValue("errorStatus").replace("_".toRegex(), " ")
                                    .replace("\\+".toRegex(), " ")
                            if (value == "fail"){
                                val dialog = OptionDialog(this@PaytmWebActivity,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    "$value1",
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {
                                        launchActivity(
                                            HomeActivity::class.java,
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        )
                                    },
                                    negativeClick = {

                                    })
                                dialog.show()
                            }else{
                                // Mobikwik Query
                                authViewModel.mobikwikPay(
                                    preferences.getUserId(),
                                    Constant.BOOKING_ID,
                                    Constant.TRANSACTION_ID,
                                    Constant.BOOK_TYPE, intent.getStringExtra("mobile").toString(),intent.getStringExtra("otp").toString(),
                                    Constant.CINEMA_ID
                                )
                            }
                        }else if (url.contains("fcmobresp")){
                            val sanitzer = UrlQuerySanitizer(url)
                            val value = sanitzer.getValue("result")
                            val value1 = sanitzer.getValue("errorMessage").replace("_".toRegex(), " ")
                                    .replace("\\+".toRegex(), " ")
                            if (value == "fail"){
                                val dialog = OptionDialog(this@PaytmWebActivity,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    "$value1",
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {
                                        launchActivity(
                                            HomeActivity::class.java,
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        )
                                    },
                                    negativeClick = {

                                    })
                                dialog.show()
                            }else{
                                // Mobikwik Query
                                PaytmPostPaidActivity.callFc = true
                                onBackPressed()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } catch (e1: Exception) {
                    e1.printStackTrace()
                }
            }


        }
        if (data.contains("SUBSCRIPTION")) url?.let {
            binding?.webView?.postUrl(
                it, data.toByteArray()
            )
        } else url?.let { binding?.webView?.postUrl(it, data.toByteArray()) }
    }

    override fun onBackPressed() {
        if (!PaytmPostPaidActivity.callFc) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                "Do you want to end the session?",
                positiveBtnText = R.string.yes,
                negativeBtnText = R.string.cancel,
                positiveClick = {
                    launchActivity(
                        HomeActivity::class.java,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    )
                },
                negativeClick = {
                })
            dialog.show()
        }else{
            super.onBackPressed()
        }
    }


    private fun mobikwikPay() {
        authViewModel.mobikwikPaypayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.statuscode == "33"){
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                getString(R.string.you_have_insufficient_balance),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
//                                    val intent = Intent(this@MobikwikLoginActivity, PaytmWebActivity::class.java)
//                                    intent.putExtra("pTypeId", intent.getStringExtra("pid"))
//                                    intent.putExtra("mobile", binding?.mobileNumber?.text.toString())
//                                    intent.putExtra("otp", binding?.etOtp?.getStringFromFields().toString())
//                                    intent.putExtra("paidAmount", paidAmount)
//                                    intent.putExtra("title", title)
//                                    startActivity(intent)
                                },
                                negativeClick = {})
                            dialog.show()
                        }else if (it.data.output.statuscode == "159") {
                            // Create Wallet

                        }else if (it.data.output.statuscode == "164"){
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.output.statusdescription,
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()
                        }else if (it.data.output.statuscode == "0"){
                            if (it.data.output.status == "SUCCESS"){
                                Constant().printTicket(this)
                                finish()
                            }else{
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    it.data?.msg.toString(),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            }
                        }else{
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data?.msg.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()
                        }

                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }



}