package com.net.pvr1.ui.movieDetails.nowShowing

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.net.pvr1.BuildConfig
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityNowShowingBinding
import com.net.pvr1.ui.bookingSession.BookingActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.movieDetails.nowShowing.adapter.*
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr1.ui.movieDetails.nowShowing.viewModel.MovieDetailsViewModel
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NowShowingActivity : AppCompatActivity(),
    CastAdapter.RecycleViewItemClickListener,
    CrewAdapter.RecycleViewItemClickListener,
    MusicVideoAdapter.RecycleViewItemClickListener,
    TrailerAdapter.RecycleViewItemClickListener,
    TrailerTrsAdapter.RecycleViewItemClickListener,
    MusicVideoTrsAdapter.RecycleViewItemClickListener {
    private var binding: ActivityNowShowingBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: MovieDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNowShowingBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        movieDetails()
        binding?.include?.textView5?.text = resources.getString(R.string.book_now)

        authViewModel.movieDetails(
            "Delhi-NCR",
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
//                        trailerlist = it.data.output.mb.trailers
                        
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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: MovieDetailsResponse.Output) {


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
            val intent = Intent(this@NowShowingActivity, PlayerActivity::class.java)
            intent.putExtra("trailerUrl", output.t)
            startActivity(intent)
        }
        //Share
        binding?.imageView28?.setOnClickListener {
            val appPackageName = BuildConfig.APPLICATION_ID
            val appName = getString(R.string.app_name)
            val shareBodyText = "https://play.google.com/store/apps/details?id=$appPackageName"

            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, appName)
                putExtra(Intent.EXTRA_TEXT, shareBodyText)
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }
        //Back
        binding?.imageView27?.setOnClickListener {
            finish()
        }
        //Title
        binding?.textView55?.text = output.n
        //Genre
        binding?.textView56?.text = output.genre
        //Censor
        binding?.textView58?.text = output.c

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
        binding?.textView66?.text = output.p
        binding?.textView66?.let { Constant().makeTextViewResizable(it, 4, "..read more", true) }
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
            val intent = Intent(this@NowShowingActivity, BookingActivity::class.java)
            intent.putExtra("mid", output.id)
            intent.putExtra("cid", output.c)
            startActivity(intent)
        }
        //Place Holder
        if (output.ph.isEmpty()) {
            binding?.constraintLayout11?.hide()
        } else {
            binding?.constraintLayout11?.show()
        }
        //Cast
        if (output.mb != null && output.mb.name != null) {

            val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val castAdapter = CastAdapter(output.mb.cast, this, this)
            binding?.recyclerView4?.layoutManager = layoutManager
            binding?.recyclerView4?.adapter = castAdapter

            //Crew
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val crewAdapter = CrewAdapter(output.mb.crew, this, this)
            binding?.recyclerCrew?.layoutManager = layoutManagerCrew
            binding?.recyclerCrew?.adapter = crewAdapter
        }



        //condition check for Trailer and Music video
        val trailerList:ArrayList<MovieDetailsResponse.Trs> = ArrayList()
        val musicVideoList:ArrayList<MovieDetailsResponse.Trs> = ArrayList()

        for (item in output.trs){
            if (item.ty == "MUSIC"){
                musicVideoList.addAll(output.trs)
            }else{
                trailerList.addAll(output.trs)
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
            val layoutManagerTrailer =
                GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val trailerAdapter = TrailerAdapter(output.mb.videos, this, this)
            binding?.recyclerView5?.layoutManager = layoutManagerTrailer
            binding?.recyclerView5?.adapter = trailerAdapter
        }

//        //MusicVideo
        if (musicVideoList.size != 0) {
            val layoutManagerTrailer = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val trailerAdapter = MusicVideoTrsAdapter(musicVideoList, this, this)
            binding?.recyclerMusic?.layoutManager = layoutManagerTrailer
            binding?.recyclerMusic?.adapter = trailerAdapter
        } else {
            if (output.mb != null && output.mb.name != null) {
                if (output.mb.tracks.isNotEmpty()) {
                    val layoutManagerTrailer =
                        GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                    val trailerAdapter = MusicVideoAdapter(output.mb.tracks[0].roles, this, this)
                    binding?.recyclerMusic?.layoutManager = layoutManagerTrailer
                    binding?.recyclerMusic?.adapter = trailerAdapter
                }
            }

        }
    }

    override fun castClick(comingSoonItem: MovieDetailsResponse.Mb.Cast) {

    }

    override fun crewClick(comingSoonItem: MovieDetailsResponse.Mb.Crew) {

    }

    override fun musicVideo(comingSoonItem: MovieDetailsResponse.Mb.Crew.Role) {
    }

    override fun trailerClick(comingSoonItem: MovieDetailsResponse.Mb.Video) {

    }

    override fun trailerTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {

    }

    override fun musicVideoTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {

    }

}