package com.net.pvr1.ui.movieDetails.comingSoonDetails.setAlert

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
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySetAlertBinding
import com.net.pvr1.databinding.SetAlertDialogBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr1.ui.movieDetails.comingSoonDetails.setAlert.adapter.AlertTheaterAdapter
import com.net.pvr1.ui.movieDetails.comingSoonDetails.setAlert.viewModel.SetAlertViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
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
    private val selectedItemList: MutableList<BookingRetrievalResponse.Output.C> = mutableListOf()
    private val cinemaList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySetAlertBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        authViewModel.allTheater(preferences.getCityName(), preferences.getLatitudeData(), preferences.getLongitudeData(), preferences.getUserId(), "")
        allTheater()
        movedNext()
        setUiValue()
        setAlert()
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun setUiValue() {

        binding.toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            unableDisable.value = isChecked
        }

        binding.apply {
            include14.textView5.background = getDrawable(R.drawable.alert_curve_ui_unselect)
            include14.textView5.isClickable = false
        }

        unableDisable.observe(this) {
            if (it) {
                binding.textView280.text = "You have selected all the theaters"
                binding.include14.textView5.background = getDrawable(R.drawable.yellow_book_curve)
            } else {
                binding.textView280.text = "${selectedItemList.size}/5 Selected"
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
            authViewModel.setAlert(preferences.getUserId(), preferences.getCityName(), listString,intent.getStringExtra("cid").toString(), "YES","YES","YES","YES",Constant().getDeviceId(this))

        }
    }

    private fun setAlert() {
        authViewModel.setAlertResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        setAlertDialog()
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                                setAlertDialog()
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
                    loader = LoaderDialog(R.string.pleasewait)
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
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
        bindingBottom.include21.textView5.setOnClickListener {
            finish()
        }
    }

    private fun allTheater() {
        authViewModel.userResponseLiveData.observe(this) {
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

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun retrieveData(output: BookingRetrievalResponse.Output) {
        theaterAdapter = AlertTheaterAdapter(
            context = this@SetAlertActivity,
            nowShowingList = output.c,
            unableDisable = unableDisable
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