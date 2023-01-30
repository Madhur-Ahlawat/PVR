package com.net.pvr1.ui.watchList

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityWatchListBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.movieDetails.comingSoonDetails.ComingSoonDetailsActivity
import com.net.pvr1.ui.watchList.adapter.WatchListAdapter
import com.net.pvr1.ui.watchList.response.WatchListResponse
import com.net.pvr1.ui.watchList.viewModel.WatchListViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WatchListActivity : AppCompatActivity(),WatchListAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityWatchListBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: WatchListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchListBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunctions()
    }

    private fun manageFunctions() {
        authViewModel.watchlist(preferences.getUserId(),preferences.getCityName(),Constant().getDeviceId(this))
        watchListData()
        deleteAlert()
        movedNext()
        getShimmerData()
    }

    private fun movedNext() {
        //back
        binding?.include17?.imageView58?.setOnClickListener {
            finish()
        }
        //title
        binding?.include17?.textView108?.text=getString(R.string.watchlist)
    }

    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    private fun watchListData() {
        authViewModel.liveDataScope.observe(this) {
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

    private fun retrieveData(output: ArrayList<WatchListResponse.Output>) {
        //shimmer
        binding?.constraintLayout164?.hide()
        //details
        binding?.constrainLayout165?.show()

        val gridLayout =
            GridLayoutManager(this@WatchListActivity, 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView53?.layoutManager = LinearLayoutManager(this@WatchListActivity)
        val adapter = WatchListAdapter(output, this, this)
        binding?.recyclerView53?.layoutManager = gridLayout
        binding?.recyclerView53?.adapter = adapter
    }
    override fun itemClick(comingSoonItem: WatchListResponse.Output) {
        val intent = Intent(this, ComingSoonDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.moviecode)
        startActivity(intent)
    }


    override fun deleteAlertClick(comingSoonItem: WatchListResponse.Output) {
        authViewModel.deleteAlert(preferences.getUserId(),comingSoonItem.moviecode,comingSoonItem.city)
    }

    private fun deleteAlert() {
        authViewModel.deleteAlertLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        authViewModel.watchlist(preferences.getUserId(),preferences.getCityName(),Constant().getDeviceId(this))
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

}