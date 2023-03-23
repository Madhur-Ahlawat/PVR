package com.net.pvr.ui.home.fragment.more.prefrence

import android.annotation.SuppressLint
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
    LikeGenreAdapter.RecycleViewItemClickListener,
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

    //genre
    private var allGenreAdapter: AllGenreAdapter? = null
    private var likedGenreAdapter: LikeGenreAdapter? = null

    //language
    private var likeLanguageAdapter: PreferLanguageAdapter? = null
    private var allLanguageAdapter: AllLanguageAdapter? = null

    // theater
    private var likeTheaterAdapter: FavouriteTheaterAdapter? = null
    private var allTheaterAdapter: AllTheaterAdapter? = null


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
        preferenceDataLoad()
        appBar()
    }

    //////////////////////////////// App Bar ////////////////////////////////////
    private fun appBar() {
        //       TitleBar
        binding?.include11?.textView108?.text = getString(R.string.my_preferences)

        //      finish
        binding?.include11?.imageView58?.setOnClickListener {
            finish()
        }
    }

    //Category
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
                        retrieveData()

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

    private fun retrieveData() {
        binding?.constraintLayout173?.show()
        selectedPos = 0
        genre()

    }

    //    Preference type Click
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
                selectedPos = 3
                language()
            }
        }
    }

    ////////////////////     Genre    //////////////////////////////
    @SuppressLint("NotifyDataSetChanged")
    private fun genre() {
        printLog("---------------->genre")
        binding?.textView240?.show()
        binding?.textView242?.show()
        //liked language
        binding?.textView240?.text = "Preferred Genres"

        //All Language
        binding?.textView242?.text = "All Genres"

        //liked genre Check Data
        if (apiResponse?.genre?.liked?.size == 0) {
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
            likedGenreAdapter = LikeGenreAdapter(this, apiResponse?.genre?.liked!!, this)
            binding?.recyclerView55?.adapter = likedGenreAdapter
        }

        //All genre
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        allGenreAdapter = AllGenreAdapter(checkLikedGenre(), this)
        binding?.recyclerView56?.adapter = allGenreAdapter


//         allGenreAdapter?.notifyDataSetChanged()
//        likedGenreAdapter?.notifyDataSetChanged()

        //language
        likeLanguageAdapter?.notifyDataSetChanged()
        allLanguageAdapter?.notifyDataSetChanged()

        // theater
        likeTheaterAdapter?.notifyDataSetChanged()
        allTheaterAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun allGenreClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
        apiResponse?.genre?.liked?.add(comingSoonItem)
        apiResponse?.genre?.other?.remove(comingSoonItem)


        //manage show Hide
        //liked language
        binding?.textView240?.show()
        binding?.textView240?.text = "Preferred Genres"

        binding?.constraintLayout80?.hide()
        binding?.recyclerView55?.show()


        if (apiResponse?.genre?.liked?.size == 1) {
            binding?.recyclerView55?.show()
            binding?.constraintLayout80?.hide()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerView55?.layoutManager = layoutManager
            likedGenreAdapter = LikeGenreAdapter(this, apiResponse?.genre?.liked!!, this)
            binding?.recyclerView55?.adapter = likedGenreAdapter
        }

        likedGenreAdapter?.notifyDataSetChanged()
        allGenreAdapter?.notifyDataSetChanged()

        authViewModel.setPreference(
            preferences.getUserId(), comingSoonItem.id, true, "g", Constant().getDeviceId(this)
        )

        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_genre", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun genreLikeClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {

        apiResponse?.genre?.other?.add(comingSoonItem)

        apiResponse?.genre?.liked?.remove(comingSoonItem)


        if (apiResponse?.genre?.liked?.size == 0) {
            binding?.constraintLayout80?.show()
            binding?.recyclerView55?.hide()
            binding?.textView240?.hide()
            binding?.textView238?.text = getString(R.string.preferred_genres)
            binding?.textView239?.text = getString(R.string.noGenresSelected)
        } else {
            binding?.constraintLayout80?.hide()
            binding?.recyclerView55?.show()
        }



        likedGenreAdapter?.notifyDataSetChanged()

        allGenreAdapter?.notifyDataSetChanged()

        authViewModel.setPreference(
            preferences.getUserId(), comingSoonItem.id, false, "g", Constant().getDeviceId(this)
        )

        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
            bundle.putString("var_my_pvr_edit_genre_fav", "")
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_genre_favourite", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkLikedGenre(): ArrayList<PreferenceResponse.Output.Genre.Other> {
        val categoryFilterNew = apiResponse?.genre?.other
        val likedNew = apiResponse?.genre?.liked
        categoryFilterNew?.removeAll(likedNew!!.toSet())
        return categoryFilterNew!!
    }


    ///////////////////////////////            Language      ////////////////////////////////
    private fun language() {
        printLog("---------------->language")
        binding?.textView240?.show()
        binding?.textView242?.show()

        //liked language
        binding?.textView240?.text = "Preferred Languages"

        //All Language
        binding?.textView242?.text = "All Languages"

        if (apiResponse?.lang?.liked?.size == 0) {
            binding?.constraintLayout80?.show()
            binding?.recyclerView55?.hide()
            binding?.textView240?.hide()
            binding?.textView238?.text = getString(R.string.preferredLanguages)
            binding?.textView239?.text = getString(R.string.noLangaugesSelected)
        } else {
            binding?.recyclerView55?.show()
            binding?.constraintLayout80?.hide()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerView55?.layoutManager = layoutManager
            likeLanguageAdapter = PreferLanguageAdapter(this, apiResponse?.lang?.liked!!, this)
            binding?.recyclerView55?.adapter = likeLanguageAdapter

        }


        //All language
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        allLanguageAdapter = AllLanguageAdapter(checkLikedLanguage(), this)
        binding?.recyclerView56?.adapter = allLanguageAdapter



        allGenreAdapter?.notifyDataSetChanged()
        likedGenreAdapter?.notifyDataSetChanged()

        //language
//        likeLanguageAdapter?.notifyDataSetChanged()
//        allLanguageAdapter?.notifyDataSetChanged()

        // theater
        likeTheaterAdapter?.notifyDataSetChanged()
        allTheaterAdapter?.notifyDataSetChanged()
    }

    private fun checkLikedLanguage(): ArrayList<PreferenceResponse.Output.Lang.Other> {
        val categoryFilterNew = apiResponse?.lang?.other
        val likedNew = apiResponse?.lang?.liked
        categoryFilterNew?.removeAll(likedNew!!.toSet())
        return categoryFilterNew!!
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun allLanguageClick(comingSoonItem: PreferenceResponse.Output.Lang.Other) {

        apiResponse?.lang?.other?.remove(comingSoonItem)

        apiResponse?.lang?.liked?.add(comingSoonItem)


        binding?.textView240?.show()

        //liked language
        binding?.textView240?.text = "Preferred Languages"

        binding?.constraintLayout80?.hide()
        binding?.recyclerView55?.show()

        if (apiResponse?.lang?.liked?.size == 1) {

            binding?.recyclerView55?.show()
            binding?.constraintLayout80?.hide()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerView55?.layoutManager = layoutManager
            likeLanguageAdapter = PreferLanguageAdapter(this, apiResponse?.lang?.liked!!, this)
            binding?.recyclerView55?.adapter = likeLanguageAdapter
        }


        allLanguageAdapter?.notifyDataSetChanged()
        likeLanguageAdapter?.notifyDataSetChanged()

        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_language", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        authViewModel.setPreference(
            preferences.getUserId(), comingSoonItem.id, true, "l", Constant().getDeviceId(this)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun languageLikeClick(comingSoonItem: PreferenceResponse.Output.Lang.Other) {

        apiResponse?.lang?.other?.add(comingSoonItem)

        apiResponse?.lang?.liked?.remove(comingSoonItem)


        if (apiResponse?.lang?.liked?.size!! == 0) {
            binding?.constraintLayout80?.show()
            binding?.recyclerView55?.hide()
            binding?.textView240?.hide()
            binding?.textView238?.text = getString(R.string.preferredLanguages)
            binding?.textView239?.text = getString(R.string.noLangaugesSelected)
        } else {
            binding?.constraintLayout80?.hide()
            binding?.recyclerView55?.show()
        }

        allLanguageAdapter?.notifyDataSetChanged()
        likeLanguageAdapter?.notifyDataSetChanged()

        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
            bundle.putString("var_my_pvr_edit_language_fav", "")
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_language_favourite", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        authViewModel.setPreference(
            preferences.getUserId(), comingSoonItem.id, false, "l", Constant().getDeviceId(this)
        )
    }


    //theater
    private fun theater() {
        printLog("---------------->theater")
        //liked language
        binding?.textView240?.show()
        binding?.textView242?.show()
        binding?.textView240?.text = "Favourite Theaters"

        //All Language
        binding?.textView242?.text = "All Theaters"

        //liked Theater
        if (apiResponse?.theater?.liked?.size == 0) {
            binding?.constraintLayout80?.show()
            binding?.recyclerView55?.hide()
            binding?.textView240?.hide()
            binding?.textView238?.text = getString(R.string.favouriteTheaters)
            binding?.textView239?.text = getString(R.string.noTheatersSelected)
        } else {
            binding?.constraintLayout80?.hide()
            binding?.recyclerView55?.show()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerView55?.layoutManager = layoutManager
            likeTheaterAdapter =
                apiResponse?.theater?.liked?.let { FavouriteTheaterAdapter(this, it, this) }
            binding?.recyclerView55?.adapter = likeTheaterAdapter
        }


        //All Theater
        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView56?.layoutManager = layoutManagerAll
        allTheaterAdapter = AllTheaterAdapter(this, checkLikedTheater(), this)
        binding?.recyclerView56?.adapter = allTheaterAdapter


        allGenreAdapter?.notifyDataSetChanged()
        likedGenreAdapter?.notifyDataSetChanged()

        //language
        likeLanguageAdapter?.notifyDataSetChanged()
        allLanguageAdapter?.notifyDataSetChanged()

//        // theater
//        likeTheaterAdapter?.notifyDataSetChanged()
//        allTheaterAdapter?.notifyDataSetChanged()
    }

    private fun checkLikedTheater(): ArrayList<PreferenceResponse.Output.Theater.Other> {
        val categoryFilterNew = apiResponse?.theater?.other
        val likedNew = apiResponse?.theater?.liked
        categoryFilterNew?.removeAll(likedNew!!.toSet())
        return categoryFilterNew!!
    }

    //theater
    @SuppressLint("NotifyDataSetChanged")
    override fun allTheaterClick(comingSoonItem: PreferenceResponse.Output.Theater.Other) {


        apiResponse?.theater?.other?.remove(comingSoonItem)

        apiResponse?.theater?.liked?.add(comingSoonItem)

        binding?.textView240?.show()
        binding?.textView240?.text = getString(R.string.favouriteTheaters)

        binding?.constraintLayout80?.hide()

        binding?.recyclerView55?.show()
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
//            bundle.putString("var_experiences_banner", comingSoonItem.name)
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_theatres", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (apiResponse?.theater?.liked?.size == 1) {
            binding?.constraintLayout80?.hide()
            binding?.recyclerView55?.show()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerView55?.layoutManager = layoutManager
            likeTheaterAdapter =
                apiResponse?.theater?.liked?.let { FavouriteTheaterAdapter(this, it, this) }
            binding?.recyclerView55?.adapter = likeTheaterAdapter
        }

        allTheaterAdapter?.notifyDataSetChanged()
        likeTheaterAdapter?.notifyDataSetChanged()

        authViewModel.setPreference(
            preferences.getUserId(), comingSoonItem.id, true, "t", Constant().getDeviceId(this)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun favouriteTheaterClick(comingSoonItem: PreferenceResponse.Output.Theater.Other) {

        apiResponse?.theater?.other?.add(comingSoonItem)

        apiResponse?.theater?.liked?.remove(comingSoonItem)


        //liked Theater
        if (apiResponse?.theater?.liked?.size!! == 0) {
            binding?.constraintLayout80?.show()
            binding?.recyclerView55?.hide()
            binding?.textView240?.hide()
            binding?.textView238?.text = getString(R.string.favouriteTheaters)
            binding?.textView239?.text = getString(R.string.noTheatersSelected)
        } else {
            binding?.constraintLayout80?.hide()
            binding?.recyclerView55?.show()
            binding?.textView240?.show()

        }

        allTheaterAdapter?.notifyDataSetChanged()
        likeTheaterAdapter?.notifyDataSetChanged()

        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
            bundle.putString("var_my_pvr_edit_theatres_fav", "")
            GoogleAnalytics.hitEvent(this, "my_pvr_edit_theatres_favourite", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        authViewModel.setPreference(
            preferences.getUserId(), comingSoonItem.id, false, "t", Constant().getDeviceId(this)
        )

    }


    override fun paymentAllClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {

    }

    override fun paymentWalletClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {

    }


//    //Payment
//    private fun payment() {
//        //liked language
//        if (apiResponse?.pp?.isEmpty() == true) {
//            binding?.constraintLayout80?.show()
//            binding?.recyclerView55?.hide()
//            binding?.textView240?.hide()
//            binding?.textView238?.text = getString(R.string.preferredPaymentMethod)
//            binding?.textView239?.text = getString(R.string.noPaymentMethodSelected)
//        } else {
//            binding?.constraintLayout80?.hide()
//            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//            binding?.recyclerView55?.layoutManager = layoutManager
//            val termsAdapter = apiResponse?.pon?.let {
//                PaymentWalletAdapter(
//                    this, getPP(it.Other, apiResponse?.pp ?: "0"), this
//                )
//            }
//            binding?.recyclerView55?.adapter = termsAdapter
//        }
//
//        //All language
//        val layoutManagerAll = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        binding?.recyclerView56?.layoutManager = layoutManagerAll
//        val allGenreAdapter = apiResponse?.pon?.Other?.let {
//            PaymentAllAdapter(
//                this, removePP(it, apiResponse?.pp ?: ""), this
//            )
//        }
//        binding?.recyclerView56?.adapter = allGenreAdapter
//    }
//
//    override fun paymentAllClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
//        // Hit Event
//        try {
//            val bundle = Bundle()
//            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
////            bundle.putString("var_experiences_banner", comingSoonItem.name)
//            GoogleAnalytics.hitEvent(this, "my_pvr_edit_payment", bundle)
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
//
////        authViewModel.setPreference(
////            preferences.getUserId(), comingSoonItem.id, true, "p", Constant().getDeviceId(this)
////        )
//    }
//
//    override fun paymentWalletClick(comingSoonItem: PreferenceResponse.Output.Genre.Other) {
//        // Hit Event
//        try {
//            val bundle = Bundle()
//            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
//            bundle.putString("var_my_pvr_edit_payment_fav", "")
//            GoogleAnalytics.hitEvent(this, "my_pvr_edit_payment_favourite", bundle)
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
////        authViewModel.setPreference(
////            preferences.getUserId(), comingSoonItem.id, true, "p", Constant().getDeviceId(this)
////        )
//    }

}