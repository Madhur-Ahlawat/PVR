package com.net.pvr.ui.home.fragment.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.net.pvr.MainApplication.Companion.homeLoadBanner
import com.net.pvr.R
import com.net.pvr.databinding.FragmentHomeBinding
import com.net.pvr.databinding.TrailersDialogBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.bookingSession.MovieSessionActivity
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.filter.GenericFilterHome
import com.net.pvr.ui.giftCard.GiftCardActivity
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.HomeActivity.Companion.backToTop
import com.net.pvr.ui.home.formats.FormatsActivity
import com.net.pvr.ui.home.fragment.home.adapter.*
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.home.fragment.home.response.NextBookingResponse
import com.net.pvr.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr.ui.home.fragment.more.offer.offerDetails.OfferDetailsActivity
import com.net.pvr.ui.home.fragment.more.offer.response.OfferResponse
import com.net.pvr.ui.home.fragment.more.profile.userDetails.ProfileActivity
import com.net.pvr.ui.home.interfaces.PlayPopup
import com.net.pvr.ui.location.selectCity.SelectCityActivity
import com.net.pvr.ui.movieDetails.nowShowing.NowShowingMovieDetailsActivity
import com.net.pvr.ui.movieDetails.nowShowing.adapter.MusicVideoTrsAdapter
import com.net.pvr.ui.movieDetails.nowShowing.adapter.TrailerTrsAdapter
import com.net.pvr.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr.ui.myBookings.MyBookingsActivity
import com.net.pvr.ui.player.PlayerActivity
import com.net.pvr.ui.scanner.ScannerActivity
import com.net.pvr.ui.search.searchHome.SearchHomeActivity
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.PRIVILEGEPOINT
import com.net.pvr.utils.Constant.Companion.PRIVILEGEVOUCHER
import com.net.pvr.utils.Constant.Companion.PlaceHolder
import com.net.pvr.utils.Constant.SharedPreference.Companion.ACTIVE
import com.net.pvr.utils.Constant.SharedPreference.Companion.SUBS_OPEN
import com.net.pvr.utils.ga.GoogleAnalytics
import com.net.pvr.utils.isevent.ISEvents
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class HomeFragment : Fragment(),
    HomeCinemaCategoryAdapter.RecycleViewItemClickListener,
    HomeSliderAdapter.RecycleViewItemClickListener,
    HomePromotionAdapter.RecycleViewItemClickListener,
    HomeMoviesAdapter.RecycleViewItemClickListener,
    HomeOfferListAdapter.RecycleViewItemClickListener,
    GenericFilterHome.onButtonSelected,
    StoriesProgressView.StoriesListener,
    MusicVideoTrsAdapter.RecycleViewItemClickListener,
    TrailerTrsAdapter.RecycleViewItemClickListener,
    HomeOfferAdapter.RecycleViewItemClickListenerCity {

    private var binding: FragmentHomeBinding? = null


    @Inject
    lateinit var preferences: PreferenceManager
    private var loader: LoaderDialog? = null
    private val authViewModel by activityViewModels<HomeViewModel>()

    private var onButtonSelected: GenericFilterHome.onButtonSelected = this
    private var lang = "ALL"
    private var format = "ALL"
    private var special = "ALL"
    private var cinemaType = "ALL"
    private var upcomingBooking = false
    private var buttonPressed = ArrayList<String>()
    private var generaSelected: ArrayList<String?>? = ArrayList<String?>()

    //offer
    private var offerResponse: ArrayList<OfferResponse.Offer>? = null
    private var offerShow = 0


    // story board
    private var bannerShow = 0
    private var pressTime = 0L
    private var limit = 500L
    private var counterStory = 0
    private var currentPage = 1
    private var qrCode = ""
    private var appliedFilterType = ""
    private var appliedFilterItem = HashMap<String, String>()
    private var bannerModelsMain: ArrayList<HomeResponse.Pu> = ArrayList<HomeResponse.Pu>()
    private var ivBanner: ImageView? = null
    private var ivCross: ImageView? = null
    private var skip: View? = null
    private var reverse: View? = null
    private var tvButton: TextView? = null
    private var ivPlay: LinearLayout? = null
    private var rlBanner: RelativeLayout? = null
    private var stories: StoriesProgressView? = null
    private var listener: PlayPopup? = null

    var gFilter: GenericFilterHome? = null

    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null


    private var musicData: ArrayList<MovieDetailsResponse.Trs> = ArrayList()

    private var videoData: ArrayList<MovieDetailsResponse.Trs> = ArrayList()

    companion object {
        var dialogTrailer: Dialog? = null
        private var movieData:ArrayList<HomeResponse.Mv> = ArrayList()

        var mcId = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonPressed = ArrayList()
        generaSelected = ArrayList()
        gFilter = GenericFilterHome()
        appliedFilterType = ""
        appliedFilterItem = HashMap<String, String>()

        lang = "ALL"
        format = "ALL"
        special = "ALL"
        cinemaType = "ALL"

        backToTop = requireActivity().findViewById(R.id.backToTop)
        //Poster
        listener = activity as PlayPopup?
        tvButton = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.tv_button))
        ivBanner = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.ivBanner))
        ivPlay = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.ivPlay))
        skip = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.skip))
        reverse = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.reverse))
        ivCross = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.ivCross))
        rlBanner = (requireActivity().findViewById(R.id.RlBanner))
        stories = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.stories))

        if (stories == null) {
            stories?.destroy()
        }


        // functions

        manageFunction()
    }

    private fun manageFunction() {
//         manage login
        if (preferences.getIsLogin()) {
            binding?.includeAppBar?.profileBtn?.show()
            binding?.constraintLayout135?.show()

            //offer
            authViewModel.offer(preferences.getCityName(),preferences.getUserId(),Constant().getDeviceId(requireActivity()),"NO")

//            nextBooking
            authViewModel.nextBooking(
                preferences.getUserId(), Constant().getDeviceId(requireActivity())
            )

        } else {
            binding?.includeAppBar?.profileBtn?.hide()
            binding?.constraintLayout135?.hide()
        }


        //internet Check
        broadcastReceiver = NetworkReceiver()
        broadcastIntent()

        binding?.privilege?.setOnClickListener {
            val navigationView =
                requireActivity().findViewById(R.id.bottomNavigationView) as BottomNavigationView
            val bottomMenuView = navigationView.getChildAt(0) as BottomNavigationMenuView
            val newView = bottomMenuView.getChildAt(2)
            val itemView = newView as BottomNavigationItemView
            itemView.performClick()
        }


        getShimmerData()
        movedNext()
        homeApi()
        nextBooking()
        createQr()
        getMovieFormatFromApi()
        makeToTop()
        offerDataLoad()
    }

    private fun makeToTop() {
        binding?.nestedScrollView4?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (scrollY > 1000) {
                backToTop?.show()
            } else {
                backToTop?.hide()

            }
            backToTop?.setOnClickListener {
                binding?.nestedScrollView4?.postDelayed(
                    { binding?.nestedScrollView4?.fullScroll(View.FOCUS_UP) }, 0
                )
            }
        })
    }


    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    @SuppressLint("SetTextI18n")
    private fun movedNext() {
        //      Close Offer Alert
        binding?.constraintLayout55?.setOnClickListener {

        }
        binding?.imageView78?.setOnClickListener {

            try {

                binding?.constraintLayout55?.hide()

                authViewModel.hideOffer(
                    preferences.getCityName(),
                    preferences.getUserId(),
                    "",
                    "no"
                )

               // hideDataLoad()
            }catch (e:java.lang.Exception){

            }

        }

//        Dialogs
        binding?.textView185?.setOnClickListener {
            showOfferDialog()
        }

        // manage top bar ui
        binding?.includeAppBar?.searchBtn?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home Screen")
                bundle.putString("var_header_search", "")
                GoogleAnalytics.hitEvent(requireActivity(), "header_search_bar_click", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val intent = Intent(requireActivity(), SearchHomeActivity::class.java)
            startActivity(intent)
        }

        // Select City
        binding?.includeAppBar?.txtCity?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Homepage")
                GoogleAnalytics.hitEvent(requireActivity(), "home_location", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val intent = Intent(requireActivity(), SelectCityActivity::class.java)
            intent.putExtra("from", "Homepage")
            startActivity(intent)
        }
        // Qr COde
        binding?.includeAppBar?.scanQr?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home Screen")
//                bundle.putString("var_header_search", "")
                GoogleAnalytics.hitEvent(requireActivity(), "hearder_qr_code", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val intent = Intent(requireActivity(), ScannerActivity::class.java)
            startActivity(intent)
        }
        // Profile
        binding?.includeAppBar?.profileBtn?.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            intent.putExtra("from", "home")
            startActivity(intent)
        }

        //setUserName
        if (preferences.getIsLogin()) {
            binding?.includeAppBar?.profileBtn?.show()
            binding?.includeAppBar?.textView2?.text = "Hey, " + preferences.getUserName()
        } else {
            binding?.includeAppBar?.profileBtn?.hide()
            binding?.includeAppBar?.textView2?.text = "Hey!"
        }

        binding?.includeAppBar?.txtCity?.text = preferences.getCityName()

        //banner
        ivCross?.setOnClickListener {
            rlBanner?.hide()
            listener?.onShowNotification()
            listener?.onShowPrivilege()
        }

        //pull Down
        binding?.swipeHome?.setOnRefreshListener {
            binding?.swipeHome?.isRefreshing = false
            getMovieFormatFromApi()
        }
    }


    //Offer Api
    private fun offerDataLoad() {
        authViewModel.offerLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        try {
                            if (it.data.output != null && it.data.output.offer.size != 0) {
                                binding?.constraintLayout55?.show()
                                retrieveOffer(it.data.output.offer)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                is NetworkResult.Error -> {

                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }
    private fun hideDataLoad() {
        authViewModel.offerHideLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    //if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        try {
                            binding?.constraintLayout55?.hide()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                   // }
                }
                is NetworkResult.Error -> {
                    binding?.constraintLayout55?.hide()
                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }

    private fun retrieveOffer(output: ArrayList<OfferResponse.Offer>) {
        printLog("offerResponse--->${output}")
//        Set Data
        offerResponse = output

        //Manage Show Hide
        offerShow = if (output.isNotEmpty()) {
            1
        } else {
            0
        }

        if (offerShow == 1) {
            binding?.constraintLayout55?.show()
        } else {
            binding?.constraintLayout55?.hide()
        }

    }


    //  offers Dialog
    private fun showOfferDialog() {
        val dialog = BottomSheetDialog(requireActivity(), R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.offer_dialog)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView26)
        val ignore = dialog.findViewById<TextView>(R.id.textView194)
        val textView5 = dialog.findViewById<TextView>(R.id.textView5)
        val indicators = dialog.findViewById<LinearLayout>(R.id.indicators)
        val textView192 = dialog.findViewById<TextView>(R.id.textView192)
        textView5?.text = getString(R.string.explore_offers)
        val gridLayout = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView?.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = offerResponse?.let {
            HomeOfferAdapter(
                it, requireActivity(), this
            )
        }
        recyclerView?.layoutManager = gridLayout
        recyclerView?.adapter = adapter
        textView192?.text = offerResponse?.get(0)?.offerName

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position = HomeActivity.getCurrentItem(recyclerView)
                    textView192?.text = offerResponse?.get(position)?.offerName
                    textView5?.setOnClickListener {
                        printLog("----------------------->${offerResponse?.get(position)?.otherLinkRedirectUrl?.replace(
                            "https", "app"
                        )}")
                        val intent = Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                offerResponse?.get(position)?.otherLinkRedirectUrl?.replace(
                                    "https", "app"
                                )
                            )
                        )
                        startActivity(intent)
                        dialog.dismiss()
                    }
                }
            }
        })
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        ignore?.setOnClickListener {
            dialog.dismiss()
            try {
                binding?.constraintLayout55?.hide()
                authViewModel.hideOffer(
                    preferences.getCityName(),
                    preferences.getUserId(),
                    "",
                    "no"
                )
               // hideDataLoad()
            }catch (e:java.lang.Exception){

            }
        }
        if (offerResponse?.size!! >1){
            indicators?.show()
            recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val pos =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()
                    indicators?.let { btnAction(pos, offerResponse?.size!!, it) }
                }
            })
            indicators?.let { btnAction(0, offerResponse?.size!!, it) }
        }else{
            indicators?.hide()
        }

    }

    private fun btnAction(position: Int, bannerListSize: Int, indicators: LinearLayout) {
        indicators.removeAllViews()
        println("position--->$position")
        for (i in 0 until bannerListSize) {
            val imageView = View(context)
            indicators.addView(imageView)
            val layoutParams = LinearLayout.LayoutParams(30, 5)
            layoutParams.leftMargin = 5
            layoutParams.rightMargin = 5
            imageView.layoutParams = layoutParams
            if (i == position) {
                imageView.setBackgroundResource(R.drawable.rectangle_select)
            } else {
                imageView.setBackgroundResource(R.drawable.rectangle_disable)
            }
        }
    }

    private fun createQr() {
        qrCode = Constant().getLoyaltyQr(preferences.geMobileNumber(), "180x180")
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = OnTouchListener { _, event ->
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    @SuppressLint("SuspiciousIndentation", "ClickableViewAccessibility")
    private fun retrieveData(output: HomeResponse.Output) {
        movieData = ArrayList()
        movieData.addAll(output.mv)
        //layout
        binding?.nestedScrollView4?.show()
        //filter
        binding?.filterFab?.show()
        //shimmer
        binding?.constraintLayout145?.hide()

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
//        if (isAdded) {
//            binding?.recyclerViewSlider?.onFlingListener = null
//            val snapHelper = PagerSnapHelper()
//            snapHelper.attachToRecyclerView(binding?.recyclerViewSlider)
//            val gridLayoutSlider =
//                GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
//            binding?.recyclerViewSlider?.layoutManager = LinearLayoutManager(context)
//            val adapterSlider = HomeSliderAdapter(requireActivity(), output.mv, this)
//            binding?.recyclerViewSlider?.layoutManager = gridLayoutSlider
//            binding?.recyclerViewSlider?.adapter = adapterSlider
//        }

        //Promotion
        if (output.ph.isNotEmpty()) updatePH(output.ph)

        //Movies
        var size = 0
        var single = false
        size = if ((output.mv.size % 2) == 0) {
            //Is even
            single = false
            output.mv.size
        } else {
            //Is odd
            single = true
            output.mv.size - 1
        }

        val gridLayoutMovies = GridLayoutManager(context, 2)
        binding?.recyclerMovies?.layoutManager = LinearLayoutManager(context)
        gridLayoutMovies.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == size) {
                    2
                } else {

                    1
                }
                return 1
            }
        }

        val adapterMovies = HomeMoviesAdapter(requireActivity(), output.mv, this, single)
        binding?.recyclerMovies?.layoutManager = gridLayoutMovies
        binding?.recyclerMovies?.adapter = adapterMovies
        ViewCompat.setNestedScrollingEnabled(binding?.recyclerMovies!!, false)

        //Offer
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val snapHelper: SnapHelper = PagerSnapHelper()
        binding?.recyclerOffer?.layoutManager = layoutManager
        binding?.recyclerOffer?.onFlingListener = null
        snapHelper.attachToRecyclerView(binding?.recyclerOffer!!)
        binding?.recyclerOffer?.layoutManager = layoutManager
        val adapterTrailer = HomeOfferListAdapter(requireActivity(), output.cp, this)
        binding?.recyclerOffer?.adapter = adapterTrailer

        if (output.cp.size > 1) {
            val speedScroll = 5000
            val handler = Handler()
            val runnable: Runnable = object : Runnable {
                var count = 0
                var flag = true
                override fun run() {
                    if (count < adapterTrailer.itemCount) {
                        if (count == adapterTrailer.itemCount - 1) {
                            flag = false
                        } else if (count == 0) {
                            flag = true
                        }
                        if (flag) count++ else count--
                        binding?.recyclerOffer?.smoothScrollToPosition(
                            count
                        )
                        handler.postDelayed(this, speedScroll.toLong())
                    }
                }
            }
            handler.postDelayed(runnable, speedScroll.toLong())
        }


//        binding?.filterFab?.setImageResource(R.drawable.filter_unselect)

        binding?.filterFab?.setOnClickListener {
            val filterPoints = HashMap<String, ArrayList<String>>()
            filterPoints[Constant.FilterType.LANG_FILTER] = output.mlng
            filterPoints[Constant.FilterType.GENERE_FILTER] = output.mgener
            filterPoints[Constant.FilterType.FORMAT_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.ACCESSABILITY_FILTER] = ArrayList(listOf("Subtitle"))
            filterPoints[Constant.FilterType.PRICE_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.SHOWTIME_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.CINEMA_FORMAT] = ArrayList()
            filterPoints[Constant.FilterType.SPECIAL_SHOW] = ArrayList()

            gFilter?.openFilters(
                context,
                "Home",
                onButtonSelected,
                appliedFilterType,
                appliedFilterItem,
                filterPoints
            )
        }

//        binding?.filterFab?.setOnTouchListener{ v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                binding?.swipeHome?.isRefreshing = false
//                // Do what you want
//                true
//            } else {
//                binding?.swipeHome?.isRefreshing = true
//                false
//            }
//        }
        if (!homeLoadBanner && output.pu.isNotEmpty()) {
            initBanner(output.pu)
        }

        recommend(output.rm)

        GoogleAnalytics.hitItemListEvent(requireContext(), "Ticket", output.mv)
    }

    @SuppressLint("SetTextI18n")
    private fun recommend(rm: HomeResponse.Mv) {
        if (upcomingBooking) {
            binding?.homeRecommend?.CLLayout1?.hide()
            binding?.homeRecommend?.CLLayout2?.show()
        } else {
            binding?.homeRecommend?.CLLayout1?.show()

            binding?.homeRecommend?.CLLayout2?.hide()
        }

        if (rm != null) {
            binding?.constraintLayout135?.show()
            //image
            binding?.homeRecommend?.ivRecomm?.let {
                Glide.with(requireActivity()).load(rm.mih).error(R.drawable.placeholder_horizental)
                    .into(it)
            }

            //trailer
            binding?.homeRecommend?.playBtn?.setOnClickListener {
                if (rm.trs.isNotEmpty() && rm.trs.size > 1) {
                    trailerList(rm)
                } else {
                    val intent = Intent(requireActivity(), PlayerActivity::class.java)
                    intent.putExtra("trailerUrl", rm.mtrailerurl)
                    startActivity(intent)
                }
            }

            //details
            binding?.homeRecommend?.rlRecomm?.setOnClickListener {
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "HomePage")
                    bundle.putString("var_book_now_screenname", "HomePage")
                    GoogleAnalytics.hitEvent(requireActivity(), "book_now", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val intent = Intent(requireActivity(), NowShowingMovieDetailsActivity::class.java)
                intent.putExtra("mid", rm.id)
                startActivity(intent)
            }

            //book
            binding?.homeRecommend?.tvBook?.setOnClickListener {
                val intent = Intent(requireActivity(), MovieSessionActivity::class.java)
                intent.putExtra("mid", rm.id)
                ISEvents().bookBtn(requireActivity(), rm.mcc)
                GoogleAnalytics.hitViewItemEvent(requireActivity(), "Ticket", rm.mcc, rm.n)
                mcId = rm.mcc
                startActivity(intent)
            }

            //title
            binding?.homeRecommend?.tvMovie?.text = rm.n

//            other genre
            if (rm.othergenres.contains("")) {
                if (rm.othergenres.split(",").size > 2) {
                    binding?.homeRecommend?.genrePlus?.show()
                    binding?.homeRecommend?.genrePlus?.text =
                        "+" + (rm.othergenres.split(",").size - 2)
                    binding?.homeRecommend?.tvGenre?.text =
                        rm.othergenres.split(",")[0] + " | " + rm.othergenres.split(",")[1]
                } else {
                    binding?.homeRecommend?.genrePlus?.hide()
                    binding?.homeRecommend?.tvGenre?.text = rm.othergenres.replace(",", " | ")
                }
            } else {
                var string = ""
                for (i in 0 until rm.grs.size) {
                    string =
                        if (i == rm.grs.size - 1) string + rm.grs[i] else string + rm.grs[i] + " • "
                }
                binding?.homeRecommend?.tvGenre?.text = string
            }

            if (rm.rt != "") {
                binding?.homeRecommend?.tvNewRe?.show()
                binding?.homeRecommend?.tvNewRe?.text = rm.rt
            } else {
                binding?.homeRecommend?.tvNewRe?.hide()
            }

            addCensor(rm, binding?.homeRecommend?.tvCensorLang)


            if (!TextUtils.isEmpty(rm.rtt)) binding?.homeRecommend?.tvRecomm?.text =
                rm.rtt else binding?.homeRecommend?.tvRecomm?.text = "TRENDING"
        } else {
            binding?.constraintLayout135?.hide()
        }
    }

    private fun addCensor(rm: HomeResponse.Mv, tvCensorLang: TextView?) {
        val stringBuilder = StringBuilder()
        var ssChange = false
        if (rm.ce.replace("\\(", "").replace("\\)", "").equals("A", ignoreCase = true)) ssChange =
            true
        stringBuilder.append(rm.ce.replace("\\(", "").replace("\\)", "") + " • ")
        for (i in 0 until rm.mfs.size) {
            val uiList: MutableList<List<String>> =
                listOf(rm.mfs[i].split(",")) as MutableList<List<String>>
            if (uiList.isNotEmpty()) {
                if (rm.mfs.size - 1 == i) {
                    printLog("stringBuilder--qd->${uiList[0]}---->${rm.mfs[i]}")

                    stringBuilder.append(uiList[0])
                } else stringBuilder.append(
                    uiList[0] + ", "
                )
            }

        }
        if (!ssChange) {
            printLog("stringBuilder--->${stringBuilder}")
            tvCensorLang?.text =
                stringBuilder.toString().replace("[", "").replace("]", "").replace("(", "")
                    .replace(")", "")

        } else {
            printLog("stringBuilder---2>${stringBuilder}")
            spannableTextBeing(
                stringBuilder, tvCensorLang!!
            )

        }
    }

    private fun spannableTextBeing(
        stringBuilder: java.lang.StringBuilder?, tvCensorLang: TextView
    ) {
        val ss = SpannableString(stringBuilder)
        ss.setSpan(
            ForegroundColorSpan(requireActivity().resources.getColor(R.color.yellow)),
            0,
            1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            ForegroundColorSpan(requireActivity().resources.getColor(R.color.gray_)),
            2,
            ss.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvCensorLang.text = ss
        tvCensorLang.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onCategoryClick(comingSoonItem: HomeResponse.Mfi) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Book ticket")
            bundle.putString("var_cinema_format", "veg")
            GoogleAnalytics.hitEvent(requireActivity(), "book_cinema_format", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        val intent = Intent(requireActivity(), FormatsActivity::class.java)
        intent.putExtra("format", comingSoonItem.name)
        startActivity(intent)
    }

    override fun onSliderBookClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), NowShowingMovieDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun onPromotionClick(comingSoonItem: HomeResponse.Mfi) {
        val intent = Intent(requireActivity(), NowShowingMovieDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.name)
        startActivity(intent)
    }

    override fun onTrailerClick(string: String, mv: HomeResponse.Mv) {
        if (mv.trs.isNotEmpty() && mv.trs.size > 1) {
            trailerList(mv)
        } else {
            val intent = Intent(requireActivity(), PlayerActivity::class.java)
            intent.putExtra("trailerUrl", string)
            startActivity(intent)
        }

    }

    override fun onMoviesClick(comingSoonItem: HomeResponse.Mv) {

        mcId = comingSoonItem.mcc
        val intent = Intent(requireActivity(), NowShowingMovieDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun onBookClick(comingSoonItem: HomeResponse.Mv) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "HomePage")
            bundle.putString("var_book_now_screenname", "HomePage")
            GoogleAnalytics.hitEvent(requireActivity(), "book_now", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(requireActivity(), MovieSessionActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        mcId = comingSoonItem.mcc
        ISEvents().bookBtn(requireActivity(), comingSoonItem.mcc)
        startActivity(intent)
    }

    override fun onOfferClick(comingSoonItem: HomeResponse.Cp) {
        if (comingSoonItem.t != null && comingSoonItem.t.equals(
                "campaign-VIDEO", ignoreCase = true
            )
        ) {
            val intent = Intent(requireActivity(), PlayerActivity::class.java)
            intent.putExtra("trailerUrl", comingSoonItem.mtrailerurl)
            startActivity(intent)
        } else if (comingSoonItem.t == "campaign-EXPATS") {
            if (comingSoonItem.mf) {
                val intent = Intent(requireActivity(), MovieSessionActivity::class.java)
                intent.putExtra("mid", comingSoonItem.id)
                startActivity(intent)
            } else {
                val intent = Intent(requireActivity(), NowShowingMovieDetailsActivity::class.java)
                intent.putExtra("mid", comingSoonItem.id)
                startActivity(intent)
            }

        } else if (comingSoonItem.t == "campaign-VKAAO") {

        } else if (comingSoonItem.t == "campaign-PPP") {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("from", "more")
            intent.putExtra(
                "title", comingSoonItem.n
            )
            intent.putExtra("getUrl", comingSoonItem.surl)
            startActivity(intent)
        } else if (comingSoonItem.t == "campaign-ACTIVITY") {
            val intent = Intent(context, OfferDetailsActivity::class.java)
            intent.putExtra("title", comingSoonItem.c)
            intent.putExtra("disc", comingSoonItem.t)
            intent.putExtra("id", comingSoonItem.id)
            startActivity(intent)
        } else if (comingSoonItem.t == "campaign-NOURL") {

        } else if (comingSoonItem.t == "campaign-GIFTCARD") {
            val intent = Intent(requireContext(), GiftCardActivity::class.java)
            startActivity(intent)
        } else if (comingSoonItem.t == "campaign-in") {
            if (comingSoonItem.surl.lowercase(Locale.ROOT).contains("/loyalty/home")) {
                val navigationView =
                    requireActivity().findViewById(R.id.bottomNavigationView) as BottomNavigationView
                val bottomMenuView = navigationView.getChildAt(0) as BottomNavigationMenuView
                val newView = bottomMenuView.getChildAt(2)
                val itemView = newView as BottomNavigationItemView
                itemView.performClick()
            } else {
                val intent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        comingSoonItem.surl.replace(
                            "https", "app"
                        )
                    )
                )
                startActivity(intent)
            }
        } else if (comingSoonItem.t == "campaign-within") {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("from", "more")
            intent.putExtra(
                "title", comingSoonItem.n
            )
            intent.putExtra("getUrl", comingSoonItem.surl)
            startActivity(intent)
        } else if (comingSoonItem.t == "campaign-out") {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(comingSoonItem.surl)
            )
            startActivity(intent)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun trailerList(mv: HomeResponse.Mv) {
        musicData.clear()
        videoData.clear()
        for (i in 0 until mv.trs.size) {
            if (mv.trs[i].ty.equals("MUSIC", ignoreCase = true)) {
                musicData.add(mv.trs[i])
            } else {
                videoData.add(mv.trs[i])
            }
        }

        dialogTrailer = Dialog(requireActivity())
        dialogTrailer?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogTrailer?.setCancelable(true)
        val inflater = LayoutInflater.from(requireContext())
        val bindingTrailer = TrailersDialogBinding.inflate(inflater)
        dialogTrailer?.setContentView(bindingTrailer.root)
        dialogTrailer?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialogTrailer?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialogTrailer?.window?.setGravity(Gravity.CENTER)
        dialogTrailer?.show()



        //title
        bindingTrailer.titleLandingScreen.text = mv.n

        val censor =
            mv.ce + " " + getString(R.string.dots) + " " + java.lang.String.join(",", mv.grs)

        bindingTrailer.tvCensorLang.text =
            censor.replace("[", "").replace("]", "").replace("(", "").replace(")", "")


        bindingTrailer.subTitleLandingScreen.text =
            mv.lng + " " + getString(R.string.dots) + " " + java.lang.String.join(
                ",", mv.mfs
            )

        //image
        Glide.with(requireActivity()).load(mv.miv).error(R.drawable.placeholder_vertical)
            .into(bindingTrailer.imageLandingScreen)

        //dialog Dismiss
        bindingTrailer.include49.imageView58.setOnClickListener {
            dialogTrailer?.dismiss()
        }

        //title
        bindingTrailer.include49.textView108.text = getString(R.string.trailer_amp_music)

        //button
        bindingTrailer.include50.textView5.text = getString(R.string.book_now)

        bindingTrailer.include50.textView5.setOnClickListener {
            val intent = Intent(requireActivity(), MovieSessionActivity::class.java)
            intent.putExtra("mid", mv.id)
            ISEvents().bookBtn(requireActivity(), mv.mcc)
            mcId = mv.mcc
            startActivity(intent)
        }
        if (videoData.size > 0) {
            bindingTrailer.textView69.show()
        } else {
            bindingTrailer.textView69.hide()
        }

//trailer
        val gridLayoutManager1 =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        val trailerAdapter = TrailerTrsAdapter(videoData, requireContext(), this)
        bindingTrailer.recyclerView5.layoutManager = gridLayoutManager1
        bindingTrailer.recyclerView5.adapter = trailerAdapter

//music
        if (musicData.size > 0) {
            bindingTrailer.textView70.show()
            bindingTrailer.recyclerMusic.show()
            val gridLayoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            val musicVideoTrsAdapter = MusicVideoTrsAdapter(musicData, requireContext(), this)
            bindingTrailer.recyclerMusic.layoutManager = gridLayoutManager
            bindingTrailer.recyclerMusic.adapter = musicVideoTrsAdapter
        } else {
            bindingTrailer.textView70.hide()
            bindingTrailer.recyclerMusic.hide()
        }
    }

    private fun nextBooking() {
        authViewModel.nextBookingResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        loader?.dismiss()
                        if (it.data.output is LinkedTreeMap<*, *>) {
                            val obj = it.data.output
                            upcomingBooking = obj["a"] as Boolean
                        } else {
                            upcomingBooking = true
                            retrieveNextBooking(it.data.output as List<LinkedTreeMap<*, *>>)
                        }
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
            }
        }

    }

    private fun retrieveNextBooking(output: List<LinkedTreeMap<*, *>>) {
        try {
            val gson = Gson()
            val jsonObj = JSONObject(output[0])
            val data = gson.fromJson(jsonObj.toString(), NextBookingResponse.Output::class.java)
            commonBooking(data)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun commonBooking(output: NextBookingResponse.Output) {
        if (upcomingBooking) {
            binding?.homeRecommend?.CLLayout2?.show()

            binding?.homeRecommend?.CLLayout1?.hide()

            binding?.homeRecommend?.tvRecommNew?.text = getString(R.string.your_booking)

            //title
            binding?.homeRecommend?.tvMovieNew?.text = output.mn

            //time
            binding?.homeRecommend?.tvTime?.text =
                output.sd + " " + getString(R.string.dots) + " " + output.st

            //cinema name
            binding?.homeRecommend?.tvPlace?.text = output.cn

            //movie Page
            binding?.homeRecommend?.tvOpenM?.setOnClickListener {
                val intent = Intent(requireActivity(), MyBookingsActivity::class.java)
                startActivity(intent)
            }

            //bookOla
            binding?.homeRecommend?.tvOLA?.setOnClickListener {}

        } else {
            binding?.homeRecommend?.CLLayout2?.hide()

            binding?.homeRecommend?.CLLayout1?.show()
        }


    }


    //filter
    override fun onApply(
        type: ArrayList<String>?,
        filterItemSelected: HashMap<String, String>?,
        isSelected: Boolean,
        name: String
    ) {
        if (type!!.size > 1) {
            println("type--->$type---->$filterItemSelected---->")

            binding?.filterFab?.setImageResource(R.drawable.filter_selected)
            appliedFilterItem = filterItemSelected!!
            val containLanguage = filterItemSelected.contains("language")
            if (containLanguage) {
                val index = type.indexOf("language")
                var value: String = filterItemSelected[type[index]].toString()
                if (!value.equals("", ignoreCase = true)) {
                    buttonPressed = ArrayList()
                    appliedFilterType = "language"
                    value = value.uppercase(Locale.getDefault())
                    val valuesString = value.split(",").toTypedArray()
                    for (s in valuesString) {
                        if (!buttonPressed.contains(s)) buttonPressed.add("$s-language")
                    }
                    binding?.filterFab?.setImageResource(R.drawable.filter_selected)


                    // Hit Event
                    try {
                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Homepage")
                        bundle.putString("home_filter_genre", value)
                        GoogleAnalytics.hitEvent(requireActivity(), "home_filter_genre", bundle)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
                    buttonPressed = ArrayList()
                    println("value---->$value----$buttonPressed")

                }
            } else {
                buttonPressed = ArrayList()
            }

            val containGenres = filterItemSelected.contains("geners")
            if (containGenres) {
                val index = type.indexOf("geners")
                var value: String = filterItemSelected[type[index]]!!
                if (!value.equals("", ignoreCase = true)) {
                    appliedFilterType = "geners"
                    generaSelected = ArrayList()
                    value = value.uppercase(Locale.getDefault())
                    val valuesString = value.split(",").toTypedArray()
                    for (s in valuesString) {
                        if (!generaSelected?.contains(s)!!) generaSelected?.add("$s-geners")
                    }
                    binding?.filterFab?.setImageResource(R.drawable.filter_selected)

                    // Hit Event
                    try {
                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Homepage")
                        bundle.putString("var_home_filter", valuesString.toString())
                        GoogleAnalytics.hitEvent(requireActivity(), "home_filter_genre", bundle)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
                    generaSelected = ArrayList()
                }
            } else {
                generaSelected = ArrayList()
            }

            val containAccessibility = filterItemSelected.contains("accessability")
            if (containAccessibility) {
                val index = type.indexOf("accessability")
                val value: String = filterItemSelected[type[index]]!!
                if (value != "") {
                    special = "English Subtitle"
                    binding?.filterFab?.setImageResource(R.drawable.filter_selected)
                }

                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Homepage")
                    bundle.putString("home_filter_subtiltel", value)
                    GoogleAnalytics.hitEvent(requireActivity(), "home_filter_genre", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                special = "ALL"
            }

            getMovieFormatFromApi()
        } else {
            binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
        }
    }

    override fun onReset() {
        binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
        buttonPressed = ArrayList()
        generaSelected = ArrayList()
        appliedFilterItem = HashMap()
        authViewModel.home(
            preferences.getCityName(),
            "",
            preferences.getUserId(),
            preferences.geMobileNumber(),
            upcomingBooking,
            "no",
            "",
            "ALL",
            "ALL",
            "ALL",
            "no"
        )
    }

    private fun getMovieFormatFromApi() {
        getMoviesForUNowShowingHit()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getMoviesForUNowShowingHit() {
        upcomingBooking = false
        var specialText = "ALL"
        if (!special.equals("ALL", ignoreCase = true)) {
            specialText = special
        }

        if (buttonPressed.isNotEmpty()) {
            lang = buttonPressed[0].split("-").toTypedArray()[0].trim { it <= ' ' }
            for (i in 1 until buttonPressed.size) lang =
                lang + "," + buttonPressed[i].split("-").toTypedArray()[0].trim { it <= ' ' }
        } else {
            lang = "ALL"
        }
        if (generaSelected!!.isNotEmpty()) {
            format = generaSelected!![0]!!.split("-").toTypedArray()[0].trim { it <= ' ' }
            for (i in 1 until generaSelected!!.size) format =
                format + "," + generaSelected!![i]!!.split("-").toTypedArray()[0].trim { it <= ' ' }
        } else {
            format = "ALL"
        }

        if (!cinemaType.equals("All", ignoreCase = true)) {
            authViewModel.home(
                preferences.getCityName(),
                "",
                preferences.getUserId(),
                preferences.geMobileNumber(),
                upcomingBooking,
                "no",
                cinemaType,
                lang,
                format,
                specialText,
                "no"
            )
        } else {
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

        if (preferences.getIsLogin()) {
            val ls = preferences.getString(Constant.SharedPreference.LOYALITY_STATUS)
            val isHl: String = preferences.getString(Constant.SharedPreference.IS_HL)
            val isLy: String = preferences.getString(Constant.SharedPreference.IS_LY)
            println("LS---$ls--$isHl--$isLy")
            if (isLy.equals("true", ignoreCase = true)) {
                if (ls != null && !ls.equals("", ignoreCase = true)) {
                    if (isHl.equals("true", ignoreCase = true)) {
                        binding?.privilegeLoginUi?.show()
                        binding?.privilegeLogOutUi?.hide()
                        if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == ACTIVE && preferences.getString(
                                SUBS_OPEN
                            ) == "true"
                        ) {
                            binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.subs_back_b)
                        } else {
                            binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.privilege_home_back)
                        }
                    } else {
                        if (ls != null && !ls.equals("", ignoreCase = true)) {
                            binding?.privilegeLogOutUi?.hide()
                            binding?.privilegeLoginUi?.show()
                            if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == ACTIVE && preferences.getString(
                                    SUBS_OPEN
                                ) == "true"
                            ) {
                                binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.subs_back_b)
                            } else {
                                binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.privilege_home_back)
                            }
                        } else {
                            binding?.privilegeLogOutUi?.show()
                            binding?.privilegeLoginUi?.hide()
                        }
                    }
                } else {
                    binding?.privilegeLogOutUi?.show()
                    binding?.privilegeLoginUi?.hide()
                }
            } else {
                if (ls != null && !ls.equals("", ignoreCase = true)) {
                    binding?.privilegeLogOutUi?.hide()
                    binding?.privilegeLoginUi?.show()
                    if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == ACTIVE && preferences.getString(
                            SUBS_OPEN
                        ) == "true"
                    ) {
                        binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.subs_back_b)
                    } else {
                        binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.privilege_home_back)
                    }
                } else {
                    binding?.privilegeLogOutUi?.show()
                    binding?.privilegeLoginUi?.hide()
                }
            }

            printLog("pt---->${PRIVILEGEPOINT}")
            binding?.privilegeLogin?.pt?.text = PRIVILEGEPOINT
            binding?.privilegeLogin?.numVou?.text = PRIVILEGEVOUCHER
            binding?.privilegeLogin?.qrImgMainPage?.setOnClickListener {
                oPenDialogQR()
            }

        } else {
            binding?.privilegeLoginUi?.hide()
            binding?.privilegeLogOutUi?.show()
        }

    }

    public fun updatedData(){
        if (preferences.getIsLogin()) {
            val ls = preferences.getString(Constant.SharedPreference.LOYALITY_STATUS)
            val isHl: String = preferences.getString(Constant.SharedPreference.IS_HL)
            val isLy: String = preferences.getString(Constant.SharedPreference.IS_LY)
            println("LS---$ls--$isHl--$isLy")
            if (isLy.equals("true", ignoreCase = true)) {
                if (ls != null && !ls.equals("", ignoreCase = true)) {
                    if (isHl.equals("true", ignoreCase = true)) {
                        binding?.privilegeLoginUi?.show()
                        binding?.privilegeLogOutUi?.hide()
                        if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == ACTIVE && preferences.getString(
                                SUBS_OPEN
                            ) == "true"
                        ) {
                            binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.subs_back_b)
                        } else {
                            binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.privilege_home_back)
                        }
                    } else {
                        if (ls != null && !ls.equals("", ignoreCase = true)) {
                            binding?.privilegeLogOutUi?.hide()
                            binding?.privilegeLoginUi?.show()
                            if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == ACTIVE && preferences.getString(
                                    SUBS_OPEN
                                ) == "true"
                            ) {
                                binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.subs_back_b)
                            } else {
                                binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.privilege_home_back)
                            }
                        } else {
                            binding?.privilegeLogOutUi?.show()
                            binding?.privilegeLoginUi?.hide()
                        }
                    }
                } else {
                    binding?.privilegeLogOutUi?.show()
                    binding?.privilegeLoginUi?.hide()
                }
            } else {
                if (ls != null && !ls.equals("", ignoreCase = true)) {
                    binding?.privilegeLogOutUi?.hide()
                    binding?.privilegeLoginUi?.show()
                    if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == ACTIVE && preferences.getString(
                            SUBS_OPEN
                        ) == "true"
                    ) {
                        binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.subs_back_b)
                    } else {
                        binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.privilege_home_back)
                    }
                } else {
                    binding?.privilegeLogOutUi?.show()
                    binding?.privilegeLoginUi?.hide()
                }
            }

            printLog("pt---->${PRIVILEGEPOINT}")
            binding?.privilegeLogin?.pt?.text = PRIVILEGEPOINT
            binding?.privilegeLogin?.numVou?.text = PRIVILEGEVOUCHER
            binding?.privilegeLogin?.qrImgMainPage?.setOnClickListener {
                oPenDialogQR()
            }

        } else {
            binding?.privilegeLoginUi?.hide()
            binding?.privilegeLogOutUi?.show()
        }
    }

    @SuppressLint("CutPasteId", "ClickableViewAccessibility")
    private fun initBanner(bannerModels: ArrayList<HomeResponse.Pu>) {
        homeLoadBanner = true
        bannerShow += 1
        bannerModelsMain = bannerModels
        if (bannerModels.isNotEmpty()) {
            rlBanner?.show()
            stories?.setStoriesCount(bannerModels.size) // <- set stories
            stories?.setStoryDuration(5000L) // <- set a story duration
            stories?.setStoriesListener(this) // <- set listener
            stories?.startStories() // <- start progress
            counterStory = 0
            if (!TextUtils.isEmpty(bannerModels[counterStory].i)) {
                Picasso.get().load(bannerModels[counterStory].i)
                    .into(ivBanner!!, object : Callback {
                        override fun onSuccess() {
                            rlBanner?.show()
                            //  storiesProgressView.startStories(); // <- start progress
                        }

                        override fun onError(e: Exception) {
                            e.printStackTrace()
                        }
                    })
            }

            reverse?.setOnClickListener {
                stories?.reverse()
            }
            reverse?.setOnTouchListener(onTouchListener)
            showButton(bannerModels[0])
            skip?.setOnClickListener { stories?.skip() }
            skip?.setOnTouchListener(onTouchListener)
            tvButton?.setOnClickListener {
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home Screen")
                    bundle.putString("var_popup_name", bannerModels[counterStory].name.toString())
                    GoogleAnalytics.hitEvent(requireContext(), "app_popups", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                rlBanner?.hide()
                listener?.onShowNotification()
                listener?.onShowPrivilege()
                if (bannerModels.size > 0 && bannerModels[counterStory].type.equals(
                        "image", ignoreCase = true
                    )
                ) {
                    if (bannerModels[counterStory].redirectView.equals(
                            "DEEPLINK", ignoreCase = true
                        )
                    ) {
                        if (!bannerModels[counterStory].redirect_url.equals(
                                "", ignoreCase = true
                            )
                        ) {
                            if (bannerModels[counterStory].redirect_url.lowercase(Locale.ROOT)
                                    .contains("/loyalty/home")
                            ) {
                                val navigationView =
                                    requireActivity().findViewById(R.id.bottomNavigationView) as BottomNavigationView
                                val bottomMenuView =
                                    navigationView.getChildAt(0) as BottomNavigationMenuView
                                val newView = bottomMenuView.getChildAt(2)
                                val itemView = newView as BottomNavigationItemView
                                itemView.performClick()
                            } else {
                                val intent = Intent(
                                    Intent.ACTION_VIEW, Uri.parse(
                                        bannerModels[counterStory].redirect_url.replace(
                                            "https", "app"
                                        )
                                    )
                                )
                                startActivity(intent)
                            }
                        }
                    } else if (bannerModels[counterStory].redirectView == "INAPP") {
                        if (bannerModels[counterStory].redirect_url.isNotEmpty()) {
                            val intent = Intent(context, WebViewActivity::class.java)
                            intent.putExtra("title", bannerModels[counterStory].name)
                            intent.putExtra("from", "web")
                            intent.putExtra("getUrl", bannerModels[counterStory].redirect_url)
                            startActivity(intent)
                        }
                    } else if (bannerModels[counterStory].redirectView.equals(
                            "WEB", ignoreCase = true
                        )
                    ) {
                        if (!bannerModels[counterStory].redirect_url.equals(
                                "", ignoreCase = true
                            )
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
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home Screen")
                    bundle.putString("var_popup_name", bannerModels[counterStory].name.toString())
                    GoogleAnalytics.hitEvent(requireContext(), "app_popups", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                rlBanner?.hide()
                listener?.onShowNotification()
                listener?.onShowPrivilege()
                if (bannerModels.size > 0 && bannerModels[counterStory].type.equals(
                        "video", ignoreCase = true
                    )
                ) {
                    val intent = Intent(requireActivity(), PlayerActivity::class.java)
                    intent.putExtra("trailerUrl", bannerModels[counterStory].trailerUrl)
                    startActivity(intent)
                }
            }

        } else {
            rlBanner?.hide()
            listener?.onShowNotification()
            listener?.onShowPrivilege()
        }
    }

    private fun showButton(bannerModel: HomeResponse.Pu) {
        if (bannerModel.type.uppercase(Locale.getDefault()) == "VIDEO" && bannerModel.trailerUrl != "") {
            ivPlay?.show()
            tvButton?.hide()
        } else if (bannerModel.type.uppercase(Locale.getDefault()) == "IMAGE" && bannerModel.redirect_url != "") {
            ivPlay?.hide()
            tvButton?.text = bannerModel.buttonText
            if (bannerModel.buttonText.isNotEmpty()) tvButton?.show()
        } else {
            ivPlay?.hide()
            tvButton?.hide()
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
        rlBanner?.hide()
        listener?.onShowNotification()
        listener?.onShowPrivilege()
    }

    private fun oPenDialogQR() {
        val dialogQR = Dialog(requireActivity())
        dialogQR.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogQR.setCancelable(false)
        dialogQR.setContentView(R.layout.activity_privilege_details)
        dialogQR.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialogQR.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialogQR.window?.setGravity(Gravity.CENTER)
        dialogQR.setTitle("")
        val pointsPcTextView = dialogQR.findViewById<TextView>(R.id.points_txt)
        val vouchersPcTextView = dialogQR.findViewById<TextView>(R.id.vouchers_txt_)
        val tvUserName: TextView = dialogQR.findViewById<View>(R.id.TVusername) as TextView
        val ivImage = dialogQR.findViewById<View>(R.id.ivImage) as ImageView
        val tvCross = dialogQR.findViewById<View>(R.id.tvCross) as ImageView
        Glide.with(requireActivity()).load(qrCode).into(ivImage)
        tvUserName.text = preferences.getUserName()
        pointsPcTextView.text = PRIVILEGEPOINT
        vouchersPcTextView.text = PRIVILEGEVOUCHER
        tvCross.setOnClickListener { dialogQR.dismiss() }
        dialogQR.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialogQR.dismiss()
            }
            true
        }
        dialogQR.show()
    }

    private fun updatePH(phd: ArrayList<HomeResponse.Ph>?) {
        if (phd != null && phd.size > 0) {
            binding?.includePlaceHolder?.placeHolderView?.show()
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            binding?.includePlaceHolder?.recyclerPromotion?.layoutManager = layoutManager
            binding?.includePlaceHolder?.recyclerPromotion?.onFlingListener = null
            snapHelper.attachToRecyclerView(binding?.includePlaceHolder?.recyclerPromotion!!)
            binding?.includePlaceHolder?.recyclerPromotion?.layoutManager = layoutManager
            val adapter = PromotionAdapter(requireActivity(), phd)
            binding?.includePlaceHolder?.recyclerPromotion?.adapter = adapter
            if (phd.size > 1) {
                val speedScroll = 5000
                val handler = Handler()
                val runnable: Runnable = object : Runnable {
                    var count = 0
                    var flag = true
                    override fun run() {
                        if (count < adapter.itemCount) {
                            if (count == adapter.itemCount - 1) {
                                flag = false
                            } else if (count == 0) {
                                flag = true
                            }
                            if (flag) count++ else count--
                            binding?.includePlaceHolder?.recyclerPromotion?.smoothScrollToPosition(
                                count
                            )
                            handler.postDelayed(this, speedScroll.toLong())
                        }
                    }
                }
                handler.postDelayed(runnable, speedScroll.toLong())
            }
        } else {
            binding?.includePlaceHolder?.placeHolderView?.hide()
        }
    }

    //Internet Check
    private fun broadcastIntent() {
        requireActivity().registerReceiver(
            broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun musicVideoTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.u)
        startActivity(intent)
    }

    override fun trailerTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.u)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        binding?.includeAppBar?.txtCity?.text = preferences.getCityName()
        if (preferences.getIsLogin()) {
            binding?.includeAppBar?.profileBtn?.hide()
            binding?.includeAppBar?.textView2?.text = "Hello, " + preferences.getUserName()
        } else {
            binding?.includeAppBar?.profileBtn?.hide()
            binding?.includeAppBar?.textView2?.text = "Hello!"
        }


    }

    override fun offerClick(comingSoonItem: OfferResponse.Offer) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun getShowCountHome(filterStrings: Map<String, ArrayList<String?>>): Int {
        var showCount = 0
        var languages: List<String?> = ArrayList()
        var genres: List<String?> = ArrayList()
        var spShows: List<String?> = ArrayList()

        for (entry in filterStrings) {
            val type = entry.key
            if (type.contains("language")) {
                languages = entry.value
            } else if (type.contains("geners")) {
                genres = entry.value
            } else if (type.contains("accessability")) {
                spShows = entry.value
            }
            //showCount = showCount +getCount(type , entry.getValue());
        }

        showCount = if (languages.isEmpty() && genres.isEmpty() && spShows.isEmpty()){
            0
        }else {
            filterMovies(
                movieData,
                languages,
                genres,
                spShows
            )
        }
        println("showCount--->$showCount---${movieData.size}")
        return showCount
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun filterMovies(
        movies:ArrayList<HomeResponse.Mv>,
        languages: List<String?>,
        genres: List<String?>,
        spShows: List<String?>
    ): Int {
        val filteredMovies: MutableList<HomeResponse.Mv> = ArrayList<HomeResponse.Mv>()
        for (movie in movies) {
            if (!languages.contains("ALL") && languages.isNotEmpty()) {

                val languagesM = movie.otherlanguages.split(",").toList()

                if (languagesM.stream().noneMatch { m: String -> languages.contains(m.uppercase()) }) continue
            }
            if (!genres.contains("ALL") && genres.isNotEmpty()) {
                val genresM: List<String> = movie.othergenres.split(",").toList()
                if (genresM.stream().noneMatch { m: String ->
                        genres.contains(
                            m.uppercase()
                        )
                    }) continue
            }
            if (!spShows.contains("ALL") && spShows.isNotEmpty()) {
                if (movie.sh != "") continue
            }

            filteredMovies.add(movie)
        }
        return filteredMovies.size
    }


}