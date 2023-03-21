package com.net.pvr.ui.home.fragment.comingSoon

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
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityComingSoonBinding
import com.net.pvr.databinding.TrailersDialogBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.filter.GenericFilterComing
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.fragment.comingSoon.adapter.ComingSoonMovieAdapter
import com.net.pvr.ui.home.fragment.comingSoon.response.CommingSoonResponse
import com.net.pvr.ui.home.fragment.comingSoon.search.CinemaSearchActivity
import com.net.pvr.ui.home.fragment.comingSoon.viewModel.ComingSoonViewModel
import com.net.pvr.ui.home.fragment.home.HomeFragment
import com.net.pvr.ui.home.fragment.home.HomeFragment.Companion.dialogTrailer
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.movieDetails.comingSoonDetails.ComingSoonDetailsActivity
import com.net.pvr.ui.movieDetails.comingSoonDetails.adapter.ComDetailsHomePhAdapter
import com.net.pvr.ui.movieDetails.comingSoonDetails.setAlert.SetAlertActivity
import com.net.pvr.ui.movieDetails.nowShowing.adapter.MusicVideoTrsAdapter
import com.net.pvr.ui.movieDetails.nowShowing.adapter.TrailerTrsAdapter
import com.net.pvr.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr.ui.player.PlayerActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.ga.GoogleAnalytics
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ComingSoonFragment : Fragment(),
    ComingSoonMovieAdapter.VideoPlay,
    GenericFilterComing.onButtonSelected ,
    StoriesProgressView.StoriesListener,
    TrailerTrsAdapter.RecycleViewItemClickListener,
    MusicVideoTrsAdapter.RecycleViewItemClickListener
{
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

    companion object{
        var comingMovieData:ArrayList<CommingSoonResponse.Output.Movy> = ArrayList()
    }


    private var musicData: ArrayList<MovieDetailsResponse.Trs> =
        ArrayList<MovieDetailsResponse.Trs>()
    private var videoData: ArrayList<MovieDetailsResponse.Trs> =
        ArrayList<MovieDetailsResponse.Trs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ActivityComingSoonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HomeActivity.backToTop?.hide()
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
        RlBanner = (requireActivity().findViewById(R.id.RlBanner))
        stories = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.stories))

        if (stories == null) {
            stories?.destroy()
        }
        manageFunction()
    }

    private fun manageFunction() {
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
        if (genre == "NULL"){
            genre = "ALL"
        }
        if (language == "NULL"){
            language = "ALL"
        }
        authViewModel.comingSoon(preferences.getCityName(), genre, language, preferences.getUserId())

    }

    private fun movedNext() {
//        Search
        binding?.imageView167?.setOnClickListener {
            val intent = Intent(requireActivity(), CinemaSearchActivity::class.java)
            startActivity(intent)
        }

        //pull Down
        binding?.swipeComingSoon?.setOnRefreshListener{
            binding?.swipeComingSoon?.isRefreshing = false
            authViewModel.comingSoon(preferences.getCityName(), genre, language, preferences.getUserId())
        }

        //banner
        ivCross?.setOnClickListener {
            RlBanner?.hide()
        }
    }

    private fun comingSoonApi() {
        authViewModel.userResponseLiveData.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        binding?.recComSoonMovie?.show()
                        retrieveData(it.data.output)
                    } else {
                        binding?.recComSoonMovie?.hide()
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

    override fun onResume() {
        super.onResume()
        if (SetAlertActivity.alert){
            SetAlertActivity.alert = false
            authViewModel.comingSoon(preferences.getCityName(), genre, language, preferences.getUserId())
        }
    }

    private fun retrieveData(output: CommingSoonResponse.Output) {
        //shimmer
        binding?.constraintLayout148?.hide()
        //Ui
        binding?.constraintLayout147?.show()

        comingMovieData = ArrayList()

        comingMovieData.addAll(output.movies)

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

    override fun onDateClick(comingSoonItem: CommingSoonResponse.Output.Movy) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Coming Soon")
            bundle.putString("var_coming_soon_banner", "")
            GoogleAnalytics.hitEvent(requireActivity(), "coming_soon_movie_banner", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }

        val intent = Intent(requireActivity(), ComingSoonDetailsActivity::class.java)
        HomeFragment.mcId = comingSoonItem.movieId
        intent.putExtra("mid", comingSoonItem.masterMovieId)
        startActivity(intent)
    }

    override fun onTrailerClick(comingSoonItem: CommingSoonResponse.Output.Movy) {
        if (comingSoonItem.trs.isNotEmpty() && comingSoonItem.trs.size > 1) {
            trailerList(comingSoonItem)
        } else {
            val intent = Intent(requireActivity(), PlayerActivity::class.java)
            intent.putExtra("trailerUrl", comingSoonItem.videoUrl)
            startActivity(intent)
        }
    }

    override fun onApply(
        type: ArrayList<String>,
        filterItemSelected: HashMap<String, String>,
        isSelected: Boolean,
        name: String
    ) {
        println("buttonPressed--->$type---$filterItemSelected")

        if (type.size > 1) {
            try {
                binding?.filterFab?.setImageResource(R.drawable.filter_selected)
                appliedFilterItem = filterItemSelected
                binding?.appliedFilter?.visibility = View.VISIBLE
                val containLanguage = type.contains("language")
                if (containLanguage) {
                    val index = type.indexOf("language")
                    var value: String = filterItemSelected[type[index]]!!
                    if (!value.equals("", ignoreCase = true)) {
                        buttonPressed.clear()
                        value = value.uppercase(Locale.getDefault())
                        val valuesString = value.split(",").toTypedArray()
                        for (s in valuesString) {
                            if (!buttonPressed.contains(s)) buttonPressed.add(s.trim { it <= ' ' } + "-language")
                        }
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
            }catch (e:Exception){
                e.printStackTrace()
            }
        } else binding?.appliedFilter?.visibility = View.GONE
    }


    override fun onReset() {
         language="ALL"
         genre="ALL"
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
                        val intent = Intent(context, PlayerActivity::class.java)
                        intent.putExtra("trailerUrl",  bannerModels[counterStory].trailerUrl)
                        startActivity(intent)
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
        if (bannerModel.type.uppercase(Locale.getDefault()) == "VIDEO" && bannerModel.trailerUrl!=""){
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

    //Internet Check
    private fun broadcastIntent() {
        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }



    @SuppressLint("SetTextI18n")
    private fun trailerList(comingSoonItem: CommingSoonResponse.Output.Movy) {
        musicData.clear()
        videoData.clear()
        for (i in 0 until comingSoonItem.trs.size) {
            if (comingSoonItem.trs[i].ty.equals("MUSIC", ignoreCase = true)) {
                musicData.add(comingSoonItem.trs[i])
            } else {
                videoData.add(comingSoonItem.trs[i])
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
        bindingTrailer.titleLandingScreen.text = comingSoonItem.name

        val censor =
             java.lang.String.join(",", comingSoonItem.language)+" "+getString(R.string.dots)+" "+comingSoonItem.date_caption

        bindingTrailer.tvCensorLang.text =
            censor.replace("[", "").replace("]", "").replace("(", "").replace(")", "")


        bindingTrailer.subTitleLandingScreen.text =
            comingSoonItem.otherlanguages + " " + getString(R.string.dots) + " " + java.lang.String.join(
                ",",
                comingSoonItem.othergenres
            )

        //image
        Glide.with(requireActivity())
            .load(comingSoonItem.miv)
            .error(R.drawable.placeholder_vertical)
            .into(bindingTrailer.imageLandingScreen)

        //dialog Dismiss
        bindingTrailer.include49.imageView58.setOnClickListener {
            dialogTrailer?.dismiss()
        }

        //title
        bindingTrailer.include49.textView108.text = getString(R.string.trailer_amp_music)

        //button
        bindingTrailer.include50.textView5.text = getString(R.string.set_alert)

        bindingTrailer.include50.textView5.setOnClickListener {
            val intent = Intent(requireContext(), SetAlertActivity::class.java)
            intent.putExtra("cid",comingSoonItem.masterMovieId)
            startActivity(intent)
        }

//trailer
        if (videoData.size>0){
            bindingTrailer.textView69.show()
        }else{
            bindingTrailer.textView69.hide()
        }
        val gridLayoutManager1 =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        val trailerAdapter = TrailerTrsAdapter(videoData, requireContext(), this)
        bindingTrailer.recyclerView5.layoutManager = gridLayoutManager1
        bindingTrailer.recyclerView5.adapter = trailerAdapter

//music
        if (musicData.size>0){
            bindingTrailer.textView70.show()
        }else{
            bindingTrailer.textView70.hide()
        }
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        val musicVideoTrsAdapter = MusicVideoTrsAdapter(musicData, requireContext(), this)
        bindingTrailer.recyclerMusic.layoutManager = gridLayoutManager
        bindingTrailer.recyclerMusic.adapter = musicVideoTrsAdapter

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

        showCount = if (languages.isEmpty() && genres.isEmpty()){
            0
        }else {
            filterMovies(
                comingMovieData,
                languages,
                genres,
                spShows
            )
        }
        println("showCount--->$showCount---${ComingSoonFragment.comingMovieData.size}")
        return showCount
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun filterMovies(
        movies:ArrayList<CommingSoonResponse.Output.Movy>,
        languages: List<String?>,
        genres: List<String?>,
        spShows: List<String?>): Int {
        val filteredMovies: ArrayList<CommingSoonResponse.Output.Movy> = ArrayList()
        println("languages-->$languages---$genres--->${filteredMovies.size}")
        for (movie in movies) {

            if (!languages.contains("ALL") && languages.isNotEmpty()) {

                val languagesM = movie.otherlanguages.split(",").toList()

                if (!languagesM.stream().anyMatch { m: String -> languages.contains(m.uppercase()) })
                    continue
            }
            if (!genres.contains("ALL") && genres.isNotEmpty()) {
                val genresM: List<String> = movie.othergenres.split(",").toList()
                if (!genresM.stream().anyMatch { m: String -> genres.contains(m.uppercase()) })
                    continue
            }

            filteredMovies.add(movie)
        }
        return filteredMovies.size
    }


}