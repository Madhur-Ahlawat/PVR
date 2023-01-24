package com.net.pvr1.ui.home.fragment.more.offer.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.databinding.ActivityOfferListBinding
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.ui.home.fragment.more.offer.list.adapter.OfferListAdapter
import com.net.pvr1.ui.home.fragment.more.offer.response.MOfferResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferListActivity : AppCompatActivity(),OfferListAdapter.RecycleViewItemClickListenerCity {
    private var binding: ActivityOfferListBinding? = null
//    private var offerList: ArrayList<MOfferResponse.Output.Offer> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferListBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunction()
    }

    private fun manageFunction() {
        movedNext()
        loadData()
    }

    private fun loadData() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView59?.layoutManager = layoutManager
        val adapter = OfferListAdapter(this,this,  intent.getSerializableExtra("list") as ArrayList<MOfferResponse.Output.Offer>)
        binding?.recyclerView59?.adapter = adapter
    }

    private fun movedNext() {
        //title
        binding?.include36?.textView108?.text = intent.getStringExtra("title").toString()
        //Back
        binding?.include36?.imageView58?.setOnClickListener {
            finish()
        }
    }

    override fun itemClick(comingSoonItem: FoodResponse.Output.Mfl) {

    }
}