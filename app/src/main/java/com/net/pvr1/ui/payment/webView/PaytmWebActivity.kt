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
import com.net.pvr1.di.preference.PreferenceManager
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
        if (intent.getStringExtra("pTypeId") == "102") {
            authViewModel.airtelPay(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
        }else if (intent.getStringExtra("pTypeId") == "112"){
            authViewModel.walletPay(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
        }else{
            authViewModel.postPaidPay(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
        }
        getToken()
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