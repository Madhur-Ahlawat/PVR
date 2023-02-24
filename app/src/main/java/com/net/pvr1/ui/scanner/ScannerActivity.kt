package com.net.pvr1.ui.scanner

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.utils.URLEncodedUtils
import com.journeyapps.barcodescanner.*
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityScannerBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.food.FoodActivity
import com.net.pvr1.ui.scanner.bookings.SelectBookingsActivity
import com.net.pvr1.utils.printLog
import dagger.hilt.android.AndroidEntryPoint
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class ScannerActivity : AppCompatActivity(), DecoratedBarcodeView.TorchListener {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityScannerBinding? = null
    private var fromScan: String? = ""
    private var capture: CaptureManager? = null
    private var barcodeScannerView: DecoratedBarcodeView? = null
    private  var titleScanner:TextView?=null
    private var viewfinderView: ViewfinderView? = null
    private var torchClick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner)
        barcodeScannerView?.setTorchListener(this)

        viewfinderView = findViewById(R.id.zxing_viewfinder_view)

        val rnd = Random()
        val color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        titleScanner = findViewById(R.id.zxing_status_view)

        if (!hasFlash()) {
            binding?.switchFlashlight?.visibility = View.GONE
        }

        capture = CaptureManager(this, barcodeScannerView)
        capture?.initializeFromIntent(intent, savedInstanceState)
        capture?.setShowMissingCameraPermissionDialog(true)

        manageFunction()
    }

    private fun manageFunction() {
        barcodeScannerView?.decodeContinuous {
            handelResult(it.text)
        }

        changeMaskColor(null)
        changeLaserVisibility(true)
        performAction()
        movedNext()

    }


    private fun movedNext() {
        //title
        binding?.includeHeader?.textView108?.text = getString(R.string.scan_qr_code)

        //back
        binding?.includeHeader?.imageView58?.setOnClickListener {
            finish()
        }

        //Flash ON/OFF
        binding?.switchFlashlight?.setOnClickListener {
            if (torchClick == 0) {
                torchClick = 1
                binding?.switchFlashlight?.setImageResource(R.drawable.flash_off)
                barcodeScannerView?.setTorchOn()
            } else {
                torchClick = 0
                binding?.switchFlashlight?.setImageResource(R.drawable.flash_on)
                barcodeScannerView?.setTorchOff()
            }
        }

    }


    private fun performAction() {
        val options = ScanOptions()
        //barcodeLauncher.launch(options)
    }


    private fun handelResult(contents: String) {
        try {
            titleScanner?.text=getString(R.string.scanQr)

            val uri = Uri.parse(contents)
            val server = uri.authority
            val path = uri.path
            val parts = path?.split("/")?.toTypedArray()
            if (path?.contains("newpromo") == true) {
                getOfferCode()
            }
            else if (path?.contains("food") == true || path?.contains("booking") == true) {
                var type = ""
                if (contents.contains("type")) {
                    var params: List<NameValuePair?>? = null
                    try {
                        params = URLEncodedUtils.parse(URI(contents), "UTF-8")
                        for (param in params) {
                            printLog(param.name + " : " + param.value)

                            if (param.name != "type") type = param.value
                        }
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }
                }
                val intent = Intent(this, SelectBookingsActivity::class.java)
                intent.putExtra("from", "pscan")
                intent.putExtra("SEAT", "")
                intent.putExtra("cid", parts?.get(2))
                intent.putExtra("AUDI", "")
                if (!TextUtils.isEmpty(type))
                    intent.putExtra("type", type)
                if (path.contains("newpromo"))
                    intent.putExtra("promo", "NEWPROMO")

                startActivity(intent)
                finish()
            }
            else if (path?.contains("getqrcode") == true) {
                if (parts?.size == 5) {

                } else if (parts?.size!! > 2) {
                    fromScan = "scan"
                    var type = ""
                    var option = ""
                    var iserv = ""
                    var seat = ""
                    if (contents.contains("type")) {
                        var params: List<NameValuePair?>? = null
                        try {
                            params = URLEncodedUtils.parse(URI(contents), "UTF-8")
                            for (param2 in (params as MutableList<NameValuePair>?)!!) {
                                println(param2.name + " : " + param2.value)
                                if (param2.name.equals("type", ignoreCase = true)) type =
                                    param2.value else if (param2.name.equals(
                                        "option",
                                        ignoreCase = true
                                    )
                                ) option = param2.value else if (param2.name.equals(
                                        "iserv",
                                        ignoreCase = true
                                    )
                                ) iserv = param2.value else if (param2.name.equals(
                                        "seat",
                                        ignoreCase = true
                                    )
                                ) seat = param2.value
                            }
                        } catch (e: URISyntaxException) {
                            e.printStackTrace()
                        }
                    }

                }
            } else{
                titleScanner?.text=getString(R.string.invalidQr)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        capture?.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture?.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeScannerView?.onKeyDown(keyCode, event) == true || super.onKeyDown(
            keyCode,
            event
        )
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private fun hasFlash(): Boolean {
        return applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }


    private fun changeMaskColor(view: View?) {
        val rnd = Random()
        val color: Int = Color.argb(100, rnd.nextInt(255), rnd.nextInt(203), rnd.nextInt(7))

        viewfinderView?.setMaskColor(color)
    }

    private fun changeLaserVisibility(visible: Boolean) {
        viewfinderView?.setLaserVisibility(visible)
    }

    private fun getOfferCode() {

    }

    override fun onTorchOn() {

        binding?.switchFlashlight?.setImageResource(R.drawable.flash_off)

//        binding?.switchFlashlight?.text = "Turn on Flashlight"
    }

    override fun onTorchOff() {
        binding?.switchFlashlight?.setImageResource(R.drawable.flash_on)

//        binding?.switchFlashlight?.text = "Turn off Flashlight"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        capture?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}