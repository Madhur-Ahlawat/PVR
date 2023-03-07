package com.net.pvr.ui.home.fragment.more.eVoucher.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ActivityEvoucherDetailsBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.home.fragment.more.eVoucher.model.HowItWorkModel
import com.net.pvr.ui.home.fragment.more.eVoucher.viewModel.EVoucherViewModel
import com.net.pvr.ui.home.fragment.more.model.ProfileModel
import com.net.pvr.utils.Constant
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class EVoucherDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityEvoucherDetailsBinding? = null

    private var loader: LoaderDialog? = null
    private val authViewModel: EVoucherViewModel by viewModels()

   private var voucherDetShow=0
   private var howWorkShow=0

    private val howItWorkModel: ArrayList<HowItWorkModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvoucherDetailsBinding.inflate(layoutInflater, null, false)
        setContentView(binding?.root)
        manageFunction()
    }

    @SuppressLint("SetTextI18n")
    private fun manageFunction() {
        //title
        binding?.textView408?.text= intent.getStringExtra("name").toString()

        //validDate
        binding?.textView409?.text= getString(R.string.valid_till)+" "+
                Constant().dateFormatter(intent.getStringExtra("endDate").toString())

        //Image
        Glide.with(this)
            .load(intent.getStringExtra("image"))
            .error(R.drawable.placeholder_horizental)
            .into(binding?.imageView133!!)

        //voucherDetails
        binding?.imageView179?.setOnClickListener {
            if (voucherDetShow==0){
                voucherDetShow=1
                binding?.imageView179?.setImageResource(R.drawable.arrow_down)
                binding?.textView413?.show()
                //  details Txt
                binding?.textView414?.hide()

            }else{
                binding?.imageView179?.setImageResource(R.drawable.arrow_up)
                binding?.textView413?.hide()
                //  details Txt
                binding?.textView414?.show()
                binding?.textView414?.text = Html.fromHtml( intent.getStringExtra("shortDesc").toString(),Html.FROM_HTML_MODE_LEGACY)
                voucherDetShow=0

            }
        }

        //How It Work
        binding?.imageView181?.setOnClickListener {
            if (howWorkShow==0){
                howWorkShow=1
                binding?.imageView181?.setImageResource(R.drawable.arrow_down)
            }else{
                howWorkShow=0
                binding?.imageView181?.setImageResource(R.drawable.arrow_up)

            }
        }

        movedNext()
        categoryData()
    }

    private fun categoryData() {

//        howItWorkModel.add(
//            HowItWorkModel("How to buy:",HowItWorkModel.Category
//        )

    }

    private fun movedNext() {
        binding?.include52?.textView108?.text="Purchase Voucher"

        binding?.include52?.imageView58?.setOnClickListener {
            finish()
        }

    }

}