package com.net.pvr.ui.webView

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityWebViewBinding
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewReadyToLeave : AppCompatActivity() {
    private var mGeoLocationCallback: GeolocationPermissions.Callback? = null
    private var mGeoLocationRequestOrigin: String? = null
    private val MY_PERMISSIONS_REQUEST_LOCATION: Int = 111
    private var binding: ActivityWebViewBinding? = null
    private var from: String = ""
    private var title: String = ""
    private var get: String = ""
    private var loaderText: Int = 0
    private var loader: LoaderDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        initUI()
        from = intent.getStringExtra("from").toString()
        title = intent.getStringExtra("title").toString()
        get = intent.getStringExtra("getUrl").toString()
        loaderText = R.string.ready_to_leave
        loadWebData(get)
    }

    private fun initUI() {
        binding?.apply {
            webView?.apply {
                settings?.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    setSupportMultipleWindows(false)
                    javaScriptCanOpenWindowsAutomatically = false
                    setSupportZoom(false);
                    builtInZoomControls = false
                    setDatabaseEnabled(true);
                }
            }
            include3?.imageView58?.setOnClickListener {
                onBackPressed()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebData(url: String) {
        try {
            binding?.webView?.loadUrl(url)
            binding?.webView?.addJavascriptInterface(JavaScriptInterface(this), "Android")
        } catch (exception: Exception) {
            println("exception--->${exception.message}")
            exception.printStackTrace()
        }
        binding?.webView?.webChromeClient = object : WebChromeClient() {

            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                mGeoLocationRequestOrigin = null
                mGeoLocationCallback = null
                // Do We need to ask for permission?
                if (ContextCompat.checkSelfPermission(
                        this@WebViewReadyToLeave,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@WebViewReadyToLeave,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {

                        AlertDialog.Builder(this@WebViewReadyToLeave)
                            .setMessage(R.string.permission_location_rationale)
                            .setNeutralButton(android.R.string.ok,
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        mGeoLocationRequestOrigin = origin
                                        mGeoLocationCallback = callback
                                        ActivityCompat.requestPermissions(
                                            this@WebViewReadyToLeave,
                                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                            MY_PERMISSIONS_REQUEST_LOCATION
                                        )
                                    }
                                })
                            .show()

                    } else {
                        // No explanation needed, we can request the permission.

                        mGeoLocationRequestOrigin = origin
                        mGeoLocationCallback = callback
                        ActivityCompat.requestPermissions(
                            this@WebViewReadyToLeave,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                } else {
                    // Tell the WebView that permission has been granted
                    callback.invoke(origin, true, false)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    mGeoLocationCallback?.invoke(mGeoLocationRequestOrigin, true, false)
                } else {

                    mGeoLocationCallback?.invoke(mGeoLocationRequestOrigin, false, false)
                }
            }
        }
    }

    class JavaScriptInterface internal constructor(var mContext: Activity) {
        @JavascriptInterface
        fun paymentResponse(response: String, pageName: String) {

        }
    }
}