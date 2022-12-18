package com.net.pvr1.ui.home.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentHomeBinding
import com.net.pvr1.ui.bookingSession.BookingActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.filter.GenericFilterHome
import com.net.pvr1.ui.formats.FormatsActivity
import com.net.pvr1.ui.home.fragment.home.adapter.*
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr1.ui.movieDetails.nowShowing.NowShowingActivity
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.ui.search.searchHome.SearchHomeActivity
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.PlaceHolder
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeCinemaCategoryAdapter.RecycleViewItemClickListener,
    HomeSliderAdapter.RecycleViewItemClickListener,
    HomePromotionAdapter.RecycleViewItemClickListener,
    HomeMoviesAdapter.RecycleViewItemClickListener, HomeTrailerAdapter.RecycleViewItemClickListener,
    GenericFilterHome.onButtonSelected, StoriesProgressView.StoriesListener {
    private var binding: FragmentHomeBinding? = null
    @Inject
    lateinit var preferences: PreferenceManager
    private var loader: LoaderDialog? = null
    private val authViewModel by activityViewModels<HomeViewModel>()
    private var onButtonSelected: GenericFilterHome.onButtonSelected = this
    private var lang = "ALL"
    private var format = "ALL"
    private var special = "ALL"
    private var cinema_type = "ALL"
    private var upcomingBooking = true
    private var buttonPressed = ArrayList<String>()
    private var generseleced: ArrayList<String?>? = ArrayList<String?>()

// story board
    private var pressTime = 0L
    private var limit = 500L
    private var counterStory = 0
    private var currentPage = 1
    private var appliedFilterType = ""
    private var appliedFilterItem = HashMap<String, String>()
    private var bannerModels: ArrayList<HomeResponse.Pu> = ArrayList<HomeResponse.Pu>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (binding?.bannerLayout?.includeStoryLayout?.stories== null) {
            binding?.bannerLayout?.includeStoryLayout?.stories?.destroy()
        }

        (requireActivity().findViewById(R.id.include) as ConstraintLayout).show()
        (requireActivity().findViewById(R.id.notify) as ImageView).show()
        (requireActivity().findViewById(R.id.locationBtn) as ImageView).show()
        (requireActivity().findViewById(R.id.scanQr) as ImageView).show()
        (requireActivity().findViewById(R.id.searchBtn) as ImageView).show()
        (requireActivity().findViewById(R.id.searchCinema) as ImageView).hide()
        (requireActivity().findViewById(R.id.searchBtn) as ImageView).setOnClickListener {
            if (isAdded) {
                val intent = Intent(requireActivity(), SearchHomeActivity::class.java)
                startActivity(intent)
            }
        }

        movedNext()
        homeApi()
        getMovieFormatFromApi(true)
    }

    private fun movedNext() {
        binding?.txtTrailers?.setOnClickListener {
            binding?.txtTrailers?.setTextColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.black
                )
            )

            binding?.txtMusic?.setTextColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.textColorGray
                )
            )

            binding?.view33?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.yellow
                )
            )

            binding?.view34?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.gray
                )
            )
        }

        binding?.txtMusic?.setOnClickListener {
            binding?.txtMusic?.setTextColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.black
                )
            )
            binding?.txtTrailers?.setTextColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.textColorGray
                )
            )
            binding?.view33?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.gray
                )
            )
            binding?.view34?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.yellow
                )
            )

        }

        //banner
        binding?.bannerLayout?.ivCross?.setOnClickListener {
            binding?.RlBanner?.hide()
//                Constant.PvrHB = ""
        }
        binding?.RlBanner?.hide()

    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                binding?.bannerLayout?.includeStoryLayout?.stories?.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                binding?.bannerLayout?.includeStoryLayout?.stories?.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }

    private fun homeApi() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        loader?.dismiss()
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
                    val dialog = OptionDialog(requireContext(),
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
                    println("loadingHome--->")
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: HomeResponse.Output) {
        PlaceHolder = output
        if (isAdded) {
            //Category
            val gridLayout =
                GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
            binding?.recyclerCinemaCat?.layoutManager = LinearLayoutManager(context)
            val adapter = HomeCinemaCategoryAdapter(requireActivity(), output.mfi, this)
            binding?.recyclerCinemaCat?.layoutManager = gridLayout
            binding?.recyclerCinemaCat?.adapter = adapter

        }

        //Slider
        if (isAdded) {
            binding?.recyclerViewSlider?.onFlingListener = null
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding?.recyclerViewSlider)
            val gridLayoutSlider =
                GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
            binding?.recyclerViewSlider?.layoutManager = LinearLayoutManager(context)
            val adapterSlider = HomeSliderAdapter(requireActivity(), output.mv, this)
            binding?.recyclerViewSlider?.layoutManager = gridLayoutSlider
            binding?.recyclerViewSlider?.adapter = adapterSlider

        }

        //Promotion
        val gridLayoutSlider =
            GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerPromotion?.layoutManager = gridLayoutSlider
        binding?.recyclerPromotion?.adapter = PromotionAdapter(requireActivity(), output.ph)

        //Movies
        val gridLayoutMovies = GridLayoutManager(context, 2)
        binding?.recyclerMovies?.layoutManager = LinearLayoutManager(context)
        val adapterMovies = HomeMoviesAdapter(requireActivity(), output.mv, this)
        binding?.recyclerMovies?.layoutManager = gridLayoutMovies
        binding?.recyclerMovies?.adapter = adapterMovies

        //Trailer
        val gridLayoutTrailer = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerTrailer?.layoutManager = LinearLayoutManager(context)
        val adapterTrailer = HomeTrailerAdapter(requireActivity(), output.cp, this)
        binding?.recyclerTrailer?.layoutManager = gridLayoutTrailer
        binding?.recyclerTrailer?.adapter = adapterTrailer

        binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
        binding?.filterFab?.setOnClickListener {
            val gFilter = GenericFilterHome()
            val filterPoints = HashMap<String, ArrayList<String>>()
            filterPoints[Constant.FilterType.LANG_FILTER] = output.mlng
            filterPoints[Constant.FilterType.GENERE_FILTER] = output.mgener
            filterPoints[Constant.FilterType.FORMAT_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.ACCESSABILITY_FILTER] =
                ArrayList(listOf("Subtitle"))
            filterPoints[Constant.FilterType.PRICE_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.SHOWTIME_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.CINEMA_FORMAT] = ArrayList()
            filterPoints[Constant.FilterType.SPECIAL_SHOW] = ArrayList()
            gFilter.openFilters(
                context,
                "Home",
                onButtonSelected,
                appliedFilterType,
                appliedFilterItem,
                filterPoints
            )
        }


        if (output.rm!=null){

        }
        initBanner(output.pu)
    }

    override fun onCategoryClick(comingSoonItem: HomeResponse.Mfi) {
        val intent = Intent(requireActivity(), FormatsActivity::class.java)
        intent.putExtra("format", comingSoonItem.name)
        startActivity(intent)
    }

    override fun onSliderBookClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), NowShowingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun onPromotionClick(comingSoonItem: HomeResponse.Mfi) {
        val intent = Intent(requireActivity(), NowShowingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.name)
        startActivity(intent)
    }

    override fun onMoviesClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), NowShowingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun onBookClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), BookingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)

    }

    override fun onTrailerClick(comingSoonItem: HomeResponse.Cp) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.mtrailerurl)
        startActivity(intent)
    }

    override fun onApply(
        type: ArrayList<String>?,
        filterItemSelected: HashMap<String, String>?,
        isSelected: Boolean,
        name: String
    ) {
        println("filterItemSelected--->$type")
        if (type!!.size > 1) {
            binding?.filterFab?.setImageResource(R.drawable.filter_selected)
            appliedFilterItem = filterItemSelected!!
            binding?.appliedFilter?.visibility = View.VISIBLE
            val containLanguage = type.contains("language")
            if (containLanguage) {
                val index = type.indexOf("language")
                var value: String = filterItemSelected.get(type[index])!!
                if (value != null && !value.equals("", ignoreCase = true)) {
                    buttonPressed.clear()
                    appliedFilterType = "language"
                    value = value.uppercase(Locale.getDefault())
                    val valuesString = value.split(",").toTypedArray()
                    for (s in valuesString) {
                        if (!buttonPressed.contains(s)) buttonPressed.add("$s-language")
                    }
                    binding?.filterFab?.setImageResource(R.drawable.filter_selected)
                } else {
                    binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
                    buttonPressed.clear()
                }
            }
            val containGenres = type.contains("geners")
            if (containGenres) {
                val index = type.indexOf("geners")
                var value: String = filterItemSelected[type[index]]!!
                if (value != null && !value.equals("", ignoreCase = true)) {
                    appliedFilterType = "geners"
                    generseleced?.clear()
                    value = value.uppercase(Locale.getDefault())
                    val valuesString = value.split(",").toTypedArray()
                    for (s in valuesString) {
                        if (!generseleced?.contains(s)!!) generseleced?.add("$s-geners")
                    }
                    binding?.filterFab?.setImageResource(R.drawable.filter_selected)
                } else {
                    binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
                    generseleced?.clear()
                }
            }
            val containAccessability = type.contains("accessability")
            if (containAccessability) {
                val index = type.indexOf("accessability")
                val value: String = filterItemSelected[type[index]]!!
//                if (value != null && !value.equals("", ignoreCase = true)) {
//                    if (value.equals("Subtitle", ignoreCase = true)) special = output.getMspecial().get(0)
//                } else {
//                    special = "ALL"
//                }
                Log.d("ShowSelection", "onAply filter accessability: $special")
            }
            getMovieFormatFromApi(true)
        } else {
            binding?.appliedFilter?.visibility = View.GONE
            binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
        }
    }

    override fun onReset() {
        binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
        buttonPressed = ArrayList()
        generseleced = ArrayList()
        appliedFilterItem = HashMap()
        binding?.appliedFilter?.visibility = View.GONE
        getMovieFormatFromApi(true)
    }

    private fun getMovieFormatFromApi(isFirstTime: Boolean) {
        getMoviesForUNowShowingHit(isFirstTime)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getMoviesForUNowShowingHit(firstTime: Boolean) {
//        if (!TextUtils.isEmpty( PCApplication.getPreference() .getString(PCConstants.SharedPreference.BOOKING_FIRST_INDEX)) && preferences.getIsLogin()==true
//        )
        upcomingBooking = preferences.getIsLogin() == true

        if (!special.equals("All", ignoreCase = true))

            if (buttonPressed.isNotEmpty()) {
                lang = buttonPressed[0].split("-").toTypedArray()[0].trim { it <= ' ' }
                for (i in 1 until buttonPressed.size) lang =
                    lang + "," + buttonPressed[i].split("-").toTypedArray()[0].trim { it <= ' ' }
            }
        if (generseleced!!.isNotEmpty()) {
            format = generseleced!![0]!!.split("-").toTypedArray()[0].trim { it <= ' ' }
            for (i in 1 until generseleced!!.size) format =
                format + "," + generseleced!![i]!!.split("-").toTypedArray()[0].trim { it <= ' ' }
        }

        if (!cinema_type.equals("All", ignoreCase = true)){

            authViewModel.home(
                preferences.getCityName(),
                "",
                preferences.getUserId(),
                preferences.geMobileNumber(),
                upcomingBooking,
                "no",
                cinema_type,
                lang,
                format,
                special,
                "no"
            )
        }else{

            authViewModel.home(
                preferences.getCityName(),
                "",
                preferences.getUserId(),
                preferences.geMobileNumber(),
                upcomingBooking,
                "no",
                "",
                lang,
                format,
                special,
                "no"
            )
        }

    }


    @SuppressLint("CutPasteId")
    private fun initBanner(bannerModels: ArrayList<HomeResponse.Pu>) {
        println("InitBannerResponse--->" + bannerModels.size)

        if ((bannerModels != null) && bannerModels.isNotEmpty()) {
            (requireActivity().findViewById(R.id.RlBanner) as RelativeLayout).show()
            binding?.bannerLayout?.includeStoryLayout?.stories?.setStoriesCount(bannerModels.size) // <- set stories
            binding?.bannerLayout?.includeStoryLayout?.stories?.setStoryDuration(5000L) // <- set a story duration
            binding?.bannerLayout?.includeStoryLayout?.stories?.setStoriesListener(this) // <- set listener
            binding?.bannerLayout?.includeStoryLayout?.stories?.startStories() // <- start progress
            counterStory = 0
            if (!TextUtils.isEmpty(bannerModels[counterStory].i)) {
                Picasso.get().load(bannerModels[counterStory].i).into(binding?.bannerLayout?.includeStoryLayout?.ivBanner, object : Callback {
                        override fun onSuccess() {
                            (requireActivity().findViewById(R.id.RlBanner) as RelativeLayout).show()

                            //  storiesProgressView.startStories(); // <- start progress
                        }
                        override fun onError(e: Exception?) {}
                    })
            }



            binding?.bannerLayout?.includeStoryLayout?.reverse?.setOnClickListener { binding?.bannerLayout?.includeStoryLayout?.stories ?.reverse() }
            binding?.bannerLayout?.includeStoryLayout?.reverse?.setOnTouchListener(onTouchListener)
            showButton(bannerModels[0])
            binding?.bannerLayout?.includeStoryLayout?.skip?.setOnClickListener { binding?.bannerLayout?.includeStoryLayout?.stories ?.skip() }
            binding?.bannerLayout?.includeStoryLayout?.skip?.setOnTouchListener(onTouchListener)
            binding?.bannerLayout?.tvButton?.setOnClickListener {
                (requireActivity().findViewById(R.id.RlBanner) as RelativeLayout).hide()
                if (bannerModels != null && bannerModels.size > 0 && bannerModels[counterStory].type != "image"
//                        .equalsIgnoreCase("image")
                ) {
                    if (bannerModels[counterStory].redirectView != "DEEPLINK"
//                        equalsIgnoreCase("DEEPLINK")
                    ) {
                        if (bannerModels[counterStory].redirect_url != null && bannerModels[counterStory].redirect_url != "") {
                            if (bannerModels[counterStory].redirect_url.lowercase(Locale.ROOT)
                                    .contains("/loyalty/home")
                            ) {
//                                if (context is PCLandingActivity) (context as PCLandingActivity).PriviegeFragment(
//                                    "C"
//                                )
                            } else {
//                                .replaceAll("https", "app").replaceAll("http", "app")
                                val intent = Intent(
                                    Intent.ACTION_VIEW, Uri.parse(
                                        bannerModels[counterStory].redirect_url.replace(
                                                "https",
                                                "app"
                                            )
                                    )
                                )
                                startActivity(intent)
                            }
                        }
                    } else if (bannerModels[counterStory].redirect_url != "INAPP"
//                            .equalsIgnoreCase("INAPP")
                    ) {
                        if (bannerModels[counterStory].redirect_url != null && bannerModels[counterStory].redirect_url != ""
//                                .equalsIgnoreCase("")
                        ) {
//                            val intent = Intent(context, PrivacyActivity::class.java)
//                            intent.putExtra("url", bannerModels[counterStory].getRedirect_url())
//                            intent.putExtra(PCConstants.IS_FROM, 2000)
//                            intent.putExtra("title", bannerModels[counterStory].getName())
//                            startActivity(intent)
                        }
                    } else if (bannerModels[counterStory].redirect_url != "WEB"
//                            .equalsIgnoreCase("WEB")
                    ) {
                        if (bannerModels[counterStory].redirect_url != null && bannerModels[counterStory].redirect_url != ""
//                                .equalsIgnoreCase("")
                        ) {
//                            val intent = Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse(bannerModels[counterStory].redirect_url)
//                                        activity.startActivity(intent)
                        }
                    }
                }
            }
            (requireActivity().findViewById(R.id.bannerLayout) as RelativeLayout).show()

            binding?.bannerLayout?.ivPlay?.setOnClickListener {
                (requireActivity().findViewById(R.id.RlBanner) as RelativeLayout).hide()
                if (bannerModels != null && bannerModels.size > 0 && bannerModels[counterStory].type != "video"
//                        .equalsIgnoreCase("video")

                ) {
                }
            }

        } else {
            (requireActivity().findViewById(R.id.RlBanner) as RelativeLayout).hide()
        }
    }

    private fun showButton(bannerModel: HomeResponse.Pu) {
//        equalsIgnoreCase("video")
        if (bannerModel.type != "video") {
            binding?.bannerLayout?.ivPlay?.visibility = View.VISIBLE
            binding?.bannerLayout?.tvButton?.visibility = View.GONE
        } else if (bannerModel.type != "image" && bannerModel.redirect_url != ""
//                .equalsIgnoreCase("")
        ) {
            binding?.bannerLayout?.ivPlay?.visibility = View.GONE
            binding?.bannerLayout?.tvButton?.text = bannerModel.buttonText
            binding?.bannerLayout?.tvButton?.visibility = View.VISIBLE
        } else {
            binding?.bannerLayout?.ivPlay?.visibility = View.GONE
            binding?.bannerLayout?.tvButton?.visibility = View.GONE
        }

    }

    override fun onNext() {
        if (!TextUtils.isEmpty(bannerModels[counterStory].i)) {
            ++counterStory
            showButton(bannerModels[counterStory])
            binding?.bannerLayout?.includeStoryLayout?.ivBanner?.let {
                Glide.with(this)
                    .load(bannerModels[counterStory].i)
                    .into(it)
            }

        }
    }

    override fun onPrev() {
        if (counterStory - 1 < 0) return
        if (!TextUtils.isEmpty(bannerModels[counterStory].i)) {
            --counterStory
            showButton(bannerModels[counterStory])
            binding?.bannerLayout?.includeStoryLayout?.ivBanner?.let {
                Glide.with(this)
                    .load(bannerModels[counterStory].i)
                    .into(it)
            }

        }
    }

    override fun onComplete() {
        binding?.bannerLayout?.includeStoryLayout?.stories ?.destroy()
        binding?.bannerLayout?.includeStoryLayout?.stories ?.startStories()
        currentPage = 0
        (requireActivity().findViewById(R.id.RlBanner) as RelativeLayout).hide()
    }
}