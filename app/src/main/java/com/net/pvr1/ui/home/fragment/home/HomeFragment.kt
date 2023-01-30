package com.net.pvr1.ui.home.fragment.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
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
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentHomeBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.bookingSession.BookingActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.filter.GenericFilterHome
import com.net.pvr1.ui.home.formats.FormatsActivity
import com.net.pvr1.ui.home.fragment.home.adapter.*
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.home.fragment.home.response.NextBookingResponse
import com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr1.ui.home.fragment.more.profile.userDetails.ProfileActivity
import com.net.pvr1.ui.home.interfaces.PlayPopup
import com.net.pvr1.ui.location.selectCity.SelectCityActivity
import com.net.pvr1.ui.movieDetails.nowShowing.NowShowingActivity
import com.net.pvr1.ui.myBookings.MyBookingsActivity
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.ui.scanner.ScannerActivity
import com.net.pvr1.ui.search.searchHome.SearchHomeActivity
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.PRIVILEGEPOINT
import com.net.pvr1.utils.Constant.Companion.PRIVILEGEVOUCHER
import com.net.pvr1.utils.Constant.Companion.PlaceHolder
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeCinemaCategoryAdapter.RecycleViewItemClickListener,
    HomeSliderAdapter.RecycleViewItemClickListener,
    HomePromotionAdapter.RecycleViewItemClickListener,
    HomeMoviesAdapter.RecycleViewItemClickListener, HomeOfferAdapter.RecycleViewItemClickListener,
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
    private var cinemaType = "ALL"
    private var upcomingBooking = false
    private var buttonPressed = ArrayList<String>()
    private var generaSelected: ArrayList<String?>? = ArrayList<String?>()

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

    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

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

//            nextBooking
            authViewModel.nextBooking(preferences.getUserId(),Constant().getDeviceId(requireActivity()))

        } else {
            binding?.includeAppBar?.profileBtn?.hide()
            binding?.constraintLayout135?.hide()
        }

        getShimmerData()
        movedNext()
        homeApi()
        nextBooking()
        createQr()
        getMovieFormatFromApi()

        //internet Check
        broadcastReceiver = NetworkReceiver()
        broadcastIntent()
    }

    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    private fun movedNext() {
        // manage top bar ui
        binding?.includeAppBar?.searchBtn?.setOnClickListener {
            val intent = Intent(requireActivity(), SearchHomeActivity::class.java)
            startActivity(intent)
        }

        // Select City
        binding?.includeAppBar?.txtCity?.setOnClickListener {
            val intent = Intent(requireActivity(), SelectCityActivity::class.java)
            startActivity(intent)
        }
        // Qr COde
        binding?.includeAppBar?.scanQr?.setOnClickListener {
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
            binding?.includeAppBar?.textView2?.text = "Hello, " + preferences.getUserName()
        } else {
            binding?.includeAppBar?.profileBtn?.hide()
            binding?.includeAppBar?.textView2?.text = "Hello!"
        }

        binding?.includeAppBar?.txtCity?.text = preferences.getCityName()

        //banner
        ivCross?.setOnClickListener {
            rlBanner?.hide()
            listener?.onShowNotification()
            listener?.onShowPrivilege()
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
                    println("loadingHome--->")
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun retrieveData(output: HomeResponse.Output) {
        //layout
        binding?.constraintLayout144?.show()
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
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val snapHelper: SnapHelper = PagerSnapHelper()
        binding?.recyclerOffer?.layoutManager = layoutManager
        binding?.recyclerOffer?.onFlingListener = null
        snapHelper.attachToRecyclerView(binding?.recyclerOffer!!)
        val adapterTrailer = HomeOfferAdapter(requireActivity(), output.cp, this)
        binding?.recyclerOffer?.layoutManager = layoutManager
        binding?.recyclerOffer?.adapter = adapterTrailer

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

        if (bannerShow == 0 && output.pu.isNotEmpty()) {
            initBanner(output.pu)
        }

        if (preferences.getIsLogin())
            recommend(output.rm)
    }

    @SuppressLint("SetTextI18n")
    private fun recommend(rm: HomeResponse.Rm) {
        if (upcomingBooking==true){

            binding?.homeRecommend?.CLLayout1?.hide()
            binding?.homeRecommend?.CLLayout2?.show()
        }else{
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
                val intent = Intent(requireActivity(), PlayerActivity::class.java)
                intent.putExtra("trailerUrl", rm.mtrailerurl)
                startActivity(intent)
            }

            //details
            binding?.homeRecommend?.rlRecomm?.setOnClickListener {
                val intent = Intent(requireActivity(), NowShowingActivity::class.java)
                intent.putExtra("mid", rm.id)
                startActivity(intent)
            }

            //book
            binding?.homeRecommend?.tvBook?.setOnClickListener {
                val intent = Intent(requireActivity(), BookingActivity::class.java)
                intent.putExtra("mid", rm.id)
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

            addCensor(rm,binding?.homeRecommend?.tvCensorLang)


            if (!TextUtils.isEmpty(rm.rtt))
                binding?.homeRecommend?.tvRecomm?.text =
                rm.rtt else binding?.homeRecommend?.tvRecomm?.text = "TRENDING"
        } else {
            binding?.constraintLayout135?.hide()
        }
    }

    private fun addCensor(rm: HomeResponse.Rm, tvCensorLang: TextView?) {
        val stringBuilder = StringBuilder()
        var ssChange = false
        if (rm.ce.replace("\\(", "").replace("\\)", "")
                .equals("A",ignoreCase = true)) ssChange = true
        stringBuilder.append(rm.ce.replace("\\(", "").replace("\\)", "") + " • ")
        for (i in 0 until rm.mfs.size) {
            val uiList: MutableList<List<String>> = listOf(rm.mfs[i].split(",")) as MutableList<List<String>>
            if (uiList.isNotEmpty()) {
                if (rm.mfs.size - 1 == i){
                    printLog("stringBuilder--qd->${uiList[0]}---->${rm.mfs[i]}")

                    stringBuilder.append(uiList[0])
                } else stringBuilder.append(
                    uiList[0] + ", "
                )
            }

        }
        if (!ssChange){
            printLog("stringBuilder--->${stringBuilder}")
            tvCensorLang?.text = stringBuilder.toString().replace("[", "").replace("]", "").replace("(", "").replace(")", "")

        }
        else {
            printLog("stringBuilder---2>${stringBuilder}")
            spannableTextBeing(
               stringBuilder,
                tvCensorLang!!
            )

        }
    }
    private fun spannableTextBeing(
        stringBuilder: java.lang.StringBuilder?,
        tvCensorLang: TextView
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

    override fun onTrailerClick(string: String) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java)
        intent.putExtra("trailerUrl", string)
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

    override fun onOfferClick(comingSoonItem: HomeResponse.Cp) {
        if (comingSoonItem.t != null && comingSoonItem.t.equals("campaign-VIDEO", ignoreCase = true)) {
            val intent = Intent(requireActivity(), PlayerActivity::class.java)
            intent.putExtra("trailerUrl", comingSoonItem.mtrailerurl)
            startActivity(intent)
        }
    }

    private fun nextBooking() {
        authViewModel.nextBookingResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        loader?.dismiss()
                        if (it.data.output is LinkedTreeMap<*, *>){
                            val obj = it.data.output as LinkedTreeMap<*, *>
                            upcomingBooking = obj["a"] as Boolean
                        }else {
                            upcomingBooking= true
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
        }catch (e:JSONException){
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun commonBooking(output: NextBookingResponse.Output) {
        if (upcomingBooking==true){
            binding?.homeRecommend?.CLLayout2?.show()

            binding?.homeRecommend?.CLLayout1?.hide()

            binding?.homeRecommend?.tvRecommNew?.text = getString(R.string.your_booking)

            //title
            binding?.homeRecommend?.tvMovieNew?.text = output.mn

            //time
            binding?.homeRecommend?.tvTime?.text = output.sd +" "+ getString(R.string.dots)+" "+output.st

            //cinema name
            binding?.homeRecommend?.tvPlace?.text = output.cn

            //movie Page
            binding?.homeRecommend?.tvOpenM?.setOnClickListener {
                val intent = Intent(requireActivity(), MyBookingsActivity::class.java)
                startActivity(intent)
            }

            //bookOla
            binding?.homeRecommend?.tvOLA?.setOnClickListener {

            }

        }else{
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
            binding?.filterFab?.setImageResource(R.drawable.filter_selected)
            appliedFilterItem = filterItemSelected!!
            val containLanguage = type.contains("language")
            if (containLanguage) {
                val index = type.indexOf("language")
                var value: String = filterItemSelected[type[index]]!!
                if (!value.equals("", ignoreCase = true)) {
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
                if (!value.equals("", ignoreCase = true)) {
                    appliedFilterType = "geners"
                    generaSelected?.clear()
                    value = value.uppercase(Locale.getDefault())
                    val valuesString = value.split(",").toTypedArray()
                    for (s in valuesString) {
                        if (!generaSelected?.contains(s)!!) generaSelected?.add("$s-geners")
                    }
                    binding?.filterFab?.setImageResource(R.drawable.filter_selected)
                } else {
                    binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
                    generaSelected?.clear()
                }
            }
            val containAccessibility = type.contains("accessability")
            if (containAccessibility) {
                val index = type.indexOf("accessability")
                val value: String = filterItemSelected[type[index]]!!
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
        upcomingBooking = preferences.getIsLogin() == false
        if (!special.equals("All", ignoreCase = true))

            if (buttonPressed.isNotEmpty()) {
                lang = buttonPressed[0].split("-").toTypedArray()[0].trim { it <= ' ' }
                for (i in 1 until buttonPressed.size) lang =
                    lang + "," + buttonPressed[i].split("-").toTypedArray()[0].trim { it <= ' ' }
            }
        if (generaSelected!!.isNotEmpty()) {
            format = generaSelected!![0]!!.split("-").toTypedArray()[0].trim { it <= ' ' }
            for (i in 1 until generaSelected!!.size) format =
                format + "," + generaSelected!![i]!!.split("-").toTypedArray()[0].trim { it <= ' ' }
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
                oPenDialogQR()
            }

        } else {
            binding?.privilegeLoginUi?.hide()
            binding?.privilegeLogOutUi?.show()
        }

    }

    @SuppressLint("CutPasteId", "ClickableViewAccessibility")
    private fun initBanner(bannerModels: ArrayList<HomeResponse.Pu>) {
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
                rlBanner?.hide()
                listener?.onShowNotification()
                listener?.onShowPrivilege()
                if (bannerModels.size > 0 && bannerModels[counterStory].type.equals(
                        "image", ignoreCase = true
                    )
//                        .equalsIgnoreCase("image")
                ) {
                    if (bannerModels[counterStory].redirectView.equals(
                            "DEEPLINK", ignoreCase = true
                        )
                    ) {
                        if (bannerModels[counterStory].redirect_url.equals("", ignoreCase = true)) {
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
                    } else if (bannerModels[counterStory].redirect_url.equals(
                            "INAPP", ignoreCase = true
                        )
                    ) {
                        if (bannerModels[counterStory].redirect_url.equals("", ignoreCase = true)

                        ) {
//                            val intent = Intent(context, PrivacyActivity::class.java)
//                            intent.putExtra("url", bannerModels[counterStory].getRedirect_url())
//                            intent.putExtra(PCConstants.IS_FROM, 2000)
//                            intent.putExtra("title", bannerModels[counterStory].getName())
//                            startActivity(intent)
                        }
                    } else if (bannerModels[counterStory].redirect_url.equals(
                            "WEB", ignoreCase = true
                        )
//                            .equalsIgnoreCase("WEB")
                    ) {
                        if (bannerModels[counterStory].redirect_url.equals("", ignoreCase = true)
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
                rlBanner?.hide()
                listener?.onShowNotification()
                listener?.onShowPrivilege()
                if (bannerModels.size > 0 && bannerModels[counterStory].type.equals(
                        "video", ignoreCase = true
                    )
                ) {
                }
            }

        } else {
            rlBanner?.hide()
            listener?.onShowNotification()
            listener?.onShowPrivilege()
        }
    }

    private fun showButton(bannerModel: HomeResponse.Pu) {
        if (bannerModel.type.uppercase(Locale.getDefault()).equals("VIDEO")&& bannerModel.trailerUrl!=""){
            ivPlay?.show()
            tvButton?.hide()
        } else if (bannerModel.type.uppercase(Locale.getDefault()) == "IMAGE" && bannerModel.redirect_url != "") {
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

}