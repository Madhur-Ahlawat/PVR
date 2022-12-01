package com.net.pvr1.ui.selectCity

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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySelectCityBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.adapter.BottomFoodAdapter
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter
import com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter
import com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import com.net.pvr1.ui.selectCity.viewModel.SelectCityViewModel
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SelectCityActivity : AppCompatActivity(), SearchCityAdapter.RecycleViewItemClickListener,
    OtherCityAdapter.RecycleViewItemClickListenerCity,
    SelectCityAdapter.RecycleViewItemClickListenerSelectCity {

    @Inject
    lateinit var preferences: PreferenceManager

    private var binding: ActivitySelectCityBinding? = null
    private var loader: LoaderDialog? = null
    private val selectCityViewModel: SelectCityViewModel by viewModels()

    private var filterCityList: ArrayList<SelectCityResponse.Output.Ot>? = null
    private var searchCityAdapter: SearchCityAdapter? = null
    private var dataList: ArrayList<Any> = ArrayList()

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
        const val REQUEST_CHECK_SETTINGS = 43
    }

    var latitude: String = ""
    var longitude: String = ""
    var currentAddress: String = ""

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCityBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        selectCityViewModel.selectCity(
            preferences.getLatData(),
            preferences.getLangData(),
            preferences.getUserId(),
            "no",
            "no"
        )
        selectCity()

        binding?.searchCity?.setOnClickListener {
            binding?.recyclerViewSearchCity?.show()
            binding?.consSelectCity?.hide()
//            binding?.searchCity?.isFocusable = true
//            binding?.searchCity?.isClickable = true
//            binding?.consSelectedLocation?.hide()
        }
        movedNext()

        // Location City Name
        binding?.txtSelectedCity?.text = preferences.getCityName()
    }

    fun EditText.hideKeyboard() {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }


    private fun movedNext() {
        binding?.imageView35?.setOnClickListener {
            finish()
        }

        binding?.searchCity?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding?.recyclerViewSearchCity?.show()
                binding?.consSelectCity?.hide()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.recyclerViewSearchCity?.show()
                binding?.consSelectCity?.hide()

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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    override fun onItemClickCitySearch(
        city: ArrayList<SelectCityResponse.Output.Ot>,
        position: Int
    ) {

        binding?.searchCity?.setText("")
        binding?.txtSelectedCity?.text = city[position].name
        preferences.cityName(city[position].name)
        preferences.latData(city[position].lat)
        preferences.langData(city[position].lng)
        val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
        startActivity(intent)

//        binding?.searchCity?.isFocusable = false
//        binding?.searchCity?.isClickable = false
//        binding?.txtSelectedCity?.text = city[position].name
//        preferences.putString(Constant.CITY_NAME, city[position].name)
//        binding?.recyclerViewSearchCity?.hide()
//        binding?.consSelectCity?.show()
//        binding?.consSelectedLocation?.show()

    }


    private fun retrieveData(output: SelectCityResponse.Output) {

        filterCityList = output.ot

        val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val otherCityAdapter = OtherCityAdapter(output.ot, this, this)
        binding?.recyclerViewOtherCity?.layoutManager = gridLayout2
        binding?.recyclerViewOtherCity?.adapter = otherCityAdapter


        val gridLayout = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val selectCityAdapter = SelectCityAdapter(output.pc, this, this)
        binding?.recyclerCity?.layoutManager = gridLayout
        binding?.recyclerCity?.adapter = selectCityAdapter

        val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        searchCityAdapter = SearchCityAdapter(output.ot, this, this)
        binding?.recyclerViewSearchCity?.layoutManager = gridLayout3
        binding?.recyclerViewSearchCity?.adapter = searchCityAdapter
    }


    override fun onItemClickCityOtherCity(
        city: ArrayList<SelectCityResponse.Output.Ot>,
        position: Int
    ) {
//        binding?.consSelectedLocation?.show()
        binding?.txtSelectedCity?.text = city[position].name
        preferences.cityName(city[position].name)
        preferences.latData(city[position].lat)
        preferences.langData(city[position].lng)
        val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClickCityImgCity(
        city: ArrayList<SelectCityResponse.Output.Pc>,
        position: Int) {
//      binding?.consSelectedLocation?.show()
        cityDialog()
        binding?.txtSelectedCity?.text = city[position].name
        preferences.cityName(city[position].name)
        preferences.latData(city[position].lat)
        preferences.langData(city[position].lng)

//        val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
//        startActivity(intent)

    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<SelectCityResponse.Output.Ot> = ArrayList()
        // running a for loop to compare elements.
        for (item in filterCityList!!) {

            // checking if the entered string matched with any item of our recycler view.
            if (item.name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {

//            binding?.txtNoRecord?.show()
//            binding?.recyclerView?.hide()
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()


        } else {

//            binding?.txtNoRecord?.hide()
//            binding?.recyclerView?.show()
            // at last we are passing that filtered
            // list to our adapter class.
            searchCityAdapter?.filterList(filteredlist)
        }
    }

   private fun cityDialog(){

       val dialog = Dialog(this)
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
       dialog.setContentView(R.layout.city_select_dialog)
       dialog.window!!.setLayout(
           ViewGroup.LayoutParams.MATCH_PARENT,
           ViewGroup.LayoutParams.WRAP_CONTENT
       )
       dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
       dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
       dialog.window!!.setGravity(Gravity.BOTTOM)
       dialog.show()

       val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewCityDialog)
       val cancel = dialog.findViewById<ImageView>(R.id.imgCross)
       val tittle = dialog.findViewById<TextView>(R.id.textView108)

//       val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
//       bottomFoodAdapter = BottomFoodAdapter(comingSoonItem, this, this)
//       recyclerView.layoutManager = layoutManager
//       recyclerView.adapter = bottomFoodAdapter

       cancel.setOnClickListener {
           dialog.dismiss()
       }

   }


}