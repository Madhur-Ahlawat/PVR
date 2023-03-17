package com.net.pvr.ui.home.fragment.privilege

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.text.TextUtils
import android.view.*
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityPrivilegeLogInBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.HomeActivity.Companion.getCurrentItem
import com.net.pvr.ui.home.HomeActivity.Companion.reviewPosition
import com.net.pvr.ui.home.fragment.privilege.adapter.HowItWorkAdapter
import com.net.pvr.ui.home.fragment.privilege.adapter.PrivilegeCardAdapter
import com.net.pvr.ui.home.fragment.privilege.adapter.PrivilegeHistoreyAdapter
import com.net.pvr.ui.home.fragment.privilege.adapter.PrivilegeVochersAdapter
import com.net.pvr.ui.home.fragment.privilege.response.LoyaltyDataResponse
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeCardData
import com.net.pvr.ui.home.fragment.privilege.viewModel.PrivilegeLoginViewModel
import com.net.pvr.ui.login.LoginActivity
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.SharedPreference.Companion.ACTIVE
import com.net.pvr.utils.Constant.SharedPreference.Companion.LOYALITY_STATUS
import com.net.pvr.utils.Constant.SharedPreference.Companion.SUBSCRIPTION_STATUS
import com.net.pvr.utils.Constant.SharedPreference.Companion.SUBS_OPEN
import com.net.pvr.utils.ga.GoogleAnalytics
import com.net.pvr.utils.view.CustomViewPager
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import jp.shts.android.storiesprogressview.StoriesProgressView.StoriesListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MemberFragment : Fragment(), PrivilegeCardAdapter.RecycleViewItemClickListener,PrivilegeVochersAdapter.RecycleViewItemClickListener,
    StoriesListener,HowItWorkAdapter.RecycleViewItemClickListener {
    private var storiesProgressView: StoriesProgressView? = null
    private var storyDialog: Dialog? = null
    private var ivBanner: RecyclerView? = null
    private var counterStory = 0
    private var currentPage = 1
    private var pressTime = 0L
    private var recyclerAdapter: HowItWorkAdapter? = null
    private var limit = 500L
    private var binding: ActivityPrivilegeLogInBinding? = null
    private var loader: LoaderDialog? = null

    @Inject
    lateinit var preferences: PreferenceManager
    private val authViewModel by activityViewModels<PrivilegeLoginViewModel>()
    private var qrCode:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ActivityPrivilegeLogInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val time = SystemClock.uptimeMillis()
        HomeActivity.backToTop?.hide()
        authViewModel.loyaltyData(
            preferences.getUserId(),
            preferences.getCityName(),
            preferences.getToken().toString(),
            time.toString(),
            Constant.getHash(preferences.getUserId() + "|" + preferences.getToken().toString() + "|" + time.toString())

        )
        createQr()
        getProfileData()
        manageClicks()
    }

    private fun manageClicks() {

        if (arguments?.containsKey("from") == true){
            showDialogLoyalty(
                requireContext(),
                arguments?.getString("amt").toString(),
                arguments?.getString("id").toString()
            )
        }
        binding?.bookBtn?.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
        }
        binding?.howWork?.setOnClickListener { openHowToWork() }

        binding?.faq?.setOnClickListener{
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("from", "privilege")
            intent.putExtra("title", "Passport FAQ")
            intent.putExtra("getUrl", Constant.PrivilegeHomeResponseConst?.faq)
            startActivity(intent)
        }

        binding?.unLockView?.setOnClickListener {
            if ( Constant.PrivilegeHomeResponseConst?.pinfo?.size == 1) {
                val intent1 = Intent(context, NonMemberActivity::class.java)
                intent1.putExtra("data",  Constant.PrivilegeHomeResponseConst?.pinfo)
                intent1.putExtra("type",  Constant.PrivilegeHomeResponseConst?.pinfo?.get(0)?.ptype)
                startActivity(intent1)
            } else {
                reviewPosition = if ( Constant.PrivilegeHomeResponseConst?.pinfo?.size == 2 && reviewPosition == 2) {
                    1
                } else {
                    0
                }
                if ( Constant.PrivilegeHomeResponseConst?.pinfo?.get(reviewPosition)?.ptype == ("PP")) {
                    if (!preferences.getIsLogin()) {
                        val intent4 = Intent(requireActivity(), LoginActivity::class.java)
                        intent4.putExtra(Constant.PCBackStackActivity.OPEN_ACTIVITY_NAME, Constant.PCBackStackActivity.LOYALITY_UNLIMITED_ACTIVITY)
                        requireActivity().startActivity(intent4)
                    } else {
                        val intent1 = Intent(context, NonMemberActivity::class.java)
                        intent1.putExtra("data", Constant.PrivilegeHomeResponseConst?.pinfo)
                        intent1.putExtra("type", "PP")
                        intent1.putExtra(
                            Constant.PCBackStackActivity.OPEN_ACTIVITY_NAME,
                            Constant.PCBackStackActivity.LANDING_ACTIVITY
                        )
                        startActivity(intent1)
                    }
                } else {
                    val intent1 = Intent(context, NonMemberActivity::class.java)
                    intent1.putExtra("data", Constant.PrivilegeHomeResponseConst?.pinfo)
                    intent1.putExtra("type", "PPP")
                    intent1.putExtra(
                        Constant.PCBackStackActivity.OPEN_ACTIVITY_NAME,
                        Constant.PCBackStackActivity.LANDING_ACTIVITY
                    )
                    startActivity(intent1)
                }
            }
        }

        binding?.textView378?.setOnClickListener {
            val newUrl = "https://www.pvrcinemas.com/loyalty/home"
            Constant.onShareClick(
                requireActivity(), newUrl, "The secret to my movie binge-watching: PVR Passport voucher!\nCheck it out:"
            )
        }
    }

    private fun manageTabs(output: LoyaltyDataResponse.Output) {
        val voucherList = ArrayList<LoyaltyDataResponse.Voucher>()
        for (data in output.vou){
            voucherList.addAll(data.vouchers)
        }
        val sdf = SimpleDateFormat("MMM dd, yyyyy")
        voucherList.sortByDescending { sdf.parse(it.exf) }
        voucherList.sortBy { it.usd }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.privilegeList?.layoutManager = layoutManager
        val cardAdapter =
            PrivilegeVochersAdapter(voucherList, requireActivity(), preferences, this)
        binding?.privilegeList?.adapter = cardAdapter
//        MMM dd yyyy hh:mma
        binding?.vocCard?.setOnClickListener {

            binding?.aboutPvrLl?.hide()
//            Toast.makeText(context, "called.....", Toast.LENGTH_SHORT).show()
//            binding?.hisInactiveLayout?.show()
//            binding?.vocCard?.show()
//            binding?.historyCard?.hide()
//            binding?.hisInactiveLayout?.hide()
        }
        binding?.historyCard?.setOnClickListener {
            binding?.aboutPvrLl?.hide()
//            binding?.hisInactiveLayout?.show()
//            binding?.vocCard?.show()
//            binding?.historyCard?.show()
//            binding?.hisInactiveLayout?.show()
        }
        binding?.totalVocBox?.text = getCounOfActivVo(voucherList)

        binding?.vocIncLayout?.setOnClickListener {
            binding?.hisInactiveLayout?.show()
            binding?.vocCard?.show()
            binding?.historyCard?.hide()
            binding?.aboutPvrLl?.hide()
            binding?.vocIncLayout?.hide()
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding?.privilegeList?.layoutManager = layoutManager
            val cardAdapter =
                PrivilegeVochersAdapter(voucherList, requireActivity(), preferences, this)
            binding?.privilegeList?.adapter = cardAdapter
            binding?.totalVocBox?.text = getCounOfActivVo(voucherList)
            binding?.totalVocBoxIn?.text = getCounOfActivVo(voucherList)
        }
        binding?.hisInactiveLayout?.setOnClickListener {

            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Privilege")
                GoogleAnalytics.hitEvent(requireActivity(), "privilege_history_tab", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding?.hisInactiveLayout?.hide()
            binding?.vocCard?.hide()
            binding?.historyCard?.show()
            binding?.aboutPvrLl?.show()
            binding?.vocIncLayout?.show()
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding?.privilegeList?.layoutManager = layoutManager
            val cardAdapter =
                PrivilegeHistoreyAdapter(output.his, requireActivity(), preferences)
            binding?.privilegeList?.adapter = cardAdapter
            binding?.totalVocBox?.text = getCounOfActivVo(voucherList)
            binding?.totalVocBoxIn?.text = getCounOfActivVo(voucherList)
        }
        //About Click
        binding?.aboutPvrLl?.setOnClickListener {

            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Privilege")
                GoogleAnalytics.hitEvent(requireActivity(), "privilege_about_priviege", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val intent = Intent(requireActivity(), WebViewActivity::class.java)
            intent.putExtra("from", "privilege")
            intent.putExtra("title", getString(R.string.terms_condition_text))
            intent.putExtra("getUrl", Constant.termsCondition)
            startActivity(intent)

        }
    }

    private fun getCounOfActivVo(voucherList: ArrayList<LoyaltyDataResponse.Voucher>): String? {
        var count = 0

        for (data in voucherList){
            if (data.st == "V"){
                count+=1
            }
        }
        return count.toString()
    }

    private fun getProfileData() {
        authViewModel.loyaltyDataLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        loader?.dismiss()
                        retrieveData(it.data.output)
                    } else {
                        loader?.dismiss()
                        val dialog = OptionDialog(requireActivity(),
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(requireActivity(),
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: LoyaltyDataResponse.Output) {
        binding?.scrollView?.show()
        try {
            addLoyalty(output)
            updateHeaderView(output)
            manageTabs(output)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun addLoyalty(output: LoyaltyDataResponse.Output) {
        if (!TextUtils.isEmpty(output.pt)) {
            val points: Float = output.pt.toFloat()
            var pointsValue: String
            var percentage: Float
            var pointsValue1 = 0f
            val ls: String = preferences.getString(LOYALITY_STATUS)
            if (ls.equals("GOLD", ignoreCase = true)) {
                pointsValue = String.format("%.2f", 50 - points)
                pointsValue1 = 50 - points
                percentage = points * 100 / 50
            } else {
                pointsValue = String.format("%.2f", 100 - points)
                pointsValue1 = 100 - points
                percentage = points
                println("percentage===========$percentage")
            }
            if (pointsValue1 < 0) {
                pointsValue = String.format("%.2f", 0f)
                percentage = 0f
            }
            println("percentage===========$percentage")
            binding?.ProgressBAR?.progress = percentage.toInt()
            if (preferences.getString(SUBSCRIPTION_STATUS) == ACTIVE && preferences.getString(
                    SUBS_OPEN
                ) == "true"
            ) {
                binding?.tvPoints?.text = Html.fromHtml("<b>$pointsValue Points Needed</b>")
                binding?.tvPointsMsg?.text = Html.fromHtml("To Unlock Next Voucher")
                binding?.tvPoints?.show()
            } else {
                binding?.tvPoints?.show()
                binding?.tvPoints?.text = Html.fromHtml("<b>$pointsValue Points Needed</b>")
                binding?.tvPointsMsg?.text = Html.fromHtml("To Unlock Next Voucher")
            }


            //  "yyyy-mm-dd HH:mm:ss"
            //, hh:mm a
            if (output.sync_time.isNotEmpty()){
            val date: String = Constant.getDate("yyyy-MM-dd HH:mm:ss", "dd MMM yyyy", output.sync_time).toString()
            val time: String = Constant.getDate("yyyy-mm-dd HH:mm:ss", "hh:mm a", output.sync_time).toString()

                binding?.tvLastHistory?.text = "Updated on: $date at $time"
        }
        }
    }

    private fun updateHeaderView(output: LoyaltyDataResponse.Output) {

        val points_data = Constant.PRIVILEGEPOINT
        val cardDataList: ArrayList<PrivilegeCardData> = ArrayList()
        var count = 3
        count = if (preferences.getString(SUBS_OPEN) != "true" && preferences.getString(SUBSCRIPTION_STATUS) != ACTIVE) {
                2
            } else {
                3
            }
        for (i in 0 until count) {
            if (i == 0) {
                cardDataList.add(PrivilegeCardData("", "P", false, "", "", points_data, output))
            } else if (i == 1) {
                if (preferences.getString(SUBSCRIPTION_STATUS) == ACTIVE) {
                    cardDataList.add(
                        PrivilegeCardData(java.lang.String.valueOf(output.subscription.can_redeem - output.subscription.redeemed),
                            "PP",
                            false,
                            "",
                            "",
                            points_data,
                            output
                        )
                    )
                } else {
                    if (preferences.getString(SUBS_OPEN) == "true") {
                        cardDataList.add(PrivilegeCardData(Constant.PrivilegeHomeResponseConst?.pinfo?.get(0)?.pi.toString(), "PP", true, "", "", points_data, output))
                    } else {
                        if (preferences.getString(LOYALITY_STATUS) == "PPP") {
                            cardDataList.add(PrivilegeCardData(
                                "",
                                "PPP",
                                false,
                                "",
                                "",
                                points_data.toString(),
                                output
                            ))
                        } else {
                            cardDataList.add(PrivilegeCardData(
                                Constant.PrivilegeHomeResponseConst?.pinfo?.get(0)?.pi.toString(),
                                "PPP",
                                true,
                                "",
                                "",
                                points_data.toString(),
                                output
                            ))
                        }
                    }
                }
            } else {
                if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 2) {
                    if (preferences.getString(LOYALITY_STATUS) == "PPP") {
                        cardDataList.add(PrivilegeCardData("", "PPP", false, "", "", points_data.toString(), output))
                    } else {
                        cardDataList.add(PrivilegeCardData(
                            Constant.PrivilegeHomeResponseConst?.pinfo?.get(1)?.pi.toString(),
                            "PPP",
                            true,
                            "",
                            "",
                            points_data.toString(),
                            output
                        ))
                    }
                } else if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 1) {
                    if (preferences.getString(LOYALITY_STATUS) == "PPP") {
                        cardDataList.add(PrivilegeCardData("", "PPP", false, "", "", points_data.toString(), output))
                    } else {
                        cardDataList.add(PrivilegeCardData(
                            Constant.PrivilegeHomeResponseConst?.pinfo?.get(0)?.pi.toString(),
                            "PPP",
                            true,
                            "",
                            "",
                            points_data.toString(),
                            output
                        ))
                    }
                } else if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 0) {
                    cardDataList.add(PrivilegeCardData("", "PPP", false, "", "", points_data.toString(), output))
                }
            }
        }
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val mSnapHelper = PagerSnapHelper()
            binding?.privilegeCardList?.layoutManager = layoutManager
            val cardAdapter =
                PrivilegeCardAdapter(cardDataList, requireActivity(), preferences, this)
            binding?.privilegeCardList?.adapter = cardAdapter
            binding?.privilegeCardList?.onFlingListener = null
            mSnapHelper.attachToRecyclerView(binding?.privilegeCardList!!)
            binding?.privilegeCardList?.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        //Dragging
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        reviewPosition = getCurrentItem(binding?.privilegeCardList!!)
                        if (reviewPosition == 0) {
                            binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_loyalty)
                            if (cardDataList[reviewPosition].lock == true) {
                                binding?.unLockView?.show()
                                binding?.cardBelowView?.hide()
                                binding?.passportView?.hide()
                                binding?.titleUnlock?.text = "PVR Privilege"
                                binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_loyalty)
                            } else {
                                binding?.unLockView?.hide()
                                binding?.passportView?.hide()
                                binding?.cardBelowView?.show()
                            }
                        } else if (reviewPosition == 1) {
                            if (cardDataList[reviewPosition].lock == true) {
                                binding?.unLockView?.show()
                                binding?.cardBelowView?.hide()
                                binding?.passportView?.hide()
                                if (preferences.getString(SUBS_OPEN) == "true") {
                                    binding?.titleUnlock?.text = "PVR Passport"
                                    binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_passport)
                                } else {
                                    binding?.titleUnlock?.text = "Kotak Card"
                                    binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_kotak)
                                }
                            } else {
                                binding?.cardBelowView?.hide()
                                binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_kotak)
                                if (cardDataList[reviewPosition].type.equals("PPP")) {
                                    binding?.passportView?.hide()
                                    binding?.unLockView?.hide()
                                    binding?.cardBelowView?.show()
                                } else {
                                    if (preferences.getString(SUBS_OPEN) == "true") {
                                        binding?.passportView?.show()
                                        binding?.unLockView?.hide()
                                        binding?.cardBelowView?.hide()
                                        binding?.bookBtn?.isEnabled = true
                                        binding?.bookBtn?.isClickable = true
                                    } else {
                                        binding?.passportView?.show()
                                        binding?.unLockView?.hide()
                                        binding?.cardBelowView?.hide()
                                        binding?.bookBtn?.isEnabled = false
                                        binding?.bookBtn?.isClickable = false
                                    }
                                }
                            }
                        } else if (reviewPosition == 2) {
                            binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_kotak)
                            if (cardDataList[reviewPosition].lock == true) {
                                binding?.unLockView?.show()
                                binding?.cardBelowView?.hide()
                                binding?.passportView?.hide()
                                binding?.titleUnlock?.text = "Kotak Card"
                            } else {
                                binding?.passportView?.hide()
                                binding?.cardBelowView?.show()
                                binding?.unLockView?.hide()
                            }
                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
            if (!requireArguments().isEmpty && requireArguments().getString("type") != null && !requireArguments().getString("type").equals("", ignoreCase = true)) {
                for (i in cardDataList.indices) {
                    if (cardDataList[i].type.equals(requireArguments().getString("type"))) {
                        if (requireArguments().getString("type").equals("T", ignoreCase = true)) {
                            reviewPosition = 1
                            binding?.passportView?.show()
                            binding?.unLockView?.hide()
                            binding?.cardBelowView?.hide()
                            if (preferences.getString(SUBS_OPEN) != "true") {
                                binding?.bookBtn?.isEnabled = false
                                binding?.bookBtn?.isClickable = false
                            }
                            break
                        } else {
                            reviewPosition = i
                            if (cardDataList[i].type.equals("PP")) {
                                if (cardDataList[i].lock == true) {
                                    binding?.unLockView?.show()
                                    binding?.cardBelowView?.hide()
                                    binding?.passportView?.hide()
                                    binding?.titleUnlock?.text = "PVR Passport"
                                }
                            } else if (requireArguments().getString("type").equals("T", ignoreCase = true)) {
                                reviewPosition = 1
                                binding?.passportView?.show()
                                binding?.unLockView?.hide()
                                binding?.cardBelowView?.hide()
                                if (preferences.getString(SUBS_OPEN) != "true") {
                                    binding?.bookBtn?.isEnabled = false
                                    binding?.bookBtn?.isClickable = false
                                }
                            } else {
                                if (cardDataList[i].lock == true) {
                                    binding?.unLockView?.show()
                                    binding?.cardBelowView?.hide()
                                    binding?.titleUnlock?.text = "Kotak Card"
                                }
                            }
                        }
                        break
                    } else {
                        if (!requireArguments().isEmpty && requireArguments().getString("type").equals("T", ignoreCase = true)) {
                            reviewPosition = 1
                            binding?.passportView?.show()
                            binding?.unLockView?.hide()
                            binding?.cardBelowView?.hide()
                            if (preferences.getString(SUBS_OPEN) != "true") {
                                binding?.bookBtn?.isEnabled = false
                                binding?.bookBtn?.isClickable = false
                            }
                        } else {
                            if (!requireArguments().isEmpty && requireArguments().getString("type").equals("C", ignoreCase = true)) {
                                binding?.unLockView?.hide()
                                binding?.cardBelowView?.show()
                                binding?.passportView?.hide()
                                reviewPosition = 0
                            } else {
                                reviewPosition = i
                                if (cardDataList[i].type.equals("PP")) {
                                    if (cardDataList[i].lock == true) {
                                        binding?.unLockView?.show()
                                        binding?.cardBelowView?.hide()
                                        binding?.passportView?.hide()
                                        binding?.titleUnlock?.text = "PVR Passport"
                                    }
                                } else {
                                    if (cardDataList[i].lock == true) {
                                        binding?.unLockView?.show()
                                        binding?.cardBelowView?.hide()
                                        binding?.passportView?.hide()
                                        binding?.titleUnlock?.text = "Kotak Card"
                                    }
                                }
                            }
                        }
                    }
                }
                if (preferences.getString(SUBSCRIPTION_STATUS) == ACTIVE && requireArguments().getString("type").equals("C", ignoreCase = true)) {
                    reviewPosition = 1
                    binding?.unLockView?.hide()
                    binding?.cardBelowView?.hide()
                    binding?.passportView?.show()

                    if (preferences.getString(SUBS_OPEN) != "true") {
                        binding?.bookBtn?.isClickable = false
                        binding?.bookBtn?.isEnabled = false
                    }
                }
                binding?.privilegeCardList?.smoothScrollToPosition(reviewPosition)
            }
        }

    override fun onQrClick() {
        oPenDialogQR()
    }

    private fun createQr() {
        qrCode = Constant().getLoyaltyQr(preferences.geMobileNumber(), "180x180")
    }

    //Privilege Details
    private fun oPenDialogQR() {
        val dialogQR = Dialog(requireActivity())
        dialogQR.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogQR.setCancelable(false)
        dialogQR.setContentView(R.layout.activity_privilege_details)
        dialogQR.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialogQR.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialogQR.window?.setGravity(Gravity.CENTER)
        dialogQR.setTitle("")
        val pointsPcTextView = dialogQR.findViewById<TextView>(R.id.points_txt)
        val vochersPcTextView = dialogQR.findViewById<TextView>(R.id.vouchers_txt_)
        val TVusername: TextView = dialogQR.findViewById<View>(R.id.TVusername) as TextView
        val ivImage = dialogQR.findViewById<View>(R.id.ivImage) as ImageView
        val tvCross = dialogQR.findViewById<View>(R.id.tvCross) as ImageView
        Glide.with(requireActivity()).load(qrCode).into(ivImage)
        TVusername.text = preferences.getUserName()
        pointsPcTextView.text = Constant.PRIVILEGEPOINT
        vochersPcTextView.text = Constant.PRIVILEGEVOUCHER
        tvCross.setOnClickListener { dialogQR.dismiss() }
        dialogQR.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialogQR.dismiss()
            }
            true
        }
        dialogQR.show()
    }

    override fun onItemClick(nowShowingList: ArrayList<LoyaltyDataResponse.Voucher>, position: Int) {

        if (nowShowingList[position].type == "SUBSCRIPTION" && nowShowingList[position].st == "V"){
            val intent = Intent(context, PrivilegeDetailsActivity::class.java)
            startActivity(intent)
        }else {
            if (nowShowingList[position].st == "V")
                openDialog(getValidList(nowShowingList), position)
        }
    }

    private fun getValidList(nowShowingList: ArrayList<LoyaltyDataResponse.Voucher>): ArrayList<LoyaltyDataResponse.Voucher> {
        val list = ArrayList<LoyaltyDataResponse.Voucher>()
        for (item in nowShowingList){
            if (item.st == "V" && item.type != "SUBSCRIPTION"){
                list.add(item)
            }
        }
        return list
    }

    override fun onItemTermsClick(info: String) {
        showATDialog(context,info)
    }

    private fun showATDialog(mContext: Context?, format: String) {
        val dialog = Dialog(mContext!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.atinfo_dialoge)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        val tv: TextView = dialog.findViewById<View>(R.id.message) as TextView
        val cross: TextView = dialog.findViewById<View>(R.id.cross) as TextView
        val close: TextView = dialog.findViewById<View>(R.id.close) as TextView
        val dboxmessage: TextView = dialog.findViewById<View>(R.id.dboxmessage) as TextView
        val logot = dialog.findViewById<View>(R.id.logot) as ImageView
        close.setText("Close")
        val dboxview = dialog.findViewById<View>(R.id.dboxview)
        logot.hide()
        dboxview.hide()
        dboxmessage.hide()
        cross.text = "Terms & Conditions"
        tv.text = format.replace("\\|".toRegex(), "\n")
        cross.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        close.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        dialog.show()
    }

    private fun openDialog(voucherNewCombineLists: ArrayList<LoyaltyDataResponse.Voucher>, pos: Int) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.voc_detail_dailog_view)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setGravity(Gravity.CENTER)
        dialog?.setTitle("")
        val crossImageView = dialog?.findViewById<ImageView>(R.id.tvCross)
        crossImageView?.setOnClickListener { dialog?.dismiss() }
        val adapter: ViewPagerAdapter =
            ViewPagerAdapter(requireContext(), voucherNewCombineLists)
        val pager: CustomViewPager =
            dialog?.findViewById<View>(R.id.overlap_pager) as CustomViewPager
        pager.adapter = adapter
        pager.currentItem = pos
        val bundle1 = Bundle()
        dialog?.show()
    }

    class ViewPagerAdapter(
        private val mContext: Context,
        voucherNewCombineLists: java.util.ArrayList<LoyaltyDataResponse.Voucher>
    ) :
        PagerAdapter() {
        var voucherNewCombineLists: java.util.ArrayList<LoyaltyDataResponse.Voucher>

        init {
            this.voucherNewCombineLists = voucherNewCombineLists
        }

        override fun getCount(): Int {
            return if (voucherNewCombineLists.size > 15) {
                voucherNewCombineLists.size / 2
            } else {
                voucherNewCombineLists.size
            }
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as RelativeLayout
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as RelativeLayout)
        }

        @SuppressLint("SetTextI18n")
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView: View = LayoutInflater.from(mContext)
                .inflate(R.layout.privilege_voc_detail_item, container, false)
            val qrImageView = itemView.findViewById(R.id.qr_code_pv) as ImageView
            val qrCodeTextView: TextView = itemView.findViewById(R.id.qr_text) as TextView
            val vocTypeDetailsTextView: TextView = itemView.findViewById(R.id.voc_type_details) as TextView
            val detailsVocTextView: TextView = itemView.findViewById(R.id.details_voc) as TextView
            //PCTextView subDetailsTextView=itemView.findViewById(R.id.voc_sub_details);
            val exPcTextView: TextView = itemView.findViewById(R.id.ex_date) as TextView
            val textValid: TextView = itemView.findViewById(R.id.text_valid) as TextView
            PrivilegeVochersAdapter.generateQrcode(qrImageView).execute(
                voucherNewCombineLists[position].cd
            )
            qrCodeTextView.text = voucherNewCombineLists[position].cd
            vocTypeDetailsTextView.text = voucherNewCombineLists[position].tpt
            detailsVocTextView.text = Html.fromHtml(
                Html.fromHtml(
                    voucherNewCombineLists[position].itd
                ).toString()
            ).toString() + " " + voucherNewCombineLists[position].itn
            // subDetailsTextView.setText(voucherNewCombineLists.get(position).getVoucherVo().getItn());
            if (getDays( voucherNewCombineLists[position].ex) > 1) {
                exPcTextView.text = getDays(voucherNewCombineLists[position].ex).toString() + " days left"
            } else {
                exPcTextView.text = getDays(voucherNewCombineLists[position].ex).toString() + " day left"
            }
            textValid.text =
                "Valid till " + Constant.getDate(
                    "MMM dd yyyy hh:mma",
                    "MMM dd, yyyy",
                    voucherNewCombineLists[position].ex.replace("  ".toRegex(), " ")
                )

            container.addView(itemView)
            return itemView
        }
    }

    companion object{
        fun getDays(ex:String):Int{
                var vocEx: Date? = null
                val today = Calendar.getInstance().time
            val simpleDateFormat = SimpleDateFormat("MMM dd yyyy hh:mma", Locale.US)
            try {
                    vocEx = simpleDateFormat.parse(ex)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            return if (today.time <= (vocEx?.time ?: 0)) {
                val l = vocEx!!.time - today.time
                val days = (l / (1000 * 60 * 60 * 24)).toInt()
                println("days--->$days")
                days
            } else {
                0
            }

        }
    }

    private fun openHowToWork() {
        storyDialog = Dialog(requireContext())
        storyDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        storyDialog?.setCancelable(false)
        storyDialog?.setContentView(R.layout.how_it_work_layout)
        storyDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        storyDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        storyDialog?.window?.setGravity(Gravity.CENTER)
        storyDialog?.setTitle("")
        val RlBanner = storyDialog?.findViewById<View>(R.id.RlBannerl) as RelativeLayout
        RlBanner.visibility = View.VISIBLE
        storiesProgressView = storyDialog?.findViewById<View>(R.id.storiesl) as StoriesProgressView
        storiesProgressView?.setStoriesCount(Constant.PrivilegeHomeResponseConst?.st?.size!!) // <- set stories
        storiesProgressView?.setStoryDuration(5000L) // <- set a story duration
        storiesProgressView?.setStoriesListener(this@MemberFragment) // <- set listener
        storiesProgressView?.startStories() // <- start progress
        counterStory = 0
        val ivplay = storyDialog?.findViewById<View>(R.id.iv_play) as LinearLayout
        val tv_button = storyDialog?.findViewById<View>(R.id.tv_button) as TextView
        val ivCross: TextView = storyDialog?.findViewById<View>(R.id.crossText) as TextView
        ivCross.setOnClickListener(View.OnClickListener {
            storiesProgressView?.destroy()
            currentPage = 0
            storyDialog?.dismiss()
        })
        ivplay.visibility = View.GONE
        tv_button.visibility = View.GONE
        ivBanner = storyDialog?.findViewById<View>(R.id.storyList) as RecyclerView
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        ivBanner?.layoutManager = layoutManager
        recyclerAdapter = HowItWorkAdapter(Constant.PrivilegeHomeResponseConst?.st!!, requireActivity(), this)
        ivBanner?.adapter = recyclerAdapter
        recyclerAdapter?.notifyDataSetChanged()
        val reverse: View = storyDialog?.findViewById<View>(R.id.reversel) as View
        reverse.setOnClickListener { storiesProgressView?.reverse() }
        reverse.setOnTouchListener(onTouchListener)

        // bind skip view
        val skip: View = storyDialog?.findViewById<View>(R.id.skipl) as View
        skip.setOnClickListener { // Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();
            storiesProgressView?.skip()
        }
        skip.setOnTouchListener(onTouchListener)
        storyDialog?.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                storiesProgressView!!.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                storiesProgressView!!.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }
    override fun onNext() {
        if (!TextUtils.isEmpty(Constant.PrivilegeHomeResponseConst?.st?.get(counterStory)?.pi)) {
            ++counterStory
            ivBanner!!.smoothScrollToPosition(counterStory)
        }
    }

    override fun onPrev() {
        if (counterStory - 1 < 0) return
        if (!TextUtils.isEmpty(Constant.PrivilegeHomeResponseConst?.st?.get(counterStory)?.pi)) {
            --counterStory
            ivBanner?.smoothScrollToPosition(counterStory)
        }
    }

    override fun onComplete() {
        storiesProgressView?.destroy()
        currentPage = 0
        if (storyDialog != null && storyDialog?.isShowing == true) {
            storyDialog?.dismiss()
        }
        storiesProgressView?.startStories()
    }

    override fun openRedirection() {

    }


    private fun showDialogLoyalty(mContext: Context?, price1: String, id: String) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Privilege")
            bundle.putString("ecommerce values will be pass", "")

            GoogleAnalytics.hitEvent(requireActivity(), "passport_purchase", bundle)
            GoogleAnalytics.hitPurchaseEvent(requireContext(),
                id,price1,"Passport", 1)
        }catch (e:Exception) {
            e.printStackTrace()
        }

        val dialog = BottomSheetDialog(mContext!!, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.payment_success_gc_pp)
        dialog.setCancelable(true)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        val referenceNo = dialog.findViewById<View>(R.id.refrenceNo) as TextView?
        val price = dialog.findViewById<View>(R.id.price) as TextView?
        val paidDT = dialog.findViewById<View>(R.id.paidDT) as TextView?
        val titleText = dialog.findViewById<View>(R.id.titleText) as TextView?
        val dic = dialog.findViewById<View>(R.id.dic) as TextView?
        val btn = dialog.findViewById<View>(R.id.btn) as CardView?
        val logo = dialog.findViewById<View>(R.id.logo) as ImageView?
        val c = Calendar.getInstance().time
        println("Current time => $c")
        titleText?.text = "Thanks for purchasing your PVR Gift Card!"
        dic?.text = "Redirecting you to your active gift card page now! We shall be reviewing the uploaded image & will update you in the next 24 hours!"
        val df = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val formattedDate = df.format(c)
        price!!.text = "₹$price1"
        paidDT!!.text = "Paid on: $formattedDate"
        referenceNo!!.text = Html.fromHtml("Order ID:<br></br>$id")
        btn?.show()
        btn?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }



}