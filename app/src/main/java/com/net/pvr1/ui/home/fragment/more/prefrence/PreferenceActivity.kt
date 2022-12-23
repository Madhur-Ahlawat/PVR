package com.net.pvr1.ui.home.fragment.more.prefrence

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPrefrenceBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.more.prefrence.adapter.*
import com.net.pvr1.ui.home.fragment.more.prefrence.response.PreferenceResponse
import com.net.pvr1.ui.profile.userDetails.viewModel.PreferenceViewModel
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PreferenceActivity : AppCompatActivity(), PreferenceListAdapter.RecycleViewItemClickListener,
    PreferdGenreAdapter.RecycleViewItemClickListener, AllGenreAdapter.RecycleViewItemClickListener,
    AllLanguageAdapter.RecycleViewItemClickListener,PreferdLanguageAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPrefrenceBinding? = null
    private val authViewModel: PreferenceViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var apiResponse: PreferenceResponse.Output? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrefrenceBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include11?.textView108?.text = getString(R.string.my_preferences)
        authViewModel.preference(preferences.getCityName(), preferences.getUserId())
        loadData()
        preferenceDataLoad()
    }

    private fun loadData() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding?.recyclerView28?.layoutManager = layoutManager
        val list: Array<String> = resources.getStringArray(R.array.genres)
        val termsAdapter = PreferenceListAdapter(this, list, this)
        binding?.recyclerView28?.adapter = termsAdapter
    }


    private fun preferenceDataLoad() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        printLog("output--->${it.data.output}")
                        apiResponse=it.data.output
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }


    private fun retrieveData(output: PreferenceResponse.Output) {
        //liked genre
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView55?.layoutManager = layoutManager
        val termsAdapter = PreferdGenreAdapter(this, output.genre.liked, this)
        binding?.recyclerView55?.adapter = termsAdapter

        //All genre
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        val allGenreAdapter = AllGenreAdapter(this, output.genre.other, this)
        binding?.recyclerView56?.adapter = allGenreAdapter
    }

    override fun prefrenceTypeClick(comingSoonItem: String, position: Int) {
        when (position) {
            0 -> {
                genre()
            }
            1 -> {
                theater()
            }
            2 -> {
                payment()
            }
            3 -> {
                language()
            }
        }
    }



    override fun allGenreClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        binding?.constraintLayout141?.show()
    }


    override fun genreLikeClick(comingSoonItem: PreferenceResponse.Output.Genre.Liked) {

    }

    private fun genre() {
        //liked genre
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView55?.layoutManager = layoutManager
        val termsAdapter = apiResponse?.genre?.liked?.let { PreferdGenreAdapter(this, it, this) }
        binding?.recyclerView55?.adapter = termsAdapter

        //All genre
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        val allGenreAdapter = apiResponse?.genre?.other?.let { AllGenreAdapter(this, it, this) }
        binding?.recyclerView56?.adapter = allGenreAdapter
    }


    override fun allLanguageClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {

    }

    override fun languageLikeClick(comingSoonItem: PreferenceResponse.Output.Genre.Liked) {
        binding?.constraintLayout141?.show()

    }
    private fun language() {
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
        //liked language
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView55?.layoutManager = layoutManager
        val termsAdapter = apiResponse?.lang?.liked?.let { PreferdLanguageAdapter(this, it, this) }
        binding?.recyclerView55?.adapter = termsAdapter

        //All language
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        val allGenreAdapter = apiResponse?.lang?.other?.let { AllLanguageAdapter(this, it, this) }
        binding?.recyclerView56?.adapter = allGenreAdapter
    }

    private fun theater() {

    }

    private fun payment() {

    }



}