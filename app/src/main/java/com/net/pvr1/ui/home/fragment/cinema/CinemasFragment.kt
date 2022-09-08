package com.net.pvr1.ui.home.fragment.cinema

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentCinemasBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.cinemaSession.CinemaSessionActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.home.fragment.cinema.viewModel.CinemaViewModel
import com.net.pvr1.ui.search.searchCinema.SearchCinemaActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.hide

class CinemasFragment : Fragment(), CinemaAdapter.Direction, CinemaAdapter.Location {
    private var binding: FragmentCinemasBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel by activityViewModels<CinemaViewModel>()
    private lateinit var preferences: AppPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCinemasBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = AppPreferences()
        authViewModel.cinema("Delhi-NCR", "0.0", "0.0", "", "")
        (requireActivity().findViewById(R.id.notify) as ImageView).hide()
        (requireActivity().findViewById(R.id.constraintLayout55) as ConstraintLayout).hide()

        cinemaApi()
        movedNext()
    }

    private fun movedNext() {
       val search=requireActivity().findViewById(R.id.searchBtn) as ImageView
        search.setOnClickListener {
            val intent = Intent(requireActivity(), SearchCinemaActivity::class.java)
            startActivity(intent)
        }
    }

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
                            positiveClick = {
                            },
                            negativeClick = {
                            })
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
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: CinemaResponse.Output) {
        val gridLayout2 = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        val comingSoonMovieAdapter = CinemaAdapter(output.c, requireActivity(), this, this)
        binding?.recyclerCinema?.layoutManager = gridLayout2
        binding?.recyclerCinema?.adapter = comingSoonMovieAdapter
    }

    override fun onDirectionClick(comingSoonItem: CinemaResponse.Output.C) {
        val strUri =
            "http://maps.google.com/maps?q=loc:" + comingSoonItem.lat+ "," +  comingSoonItem.lang + " (" + "Label which you want" + ")"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)
    }

    override fun onLocationClick(comingSoonItem: CinemaResponse.Output.C) {
        val intent = Intent(requireActivity(), CinemaSessionActivity::class.java)
        intent.putExtra("cid",comingSoonItem.cId.toString())
        intent.putExtra("lat", comingSoonItem.lat)
        intent.putExtra("lang", comingSoonItem.lang)
        startActivity(intent)
    }

}