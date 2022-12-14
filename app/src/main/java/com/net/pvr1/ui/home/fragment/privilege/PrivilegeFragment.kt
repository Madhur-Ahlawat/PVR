package com.net.pvr1.ui.home.fragment.privilege

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentPrivilegeBinding
import com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeTypeAdapter
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.PrivilegeHomeResponseConst
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PrivilegeFragment : Fragment(), PrivilegeTypeAdapter.RecycleViewItemClickListener {
    private var binding: FragmentPrivilegeBinding? = null

    @Inject
    lateinit var preferences: PreferenceManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPrivilegeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().printLog("response!Login--->${PrivilegeHomeResponseConst}")
        if (!preferences.getIsLogin()) {

        } else {

        }
//        notLoginCategoryDataLoad()
    }

    private fun notLoginCategoryDataLoad() {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding?.recyclerView35)
        val gridLayout =
            GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerView35?.layoutManager =
            LinearLayoutManager(requireActivity())
        val adapter =
            PrivilegeTypeAdapter(PrivilegeHomeResponseConst?.pinfo!!, requireActivity(),this,binding?.recyclerView35)
        binding?.recyclerView35?.layoutManager = gridLayout
        binding?.recyclerView35?.adapter = adapter
    }

    override fun privilegeTypeScroll(
        nowShowingList: ArrayList<PrivilegeHomeResponse.Output.Pinfo>,
        position: Int
    ) {

        context.toast("data--->-pos-->${position}")
        when (nowShowingList[position].ptype) {
            "P" -> {
                binding?.include15?.show()
                binding?.include16?.hide()
                binding?.include15Logout?.salted?.text =
                    requireActivity().getString(R.string.p1TxtOne)
                //points
                binding?.include15Logout?.points?.text =
                    requireActivity().getString(R.string.p2TxtOne)
            }
            "PP" -> {

                binding?.include15?.hide()
                binding?.include16?.show()
                binding?.include15Logout?.salted?.text = ""
                //points
                binding?.include15Logout?.points?.text = ""
            }
            "PPP" -> {
                binding?.include15Logout?.salted?.text =
                    requireActivity().getString(R.string.p3TxtOne)
                //points
                binding?.include15Logout?.points?.text =
                    requireActivity().getString(R.string.p3TxtOne)
            }
        }
    }


}