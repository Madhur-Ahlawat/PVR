package com.net.pvr.ui.home.fragment.privilege

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr.databinding.ActivityPrivilegeDetailsBinding
import com.net.pvr.di.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PrivilegeDetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPrivilegeDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivilegeDetailsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

    }
}