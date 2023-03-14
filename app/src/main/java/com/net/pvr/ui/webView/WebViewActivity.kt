package com.net.pvr.ui.webView

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityWebViewBinding
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {
    private var binding: ActivityWebViewBinding? = null
    private var from: String = ""
    private var title: String = ""
    private var get: String = ""

    private var loader: LoaderDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        from = intent.getStringExtra("from").toString()
        title = intent.getStringExtra("title").toString()
        get = intent.getStringExtra("getUrl").toString()
        movedNext()
        binding?.webView?.settings?.builtInZoomControls = true

             if (from == "Experience") {
                 requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                binding?.webView?.settings?.setSupportMultipleWindows(true)
                binding?.webView?.settings?.javaScriptCanOpenWindowsAutomatically = true
            } else {
                 requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }


        when (from) {
            "merchandise" -> {
                binding?.include3?.textView108?.text= title
                loadWebData(get)
            }
            "PVRcare" -> {
                binding?.include3?.textView108?.text= title
                loadWebData(get)
            }"passFaq" -> {

                binding?.include3?.textView108?.text= title
                loadWebData(get)
            }"kotakApply" -> {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Privilege")
                GoogleAnalytics.hitEvent(this, "kotak_saving_account", bundle)
            }catch (e:Exception) {
                e.printStackTrace()
            }
                binding?.include3?.textView108?.text= title
                loadWebData(get)
            }
            else -> {
                binding?.include3?.textView108?.text= title
                loadWebData(get)
            }
        }

    }

    private fun movedNext() {
        binding?.include3?.imageView58?.setOnClickListener {
            onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebData(url: String) {
        try {
            binding?.webView?.loadUrl(url)
            val webSettings = binding?.webView?.settings
            webSettings?.javaScriptEnabled = true
            webSettings?.domStorageEnabled = true
            binding?.webView?.clearFormData()
            binding?.webView?.addJavascriptInterface(JavaScriptInterface(this), "Android")
        } catch (exception: Exception) {
            println("exception--->${exception.message}")
            exception.printStackTrace()
        }
        binding?.webView?.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java", ReplaceWith("false"))
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loader = LoaderDialog(R.string.pleaseWait)
                loader?.show(supportFragmentManager, null)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                loader?.dismiss()
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                loader?.dismiss()

            }

            override fun onReceivedHttpError(
                view: WebView,
                request: WebResourceRequest,
                errorResponse: WebResourceResponse
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                loader?.dismiss()

            }

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                super.onReceivedSslError(view, handler, error)
                loader?.dismiss()

            }
        }
    }

    class JavaScriptInterface internal constructor(var mContext: Activity) {
        @JavascriptInterface
        fun paymentResponse(response: String, pageName: String) {

        }
    }




}