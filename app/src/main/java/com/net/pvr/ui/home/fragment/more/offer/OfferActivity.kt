package com.net.pvr.ui.home.fragment.more.offer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr.R
import com.net.pvr.databinding.ActivityOfferBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.more.offer.adapter.OffersAdapter
import com.net.pvr.ui.home.fragment.more.offer.response.MOfferResponse
import com.net.pvr.ui.home.fragment.more.offer.response.OfferLocalData
import com.net.pvr.ui.home.fragment.more.offer.viewModel.OfferViewModel
import com.net.pvr.utils.Constant
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OfferActivity : AppCompatActivity(), OffersAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityOfferBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: OfferViewModel by viewModels()
    private var catList: List<String> = ArrayList()
    private var offerList: ArrayList<OfferLocalData> = ArrayList()
    private var offerLoadBanner = false
    private var phList: ArrayList<MOfferResponse.Output.Pu> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        binding?.include4?.textView108?.text = getString(R.string.offers)
        authViewModel.offer(Constant().getDeviceId(this), preferences.getCityName())
        offerDataLoad()
        movedNext()
    }

    private fun movedNext() {
        binding?.include4?.imageView58?.setOnClickListener {
            finish()
        }
    }

    private fun offerDataLoad() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.SUCCESS_CODE == it.data?.code) {
                        val data: ArrayList<MOfferResponse.Output.Offer> = it.data.output.offers
                        if (it.data.output.pu.size > 0 && offerLoadBanner) {
                            offerLoadBanner = false
                        }
                        if (it.data.output.ph.size > 0) {
                            phList = it.data.output.pu
                        }
                        if (data.size > 0) {
                            catList = data[0].catp.split(",").toList()
                            offerList = getList(data)
                            updateAdapter(offerList)
                        }


                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
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
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun updateAdapter(offer: ArrayList<OfferLocalData>) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = layoutManager
        val adapter = OffersAdapter(this, offer, offer, this)
        binding?.recyclerView?.adapter = adapter
    }


    private fun getList(data: List<MOfferResponse.Output.Offer>): ArrayList<OfferLocalData> {
        val offerList: ArrayList<OfferLocalData> = ArrayList()
        for (i in catList.indices) {
            val list = ArrayList<MOfferResponse.Output.Offer>()
            for (datum in data) {
                if (datum.oc.equals(catList[i], ignoreCase = true)) {
                    list.add(datum)
                }
            }
            if (i == 0 && list.size > 0) {
                offerList.add(OfferLocalData(catList[i], list))
                offerList.add(OfferLocalData("", list))
            } else {
                if (i == 0) {
                    list.add(data[0])
                    offerList.add(OfferLocalData("", list))
                } else {
                    if (list.size > 0) {
                        offerList.add(OfferLocalData(catList[i], list))
                    }
                }
            }
        }
        return offerList
    }

    override fun onItemClick(offer: MOfferResponse.Output.Offer?) {


    }

}




