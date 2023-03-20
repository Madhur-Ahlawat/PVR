package com.net.pvr.ui.home.formats

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ActivityFormatsBinding
import com.net.pvr.databinding.TrailersDialogBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.bookingSession.MovieSessionActivity
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.formats.adapter.FormatCategoryAdapter
import com.net.pvr.ui.home.formats.response.FormatResponse
import com.net.pvr.ui.home.formats.viewModel.FormatsViewModel
import com.net.pvr.ui.home.fragment.home.HomeFragment
import com.net.pvr.ui.home.fragment.home.adapter.HomeMoviesAdapter
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.movieDetails.nowShowing.NowShowingMovieDetailsActivity
import com.net.pvr.ui.movieDetails.nowShowing.adapter.MusicVideoTrsAdapter
import com.net.pvr.ui.movieDetails.nowShowing.adapter.TrailerTrsAdapter
import com.net.pvr.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr.ui.player.PlayerActivity
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.isevent.ISEvents
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class FormatsActivity : AppCompatActivity() , FormatCategoryAdapter.RecycleViewItemClickListener,
    HomeMoviesAdapter.RecycleViewItemClickListener,TrailerTrsAdapter.RecycleViewItemClickListener,MusicVideoTrsAdapter.RecycleViewItemClickListener{
    private var binding: ActivityFormatsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: FormatsViewModel by viewModels()
    @Inject
    lateinit var preferences: PreferenceManager
    private var format = ""
    private var musicData: ArrayList<MovieDetailsResponse.Trs> =
        ArrayList<MovieDetailsResponse.Trs>()
    private var videoData: ArrayList<MovieDetailsResponse.Trs> =
        ArrayList<MovieDetailsResponse.Trs>()

    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormatsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        // dx_app_home onyx_app_home imax_app_home gold_app_home playhouse_app_home
        //  format=


        // IMAX 4DX PLAYHOUSE GOLD ONYX PXL
        val intent = intent
        val action = intent.action
        val data = intent.data

        //    [PLAYHOUSE, GOLD, PXL, ONYX, 4DX, IMAX]

        //    [PLAYHOUSE, GOLD, PXL, ONYX, 4DX, IMAX]
        if (data != null) {
            val path = data.path
            val parts = path!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            format = parts[3]
        }

        manageFunctions()
    }

    private fun manageFunctions() {
        format = intent.getStringExtra("format").toString()
        //title
        binding?.toolbar?.textView108?.text= format
        //back
        binding?.toolbar?.imageView58?.setOnClickListener {
            finish()
        }
        authViewModel.formats(format, preferences.getCityName(), "no")
        formats()
        getShimmerData()

        //internet Check
        broadcastReceiver = NetworkReceiver()
        broadcastIntent()
    }



    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    private fun formats() {
        authViewModel.liveDataScope.observe(this) {
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
                            positiveClick = {
                                finish()
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: FormatResponse.Output) {
        //layout
        binding?.constraintLayout169?.show()
        //shimmer
        binding?.constraintLayout145?.hide()

        //category
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding?.recyclerView39)
        val gridLayout =
            GridLayoutManager(this@FormatsActivity, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerView39?.layoutManager = LinearLayoutManager(this@FormatsActivity)
        val adapter = FormatCategoryAdapter(output.ph, this, this)
        binding?.recyclerView39?.layoutManager = gridLayout
        binding?.recyclerView39?.adapter = adapter

        //movie
        var size = 0
        var single = false
        size = if ((output.cinemas.m.size % 2) == 0) {
            //Is even
            single = false
            output.cinemas.m.size
        } else {
            //Is odd
            single = true
            output.cinemas.m.size - 1
        }

        val gridLayoutMovies = GridLayoutManager(this, 2)
        binding?.recyclerView40?.layoutManager = LinearLayoutManager(this)
        gridLayoutMovies.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == size) {
                    2
                } else {

                    1
                }
                return 1
            }
        }
        binding?.recyclerView40?.layoutManager = LinearLayoutManager(this@FormatsActivity)
        val adapter2 = HomeMoviesAdapter(this, output.cinemas.m, this,single)
        binding?.recyclerView40?.layoutManager = gridLayoutMovies
        binding?.recyclerView40?.adapter = adapter2
    }

    override fun categoryClick(comingSoonItem: FormatResponse.Output.Ph) {
        if (comingSoonItem.redirect_url!="") {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("title", comingSoonItem.name)
            intent.putExtra("from", "format")
            intent.putExtra("getUrl", comingSoonItem.redirect_url.toString())
            startActivity(intent)
        }
    }

    override fun onTrailerClick(string: String, trs: HomeResponse.Mv) {
        if (trs.trs.isNotEmpty() && trs.trs.size > 1) {
            trailerList(trs)
        } else {
            val intent = Intent(this@FormatsActivity, PlayerActivity::class.java)
            intent.putExtra("trailerUrl", string)
            startActivity(intent)
        }
//        val intent = Intent(this@FormatsActivity, PlayerActivity::class.java)
//        intent.putExtra("trailerUrl", string)
//        startActivity(intent)
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

        HomeFragment.dialogTrailer = Dialog(this)
        HomeFragment.dialogTrailer?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        HomeFragment.dialogTrailer?.setCancelable(true)
        val inflater = LayoutInflater.from(this)
        val bindingTrailer = TrailersDialogBinding.inflate(inflater)
        HomeFragment.dialogTrailer?.setContentView(bindingTrailer.root)
        HomeFragment.dialogTrailer?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        HomeFragment.dialogTrailer?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        HomeFragment.dialogTrailer?.window?.setGravity(Gravity.CENTER)
        HomeFragment.dialogTrailer?.show()



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
        Glide.with(this).load(mv.miv).error(R.drawable.placeholder_vertical)
            .into(bindingTrailer.imageLandingScreen)

        //dialog Dismiss
        bindingTrailer.include49.imageView58.setOnClickListener {
            HomeFragment.dialogTrailer?.dismiss()
        }

        //title
        bindingTrailer.include49.textView108.text = getString(R.string.trailer_amp_music)

        //button
        bindingTrailer.include50.textView5.text = getString(R.string.book_now)

        bindingTrailer.include50.textView5.setOnClickListener {
            val intent = Intent(this, MovieSessionActivity::class.java)
            intent.putExtra("mid", mv.id)
            ISEvents().bookBtn(this, mv.mcc)
            HomeFragment.mcId = mv.mcc
            startActivity(intent)
        }
        if (videoData.size > 0) {
            bindingTrailer.textView69.show()
        } else {
            bindingTrailer.textView69.hide()
        }

//trailer
        val gridLayoutManager1 =
            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val trailerAdapter = TrailerTrsAdapter(videoData, this, this)
        bindingTrailer.recyclerView5.layoutManager = gridLayoutManager1
        bindingTrailer.recyclerView5.adapter = trailerAdapter

//music
        if (musicData.size > 0) {
            bindingTrailer.textView70.show()
            bindingTrailer.recyclerMusic.show()
            val gridLayoutManager =
                GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val musicVideoTrsAdapter = MusicVideoTrsAdapter(musicData, this, this)
            bindingTrailer.recyclerMusic.layoutManager = gridLayoutManager
            bindingTrailer.recyclerMusic.adapter = musicVideoTrsAdapter
        } else {
            bindingTrailer.textView70.hide()
            bindingTrailer.recyclerMusic.hide()
        }
    }

    override fun onMoviesClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(this@FormatsActivity, NowShowingMovieDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun onBookClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(this@FormatsActivity, MovieSessionActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }


    //Internet Check
    private fun broadcastIntent() {
        registerReceiver(
            broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun trailerTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.u)
        startActivity(intent)
    }

    override fun musicVideoTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.u)
        startActivity(intent)
    }
}