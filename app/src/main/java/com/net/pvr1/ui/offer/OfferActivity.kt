package com.net.pvr1.ui.offer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityOfferBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.offer.adapter.OffersAdapter
import com.net.pvr1.ui.offer.response.LocalOfferList
import com.net.pvr1.ui.offer.response.MOfferResponse
import com.net.pvr1.ui.offer.viewModel.OfferViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OfferActivity : AppCompatActivity(){
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityOfferBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: OfferViewModel by viewModels()

    var phList: ArrayList<MOfferResponse.Output.Ph> = ArrayList()

    private var catList: List<String> = ArrayList()
    private var offerList: List<LocalOfferList> = ArrayList()

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
                        updateAdapter(offerList as ArrayList<LocalOfferList>,it.data.output.ph)
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
        offerList: ArrayList<LocalOfferList>,
        phList: ArrayList<MOfferResponse.Output.Ph>
    ) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

//        binding?.recyclerView?.addItemDecoration(
//            DividerItemDecoration(this, R.drawable.divider_8)
//        )
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this@OfferActivity)
//      val  adapter = OffersAdapter(this, offerList, offerList, this, "O")
      val  adapter = OffersAdapter(this, offerList, offerList, "O",phList)
        binding?.recyclerView?.layoutManager=layoutManager
        binding?.recyclerView?.adapter = adapter
    }

    private fun getList(offers: ArrayList<MOfferResponse.Output.Offer>): ArrayList<LocalOfferList> {
        val offerList: ArrayList<LocalOfferList> = ArrayList()
        for (i in catList.indices) {
            val list: ArrayList<MOfferResponse.Output.Offer> = ArrayList()
            for (datum in offers) {
                println("list1--->" + datum.oc + "----" + catList[i])
                if (datum.oc == catList[i]) {
                    list.add(datum)
                }
            }
            val list1 = null
            if (i == 0 && list.size > 0) {
                offerList.add(LocalOfferList(catList[i],list))
                offerList.add(LocalOfferList("",list))
            } else {
                if (i == 0) {
                    val list2 =null
                    offerList.add(LocalOfferList(catList[0],list))
                    offerList.add(LocalOfferList("",list))
//                    list.add(data[0])
//                    list2.setList(list)
//                    list2.setCat("")
//                    offerList.add(list2)
                } else {
                    if (list.size > 0) {
                        offerList.add(LocalOfferList("",list))
                        offerList.add(LocalOfferList(catList[i],list))
//                        list1.setList(list)
//                        list1.setCat(catList[i])
//                        offerList.add(list1)
                    }
                }
            }
        }
        return offerList
    }

}