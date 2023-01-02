package com.net.pvr1.ui.giftCard

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityGiftCardBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.giftCard.activateGiftCard.ActivateGiftCardActivity
import com.net.pvr1.ui.giftCard.adapter.GiftCardMainAdapter
import com.net.pvr1.ui.giftCard.response.GiftCardResponse
import com.net.pvr1.ui.giftCard.viewModel.GiftCardViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class GiftCardActivity : AppCompatActivity() ,GiftCardMainAdapter.RecycleViewItemClickListener{
    private var binding: ActivityGiftCardBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: GiftCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGiftCardBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include6?.titleCommonToolbar?.text = "Gift Card"

        //Screen Width
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        authViewModel.offer(width.toString(), "")
        giftCard()
        movedNext()
    }

    private fun movedNext() {
        binding?.include6?.btnBack?.setOnClickListener {
            finish()
        }
    }


    private fun giftCard() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
//                        retrieveData(it.data.output)
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

    private fun retrieveData(output: GiftCardResponse.Output) {

        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val giftCardMainAdapter = GiftCardMainAdapter(output.giftCards,  this, this)
        binding?.recyclerView29?.layoutManager = gridLayout
        binding?.recyclerView29?.adapter = giftCardMainAdapter

        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val giftCardMainAdapter2 = GiftCardMainAdapter(output.giftCards,  this, this)
        binding?.recyclerView3?.layoutManager = gridLayout2
        binding?.recyclerView3?.adapter = giftCardMainAdapter2

    }

    override fun giftCardClick(comingSoonItem: GiftCardResponse.Output.GiftCard) {
        startActivity(Intent(this,ActivateGiftCardActivity::class.java))
    }

}