package com.net.pvr.ui.home.fragment.more.prefrence

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityPrefrenceBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.more.prefrence.adapter.*
import com.net.pvr.ui.home.fragment.more.prefrence.response.PreferenceResponse
import com.net.pvr.ui.home.fragment.more.profile.userDetails.viewModel.PreferenceViewModel
import com.net.pvr.utils.*
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PreferenceActivity : AppCompatActivity(),
    PreferenceListAdapter.RecycleViewItemClickListener,
    PreferdGenreAdapter.RecycleViewItemClickListener,
    AllGenreAdapter.RecycleViewItemClickListener,
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

        manageData()
    }

    private fun manageData() {
        authViewModel.preference(preferences.getCityName(), preferences.getUserId())
        loadData()
        setPreference()
        preferenceDataLoad()
        appBar()
    }


    private fun appBar() {
        //       TitleBar
        binding?.include11?.textView108?.text = getString(R.string.my_preferences)

        //      finish
        binding?.include11?.imageView58?.setOnClickListener {
            finish()
        }
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
        binding?.constraintLayout173?.show()
        //liked genre
        when (selectedPos) {
            0 -> {
                selectedPos = 0
                binding?.textView242?.text = "All Genres"
                genre()
            }
            1 -> {
                binding?.textView242?.text = "All Theaters"
                selectedPos = 1
                theater()
            }
            2 -> {
                binding?.textView242?.text = "All Payments"
                selectedPos = 2
                payment()
            }
            3 -> {
                binding?.textView242?.text = "All Languages"
                selectedPos = 3
                language()
            }
        }

    }

    private fun removeAllSubList(list: ArrayList<*>, subList: ArrayList<*>): ArrayList<*> {
        for (data in subList) {
            if (list.contains(data)) {
                list.remove(data)
            }
        }
        return list
    }

    private fun removePP(
        list: ArrayList<PreferenceResponse.Output.Genre.Other>, id: String
    ): ArrayList<PreferenceResponse.Output.Genre.Other> {
        for (data in list) {
            if (data.id == id) {
                list.remove(data)
                break
            }
        }
        return list
    }

    private fun getPP(
        list: ArrayList<PreferenceResponse.Output.Genre.Other>, id: String
    ): ArrayList<PreferenceResponse.Output.Genre.Other> {
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
                selectedPos = 0
                genre()
            }
            1 -> {
                selectedPos = 1
                theater()
            }
            2 -> {
                selectedPos = 2
                payment()
            }
            3 -> {
                language()
                selectedPos = 3
            }
        }
    }


    //Genre
    override fun allGenreClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
//            bundle.putString("var_experiences_banner", comingSoonItem.name)
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_genre", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }

//        authViewModel.setPreference(
//            preferences.getUserId(), comingSoonItem.id, true, "g", Constant().getDeviceId(this)
//        )
    }

    override fun genreLikeClick(comingSoonItem: PreferenceResponse.Output.Genre.Liked) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
            bundle.putString("var_my_pvr_edit_genre_fav", "")
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_genre_favourite", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }

//        authViewModel.setPreference(
//            preferences.getUserId(), comingSoonItem.id, false, "g", Constant().getDeviceId(this)
//        )
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
            binding?.recyclerView55?.show()
            binding?.constraintLayout80?.hide()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerView55?.layoutManager = layoutManager
            val termsAdapter =
                apiResponse?.genre?.liked?.let { PreferdGenreAdapter(this, it, this) }
            binding?.recyclerView55?.adapter = termsAdapter

            //clear All
            binding?.textView241?.setOnClickListener {
                toast("12345")
                apiResponse?.genre?.liked?.clear()
                termsAdapter?.notifyDataSetChanged()
                binding?.textView241?.hide()
                binding?.constraintLayout80?.show()
                binding?.recyclerView55?.hide()
                binding?.textView240?.hide()
                binding?.textView238?.text = getString(R.string.preferred_genres)
                binding?.textView239?.text = getString(R.string.noGenresSelected)
            }
        }

        //select All
        //All genre
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        val allGenreAdapter = apiResponse?.genre?.other?.let {
            AllGenreAdapter(this, removeAllSubList(it, apiResponse?.genre?.liked!!)
                    as ArrayList<PreferenceResponse.Output.Genre.Other>, this) }
        binding?.recyclerView56?.adapter = allGenreAdapter

        binding?.textView243?.setOnClickListener {
            for (item in apiResponse?.genre?.other!!){
                item.liked= true
                printLog("liked---->${item.liked}")
                allGenreAdapter?.notifyDataSetChanged()

            }

        }


    }


    //Language
    override fun allLanguageClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
//            bundle.putString("var_experiences_banner", comingSoonItem.name)
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_language", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }
//        authViewModel.setPreference(
//            preferences.getUserId(), comingSoonItem.id, true, "l", Constant().getDeviceId(this)
//        )
    }

    override fun languageLikeClick(comingSoonItem: PreferenceResponse.Output.Genre.Liked) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
            bundle.putString("var_my_pvr_edit_language_fav", "")
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_language_favourite", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }

//        authViewModel.setPreference(
//            preferences.getUserId(), comingSoonItem.id, false, "l", Constant().getDeviceId(this)
//        )
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
        val allGenreAdapter = apiResponse?.lang?.other?.let {
            AllLanguageAdapter(
                this, removeAllSubList(
                    it, apiResponse?.lang?.liked!!
                ) as ArrayList<PreferenceResponse.Output.Genre.Other>, this
            )
        }
        binding?.recyclerView56?.adapter = allGenreAdapter
    }

    //theater
    override fun allTheaterClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
//            bundle.putString("var_experiences_banner", comingSoonItem.name)
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_theatres", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }

//        authViewModel.setPreference(
//            preferences.getUserId(), comingSoonItem.id, true, "t", Constant().getDeviceId(this)
//        )
    }

    override fun favouriteTheaterClick(comingSoonItem: PreferenceResponse.Output.Genre.Liked) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
            bundle.putString("var_my_pvr_edit_theatres_fav", "")
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_theatres_favourite", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }


//        authViewModel.setPreference(
//            preferences.getUserId(), comingSoonItem.id, false, "t", Constant().getDeviceId(this)
//        )
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
        val allGenreAdapter = apiResponse?.theater?.other?.let {
            AllTheaterAdapter(
                this, removeAllSubList(
                    it, apiResponse?.theater?.liked!!
                ) as ArrayList<PreferenceResponse.Output.Genre.Other>, this
            )
        }
        binding?.recyclerView56?.adapter = allGenreAdapter

    }

    //Payment
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
            val termsAdapter = apiResponse?.pon?.let {
                PaymentWalletAdapter(
                    this, getPP(it.Other, apiResponse?.pp ?: "0"), this
                )
            }
            binding?.recyclerView55?.adapter = termsAdapter
        }

        //All language
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        val allGenreAdapter = apiResponse?.pon?.Other?.let {
            PaymentAllAdapter(
                this, removePP(it, apiResponse?.pp ?: ""), this
            )
        }
        binding?.recyclerView56?.adapter = allGenreAdapter
    }

    override fun paymentAllClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
//            bundle.putString("var_experiences_banner", comingSoonItem.name)
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_payment", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }

//        authViewModel.setPreference(
//            preferences.getUserId(), comingSoonItem.id, true, "p", Constant().getDeviceId(this)
//        )
    }

    override fun paymentWalletClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
            bundle.putString("var_my_pvr_edit_payment_fav", "")
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_payment_favourite", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }
//        authViewModel.setPreference(
//            preferences.getUserId(), comingSoonItem.id, true, "p", Constant().getDeviceId(this)
//        )
    }

}