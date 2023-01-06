package com.net.pvr1.ui.location.enableLocation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityEnableLocationBinding
import com.net.pvr1.ui.location.selectCity.SelectCityActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EnableLocationActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityEnableLocationBinding? = null
    private var from: String = ""
    private var cid: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnableLocationBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        //Get Intent  Qr Case
        cid = intent.getStringExtra("cid").toString()
        from = intent.getStringExtra("from").toString()
        movedNext()
    }

    private fun movedNext() {
        binding?.include39?.textView5?.text=getString(R.string.enable_location)
        //not Now
        binding?.noThanksTextView?.setOnClickListener {
            val intent = Intent(this@EnableLocationActivity, SelectCityActivity::class.java)
            intent.putExtra("from", from)
            intent.putExtra("cid", cid)
            startActivity(intent)
            finish()
        }

        //Enable Location
        binding?.include39?.textView5?.setOnClickListener {
            if (Constant().locationServicesEnabled(this) && Constant.latitude!=0.0 && Constant.longitude!= 0.0) {
                preferences.saveLatitudeData(Constant.latitude.toString())
                preferences.saveLongitudeData(Constant.longitude.toString())
                val intent2 = Intent(this@EnableLocationActivity, SelectCityActivity::class.java)
                intent.putExtra("from", from)
                intent.putExtra("cid", cid)
                startActivity(intent2)
                finish()
            } else {
                Constant().enableLocation(this)
            }
        }

        //back press
        binding?.include32?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}