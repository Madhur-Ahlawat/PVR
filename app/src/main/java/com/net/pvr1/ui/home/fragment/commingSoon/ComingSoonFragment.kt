package com.net.pvr1.ui.home.fragment.commingSoon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityComingSoonBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.filter.GenericFilterComing
import com.net.pvr1.ui.home.fragment.commingSoon.adapter.ComingSoonMovieAdapter
import com.net.pvr1.ui.home.fragment.commingSoon.adapter.LanguageAdapter
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.home.fragment.commingSoon.search.CinemaSearchActivity
import com.net.pvr1.ui.home.fragment.commingSoon.viewModel.ComingSoonViewModel
import com.net.pvr1.ui.movieDetails.comingSoonDetails.ComingSoonDetailsActivity
import com.net.pvr1.ui.movieDetails.comingSoonDetails.adapter.ComDetailsHomePhAdapter
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ComingSoonFragment : Fragment(), LanguageAdapter.RecycleViewItemClickListener,
    ComingSoonMovieAdapter.VideoPlay, GenericFilterComing.onButtonSelected {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ActivityComingSoonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().findViewById(R.id.include) as ConstraintLayout).hide()
        comingSoonApi()
        movedNext()
        comingSoonAPICall()
    }
    private fun comingSoonAPICall() {

        if (buttonPressed.isEmpty()){

            language="ALL"
        }
        else {
            var tempLang = buttonPressed[0].split("-").toTypedArray()[0].trim { it <= ' ' }
            for (i in 1 until buttonPressed.size) tempLang =
                tempLang + "," + buttonPressed[i].split("-").toTypedArray()[0].trim { it <= ' ' }
//            params.put("lang", tempLang)
            language=tempLang
        }

        if (generseleced.isEmpty()){
            genre="ALL"
//            params.put("genre", "ALL")
        }
             else {
            var tempGenera = generseleced[0].split("-").toTypedArray()[0].trim { it <= ' ' }
            for (i in 1 until generseleced.size) tempGenera =
                tempGenera + "," + generseleced[i].split("-").toTypedArray()[0].trim { it <= ' ' }
//            params.put("genre", tempGener)
            genre=tempGenera
        }
        authViewModel.comingSoon(preferences.getCityName(), genre, language, preferences.getUserId())

    }

    private fun movedNext() {
        binding?.imageView167?.setOnClickListener {
            val intent = Intent(requireActivity(), CinemaSearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun comingSoonApi() {
        authViewModel.userResponseLiveData.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: CommingSoonResponse.Output) {
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
        requireActivity().printLog("movies--->${output.movies}")
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

    }

    override fun onDateClick(comingSoonItem: Any) {

    }

    override fun onDateClick(comingSoonItem: CommingSoonResponse.Output.Movy) {
        val intent = Intent(requireActivity(), ComingSoonDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.masterMovieId)
        startActivity(intent)
    }

    override fun onTrailerClick(comingSoonItem: CommingSoonResponse.Output.Movy) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.videoUrl)
        startActivity(intent)
    }

    override fun onApply(
        type: ArrayList<String>,
        filterItemSelected: HashMap<String, String>,
        isSelected: Boolean,
        name: String
    ) {
        if (type.size > 1) {
            binding?.filterFab?.setImageResource(R.drawable.filter_selected)
            appliedFilterItem = filterItemSelected
            binding?.appliedFilter?.visibility = View.VISIBLE
            val containLanguage = type.contains("language")
            if (containLanguage) {
                val index = type.indexOf("language")
                var value: String = filterItemSelected.get(type[index])!!
                if (!value.equals("", ignoreCase = true)) {
                    buttonPressed.clear()
                    value = value.uppercase(Locale.getDefault())
                    val valuesString = value.split(",").toTypedArray()
                    for (s in valuesString) {
                        if (!buttonPressed.contains(s)) buttonPressed.add(s.trim { it <= ' ' } + "-language")
                    }
                    println("buttonPressed--->$value")
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
        } else binding?.appliedFilter?.visibility = View.GONE
    }


    override fun onReset() {
        binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
        buttonPressed = ArrayList<String>()
        generseleced = ArrayList<String>()
        binding?.appliedFilter?.setVisibility(View.GONE)
        appliedFilterItem = HashMap()
    }
}