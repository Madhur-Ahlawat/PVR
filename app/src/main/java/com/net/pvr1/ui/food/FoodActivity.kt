package com.net.pvr1.ui.food

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityFoodBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.adapter.FoodBestSellerAdapter
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.ui.food.viewModel.FoodViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodActivity : AppCompatActivity(), FoodBestSellerAdapter.RecycleViewItemClickListenerCity {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityFoodBinding? = null
    private val authViewModel: FoodViewModel by viewModels()
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        authViewModel.food(
            "uIKPx9AqYvg=",
            "GURM",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "no",
            "no",
            "no"
        )
        foodDetails()
    }

    private fun foodDetails() {
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: FoodResponse.Output) {
        val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val crewAdapter = FoodBestSellerAdapter(output.bestsellers, this, this)
        binding?.recyclerView19?.layoutManager = layoutManagerCrew
        binding?.recyclerView19?.adapter = crewAdapter

    }

    override fun foodBestImageClick(comingSoonItem: FoodResponse.Output.Bestseller) {

    }

    override fun addFood(comingSoonItem: FoodResponse.Output.Bestseller) {
    }

    override fun addFoodPlus(comingSoonItem: FoodResponse.Output.Bestseller) {
    }

    override fun addFoodMinus(comingSoonItem: FoodResponse.Output.Bestseller) {
    }

}