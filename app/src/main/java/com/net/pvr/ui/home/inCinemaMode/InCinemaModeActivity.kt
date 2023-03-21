package com.net.pvr.ui.home.inCinemaMode

import android.R.attr.targetPackage
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityInCinemaModeBinding
import com.net.pvr.databinding.IntervalTimingItemBinding
import com.net.pvr.databinding.MovieDetailsItemBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.GridAutoFitLayoutManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr.ui.home.fragment.privilege.NonMemberActivity
import com.net.pvr.ui.home.fragment.privilege.adapter.HowItWorkAdapter
import com.net.pvr.ui.home.fragment.privilege.adapter.PrivilegeCardAdapter
import com.net.pvr.ui.home.fragment.privilege.response.LoyaltyDataResponse
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeCardData
import com.net.pvr.ui.login.LoginActivity
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.ga.GoogleAnalytics
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class InCinemaModeActivity : AppCompatActivity(),
    PrivilegeCardAdapter.RecycleViewItemClickListener, StoriesProgressView.StoriesListener,
    HowItWorkAdapter.RecycleViewItemClickListener {
    private var storyDialog: Dialog? = null
    private var ivBanner: RecyclerView? = null
    private var cardAdapter: PrivilegeCardAdapter? = null
    private var cardDataList: ArrayList<PrivilegeCardData> = arrayListOf()
    private var storiesProgressView: StoriesProgressView? = null
    private var counterStory = 0
    private var qrCode: String = ""
    private var currentPage = 1
    private var pressTime = 0L
    private var recyclerAdapter: HowItWorkAdapter? = null
    private var limit = 500L

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityInCinemaModeBinding? = null
    private val authViewModel: HomeViewModel by viewModels()
    private var loader: LoaderDialog? = null
    val mSnapHelper = PagerSnapHelper()
    private var orderAdapter: GroupAdapter<ViewHolder>? = null
    private val movieDetailsAdapter: GenericRecyclerViewAdapter<String> by lazy { createMovieDetailAdapter() }
    private val intervalAdadapter: GenericRecyclerViewAdapter<String> by lazy { createIntervalTimingAdapter() }
    private var mIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIntent = intent
        binding = ActivityInCinemaModeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initData()
        initUI()
        setClickListeners()
    }


    private fun setStatusBarColor() {
        val window: Window = getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black_111111))
    }

    private fun setClickListeners() {
        binding?.shareBtn?.setOnClickListener {
            val newUrl = "https://www.pvrcinemas.com/loyalty/home"
            Constant.onShareClick(
                this@InCinemaModeActivity,
                newUrl,
                "The secret to my movie binge-watching: PVR Passport voucher!\nCheck it out:"
            )
        }
        if (mIntent != null && mIntent!!.hasExtra("from") && !mIntent!!.getStringExtra("from")
                .isNullOrEmpty()
        ) {
            showDialogLoyalty(
                this@InCinemaModeActivity,
                mIntent!!.getStringExtra("amt").toString(),
                mIntent!!.getStringExtra("id").toString()
            )
        }
        binding?.bookBtn?.setOnClickListener {
            val intent = Intent(this@InCinemaModeActivity, HomeActivity::class.java)
            startActivity(intent)
        }
        binding?.howWork?.setOnClickListener { openHowToWork() }

        binding?.faq?.setOnClickListener {
            val intent = Intent(this@InCinemaModeActivity, WebViewActivity::class.java)
            intent.putExtra("from", "privilege")
            intent.putExtra("title", "Passport FAQ")
            intent.putExtra("getUrl", Constant.PrivilegeHomeResponseConst?.faq)
            startActivity(intent)
        }

        binding?.unLockView?.setOnClickListener {
            if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 1) {
                val intent1 = Intent(this@InCinemaModeActivity, NonMemberActivity::class.java)
                intent1.putExtra("data", Constant.PrivilegeHomeResponseConst?.pinfo)
                intent1.putExtra("type", Constant.PrivilegeHomeResponseConst?.pinfo?.get(0)?.ptype)
                startActivity(intent1)
            } else {
                HomeActivity.reviewPosition =
                    if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 2 && HomeActivity.reviewPosition == 2) {
                        1
                    } else {
                        0
                    }
                if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(HomeActivity.reviewPosition)?.ptype == ("PP")) {
                    if (!preferences.getIsLogin()) {
                        val intent4 = Intent(this@InCinemaModeActivity, LoginActivity::class.java)
                        intent4.putExtra(
                            Constant.PCBackStackActivity.OPEN_ACTIVITY_NAME,
                            Constant.PCBackStackActivity.LOYALITY_UNLIMITED_ACTIVITY
                        )
                        (this@InCinemaModeActivity.startActivity(intent4))
                    } else {
                        val intent1 =
                            Intent(this@InCinemaModeActivity, NonMemberActivity::class.java)
                        intent1.putExtra("data", Constant.PrivilegeHomeResponseConst?.pinfo)
                        intent1.putExtra("type", "PP")
                        intent1.putExtra(
                            Constant.PCBackStackActivity.OPEN_ACTIVITY_NAME,
                            Constant.PCBackStackActivity.LANDING_ACTIVITY
                        )
                        startActivity(intent1)
                    }
                } else {
                    val intent1 = Intent(this@InCinemaModeActivity, NonMemberActivity::class.java)
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
    }

    private fun openHowToWork() {
        storyDialog = Dialog(this@InCinemaModeActivity)
        storyDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        storyDialog?.setCancelable(false)
        storyDialog?.setContentView(R.layout.how_it_work_layout)
        storyDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        storyDialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        storyDialog?.window?.setGravity(Gravity.CENTER)
        storyDialog?.setTitle("")
        val RlBanner = storyDialog?.findViewById<View>(R.id.RlBannerl) as RelativeLayout
        RlBanner.visibility = View.VISIBLE
        storiesProgressView = storyDialog?.findViewById<View>(R.id.storiesl) as StoriesProgressView
        storiesProgressView?.setStoriesCount(Constant.PrivilegeHomeResponseConst?.st?.size!!) // <- set stories
        storiesProgressView?.setStoryDuration(5000L) // <- set a story duration
        storiesProgressView?.setStoriesListener(this@InCinemaModeActivity) // <- set listener
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
            LinearLayoutManager(this@InCinemaModeActivity, LinearLayoutManager.HORIZONTAL, false)
        ivBanner?.layoutManager = layoutManager
        recyclerAdapter = HowItWorkAdapter(
            Constant.PrivilegeHomeResponseConst?.st!!,
            this@InCinemaModeActivity,
            this
        )
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
    private val onTouchListener = View.OnTouchListener { v, event ->
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

    private fun showDialogLoyalty(mContext: Context?, price1: String, id: String) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Privilege")
            bundle.putString("ecommerce values will be pass", "")

            GoogleAnalytics.hitEvent(this@InCinemaModeActivity, "passport_purchase", bundle)
            GoogleAnalytics.hitPurchaseEvent(
                this@InCinemaModeActivity,
                id, price1, "Passport", 1
            )
        } catch (e: Exception) {
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
        dic?.text =
            "Redirecting you to your active gift card page now! We shall be reviewing the uploaded image & will update you in the next 24 hours!"
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

    private fun initUI() {
        binding!!.rvSeatNumber.layoutManager = GridLayoutManager(this, 7)
        binding!!.rvSeatNumber.adapter = movieDetailsAdapter
        binding!!.rvIntervalTiming.addItemDecoration(RecyclerViewMargin(14, 1))
//        binding!!.rvSeatNumber.addItemDecoration(GridSpacingItemDecoration(7,20,false))

        binding!!.rvSeatNumber.layoutManager=GridAutoFitLayoutManager(this@InCinemaModeActivity,46,LinearLayoutManager.VERTICAL,false)
        binding!!.rvFoodandbevrages.addItemDecoration(RecyclerViewMarginFoodOrder(30, 1))
        binding!!.rvIntervalTiming.adapter = intervalAdadapter
        LinearSnapHelper().attachToRecyclerView(binding!!.rvIntervalTiming)
        orderAdapter = GroupAdapter<ViewHolder>()
        var orders: MutableList<Order> = mutableListOf(Order(), Order(), Order())
        orders.forEach {
            orderAdapter!!.add(OrderItemView(context = this@InCinemaModeActivity, it))
        }
        binding!!.rvFoodandbevrages.adapter = orderAdapter
        val layoutManager =
            LinearLayoutManager(this@InCinemaModeActivity, LinearLayoutManager.VERTICAL, false)

        binding?.privilegeCardList?.layoutManager = layoutManager
        cardAdapter =
            PrivilegeCardAdapter(cardDataList!!, this@InCinemaModeActivity, preferences, this)
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
                    HomeActivity.reviewPosition =
                        HomeActivity.getCurrentItem(binding?.privilegeCardList!!)
                    if (HomeActivity.reviewPosition == 0) {
                        binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_loyalty)
                        if (cardDataList!![HomeActivity.reviewPosition].lock == true) {
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
                    } else if (HomeActivity.reviewPosition == 1) {
                        if (cardDataList!![HomeActivity.reviewPosition].lock == true) {
                            binding?.unLockView?.show()
                            binding?.cardBelowView?.hide()
                            binding?.passportView?.hide()
                            if (preferences.getString(Constant.SharedPreference.SUBS_OPEN) == "true") {
                                binding?.titleUnlock?.text = "PVR Passport"
                                binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_passport)
                            } else {
                                binding?.titleUnlock?.text = "Kotak Card"
                                binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_kotak)
                            }
                        } else {
                            binding?.cardBelowView?.hide()
                            binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_kotak)
                            if (cardDataList!![HomeActivity.reviewPosition].type.equals("PPP")) {
                                binding?.passportView?.hide()
                                binding?.unLockView?.hide()
                                binding?.cardBelowView?.show()
                            } else {
                                if (preferences.getString(Constant.SharedPreference.SUBS_OPEN) == "true") {
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
                    } else if (HomeActivity.reviewPosition == 2) {
                        binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_kotak)
                        if (cardDataList!![HomeActivity.reviewPosition].lock == true) {
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
    }

    private fun retrieveData(output: LoyaltyDataResponse.Output) {
//        binding?.scrollView?.show()
        try {
//            addLoyalty(output)
            updateHeaderView(output)
//            manageTabs(output)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateHeaderView(output: LoyaltyDataResponse.Output) {

        val points_data = Constant.PRIVILEGEPOINT
        cardDataList = ArrayList()
        var count = 3
        count =
            if (preferences.getString(Constant.SharedPreference.SUBS_OPEN) != "true" && preferences.getString(
                    Constant.SharedPreference.SUBSCRIPTION_STATUS
                ) != Constant.SharedPreference.ACTIVE
            ) {
                2
            } else {
                3
            }
        for (i in 0 until count) {
            if (i == 0) {
                cardDataList!!.add(PrivilegeCardData("", "P", false, "", "", points_data, output))
            } else if (i == 1) {
                if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == Constant.SharedPreference.ACTIVE) {
                    cardDataList!!.add(
                        PrivilegeCardData(
                            java.lang.String.valueOf(output.subscription.can_redeem - output.subscription.redeemed),
                            "PP",
                            false,
                            "",
                            "",
                            points_data,
                            output
                        )
                    )
                } else {
                    if (preferences.getString(Constant.SharedPreference.SUBS_OPEN) == "true") {
                        cardDataList!!.add(
                            PrivilegeCardData(
                                Constant.PrivilegeHomeResponseConst?.pinfo?.get(
                                    0
                                )?.pi.toString(), "PP", true, "", "", points_data, output
                            )
                        )
                    } else {
                        if (preferences.getString(Constant.SharedPreference.LOYALITY_STATUS) == "PPP") {
                            cardDataList!!.add(
                                PrivilegeCardData(
                                    "",
                                    "PPP",
                                    false,
                                    "",
                                    "",
                                    points_data.toString(),
                                    output
                                )
                            )
                        } else {
                            cardDataList!!.add(
                                PrivilegeCardData(
                                    Constant.PrivilegeHomeResponseConst?.pinfo?.get(0)?.pi.toString(),
                                    "PPP",
                                    true,
                                    "",
                                    "",
                                    points_data.toString(),
                                    output
                                )
                            )
                        }
                    }
                }
            } else {
                if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 2) {
                    if (preferences.getString(Constant.SharedPreference.LOYALITY_STATUS) == "PPP") {
                        cardDataList!!.add(
                            PrivilegeCardData(
                                "",
                                "PPP",
                                false,
                                "",
                                "",
                                points_data.toString(),
                                output
                            )
                        )
                    } else {
                        cardDataList!!.add(
                            PrivilegeCardData(
                                Constant.PrivilegeHomeResponseConst?.pinfo?.get(1)?.pi.toString(),
                                "PPP",
                                true,
                                "",
                                "",
                                points_data.toString(),
                                output
                            )
                        )
                    }
                } else if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 1) {
                    if (preferences.getString(Constant.SharedPreference.LOYALITY_STATUS) == "PPP") {
                        cardDataList!!.add(
                            PrivilegeCardData(
                                "",
                                "PPP",
                                false,
                                "",
                                "",
                                points_data.toString(),
                                output
                            )
                        )
                    } else {
                        cardDataList!!.add(
                            PrivilegeCardData(
                                Constant.PrivilegeHomeResponseConst?.pinfo?.get(0)?.pi.toString(),
                                "PPP",
                                true,
                                "",
                                "",
                                points_data.toString(),
                                output
                            )
                        )
                    }
                } else if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 0) {
                    cardDataList!!.add(
                        PrivilegeCardData(
                            "",
                            "PPP",
                            false,
                            "",
                            "",
                            points_data.toString(),
                            output
                        )
                    )
                }
            }
        }
        val layoutManager =
            LinearLayoutManager(this@InCinemaModeActivity, LinearLayoutManager.HORIZONTAL, false)
        val mSnapHelper = PagerSnapHelper()
        binding?.privilegeCardList?.layoutManager = layoutManager
        val cardAdapter =
            PrivilegeCardAdapter(cardDataList!!, this@InCinemaModeActivity, preferences, this)
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
                    HomeActivity.reviewPosition =
                        HomeActivity.getCurrentItem(binding?.privilegeCardList!!)
                    if (HomeActivity.reviewPosition == 0) {
//                        binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_loyalty)
                        if (cardDataList!![HomeActivity.reviewPosition].lock == true) {
//                            binding?.unLockView?.show()
//                            binding?.cardBelowView?.hide()
//                            binding?.passportView?.hide()
//                            binding?.titleUnlock?.text = "PVR Privilege"
//                            binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_loyalty)
                        } else {
//                            binding?.unLockView?.hide()
//                            binding?.passportView?.hide()
//                            binding?.cardBelowView?.show()
                        }
                    } else if (HomeActivity.reviewPosition == 1) {
                        if (cardDataList!![HomeActivity.reviewPosition].lock == true) {
//                            binding?.unLockView?.show()
//                            binding?.cardBelowView?.hide()
//                            binding?.passportView?.hide()
                            if (preferences.getString(Constant.SharedPreference.SUBS_OPEN) == "true") {
//                                binding?.titleUnlock?.text = "PVR Passport"
//                                binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_passport)
                            } else {
//                                binding?.titleUnlock?.text = "Kotak Card"
//                                binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_kotak)
                            }
                        } else {
//                            binding?.cardBelowView?.hide()
//                            binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_kotak)
                            if (cardDataList!![HomeActivity.reviewPosition].type.equals("PPP")) {
//                                binding?.passportView?.hide()
//                                binding?.unLockView?.hide()
//                                binding?.cardBelowView?.show()
                            } else {
                                if (preferences.getString(Constant.SharedPreference.SUBS_OPEN) == "true") {
//                                    binding?.passportView?.show()
//                                    binding?.unLockView?.hide()
//                                    binding?.cardBelowView?.hide()
//                                    binding?.bookBtn?.isEnabled = true
//                                    binding?.bookBtn?.isClickable = true
                                } else {
//                                    binding?.passportView?.show()
//                                    binding?.unLockView?.hide()
//                                    binding?.cardBelowView?.hide()
//                                    binding?.bookBtn?.isEnabled = false
//                                    binding?.bookBtn?.isClickable = false
                                }
                            }
                        }
                    } else if (HomeActivity.reviewPosition == 2) {
//                        binding?.rlImgContainer?.setBackgroundResource(R.drawable.gradient_kotak)
                        if (cardDataList!![HomeActivity.reviewPosition].lock == true) {
//                            binding?.unLockView?.show()
//                            binding?.cardBelowView?.hide()
//                            binding?.passportView?.hide()
//                            binding?.titleUnlock?.text = "Kotak Card"
                        } else {
//                            binding?.passportView?.hide()
//                            binding?.cardBelowView?.show()
//                            binding?.unLockView?.hide()
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        if (mIntent!! != null && mIntent!!.getStringExtra("type") != null && !mIntent!!.getStringExtra("type")
                .equals("", ignoreCase = true)
        ) {
            for (i in cardDataList!!.indices) {
                if (cardDataList!![i].type.equals(mIntent!!.getStringExtra("type"))) {
                    if (mIntent!!.getStringExtra("type").equals("T", ignoreCase = true)) {
                        HomeActivity.reviewPosition = 1
                        binding?.passportView?.show()
                        binding?.unLockView?.hide()
                        binding?.cardBelowView?.hide()
                        if (preferences.getString(Constant.SharedPreference.SUBS_OPEN) != "true") {
                            binding?.bookBtn?.isEnabled = false
                            binding?.bookBtn?.isClickable = false
                        }
                        break
                    } else {
                        HomeActivity.reviewPosition = i
                        if (cardDataList!![i].type.equals("PP")) {
                            if (cardDataList!![i].lock == true) {
                                binding?.unLockView?.show()
                                binding?.cardBelowView?.hide()
                                binding?.passportView?.hide()
                                binding?.titleUnlock?.text = "PVR Passport"
                            }
                        } else if (mIntent!!.getStringExtra("type").equals("T", ignoreCase = true)) {
                            HomeActivity.reviewPosition = 1
                            binding?.passportView?.show()
                            binding?.unLockView?.hide()
                            binding?.cardBelowView?.hide()
                            if (preferences.getString(Constant.SharedPreference.SUBS_OPEN) != "true") {
                                binding?.bookBtn?.isEnabled = false
                                binding?.bookBtn?.isClickable = false
                            }
                        } else {
                            if (cardDataList!![i].lock == true) {
                                binding?.unLockView?.show()
                                binding?.cardBelowView?.hide()
                                binding?.titleUnlock?.text = "Kotak Card"
                            }
                        }
                    }
                    break
                } else {
                    if (getIntent() != null && mIntent!!.getStringExtra("type")
                            .equals("T", ignoreCase = true)
                    ) {
                        HomeActivity.reviewPosition = 1
                        binding?.passportView?.show()
                        binding?.unLockView?.hide()
                        binding?.cardBelowView?.hide()
                        if (preferences.getString(Constant.SharedPreference.SUBS_OPEN) != "true") {
                            binding?.bookBtn?.isEnabled = false
                            binding?.bookBtn?.isClickable = false
                        }
                    } else {
                        if (getIntent() != null && mIntent!!.getStringExtra("type")
                                .equals("C", ignoreCase = true)
                        ) {
                            binding?.unLockView?.hide()
                            binding?.cardBelowView?.show()
                            binding?.passportView?.hide()
                            HomeActivity.reviewPosition = 0
                        } else {
                            HomeActivity.reviewPosition = i
                            if (cardDataList!![i].type.equals("PP")) {
                                if (cardDataList!![i].lock == true) {
                                    binding?.unLockView?.show()
                                    binding?.cardBelowView?.hide()
                                    binding?.passportView?.hide()
                                    binding?.titleUnlock?.text = "PVR Passport"
                                }
                            } else {
                                if (cardDataList!![i].lock == true) {
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
            if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == Constant.SharedPreference.ACTIVE && mIntent!!.getStringExtra(
                    "type"
                ).equals("C", ignoreCase = true)
            ) {
                HomeActivity.reviewPosition = 1
                binding?.unLockView?.hide()
                binding?.cardBelowView?.hide()
                binding?.passportView?.show()

                if (preferences.getString(Constant.SharedPreference.SUBS_OPEN) != "true") {
                    binding?.bookBtn?.isClickable = false
                    binding?.bookBtn?.isEnabled = false
                }
            }
            binding?.privilegeCardList?.smoothScrollToPosition(HomeActivity.reviewPosition)
        }
    }

    private fun initData() {
        movieDetailsAdapter.submitList(
            mutableListOf(
                "D10",
                "D11",
                "D12",
                "D13",
                "D14",
                "D15",
                "D16",
                "D10",
                "D11",
                "D12",
                "D13",
                "D14",
                "D15",
                "D16",
                "D12",
                "D13",
                "D14",
                "D15"
            )
        )
        intervalAdadapter.submitList(mutableListOf("D10", "D11", "D12", "D13", "D14", "D15", "D16"))
    }

    private fun createMovieDetailAdapter() = GenericRecyclerViewAdapter(
        getViewLayout = { R.layout.movie_details_item },
        areItemsSame = ::isMovieDetailSame,
        areItemContentsEqual = ::isMovieDetailSame,
        onBind = { ratingTag, viewDataBinding, _ ->
            with(viewDataBinding as MovieDetailsItemBinding) {
                textviewSeatNumber.text = ratingTag
            }
        }
    )

    fun isMovieDetailSame(any: String, any1: String): Boolean {
        return false
    }

    private fun createIntervalTimingAdapter() = GenericRecyclerViewAdapter(
        getViewLayout = { R.layout.interval_timing_item },
        areItemsSame = ::isIntervalTimingSame,
        areItemContentsEqual = ::isIntervalTimingSame,
        onBind = { ratingTag, viewDataBinding, _ ->
            with(viewDataBinding as IntervalTimingItemBinding) {

            }
        }
    )

    fun isIntervalTimingSame(any: String, any1: String): Boolean {
        return false
    }

    override fun onQrClick() {
        oPenDialogQR()

    }

    private fun oPenDialogQR() {
        val dialogQR = Dialog(this@InCinemaModeActivity)
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
        Glide.with(this@InCinemaModeActivity).load(qrCode).into(ivImage)
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
        TODO("Not yet implemented")
    }
    fun isPackageExisted(targetPackage: String?): Boolean {
        val pm = packageManager
        try {
            val info = pm.getPackageInfo(targetPackage!!, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }
    fun openAppOrPlaySotre(packageId:String, playStoreAppUrl:String="market://details?id=com.ubercab", uri: String="uber://?action=setPickup&pickup=my_location", playStoreUrl:String="http://play.google.com/store/apps/details?id=com.olacabs.customer"){
        var pm = packageManager
        if(isPackageExisted(packageId)){
            try {
                pm.getPackageInfo(packageId, PackageManager.GET_ACTIVITIES)
                val uri = uri
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(uri)
                startActivity(intent)
            }
            catch (e: PackageManager.NameNotFoundException) {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(playStoreAppUrl)
                        )
                    )
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(playStoreUrl)
                        )
                    )
                }
            }
        }
        else{
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(playStoreAppUrl)
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(playStoreUrl)
                    )
                )
            }
        }
    }
}


