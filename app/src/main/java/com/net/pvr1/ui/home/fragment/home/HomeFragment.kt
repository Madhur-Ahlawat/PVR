package com.net.pvr1.ui.home.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentHomeBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaChildAdapter
import com.net.pvr1.ui.home.fragment.commingSoon.viewModel.ComingSoonViewModel
import com.net.pvr1.ui.home.fragment.home.adapter.CategoryHomeAdapter
import net.seifhadjhassen.recyclerviewpager.PagerModel


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private var loader: LoaderDialog? = null
    private val authViewModel by activityViewModels<ComingSoonViewModel>()
    private lateinit var preferences: AppPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {

        // Inflate the layout for this fragment
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sliderAuto()

    }

    private fun movedNext(view: View) {
        // Select City
    }

    // Slider
    private fun sliderAuto() {

        binding?.pagerSlider?.addItem(PagerModel(R.drawable.img, "", requireContext()))
        binding?.pagerSlider?.addItem(PagerModel(R.drawable.app_icon, "", requireContext()))
        binding?.pagerSlider?.addItem(PagerModel(R.drawable.img, "", requireContext()))
        binding?.pagerSlider?.addItem(PagerModel(R.drawable.app_icon, "", requireContext()))

        binding?.pagerSlider?.start()

        binding?.pagerSlider?.setOnItemClickListener {
            Toast.makeText(requireContext(), "Click Position " + it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun categoryAdapter() {
        //Call Category Adapter
//        val gridLayout2 = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
//        val comingSoonMovieAdapter = CategoryHomeAdapter(cinemaItem.m, context)
//        binding?.recyclerViewCategory?.layoutManager = gridLayout2
//        binding?.recyclerViewCategory?.adapter = comingSoonMovieAdapter
    }

}