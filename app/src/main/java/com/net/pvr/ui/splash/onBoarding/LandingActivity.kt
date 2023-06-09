package com.net.pvr.ui.splash.onBoarding

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.net.pvr.R
import com.net.pvr.databinding.ActivityLandingBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.login.LoginActivity
import com.net.pvr.utils.Constant.Companion.ON_BOARDING_CLICK
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityLandingBinding? = null
    private var layouts: IntArray? = null
    private val myPreference = "MyPrefs"
    private var sharedPreferences: SharedPreferences? = null
    private val onBoardingClick = "Name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        mangeFunction()
    }

    private fun mangeFunction() {
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        slider()
        movedNext()
    }

    //Moved To Next Page
    @SuppressLint("ApplySharedPref")
    private fun movedNext() {
        binding?.tvNext?.setOnClickListener {
            binding?.viewPager?.setCurrentItem(getItem(+1), true)
        }
        binding?.tvPrev?.setOnClickListener {
            binding?.viewPager?.setCurrentItem(getItem(-1), true)
        }

        binding?.movedToNext?.setOnClickListener {
            val editor = sharedPreferences?.edit()
            editor?.putBoolean(onBoardingClick, true)
            editor?.commit()
            val intent = Intent(this@LandingActivity, LoginActivity::class.java)
            ON_BOARDING_CLICK
            startActivity(intent)
            finish()
        }

    }

    //Slider Item Position
    private fun getItem(i: Int): Int {
        return binding?.viewPager?.currentItem!! + i
    }

    //Slider Set Data
    private fun slider() {
        layouts = intArrayOf(
            R.layout.onboarding_layout_one,
            R.layout.onboarding_layout_three,
            R.layout.onboarding_layout_two,
            R.layout.onboarding_layout_four
        )

        val myViewPagerAdapter = MyViewPagerAdapter(layouts!!, this)
        binding?.viewPager?.adapter = myViewPagerAdapter
        binding?.tabLayout?.setupWithViewPager(binding?.viewPager, true)

        val viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                when (position) {

                    0 -> {
                        binding?.tvPrev?.hide()
                        binding?.tvNext?.show()
                        binding?.movedToNext?.text = getString(R.string.get_started)
                        binding?.changeText?.text = getString(R.string.onboard_txt1)
                    }

                    1 -> {
                        binding?.tvPrev?.show()
                        binding?.tvNext?.show()
                        binding?.movedToNext?.text = getString(R.string.get_started)
                        binding?.changeText?.text = getString(R.string.onboard_txt3)
                    }

                    2 -> {
                        binding?.tvPrev?.show()
                        binding?.tvNext?.show()
                        binding?.movedToNext?.text = getString(R.string.get_started)
                        binding?.changeText?.text = getString(R.string.onboard_txt2)
                    }

                    3 -> {
                        binding?.tvPrev?.show()
                        binding?.tvNext?.hide()
                        binding?.movedToNext?.text = getString(R.string.get_started)
                        binding?.changeText?.text = getString(R.string.onboard_txt4)
                    }
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(arg0: Int) {

            }
        }

        binding?.viewPager?.addOnPageChangeListener(viewPagerPageChangeListener)

    }

    //Slider Adapter
    class MyViewPagerAdapter(
        private val layouts: IntArray, private val landingActivity: LandingActivity
    ) : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater =
                landingActivity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
            val view: View? = layoutInflater?.inflate(layouts[position], container, false)
            container.addView(view)
            return view!!
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            val view = obj as View
            container.removeView(view)
        }
    }

}