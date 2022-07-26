package com.net.pvr1.ui.webView

import android.app.Activity
import android.net.http.SslError
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {
    private var binding: ActivityWebViewBinding? = null
    private var from: String = ""
    private var title: String = ""
    private var get: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        from = intent.getStringExtra("from").toString()
        title = intent.getStringExtra("title").toString()
        get = intent.getStringExtra("getUrl").toString()
        loadWebData(get)
    }

    private fun loadWebData(url: String) {
        try {
            binding?.webView?.loadUrl(url)
            val webSettings = binding?.webView?.settings
            webSettings?.javaScriptEnabled = true
            webSettings?.domStorageEnabled = true
            binding?.webView?.clearFormData()
            binding?.webView?.addJavascriptInterface(
                JavaScriptInterface(
                    this
                ), "Android"
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        binding?.webView?.webViewClient = object : WebViewClient() {

            @Deprecated("Deprecated in Java", ReplaceWith("false"))
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)


            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedHttpError(
                view: WebView,
                request: WebResourceRequest,
                errorResponse: WebResourceResponse
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                super.onReceivedSslError(view, handler, error)
            }
        }
    }

    class JavaScriptInterface internal constructor(var mContext: Activity) {
        @android.webkit.JavascriptInterface
        fun paymentResponse(response: String, pageName: String) {

        }
    }


}