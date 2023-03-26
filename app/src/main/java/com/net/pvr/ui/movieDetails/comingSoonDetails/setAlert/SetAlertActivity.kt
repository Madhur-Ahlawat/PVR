package com.net.pvr.ui.movieDetails.comingSoonDetails.setAlert

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.net.pvr.R
import com.net.pvr.databinding.ActivitySetAlertBinding
import com.net.pvr.databinding.SetAlertDialogBinding
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr.ui.movieDetails.comingSoonDetails.setAlert.adapter.AlertTheaterAdapter
import com.net.pvr.ui.movieDetails.comingSoonDetails.setAlert.viewModel.SetAlertViewModel
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.utils.*
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.and
import org.json.JSONObject
import java.security.MessageDigest
import javax.inject.Inject

@AndroidEntryPoint
class SetAlertActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var _binding: ActivitySetAlertBinding? = null
    val binding get() = _binding!!
    lateinit var theaterAdapter: AlertTheaterAdapter
    private val unableDisable = MutableLiveData(false)
    private var loader: LoaderDialog? = null
    private val authViewModel: SetAlertViewModel by viewModels()
    private var selectedItemList: MutableList<BookingRetrievalResponse.Output.C> = mutableListOf()
    private val cinemaList: ArrayList<String> = arrayListOf()

    private var timeStamp = ""
    private var whatsappStatusCheck:Boolean = false

    companion object{
        var alert = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySetAlertBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        manageFunction()
    }

    private fun manageFunction() {
        authViewModel.allTheater(preferences.getCityName(), preferences.getLatitudeData(), preferences.getLongitudeData(), preferences.getUserId(), "")
        timeStamp = (System.currentTimeMillis() / 1000).toString()
        if (preferences.getIsLogin()){
            authViewModel.whatsappOpt(
                preferences.getUserId(),
                preferences.getToken().toString(),
                timeStamp,
                getHash(preferences.getUserId() + "|" + preferences.getToken() + "|" + "optin-info" + "|" + timeStamp)
            )
        }

        allTheater()
        movedNext()
        setUiValue()
        setAlert()
        whatsappOptStatus()
    }

    @Throws(Exception::class)
    fun getHash(text: String): String {
        val mdText = MessageDigest.getInstance("SHA-512")
        val byteData = mdText.digest(text.toByteArray())
        val sb = StringBuffer()
        for (i in byteData.indices) {
            sb.append(((byteData[i] and 0xff) + 0x100).toString(16).substring(1))
        }
        return sb.toString()
    }

    private fun whatsappOptStatus() {
        authViewModel.optResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveWhatsappData(it.data.output)
                    }
                }
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
            }
        }

    }

    private fun retrieveWhatsappData(output: Boolean) {
        whatsappStatusCheck= output
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun setUiValue() {

        binding.toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            unableDisable.value = isChecked
            theaterAdapter?.notifyDataSetChanged()
        }

        binding.apply {
            include14.textView5.background = getDrawable(R.drawable.alert_curve_ui_unselect)
            include14.textView5.isClickable = false
        }

        unableDisable.observe(this) {
            if (it) {
                selectedItemList = ArrayList()
                binding.textView280.text = "You have selected all the theaters"
                binding.include14.textView5.background = getDrawable(R.drawable.yellow_book_curve)
                binding.include14.textView5.isClickable = true
            } else {
                binding.textView280.text = "${selectedItemList.size}/5 Selected"
                binding.include14.textView5.isClickable = false

                binding.include14.textView5.background =
                    getDrawable(R.drawable.alert_curve_ui_unselect)
                //binding.include14.textView5.setBackgroundColor(getColor(R.color.unSelectBg))
            }
        }
        //set city name here
        binding.textView36.text = preferences.getCityName()
    }

    private fun movedNext() {

        binding.include13.imageView58.setOnClickListener {
            finish()
        }

        //search
        binding.searchAlert.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                theaterAdapter.filter.filter(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        binding.include14.textView5.setOnClickListener {
            for (item in selectedItemList){
                for (itemChild in item.childs){
                    cinemaList.add(itemChild.ccid)
                }
            }
            val listString: String = java.lang.String.join(", ", cinemaList)

            authViewModel.setAlert(
                preferences.getUserId(),
                preferences.getCityName(),
                intent.getStringExtra("cid").toString(),
                listString,
                "YES",
                "YES",
                "YES",
                "YES",
                Constant().getDeviceId(this))

        }
    }

    private fun setAlert() {
        authViewModel.setAlertResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

                        // Hit Event
                        try {
                            val bundle = Bundle()
                            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Set Alert")
                            bundle.putString("var_my_pvr_edit_my_watchlist_fav", "")
                            GoogleAnalytics.hitEvent(this, "my_pvr_edit_my_watchlist_favourite", bundle)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                        alert = true
                        setAlertDialog()
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                                //setAlertDialog()
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

    private fun setAlertDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val bindingBottom = SetAlertDialogBinding.inflate(inflater)
        dialog.setContentView(bindingBottom.root)
        alert = true
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        if (whatsappStatusCheck){
            bindingBottom.textView295.hide()
            bindingBottom.cardView12.hide()
        }else{
            bindingBottom.textView295.show()
            bindingBottom.cardView12.show()
        }


        bindingBottom.switch1.isChecked = whatsappStatusCheck
        bindingBottom.switch1.setOnCheckedChangeListener { _, isChecked ->
            timeStamp = (System.currentTimeMillis() / 1000).toString()
            if (isChecked) {
                authViewModel.whatsappOptIn(
                    preferences.getUserId(),
                    preferences.getToken().toString(),
                    timeStamp,
                    getHash(preferences.getUserId() + "|" + preferences.getToken() + "|" + "opt-in" + "|" + timeStamp)
                )
            } else {
                authViewModel.whatsappOptOut(
                    preferences.getUserId(),
                    preferences.getToken().toString(),
                    timeStamp,
                    getHash(preferences.getUserId() + "|" + preferences.getToken() + "|" + "opt-out" + "|" + timeStamp)
                )
            }
        }

        bindingBottom.include21.textView5.setOnClickListener {
            alert = true
            finish()
        }
    }

    private fun allTheater() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output as LinkedTreeMap<*, *>)
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

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun retrieveData(output: LinkedTreeMap<*, *>) {
        _binding?.constraintLayout178?.show()
        val gson = Gson()
        val data = gson.fromJson(JSONObject(output).toString(), BookingRetrievalResponse.Output::class.java)
        theaterAdapter = AlertTheaterAdapter(
            context = this@SetAlertActivity,
            nowShowingList = data.c,
            unableDisable = unableDisable
        ,binding?.toggleButton!!
        ) { item, addToList ->
            if (addToList) {
                selectedItemList.add(item)
            } else {
                selectedItemList.remove(item)
            }

            binding.apply {
                textView280.text = "${selectedItemList.size}/5 Selected"
                if (selectedItemList.size == 0) {
                    include14.textView5.text = "Proceed"
                    include14.textView5.background = getDrawable(R.drawable.grey_curve)
                    include14.textView5.isClickable = false
                } else {
                    include14.textView5.background = getDrawable(R.drawable.yellow_book_curve)
                    include14.textView5.isClickable = true
                }
            }
        }
        binding.theaterRv.adapter = theaterAdapter
    }
}