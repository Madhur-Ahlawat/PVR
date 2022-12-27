package com.net.pvr1.ui.location.enableLocation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityEnableLocationBinding
import com.net.pvr1.ui.location.enableLocation.viewModel.EnableLocationViewModel
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
    private val authViewModel: EnableLocationViewModel by viewModels()
   private var from:String=""
   private var cid:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnableLocationBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        //Get Intent  Qr Case
        cid= intent.getStringExtra("cid").toString()
        from= intent.getStringExtra("from").toString()
        movedNext()
    }

    private fun movedNext() {
        //not Now
        binding?.noThanksTextView?.setOnClickListener {
            val intent = Intent(this@EnableLocationActivity, SelectCityActivity::class.java)
            intent.putExtra("from",from)
            intent.putExtra("cid",cid)
            startActivity(intent)
        }

        //Enable Location
        binding?.enableLocationButton?.setOnClickListener {
            if (Constant().isLocationEnabled(this)) {
                val intent2 = Intent(this@EnableLocationActivity, SelectCityActivity::class.java)
                intent.putExtra("from",from)
                intent.putExtra("cid",cid)
                startActivity(intent2)
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