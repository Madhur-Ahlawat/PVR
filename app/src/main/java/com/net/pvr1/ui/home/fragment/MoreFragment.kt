package com.net.pvr1.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.net.pvr1.databinding.FragmentMoreBinding
import com.net.pvr1.ui.myBookings.MyBookingsActivity


class MoreFragment : Fragment() {
    private var binding: FragmentMoreBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movedNext()
    }

    private fun movedNext() {
        //MyBookings
        binding?.myBookings?.setOnClickListener {
            val intent = Intent(requireContext(), MyBookingsActivity::class.java)
            startActivity(intent)
        }
    }
}