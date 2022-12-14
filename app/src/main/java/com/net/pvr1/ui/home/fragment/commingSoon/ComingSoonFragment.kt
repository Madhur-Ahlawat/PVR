package com.net.pvr1.ui.home.fragment.commingSoon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityComingSoonBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.commingSoon.adapter.ComingSoonMovieAdapter
import com.net.pvr1.ui.home.fragment.commingSoon.adapter.LanguageAdapter
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.home.fragment.commingSoon.viewModel.ComingSoonViewModel
import com.net.pvr1.ui.movieDetails.comingSoonDetails.ComingSoonDetailsActivity
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import com.net.pvr1.utils.hide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ComingSoonFragment : Fragment(), LanguageAdapter.RecycleViewItemClickListener,
    ComingSoonMovieAdapter.VideoPlay {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityComingSoonBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ComingSoonViewModel by viewModels()
    private var checkLogin: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ActivityComingSoonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().findViewById(R.id.include) as ConstraintLayout).hide()

        authViewModel.comingSoon(preferences.getCityName(), "ALL", "ALL", preferences.getUserId())
        comingSoonApi()
        movedNext()
    }

    private fun movedNext() {
//        val search = this.findViewById(R.id.searchBtn) as ImageView
//        search.setOnClickListener {
//            val intent = Intent(this, SearchComingSoonActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun comingSoonApi() {
        authViewModel.userResponseLiveData.observe(requireActivity()) {
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
        val gridLayout = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        val adapter = LanguageAdapter(output.language, this)
        binding?.recyclerView?.layoutManager = gridLayout
        binding?.recyclerView?.adapter = adapter

        //ComingSoon
        val gridLayout2 = GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
        val comingSoonMovieAdapter =
            ComingSoonMovieAdapter(output.movies,requireActivity(), this, checkLogin)
        binding?.recComSoonMovie?.layoutManager = gridLayout2
        binding?.recComSoonMovie?.adapter = comingSoonMovieAdapter
    }

    override fun onDateClick(comingSoonItem: Any) {

    }

    override fun onDateClick(comingSoonItem: CommingSoonResponse.Output.Movy) {
        val intent = Intent(requireActivity(), ComingSoonDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.masterMovieId)
        startActivity(intent)
    }

    override fun onTrailerClick(comingSoonItem: CommingSoonResponse.Output.Movy) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.videoUrl)
        startActivity(intent)
    }
}