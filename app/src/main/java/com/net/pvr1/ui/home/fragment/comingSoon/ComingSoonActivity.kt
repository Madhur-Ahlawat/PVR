package com.net.pvr1.ui.home.fragment.comingSoon

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityComingSoonBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.comingSoon.adapter.ComingSoonMovieAdapter
import com.net.pvr1.ui.home.fragment.comingSoon.adapter.LanguageAdapter
import com.net.pvr1.ui.home.fragment.comingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.home.fragment.comingSoon.viewModel.ComingSoonViewModel
import com.net.pvr1.ui.movieDetails.comingSoonDetails.ComingSoonDetailsActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.di.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ComingSoonActivity : AppCompatActivity(), LanguageAdapter.RecycleViewItemClickListener,
    ComingSoonMovieAdapter.VideoPlay {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityComingSoonBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ComingSoonViewModel by viewModels()
    private var checkLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComingSoonBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        authViewModel.comingSoon("Delhi-NCR", "ALL", "ALL", "")
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
        authViewModel.userResponseLiveData.observe(this) {
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: CommingSoonResponse.Output) {
        //All Language
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val adapter = LanguageAdapter(output.language, this)
        binding?.recyclerView?.layoutManager = gridLayout
        binding?.recyclerView?.adapter = adapter

        //ComingSoon
        val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val comingSoonMovieAdapter =
            ComingSoonMovieAdapter(output.movies,this, this, checkLogin)
        binding?.recComSoonMovie?.layoutManager = gridLayout2
        binding?.recComSoonMovie?.adapter = comingSoonMovieAdapter
    }

    override fun onDateClick(comingSoonItem: Any) {
        Toast.makeText(this, comingSoonItem.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onDateClick(comingSoonItem: CommingSoonResponse.Output.Movy) {
        val intent = Intent(this, ComingSoonDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.masterMovieId)
        startActivity(intent)
    }

    override fun onTrailerClick(comingSoonItem: CommingSoonResponse.Output.Movy) {

    }
}