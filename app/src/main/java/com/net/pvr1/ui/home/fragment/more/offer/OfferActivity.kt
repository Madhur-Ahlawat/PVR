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
import com.net.pvr1.utils.printLog
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
    private var offerList: List<OfferList> = ArrayList()
    private var offer_loadBanner = false

    companion object{
        private   var phList: ArrayList<MOfferResponse.Output.Pu> = ArrayList()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include4?.textView108?.text = getString(R.string.offers)
        authViewModel.offer(Constant().getDeviceId(this), "Delhi-NCR")
        offerDataLoad()

    }

    private fun offerDataLoad() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.SUCCESS_CODE == it.data?.code) {


                        val data: ArrayList<MOfferResponse.Output.Offer> = it.data.output.offers

                        if (it.data.output.pu != null && it.data.output.pu.size > 0 && offer_loadBanner) {
                            offer_loadBanner = false
//                            updatePU(it.data.output.pu)
                        }
                        if (it.data.output.ph != null && it.data.output.ph.size > 0) {
                            phList = it.data.output.pu
                        }
                        if (data != null && data.size > 0) {
                            catList = listOf(listOf(data[0].catp.split("\\s*,\\s*")).toString())
                            offerList = getList(data)
                            printLog("categoryList--->$offerList---->${data[0].catp}")
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

//    private fun updatePU(phd: List<MOfferResponse.Output.Pu>) {
//        bannerModels = phd
//        println("phd---" + phd.size)
//        RlBanner = findViewById<View>(R.id.RlBanner) as RelativeLayout
//        RlBanner.setVisibility(View.VISIBLE)
//        val view_offer = findViewById<ViewPager>(R.id.view_offer)
//        val ivCross = findViewById<ImageView>(R.id.ivCross)
//        ivCross.setOnClickListener { v: View? -> RlBanner.setVisibility(View.GONE) }
//        storiesProgressView = findViewById<View>(R.id.stories) as StoriesProgressView
//        storiesProgressView.setStoriesCount(phd.size) // <- set stories
//        storiesProgressView.setStoryDuration(5000L) // <- set a story duration
//        storiesProgressView.setStoriesListener(this) // <- set listener
//        storiesProgressView.startStories() // <- start progress
//        Pvrlog.write("promotion---", phd.size.toString())
//        counterStory = 0
//        ivplay = findViewById<View>(R.id.iv_play) as LinearLayout
//        tv_button = findViewById<View>(R.id.tv_button) as TextView
//        ivBanner = findViewById<View>(R.id.ivBanner) as ImageView
//        if (!TextUtils.isEmpty(phd[counterStory].getI())) {
//            Picasso.get()
//                .load(phd[counterStory].getI())
//                .into(ivBanner, object : Callback {
//                    override fun onSuccess() {
//                        RlBanner.setVisibility(View.VISIBLE)
//                    }
//
//                    override fun onError(e: Exception) {}
//                })
//        }
//        // bind reverse view
//        val reverse = findViewById<View>(R.id.reverse)
//        reverse.setOnClickListener { storiesProgressView.reverse() }
//        reverse.setOnTouchListener(onTouchListener)
//
//        // bind skip view
//        val skip = findViewById<View>(R.id.skip)
//        skip.setOnClickListener { storiesProgressView.skip() }
//        skip.setOnTouchListener(onTouchListener)
//        showButton(phd[0])
//        tv_button.setOnClickListener(View.OnClickListener {
//            RlBanner.setVisibility(View.GONE)
//            if (phd != null && phd.size > 0 && phd[counterStory].getType()
//                    .equalsIgnoreCase("image")
//            ) {
//                if (phd[counterStory].getRedirectView().equalsIgnoreCase("DEEPLINK")) {
//                    if (phd[counterStory].getRedirect_url() != null && !phd[counterStory].getRedirect_url()
//                            .equalsIgnoreCase("")
//                    ) {
//                        val intent = Intent(
//                            Intent.ACTION_VIEW,
//                            Uri.parse(phd[counterStory].getRedirect_url())
//                        )
//                        startActivity(intent)
//                    }
//                } else if (phd[counterStory].getRedirectView().equalsIgnoreCase("INAPP")) {
//                    if (phd[counterStory].getRedirect_url() != null && !phd[counterStory].getRedirect_url()
//                            .equalsIgnoreCase("")
//                    ) {
//                        val intent = Intent(context, PrivacyActivity::class.java)
//                        intent.putExtra("url", phd[counterStory].getRedirect_url())
//                        intent.putExtra(PCConstants.IS_FROM, 2000)
//                        intent.putExtra("title", phd[counterStory].getName())
//                        startActivity(intent)
//                    }
//                } else if (phd[counterStory].getRedirectView().equalsIgnoreCase("WEB")) {
//                    if (phd[counterStory].getRedirect_url() != null && !phd[counterStory].getRedirect_url()
//                            .equalsIgnoreCase("")
//                    ) {
//                        val intent = Intent(
//                            Intent.ACTION_VIEW,
//                            Uri.parse(phd[counterStory].getRedirect_url())
//                        )
//                        startActivity(intent)
//                    }
//                }
//            }
//        })
//        ivplay.setOnClickListener(View.OnClickListener {
//            RlBanner.setVisibility(View.GONE)
//            if (phd != null && phd.size > 0 && phd[counterStory].getType()
//                    .equalsIgnoreCase("video")
//            ) {
//                if (phd[counterStory].getTrailerUrl() != null) {
//                    Log.e("TAG", "onClickviedeo: " + phd[counterStory].getTrailerUrl())
//                    val intent = Intent(context, MovieTrailerActivity::class.java)
//                    intent.putExtra("id", "")
//                    intent.putExtra("name", "")
//                    intent.putExtra("language", "")
//                    intent.putExtra(
//                        PCConstants.IntentKey.YOUTUBE_URL,
//                        phd[counterStory].getTrailerUrl()
//                    )
//                    startActivity(intent)
//                }
//            }
//        })
//    }

    private fun updateAdapter(offer: List<OfferList>) {
         printLog("categoryList--->--->$offer")

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager =layoutManager
        val adapter = OffersAdapter(this, offer, offer, this, "O")
        binding?.recyclerView?.adapter = adapter
    }

    private fun getList(data: List<MOfferResponse.Output.Offer>): List<OfferList> {
        val offerList: MutableList<OfferList> = ArrayList()
        printLog("printCategory--->2${offerList}")
        for (i in catList.indices) {
            val list = ArrayList<MOfferResponse.Output.Offer>()
            for (datum in data) {
                if (datum.oc.equals(catList[i], ignoreCase = true)) {
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

    override fun onItemClick(offer: MOfferResponse.Output.Offer?) {

    }

}




