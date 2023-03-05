package com.net.pvr.ui.home.fragment.more.offer.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.databinding.ActivityOfferListBinding
import com.net.pvr.ui.home.fragment.more.offer.list.adapter.OfferListAdapter
import com.net.pvr.ui.home.fragment.more.offer.offerDetails.OfferDetailsActivity
import com.net.pvr.ui.home.fragment.more.offer.response.MOfferResponse
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferListActivity : AppCompatActivity(),
    OfferListAdapter.RecycleViewItemClickListenerCity {
    private var binding: ActivityOfferListBinding? = null

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

    override fun itemClick(comingSoonItem: MOfferResponse.Output.Offer) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Offers")
//                bundle.putString("var_experiences_banner", comingSoonItem.name)
            GoogleAnalytics.hitEvent(this, "offers_more_offers", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(this, OfferDetailsActivity::class.java)
        intent.putExtra("title", comingSoonItem.c)
        intent.putExtra("disc", comingSoonItem.vt)
        intent.putExtra("id", comingSoonItem.id.toString())
        startActivity(intent)
    }

}