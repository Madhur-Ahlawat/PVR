package com.net.pvr1.ui.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityHomeBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.adapter.HomeOfferAdapter
import com.net.pvr1.ui.home.fragment.cinema.CinemasFragment
import com.net.pvr1.ui.home.fragment.commingSoon.ComingSoonActivity
import com.net.pvr1.ui.home.fragment.home.HomeFragment
import com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr1.ui.home.fragment.more.MoreFragment
import com.net.pvr1.ui.home.fragment.privilege.PrivilegeFragment
import com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeDialogAdapter
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.ui.offer.response.OfferResponse
import com.net.pvr1.ui.search.searchHome.SearchHomeActivity
import com.net.pvr1.ui.selectCity.SelectCityActivity
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.PrivilegeHomeResponseConst
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), HomeOfferAdapter.RecycleViewItemClickListenerCity,
    PrivilegeHomeDialogAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityHomeBinding? = null
    private val authViewModel: HomeViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var offerResponse: ArrayList<OfferResponse.Output>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        switchFragment()
        authViewModel.offer(Constant().getDeviceId(this))
        offerDataLoad()
        println("userId--->${preferences.getUserId()}")
        //setUserName
        binding?.includeAppBar?.textView2?.text = preferences.getUserName()
        if (preferences.getIsLogin()){
            authViewModel.privilegeHome(preferences.geMobileNumber().toString(), "Delhi-NCR")
        }
        privilegeDataLoad()
        movedNext()
    }

    //ClickMovedNext
    private fun movedNext() {
        // Select City
        binding?.includeAppBar?.txtCity?.setOnClickListener {
            val intent = Intent(this, SelectCityActivity::class.java)
            startActivity(intent)
        }

        binding?.includeAppBar?.searchBtn?.setOnClickListener {
            val intent = Intent(this@HomeActivity, SearchHomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Coming Soon for Testing

        // Select City
        binding?.includeAppBar?.notify?.setOnClickListener {
            val intent = Intent(this, ComingSoonActivity::class.java)
            startActivity(intent)
        }

        //Close Offer Alert
        binding?.imageView78?.setOnClickListener {
            binding?.constraintLayout55?.hide()
        }

        binding?.textView185?.setOnClickListener {
            showOfferDialog()
        }
    }

    private fun switchFragment() {
        val firstFragment = HomeFragment()
        val secondFragment = CinemasFragment()
        val thirdFragment = PrivilegeFragment()
        val fifthFragment = MoreFragment()

        setCurrentFragment(firstFragment)
        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> setCurrentFragment(firstFragment)
                R.id.cinemaFragment -> setCurrentFragment(secondFragment)
                R.id.privilegeFragment -> setCurrentFragment(thirdFragment)
                R.id.moreFragment -> setCurrentFragment(fifthFragment)
            }
            true
        }
    }

    private fun showOfferDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.offer_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView26)
        val ignore = dialog.findViewById<TextView>(R.id.textView194)

        try {
            val gridLayout =
                GridLayoutManager(this@HomeActivity, 1, GridLayoutManager.HORIZONTAL, false)
            recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)
            val adapter = HomeOfferAdapter(offerResponse!!, this, this)
            recyclerView.layoutManager = gridLayout
            recyclerView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

        ignore.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun privilegeDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.privilege_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView34)
        val icon = dialog.findViewById<ImageView>(R.id.imageView109)
        //icon
        Glide.with(this)
            .load(PrivilegeHomeResponseConst?.pinfo?.get(0)?.plogo)
            .into(icon)

        // add pager behavior
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        val gridLayout =
            GridLayoutManager(this@HomeActivity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)
        val adapter = PrivilegeHomeDialogAdapter(PrivilegeHomeResponseConst?.pinfo!!, this,0, this)
        recyclerView.layoutManager = gridLayout
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(LinePagerIndicatorDecoration())
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()
        }

    private fun offerDataLoad() {
        authViewModel.userResponseOfferLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        printLog("output--->${it.data.output}")
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun privilegeDataLoad() {
        authViewModel.privilegeHomeResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        privilegeRetrieveData(it.data.output)
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun privilegeRetrieveData(output: PrivilegeHomeResponse.Output) {
        PrivilegeHomeResponseConst = output
//        privilegeDialog()
    }

    private fun retrieveData(output: ArrayList<OfferResponse.Output>) {
        offerResponse = output
        printLog("offer--->${output}")
        showOfferDialog()
    }

    override fun offerClick(comingSoonItem: OfferResponse.Output) {

    }


    override fun onBackPressed() {
        if (binding?.bottomNavigationView?.selectedItemId == R.id.homeFragment) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.exitApp),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
            finish()

        } else {
            binding?.bottomNavigationView?.selectedItemId = R.id.homeFragment
        }
    }

    override fun privilegeHomeClick(comingSoonItem: PrivilegeHomeResponse.Output.Pinfo) {

    }

}