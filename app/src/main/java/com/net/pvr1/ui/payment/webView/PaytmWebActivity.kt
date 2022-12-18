package com.net.pvr1.ui.payment.webView

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.UrlQuerySanitizer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EncodingUtils
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPaymentWebBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.payment.paytmpostpaid.viewModel.PaytmPostPaidViewModel
import com.net.pvr1.ui.payment.response.PaytmHmacResponse
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
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
        }else{
            authViewModel.postPaidMakePayment(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
        }
        getToken()
        getAirtelToken()

    }



    private fun getToken() {
        authViewModel.liveDatapaytmHmacOldScope.observe(this) {
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
                    loader = LoaderDialog(R.string.pleasewait)
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
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

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                printLog("shouldOverrideUrlLoading--->${url}")

                return if (url.contains("paytmexmobresp") || url.contains("recurring")) {
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
                                        getString(R.string.ok) + "$value1",
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                            finish()
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
                        } else {
                            printLog("---->")
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
        } else url?.let { binding?.webView?.postUrl(it, EncodingUtils.getBytes(data, "BASE64")) }
    }

    override fun onBackPressed() {
        val dialog = OptionDialog(this,
            R.mipmap.ic_launcher,
            R.string.app_name,
            "Do you want to end the session?",
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
    }


}