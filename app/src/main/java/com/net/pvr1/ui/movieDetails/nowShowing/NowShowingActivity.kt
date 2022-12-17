package com.net.pvr1.ui.movieDetails.nowShowing

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.devs.readmoreoption.ReadMoreOption
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityNowShowingBinding
import com.net.pvr1.ui.bookingSession.BookingActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.movieDetails.nowShowing.adapter.*
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr1.ui.movieDetails.nowShowing.viewModel.MovieDetailsViewModel
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NowShowingActivity : AppCompatActivity(), CastAdapter.RecycleViewItemClickListener,
    CrewAdapter.RecycleViewItemClickListener, MusicVideoAdapter.RecycleViewItemClickListener,
    TrailerAdapter.RecycleViewItemClickListener, TrailerTrsAdapter.RecycleViewItemClickListener,
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
            "Delhi-NCR", intent.getStringExtra("mid").toString(), "", "", "", "", "no", "no"
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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: MovieDetailsResponse.Output) {


        //Image
        Glide.with(this).load(output.wit).error(R.drawable.app_icon).into(binding?.imageView26!!)
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
            Constant().shareData(this,"","")
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
        if (output.mb != null && output.mb.name != null) {
            //Cast
            if (output.mb.cast.isNotEmpty()) {
                binding?.recyclerView4?.show()
                binding?.textView67?.show()
                val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val castAdapter = CastAdapter(output.mb.cast, this, this)
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
                val crewAdapter = CrewAdapter(output.mb.crew, this, this)
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

        for (item in output.trs) {
            if (item.ty == "MUSIC") {
                musicVideoList.addAll(output.trs)
            } else {
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
    }

    override fun castClick(comingSoonItem: MovieDetailsResponse.Mb.Cast) {

    }

    override fun crewClick(comingSoonItem: MovieDetailsResponse.Mb.Crew) {

    }

    override fun musicVideo(comingSoonItem: MovieDetailsResponse.Mb.Crew.Role) {
        val intent = Intent(this@NowShowingActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.url)
        startActivity(intent)

    }

    override fun trailerClick(comingSoonItem: MovieDetailsResponse.Mb.Video) {
        val intent = Intent(this@NowShowingActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.url)
        startActivity(intent)
    }

    override fun trailerTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {
        val intent = Intent(this@NowShowingActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.t)
        startActivity(intent)
    }

    override fun musicVideoTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {
        val intent = Intent(this@NowShowingActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.t)
        startActivity(intent)
    }

}