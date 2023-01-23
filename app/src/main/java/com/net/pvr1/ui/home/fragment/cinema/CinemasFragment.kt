package com.net.pvr1.ui.home.fragment.cinema

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentCinemasBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.cinemaSession.CinemaSessionActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.home.fragment.cinema.viewModel.CinemaViewModel
import com.net.pvr1.ui.home.fragment.home.adapter.PromotionAdapter
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.location.selectCity.SelectCityActivity
import com.net.pvr1.ui.search.searchCinema.SearchCinemaActivity
import com.net.pvr1.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.io.IOException
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CinemasFragment : Fragment(), CinemaAdapter.Direction, CinemaAdapter.Location,
    CinemaAdapter.SetPreference, StoriesProgressView.StoriesListener {
    private var binding: FragmentCinemasBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel by activityViewModels<CinemaViewModel>()

    @Inject
    lateinit var preferences: PreferenceManager
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private var lat = ""
    private var lng = ""
    private var cityName = "Delhi-NCR"

    // story board
    private var bannerShow = 0
    private var pressTime = 0L
    private var limit = 500L
    private var counterStory = 0
    private var currentPage = 1
    private var bannerModelsMain: ArrayList<CinemaResponse.Output.Pu> = ArrayList()
    private var ivBanner: ImageView? = null
    private var ivCross: ImageView? = null
    private var skip: View? = null
    private var reverse: View? = null
    private var tvButton: TextView? = null
    private var ivPlay: LinearLayout? = null
    private var rlBanner: RelativeLayout? = null
    private var stories: StoriesProgressView? = null

    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCinemasBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        tvButton = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.tv_button))
        ivBanner = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.ivBanner))
        ivPlay = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.ivPlay))
        skip = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.skip))
        reverse = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.reverse))
        ivCross = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.ivCross))
        rlBanner = (requireActivity().findViewById(R.id.RlBanner))
        stories = (requireActivity().findViewById<RelativeLayout?>(R.id.bannerLayout)
            .findViewById(R.id.stories))

        if (stories == null) {
            stories?.destroy()
        }

        // functions
        manageFunction()
    }

    private fun manageFunction() {
        //City Name
        binding?.txtCity?.text = preferences.getCityName()
        //Shimmer
        getShimmerData()
        //internet Check
        broadcastReceiver = NetworkReceiver()
        broadcastIntent()

        cinemaApi()
        getLocation()
        movedNext()
    }

    private fun movedNext() {
//        Search
        binding?.searchBtn?.setOnClickListener {
            val intent = Intent(requireActivity(), SearchCinemaActivity::class.java)
            startActivity(intent)
        }

        //Select City
        binding?.txtCity?.setOnClickListener {
            val intent = Intent(requireActivity(), SelectCityActivity::class.java)
            startActivity(intent)
        }

        //banner
        ivCross?.setOnClickListener {
            rlBanner?.hide()
        }
    }

    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), permissionId
        )
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                    try {
                        val addresses = location?.longitude?.let {
                            location.latitude.let { it1 ->
                                geocoder.getFromLocation(
                                    it1, it, 1
                                )
                            }
                        }
                        if (addresses?.isNotEmpty() == true) {
                            val currentAddress: String = addresses[0].locality
                            preferences.cityNameCinema(currentAddress)
                            lat = location.latitude.toString()
                            lng = location.longitude.toString()
                            authViewModel.cinema(cityName, lat, lng, preferences.getUserId(), "")
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    //CinemaData
    private fun cinemaApi() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: CinemaResponse.Output) {
//        Design
        binding?.constraintLayout160?.show()

//        Shimmer
        binding?.constraintLayout146?.hide()

//        List
        val gridLayout2 = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        val comingSoonMovieAdapter =
            CinemaAdapter(output.c, requireActivity(), this, this, this, preferences.getIsLogin())
        binding?.recyclerCinema?.layoutManager = gridLayout2
        binding?.recyclerCinema?.adapter = comingSoonMovieAdapter

//        PlaceHolder
        if (output.pu.isNotEmpty()) {
            binding?.constraintLayout161?.show()
            initBanner(output.pu)
        } else {
            binding?.constraintLayout161?.hide()
        }

        //Promotion
        if (output.ph.isNotEmpty()) updatePH(output.ph)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBanner(bannerModels: ArrayList<CinemaResponse.Output.Pu>) {
        bannerShow += 1
        bannerModelsMain = bannerModels
        if ((bannerModels != null) && bannerModels.isNotEmpty()) {
            rlBanner?.show()
            stories?.setStoriesCount(bannerModels.size) // <- set stories
            stories?.setStoryDuration(5000L) // <- set a story duration
            stories?.setStoriesListener(this) // <- set listener
            stories?.startStories() // <- start progress
            counterStory = 0
            if (!TextUtils.isEmpty(bannerModels[counterStory].i)) {
                Picasso.get().load(bannerModels[counterStory].i).into(ivBanner!!, object :
                    Callback {
                    override fun onSuccess() {
                        rlBanner?.show()
                        //  storiesProgressView.startStories(); // <- start progress
                    }

                    override fun onError(e: Exception?) {}
                })
            }

            reverse?.setOnClickListener { stories?.reverse() }
            reverse?.setOnTouchListener(onTouchListener)
            showButton(bannerModels[0])
            skip?.setOnClickListener { stories?.skip() }
            skip?.setOnTouchListener(onTouchListener)
            tvButton?.setOnClickListener {
                rlBanner?.hide()

                if (bannerModels != null && bannerModels.size > 0 && bannerModels[counterStory].type
                        .equals("image", ignoreCase = true)
                ) {
                    if (bannerModels[counterStory].redirectView.equals(
                            "DEEPLINK",
                            ignoreCase = true
                        )
                    ) {
                        if (bannerModels[counterStory].redirect_url != null && !bannerModels[counterStory].redirect_url
                                .equals("", ignoreCase = true)
                        ) {
                            if (bannerModels[counterStory].redirect_url
                                    .toLowerCase(Locale.ROOT).contains("/loyalty/home")
                            ) {
//                                if (context is PCLandingActivity) (context as PCLandingActivity).PriviegeFragment(
//                                    "C"
//                                )
                            } else {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(
                                        bannerModels[counterStory].redirect_url
                                            .replace("https", "app").replace("http", "app")
                                    )
                                )
                                startActivity(intent)
                            }
                        }
                    } else if (bannerModels[counterStory].redirectView
                            .equals("INAPP", ignoreCase = true)
                    ) {
                        if (bannerModels[counterStory].redirect_url != null && !bannerModels[counterStory].redirect_url
                                .equals("", ignoreCase = true)
                        ) {
//                            val intent = Intent(context, PrivacyActivity::class.java)
//                            intent.putExtra("url", bannerModels[counterStory].redirect_url)
//                            intent.putExtra(PCConstants.IS_FROM, 2000)
//                            intent.putExtra("title", bannerModels[counterStory].name)
//                            startActivity(intent)
                        }
                    } else if (bannerModels[counterStory].redirectView
                            .equals("WEB", ignoreCase = true)
                    ) {
                        if (bannerModels[counterStory].redirect_url != null && !bannerModels[counterStory].redirect_url
                                .equals("", ignoreCase = true)
                        ) {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(bannerModels[counterStory].redirect_url)
                            )
                            startActivity(intent)
                        }
                    }
                }

            }

            (requireActivity().findViewById(R.id.bannerLayout) as RelativeLayout).show()
            ivPlay?.setOnClickListener {
                rlBanner?.hide()
                if (bannerModels != null && bannerModels.size > 0 && bannerModels[counterStory].type.equals(
                        "video",
                        ignoreCase = true
                    )

                ) {
                }
            }

        } else {
            rlBanner?.hide()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                stories?.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                stories?.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }


    private fun showButton(bannerModel: CinemaResponse.Output.Pu) {
        if (bannerModel.type
                .contains("VIDEO") && bannerModel.trailerUrl != null && !bannerModel.trailerUrl
                .contains("")
        ) {
            ivPlay?.visibility = View.VISIBLE
            tvButton?.visibility = View.GONE
        } else if (bannerModel.type.contains("IMAGE") && !bannerModel.redirect_url
                .contains("")
        ) {
            ivPlay?.hide()
            tvButton?.text = bannerModel.buttonText
            tvButton?.show()
        } else {
            ivPlay?.hide()
            tvButton?.hide()
        }
    }


    override fun onNext() {
        try {
            if (!TextUtils.isEmpty(bannerModelsMain[counterStory].i)) {
                ++counterStory
                showButton(bannerModelsMain[counterStory])
                ivBanner?.let {
                    Glide.with(this).load(bannerModelsMain[counterStory].i).into(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onPrev() {
        if (counterStory - 1 < 0) return
        if (!TextUtils.isEmpty(bannerModelsMain[counterStory].i)) {
            --counterStory
            showButton(bannerModelsMain[counterStory])
            ivBanner?.let {
                Glide
                    .with(this)
                    .load(bannerModelsMain[counterStory].i)
                    .into(it)
            }
        }
    }

    override fun onComplete() {
        stories?.destroy()
        stories?.startStories()
        currentPage = 0
        rlBanner?.hide()
    }


    override fun onDirectionClick(comingSoonItem: CinemaResponse.Output.C) {
        Constant().openMap(requireActivity(), comingSoonItem.lat, comingSoonItem.lang)
    }

    override fun onCinemaClick(comingSoonItem: CinemaResponse.Output.C) {
        val intent = Intent(requireActivity(), CinemaSessionActivity::class.java)
        intent.putExtra("cid", comingSoonItem.cId.toString())
        intent.putExtra("lat", comingSoonItem.lat)
        intent.putExtra("lang", comingSoonItem.lang)
        intent.putExtra("addressCinema", "yes")
        startActivity(intent)
    }

    override fun onPreferenceClick(comingSoonItem: CinemaResponse.Output.C, rowIndex: Boolean) {
        authViewModel.cinemaPreference(
            preferences.getUserId(),
            comingSoonItem.cId.toString(),
            rowIndex,
            "t",
            Constant().getDeviceId(requireActivity())
        )
    }

    //Internet Check
    private fun broadcastIntent() {
        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    //    PlaceHolder
    private fun updatePH(phd: ArrayList<HomeResponse.Ph>?) {
        if (phd != null && phd.size > 0) {
            binding?.include39?.placeHolderView?.show()
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            binding?.include39?.recyclerPromotion?.layoutManager = layoutManager
            binding?.include39?.recyclerPromotion?.onFlingListener = null
            snapHelper.attachToRecyclerView(binding?.include39?.recyclerPromotion!!)
            binding?.include39?.recyclerPromotion?.layoutManager = layoutManager
            val adapter = PromotionAdapter(requireActivity(), phd)
            binding?.include39?.recyclerPromotion?.adapter = adapter
            if (phd.size > 1) {
                val speedScroll = 5000
                val handler = Handler()
                val runnable: Runnable = object : Runnable {
                    var count = 0
                    var flag = true
                    override fun run() {
                        if (count < adapter.itemCount) {
                            if (count == adapter.itemCount - 1) {
                                flag = false
                            } else if (count == 0) {
                                flag = true
                            }
                            if (flag) count++ else count--
                            binding?.include39?.recyclerPromotion?.smoothScrollToPosition(
                                count
                            )
                            handler.postDelayed(this, speedScroll.toLong())
                        }
                    }
                }
                handler.postDelayed(runnable, speedScroll.toLong())
            }
        } else {
            binding?.include39?.placeHolderView?.hide()
        }
    }
}