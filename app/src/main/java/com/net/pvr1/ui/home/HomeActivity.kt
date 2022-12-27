package com.net.pvr1.ui.home

import android.app.Dialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityHomeBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.adapter.HomeOfferAdapter
import com.net.pvr1.ui.home.fragment.cinema.CinemasFragment
import com.net.pvr1.ui.home.fragment.commingSoon.ComingSoonFragment
import com.net.pvr1.ui.home.fragment.home.HomeFragment
import com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr1.ui.home.fragment.more.MoreFragment
import com.net.pvr1.ui.home.fragment.more.offer.response.OfferResponse
import com.net.pvr1.ui.home.fragment.privilege.MemberFragment
import com.net.pvr1.ui.home.fragment.privilege.NonMemberFragment
import com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeDialogAdapter
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.ui.home.interfaces.PlayPopup
import com.net.pvr1.ui.location.selectCity.SelectCityActivity
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.PRIVILEGEVOUCHER
import com.net.pvr1.utils.Constant.Companion.PrivilegeHomeResponseConst
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), HomeOfferAdapter.RecycleViewItemClickListenerCity,
    PrivilegeHomeDialogAdapter.RecycleViewItemClickListener,PlayPopup {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityHomeBinding? = null
    private val authViewModel: HomeViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var offerShow: Boolean = false
    private var offerResponse: ArrayList<OfferResponse.Output>? = null

    private val firstFragment = HomeFragment()
    private val secondFragment = CinemasFragment()
    private val thirdFragment = NonMemberFragment()
    private val memberFragment = MemberFragment()
    private val fourthFragment = ComingSoonFragment()
    private val fifthFragment = MoreFragment()

    companion object{
        var review_position = 0
        var pcheck = "0"
        var pdays = "0"
        fun getCurrentItem(recyclerView: RecyclerView): Int {
            return (Objects.requireNonNull(recyclerView.layoutManager) as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        switchFragment()
//        authViewModel.offer(Constant().getDeviceId(this))
        //setUserName
        if (preferences.getIsLogin()) {
            binding?.includeAppBar?.textView2?.text = "Hello, "+preferences.getUserName()
        } else {
            binding?.includeAppBar?.textView2?.text = "Hello!"
        }
        //if (preferences.getIsLogin()){
            authViewModel.privilegeHome(preferences.geMobileNumber(), preferences.getCityName())
       // }
        binding?.includeAppBar?.txtCity?.text = preferences.getCityName()
        privilegeDataLoad()
        movedNext()
        //offerDataLoad()
    }

    //ClickMovedNext
    private fun movedNext() {
        // Select City
        binding?.includeAppBar?.txtCity?.setOnClickListener {
            val intent = Intent(this, SelectCityActivity::class.java)
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
        setCurrentFragment(firstFragment)
        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment ->{
                    setCurrentFragment(firstFragment)
                    if (offerShow){
                        binding?.constraintLayout55?.show()
                    }else{
                        binding?.constraintLayout55?.hide()
                    }
                }
                R.id.cinemaFragment -> {
                    setCurrentFragment(secondFragment)
                    binding?.includeAppBar?.textView2?.text = getString(R.string.all_theaters)
                }
                R.id.privilegeFragment ->{
                    if (preferences.getIsLogin()){
                        val ls = preferences
                            .getString(Constant.SharedPreference.LOYALITY_STATUS)
                        val isHl: String = preferences
                            .getString(Constant.SharedPreference.IS_HL)
                        val isLy: String = preferences
                            .getString(Constant.SharedPreference.IS_LY)
                        if (isLy.equals("true", ignoreCase = true)) {
                            if (ls != null && !ls.equals("", ignoreCase = true)) {
                                if (isHl.equals("true", ignoreCase = true)) {
                                    setCurrentFragment(memberFragment)
                                }
                            }
                        }
                    }else{
                        setCurrentFragment(thirdFragment)
                    }
                }
                R.id.comingSoonFragment -> setCurrentFragment(fourthFragment)
                R.id.moreFragment -> setCurrentFragment(fifthFragment)
            }
            true
        }
    }

    private fun showOfferDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.offer_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView26)
        val ignore = dialog.findViewById<TextView>(R.id.textView194)

        try {
            val gridLayout =
                GridLayoutManager(this@HomeActivity, 1, GridLayoutManager.HORIZONTAL, false)
            recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)
            val adapter = offerResponse?.let { HomeOfferAdapter(it, this, this) }
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
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.loyaltyList)
        val icon = dialog.findViewById<ImageView>(R.id.logo)
        val turnOn = dialog.findViewById<TextView>(R.id.turnOn)
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
        val adapter =
            PrivilegeHomeResponseConst?.pinfo?.let { PrivilegeHomeDialogAdapter(it, this,0, this) }
        recyclerView.layoutManager = gridLayout
        recyclerView.adapter = adapter
        val mSnapHelper = PagerSnapHelper()
        if ( PrivilegeHomeResponseConst?.pinfo?.size!! > 1) {
            review_position = 0
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    println("review_positionnewState--->$newState")
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        //Dragging
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        review_position = getCurrentItem(recyclerView)


                        /*
                    Here load the Image to image view with picaso
                 */
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val firstVisibleItem: Int = getCurrentItem(recyclerView)
                    println("review_position123--->$firstVisibleItem")
                    /* Log.e ("VisibleItem", String.valueOf(firstVisibleItem));*/
                }
            })
            recyclerView.onFlingListener = null
            mSnapHelper.attachToRecyclerView(recyclerView)
            recyclerView.addItemDecoration(LinePagerIndicatorDecoration())
        }

        turnOn.setOnClickListener {
            managePrivilege()
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()
    }

//    private fun offerDataLoad() {
//        authViewModel.userResponseOfferLiveData.observe(this) {
//            when (it) {
//                is NetworkResult.Success -> {
//                    loader?.dismiss()
//                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
//                        printLog("output--->${it.data.output}")
//                        retrieveData(it.data.output)
//                    } else {
//                        val dialog = OptionDialog(this,
//                            R.mipmap.ic_launcher,
//                            R.string.app_name,
//                            it.data?.msg.toString(),
//                            positiveBtnText = R.string.ok,
//                            negativeBtnText = R.string.no,
//                            positiveClick = {
//                            },
//                            negativeClick = {
//                            })
//                        dialog.show()
//                    }
//                }
//                is NetworkResult.Error -> {
//                    loader?.dismiss()
//                    val dialog = OptionDialog(this,
//                        R.mipmap.ic_launcher,
//                        R.string.app_name,
//                        it.message.toString(),
//                        positiveBtnText = R.string.ok,
//                        negativeBtnText = R.string.no,
//                        positiveClick = {
//                        },
//                        negativeClick = {
//                        })
//                    dialog.show()
//                }
//                is NetworkResult.Loading -> {
////                    loader = LoaderDialog(R.string.pleasewait)
////                    loader?.show(supportFragmentManager, null)
//                }
//            }
//        }
//    }

    private fun privilegeDataLoad() {
        authViewModel.privilegeHomeResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        Constant.PRIVILEGEPOINT= it.data.output.pt
                        PRIVILEGEVOUCHER= it.data.output.vou
                        privilegeRetrieveData(it.data.output)
                    } else {
                        if (it?.data?.output!=null)
                        privilegeRetrieveData(it.data.output)
//                        val dialog = OptionDialog(this,
//                            R.mipmap.ic_launcher,
//                            R.string.app_name,
//                            it.data?.msg.toString(),
//                            positiveBtnText = R.string.ok,
//                            negativeBtnText = R.string.no,
//                            positiveClick = {
//                            },
//                            negativeClick = {
//                            })
//                        dialog.show()
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
//                    loader = LoaderDialog(R.string.pleasewait)
//                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun privilegeRetrieveData(output: PrivilegeHomeResponse.Output) {
        try {
            PrivilegeHomeResponseConst = output
            pcheck = output.pcheck
            preferences.saveString("FAQ", output.faq)
            pdays = output.pdays
            preferences.saveString(Constant.SharedPreference.pcities, output.pcities)
            preferences.saveString("KOTAK_URL", output.pkotakurl)
            val jsonObject1 = JSONObject()
            jsonObject1.put("points", output.pt)
            jsonObject1.put("voucher", output.vou)
            preferences
                .saveString(Constant.SharedPreference.LOYALITY_POINT, jsonObject1.toString())
            preferences
                .saveString(Constant.SharedPreference.LOYALITY_CARD, output.passportbuy.toString())
            preferences
                .saveString(Constant.SharedPreference.SUBS_OPEN, output.passport.toString())
            preferences
                .saveString(Constant.SharedPreference.LOYALITY_STATUS, output.ls)
            preferences
                .saveString(Constant.SharedPreference.SUBSCRIPTION_STATUS,output.ulm)
        }catch (e:Exception){

        }
    }

    private fun retrieveData(output: ArrayList<OfferResponse.Output>) {
        if (output.isNotEmpty()){
            offerShow=true
            binding?.constraintLayout55?.show()
        }else{
            offerShow= false
            binding?.constraintLayout55?.hide()
        }
        offerResponse = output
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

    override fun onShowNotification() {
        if (!areNotificationsEnabled()) {
            if (preferences.getString(Constant.SharedPreference.NT) == "true") {
                showDialog(
                    this@HomeActivity,
                    preferences.getString(Constant.SharedPreference.NTBT)
                )
            }
        }
    }

    override fun onShowPrivilege() {
        privilegeDialog()
    }

    override fun onViewOffers() {

    }

    override fun onFeedbackOffers() {
    }

    private fun areNotificationsEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (!manager.areNotificationsEnabled()) {
                return false
            }
            val channels = manager.notificationChannels
            for (channel in channels) {
                if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                    return false
                }
            }
            true
        } else {
            NotificationManagerCompat.from(this).areNotificationsEnabled()
        }
    }

    private fun showDialog(mContext: Context?, banner: String?) {
        if (hasMonthPassed()) {
            preferences.saveLong("SHOW_POP", System.currentTimeMillis())
            val dialog = BottomSheetDialog(mContext!!, R.style.NoBackgroundDialogTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.notification_dialoge)
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setGravity(Gravity.CENTER)
            val latter = dialog.findViewById<View>(R.id.latter) as TextView?
            val turnOn = dialog.findViewById<View>(R.id.turnOn) as TextView?
            val bannerImg = dialog.findViewById<View>(R.id.bannerImg) as ImageView?
                          Glide.with(mContext)
                    .load(banner)
                              .error(R.drawable.placeholder_horizontal_movie)
                   .into(bannerImg!!)


            turnOn!!.setOnClickListener {
                val settingsIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    .putExtra(Settings.EXTRA_CHANNEL_ID, "WAP")
                startActivity(settingsIntent)
                dialog.dismiss()
            }
            latter!!.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }

    private fun hasMonthPassed(): Boolean {
        return if (preferences.getLong("SHOW_POP") > 0) {
            val lastTimestamp: Long = preferences.getLong("SHOW_POP")
            val currentTimestamp = System.currentTimeMillis()
            val result = currentTimestamp - lastTimestamp >= TimeUnit.DAYS.toMillis(15)
            println(result)
            result
        } else {
            true
        }
    }

    private fun managePrivilege(){
        if (preferences.getIsLogin()){
            val ls = preferences
                .getString(Constant.SharedPreference.LOYALITY_STATUS)
            val isHl: String = preferences
                .getString(Constant.SharedPreference.IS_HL)
            val isLy: String = preferences
                .getString(Constant.SharedPreference.IS_LY)
            if (isLy.equals("true", ignoreCase = true)) {
                if (ls != null && !ls.equals("", ignoreCase = true)) {
                    if (isHl.equals("true", ignoreCase = true)) {
                        setCurrentFragment(memberFragment)
                    }else{
                        setCurrentFragment(thirdFragment)
                    }
                }else{
                    setCurrentFragment(thirdFragment)
                }
            }else{
                setCurrentFragment(thirdFragment)
            }
        }else{
            setCurrentFragment(thirdFragment)
        }
    }


}