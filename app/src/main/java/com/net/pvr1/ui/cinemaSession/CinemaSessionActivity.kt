package com.net.pvr1.ui.cinemaSession

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityCinemaSessionBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionCinParentAdapter
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionDaysAdapter
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionLanguageAdapter
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionNearTheaterAdapter
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.cinemaSession.viewModel.CinemaSessionViewModel
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CinemaSessionActivity : AppCompatActivity(),
    CinemaSessionDaysAdapter.RecycleViewItemClickListenerCity,
    CinemaSessionLanguageAdapter.RecycleViewItemClickListenerCity,
    CinemaSessionCinParentAdapter.RecycleViewItemClickListenerCity,
    CinemaSessionNearTheaterAdapter.RecycleViewItemClickListenerCity {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityCinemaSessionBinding? = null
    private val authViewModel: CinemaSessionViewModel by viewModels()
    private var loader: LoaderDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSessionBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        //Data Load
        authViewModel.cinemaSession(
            "Delhi-NCR",
            intent.getStringExtra("cid").toString(),
            intent.getStringExtra("lat").toString(),
            intent.getStringExtra("lang").toString(),
            "0",
            "NA",
            "ALL",
            "ALL",
            "ALL",
            "ALL",
            "ALL",
            "ALL",
            "ALL",
            "no",
            "ALL",
            "ALL"
        )
        //Theater
        authViewModel.cinemaNearTheater("Delhi-NCR", "0", "0", "192")
        cinemaSessionDataLoad()
        cinemaNearTheaterLoad()
    }

    private fun cinemaSessionDataLoad() {
        authViewModel.cinemaSessionLiveData.observe(this) {
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun cinemaNearTheaterLoad() {
        authViewModel.cinemaSessionNearTheaterLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveTheaterData(it.data.output)
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
//                    loader = LoaderDialog(R.string.pleasewait)
//                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }


    private fun retrieveTheaterData(output: CinemaNearTheaterResponse.Output) {
        //recycler Cinemas
        val gridLayout4 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val cinemaSessionNearTheaterAdapter =
            CinemaSessionNearTheaterAdapter(output.c, this, this)
        binding?.recyclerView18?.layoutManager = gridLayout4
        binding?.recyclerView18?.adapter = cinemaSessionNearTheaterAdapter
    }

    private fun retrieveData(output: CinemaSessionResponse.Output) {
        //title
        binding?.textView84?.text = output.cn
        //address
        binding?.textView81?.text = output.addr
        //Image
        Glide.with(this@CinemaSessionActivity)
            .load(output.imob)
            .error(R.drawable.app_icon)
            .into(binding?.imageView40!!)
        //Direction
        binding?.view64?.setOnClickListener {
            val strUri =
                "http://maps.google.com/maps?q=loc:" + output.lat + "," + output.lang + " (" + "Label which you want" + ")"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )
            startActivity(intent)
        }
        //Distance
        binding?.textView86?.text = output.d
        //Shows
        binding?.textView85?.text = "40 Dummy"

        //recycler Days
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val cinemaSessionDaysAdapter = CinemaSessionDaysAdapter(output.bd, this, this)
        binding?.recyclerView13?.layoutManager = gridLayout2
        binding?.recyclerView13?.adapter = cinemaSessionDaysAdapter

        //recycler Language
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val cinemaSessionLanguageAdapter = CinemaSessionLanguageAdapter(output.lngs, this, this)
        binding?.recyclerView14?.layoutManager = gridLayout
        binding?.recyclerView14?.adapter = cinemaSessionLanguageAdapter

        //recycler Cinemas
        val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val cinemaSessionCinParentAdapter =
            CinemaSessionCinParentAdapter(output.childs, this, this)
        binding?.recyclerView14?.layoutManager = gridLayout3
        binding?.recyclerView14?.adapter = cinemaSessionCinParentAdapter


        binding?.textView99?.text = output.cn

    }

    override fun dateClick(comingSoonItem: CinemaSessionResponse.Output.Bd) {

    }

    override fun languageClick(comingSoonItem: String) {

    }

    override fun cinemaClick(comingSoonItem: CinemaSessionResponse.Child) {
    }

    override fun nearTheaterClick(comingSoonItem: CinemaNearTheaterResponse.Output.C) {

    }

    override fun nearTheaterDirectionClick(comingSoonItem: CinemaNearTheaterResponse.Output.C) {
        val strUri =
            "http://maps.google.com/maps?q=loc:" + comingSoonItem.lat+ "," +  comingSoonItem.lang + " (" + "Label which you want" + ")"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)



    }

}