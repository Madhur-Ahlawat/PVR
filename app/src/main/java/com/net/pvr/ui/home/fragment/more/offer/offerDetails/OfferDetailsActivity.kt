package com.net.pvr.ui.home.fragment.more.offer.offerDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ActivityOfferDetialsBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.fragment.more.offer.offerDetails.response.OfferDetailsResponse
import com.net.pvr.ui.home.fragment.more.offer.offerDetails.viewModel.OfferDetailsViewModel
import com.net.pvr.utils.Constant
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OfferDetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityOfferDetialsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: OfferDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferDetialsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        authViewModel.offerDetails(
            intent.getStringExtra("id").toString(),
            Constant().getDeviceId(this)
        )
        movedNext()
        offerDetailsDataLoad()
        Constant().appBarHide(this)
    }

    private fun movedNext() {
        //Back Press
        binding?.imageView92?.setOnClickListener {
            finish()
        }

        //Share
        binding?.imageView93?.setOnClickListener {
            Constant().shareData(this, "", "")
        }
    }

    private fun offerDetailsDataLoad() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

                        retrieveData(it.data.output)
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun retrieveData(output: OfferDetailsResponse.Output) {
        //title
        binding?.textView186?.text= intent.getStringExtra("title")

        // desc
        binding?.textView187?.text= "Valid till "+intent.getStringExtra("disc")

        //Image
        Glide.with(this)
            .load(output.i)
            .into(binding?.imageView91!!)

        //Description
        binding?.textView188?.loadDataWithBaseURL(null, output.d, "text/html", "utf-8", null);

        if (output.btn!=null&&output.btn!=""){
            binding?.include8?.textView5?.text= getString(R.string.book_now)

        }else{
            binding?.include8?.textView5?.text= output.btn
        }


        binding?.include8?.textView5?.setOnClickListener {
            if (output.btn.contains("BOOK")) {
                val intent: Intent
                if (!TextUtils.isEmpty(preferences.getPS()) &&preferences.getPS()
                        .equals("true",ignoreCase = true) && !TextUtils.isEmpty(
                        output.t) && output.t.equals("Private Screening at PVR",ignoreCase = true)
                ) {
//                intent = Intent(this@PcOfferDetailActivity, Private_Screening::class.java)
//                startActivity(intent)
                } else {
                    intent = Intent(this@OfferDetailsActivity, HomeActivity::class.java)
//                intent.putExtra(
//                    PCConstants.PCBackStackActivity.OPEN_ACTIVITY_NAME,
//                    PCConstants.PCBackStackActivity.LANDING_ACTIVITY
//                )
                    startActivity(intent)
                }
            } else {
                if (output.rurl != null && output.rurl != "") {
                    val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(output.rurl))
                    startActivity(myIntent)
                } else {
                    val intent = Intent(this@OfferDetailsActivity, HomeActivity::class.java)
//                intent.putExtra(
//                    PCConstants.PCBackStackActivity.OPEN_ACTIVITY_NAME,
//                    PCConstants.PCBackStackActivity.LANDING_ACTIVITY
//                )
                    startActivity(intent)
                }
            }
        }


    }

}