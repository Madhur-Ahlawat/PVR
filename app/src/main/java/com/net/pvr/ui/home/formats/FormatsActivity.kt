package com.net.pvr.ui.home.formats

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.net.pvr.R
import com.net.pvr.databinding.ActivityFormatsBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.bookingSession.MovieSessionActivity
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.formats.adapter.FormatCategoryAdapter
import com.net.pvr.ui.home.formats.response.FormatResponse
import com.net.pvr.ui.home.formats.viewModel.FormatsViewModel
import com.net.pvr.ui.home.fragment.home.adapter.HomeMoviesAdapter
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.movieDetails.nowShowing.NowShowingMovieDetailsActivity
import com.net.pvr.ui.player.PlayerActivity
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FormatsActivity : AppCompatActivity() , FormatCategoryAdapter.RecycleViewItemClickListener,
    HomeMoviesAdapter.RecycleViewItemClickListener{
    private var binding: ActivityFormatsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: FormatsViewModel by viewModels()
    @Inject
    lateinit var preferences: PreferenceManager

    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormatsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunctions()
    }

    private fun manageFunctions() {
        //title
        binding?.toolbar?.textView108?.text=intent.getStringExtra("format").toString()
        //back
        binding?.toolbar?.imageView58?.setOnClickListener {
            finish()
        }
        authViewModel.formats(intent.getStringExtra("format").toString(), preferences.getCityName(), "no")
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
        val intent = Intent(this@FormatsActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", string)
        startActivity(intent)
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
}