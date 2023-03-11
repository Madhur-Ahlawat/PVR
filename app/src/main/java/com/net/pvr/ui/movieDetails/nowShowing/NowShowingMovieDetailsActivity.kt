package com.net.pvr.ui.movieDetails.nowShowing

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.devs.readmoreoption.ReadMoreOption
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityNowShowingBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.bookingSession.MovieSessionActivity
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.home.HomeFragment
import com.net.pvr.ui.home.fragment.home.adapter.PromotionAdapter
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.movieDetails.nowShowing.adapter.*
import com.net.pvr.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr.ui.movieDetails.nowShowing.viewModel.MovieDetailsViewModel
import com.net.pvr.ui.player.PlayerActivity
import com.net.pvr.utils.Constant
import com.net.pvr.utils.NetworkResult
import com.net.pvr.utils.ga.GoogleAnalytics
import com.net.pvr.utils.hide
import com.net.pvr.utils.isevent.ISEvents
import com.net.pvr.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NowShowingMovieDetailsActivity : AppCompatActivity(), MusicVideoAdapter.RecycleViewItemClickListener,
    TrailerAdapter.RecycleViewItemClickListener, TrailerTrsAdapter.RecycleViewItemClickListener,
    MusicVideoTrsAdapter.RecycleViewItemClickListener {
    private var binding: ActivityNowShowingBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: MovieDetailsViewModel by viewModels()

    @Inject
    lateinit var preferences: PreferenceManager

    private var stringBuilder: StringBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNowShowingBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        movieDetails()

//        title
        binding?.include?.textView5?.text = resources.getString(R.string.book_now)

        authViewModel.movieDetails(
            preferences.getCityName(),
            intent.getStringExtra("mid").toString(),
            "",
            "",
            "",
            "",
            "no",
            "no"
        )
    }

    private fun movieDetails() {
        authViewModel.movieDetailsLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output)
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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: MovieDetailsResponse.Output) {
        //Design
        binding?.constraintLayout177?.show()
        ISEvents().movieDetail(this, HomeFragment.mcId)

        //Image
        Glide.with(this)
            .load(output.wit)
            .error(R.drawable.app_icon)
            .into(binding?.imageView26!!)

        //Trailer
        if (output.t.isEmpty()) {
            binding?.imageView29?.hide()
        } else {
            binding?.imageView29?.show()
        }

        binding?.imageView29?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Now Showing Details")
                bundle.putString("var_movie_trailer","")
                GoogleAnalytics.hitEvent(this, "movie_trailer", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            val intent = Intent(this@NowShowingMovieDetailsActivity, PlayerActivity::class.java)
            intent.putExtra("trailerUrl", output.t)
            startActivity(intent)
        }
        //Share
        binding?.imageView28?.setOnClickListener {
            Constant().shareData(this, "Check out this movie at PVR!",output.su)
        }
        //Back
        binding?.imageView27?.setOnClickListener {
            finish()
        }

        //Title
        binding?.textView55?.text = output.n

//        category
        stringBuilder = java.lang.StringBuilder()
        var data = ""
        for (k in 0 until output.icons.size) {
            data = if (output.icons[k].contains("D3", ignoreCase = true)) {
                "3D"
            } else if (output.icons[k].equals("DATMOS", ignoreCase = true)) {
                "ATMOS"
            } else if (output.icons[k].contains("IMAX", ignoreCase = true)) {
                "IMAX"
            } else if (output.icons[k].equals("D4", ignoreCase = true)) {
                "4Dx"
            } else if (output.icons[k].equals("D2", ignoreCase = true)) {
                "2D"
            } else {
                output.icons[k]
            }
            if (k == 0) {
                stringBuilder?.append(data)
            } else {
                stringBuilder?.append("  •  $data")
            }
        }

//        //language
//        stringBuilder = java.lang.StringBuilder()

        //Genre
        binding?.textView56?.text =
            output.genre + " " + getString(R.string.dots) + " " + stringBuilder
        //Censor
        binding?.textView58?.text =
            output.c.replace("[", "").replace("]", "").replace("(", "").replace(")", "")

        //Duration
//        binding?.textView59?.text = output.l
        binding?.textView60?.text = output.l

        //Release Data
        binding?.textView252?.text = output.rt
        //Avail in
        binding?.textView254?.text = output.lngs.joinToString { it }
        //Language
        val commaSeparatedString = output.lngs.joinToString { it }

        //Description
        val readMoreOption = ReadMoreOption.Builder(this).textLength(4, ReadMoreOption.TYPE_LINE)
            .moreLabel("read more").lessLabel("read less").moreLabelColor(Color.BLACK)
            .lessLabelColor(Color.BLACK).labelUnderLine(false).expandAnimation(true).build()

        binding?.textView66?.text = output.p
        if (binding?.textView66?.lineCount!! > 2) {
            Constant().makeTextViewResizable(binding?.textView66!!, 4, "Read More", true)
        }
//        binding?.textView66?.let { Constant().makeTextViewResizable(it, 4, "..read more", true,this) }
//        readMoreOption.addReadMoreTo(binding?.textView66, binding?.textView66?.text)

        //FilmType
        binding?.textView75?.text = output.tag
        //Language
        binding?.textView76?.text = output.lng
        //ColorInfo
        binding?.textView77?.text = output.sm
        //SoundMix
        binding?.textView79?.text = output.p
        //MovedNext
        binding?.include?.textView5?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Now Showing Details")
//                bundle.putString("var_book_now_screenname","HomePage")
                GoogleAnalytics.hitEvent(this, "movie_book_now", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            val intent = Intent(this@NowShowingMovieDetailsActivity, MovieSessionActivity::class.java)
            intent.putExtra("mid", output.id)
            intent.putExtra("cid", output.c)
            startActivity(intent)
        }
        //Place Holder
        if (output.ph.isEmpty()) {
            binding?.constraintLayout11?.recyclerPromotion?.hide()
        } else {
            binding?.constraintLayout11?.recyclerPromotion?.show()
        }
        if (output.mb != null && output.mb.name != null) {

            //Cast
            if (output.mb.cast.isNotEmpty()) {
                binding?.recyclerView4?.show()
                binding?.textView67?.show()
                val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val castAdapter = CastAdapter(output.mb.cast, this)
                binding?.recyclerView4?.layoutManager = layoutManager
                binding?.recyclerView4?.adapter = castAdapter

            } else {
                binding?.textView67?.hide()
                binding?.recyclerView4?.hide()
            }

            //Crew
            if (output.mb.crew.isNotEmpty()) {
                binding?.recyclerCrew?.show()
                binding?.textView68?.show()
                val layoutManagerCrew =
                    GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val crewAdapter = CrewAdapter(getUpdatedList(output.mb.crew), this)
                binding?.recyclerCrew?.layoutManager = layoutManagerCrew
                binding?.recyclerCrew?.adapter = crewAdapter
            } else {
                binding?.recyclerCrew?.hide()
                binding?.textView68?.hide()
            }
        }


        //condition check for Trailer and Music video
        val trailerList: ArrayList<MovieDetailsResponse.Trs> = ArrayList()
        val musicVideoList: ArrayList<MovieDetailsResponse.Trs> = ArrayList()

        if (output.trs != null){
            for (item in output.trs) {
                if (item.ty == "MUSIC") {
                    musicVideoList.addAll(output.trs)
                } else {
                    trailerList.addAll(output.trs)
                }
            }
        }

        //trailer
        if (trailerList.size != 0) {
            val layoutManagerTrailer =
                GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val trailerAdapter = TrailerTrsAdapter(trailerList, this, this)
            binding?.recyclerView5?.layoutManager = layoutManagerTrailer
            binding?.recyclerView5?.adapter = trailerAdapter
        } else {
            if (output.mb.videos.isNotEmpty()) {
                binding?.recyclerView5?.show()
                binding?.textView69?.show()
                val layoutManagerTrailer =
                    GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val trailerAdapter = TrailerAdapter(output.mb.videos, this, this)
                binding?.recyclerView5?.layoutManager = layoutManagerTrailer
                binding?.recyclerView5?.adapter = trailerAdapter
            } else {
                binding?.recyclerView5?.hide()
                binding?.textView69?.hide()
            }
        }

//        //MusicVideo
        if (musicVideoList.size != 0) {
            binding?.textView70?.show()
            val layoutManagerTrailer =
                GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val trailerAdapter = MusicVideoTrsAdapter(musicVideoList, this, this)
            binding?.recyclerMusic?.layoutManager = layoutManagerTrailer
            binding?.recyclerMusic?.adapter = trailerAdapter
        } else {
            if (output.mb != null && output.mb.name != null) {
                if (output.mb.tracks.isNotEmpty()) {
                    binding?.textView70?.show()
                    binding?.recyclerMusic?.show()
                    val layoutManagerTrailer =
                        GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                    val trailerAdapter = MusicVideoAdapter(output.mb.tracks[0].roles, this, this)
                    binding?.recyclerMusic?.layoutManager = layoutManagerTrailer
                    binding?.recyclerMusic?.adapter = trailerAdapter
                } else {
                    binding?.textView70?.hide()
                    binding?.recyclerMusic?.hide()
                }
            }
        }

        //Promotion
        if (output.ph.isNotEmpty()) updatePH(output.ph)
    }

    private fun getUpdatedList(crew: ArrayList<MovieDetailsResponse.Mb.Crew>): List<MovieDetailsResponse.Mb.Crew.Role> {
        val list = ArrayList<MovieDetailsResponse.Mb.Crew.Role>()
        val listRole = ArrayList<MovieDetailsResponse.Mb.Crew.Role>()
        val listSupport = ArrayList<MovieDetailsResponse.Mb.Crew.Role>()
        val newList = ArrayList<MovieDetailsResponse.Mb.Crew.Role>()

        for (data in crew){
            list.addAll(data.roles)
        }
        for (data in list){
            if (data.role == "Director") {
                listRole.add(data)
            }else{
                listSupport.add(data)
            }
        }

        newList.addAll(listRole)
        newList.addAll(listSupport)

        println("listSupport--->${newList.size}")

        return newList
    }

    private fun updatePH(phd: ArrayList<MovieDetailsResponse.Ph>) {
        if (phd != null && phd.size > 0) {
            binding?.constraintLayout11?.recyclerPromotion?.show()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            binding?.constraintLayout11?.recyclerPromotion?.layoutManager = layoutManager
            binding?.constraintLayout11?.recyclerPromotion?.onFlingListener = null
            snapHelper.attachToRecyclerView(binding?.constraintLayout11?.recyclerPromotion!!)
            binding?.constraintLayout11?.recyclerPromotion?.layoutManager = layoutManager
            val adapter = PromotionAdapter(this, phd as ArrayList<HomeResponse.Ph>)
            binding?.constraintLayout11?.recyclerPromotion?.adapter = adapter
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
                            binding?.constraintLayout11?.recyclerPromotion?.smoothScrollToPosition(
                                count
                            )
                            handler.postDelayed(this, speedScroll.toLong())
                        }
                    }
                }
                handler.postDelayed(runnable, speedScroll.toLong())
            }
        } else {
            binding?.constraintLayout11?.recyclerPromotion?.hide()
        }
    }

    override fun musicVideo(comingSoonItem: MovieDetailsResponse.Mb.Crew.Role) {
        val intent = Intent(this@NowShowingMovieDetailsActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.url)
        startActivity(intent)

    }

    override fun trailerClick(comingSoonItem: MovieDetailsResponse.Mb.Video) {
        val intent = Intent(this@NowShowingMovieDetailsActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.url)
        startActivity(intent)
    }

    override fun trailerTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {
        val intent = Intent(this@NowShowingMovieDetailsActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.u)
        startActivity(intent)
    }

    override fun musicVideoTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {
        val intent = Intent(this@NowShowingMovieDetailsActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.u)
        startActivity(intent)
    }

}