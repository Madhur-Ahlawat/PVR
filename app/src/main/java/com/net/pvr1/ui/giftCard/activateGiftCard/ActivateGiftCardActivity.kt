package com.net.pvr1.ui.giftCard.activateGiftCard

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.joooonho.SelectableRoundedImageView
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityActivateGiftCardBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.giftCard.activateGiftCard.adapter.ActivateGiftCardAdapter
import com.net.pvr1.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr1.ui.giftCard.response.ActiveGCResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ActivateGiftCardActivity : AppCompatActivity() ,ActivateGiftCardAdapter.RecycleViewItemClickListener{
    private var binding: ActivityActivateGiftCardBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ActivateGiftCardViewModel by viewModels()
    @Inject
    lateinit var preferences: PreferenceManager
    private var activeGiftList = ArrayList<ActiveGCResponse.Gca>()
    private var inActiveGiftList = ArrayList<ActiveGCResponse.Gca>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivateGiftCardBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include12?.titleCommonToolbar?.text = "Activate Gift Card"
        binding?.include12?.btnBack?.setOnClickListener {
            onBackPressed()
        }

        //Screen Width
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        authViewModel.activeGiftCard(preferences.getUserId())
        activateGiftCard()

        binding?.tvExpiredGift?.setOnClickListener {
            val intent = Intent(this, ExpiredGiftCardActivity::class.java)
            if (inActiveGiftList.size > 0) {
                intent.putExtra("expiredList", inActiveGiftList)
            }
            startActivity(intent)
        }

    }
    private fun activateGiftCard() {
        authViewModel.activeGCResponseLiveData.observe(this) {
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

    private fun retrieveData(output: ActiveGCResponse.Output) {
        inActiveGiftList = output.gci
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val giftCardMainAdapter2 = ActivateGiftCardAdapter(output.gca,  this, this)
        binding?.recyclerView30?.layoutManager = gridLayout2
        binding?.recyclerView30?.adapter = giftCardMainAdapter2

    }

    override fun activateGiftCard(comingSoonItem: ActiveGCResponse.Gca) {
        showDialog(comingSoonItem.gcn)
    }

    private fun showDialog(giftId: String) {
        val pinDialog = Dialog(this)
        pinDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pinDialog.setCancelable(true)
        pinDialog.setCanceledOnTouchOutside(true)
        pinDialog.setContentView(R.layout.gift_pin_dialog)
        pinDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pinDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pinDialog.window!!.setGravity(Gravity.BOTTOM)
        val et_card_pin = pinDialog.findViewById<EditText>(R.id.et_card_pin)
        val tv_proceed_detail = pinDialog.findViewById<TextView>(R.id.tv_proceed_detail)
        val iv_gift_image = pinDialog.findViewById<SelectableRoundedImageView>(R.id.iv_gift_image)
        tv_proceed_detail.setOnClickListener {
            if (et_card_pin.text.toString().isNotEmpty()) {
                authViewModel.redeemGC(preferences.getUserId(), giftId.replace("ID:", "").trim(),et_card_pin.text.toString())
                et_card_pin.setText("")
                pinDialog.dismiss()
            } else {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "Please enter Card Pin",
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }
        }
        pinDialog.window!!.setWindowAnimations(R.style.AppTheme_Slide)
        pinDialog.show()
    }


}