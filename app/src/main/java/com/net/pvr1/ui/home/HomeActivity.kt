package com.net.pvr1.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityHomeBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.home.fragment.*
import com.net.pvr1.ui.home.fragment.cinema.CinemasFragment
import com.net.pvr1.ui.home.fragment.commingSoon.ComingSoonFragment
import com.net.pvr1.ui.home.fragment.home.HomeFragment
import com.net.pvr1.ui.login.viewModel.LoginViewModel
import com.net.pvr1.ui.selectCity.SelectCityActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityHomeBinding? = null
    private val authViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        switchFragment()

        // Select City
       val llLocation = findViewById<LinearLayout>(R.id.llLocation)
        llLocation.setOnClickListener {
            val intent = Intent(this, SelectCityActivity::class.java)
            startActivity(intent)
        }

    }

    private fun switchFragment() {
        val firstFragment = HomeFragment()
        val secondFragment = CinemasFragment()
        val thirdFragment = PrivilegeFragment()
        val fourthFragment = ComingSoonFragment()
        val fifthFragment = MoreFragment()

        setCurrentFragment(firstFragment)
        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> setCurrentFragment(firstFragment)
                R.id.cinemaFragment -> setCurrentFragment(secondFragment)
                R.id.privilegeFragment -> setCurrentFragment(thirdFragment)
                R.id.comingSoonFragment -> setCurrentFragment(fourthFragment)
                R.id.moreFragment -> setCurrentFragment(fifthFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()
        }
}