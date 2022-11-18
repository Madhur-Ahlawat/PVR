package com.net.pvr1.ui.offer.offerDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityOfferBinding
import com.net.pvr1.databinding.ActivityOfferDetialsBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.offer.OfferActivity
import com.net.pvr1.ui.offer.offerDetails.viewModel.OfferDetailsViewModel
import com.net.pvr1.ui.offer.viewModel.OfferViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferDetailsActivity : AppCompatActivity() {
//    private lateinit var preferences: AppPreferences
    private var binding: ActivityOfferDetialsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: OfferDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferDetialsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        movedNext()
        authViewModel.offerDetails(intent.getStringExtra("id").toString(),Constant().getDeviceId(this))
        offerDetailsDataLoad()
    }

    private fun movedNext() {
        //Back Press
        binding?.imageView92?.setOnClickListener {
            finish()
        }

        //Share
        binding?.imageView93?.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            startActivity(Intent.createChooser(shareIntent,getString(R.string.app_name)))
        }
    }

    private fun offerDetailsDataLoad() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
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
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

}