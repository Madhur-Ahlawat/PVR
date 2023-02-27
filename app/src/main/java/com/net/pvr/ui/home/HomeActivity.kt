package com.net.pvr.ui.home

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr.R
import com.net.pvr.databinding.ActivityHomeBinding
import com.net.pvr.databinding.ExperienceDialogBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.adapter.HomeOfferAdapter
import com.net.pvr.ui.home.fragment.cinema.CinemasFragment
import com.net.pvr.ui.home.fragment.comingSoon.ComingSoonFragment
import com.net.pvr.ui.home.fragment.home.HomeFragment
import com.net.pvr.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr.ui.home.fragment.more.MoreFragment
import com.net.pvr.ui.home.fragment.more.offer.response.OfferResponse
import com.net.pvr.ui.home.fragment.privilege.MemberFragment
import com.net.pvr.ui.home.fragment.privilege.NonMemberActivity
import com.net.pvr.ui.home.fragment.privilege.NonMemberFragment
import com.net.pvr.ui.home.fragment.privilege.adapter.PrivilegeHomeDialogAdapter
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr.ui.home.interfaces.PlayPopup
import com.net.pvr.ui.movieDetails.nowShowing.NowShowingMovieDetailsActivity
import com.net.pvr.ui.player.PlayerActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.PRIVILEGEVOUCHER
import com.net.pvr.utils.Constant.Companion.PrivilegeHomeResponseConst
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), HomeOfferAdapter.RecycleViewItemClickListenerCity,
    PrivilegeHomeDialogAdapter.RecycleViewItemClickListener, PlayPopup {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityHomeBinding? = null
    private val authViewModel: HomeViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var offerShow = 0

    private var offerResponse: ArrayList<OfferResponse.Output>? = null

    private val firstFragment = HomeFragment()
    private val secondFragment = CinemasFragment()
    private val thirdFragment = NonMemberFragment()
    private val memberFragment = MemberFragment()
    private val fourthFragment = ComingSoonFragment()
    private val fifthFragment = MoreFragment()

    //redirect
    private var from: String = ""

    companion object {
        var reviewPosition = 0
        var backToTop:ConstraintLayout? = null
        var pCheck = "0"
        var pDays = "0"
        fun getCurrentItem(recyclerView: RecyclerView): Int {
            return (Objects.requireNonNull(recyclerView.layoutManager) as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunction()
        Constant.setAverageUserIdSCM(preferences)
        Constant.setUPSFMCSDK(preferences)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun manageFunction() {
        privilegeDataLoad()
        offerDataLoad()
        movedNext()
//        experienceDialog()
        // Call Loyalty Home Api
        authViewModel.privilegeHome(preferences.geMobileNumber(), preferences.getCityName())

        //offer
        if (preferences.getIsLogin()) {
//            authViewModel.offer(preferences.getCityName(),preferences.getUserId(),Constant().getDeviceId(this),"no")
        }

        if (intent.hasExtra("from"))
        from= intent.getStringExtra("from").toString()
        printLog("from---->${from}")

        manageDeepLinks()
    }

    private fun switchFragment() {
        if (from=="cinema"){
            setCurrentFragment(secondFragment)
            binding?.bottomNavigationView?.selectedItemId = R.id.cinemaFragment

        }else{
            binding?.bottomNavigationView?.selectedItemId = R.id.homeFragment
            setCurrentFragment(firstFragment)
        }


        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    setCurrentFragment(firstFragment)
                    if (offerShow == 1) {
                        binding?.constraintLayout55?.show()
                    } else {
                        binding?.constraintLayout55?.hide()
                    }
                }
                R.id.cinemaFragment -> {
                    setCurrentFragment(secondFragment)
                }
                R.id.privilegeFragment ->
                    managePrivilege("")
                R.id.comingSoonFragment ->
                    setCurrentFragment(fourthFragment)
                R.id.moreFragment ->
                    setCurrentFragment(fifthFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()
        }

    //ClickMovedNext
    private fun movedNext() {
//      Close Offer Alert
        binding?.imageView78?.setOnClickListener {
            binding?.constraintLayout55?.hide()
        }

//        Dialogs
        binding?.textView185?.setOnClickListener {
            showOfferDialog()
        }

    }

//    experience Dialog

    private fun experienceDialog() {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val bindingProfile = ExperienceDialogBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<FrameLayout> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(bindingProfile.root)
        dialog.show()

    }

    //  offers Dialog
    private fun showOfferDialog() {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.offer_dialog)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()

//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.offer_dialog)
//        dialog.window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
//        dialog.window?.setGravity(Gravity.BOTTOM)
//        dialog.show()

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView26)
        val ignore = dialog.findViewById<TextView>(R.id.textView194)

        val gridLayout =
            GridLayoutManager(this@HomeActivity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView?.layoutManager = LinearLayoutManager(this@HomeActivity)
        val adapter = offerResponse?.let { HomeOfferAdapter(it, this, this) }
        recyclerView?.layoutManager = gridLayout
        recyclerView?.adapter = adapter


        ignore?.setOnClickListener {
            dialog.dismiss()
        }
    }

    //    privilegeDialogs
    private fun privilegeDialog() {
        if (PrivilegeHomeResponseConst?.pdays?.toInt()?.let { hasDaysPassed(it) } == true) {
            preferences.saveLong("SHOW_LOYALTY", System.currentTimeMillis())
            val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.privilege_dialog)
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.show()

            val recyclerView = dialog.findViewById<RecyclerView>(R.id.loyaltyList)
            val icon = dialog.findViewById<ImageView>(R.id.logo)
            val turnOn = dialog.findViewById<TextView>(R.id.turnOn)
            //icon
            Glide.with(this).load(PrivilegeHomeResponseConst?.pinfo?.get(0)?.plogo).into(icon!!)

            // add pager behavior
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(recyclerView)
            val gridLayout =
                GridLayoutManager(this@HomeActivity, 1, GridLayoutManager.HORIZONTAL, false)
            recyclerView?.layoutManager = LinearLayoutManager(this@HomeActivity)
            val adapter =
                PrivilegeHomeResponseConst?.pinfo?.let {
                    PrivilegeHomeDialogAdapter(
                        it,
                        this,
                        0,
                        this
                    )
                }
            recyclerView?.layoutManager = gridLayout
            recyclerView?.adapter = adapter
            val mSnapHelper = PagerSnapHelper()
            if ((PrivilegeHomeResponseConst?.pinfo?.size ?: 0) > 1) {
                reviewPosition = 0
                recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        println("reviewPositionNewState--->$newState")
                        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                            //Dragging
                        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            reviewPosition = getCurrentItem(recyclerView)


                            /*
                    Here load the Image to image view with Glide
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
                recyclerView?.onFlingListener = null
                mSnapHelper.attachToRecyclerView(recyclerView)
                recyclerView?.addItemDecoration(LinePagerIndicatorDecoration())
            }

            turnOn?.setOnClickListener {
                dialog.dismiss()
                managePrivilege("")
            }
        }
    }

    private fun hasDaysPassed(days: Int): Boolean {
        println("wert-------" + preferences.getLong("SHOW_LOYALTY"))
        return if (preferences.getLong("SHOW_LOYALTY")>0) {
            val lastTimestamp: Long = preferences.getLong("SHOW_LOYALTY")
            val currentTimestamp = System.currentTimeMillis()
            val result = currentTimestamp - lastTimestamp >= TimeUnit.DAYS.toMillis(days.toLong())
            println(result)
            result
        } else {
            true
        }
    }

    //  notification Dialogs
    @SuppressLint("InlinedApi")
    private fun notificationDialog(banner: String?) {
        if (hasMonthPassed()) {
            preferences.saveLong("SHOW_POP", System.currentTimeMillis())
            val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.notification_dialoge)
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setGravity(Gravity.CENTER)
            val latter = dialog.findViewById<View>(R.id.latter) as TextView?
            val turnOn = dialog.findViewById<View>(R.id.turnOn) as TextView?
            val bannerImg = dialog.findViewById<View>(R.id.bannerImg) as ImageView?

            Glide.with(this).load(banner).error(R.drawable.placeholder_horizental).into(bannerImg!!)

            turnOn?.setOnClickListener {
                val settingsIntent =
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                        .putExtra(Settings.EXTRA_CHANNEL_ID, "WAP")
                startActivity(settingsIntent)
                dialog.dismiss()
            }
            latter?.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    //    privilege Api
    private fun privilegeDataLoad() {
        authViewModel.privilegeHomeResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        Constant.PRIVILEGEPOINT = it.data.output.pt
                        PRIVILEGEVOUCHER = it.data.output.vou
                        privilegeRetrieveData(it.data.output)
                        switchFragment()

                    } else {
                        if (it.data?.output != null) it.data.output.let { it1 ->
                            privilegeRetrieveData(
                                it1
                            )
                        }
                    }
                    switchFragment()

                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun privilegeRetrieveData(output: PrivilegeHomeResponse.Output) {
        try {
            PrivilegeHomeResponseConst = output
            pCheck = output.pcheck
            preferences.saveString("FAQ", output.faq)
            pDays = output.pdays
            preferences.saveString(Constant.SharedPreference.pcities, output.pcities)
            preferences.saveString("KOTAK_URL", output.pkotakurl)
            val jsonObject1 = JSONObject()
            jsonObject1.put("points", output.pt)
            jsonObject1.put("voucher", output.vou)
            preferences.saveString(Constant.SharedPreference.LOYALITY_POINT, jsonObject1.toString())
            preferences.saveString(
                Constant.SharedPreference.LOYALITY_CARD, output.passportbuy.toString()
            )
            preferences.saveString(Constant.SharedPreference.SUBS_OPEN, output.passport.toString())
            preferences.saveString(Constant.SharedPreference.LOYALITY_STATUS, output.ls)
            preferences.saveString(Constant.SharedPreference.SUBSCRIPTION_STATUS, output.ulm)
            if (intent.hasExtra("from") && (intent.getStringExtra("from") == "PP" || intent.getStringExtra("from") == "PP")) {
                managePrivilege(intent.getStringExtra("from").toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun managePrivilege(s: String) {
        if (preferences.getIsLogin()) {
            val ls = preferences.getString(Constant.SharedPreference.LOYALITY_STATUS)
            val isHl: String = preferences.getString(Constant.SharedPreference.IS_HL)
            val isLy: String = preferences.getString(Constant.SharedPreference.IS_LY)
            val data = Bundle()
            if (s == "PP") {
                data.putString("type", "PP")
                data.putString("from", "payment")
                data.putString("id", intent.getStringExtra("id"))
                data.putString("amt", intent.getStringExtra("amt"))
            } else {
                data.putString("type", "P")
            }
            memberFragment.arguments = data
            println("ls--$ls---$isHl---$isLy")
            if (isLy.equals("true", ignoreCase = true)) {
                if (ls != null && !ls.equals("", ignoreCase = true)) {
                    if (isHl.equals("true", ignoreCase = true)) {
                        setCurrentFragment(memberFragment)
                    } else {
                        setCurrentFragment(thirdFragment)
                    }
                } else {
                    setCurrentFragment(thirdFragment)
                }
            } else {
                setCurrentFragment(thirdFragment)
            }
        } else {
            setCurrentFragment(thirdFragment)
        }
    }

    override fun privilegeHomeClick(comingSoonItem: PrivilegeHomeResponse.Output.Pinfo) {

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onShowPrivilege() {
        if (PrivilegeHomeResponseConst?.pcheck == "true")
        privilegeDialog()
    }

    //Offer Api
    private fun offerDataLoad() {
        authViewModel.offerLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output != null && it.data.output.size != 0) {
                            binding?.constraintLayout55?.show()
                            retrieveOffer(it.data.output)
                        }
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun retrieveOffer(output: ArrayList<OfferResponse.Output>) {
//        Set Data
        offerResponse = output

        //Manage Show Hide
        offerShow = if (output.isNotEmpty()) {
            1
        } else {
            0
        }

    }

    override fun offerClick(comingSoonItem: OfferResponse.Output) {

    }

    override fun onShowNotification() {
        if (!areNotificationsEnabled()) {
            if (preferences.getString(Constant.SharedPreference.NT) == "true") {
                notificationDialog(
                    preferences.getString(Constant.SharedPreference.NTBT)
                )
            }
        }
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

    override fun onBackPressed() {
        if (binding?.bottomNavigationView?.selectedItemId == R.id.homeFragment) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.exitApp),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
            finish()

        } else {
            binding?.bottomNavigationView?.selectedItemId = R.id.homeFragment
        }
    }


    private fun manageDeepLinks(){
        try {
            val data = intent.data
            if (data != null) {
                var path = data.path
                if (path?.contains("utm") == true)
                    sendGACampaign(path, "Home")
                if (path?.contains("loyaltys/home") == true || path?.contains("loyalty/home") == true) {
                    val isLy: String = preferences.getString(Constant.SharedPreference.IS_LY)
                    if (isLy.equals("true", ignoreCase = true)) {
                        val ls: String = preferences.getString(Constant.SharedPreference.LOYALITY_STATUS)
                        val isHl: String = preferences.getString(Constant.SharedPreference.IS_HL)
                        if (!ls.equals("", ignoreCase = true) && ls != null) {
                            if (isHl.equals("true", ignoreCase = true)) {
                                managePrivilege("C")
                            } else {
                                val intent1 = Intent(
                                    this@HomeActivity,
                                    NonMemberActivity::class.java
                                )
                                intent1.putExtra("type","P")
                                startActivity(intent1)
                                finish()
                            }
                        } else {
                            val intent1 = Intent(
                                this@HomeActivity,
                                NonMemberActivity::class.java
                            )
                            intent1.putExtra("type","P")
                            startActivity(intent1)
                            finish()
                        }
                    } else {
                        val intent1 = Intent(
                            this@HomeActivity,
                            NonMemberActivity::class.java
                        )
                        intent1.putExtra("type","P")
                        startActivity(intent1)
                        finish()
                    }
                } else if (path?.contains("/cinemasd") == true) {
                    setCurrentFragment(secondFragment)
                } else if (path?.contains("/PSPT") == true) {
                    managePrivilege("C")
                } else if (path?.contains("/comingsoontrailer") == true) {
                    val trailerPathUrl = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    val intent = Intent(this, NowShowingMovieDetailsActivity::class.java)
                    intent.putExtra("mid", trailerPathUrl[2])
                    startActivity(intent)
                } else if (path?.contains("/comingsoon") == true) {
                    if (path.contains("NHO")) {
                        val pathUrl = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        val intent = Intent(this, NowShowingMovieDetailsActivity::class.java)
                        intent.putExtra("mid", pathUrl[4])
                        startActivity(intent)
                    } else setCurrentFragment(fourthFragment)
                } else if (path?.contains("/playtrailer") == true) {
                    path = data.toString()
                    if (path.contains("utm"))
                        sendGACampaign(path, "Trailer")
                    val pathNew = path.split("Url".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[1]
                    val url = pathNew.substring(1, pathNew.length)
                    if (url != null) {
                        val intent = Intent(this@HomeActivity, PlayerActivity::class.java)
                        intent.putExtra("trailerUrl", url)
                        startActivity(intent)
                    }
                } else setCurrentFragment(firstFragment)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            setCurrentFragment(firstFragment)
        }
    }

    private fun sendGACampaign(path: String?, screen: String?) {
//        val mTracker: Tracker = PCApplication.getInstance().getDefaultTracker()
//        mTracker.setScreenName(screen)
//        mTracker.send(
//            ScreenViewBuilder()
//                .setCampaignParamsFromUrl(path)
//                .build()
//        )
    }

}