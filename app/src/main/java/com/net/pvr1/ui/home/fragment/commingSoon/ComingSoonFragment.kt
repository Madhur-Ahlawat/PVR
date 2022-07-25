package com.net.pvr1.ui.home.fragment.commingSoon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentComingSoonBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.commingSoon.adapter.ComingSoonMovieAdapter
import com.net.pvr1.ui.home.fragment.commingSoon.adapter.LanguageAdapter
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.home.fragment.commingSoon.viewModel.ComingSoonViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult


class ComingSoonFragment : Fragment(), LanguageAdapter.RecycleViewItemClickListener,
    ComingSoonMovieAdapter.VideoPlay {
    private var binding: FragmentComingSoonBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel by activityViewModels<ComingSoonViewModel>()
    private lateinit var preferences: AppPreferences
    private var checkLogin: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentComingSoonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = AppPreferences()
        checkLogin=preferences.getBoolean(Constant.IS_LOGIN)
        authViewModel.comingSoon("Delhi-NCR", "ALL", "ALL", "")
        comingSoonApi()
    }

    private fun comingSoonApi() {
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

    private fun retrieveData(output: CommingSoonResponse.Output) {
        //All Language
        val gridLayout = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        val adapter = LanguageAdapter(output.language, this)
        binding?.recyclerView?.layoutManager = gridLayout
        binding?.recyclerView?.adapter = adapter

        //ComingSoon
        val gridLayout2 = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        val comingSoonMovieAdapter = ComingSoonMovieAdapter(output.movies, requireActivity(), this,checkLogin)
        binding?.recComSoonMovie?.layoutManager = gridLayout2
        binding?.recComSoonMovie?.adapter = comingSoonMovieAdapter

    }

    override fun onDateClick(comingSoonItem: Any) {
        Toast.makeText(requireContext(), comingSoonItem.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onDateClick(comingSoonItem: String) {

    }
}