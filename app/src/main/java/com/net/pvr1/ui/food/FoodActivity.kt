package com.net.pvr1.ui.food

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityFoodBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.food.viewModel.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodActivity : AppCompatActivity() {
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
    }
}