package com.net.pvr1.ui.home.fragment.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
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
import com.net.pvr1.utils.Constant.Companion.PRIVILEGEPOINT
import com.net.pvr1.utils.Constant.Companion.PRIVILEGEVOUCHER
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
    private var upcomingBooking = false
    private var buttonPressed = ArrayList<String>()
    private var generseleced: ArrayList<String?>? = ArrayList<String?>()

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
    private var RlBanner: RelativeLayout? = null
    private var stories: StoriesProgressView? = null

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
        createQr()
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
        ivCross?.setOnClickListener {
            RlBanner?.hide()
//                Constant.PvrHB = ""
        }
        RlBanner?.hide()

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
        binding?.recyclerTrailer?.onFlingListener = null
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding?.recyclerTrailer)
        val gridLayoutTrailer =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
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
            filterPoints[Constant.FilterType.ACCESSABILITY_FILTER] = ArrayList(listOf("Subtitle"))
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


        if (output.rm != null) {
            recommend(output.rm)
        }

        if (bannerShow == 0) {
            initBanner(output.pu)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun recommend(rm: HomeResponse.Rm) {
        if (rm != null) {
            //image
            Glide.with(this).load(rm.i).into(binding?.homeRecommend?.ivRecomm!!)
            //trailer
            binding?.homeRecommend?.playBtn?.setOnClickListener {
                val intent = Intent(requireActivity(), PlayerActivity::class.java)
                intent.putExtra("trailerUrl", rm.mtrailerurl)
                startActivity(intent)
            }
            //title
            binding?.homeRecommend?.tvMovie?.text = rm.n

            //trending
            binding?.homeRecommend?.tvCensorLang?.text =
                rm.ce + " • " + java.lang.String.join(",", rm.grs)

            //    tvMovie.setSelected(true);
            if (rm.otherlanguages.equals("",ignoreCase = true)) {
                if (rm.otherlanguages.split(",").size > 2) {
                    binding?.homeRecommend?.genrePlus?.visibility = View.VISIBLE
                    binding?.homeRecommend?.genrePlus?.text =
                        "+" + (rm.othergenres.split(",").size - 2)
                    binding?.homeRecommend?.tvGenre?.text =
                        rm.othergenres.split(",")[0] + " | " + rm.othergenres.split(",")[1]
                } else {
                    binding?.homeRecommend?.genrePlus?.visibility = View.GONE
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


            if (!TextUtils.isEmpty(rm.rtt)) binding?.homeRecommend?.tvRecomm?.text =
                rm.rtt else binding?.homeRecommend?.tvRecomm?.text = "TRENDING"
        }

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
                var value: String = filterItemSelected[type[index]]!!
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
            val containAccessibility = type.contains("accessability")
            if (containAccessibility) {
                val index = type.indexOf("accessability")
                val value: String = filterItemSelected[type[index]]!!
//
//                if (value != null && !value.equals("", ignoreCase = true)) {
//                    if (value.equals("Subtitle", ignoreCase = true)) special = output.getMspecial().get(0)
//                } else {
//                    special = "ALL"
//                }
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

    private fun getMovieFormatFromApi(isFirstTime: Boolean) {
        getMoviesForUNowShowingHit()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getMoviesForUNowShowingHit() {
        upcomingBooking = preferences.getIsLogin() == false
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

        if (!cinema_type.equals("All", ignoreCase = true)) {
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
            binding?.privilegeLoginUi?.show()
            binding?.privilegeLogOutUi?.hide()
            printLog("pt---->${PRIVILEGEPOINT}")
            binding?.privilegeLogin?.pt?.text = PRIVILEGEPOINT
            binding?.privilegeLogin?.numVou?.text = PRIVILEGEVOUCHER
            binding?.privilegeLogin?.qrImgMainPage?.setOnClickListener {
//                val intent = Intent(requireActivity(), PrivilegeDetailsActivity::class.java)
//                startActivity(intent)
                oPenDialogQR()
            }

            authViewModel.nextBooking(
                preferences.getUserId(), Constant().getDeviceId(requireActivity())
            )
        } else {
            binding?.privilegeLoginUi?.hide()
            binding?.privilegeLogOutUi?.show()
        }

    }

    @SuppressLint("CutPasteId", "ClickableViewAccessibility")
    private fun initBanner(bannerModels: ArrayList<HomeResponse.Pu>) {
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
                Picasso.get().load(bannerModels[counterStory].i).into(ivBanner!!, object : Callback {
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
                if (bannerModels != null && bannerModels.size > 0 && bannerModels[counterStory].type.equals("image",ignoreCase = true)
//                        .equalsIgnoreCase("image")
                ) {
                    if (bannerModels[counterStory].redirectView.equals("DEEPLINK",ignoreCase = true)
//                        equalsIgnoreCase("DEEPLINK")
                    ) {
                        if (bannerModels[counterStory].redirect_url != null && bannerModels[counterStory].redirect_url.equals("",ignoreCase = true)) {
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
                                            "https", "app"
                                        )
                                    )
                                )
                                startActivity(intent)
                            }
                        }
                    } else if (bannerModels[counterStory].redirect_url.equals("INAPP",ignoreCase = true)
                    ) {
                        if (bannerModels[counterStory].redirect_url != null && bannerModels[counterStory].redirect_url.equals("",ignoreCase = true)
                        ) {
//                            val intent = Intent(context, PrivacyActivity::class.java)
//                            intent.putExtra("url", bannerModels[counterStory].getRedirect_url())
//                            intent.putExtra(PCConstants.IS_FROM, 2000)
//                            intent.putExtra("title", bannerModels[counterStory].getName())
//                            startActivity(intent)
                        }
                    } else if (bannerModels[counterStory].redirect_url.equals("WEB",ignoreCase = true)
//                            .equalsIgnoreCase("WEB")
                    ) {
                        if (bannerModels[counterStory].redirect_url != null && bannerModels[counterStory].redirect_url.equals("",ignoreCase = true)
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

            ivPlay?.setOnClickListener {
                RlBanner?.hide()
                if (bannerModels != null && bannerModels.size > 0 && bannerModels[counterStory].type.equals("video",ignoreCase = true)
//                        .equalsIgnoreCase("video")

                ) {
                }
            }

        } else {
            RlBanner?.hide()
        }
    }

    private fun showButton(bannerModel: HomeResponse.Pu) {
        if (bannerModel.type.equals("video",ignoreCase = true)) {
            ivPlay?.show()
            tvButton?.hide()
        } else if (bannerModel.type.equals("image",ignoreCase = true)&& bannerModel.redirect_url.equals("",ignoreCase = true)
//                .equalsIgnoreCase("")
        ) {
            ivPlay?.hide()
            tvButton?.text = bannerModel.buttonText
            tvButton?.show()
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
        RlBanner?.hide()
    }

    private fun oPenDialogQR() {
        val dialogQR = Dialog(requireActivity())
        dialogQR.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogQR.setCancelable(false)
        dialogQR.setContentView(R.layout.activity_privilege_details)
        dialogQR.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialogQR.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
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

}