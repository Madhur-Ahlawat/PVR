package com.net.pvr.ui.home.fragment.privilege

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr.databinding.FragmentPrivilegeBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.home.fragment.privilege.viewModel.PrivilegeLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EnrollInPrivilegeActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: FragmentPrivilegeBinding? = null
    private val authViewModel: PrivilegeLoginViewModel by viewModels()
    private var loader: LoaderDialog? = null
    companion object {
        var scheme_id = ""
        var scheme_price = "0.0"
        var visits = "0.0"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPrivilegeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

    }
    
}