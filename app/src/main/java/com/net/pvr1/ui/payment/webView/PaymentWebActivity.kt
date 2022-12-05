package com.net.pvr1.ui.payment.webView

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.UrlQuerySanitizer
import android.os.Build
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EncodingUtils
import com.net.pvr1.databinding.ActivityPaymentWebBinding
import com.net.pvr1.utils.Constant.Companion.BOOKING_ID
import com.net.pvr1.utils.Constant.Companion.BOOK_TYPE
import com.net.pvr1.utils.printLog
import dagger.hilt.android.AndroidEntryPoint
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

@Suppress("DEPRECATION")
@AndroidEntryPoint
class PaymentWebActivity : AppCompatActivity() {
    private var binding: ActivityPaymentWebBinding? = null
    private var from: String = ""
    private var title: String = ""
    private var get: String = ""

    private var bookType = ""
    private var saveCard = "0"
    private var subscriptionId = ""
    private var saveCardId = ""
    // Specify the type of payment mode (DEBIT,CREDIT,NET_BANKING)

    // Specify the type of payment mode (DEBIT,CREDIT,NET_BANKING)
    private var cvv = ""
    private var ccnumber: String? = ""
    private var expmonth: String? = ""
    private var expyear: String? = ""
    private var bankname: String? = ""
    private var amount = ""
    private var checksum = ""
    private var mid: String? = ""
    private var currency: String? = ""
    private var token: String? = ""

    var pType = ""
    var payerAccount = ""

    var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentWebBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        from = intent.getStringExtra("from").toString()
        title = intent.getStringExtra("title").toString()
        get = intent.getStringExtra("getUrl").toString()

        webView = binding?.webView
        if (intent.getStringExtra("token") != null) {
            token = intent.getStringExtra("token")
        }
        if (intent.getStringExtra("mid") != null) {
            mid = intent.getStringExtra("mid")
        }
        if (intent.getStringExtra("saveCardId") != null) {
            saveCardId = intent.getStringExtra("saveCardId")!!
        }
        if (intent.getStringExtra("amount") != null) {
            amount = intent.getStringExtra("amount")!!
        }

        if (intent.getStringExtra("cvv") != null) {
            cvv = intent.getStringExtra("cvv")!!
        }
        if (intent.getStringExtra("ccnumber") != null) {
            ccnumber = intent.getStringExtra("ccnumber")
        }
        if (intent.getStringExtra("expmonth") != null) {
            expmonth = intent.getStringExtra("expmonth")
        }
        if (intent.getStringExtra("expyear") != null) {
            expyear = intent.getStringExtra("expyear")
        }
        if (intent.getStringExtra("channelCode") != null) {
            bankname = intent.getStringExtra("channelCode")
        }
        if (intent.getStringExtra("paymenttype") != null) {
            pType = intent.getStringExtra("paymenttype")!!
        }

        if (intent.getStringExtra("checksum") != null) {
            checksum = intent.getStringExtra("checksum")!!
        }

        if (intent.getStringExtra("currency") != null) {
            currency = intent.getStringExtra("currency")
        }

        if (intent.getStringExtra("saveCard") != null) {
            saveCard = intent.getStringExtra("saveCard")!!
        }

        if (intent.getStringExtra("subscriptionId") != null) {
            subscriptionId = intent.getStringExtra("subscriptionId")!!
        }

        if (intent.getStringExtra("payerAccount") != null) {
            payerAccount = intent.getStringExtra("payerAccount")!!
        }

        if (intent.getStringExtra(BOOK_TYPE) != null) {
            bookType = intent.getStringExtra(BOOK_TYPE)!!
        }


    }

    @Throws(UnsupportedEncodingException::class)
    private fun getToken() {

        //CC ,DC,NB
        var urlParams = "&mid=$mid"
        urlParams = "$urlParams&amount=$amount"
        urlParams = urlParams + "&orderId=" + BOOKING_ID
        urlParams = "$urlParams&channelId=WAP"
        if (pType.equals("NB", ignoreCase = true)) {
            pType = "NET_BANKING"
            urlParams = "$urlParams&channelCode=$bankname"
        } else if (pType.equals("CC", ignoreCase = true)) {
            pType = "CREDIT_CARD"
            urlParams = "$urlParams&cardInfo=$saveCardId|$ccnumber|$cvv|$expmonth$expyear"
        } else if (pType.equals("DC", ignoreCase = true)) {
            pType = "DEBIT_CARD"
            urlParams = "$urlParams&cardInfo=$saveCardId|$ccnumber|$cvv|$expmonth$expyear"
        } else if (pType.equals("UPI", ignoreCase = true)) {
            pType = "UPI"
            urlParams = "$urlParams&payerAccount=$payerAccount&channelCode=$bankname"
        } else if (pType.equals("UPI", ignoreCase = true)) {
            pType = "UPI"
            urlParams = "$urlParams&payerAccount=$payerAccount&channelCode=$bankname"
        }
        urlParams = "$urlParams&txnToken=$token"
        urlParams = "$urlParams&paymentMode=$pType"
        urlParams = "$urlParams&authMode=otp"
        urlParams =
            if (pType.equals(
                    "UPI",
                    ignoreCase = true
                )
            ) "$urlParams&storeInstrument=" else "$urlParams&storeInstrument=$saveCard"


        //urlParams= URLEncoder.encode(urlParams,"UTF-8");
//        Pvrlog.write("==urlparam==", urlParams)
        val url = checksum
        if (subscriptionId != "") {
            var recurringparams = ""
            recurringparams =
                "$recurringparams&cardInfo=$saveCardId|$ccnumber|$cvv|$expmonth$expyear"
            recurringparams = "$recurringparams&paymentMode=$pType"
            recurringparams = "$recurringparams&authMode=otp"
            recurringparams = "$recurringparams&SUBSCRIPTION_ID=$subscriptionId"
            recurringparams = "$recurringparams&txnToken=$token"
            printLog("--->mix${"$recurringparams"}")

            val cardInfo = "$saveCardId|$ccnumber|$cvv|$expmonth$expyear"
            val postData = ("&cardInfo=" + URLEncoder.encode(
                cardInfo,
                "UTF-8"
            ) + "&paymentMode=" + URLEncoder.encode(pType, "UTF-8")
                    + "&authMode=" + URLEncoder.encode(
                "otp",
                "UTF-8"
            ) + "&SUBSCRIPTION_ID=" + URLEncoder.encode(subscriptionId, "UTF-8")
                    + "&txnToken=" + URLEncoder.encode(token, "UTF-8"))
            postRequestInWebView(url, postData)
            //            webView.postUrl(url,postData.getBytes());

//            hitWebViewUrl(url);
        } else postRequestInWebView(url, urlParams)
//        String furl=url+urlParams;


        //openWebView(furl);
    }

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    private fun postRequestInWebView(url: String, data: String) {
        val webSettings: WebSettings? = webView?.settings
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
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        }
        postWebView(url, data)
    }

    private fun postWebView(url: String?, data: String?) {
        data?.let { pp(url, it) }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun pp(url: String?, data: String) {
        webView?.webChromeClient = object : WebChromeClient() {}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            printLog("--->mix${"true"}")
            webView?.settings?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webView?.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                println("$errorCode,$description,$failingUrl")
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                //                dialog.show();
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                printLog("1----->${url}")
                /*URI uri = null;
                    URL uu=null;
                    try {
                        uri = new URI(url);
                        try {
                            uu = uri.toURL();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }*/return if (url.contains("paytmexmobresp") || url.contains("recurring")) {
                    true
                } else {
                    super.shouldOverrideUrlLoading(view, url)
                }

                //return true;
                // return super.shouldOverrideUrlLoading(view, url);


                // ...
                // return false;
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                printLog("2----->${url}")

                //                Pvrlog.write("==on page finish==", url)
                try {
                    try {
                        if (url.contains("paytmexmobresp") || url.contains("recurring")) {
                            //                            Pvrlog.write2("==host match=true=", url)
                            val sanitzer = UrlQuerySanitizer(url)
                            val value = sanitzer.getValue("result")
                            if (value.equals("fail", ignoreCase = true)) {
                                val sanitzer1 = UrlQuerySanitizer(url)
                                val value1 =
                                    sanitzer1.getValue("errorStatus").replace("_".toRegex(), " ")
                                        .replace("\\+".toRegex(), " ")
                                if (bookType.equals(
                                        "GIFTCARD",
                                        ignoreCase = true
                                    ) || bookType.equals(
                                        "LOYALTYUNLIMITED",
                                        ignoreCase = true
                                    ) || bookType.equals("CINE_MOVIE_PASS", ignoreCase = true)
                                ) {
                                    //                                    DialogClass.taskClearDialog(
                                    //                                        context, value1,
                                    //                                        getString(R.string.ok), "",
                                    //                                        "", paymentType, paymentIntentData.getBookingID()
                                    //                                    )
                                } else {
                                    //                                    DialogClass.taskClearDialog(
                                    //                                        context,
                                    //                                        value1,
                                    //                                        getString(R.string.ok),
                                    //                                        paymentIntentData.getCinemaID(),
                                    //                                        paymentIntentData.getTransactionID(),
                                    //                                        paymentType,
                                    //                                        paymentIntentData.getBookingID()
                                    //                                    )
                                }
                            } else if (value.equals("success", ignoreCase = true)) {
                                //                                TicketView.makeTicket(paymentIntentData, context, bookType)
                                finish()
                            }
                        } else {
                            //                            Pvrlog.write2("==host match=false=", "")
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
            webView?.postUrl(
                it,
                data.toByteArray()
            )
        } else url?.let { webView?.postUrl(it, EncodingUtils.getBytes(data, "BASE64")) }
    }

}