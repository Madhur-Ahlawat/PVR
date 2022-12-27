package com.net.pvr1.ui.scanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.utils.URLEncodedUtils
import com.google.zxing.integration.android.IntentIntegrator
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityScannerBinding
import com.net.pvr1.ui.scanner.bookings.SelectBookingsActivity
import com.net.pvr1.utils.PreferenceManager
import com.net.pvr1.utils.printLog
import dagger.hilt.android.AndroidEntryPoint
import java.net.URI
import java.net.URISyntaxException
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ScannerActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityScannerBinding? = null
    private var fromScan: String? = ""
    private var qrScanIntegrator: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        qrScanIntegrator = IntentIntegrator(this)

        performAction()
        movedNext()
    }

    private fun movedNext() {
    //title
        binding?.includeHeader?.textView108?.text= getString(R.string.scan_qr_code)

    //back
        binding?.includeHeader?.imageView58?.setOnClickListener {
            finish()
        }

    }


    private fun performAction() {
        // Code to perform action when button is .
        qrScanIntegrator?.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // If QRCode has no data.
            if (result.contents == null) {
                finish()
            } else {
                handelResult(result.contents)
                binding?.name?.text = result.contents
            }
        }
    }

    private fun handelResult(contents: String) {

        try {
            val uri = Uri.parse(contents)
            val server = uri.authority
            val path = uri.path
            val parts = path!!.split("/").toTypedArray()
            println("Result-->" + path + "=====" + parts.size)
            if (path.contains("newpromo")) {
                getOfferCode()
            } else if (path.contains("food") || path.contains("booking")) {
//                handler.postDelayed({
                    // close your dialog
                    if (parts.size == 5) {
//                            successIMG.setVisibility(View.VISIBLE)
//                            fromScan = "scan"
//                            val intent = Intent(this@PCCouponScan, GrabABiteActivity::class.java)
//                            val paymentIntentData = PaymentIntentData()
//                            intent.putExtra("from", "scan")
//                            paymentIntentData.setCinemaID(parts[2])
//                            paymentIntentData.setPaymentType(PCConstants.PaymentType.INTHEATRE)
//                            paymentIntentData.setName("")
//                            paymentIntentData.setFnb(PCConstants.FNB)
//                            paymentIntentData.setSessionActive(true)
//                            intent.putExtra(
//                                PCConstants.IntentKey.TICKET_BOOKING_DETAILS,
//                                paymentIntentData
//                            )
//                            intent.putExtra("SEAT", parts[4])
//                            intent.putExtra("AUDI", parts[3])
//                            startActivity(intent)
                    } else if (parts.size > 2) {
                        fromScan = "scan"
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
//                            successIMG.setVisibility(View.VISIBLE)
                            val intent =Intent(this, SelectBookingsActivity::class.java)
                              intent.putExtra("from", "pscan")
//                            val paymentIntentData = PaymentIntentData()
//                            paymentIntentData.setCinemaID(parts[2])
//                            paymentIntentData.setPaymentType(PCConstants.PaymentType.INTHEATRE)
//                            paymentIntentData.setName("")
//                            paymentIntentData.setFnb(PCConstants.FNB)
//                            paymentIntentData.setSessionActive(true)
//                            intent.putExtra(
//                                PCConstants.IntentKey.TICKET_BOOKING_DETAILS, paymentIntentData
//                            )
                            intent.putExtra("SEAT", "")
                            intent.putExtra("cid",parts[2])
                            intent.putExtra("AUDI", "")
                        if (!TextUtils.isEmpty(type))
                            intent.putExtra("type", type)
                        if (path.contains("newpromo"))
                            intent.putExtra("promo", "NEWPROMO")

                        startActivity(intent)
                        finish()

                    }
//                }, 300)
            } else if (path.contains("getqrcode")) {
//                handler.postDelayed({ // close your dialog
                    if (parts.size == 5) {
//                            successIMG.setVisibility(View.VISIBLE)
//                            fromScan = "scan"
//                            val intent = Intent(this@PCCouponScan, GrabABiteActivity::class.java)
//                            val paymentIntentData = PaymentIntentData()
//                            intent.putExtra("from", "scan")
//                            paymentIntentData.setCinemaID(parts[2])
//                            paymentIntentData.setPaymentType(PCConstants.PaymentType.INTHEATRE)
//                            paymentIntentData.setName("")
//                            paymentIntentData.setFnb(PCConstants.FNB)
//                            paymentIntentData.setSessionActive(true)
//                            intent.putExtra(
//                                PCConstants.IntentKey.TICKET_BOOKING_DETAILS, paymentIntentData
//                            )
//                            intent.putExtra("SEAT", parts[4])
//                            intent.putExtra("AUDI", parts[3])
//                            startActivity(intent)
                    } else if (parts.size > 2) {
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
//                             successIMG.setVisibility( View.VISIBLE )

                        if (option.equals("Food", ignoreCase = true)) {
//                                val intent =
//                                    Intent(this@PCCouponScan, GrabABiteActivity::class.java)
//                                val paymentIntentData = PaymentIntentData()
//                                intent.putExtra("from", "pscan")
//                                paymentIntentData.setCinemaID(parts[2])
//                                paymentIntentData.setPaymentType(PCConstants.PaymentType.INTHEATRE)
//                                paymentIntentData.setName("")
//                                paymentIntentData.setFnb(PCConstants.FNB)
//                                paymentIntentData.setSessionActive(true)
//                                intent.putExtra(
//                                    PCConstants.IntentKey.TICKET_BOOKING_DETAILS, paymentIntentData
//                                )
//                                intent.putExtra("SEAT", seat)
//                                intent.putExtra("AUDI", "")
//                                if (!TextUtils.isEmpty(type)) intent.putExtra("type", type)
//                                if (!TextUtils.isEmpty(iserv) && iserv.equals(
//                                        "yes", ignoreCase = true
//                                    )
//                                ) intent.putExtra("iserv", iserv)
//
//                                startActivity(
//                                    intent
//                                )
                        } else {
//                                val intent =
//                                    Intent(this@PCCouponScan, SelectBookingActivity::class.java)
//                                val paymentIntentData = PaymentIntentData()
//                                intent.putExtra("from", "pscan")
//                                paymentIntentData.setCinemaID(parts[2])
//                                paymentIntentData.setPaymentType(PCConstants.PaymentType.INTHEATRE)
//                                paymentIntentData.setName("")
//                                paymentIntentData.setFnb(PCConstants.FNB)
//                                paymentIntentData.setSessionActive(true)
//                                intent.putExtra(
//                                    PCConstants.IntentKey.TICKET_BOOKING_DETAILS, paymentIntentData
//                                )
//                                intent.putExtra("SEAT", seat)
//                                intent.putExtra("AUDI", "")
//                                if (!TextUtils.isEmpty(type)) intent.putExtra("type", type)
//                                if (path.contains("newpromo")) intent.putExtra(
//                                    "promo", "NEWPROMO"
//                                )
//                                if (!TextUtils.isEmpty(iserv) && iserv.equals(
//                                        "yes", ignoreCase = true
//                                    )
//                                ) intent.putExtra("iserv", iserv)
//                                startActivity(
//                                    intent
//                                )
                        }

                    }
//                }, 300)
//                } else {
//                    println("successIMG============else")
//                    successIMG.setVisibility(View.GONE)
//                    textScan.setText(
//                        """
//                    Invalid QR code!
//                    Try a new one or try again
//                    """.trimIndent()
//                    )
//                    mScannerView.resumeCameraPreview(this@PCCouponScan)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    private fun getOfferCode() {

    }

}