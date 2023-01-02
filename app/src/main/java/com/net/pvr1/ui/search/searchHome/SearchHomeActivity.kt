package com.net.pvr1.ui.search.searchHome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySearchHomeBinding
import com.net.pvr1.ui.cinemaSession.CinemaSessionActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.movieDetails.nowShowing.NowShowingActivity
import com.net.pvr1.ui.search.searchHome.adapter.SearchHomeCinemaAdapter
import com.net.pvr1.ui.search.searchHome.adapter.SearchHomeMovieAdapter
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr1.ui.search.searchHome.viewModel.HomeSearchViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@Suppress("NAME_SHADOWING")
@AndroidEntryPoint
class SearchHomeActivity : AppCompatActivity(),
    SearchHomeMovieAdapter.RecycleViewItemClickListenerCity,
    SearchHomeCinemaAdapter.RecycleViewItemClickListenerCity {
    private var binding: ActivitySearchHomeBinding? = null
    private val authViewModel: HomeSearchViewModel by viewModels()
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private var loader: LoaderDialog? = null

    private var cinemaClick: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        authViewModel.homeSearch("Delhi-NCR", "", "", "77.04", "28.56")


        movedNext()
        search()
    }

    private fun movedNext() {
        //On Back
        binding?.imageView23?.setOnClickListener {
            onBackPressed()
        }
        //Voice Search
        binding?.voiceBtn?.setOnClickListener {
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
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast
                    .makeText(
                        this@SearchHomeActivity, " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }


        binding?.editText?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //do here your stuff f
                val text = binding?.editText?.text.toString()
                authViewModel.homeSearch("Delhi-NCR", "", text, "77.04", "28.56")

                true
            } else false
        })
    }

    @Deprecated("Deprecated in Java")
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {

                if (resultCode == RESULT_OK) {
                    val result: ArrayList<String>? = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                    )
                    binding?.editText?.setText(
                        Objects.requireNonNull(result)!![0]
                    )
                }
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

        if (cinemaClick == true) {
            binding?.moviesBtn?.setTextColor(getColor(R.color.textColorGray))
            binding?.cinema?.setTextColor(getColor(R.color.black))
            binding?.viewCinema?.setBackgroundResource(R.color.yellow)
            binding?.viewMovie?.setBackgroundResource(R.color.grayEd)
            binding?.textView302?.text = getString(R.string.cinema_near_your)

            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val otherCityAdapter = SearchHomeCinemaAdapter(
                output.t as ArrayList<HomeSearchResponse.Output.T>,
                this,
                this
            )
            binding?.movieRecycler?.layoutManager = gridLayout2
            binding?.movieRecycler?.adapter = otherCityAdapter

        } else {
            binding?.cinema?.setTextColor(getColor(R.color.textColorGray))
            binding?.viewCinema?.setBackgroundResource(R.color.grayEd)
            binding?.viewMovie?.setBackgroundResource(R.color.yellow)
            binding?.textView302?.text = getString(R.string.trending_movies)

            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val otherCityAdapter = SearchHomeMovieAdapter(output.m, this, this)
            binding?.movieRecycler?.layoutManager = gridLayout2
            binding?.movieRecycler?.adapter = otherCityAdapter
        }

        //Cinema
        binding?.linearLayout8?.setOnClickListener {
            cinemaClick = true
            binding?.moviesBtn?.setTextColor(getColor(R.color.textColorGray))
            binding?.cinema?.setTextColor(getColor(R.color.black))
            binding?.viewCinema?.setBackgroundResource(R.color.yellow)
            binding?.viewMovie?.setBackgroundResource(R.color.grayEd)
            binding?.textView302?.text = getString(R.string.cinema_near_your)

            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val otherCityAdapter = SearchHomeCinemaAdapter(
                output.t as ArrayList<HomeSearchResponse.Output.T>,
                this,
                this
            )
            binding?.movieRecycler?.layoutManager = gridLayout2
            binding?.movieRecycler?.adapter = otherCityAdapter

        }

        //Movie
        binding?.linearLayout7?.setOnClickListener {
            binding?.moviesBtn?.setTextColor(getColor(R.color.black))
            binding?.cinema?.setTextColor(getColor(R.color.textColorGray))
            binding?.viewCinema?.setBackgroundResource(R.color.grayEd)
            binding?.viewMovie?.setBackgroundResource(R.color.yellow)
            binding?.textView302?.text = getString(R.string.trending_movies)


            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val otherCityAdapter = SearchHomeMovieAdapter(output.m, this, this)
            binding?.movieRecycler?.layoutManager = gridLayout2
            binding?.movieRecycler?.adapter = otherCityAdapter
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
        val lat = selectCityItemList.lat
        val lang = selectCityItemList.lng
        val strUri =
            "http://maps.google.com/maps?q=loc:$lat,$lang"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName(
            "com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity"
        )
        startActivity(intent)

    }

    override fun onSearchMovie(selectCityList: HomeSearchResponse.Output.M) {
        val intent = Intent(this, NowShowingActivity::class.java)
        intent.putExtra("mid", selectCityList.id)
        startActivity(intent)
    }

}