package com.net.pvr1.ui.home.fragment.more.contactUs

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityContactUsBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.more.contactUs.adapter.ContactUsItemAdapter
import com.net.pvr1.ui.home.fragment.more.contactUs.viewModel.ContactUsViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactUsActivity : AppCompatActivity(), ContactUsItemAdapter.RecycleViewItemClickListener {
    private var binding: ActivityContactUsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ContactUsViewModel by viewModels()
    private var listData: ArrayList<String> = ArrayList()
    private  var type: String = ""

    @Inject
    lateinit var preferences: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunction()
    }

    private fun manageFunction() {
        binding?.include22?.textView5?.text = getString(R.string.submit)
        binding?.toolbar?.textView108?.text=getString(R.string.contact_us)
        if (preferences.getIsLogin()){
            //mobile
            binding?.mobileNumber?.setText(preferences.geMobileNumber())
            //email
            binding?.email?.setText(preferences.getEmail())
        }


//List
        listData.add("Feedback")
        listData.add("Advertising/Corporate")
        listData.add("Bulk Booking")

        type=listData[0]

        //Set Data
        val gridLayout =
            GridLayoutManager(this@ContactUsActivity, 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView40?.layoutManager = LinearLayoutManager(this@ContactUsActivity)
        val adapter = ContactUsItemAdapter(listData, this, this)
        binding?.recyclerView40?.layoutManager = gridLayout
        binding?.recyclerView40?.adapter = adapter
        movedNext()
        contactUs()    }

    private fun movedNext() {
        //toolbar Back
        binding?.toolbar?.imageView58?.setOnClickListener {
            finish()
        }

        //Click Submit
        binding?.include22?.textView5?.setOnClickListener {
            val mobile = binding?.mobileNumber?.text.toString()
            val email = binding?.email?.text.toString()
            val notes = binding?.notes?.text.toString()
            if (mobile == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.enterMobile),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else if (email == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.enterEmail),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()

            } else if (notes == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.enterComment),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else {
                authViewModel.contactUs(
                    notes,
                    email,
                    mobile,
                    Constant().getDeviceId(this),
                    "no",
                    type
                )
            }
        }
    }

    override fun contactUsClick(comingSoonItem: String) {
        type = comingSoonItem
    }


    private fun contactUs() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.blank_space,
                            "Submitted Successfully",
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                               retrieveData()
                            },
                            negativeClick = {
                            })
                        dialog.show()
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

    private fun retrieveData() {
//       binding?.mobileNumber?.text?.clear()
//       binding?.email?.text?.clear()
       binding?.notes?.text?.clear()
    }

}