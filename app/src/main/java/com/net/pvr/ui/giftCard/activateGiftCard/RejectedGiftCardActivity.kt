package com.net.pvr.ui.giftCard.activateGiftCard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr.R
import com.net.pvr.databinding.ActivityRejectedGiftcardBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class RejectedGiftCardActivity : AppCompatActivity(){
    private var binding: ActivityRejectedGiftcardBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ActivateGiftCardViewModel by viewModels()

    @Inject
    lateinit var preferences: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRejectedGiftcardBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.tvTitle?.text = "Expired Gift Card"
        binding?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        //Screen Width

        binding?.cim?.text = intent.getStringExtra("cim")
        if (intent.hasExtra("msg") && !intent.getStringExtra("msg").equals("", ignoreCase = true)) {
            binding?.llGiftMsg?.show()
            binding?.tvMessage?.text = intent.getStringExtra("msg")
        } else {
            binding?.llGiftMsg?.hide()
        }
        Glide.with(this)
            .load(R.drawable.gift_card_default)
            .into(binding?.imageLandingScreen!!)
        binding?.tvProceedDetail?.setOnClickListener(View.OnClickListener {
            showDialogLoyalty(
                context,
                intent.getStringExtra("pkGiftId")
            )
        })

        binding?.uploadImage?.setOnClickListener(View.OnClickListener {
            val createIntent =
                Intent(this@RejectedGiftCardActivity, CreateGiftCardActivity::class.java)
            createIntent.putExtra("custom", "true")
            createIntent.putExtra("from", "reupload")
            createIntent.putExtra("pkGiftId", intent.getStringExtra("pkGiftId"))
            startActivity(createIntent)
        })

    }

    private fun showDialogLoyalty(mContext: Context?, id: String?) {
        val dialog = BottomSheetDialog(mContext!!, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.generate_into_genric)
        dialog.setCancelable(true)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        val no = dialog.findViewById<View>(R.id.no) as TextView?
        val yes = dialog.findViewById<View>(R.id.yes) as TextView?
        yes!!.setOnClickListener {
            generateGenricCard(id)
            dialog.dismiss()
        }
        no!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


}