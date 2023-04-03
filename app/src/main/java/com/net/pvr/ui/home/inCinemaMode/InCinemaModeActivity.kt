package com.net.pvr.ui.home.inCinemaMode

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.*
import android.webkit.GeolocationPermissions
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.*
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.GridAutoFitLayoutManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.food.FoodActivity
import com.net.pvr.ui.home.fragment.home.response.FeedbackDataResponse
import com.net.pvr.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr.ui.home.fragment.privilege.adapter.HowItWorkAdapter
import com.net.pvr.ui.home.fragment.privilege.adapter.PrivilegeCardAdapter
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeCardData
import com.net.pvr.ui.home.inCinemaMode.adapters.TicketPlaceHolderAdapter
import com.net.pvr.ui.home.inCinemaMode.response.*
import com.net.pvr.ui.scanner.ScannerActivity
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
    private var mCinemaData: Output? = null
    private var ivCross: TextView? = null
    private var tv_button: TextView? = null
    private var ivplay: LinearLayout? = null
    private var rlBanner: RelativeLayout? = null
    private var layoutManager: LinearLayoutManager? = null
    private var bookingIdList: MutableList<String>? = mutableListOf()
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
    private var currentBooking: Int = 0
    var intent_ready_to_leave: Intent? = null
    private var mGeoLocationCallback: GeolocationPermissions.Callback? = null
    private var mGeoLocationRequestOrigin: String? = null
    private val MY_PERMISSIONS_REQUEST_LOCATION: Int = 111

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityInCinemaModeBinding? = null
    private val authViewModel: HomeViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private val orderAdapter: GroupAdapter<ViewHolder> = GroupAdapter<ViewHolder>()
    private val seatsAdapter: GenericRecyclerViewAdapter<String> by lazy { createSeatsAdapter() }
    private val intervalAdadapter: GenericRecyclerViewAdapter<ShowData> by lazy { createIntervalTimingAdapter() }
    private val inCinemaTypesAdadapter: GenericRecyclerViewAdapter<IncinemaType> by lazy { createInCinemaTypesAdapter() }
    private var mIntent: Intent? = null
    var dialog: OptionDialog? = null
    private var noDataDialog: OptionDialog? = null
    private var inCinemaPageData: InCinemaResp? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIntent = intent
        binding = ActivityInCinemaModeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initUI()
        setClickListeners()
        createQr()
        binding?.textViewHowItWorks?.setOnClickListener {
            if (mCinemaData?.inCinemaResp?.popups?.isNotEmpty() == true) {
                openHowToWork()
            }
        }
        getInCinemaMode()
    }


    private fun setStatusBarColor() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black_111111)
    }

    private fun createQr() {
        qrCode = Constant().getLoyaltyQr(preferences.geMobileNumber(), "180x180")
    }

    private fun setClickListeners() {
        binding?.toolbar?.apply {
            imageViewQRCode.setOnClickListener {
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home Screen")
//                bundle.putString("var_header_search", "")
                    GoogleAnalytics.hitEvent(this@InCinemaModeActivity, "hearder_qr_code", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val intent = Intent(this@InCinemaModeActivity, ScannerActivity::class.java)
                startActivity(intent)
            }
        }
        binding?.apply {
            imageviewPreviousBookedMovie.setOnClickListener {
                if (currentBooking > 0) {
                    currentBooking--
                    getBookingInfo(bookingIdList!![currentBooking], preferences.getCityName())
                }
            }
            imageviewNextBookedMovie.setOnClickListener {
                if (currentBooking < bookingIdList!!.size - 1) {
                    currentBooking++
                    getBookingInfo(bookingIdList!![currentBooking], preferences.getCityName())
                }
            }
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
    }

    private fun openHowToWork() {
        storyDialog = Dialog(this@InCinemaModeActivity)
        storyDialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.how_it_work_layout)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setGravity(Gravity.CENTER)
            }
            setTitle("")
            rlBanner = (findViewById<View>(R.id.RlBannerl) as RelativeLayout).also {
                it.visibility = View.VISIBLE
            }
            storiesProgressView = (findViewById<View>(R.id.storiesl) as StoriesProgressView).also {
                it.apply {
                    setStoriesCount(Constant.PrivilegeHomeResponseConst?.st?.size!!)
                    setStoryDuration(5000L)
                    setStoriesListener(this@InCinemaModeActivity)
                    startStories()
                }
            }
            counterStory = 0
            ivplay = findViewById<View>(R.id.iv_play) as LinearLayout
            tv_button = findViewById<View>(R.id.tv_button) as TextView
            ivCross = (findViewById<View>(R.id.crossText) as TextView).also {
                it.setOnClickListener(View.OnClickListener {
                    storiesProgressView?.destroy()
                    currentPage = 0
                    storyDialog?.dismiss()
                })
            }
            ivplay!!.visibility = View.GONE
            tv_button!!.visibility = View.GONE
            ivBanner = findViewById<View>(R.id.storyList) as RecyclerView

            val layoutManager =
                LinearLayoutManager(
                    this@InCinemaModeActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            ivBanner?.layoutManager = layoutManager
            recyclerAdapter = HowItWorkAdapter(
                mCinemaData?.inCinemaResp?.st!!,
                this@InCinemaModeActivity,
                this@InCinemaModeActivity
            )
            ivBanner?.adapter = recyclerAdapter
            recyclerAdapter?.notifyDataSetChanged()
            val reverse: View = findViewById<View>(R.id.reversel)
            reverse.setOnClickListener { storiesProgressView?.reverse() }
            reverse.setOnTouchListener(onTouchListener)

            // bind skip view
            val skip: View = findViewById(R.id.skipl)
            skip.setOnClickListener {
                // Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();
                storiesProgressView?.skip()
            }
            skip.setOnTouchListener(onTouchListener)
            show()
        }
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
        cardAdapter =
            PrivilegeCardAdapter(cardDataList, this@InCinemaModeActivity, preferences, this)
        binding?.apply {
            rvSeatNumber.layoutManager = GridLayoutManager(this@InCinemaModeActivity, 7)
            layoutManager =
                LinearLayoutManager(this@InCinemaModeActivity, LinearLayoutManager.VERTICAL, false)
            rvQuickOption?.apply {
                layoutManager = layoutManager
                addItemDecoration(RecyclerViewDecorationInCinemaTypes(6, 1))
                adapter = inCinemaTypesAdadapter
            }

            rvIntervalTiming?.apply {
                adapter = intervalAdadapter
                addItemDecoration(RecyclerViewMarginBookedTickets(14, 1))
                PagerSnapHelper().attachToRecyclerView(this)
            }
            rvSeatNumber?.apply {
                layoutManager = GridLayoutManager(this@InCinemaModeActivity, 7)
                adapter = seatsAdapter
                layoutManager = GridAutoFitLayoutManager(
                    this@InCinemaModeActivity, 46, LinearLayoutManager.VERTICAL, false
                )
            }
            rvFoodandbevrages?.apply {
                addItemDecoration(RecyclerViewMarginFoodOrder(30, 1))
                adapter = orderAdapter
            }
            initNoDataDialog()
            getInCinemaMode()

        }
    }

    private fun initNoDataDialog() {
        noDataDialog = OptionDialog(this,
            R.mipmap.ic_launcher,
            R.string.app_name,
            getString(R.string.something_went_wrong),
            positiveBtnText = R.string.ok,
            negativeBtnText = R.string.no,
            positiveClick = {
                noDataDialog!!.dismiss()
                onBackPressed()
            },
            negativeClick = {})
    }

    private fun getInCinemaMode() {
        showLoader()
        authViewModel.getInCinema(preferences.getUserId(), preferences.getCityName())
        authViewModel.getInCinemaLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    dismissLoader()
                    setData(it.data)
                }

                is NetworkResult.Error -> {
                    dismissLoader()
                    binding?.nestedScrollView?.hide()
                    noDataDialog?.show()
                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun setData(data: GetInCinemaResponse?) {
        if (Constant.status == data?.result && Constant.SUCCESS_CODE == data.code) {
            currentBooking = 0
            inCinemaPageData = data.output.inCinemaResp
                bookingIdList!!.clear()
                bookingIdList!!.addAll(data.output.bookingIdList)
                if (bookingIdList!!.size != 0) {
                    if (inCinemaPageData != null) {
                        inCinemaPageData?.apply {
                            binding?.apply {
                                textviewMovieNumber.text =
                                    (currentBooking + 1).toString() + "/" + bookingIdList!!.size.toString()
                                nestedScrollView.show()
                                Glide.with(this@InCinemaModeActivity)
                                    .load(movieImage)
                                    .placeholder(getDrawable(R.drawable.placeholder_vertical))
                                    .error(getDrawable(R.drawable.placeholder_vertical))
                                    .into(imageviewMoviewPoster)
                                textViewAudiName.text = audi
                                textviewMovieTheatreLocation.text = cinemaname
                                textviewMovieDateAndTime.text = showtime
                                textviewMovieCategory.text = mcensor
                                textviewMovieLanguage.text = lang
                                textviewMovieType.text = format
                                textviewMovieName.text = mname

                                if (currentBooking ==0 && bookingIdList!!.size>0) {
                                    imageviewPreviousBookedMovie.hide()
                                    imageviewNextBookedMovie.show()
                                }

                                if(currentBooking>0 && currentBooking < bookingIdList!!.size-1){
                                    imageviewPreviousBookedMovie.show()
                                    imageviewNextBookedMovie.show()
                                }
                                if (currentBooking == bookingIdList!!.size - 1) {
                                    imageviewPreviousBookedMovie.hide()
                                    imageviewNextBookedMovie.hide()
                                }
                            }
                            seatsAdapter.submitList(seats)
                            intervalAdadapter.submitList(showData)
                            inCinemaTypesAdadapter.submitList(incinemaTypes)
                            inCinemaFoodResp?.apply {
                                if (size == 0) {
                                    binding?.apply {
                                        rvFoodandbevrages?.hide()
                                        textViewNoFoodOrders?.show()
                                        viewMarginNoFoodOrders?.show()
                                    }
                                } else {
                                    binding?.apply {
                                        rvFoodandbevrages?.show()
                                        textViewNoFoodOrders?.hide()
                                        viewMarginNoFoodOrders?.hide()
                                    }
                                    get(0)?.isExpanded = true
                                    orderAdapter!!.clear()
                                    forEach {
                                        println("it--->$it")
                                        orderAdapter!!.add(
                                            OrderItemView(
                                                context = this@InCinemaModeActivity,
                                                it,
                                                orderAdapter
                                            )
                                        )
                                    }
                                }
                            }
                            if (placeholders?.isEmpty() == true) {
                                binding?.recyclerView51?.hide()
                            } else {
                                binding?.recyclerView51?.apply {
                                    show()
                                    PagerSnapHelper().attachToRecyclerView(binding?.recyclerView51)
                                    onFlingListener = null
                                    val layoutManagerPlaceHolder =
                                        LinearLayoutManager(
                                            this@InCinemaModeActivity,
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                    val ticketPlaceHolderAdapter =
                                        TicketPlaceHolderAdapter(
                                            this@InCinemaModeActivity,
                                            inCinemaPageData?.placeholders!!
                                        )
                                    setHasFixedSize(true)
                                    layoutManager = layoutManagerPlaceHolder
                                    adapter = ticketPlaceHolderAdapter
                                }
                            }
                        }

                    } else {
                        dismissLoader()
                        binding?.nestedScrollView?.hide()
                        noDataDialog?.show()
                    }
                }
                else {
                    binding!!.nestedScrollView.hide()
                    noDataDialog!!.show()
                }
        }
        else {
            binding!!.nestedScrollView.hide()
            noDataDialog?.show()
        }
    }

    private fun dismissLoader() {
        loaderDialogs.forEach {
            it?.dismiss()
        }
        loaderDialogs.clear()
    }

    private fun getBookingInfo(bookingId: String, city: String) {
        showLoader()
        authViewModel.getBooking(bookingId, city)
        observeBookingResponse()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun observeBookingResponse() {
        authViewModel.getBookingLiveData.observe(this) { it ->
            when (it) {
                is NetworkResult.Success -> {
                    dismissLoader()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        inCinemaPageData = it.data.output
                        if (inCinemaPageData != null) {
                            inCinemaPageData?.apply {
                                binding?.apply {
                                    nestedScrollView.show()
                                    textviewMovieNumber.text =
                                        (currentBooking + 1).toString() + "/" + bookingIdList!!.size.toString()

                                    Glide.with(this@InCinemaModeActivity)
                                        .load(movieImage)
                                        .placeholder(getDrawable(R.drawable.placeholder_vertical))
                                        .error(getDrawable(R.drawable.placeholder_vertical))
                                        .into(imageviewMoviewPoster)
                                    textViewAudiName.text = audi
                                    textviewMovieTheatreLocation.text = cinemaname
                                    textviewMovieDateAndTime.text = showtime
                                    textviewMovieCategory.text = mcensor
                                    textviewMovieLanguage.text = lang
                                    textviewMovieType.text = format
                                    textviewMovieName.text = mname

                                    if (currentBooking ==0 && bookingIdList!!.size>0) {
                                        imageviewPreviousBookedMovie.hide()
                                        imageviewNextBookedMovie.show()
                                    }

                                    if(currentBooking>0 && currentBooking < bookingIdList!!.size-1){
                                        imageviewPreviousBookedMovie.show()
                                        imageviewNextBookedMovie.show()
                                    }
                                    if (currentBooking == bookingIdList!!.size - 1) {
                                        imageviewPreviousBookedMovie.hide()
                                        imageviewNextBookedMovie.hide()
                                    }

                                }
                                inCinemaPageData?.apply {
                                    seatsAdapter.submitList(seats)
                                    intervalAdadapter.submitList(showData)
                                    inCinemaTypesAdadapter.submitList(incinemaTypes)
                                    inCinemaFoodResp?.apply {
                                        if (size == 0) {
                                            binding?.apply {
                                                rvFoodandbevrages?.hide()
                                                textViewNoFoodOrders?.show()
                                                viewMarginNoFoodOrders?.show()
                                            }
                                        } else {
                                            binding?.apply {
                                                rvFoodandbevrages?.show()
                                                textViewNoFoodOrders?.hide()
                                                viewMarginNoFoodOrders?.hide()
                                            }
                                            get(0)?.isExpanded = true
                                            orderAdapter!!.clear()
                                            forEach {
                                                println("it--->$it")
                                                orderAdapter!!.add(
                                                    OrderItemView(
                                                        context = this@InCinemaModeActivity,
                                                        it,
                                                        orderAdapter
                                                    )
                                                )
                                            }
                                        }

                                    }
                                    if (placeholders?.isEmpty() == true) {
                                        binding?.recyclerView51?.hide()
                                    } else {
                                        binding?.apply {
                                            recyclerView51?.apply {
                                                show()
                                                onFlingListener = null
                                                PagerSnapHelper().attachToRecyclerView(this)
                                                val layoutManagerPlaceHolder =
                                                    LinearLayoutManager(
                                                        this@InCinemaModeActivity,
                                                        LinearLayoutManager.HORIZONTAL,
                                                        false
                                                    )
                                                val ticketPlaceHolderAdapter =
                                                    TicketPlaceHolderAdapter(
                                                        this@InCinemaModeActivity,
                                                        inCinemaPageData?.placeholders!!
                                                    )
                                                setHasFixedSize(true)
                                                layoutManager =
                                                    layoutManagerPlaceHolder
                                                adapter = ticketPlaceHolderAdapter
                                            }
                                        }
                                    }
                                }

                            }
                        } else {
                            binding?.nestedScrollView?.hide()
                            noDataDialog?.show()
                        }
                    }
                    else {
                        binding?.nestedScrollView?.hide()
                        noDataDialog?.show()
                    }
                }
                is NetworkResult.Error -> {
                    dismissLoader()
                    binding?.nestedScrollView?.hide()
                    noDataDialog?.show()
                }

                is NetworkResult.Loading -> {
                }
            }
        }
    }
    var loaderDialogs= mutableListOf<LoaderDialog>()
    private fun showLoader() {
        loader = LoaderDialog(R.string.pleaseWait).also { it.show(supportFragmentManager, null) }
        loaderDialogs.add(loader!!)
    }

    private fun createSeatsAdapter() =
        GenericRecyclerViewAdapter(getViewLayout = { R.layout.movie_details_item },
            areItemsSame = ::isSeatSame,
            areItemContentsEqual = ::isSeatSame,
            onBind = { seat, viewDataBinding, _ ->
                with(viewDataBinding as MovieDetailsItemBinding) {
                    textviewSeatNumber.text = seat

                }
            })

    private fun isSeatSame(seat1: String, seat2: String): Boolean {
        return seat1.equals(seat2)
    }

    private fun createIntervalTimingAdapter() =
        GenericRecyclerViewAdapter(getViewLayout = { R.layout.interval_timing_item },
            areItemsSame = ::isIntervalTimingSame,
            areItemContentsEqual = ::isIntervalTimingSame,
            onBind = { showData, viewDataBinding, _ ->
                with(viewDataBinding as IntervalTimingItemBinding) {
                    viewDataBinding.textviewIntervalStartsAt.text = showData.slider
                    viewDataBinding.textviewIntervalStartTime.text = showData.time
                }
            })

    private fun isIntervalTimingSame(timing1: ShowData, timing2: ShowData): Boolean {
        return timing1.time.equals(timing2.time)
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

    private fun isPackageExisted(targetPackage: String?): Boolean {
        val pm = packageManager
        try {
            val info = pm.getPackageInfo(targetPackage!!, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    fun openAppOrPlaySotre(
        packageId: String,
        playStoreAppUrl: String = "market://details?id=com.ubercab",
        uri: String = "uber://?action=setPickup&pickup=my_location",
        playStoreUrl: String = "http://play.google.com/store/apps/details?id=com.olacabs.customer"
    ) {
        val pm = packageManager
        if (isPackageExisted(packageId)) {
            try {
                pm.getPackageInfo(packageId, PackageManager.GET_ACTIVITIES)
                val mUri = uri
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(mUri)
                startActivity(intent)
            } catch (e: PackageManager.NameNotFoundException) {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse(playStoreAppUrl)
                        )
                    )
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse(playStoreUrl)
                        )
                    )
                }
            }
        } else {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(playStoreAppUrl)
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(playStoreUrl)
                    )
                )
            }
        }
    }

    private fun createInCinemaTypesAdapter() =
        GenericRecyclerViewAdapter(getViewLayout = { R.layout.item_quick_option_incinema },
            areItemsSame = ::isCinemaTypesSame,
            areItemContentsEqual = ::isCinemaTypesSame,
            onBind = { inCinemaModeItem, viewDataBinding, _ ->
                with(viewDataBinding as ItemQuickOptionIncinemaBinding) {
                    when (inCinemaModeItem.key) {
                        Constant.CAB -> {
                            Glide.with(this@InCinemaModeActivity)
                                .load(resources.getDrawable(R.drawable.ic_ready_to_leave))
                            textViewTypeLabel.text = inCinemaModeItem.value
                            cardviewQuickOptionsType.setOnClickListener {
                                val dialog = BottomSheetDialog(
                                    this@InCinemaModeActivity, R.style.NoBackgroundDialogTheme
                                ).also {
                                    it.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                    it.setContentView(R.layout.dialog_ready_to_leave)
                                    it.setCancelable(true)
                                    it.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                                    it.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                    it.window!!.setLayout(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                    it.window!!.setGravity(Gravity.CENTER)
                                }

                                val uber =
                                    dialog.findViewById<View>(R.id.cardview_uber) as MaterialCardView?
                                val ola =
                                    dialog.findViewById<View>(R.id.cardview_ola) as MaterialCardView?

                                uber!!.setOnClickListener {
                                    dialog.let {
                                        if (it.isShowing) {
                                            it.dismiss()
                                        }
                                    }
                                    val browserIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.uber.com/in/en/ride/")
                                    )
                                    startActivity(browserIntent)
//                    intent_ready_to_leave =
//                        Intent(this@InCinemaModeActivity, WebViewReadyToLeave::class.java)

//                    intent_ready_to_leave?.apply {
//                        putExtra("getUrl", "https://www.uber.com/in/en/ride/")
//                        putExtra("from", "inCinemaMode")
//                        putExtra("title", "In Cinema Mode")
//                        addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
//                        startActivity(intent_ready_to_leave)
//                    }
                                }
                                ola!!.setOnClickListener {
                                    dialog.let {
                                        if (it.isShowing) {
                                            it.dismiss()
                                        }
                                    }
                                    val browserIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://book.olacabs.com/?")
                                    )
                                    startActivity(browserIntent)
//                    intent_ready_to_leave =
//                        Intent(this@InCinemaModeActivity, WebViewReadyToLeave::class.java)

//                    intent_ready_to_leave?.apply {
//                        putExtra("getUrl", "https://book.olacabs.com/?")
//                        putExtra("from", "inCinemaMode")
//                        putExtra("title", "In Cinema Mode")
//                        addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
//                        startActivity(intent_ready_to_leave)
//                    }
                                }
                                dialog.show()
                            }

                        }
                        Constant.FnB -> {
                            Glide.with(this@InCinemaModeActivity)
                                .load(resources.getDrawable(R.drawable.ic_food_and_bevarages))
                            textViewTypeLabel.text = inCinemaModeItem.value
                            cardviewQuickOptionsType.setOnClickListener {
                                Constant.CINEMA_ID = ""
                                inCinemaPageData?.let {
                                    it.inCinemaFoodResp?.let {
                                        Constant.BOOKING_ID = bookingIdList!![currentBooking]
                                        Constant.CINEMA_ID = inCinemaPageData?.ccode!!
                                    }

                                }
                                if (!Constant.CINEMA_ID.isNullOrEmpty() && !Constant.BOOKING_ID.isNullOrEmpty()) {
                                    Constant.QR = "NO"
                                    Constant.INCINEMA = "YES"
                                    Constant.AUDI = ""
                                    Constant.SEAT = ""
                                    val intent =
                                        Intent(
                                            this@InCinemaModeActivity,
                                            FoodActivity::class.java
                                        )
                                    intent.putExtra("from", "pcOrdrsnc")
                                    intent.putExtra("NF", "true")
                                    Constant.BOOK_TYPE = "FOOD"
                                    intent.putExtra(
                                        "SEATS",
                                        java.lang.String.valueOf(mCinemaData?.inCinemaResp?.seats?.size)
                                    )
                                    startActivity(intent)

                                } else {
                                    dialog = OptionDialog(this@InCinemaModeActivity,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        getString(R.string.error_message),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {},
                                        negativeClick = {})
                                    dialog!!.show()
                                }
                            }

                        }
                        Constant.Feedback -> {
                            Glide.with(this@InCinemaModeActivity)
                                .load(resources.getDrawable(R.drawable.ic_feedback))
                            textViewTypeLabel.text = inCinemaModeItem.value
                            cardviewQuickOptionsType.setOnClickListener {
                                authViewModel.getFeedBackData(
                                    preferences.getUserId(),
                                    "INCINEMA"
                                )
                                getFeedBackData()
                                //open feedback flow
                            }
                        }
                    }
                }
            })

    private fun getFeedBackData() {
        authViewModel.getFeedBackDataResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveFeedbackData(it.data.output)
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun retrieveFeedbackData(output: FeedbackDataResponse.Output) {
        var rateVal = "5"
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val bindingProfile = FeedbackDialogeBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<FrameLayout> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(bindingProfile.root)

        bindingProfile.mainView.background.setColorFilter(
            Color.parseColor("#111111"), PorterDuff.Mode.SRC_ATOP
        );
        bindingProfile.commentBox.background.setColorFilter(
            Color.parseColor("#343434"), PorterDuff.Mode.SRC_ATOP
        );
        bindingProfile.feedbackText.background.setColorFilter(
            Color.parseColor("#333333"), PorterDuff.Mode.SRC_ATOP
        );


        bindingProfile.title.text = output.title
        bindingProfile.title.setTextColor(getColor(R.color.white))
        bindingProfile.subTitle.setTextColor(getColor(R.color.white))
        bindingProfile.feedbackText.setTextColor(getColor(R.color.white))
        bindingProfile.commentBox.setTextColor(getColor(R.color.white))
        bindingProfile.subTitle.text = "Please rate your experience"
        bindingProfile.feedbackText.hide()

        bindingProfile.rate1.setOnClickListener(View.OnClickListener {
            bindingProfile.feedbackText.text = output.ratings.L1
            bindingProfile.commentView.show()
            bindingProfile.commentBox.show()
            bindingProfile.doneBtn.show()
            bindingProfile.feedbackText.show()
            bindingProfile.rate1.setImageResource(R.drawable.select_sad)
            bindingProfile.rate2.setImageResource(R.drawable.emotion_unhappy)
            bindingProfile.rate3.setImageResource(R.drawable.emotion_normal)
            bindingProfile.rate4.setImageResource(R.drawable.emotion_laugh)
            bindingProfile.rate5.setImageResource(R.drawable.emotion_happy)
            rateVal = "L1"
        })
        bindingProfile.rate2.setOnClickListener(View.OnClickListener {
            bindingProfile.feedbackText.text = output.ratings.L2
            bindingProfile.commentView.show()
            bindingProfile.commentBox.show()
            bindingProfile.doneBtn.show()
            bindingProfile.feedbackText.show()
            bindingProfile.rate1.setImageResource(R.drawable.emotion_sad)
            bindingProfile.rate2.setImageResource(R.drawable.select_unhappy)
            bindingProfile.rate3.setImageResource(R.drawable.emotion_normal)
            bindingProfile.rate4.setImageResource(R.drawable.emotion_laugh)
            bindingProfile.rate5.setImageResource(R.drawable.emotion_happy)
            rateVal = "L2"
        })
        bindingProfile.rate3.setOnClickListener(View.OnClickListener {
            bindingProfile.feedbackText.text = output.ratings.L3
            bindingProfile.commentView.show()
            bindingProfile.commentBox.show()
            bindingProfile.doneBtn.show()
            bindingProfile.feedbackText.show()
            bindingProfile.rate1.setImageResource(R.drawable.emotion_sad)
            bindingProfile.rate2.setImageResource(R.drawable.emotion_unhappy)
            bindingProfile.rate3.setImageResource(R.drawable.select_normal)
            bindingProfile.rate4.setImageResource(R.drawable.emotion_laugh)
            bindingProfile.rate5.setImageResource(R.drawable.emotion_happy)
            rateVal = "L3"
        })
        bindingProfile.rate4.setOnClickListener(View.OnClickListener {
            bindingProfile.feedbackText.text = output.ratings.L4
            bindingProfile.commentView.show()
            bindingProfile.commentBox.show()
            bindingProfile.doneBtn.show()
            bindingProfile.feedbackText.show()
            bindingProfile.rate1.setImageResource(R.drawable.emotion_sad)
            bindingProfile.rate2.setImageResource(R.drawable.emotion_unhappy)
            bindingProfile.rate3.setImageResource(R.drawable.emotion_normal)
            bindingProfile.rate4.setImageResource(R.drawable.select_laugh)
            bindingProfile.rate5.setImageResource(R.drawable.emotion_happy)
            rateVal = "L4"
        })
        bindingProfile.rate5.setOnClickListener(View.OnClickListener {
            bindingProfile.feedbackText.text = output.ratings.L5
            bindingProfile.commentView.show()
            bindingProfile.commentBox.show()
            bindingProfile.doneBtn.show()
            bindingProfile.feedbackText.show()
            bindingProfile.rate1.setImageResource(R.drawable.emotion_sad)
            bindingProfile.rate2.setImageResource(R.drawable.emotion_unhappy)
            bindingProfile.rate3.setImageResource(R.drawable.emotion_normal)
            bindingProfile.rate4.setImageResource(R.drawable.emotion_laugh)
            bindingProfile.rate5.setImageResource(R.drawable.select_happy)
            rateVal = "L5"
        })

        bindingProfile.doneBtn.setOnClickListener(View.OnClickListener {
            authViewModel.setFeedBackData(
                preferences.getUserId(),
                "INCINEMA",
                rateVal,
                bindingProfile.feedbackText.text.toString(),
                "",
                bindingProfile.commentBox.text.toString(),
                mCinemaData?.inCinemaResp?.ccode!!,
                mCinemaData?.inCinemaResp?.bookingId!!

            )
            setFeedBackData()
            dialog.dismiss()
        })


        dialog.show()
    }

    private fun setFeedBackData() {
        authViewModel.setFeedBackDataResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveSetFeedbackData(it.data.output)
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun retrieveSetFeedbackData(output: FeedbackDataResponse.Output) {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val bindingProfile = FeedbackThanksBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<FrameLayout> = dialog.behavior
        bindingProfile.mainView.background.setColorFilter(
            Color.parseColor("#111111"), PorterDuff.Mode.SRC_ATOP
        );
        bindingProfile.title.setTextColor(getColor(R.color.white))

        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(bindingProfile.root)
        dialog.show()
    }
}

fun isCinemaTypesSame(type1: IncinemaType, type2: IncinemaType): Boolean {
    return type1.key == type2.key
}


