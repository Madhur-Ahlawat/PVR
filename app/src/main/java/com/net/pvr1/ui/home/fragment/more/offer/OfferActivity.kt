package com.net.pvr1.ui.home.fragment.more.offer

import android.os.Bundle

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityOfferBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.more.offer.adapter.OffersAdapter
import com.net.pvr1.ui.home.fragment.more.offer.response.MOfferResponse
import com.net.pvr1.ui.home.fragment.more.offer.viewModel.OfferViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class OfferActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityOfferBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: OfferViewModel by viewModels()
    private var catList: List<String> = ArrayList()
    private var offerList: List<OfferList> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include4?.textView108?.text = getString(R.string.offers)
        authViewModel.offer(Constant().getDeviceId(this),"Delhi-NCR")
        offerDataLoad()

    }

    private fun offerDataLoad() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.SUCCESS_CODE == it.data?.code) {
                        catList =
                            listOf(mutableListOf(it.data.output.offers[0].catp.split("\\s*,\\s*")).toString())
                        offerList = getList(it.data.output.offers)

                        updateAdapter(offerList as ArrayList<OfferList>,it.data.output.ph)
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun updateAdapter(
        offerList: ArrayList<OfferList>,
        phList: ArrayList<MOfferResponse.Output.Ph>
    ) {
        println("offerListMain---->${offerList.size}")
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this@OfferActivity)
        val  adapter = OffersAdapter(this, offerList, offerList, "O",phList)
        binding?.recyclerView?.layoutManager=layoutManager
        binding?.recyclerView?.adapter = adapter
    }

    private fun getList(data: List<MOfferResponse.Output.Offer>): List<OfferList> {
        val offerList: MutableList<OfferList> = ArrayList()
        for (i in catList.indices) {
            val list = ArrayList<MOfferResponse.Output.Offer>()
            for (datum in data) {
                println("list1--->" + datum.oc + "----" + catList[i])
                if (datum.oc.equals(catList[i],ignoreCase = true)) {
                    list.add(datum)
                }
            }
            val list1 = OfferList()
            if (i == 0 && list.size > 0) {
                list1.setList(list)
                list1.setCat(catList[i])
                offerList.add(list1)
                val list2 = OfferList()
                list2.setList(list)
                list2.setCat("")
                offerList.add(list2)
            } else {
                if (i == 0) {
                    val list2 = OfferList()
                    list.add(data[0])
                    list2.setList(list)
                    list2.setCat("")
                    offerList.add(list2)
                } else {
                    if (list.size > 0) {
                        list1.setList(list)
                        list1.setCat(catList[i])
                        offerList.add(list1)
                    }
                }
            }
        }
        return offerList
    }

}




