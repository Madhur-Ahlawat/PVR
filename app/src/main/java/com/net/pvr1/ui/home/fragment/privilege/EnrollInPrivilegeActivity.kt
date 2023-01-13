package com.net.pvr1.ui.home.fragment.privilege

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentPrivilegeBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.home.HomeActivity.Companion.review_position
import com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeTypeAdapter
import com.net.pvr1.ui.home.fragment.privilege.response.PassportPlanResponse
import com.net.pvr1.ui.home.fragment.privilege.viewModel.PrivilegeLoginViewModel
import com.net.pvr1.ui.login.LoginActivity
import com.net.pvr1.ui.webView.WebViewActivity
import com.net.pvr1.utils.*
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