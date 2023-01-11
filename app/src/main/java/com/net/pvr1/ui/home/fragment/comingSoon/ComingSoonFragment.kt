package com.net.pvr1.ui.home.fragment.comingSoon

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityComingSoonBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.filter.GenericFilterComing
import com.net.pvr1.ui.home.fragment.comingSoon.adapter.ComingSoonMovieAdapter
import com.net.pvr1.ui.home.fragment.comingSoon.adapter.LanguageAdapter
import com.net.pvr1.ui.home.fragment.comingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.home.fragment.comingSoon.search.CinemaSearchActivity
import com.net.pvr1.ui.home.fragment.comingSoon.viewModel.ComingSoonViewModel
import com.net.pvr1.ui.movieDetails.comingSoonDetails.ComingSoonDetailsActivity
import com.net.pvr1.ui.movieDetails.comingSoonDetails.adapter.ComDetailsHomePhAdapter
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ComingSoonFragment : Fragment(), LanguageAdapter.RecycleViewItemClickListener,
    ComingSoonMovieAdapter.VideoPlay, GenericFilterComing.onButtonSelected ,
    StoriesProgressView.StoriesListener{
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityComingSoonBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ComingSoonViewModel by viewModels()
    private var checkLogin: Boolean = false
    private var clickTime = 0
    private val appliedFilterType = ""
    private var appliedFilterItem = HashMap<String, String>()
    private var buttonPressed = ArrayList<String>()
    private var generseleced: ArrayList<String> = ArrayList<String>()
    private var language="ALL"
    private var genre="ALL"


    // story board
    private var bannerShow = 0
    private var pressTime = 0L
    private var limit = 500L
    private var counterStory = 0
    private var currentPage = 1
    private var qrCode = ""
    private var bannerModelsMain: ArrayList<CommingSoonResponse.Output.Pu> = ArrayList()
    private var ivBanner: ImageView? = null
    private var ivCross: ImageView? = null
    private var skip: View? = null
    private var reverse: View? = null
    private var tvButton: TextView? = null
    private var ivPlay: LinearLayout? = null
    private var RlBanner: RelativeLayout? = null
    private var stories: StoriesProgressView? = null


    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ActivityComingSoonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().findViewById(R.id.include) as ConstraintLayout).hide()

        tvButton = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.tv_button))
        ivBanner = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.ivBanner))
        ivPlay = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.ivPlay))
        skip = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout).findViewById(R.id.skip))
        reverse =
            (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout).findViewById(R.id.reverse))
        ivCross = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout).findViewById(R.id.ivCross))
        RlBanner = (requireActivity().findViewById(R.id.RlBanner))
        stories = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout).findViewById(R.id.stories))

        if (stories == null) {
            stories?.destroy()
        }

        //Functions
        comingSoonAPICall()

        comingSoonApi()
        movedNext()
        getShimmerData()
        //internet Check
        broadcastReceiver = NetworkReceiver()
        broadcastIntent()
    }
    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText,binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText,null)
    }

    private fun comingSoonAPICall() {
        if (buttonPressed.isEmpty()){
            language="ALL"
        }
        else {
            var tempLang = buttonPressed[0].split("-").toTypedArray()[0].trim { it <= ' ' }
            for (i in 1 until buttonPressed.size) tempLang =
                tempLang + "," + buttonPressed[i].split("-").toTypedArray()[0].trim { it <= ' ' }
            language=tempLang
        }

        if (generseleced.isEmpty()){
            genre="ALL"
        }
             else {
            var tempGenera = generseleced[0].split("-").toTypedArray()[0].trim { it <= ' ' }
            for (i in 1 until generseleced.size) tempGenera =
                tempGenera + "," + generseleced[i].split("-").toTypedArray()[0].trim { it <= ' ' }
            genre=tempGenera
        }
        authViewModel.comingSoon(preferences.getCityName(), genre, language, preferences.getUserId())

    }

    private fun movedNext() {
        binding?.imageView167?.setOnClickListener {
            val intent = Intent(requireActivity(), CinemaSearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun comingSoonApi() {
        authViewModel.userResponseLiveData.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output)
                    } else {
                        val dialog = OptionDialog(requireActivity(),
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
                    val dialog = OptionDialog(requireActivity(),
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
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: CommingSoonResponse.Output) {
        //shimmer
        binding?.constraintLayout148?.hide()
        //Ui
        binding?.constraintLayout147?.show()

        clickTime+=1
        //Promotion
        if (output.ph!=null) {
            if (clickTime==0) {
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(binding?.recyclerView)
                val gridLayoutSlider =
                    GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
                binding?.recyclerView?.layoutManager = gridLayoutSlider
                val adapter = ComDetailsHomePhAdapter(requireActivity(), output.ph)
                binding?.recyclerView?.adapter = adapter
            }
        }

        //ComingSoon}
        if (output.movies.isNotEmpty()){
            binding?.recComSoonMovie?.show()
            val gridLayout2 = GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
            val comingSoonMovieAdapter =
                ComingSoonMovieAdapter(output.movies, requireActivity(), this, checkLogin)
            binding?.recComSoonMovie?.layoutManager = gridLayout2
            binding?.recComSoonMovie?.adapter = comingSoonMovieAdapter
        }else{
            binding?.recComSoonMovie?.hide()
        }


        val onButtonSelected: GenericFilterComing.onButtonSelected = this
        // Filter
        binding?.filterFab?.setOnClickListener {
            val gFilter = GenericFilterComing()
            val filterPoints = HashMap<String, ArrayList<String>>()
            if (output != null) {
                filterPoints[Constant.FilterType.LANG_FILTER] = output.language
                filterPoints[Constant.FilterType.GENERE_FILTER] = output.genre
                filterPoints[Constant.FilterType.FORMAT_FILTER] = ArrayList()
                filterPoints[Constant.FilterType.ACCESSABILITY_FILTER] = ArrayList()
                filterPoints[Constant.FilterType.PRICE_FILTER] = ArrayList()
                filterPoints[Constant.FilterType.SHOWTIME_FILTER] = ArrayList()
                filterPoints[Constant.FilterType.CINEMA_FORMAT] = ArrayList()
                filterPoints[Constant.FilterType.SPECIAL_SHOW] = ArrayList()
                context?.let { it1 ->
                    gFilter.openFilters(
                        it1,
                        "ComingSoon",
                        onButtonSelected,
                        appliedFilterType,
                        appliedFilterItem,
                        filterPoints
                    )
                }
            }
        }
        if (appliedFilterItem.size > 0) binding?.appliedFilter?.visibility = View.VISIBLE

        if (output.pu.isNotEmpty()){
            initBanner(output.pu)
        }
    }


    override fun onDateClick(comingSoonItem: Any) {

    }

    override fun onDateClick(comingSoonItem: CommingSoonResponse.Output.Movy) {
        val intent = Intent(requireActivity(), ComingSoonDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.masterMovieId)
        startActivity(intent)
    }

    override fun onTrailerClick(comingSoonItem: CommingSoonResponse.Output.Movy) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.videoUrl)
        startActivity(intent)
    }

    override fun onApply(
        type: ArrayList<String>,
        filterItemSelected: HashMap<String, String>,
        isSelected: Boolean,
        name: String
    ) {
        if (type.size > 1) {
            binding?.filterFab?.setImageResource(R.drawable.filter_selected)
            appliedFilterItem = filterItemSelected
            binding?.appliedFilter?.visibility = View.VISIBLE
            val containLanguage = type.contains("language")
            if (containLanguage) {
                val index = type.indexOf("language")
                var value: String = filterItemSelected.get(type[index])!!
                if (!value.equals("", ignoreCase = true)) {
                    buttonPressed.clear()
                    value = value.uppercase(Locale.getDefault())
                    val valuesString = value.split(",").toTypedArray()
                    for (s in valuesString) {
                        if (!buttonPressed.contains(s)) buttonPressed.add(s.trim { it <= ' ' } + "-language")
                    }
                    println("buttonPressed--->$value")
                    binding?.filterFab?.setImageResource(R.drawable.filter_selected)
                } else {
                    binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
                    buttonPressed.clear()
                }
            }
            val containGeners = type.contains("geners")
            if (containGeners) {
                val index = type.indexOf("geners")
                var value: String = filterItemSelected[type[index]].toString()
                if (!value.equals("", ignoreCase = true)) {
                    generseleced.clear()
                    value = value.uppercase(Locale.getDefault())
                    val valuesString = value.split(",").toTypedArray()
                    for (s in valuesString) {
                        if (!generseleced.contains(s)) generseleced.add(s.trim { it <= ' ' } + "-geners")
                    }
                    binding?.filterFab?.setImageResource(R.drawable.filter_selected)
                } else {
                    binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
                    generseleced.clear()
                }
            }
            comingSoonAPICall()
        } else binding?.appliedFilter?.visibility = View.GONE
    }


    override fun onReset() {
        binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
        buttonPressed = ArrayList<String>()
        generseleced = ArrayList<String>()
        binding?.appliedFilter?.visibility = View.GONE
        appliedFilterItem = HashMap()
        authViewModel.comingSoon(preferences.getCityName(), "ALL", "ALL", preferences.getUserId())

    }

    private fun initBanner(bannerModels: ArrayList<CommingSoonResponse.Output.Pu>) {
        bannerShow += 1
        bannerModelsMain = bannerModels
        if ((bannerModels != null) && bannerModels.isNotEmpty()) {
            RlBanner?.show()
            stories?.setStoriesCount(bannerModels.size) // <- set stories
            stories?.setStoryDuration(5000L) // <- set a story duration
            stories?.setStoriesListener(this) // <- set listener
            stories?.startStories() // <- start progress
            counterStory = 0
            if (!TextUtils.isEmpty(bannerModels[counterStory].i)) {
                Picasso.get().load(bannerModels[counterStory].i).into(ivBanner!!, object :
                    Callback {
                    override fun onSuccess() {
                        RlBanner?.show()
                        //  storiesProgressView.startStories(); // <- start progress
                    }

                    override fun onError(e: Exception?) {}
                })
            }

            reverse?.setOnClickListener { stories?.reverse() }
            reverse?.setOnTouchListener(onTouchListener)
            showButton(bannerModels[0])
            skip?.setOnClickListener { stories?.skip() }
            skip?.setOnTouchListener(onTouchListener)
            tvButton?.setOnClickListener {
                RlBanner?.hide()
                if (bannerModels != null && bannerModels.size > 0 && bannerModels[counterStory].type
                        .equals("image",ignoreCase = true)
                ) {
                    if (bannerModels[counterStory].redirectView.equals("DEEPLINK",ignoreCase = true)) {
                        if (bannerModels[counterStory].redirect_url != null && !bannerModels[counterStory].redirect_url
                                .equals("",ignoreCase = true)
                        ) {
                            if (bannerModels[counterStory].redirect_url
                                    .toLowerCase(Locale.ROOT).contains("/loyalty/home")
                            ) {
//                                if (context is PCLandingActivity) (context as PCLandingActivity).PriviegeFragment(
//                                    "C"
//                                )
                            } else {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(
                                        bannerModels[counterStory].redirect_url
                                            .replace("https", "app").replace("http", "app")
                                    )
                                )
                                startActivity(intent)
                            }
                        }
                    } else if (bannerModels[counterStory].redirectView
                            .equals("INAPP",ignoreCase = true)
                    ) {
                        if (bannerModels[counterStory].redirectView != null && !bannerModels[counterStory].redirect_url
                                .equals("",ignoreCase = true)
                        ) {
//                            val intent = Intent(context, PrivacyActivity::class.java)
//                            intent.putExtra("url", bannerModels[counterStory].getRedirect_url())
//                            intent.putExtra(PCConstants.IS_FROM, 2000)
//                            intent.putExtra("title", bannerModels[counterStory].getName())
//                            startActivity(intent)
                        }
                    } else if (bannerModels[counterStory].redirectView
                            .equals("WEB",ignoreCase = true)
                    ) {
                        if (bannerModels[counterStory].redirect_url != null && !bannerModels[counterStory].redirect_url
                                .equals("",ignoreCase = true)
                        ) {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(bannerModels[counterStory].redirect_url)
                            )
                            startActivity(intent)
                        }
                    }
                }


            }
            (requireActivity().findViewById(R.id.bannerLayout) as RelativeLayout).show()

            ivPlay?.setOnClickListener {
                RlBanner?.hide()
                if (bannerModels != null && bannerModels.size > 0 && bannerModels[counterStory].type
                        .equals("video",ignoreCase = true)
                ) {
                    if (bannerModels[counterStory].trailerUrl != null) {
//                        val intent = Intent(context, MovieTrailerActivity::class.java)
//                        intent.putExtra("id", "")
//                        intent.putExtra("name", "")
//                        intent.putExtra("language", "")
//                        intent.putExtra(
//                            PCConstants.IntentKey.YOUTUBE_URL,
//                            bannerModels[counterStory].trailerUrl
//                        )
//                        startActivity(intent)
                    }
                }

            }

        } else {
            RlBanner?.hide()
        }
    }


    override fun onNext() {
        try {
            if (!TextUtils.isEmpty(bannerModelsMain[counterStory].i)) {
                ++counterStory
                showButton(bannerModelsMain[counterStory])
                ivBanner?.let {
                    Glide.with(this).load(bannerModelsMain[counterStory].i).into(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onPrev() {
        if (counterStory - 1 < 0) return
        if (!TextUtils.isEmpty(bannerModelsMain[counterStory].i)) {
            --counterStory
            showButton(bannerModelsMain[counterStory])
            ivBanner?.let {
                Glide.with(this).load(bannerModelsMain[counterStory].i).into(it)
            }
        }
    }

    override fun onComplete() {
        stories?.destroy()
        stories?.startStories()
        currentPage = 0
        RlBanner?.hide()
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                stories?.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                stories?.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }

    private fun showButton(bannerModel: CommingSoonResponse.Output.Pu) {
        if (bannerModel.type.equals("video",ignoreCase = true)) {
            ivPlay?.show()
            tvButton?.hide()
        } else if (bannerModel.type.equals("image",ignoreCase = true) && bannerModel.redirect_url.equals("",ignoreCase = true)
        ) {
            ivPlay?.hide()
            tvButton?.text = bannerModel.buttonText
            tvButton?.show()
        } else {
            ivPlay?.hide()
            tvButton?.hide()
        }

    }

    //Internet Check
    private fun broadcastIntent() {
        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

}