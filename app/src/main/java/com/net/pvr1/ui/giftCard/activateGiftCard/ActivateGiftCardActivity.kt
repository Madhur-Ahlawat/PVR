package com.net.pvr1.ui.giftCard.activateGiftCard

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
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

    }

}