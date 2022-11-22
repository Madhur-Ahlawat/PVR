package com.net.pvr1.ui.contactUs

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityContactUsBinding
import com.net.pvr1.ui.contactUs.adapter.ContactUsItemAdapter
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.contactUs.viewModel.ContactUsViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUsActivity : AppCompatActivity(), ContactUsItemAdapter.RecycleViewItemClickListener {
    private var binding: ActivityContactUsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ContactUsViewModel by viewModels()
    var list: ArrayList<String> = ArrayList()
    var type: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include22?.textView5?.text = getString(R.string.submit)
        binding?.toolbar?.textView108?.text=getString(R.string.contact_us)
//List
        list.add("Feedback")
        list.add("Advertising/Corporate")
        list.add("Bulk Booking")

        type=list[0]

        //Set Data
        val gridLayout =
            GridLayoutManager(this@ContactUsActivity, 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView40?.layoutManager = LinearLayoutManager(this@ContactUsActivity)
        val adapter = ContactUsItemAdapter(list, this, this)
        binding?.recyclerView40?.layoutManager = gridLayout
        binding?.recyclerView40?.adapter = adapter
        movedNext()
        contactUs()
    }

    private fun movedNext() {
//toolbar Back
        binding?.toolbar?.imageView58?.setOnClickListener {
            onBackPressed()
        }

        //Click Submit
        binding?.include22?.textView5?.setOnClickListener {
            val mobile = binding?.mobile?.text.toString()
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
                            R.string.app_name,
                            it.data.msg,
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

}