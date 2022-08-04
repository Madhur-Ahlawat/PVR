package com.net.pvr1.ui.home.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentHomeBinding
import com.net.pvr1.databinding.FragmentMoreBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.home.fragment.commingSoon.viewModel.ComingSoonViewModel


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private var loader: LoaderDialog? = null
    private val authViewModel by activityViewModels<ComingSoonViewModel>()
    private lateinit var preferences: AppPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
// Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun movedNext(view: View){
        // Select City


    }

}