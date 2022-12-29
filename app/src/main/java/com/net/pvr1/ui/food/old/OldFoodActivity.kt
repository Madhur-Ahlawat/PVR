package com.net.pvr1.ui.food.old

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityOldFoodBinding
import com.net.pvr1.ui.food.viewModel.FoodViewModel
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OldFoodActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityOldFoodBinding? = null
    private val authViewModel: FoodViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOldFoodBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
    }
}