package com.net.pvr1.ui.payment.starPass

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityStarPassBinding
import com.net.pvr1.ui.payment.promoCode.viewModel.PromoCodeViewModel
import com.net.pvr1.ui.payment.starPass.adapter.StarPassAdapter
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class StarPassActivity : AppCompatActivity(),StarPassAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityStarPassBinding? = null
    private var couponList = ArrayList<StarPasModel>()
    private var itemSize: Int = 3
    private val promoCodeViewModel: PromoCodeViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarPassBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include29?.imageView58?.setOnClickListener {
            onBackPressed()
        }
        binding?.include29?.textView108?.text = intent.getStringExtra("title")

        if (intent.extras?.getBoolean("ca_a") === false)
            binding?.textView371?.text = intent.extras?.getString("ca_t")

        if (intent?.hasExtra("tc") == true)
            binding?.textView373?.text = intent.extras?.getString("tc")

        //PaidAmount
        binding?.textView178?.text =
            getString(R.string.pay) + " " + getString(R.string.currency) + intent.getStringExtra("paidAmount")

//        for (a in 1..itemSize){
//            print("$a ")
//            couponList.add(StarPasModel("Coupon $a",""))
////            couponList.add("","")
//        }

//        val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
//        val foodBestSellerAdapter = StarPassAdapter(couponList, this, this)
//        binding?.recyclerView50?.layoutManager = layoutManagerCrew
//        binding?.recyclerView50?.adapter = foodBestSellerAdapter
//        binding?.recyclerView50?.setHasFixedSize(true)
    }

    override fun starPassAddClick(comingSoonItem: StarPasModel) {

    }

    override fun starPassMinusClick(comingSoonItem: StarPasModel) {

    }

}