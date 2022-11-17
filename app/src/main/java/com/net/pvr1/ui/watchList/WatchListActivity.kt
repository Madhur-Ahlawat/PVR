package com.net.pvr1.ui.watchList

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityWatchListBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.setAlert.viewModel.SetAlertViewModel
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WatchListActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityWatchListBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: SetAlertViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchListBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
    }
}