package com.net.pvr.ui.search.searchHome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivitySearchHomeBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.cinemaSession.CinemaSessionActivity
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.movieDetails.nowShowing.NowShowingMovieDetailsActivity
import com.net.pvr.ui.search.searchHome.adapter.SearchHomeCinemaAdapter
import com.net.pvr.ui.search.searchHome.adapter.SearchHomeMovieAdapter
import com.net.pvr.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr.ui.search.searchHome.viewModel.HomeSearchViewModel
import com.net.pvr.utils.Constant
import com.net.pvr.utils.NetworkResult
import com.net.pvr.utils.ga.GoogleAnalytics
import com.net.pvr.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@Suppress("NAME_SHADOWING")
@AndroidEntryPoint
class SearchHomeActivity : AppCompatActivity(),
    SearchHomeMovieAdapter.RecycleViewItemClickListenerCity,
    SearchHomeCinemaAdapter.RecycleViewItemClickListenerCity {
    private var binding: ActivitySearchHomeBinding? = null
    private val authViewModel: HomeSearchViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var cinemaClick: Boolean = false

    @Inject
    lateinit var preferences: PreferenceManager
    private var filterMovieList: ArrayList<HomeSearchResponse.Output.M>? = null
    private var filterCinemaList: ArrayList<HomeSearchResponse.Output.T>? = null

    private var searchHomeCinemaAdapter : SearchHomeCinemaAdapter? = null
    private var searchHomeMovieAdapter : SearchHomeMovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunctions()
    }

    private fun manageFunctions() {
        authViewModel.homeSearch(preferences.getCityName(), "", "", preferences.getLatitudeData(), preferences.getLongitudeData())
        movedNext()
        search()
    }

    private fun movedNext() {
//        Voice Search
        binding?.include42?.voiceBtn?.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
            try {
                resultLauncher.launch(intent)
            } catch (e: Exception) {
                toast(e.message)
            }
        }

//        Cancel button
        binding?.include42?.cancelBtn?.setOnClickListener {
            finish()
        }

//        search Text
        binding?.include42?.editTextTextPersonName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (cinemaClick){
                    filterCinema(s.toString())
                }else{
                    filterMovie(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    //Voice search
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    val result: ArrayList<String>? = data?.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                    )
                    if (cinemaClick){
                        filterMovie( Objects.requireNonNull(result)!![0])
                    }else{
                        filterCinema( Objects.requireNonNull(result)!![0])
                    }

                    binding?.include42?.editTextTextPersonName?.setText(
                        Objects.requireNonNull(result)!![0]
                    )
                }
            }
        }

    private fun search() {
        authViewModel.homeSearchLiveData.observe(this) {
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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: HomeSearchResponse.Output) {
//      Movie
        filterMovieList=output.m
//        theater
        filterCinemaList=output.t

        if (cinemaClick) {
            binding?.moviesBtn?.setTextColor(getColor(R.color.textColorGray))
            binding?.cinema?.setTextColor(getColor(R.color.black))
            binding?.viewCinema?.setBackgroundResource(R.color.yellow)
            binding?.viewMovie?.setBackgroundResource(R.color.grayEd)
            binding?.textView302?.text = getString(R.string.cinema_near_your)

            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            searchHomeCinemaAdapter = SearchHomeCinemaAdapter(output.t, this, this)
            binding?.movieRecycler?.layoutManager = gridLayout2
            binding?.movieRecycler?.adapter = searchHomeCinemaAdapter

        } else {
            binding?.cinema?.setTextColor(getColor(R.color.textColorGray))
            binding?.viewCinema?.setBackgroundResource(R.color.grayEd)
            binding?.viewMovie?.setBackgroundResource(R.color.yellow)
            binding?.textView302?.text = getString(R.string.trending_movies)

            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            searchHomeMovieAdapter= SearchHomeMovieAdapter(output.m, this, this)
            binding?.movieRecycler?.layoutManager = gridLayout2
            binding?.movieRecycler?.adapter = searchHomeMovieAdapter
        }

        //Cinema
        binding?.linearLayout8?.setOnClickListener {
            cinemaClick = true

            binding?.include42?.editTextTextPersonName?.text?.clear()
            binding?.moviesBtn?.setTextColor(getColor(R.color.textColorGray))
            binding?.cinema?.setTextColor(getColor(R.color.black))
            binding?.viewCinema?.setBackgroundResource(R.color.yellow)
            binding?.viewMovie?.setBackgroundResource(R.color.grayEd)
            binding?.textView302?.text = getString(R.string.cinema_near_your)

            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            searchHomeCinemaAdapter = SearchHomeCinemaAdapter(
                output.t,
                this,
                this
            )
            binding?.movieRecycler?.layoutManager = gridLayout2
            binding?.movieRecycler?.adapter = searchHomeCinemaAdapter

        }

        //Movie
        binding?.linearLayout7?.setOnClickListener {
            cinemaClick = false
            binding?.include42?.editTextTextPersonName?.text?.clear()
            binding?.moviesBtn?.setTextColor(getColor(R.color.black))
            binding?.cinema?.setTextColor(getColor(R.color.textColorGray))
            binding?.viewCinema?.setBackgroundResource(R.color.grayEd)
            binding?.viewMovie?.setBackgroundResource(R.color.yellow)
            binding?.textView302?.text = getString(R.string.trending_movies)

            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            searchHomeMovieAdapter = SearchHomeMovieAdapter(output.m, this, this)
            binding?.movieRecycler?.layoutManager = gridLayout2
            binding?.movieRecycler?.adapter = searchHomeMovieAdapter
        }

    }

    override fun onSearchCinema(selectCityItemList: HomeSearchResponse.Output.T) {
        val intent = Intent(this, CinemaSessionActivity::class.java)
        intent.putExtra("cid", selectCityItemList.id)
        intent.putExtra("lat", selectCityItemList.lat)
        intent.putExtra("lang", selectCityItemList.lng)
        startActivity(intent)

    }

    override fun onSearchCinemaDirection(selectCityItemList: HomeSearchResponse.Output.T) {
        Constant().shareData(this,selectCityItemList.lat,selectCityItemList.lng)
    }

    override fun onSearchMovie(selectCityList: HomeSearchResponse.Output.M) {
        val intent = Intent(this, NowShowingMovieDetailsActivity::class.java)
        intent.putExtra("mid", selectCityList.id)
        startActivity(intent)
    }

    private fun filterMovie(text: String) {
        val filtered: ArrayList<HomeSearchResponse.Output.M> = ArrayList()
        val filtered1: ArrayList<HomeSearchResponse.Output.M> = ArrayList()
        for (item in filterMovieList!!) {
            if (item.n.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filtered.add(item)
            }
        }
        if (filtered.isEmpty()) {
            searchHomeMovieAdapter?.filterMovieList(filtered1)
        } else {
            searchHomeMovieAdapter?.filterMovieList(filtered)
        }
    }

    private fun filterCinema(text: String) {
        val filtered: ArrayList<HomeSearchResponse.Output.T> = ArrayList()
        val filtered1: ArrayList<HomeSearchResponse.Output.T> = ArrayList()
        for (item in filterCinemaList!!) {
            if (item.n.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filtered.add(item)
            }
        }
        if (filtered.isEmpty()) {

            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home Screen")
                bundle.putString("header_movie_name", filtered1.toString())
                GoogleAnalytics.hitEvent(this, "header_search_bar_click", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            searchHomeCinemaAdapter?.filterCinemaList(filtered1)
        } else {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home Screen")
                bundle.putString("header_cinema_name", filtered1.toString())
                GoogleAnalytics.hitEvent(this, "header_search_bar_click", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }
            searchHomeCinemaAdapter?.filterCinemaList(filtered)
        }
    }
}