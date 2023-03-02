package com.net.pvr.ui.home.fragment.comingSoon.search

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
import com.net.pvr.databinding.ActivityCinemaSearchBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.movieDetails.comingSoonDetails.ComingSoonDetailsActivity
import com.net.pvr.ui.movieDetails.nowShowing.NowShowingMovieDetailsActivity
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


@AndroidEntryPoint
class CinemaSearchActivity : AppCompatActivity(),
    SearchHomeMovieAdapter.RecycleViewItemClickListenerCity {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityCinemaSearchBinding? = null
    private val authViewModel: HomeSearchViewModel by viewModels()
    private var loader: LoaderDialog? = null

    private var filterCinemaList: ArrayList<HomeSearchResponse.Output.M>? = null
    private var searchHomeMovieAdapter: SearchHomeMovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSearchBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunction()
    }

    private fun manageFunction() {
        authViewModel.homeSearch(preferences.getCityName(), "", "", preferences.getLatitudeData(), preferences.getLongitudeData())

        search()
        movedNext()
    }

    private fun movedNext() {
//        Voice Search
        binding?.include42?.editTextTextPersonName?.hint = "Search a movie"
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
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

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

//        SetData  Filter
        output.m = getComingMovie(output.m)
        filterCinemaList= output.m

//        SetData
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        searchHomeMovieAdapter = SearchHomeMovieAdapter(output.m, this, this)
        binding?.recyclerCinemaSearch?.layoutManager = gridLayout2
        binding?.recyclerCinemaSearch?.adapter = searchHomeMovieAdapter
    }

    private fun getComingMovie(m: ArrayList<HomeSearchResponse.Output.M>): ArrayList<HomeSearchResponse.Output.M> {
        val coming = ArrayList<HomeSearchResponse.Output.M>()
        for ( data in m){
            if (data.t == "C"){
                coming.add(data)
            }
        }
        return coming
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
                    filter( Objects.requireNonNull(result)!![0])
                    binding?.include42?.editTextTextPersonName?.setText(
                        Objects.requireNonNull(result)!![0]
                    )
                }
            }
        }

    override fun onSearchMovie(selectCityList: HomeSearchResponse.Output.M) {
        val intent = Intent(this, ComingSoonDetailsActivity::class.java)
        intent.putExtra("mid", selectCityList.id)
        startActivity(intent)
    }


    private fun filter(text: String) {
        val filtered: ArrayList<HomeSearchResponse.Output.M> = ArrayList()
        val filtered1: ArrayList<HomeSearchResponse.Output.M> = ArrayList()
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
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Coming Soon Search")
                bundle.putString("var_coming_soon_search_movie", "")
                GoogleAnalytics.hitEvent(this, "coming_soon_search_movie", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

// Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Coming Soon Search")
//                bundle.putString("var_coming_soon_search_movie", "")
                GoogleAnalytics.hitEvent(this, "coming_soon_search_movie_keywords", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            searchHomeMovieAdapter?.filterMovieList(filtered1)
        } else {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Coming Soon Search")
                bundle.putString("var_coming_soon_search_movie", "")
                GoogleAnalytics.hitEvent(this, "coming_soon_search_movie", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Coming Soon Search")
//                bundle.putString("var_coming_soon_search_movie", "")
                GoogleAnalytics.hitEvent(this, "coming_soon_search_movie_keywords", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            searchHomeMovieAdapter?.filterMovieList(filtered)
        }
    }

}