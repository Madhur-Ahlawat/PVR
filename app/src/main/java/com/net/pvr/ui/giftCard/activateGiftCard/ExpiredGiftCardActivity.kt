package com.net.pvr.ui.giftCard.activateGiftCard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr.R
import com.net.pvr.databinding.ActivityActivateGiftCardBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.giftCard.activateGiftCard.adapter.ActivateGiftCardAdapter
import com.net.pvr.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr.ui.giftCard.response.ActiveGCResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ExpiredGiftCardActivity : AppCompatActivity(),
    ActivateGiftCardAdapter.RecycleViewItemClickListener {
    private var binding: ActivityActivateGiftCardBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ActivateGiftCardViewModel by viewModels()

    @Inject
    lateinit var preferences: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivateGiftCardBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include12?.titleCommonToolbar?.text = "Expired Gift Card"
        binding?.include12?.btnBack?.setOnClickListener {
            onBackPressed()
        }
        //Screen Width

        var inActiveGiftList = ArrayList<ActiveGCResponse.Gca>()

        if (intent != null) {
            if (intent.getSerializableExtra("expiredList") != null) {
                inActiveGiftList =
                    intent.getSerializableExtra("expiredList") as ArrayList<ActiveGCResponse.Gca>
                if (inActiveGiftList.size > 0) {
                    retrieveData(inActiveGiftList)
                }
            } else {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "No Data Found",
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                        onBackPressed()
                    },
                    negativeClick = {
                    })
                dialog.show()
            }
        } else {

            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                "No Data Found",
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                    onBackPressed()
                },
                negativeClick = {
                })
            dialog.show()
        }

    }

    private fun retrieveData(output: ArrayList<ActiveGCResponse.Gca>) {
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val giftCardMainAdapter2 = ActivateGiftCardAdapter(output, this, this)
        binding?.recyclerView30?.layoutManager = gridLayout2
        binding?.recyclerView30?.adapter = giftCardMainAdapter2

    }

    override fun activateGiftCard(comingSoonItem: ActiveGCResponse.Gca) {

    }

}