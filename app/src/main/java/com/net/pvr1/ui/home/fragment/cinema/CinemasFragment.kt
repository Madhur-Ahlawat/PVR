package com.net.pvr1.ui.home.fragment.cinema

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentCinemasBinding
import com.net.pvr1.ui.cinemaSession.CinemaSessionActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.home.fragment.cinema.viewModel.CinemaViewModel
import com.net.pvr1.ui.search.searchHome.SearchHomeActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CinemasFragment : Fragment(), CinemaAdapter.Direction, CinemaAdapter.Location,
    CinemaAdapter.SetPreference {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCinemasBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        (requireActivity().findViewById(R.id.notify) as ImageView).hide()
        (requireActivity().findViewById(R.id.locationBtn) as ImageView).hide()
        (requireActivity().findViewById(R.id.scanQr) as ImageView).hide()
        (requireActivity().findViewById(R.id.textView2) as TextView).show()
        (requireActivity().findViewById(R.id.searchBtn) as ImageView).hide()
        (requireActivity().findViewById(R.id.txtCity) as TextView).show()
        (requireActivity().findViewById(R.id.searchCinema) as ImageView).show()
        //Click Search
        (requireActivity().findViewById(R.id.searchCinema) as ImageView).setOnClickListener {
            val intent = Intent(requireActivity(), SearchHomeActivity::class.java)
            startActivity(intent)
        }
        cinemaApi()
        setPreference()
        getLocation()
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
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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
                        if (addresses?.isNotEmpty()!!) {
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
                Toast.makeText(requireActivity(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    //setPreference
    private fun setPreference() {
        authViewModel.cinemaPreferenceResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        context.printLog(it.data.msg)

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
                }
            }
        }

    }

    private fun retrieveData(output: CinemaResponse.Output) {
        val gridLayout2 = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        val comingSoonMovieAdapter =
            CinemaAdapter(output.c, requireActivity(), this, this, this, preferences.getIsLogin())
        binding?.recyclerCinema?.layoutManager = gridLayout2
        binding?.recyclerCinema?.adapter = comingSoonMovieAdapter
    }

    override fun onDirectionClick(comingSoonItem: CinemaResponse.Output.C) {
        Constant().openMap(requireActivity(), comingSoonItem.lat, comingSoonItem.lang)
    }

    override fun onCinemaClick(comingSoonItem: CinemaResponse.Output.C) {
        val intent = Intent(requireActivity(), CinemaSessionActivity::class.java)
        intent.putExtra("cid", comingSoonItem.cId.toString())
        intent.putExtra("lat", comingSoonItem.lat)
        intent.putExtra("lang", comingSoonItem.lang)
        intent.putExtra("cityName", cityName)
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
}