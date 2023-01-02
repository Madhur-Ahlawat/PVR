package com.net.pvr1.ui.location.selectCity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySelectCityBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.location.selectCity.adapter.OtherCityAdapter
import com.net.pvr1.ui.location.selectCity.adapter.PopUpCityAdapter
import com.net.pvr1.ui.location.selectCity.adapter.SearchCityAdapter
import com.net.pvr1.ui.location.selectCity.adapter.SelectCityAdapter
import com.net.pvr1.ui.location.selectCity.response.SelectCityResponse
import com.net.pvr1.ui.location.selectCity.viewModel.SelectCityViewModel
import com.net.pvr1.ui.scanner.bookings.SelectBookingsActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SelectCityActivity : AppCompatActivity(), SearchCityAdapter.RecycleViewItemClickListener,
    OtherCityAdapter.RecycleViewItemClickListenerCity,
    SelectCityAdapter.RecycleViewItemClickListenerSelectCity,
    PopUpCityAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivitySelectCityBinding? = null
    private var loader: LoaderDialog? = null
    private val selectCityViewModel: SelectCityViewModel by viewModels()
    private var filterCityList: ArrayList<SelectCityResponse.Output.Ot>? = null
    private var searchCityAdapter: SearchCityAdapter? = null
    private var list: ArrayList<String> = arrayListOf()

    private var recyclerViewDialog: RecyclerView? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var enableLocation = 0

    private var cityName: String = ""
    private var cityNameMAin: String = ""
    private var from: String = ""
    private var cid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCityBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        cityName = preferences.getCityName()
        // Location City Name
        binding?.txtSelectedCity?.text = preferences.getCityName()
        selectCityViewModel.selectCity(
            preferences.getLatitudeData(),
            preferences.getLongitudeData(),
            preferences.getUserId(),
            "no",
            "no"
        )
        selectCity()
        movedNext()

        //Get Intent  Qr Case
        cid = intent.getStringExtra("cid").toString()
        from = intent.getStringExtra("from").toString()
    }



    fun EditText.hideKeyboard() {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }


    private fun movedNext() {
        binding?.imageView39?.setOnClickListener {
            if (Constant().locationServicesEnabled(this) && Constant.latitude!=0.0 && Constant.longitude!= 0.0) {
                preferences.saveLatitudeData(Constant.latitude.toString())
                preferences.saveLatitudeData(Constant.longitude.toString())

                if (from == "qr") {
                    val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                    intent.putExtra("from", "qr")
                    intent.putExtra("cid", cid)
                    startActivity(intent)
                } else {
                    enableLocation=1
                        selectCityViewModel.selectCity(
                            Constant.latitude.toString(),
                            Constant.longitude.toString(),
                            preferences.getUserId(),
                            "no",
                            "no"
                        )

                }
            }else {
                Constant().enableLocation(this)
            }
        }

        binding?.searchCity?.setOnClickListener {
            binding?.recyclerViewSearchCity?.show()
            binding?.consSelectedLocation?.show()
            binding?.consSelectCity?.hide()
        }

        binding?.imageView35?.setOnClickListener {
            finish()
        }

        binding?.searchCity?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.searchCity?.text?.chars()
                filter(s.toString())

                binding?.searchCity?.setOnEditorActionListener { _, action, _ ->
                    if (action == EditorInfo.IME_ACTION_SEARCH) {
                        binding?.searchCity?.text?.chars()
                        filter(s.toString())
                        binding?.searchCity?.hideKeyboard()
                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener true
                }

            }

            override fun afterTextChanged(s: Editable?) {
                binding?.recyclerViewSearchCity?.show()
                binding?.consSelectCity?.hide()
            }
        })
    }

    private fun selectCity() {
        selectCityViewModel.cityResponseLiveData.observe(this) {
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    override fun onItemClickCitySearch(
        city: ArrayList<SelectCityResponse.Output.Ot>, position: Int
    ) {
        list = arrayListOf(*city[position].subcities.split(",").toTypedArray())
        list.add(0, "All")
        binding?.searchCity?.setText("")
        binding?.txtSelectedCity?.text = city[position].name
        preferences.saveCityName(city[position].name)
        preferences.saveLatitudeData(city[position].lat)
        preferences.saveLongitudeData(city[position].lng)

        if (city[position].subcities.isEmpty()) {
            if (from == "qr") {
                val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                intent.putExtra("from", "qr")
                intent.putExtra("cid", cid)
                startActivity(intent)
            } else {
                val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        } else {
            cityDialog(city[position].name)
        }
    }


    private fun retrieveData(output: SelectCityResponse.Output) {
        preferences.saveCityName(output.cc.name)
        if (enableLocation==1){
            toast(output.cc.name)
            val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            filterCityList = output.ot
            val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            val otherCityAdapter = OtherCityAdapter(output.ot, output, this, this)
            binding?.recyclerViewOtherCity?.layoutManager = gridLayout2
            binding?.recyclerViewOtherCity?.adapter = otherCityAdapter

            val gridLayout = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            val selectCityAdapter = SelectCityAdapter(output.pc, output, this, this)
            binding?.recyclerCity?.layoutManager = gridLayout
            binding?.recyclerCity?.adapter = selectCityAdapter

            val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            searchCityAdapter = SearchCityAdapter(filterCityList!!, output, this, this)
            binding?.recyclerViewSearchCity?.layoutManager = gridLayout3
            binding?.recyclerViewSearchCity?.adapter = searchCityAdapter

        }

    }


    override fun onItemClickCityOtherCity(
        city: ArrayList<SelectCityResponse.Output.Ot>, position: Int
    ) {

        list = arrayListOf(*city[position].subcities.split(",").toTypedArray())
        list.add(0, "All")
        binding?.txtSelectedCity?.text = city[position].name
        preferences.saveCityName(city[position].name)
        preferences.saveLatitudeData(city[position].lat)
        preferences.saveLongitudeData(city[position].lng)

        if (city[position].subcities.isEmpty()) {

            if (from == "qr") {
                val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                intent.putExtra("from", "qr")
                intent.putExtra("cid", cid)
                startActivity(intent)
            } else {
                val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        } else {
            cityDialog(city[position].name)
        }

    }

    override fun onItemClickCityImgCity(
        city: ArrayList<SelectCityResponse.Output.Pc>, position: Int
    ) {
        list = arrayListOf(*city[position].subcities.split(",").toTypedArray())
        list.add(0, "All")
        binding?.txtSelectedCity?.text = city[position].name
        preferences.saveCityName(city[position].name)
        preferences.saveLatitudeData(city[position].lat)
        preferences.saveLongitudeData(city[position].lng)

        if (city[position].subcities.isEmpty()) {
            if (from == "qr") {
                val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                intent.putExtra("from", "qr")
                intent.putExtra("cid", cid)
                startActivity(intent)
            } else {
                val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        } else {
            cityDialog(city[position].name)
        }

    }

    private fun filter(text: String) {
        val filtered: ArrayList<SelectCityResponse.Output.Ot> = ArrayList()
        val filtered1: ArrayList<SelectCityResponse.Output.Ot> = ArrayList()
        // creating a new array list to filter our data.
        // running a for loop to compare elements.

        for (item in filterCityList!!) {

            if (item.name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filtered.add(item)
            }
        }

        if (filtered.isEmpty()) {

            println("filterTextSearch ------->${filtered}")
            binding?.consSelectedLocation?.show()
            binding?.consSelectCity?.hide()
            binding?.recyclerViewSearchCity?.hide()
            binding?.textView124?.show()
            searchCityAdapter?.filterList(filtered1)
        } else {
            println("filterTextSearch1 ------->${filtered}")
            binding?.consSelectedLocation?.show()
            binding?.textView124?.hide()
            binding?.consSelectCity?.hide()
            searchCityAdapter?.filterList(filtered)

        }
    }


    private fun cityDialog(name: String) {
        cityNameMAin = name
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.city_select_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        recyclerViewDialog = dialog.findViewById(R.id.recyclerViewCityDialog)
        val cancel = dialog.findViewById<ImageView>(R.id.imgCross)
        val tittle = dialog.findViewById<TextView>(R.id.textView108)
        tittle.text = preferences.getCityName()

        try {
            val gridLayout4 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val dialogCityAdapter = PopUpCityAdapter(list, this, this, cityName)
            recyclerViewDialog?.layoutManager = gridLayout4
            recyclerViewDialog?.adapter = dialogCityAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onItemClickCityDialog(city: String) {
        if (city == "All") {
            preferences.saveCityName(cityNameMAin)
        } else {
            preferences.saveCityName(city)
        }
        if (from == "qr") {
            val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
            intent.putExtra("from", "qr")
            intent.putExtra("cid", cid)
            startActivity(intent)
        } else {
            val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}