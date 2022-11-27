package com.net.pvr1.ui.home.fragment.more.prefrence

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.net.pvr1.databinding.ActivityPrefrenceBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.profile.userDetails.viewModel.UserProfileViewModel
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PreferenceActivity : AppCompatActivity() {
    @Inject
    private lateinit var preferences: PreferenceManager
    private var binding: ActivityPrefrenceBinding? = null
    private val authViewModel: UserProfileViewModel by viewModels()
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrefrenceBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
    }
}