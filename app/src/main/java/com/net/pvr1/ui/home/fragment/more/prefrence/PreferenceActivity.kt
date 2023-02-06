package com.net.pvr1.ui.home.fragment.more.prefrence

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPrefrenceBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.more.prefrence.adapter.*
import com.net.pvr1.ui.home.fragment.more.prefrence.response.PreferenceResponse
import com.net.pvr1.ui.home.fragment.more.profile.userDetails.viewModel.PreferenceViewModel
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PreferenceActivity : AppCompatActivity(), PreferenceListAdapter.RecycleViewItemClickListener,
    PreferdGenreAdapter.RecycleViewItemClickListener, AllGenreAdapter.RecycleViewItemClickListener,
    AllLanguageAdapter.RecycleViewItemClickListener,
    PreferLanguageAdapter.RecycleViewItemClickListener,
    FavouriteTheaterAdapter.RecycleViewItemClickListener,
    AllTheaterAdapter.RecycleViewItemClickListener,
    PaymentWalletAdapter.RecycleViewItemClickListenerCity,
    PaymentAllAdapter.RecycleViewItemClickListenerCity {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPrefrenceBinding? = null
    private val authViewModel: PreferenceViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var apiResponse: PreferenceResponse.Output? = null
    private var itemClick = 0
    private var selectedPos = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrefrenceBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include11?.textView108?.text = getString(R.string.my_preferences)
        authViewModel.preference(preferences.getCityName(), preferences.getUserId())
        loadData()
        setPreference()
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
                        apiResponse = it.data.output
                        if (itemClick == 0) {
                            itemClick += 1
                        }
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
                    if (itemClick == 0) {
                        loader = LoaderDialog(R.string.pleaseWait)
                        loader?.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun setPreference() {
        authViewModel.cinemaPreferenceResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        //preferenceDataLoad()

                        authViewModel.preference(preferences.getCityName(), preferences.getUserId())

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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }


    private fun retrieveData(output: PreferenceResponse.Output) {
        //liked genre
        when (selectedPos) {
            0 -> {
                genre()
                selectedPos = 0
                binding?.textView242?.text = "All Genres"
            }
            1 -> {
                binding?.textView242?.text = "All Theaters"
                theater()
                selectedPos = 1
            }
            2 -> {
                binding?.textView242?.text = "All Payments"
                payment()
                selectedPos = 2
            }
            3 -> {
                binding?.textView242?.text = "All Languages"
                selectedPos = 3
                language()
            }
        }
//        if (output.genre.liked.isEmpty()) {
//            binding?.constraintLayout80?.show()
//            binding?.recyclerView55?.hide()
//            binding?.textView240?.hide()
//        } else {
//            printLog("output--->${output.genre.liked.size}")
//
//            binding?.constraintLayout80?.hide()
//            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//            binding?.recyclerView55?.layoutManager = layoutManager
//            val termsAdapter = PreferdGenreAdapter(this, output.genre.liked, this)
//            binding?.recyclerView55?.adapter = termsAdapter
//        }
//        //All genre
//        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        binding?.recyclerView56?.layoutManager = layoutManagerAll
//        val allGenreAdapter = AllGenreAdapter(
//            this,
//            removeAllSubList(
//                output.genre.other,
//                output.genre.liked
//            ) as ArrayList<PreferenceResponse.Output.Genre.Other>, this
//        )
//        binding?.recyclerView56?.adapter = allGenreAdapter
    }

    private fun removeAllSubList(list: ArrayList<*>, subList: ArrayList<*>): ArrayList<*> {
        for (data in subList) {
            if (list.contains(data)) {
                list.remove(data)
            }
        }
        return list
    }

    private fun removePP(list: ArrayList<PreferenceResponse.Output.Genre.Other>, id: String): ArrayList<PreferenceResponse.Output.Genre.Other> {
        for (data in list) {
            if (data.id == id) {
                list.remove(data)
                break
            }
        }
        return list
    }

    private fun getPP(list: ArrayList<PreferenceResponse.Output.Genre.Other>, id: String): ArrayList<PreferenceResponse.Output.Genre.Other> {
         val listPP = java.util.ArrayList<PreferenceResponse.Output.Genre.Other>()
        for (data in list) {
            if (data.id == id) {
                listPP.add(data)
                break
            }
        }
        println("listPP-->$listPP$id")
        return listPP
    }

    override fun prefrenceTypeClick(comingSoonItem: String, position: Int) {
        when (position) {
            0 -> {
                genre()
                selectedPos = 0
            }
            1 -> {
                theater()
                selectedPos = 1
            }
            2 -> {
                payment()
                selectedPos = 2
            }
            3 -> {
                selectedPos = 3
                language()
            }
        }
    }


    override fun allGenreClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        authViewModel.setPreference(
            preferences.getUserId(),
            comingSoonItem.id,
            true,
            "g",
            Constant().getDeviceId(this)
        )
    }


    override fun genreLikeClick(comingSoonItem: PreferenceResponse.Output.Genre.Liked) {
        authViewModel.setPreference(
            preferences.getUserId(),
            comingSoonItem.id,
            false,
            "g",
            Constant().getDeviceId(this)
        )
    }

    private fun genre() {
        //liked genre
        if (apiResponse?.genre?.liked?.isEmpty() == true) {
            binding?.constraintLayout80?.show()
            binding?.recyclerView55?.hide()
            binding?.textView240?.hide()
            binding?.textView238?.text = getString(R.string.preferred_genres)
            binding?.textView239?.text = getString(R.string.noGenresSelected)
        } else {
            binding?.constraintLayout80?.hide()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerView55?.layoutManager = layoutManager
            val termsAdapter =
                apiResponse?.genre?.liked?.let { PreferdGenreAdapter(this, it, this) }
            binding?.recyclerView55?.adapter = termsAdapter
        }

        //All genre
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        val allGenreAdapter = apiResponse?.genre?.other?.let { AllGenreAdapter(this, removeAllSubList(
            it,
            apiResponse?.genre?.liked!!
        ) as ArrayList<PreferenceResponse.Output.Genre.Other>, this) }
        binding?.recyclerView56?.adapter = allGenreAdapter
    }


    override fun allLanguageClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        authViewModel.setPreference(
            preferences.getUserId(),
            comingSoonItem.id,
            true,
            "l",
            Constant().getDeviceId(this)
        )
    }

    override fun languageLikeClick(comingSoonItem: PreferenceResponse.Output.Genre.Liked) {
        authViewModel.setPreference(
            preferences.getUserId(),
            comingSoonItem.id,
            false,
            "l",
            Constant().getDeviceId(this)
        )
    }

    private fun language() {
        //liked language
        if (apiResponse?.lang?.liked?.isEmpty() == true) {
            binding?.constraintLayout80?.show()
            binding?.recyclerView55?.hide()
            binding?.textView240?.hide()
            binding?.textView238?.text = getString(R.string.preferredLanguages)
            binding?.textView239?.text = getString(R.string.noLangaugesSelected)
        } else {
            binding?.constraintLayout80?.hide()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerView55?.layoutManager = layoutManager
            val termsAdapter =
                apiResponse?.lang?.liked?.let { PreferLanguageAdapter(this, it, this) }
            binding?.recyclerView55?.adapter = termsAdapter
        }

        //All language
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        val allGenreAdapter = apiResponse?.lang?.other?.let { AllLanguageAdapter(this, removeAllSubList(
            it,
            apiResponse?.lang?.liked!!
        ) as ArrayList<PreferenceResponse.Output.Genre.Other>, this) }
        binding?.recyclerView56?.adapter = allGenreAdapter
    }

    override fun allTheaterClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        authViewModel.setPreference(
            preferences.getUserId(),
            comingSoonItem.id,
            true,
            "t",
            Constant().getDeviceId(this)
        )
    }

    override fun favouriteTheaterClick(comingSoonItem: PreferenceResponse.Output.Genre.Liked) {
        authViewModel.setPreference(
            preferences.getUserId(),
            comingSoonItem.id,
            false,
            "t",
            Constant().getDeviceId(this)
        )
    }

    private fun theater() {
        //liked language
        if (apiResponse?.theater?.liked?.isEmpty() == true) {
            binding?.constraintLayout80?.show()
            binding?.recyclerView55?.hide()
            binding?.textView240?.hide()
            binding?.textView238?.text = getString(R.string.favouriteTheaters)
            binding?.textView239?.text = getString(R.string.noTheatersSelected)
        } else {
            binding?.constraintLayout80?.hide()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerView55?.layoutManager = layoutManager
            val termsAdapter =
                apiResponse?.theater?.liked?.let { FavouriteTheaterAdapter(this, it, this) }
            binding?.recyclerView55?.adapter = termsAdapter
        }

        //All language
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        val allGenreAdapter = apiResponse?.theater?.other?.let { AllTheaterAdapter(this, removeAllSubList(
            it,
            apiResponse?.theater?.liked!!
        ) as ArrayList<PreferenceResponse.Output.Genre.Other>, this) }
        binding?.recyclerView56?.adapter = allGenreAdapter

    }


    private fun payment() {
        //liked language
        if (apiResponse?.pp?.isEmpty() == true) {
            binding?.constraintLayout80?.show()
            binding?.recyclerView55?.hide()
            binding?.textView240?.hide()
            binding?.textView238?.text = getString(R.string.preferredPaymentMethod)
            binding?.textView239?.text = getString(R.string.noPaymentMethodSelected)
        } else {
            binding?.constraintLayout80?.hide()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerView55?.layoutManager = layoutManager
            val termsAdapter =
                apiResponse?.pon?.let { PaymentWalletAdapter(this, getPP(it.Other,apiResponse?.pp?:"0"), this) }
            binding?.recyclerView55?.adapter = termsAdapter
        }

        //All language
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        val allGenreAdapter = apiResponse?.pon?.Other?.let { PaymentAllAdapter(this, removePP(it,apiResponse?.pp?:""), this) }
        binding?.recyclerView56?.adapter = allGenreAdapter
    }

    override fun paymentAllClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        authViewModel.setPreference(
            preferences.getUserId(),
            comingSoonItem.id,
            true,
            "p",
            Constant().getDeviceId(this)
        )
    }

    override fun paymentWalletClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        authViewModel.setPreference(
            preferences.getUserId(),
            comingSoonItem.id,
            true,
            "p",
            Constant().getDeviceId(this)
        )
    }

}