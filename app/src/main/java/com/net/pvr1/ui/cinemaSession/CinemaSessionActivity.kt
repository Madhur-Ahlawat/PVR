package com.net.pvr1.ui.cinemaSession

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityCinemaSessionBinding
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionCinParentAdapter
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionDaysAdapter
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionLanguageAdapter
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionNearTheaterAdapter
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.cinemaSession.viewModel.CinemaSessionViewModel
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.home.adapter.PromotionAdapter
import com.net.pvr1.ui.login.LoginActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CinemaSessionActivity : AppCompatActivity(),
    CinemaSessionDaysAdapter.RecycleViewItemClickListenerCity,
    CinemaSessionLanguageAdapter.RecycleViewItemClickListenerCity,
    CinemaSessionCinParentAdapter.RecycleViewItemClickListenerCity,
    CinemaSessionNearTheaterAdapter.RecycleViewItemClickListenerCity {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityCinemaSessionBinding? = null
    private val authViewModel: CinemaSessionViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var itemClick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSessionBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        authViewModel.cinemaSession(
            preferences.getCityName(),
            intent.getStringExtra("cid").toString(),
            intent.getStringExtra("lat").toString(),
            intent.getStringExtra("lang").toString(),
            preferences.getUserId(),
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
        authViewModel.cinemaNearTheater(
            preferences.getCityName(),
            preferences.getLatitudeData(),
            preferences.getLongitudeData(),
            intent.getStringExtra("cid").toString()
        )
        cinemaSessionDataLoad()
        cinemaNearTheaterLoad()
        movedNext()
    }

    private fun movedNext() {
        //onBack
        binding?.imageView41?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        //share
        binding?.imageView42?.setOnClickListener {
            Constant().shareData(this@CinemaSessionActivity, "", "")
        }

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
                            positiveClick = {},
                            negativeClick = {})
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
                        positiveClick = {},
                        negativeClick = {})
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
                            positiveClick = {},
                            negativeClick = {})
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
                        positiveClick = {},
                        negativeClick = {})
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
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding?.recyclerView18)
        val gridLayout4 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val cinemaSessionNearTheaterAdapter = CinemaSessionNearTheaterAdapter(output.c, this, this)
        binding?.recyclerView18?.layoutManager = gridLayout4
        binding?.recyclerView18?.adapter = cinemaSessionNearTheaterAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun retrieveData(output: CinemaSessionResponse.Output) {
        if (itemClick == 0) {
            binding?.textView99?.text = output.cn
            //title
            binding?.textView84?.text = output.cn
            //address
            binding?.textView81?.text = output.addr
            //Image
            Glide.with(this@CinemaSessionActivity).load(output.imob).error(R.drawable.app_icon)
                .into(binding?.imageView40!!)
            //Direction
            binding?.view64?.setOnClickListener {
                Constant().openMap(this, output.lat, output.lang)
            }

            //Distance
            binding?.textView86?.text = output.d
            //Shows
            binding?.textView85?.text = output.msc.toString() + " " + getString(R.string.shows)

            //recycler Days
            binding?.recyclerView13?.onFlingListener = null
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding?.recyclerView13)
            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val cinemaSessionDaysAdapter = CinemaSessionDaysAdapter(output.bd, this, this)
            binding?.recyclerView13?.layoutManager = gridLayout2
            binding?.recyclerView13?.adapter = cinemaSessionDaysAdapter

            //action Like
            var rowIndex: Boolean = output.like
            val isLogin = preferences.getIsLogin()
            if (rowIndex) {
                binding?.imageView43?.setImageResource(R.drawable.ic_favourite_theatre)
            } else {
                binding?.imageView43?.setImageResource(R.drawable.ic_un_favourite_theatre)
            }

            binding?.imageView43?.setOnClickListener {
                if (isLogin) {
                    if (rowIndex) {
                        rowIndex = false
                        binding?.imageView43?.setImageResource(R.drawable.ic_un_favourite_theatre)
                        authViewModel.cinemaPreference(
                            preferences.getUserId(),
                            intent.getStringExtra("cid").toString(),
                            rowIndex,
                            "t",
                            ""
                        )

                    } else {
                        rowIndex = true
                        binding?.imageView43?.setImageResource(R.drawable.ic_favourite_theatre)
                        authViewModel.cinemaPreference(
                            preferences.getUserId(),
                            intent.getStringExtra("cid").toString(),
                            rowIndex,
                            "t",
                            ""
                        )
                    }

                } else {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher_foreground,
                        R.string.app_name,
                        getString(R.string.loginCinema),
                        positiveBtnText = R.string.yes,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                            val intent =
                                Intent(this@CinemaSessionActivity, LoginActivity::class.java)
                            startActivity(intent)
                        },
                        negativeClick = {})
                    dialog.show()
                }

            }

            //Promotion
            val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val cinemaSessionLanguageAdapter = PromotionAdapter(this, output.phd)
            binding?.recyclerView14?.layoutManager = gridLayout
            binding?.recyclerView14?.adapter = cinemaSessionLanguageAdapter


            //recycler Cinemas
            val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val cinemaSessionCinParentAdapter =
                CinemaSessionCinParentAdapter(output.childs, this, this)
            binding?.recyclerView15?.layoutManager = gridLayout3
            binding?.recyclerView15?.adapter = cinemaSessionCinParentAdapter
            
        } else {
            //recycler Cinemas
            val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val cinemaSessionCinParentAdapter =
                CinemaSessionCinParentAdapter(output.childs, this, this)
            binding?.recyclerView15?.layoutManager = gridLayout3
            binding?.recyclerView15?.adapter = cinemaSessionCinParentAdapter
        }

    }

    override fun dateClick(comingSoonItem: CinemaSessionResponse.Output.Bd) {
        itemClick = 1
        authViewModel.cinemaSession(
            preferences.getCityName(),
            intent.getStringExtra("cid").toString(),
            intent.getStringExtra("lat").toString(),
            intent.getStringExtra("lang").toString(),
            preferences.getUserId(),
            comingSoonItem.dt,
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
    }

    override fun languageClick(comingSoonItem: String) {

    }

    override fun cinemaClick(comingSoonItem: CinemaSessionResponse.Child) {
    }

    override fun nearTheaterClick(comingSoonItem: CinemaNearTheaterResponse.Output.C) {

    }

    override fun nearTheaterDirectionClick(comingSoonItem: CinemaNearTheaterResponse.Output.C) {
        Constant().openMap(this, comingSoonItem.lat, comingSoonItem.lang)
    }

}