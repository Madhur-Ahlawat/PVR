package com.net.pvr.ui.food

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityFoodBinding
import com.net.pvr.databinding.FoodBottomAddFoodBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.food.adapter.*
import com.net.pvr.ui.food.response.FoodResponse
import com.net.pvr.ui.food.viewModel.FoodViewModel
import com.net.pvr.ui.home.fragment.home.adapter.PromotionAdapter
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.summery.SummeryActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.AUDI
import com.net.pvr.utils.Constant.Companion.BOOKING_ID
import com.net.pvr.utils.Constant.Companion.BOOK_TYPE
import com.net.pvr.utils.Constant.Companion.CINEMA_ID
import com.net.pvr.utils.Constant.Companion.FOOD_COUNT
import com.net.pvr.utils.Constant.Companion.QR
import com.net.pvr.utils.Constant.Companion.SEAT
import com.net.pvr.utils.Constant.Companion.SUMMERYBACK
import com.net.pvr.utils.Constant.Companion.TRANSACTION_ID
import com.net.pvr.utils.Constant.Companion.foodCartModel
import com.net.pvr.utils.ga.GoogleAnalytics
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class FoodActivity : AppCompatActivity(),
    BestSellerFoodAdapter.RecycleViewItemClickListenerCity,
    CategoryAdapter.RecycleViewItemClickListenerCity,
    AllFoodAdapter.RecycleViewItemClickListenerCity,
    BottomFoodAdapter.RecycleViewItemClickListenerCity,
    CartAdapter.RecycleViewItemClickListenerCity,
    PreviousFoodAdapter.RecycleViewItemClickListenerCity,
    StoriesProgressView.StoriesListener {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityFoodBinding? = null
    private val authViewModel: FoodViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var itemDescription: String = ""

    private var cartModel: ArrayList<CartModel> = arrayListOf()
    private var filterResponse: ArrayList<FoodResponse.Output.Mfl>? = null
    private var catFilter = ArrayList<FoodResponse.Output.Mfl>()
    private var popupFood = ArrayList<FoodResponse.Output.Bestseller.R>()
    private var catFilterBestSeller = ArrayList<FoodResponse.Output.Bestseller>()
    private var foodResponse: FoodResponse.Output? = null
    private var foodResponseCategory: ArrayList<FoodResponse.Output.Cat> = arrayListOf()
    private var bestSellerFoodAdapter: BestSellerFoodAdapter? = null
    private var bottomFoodAdapter: BottomFoodAdapter? = null
    private var allFoodAdapter: AllFoodAdapter? = null
    private var previousFoodAdapter: PreviousFoodAdapter? = null

    private var masterId: String? = "0"
    private var categoryName: String = "ALL"
    private var itemCheckPriceCart: Int = 0
    private var menuType = 0
    private var cartShow = false
    private var bestSellerType: String = "ALL"

    // Food Popup Custom
    private var amtViewFood:RelativeLayout? = null
    private var amtFood:TextView? = null

    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

    // story board
    private var bannerShow = 0
    private var pressTime = 0L
    private var limit = 500L
    private var counterStory = 0
    private var currentPage = 1
    private var bannerModelsMain: ArrayList<BookingResponse.Output.Pu> = ArrayList<BookingResponse.Output.Pu>()


    companion object{
        var itemCount = 0
        var limitCount = 0
        var seatMessage = "0"
        fun getFoodQTCount(r: List<FoodResponse.Output.Bestseller.R>): Int {
            var qt = 0
            for (data in r){
                if (data.qt>0){
                    qt += data.qt
                }
            }
            return qt
        }

         fun getItemCount(cartModel: ArrayList<CartModel>): Int {
            var count = 0

            for (data in cartModel){
                count += data.quantity
            }
            return count
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        foodCartModel = ArrayList()

        manageFunction()
    }

    private fun manageFunction() {
        Constant.viewModel = authViewModel
        var audi = ""
        if (AUDI != ""){
            audi = "AUDI"
        }
        authViewModel.food(
            preferences.getUserId(), CINEMA_ID, BOOKING_ID, "", "", audi, AUDI, SEAT, "", QR, "no", "no"
        )

        //internet Check
        broadcastReceiver = NetworkReceiver()
        broadcastIntent()

        movedNext()
        foodDetails()
        saveFoodResponseLiveData()
        getShimmerData()
    }

    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    private fun foodDetails() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        foodResponse = it.data.output
                        foodResponseCategory.add(FoodResponse.Output.Cat("", "ALL", 0))
                        foodResponseCategory.addAll(it.data.output.cat)
                        filterResponse = it.data.output.mfl
                        catFilter = it.data.output.mfl
                        limitCount = it.data.output.aqt
                        seatMessage = it.data.output.nams
                        catFilterBestSeller = it.data.output.bestsellers
                        if (it.data.output.h1!=null && it.data.output.h1 != ""){
                            binding?.foodMsg?.show()
                            binding?.h1Text?.text = it.data.output.h1
                            binding?.h2Text?.text = it.data.output.h2
                        }
                        retrieveData(it.data.output)
                        loader?.dismiss()

                    } else {
                        //ui Data Load
                        binding?.constraintLayout154?.show()
                        //shimmer
                        binding?.constraintLayout145?.hide()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                        loader?.dismiss()

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
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }
    private fun saveFoodResponseLiveData() {
        authViewModel.saveFoodResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        BOOKING_ID = it.data.output.bi
                        TRANSACTION_ID = it.data.output.tid
                        loader?.dismiss()

                        FOOD_COUNT=cartModel.size

                        val intent = Intent(this@FoodActivity, SummeryActivity::class.java)
                        intent.putExtra(BOOK_TYPE, "FOOD")
                        intent.putExtra("food", cartModel)
                        startActivity(intent)
                    } else {
                        //ui Data Load
                        binding?.constraintLayout154?.show()
                        //shimmer
                        binding?.constraintLayout145?.hide()
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                        loader?.dismiss()

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
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {

                }
            }
        }

    }

    private fun retrieveData(output: FoodResponse.Output) {

        //shimmer
        binding?.constraintLayout145?.hide()

        //ui Data Load
        binding?.nestedScrollView?.show()

        // Past Food
        if (output.pastfoods.isNotEmpty()) {
            binding?.previousFoodView?.show()
            val list = getPastFoodList(output)
            val layoutManagerCrew2 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            previousFoodAdapter = PreviousFoodAdapter(list, this, this, "past")
            binding?.previousFoodList?.adapter = previousFoodAdapter
            binding?.previousFoodList?.setHasFixedSize(true)
            binding?.previousFoodList?.layoutManager = layoutManagerCrew2
        } else {
            binding?.previousFoodView?.hide()
        }

        //Food
        val layoutManagerCrew = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        bestSellerFoodAdapter = BestSellerFoodAdapter(output.bestsellers, this, this)
        binding?.recyclerView19?.layoutManager = layoutManagerCrew
        binding?.recyclerView19?.adapter = bestSellerFoodAdapter
        binding?.recyclerView19?.setHasFixedSize(true)

        //Category
        val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val categoryAdapter =
            CategoryAdapter(foodResponseCategory, this, this, binding?.recyclerView20!!)
        binding?.recyclerView20?.layoutManager = layoutManager
        binding?.recyclerView20?.adapter = categoryAdapter
        binding?.recyclerView20?.setHasFixedSize(true)

        //All Food
        val layoutManagerCrew2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        allFoodAdapter = AllFoodAdapter(getFilterAllMfl(true, menuType, "ALL"), this, this, "")
        binding?.recyclerView21?.adapter = allFoodAdapter
        binding?.recyclerView21?.setHasFixedSize(true)
        binding?.recyclerView21?.layoutManager = layoutManagerCrew2

        // Food Banners
        if (output.banners.isNotEmpty()) {
            binding?.ppBanners?.show()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding?.pramotionBanner?.layoutManager = layoutManager
            val adapter = FoodBannersAdapter(this, output.banners)
            binding?.pramotionBanner?.adapter = adapter
        } else {
            binding?.ppBanners?.hide()
        }
        // Food PlaceHolders
        if (output.ph.isNotEmpty()) {
            binding?.constraintLayout123?.show()
            updatePH(output.ph)
        } else {
            binding?.constraintLayout123?.hide()
        }
        // Food Popups
        if (output.pu.isNotEmpty()) {
            binding?.rlBanner?.show()
            initBanner(output.pu)
        } else {
            binding?.rlBanner?.hide()
        }
    }

    private fun getPastFoodList(output: FoodResponse.Output): ArrayList<FoodResponse.Output.Mfl> {
        var list = ArrayList<FoodResponse.Output.Mfl>()
        for (data in output.mfl) {
            if (output.pastfoods.contains(data.mid.toString())) {
                list.add(data)
            }
        }
        return list
    }

    private fun movedNext() {
        //OnBack
        binding?.imageView58?.setOnClickListener {
            Constant.SeatBack = 1
            onBackPressedDispatcher.onBackPressed()
        }
        //without food
        binding?.textView374?.setOnClickListener {
// Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
//                bundle.putString("var_add_payment_info","")
                GoogleAnalytics.hitEvent(this, "FnB_continue", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            val intent = Intent(this@FoodActivity, SummeryActivity::class.java)
            intent.putExtra(BOOK_TYPE, "FOOD")
            startActivity(intent)
        }

        binding?.textView108?.text = getString(R.string.order_snack)
        binding?.include24?.textView5?.text = getString(R.string.submit)
        binding?.include24?.textView5?.text = getString(R.string.proceed)

        //with Food
        binding?.include24?.textView5?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                bundle.putString("var_FnB_food_type","veg")
                GoogleAnalytics.hitEvent(this, "FnB_proceed", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }


            cartShow = false
            binding?.constraintLayout112?.hide()
            if (BOOK_TYPE != "FOOD")
            binding?.textView374?.show()
            var totalPrice = 0;
            if (BOOK_TYPE == "FOOD"){
                cartModel.forEachIndexed { index, food ->
                    itemDescription = if (index == 0) {
                        food.title + "|" + food.id + "|" + food.quantity + "|" + food.price + "|" + food.ho +"|"+food.veg
                    } else {
                        itemDescription + "#" + food.title + "|" + food.id + "|" + food.quantity + "|" + food.price + "|" + food.ho +"|"+food.veg
                    }
                    totalPrice += food.price
                }
                val readFormat = SimpleDateFormat("MMM dd, yyyy")
               val type = if (QR == "YES"){
                   ""
                }else{
                      ""
                }
                var audi = ""
                if (AUDI != ""){
                    audi = "AUDI"
                }

                authViewModel.saveFood(preferences.getUserId(),CINEMA_ID,totalPrice.toString(),itemDescription,
                    readFormat.format(
                        Calendar.getInstance().time
                    ), BOOKING_ID, AUDI, SEAT,audi,"", QR,"","")
            } else {
                val intent = Intent(this@FoodActivity, SummeryActivity::class.java)
                intent.putExtra(BOOK_TYPE, "BOOKING")
                intent.putExtra("food", cartModel)
                startActivity(intent)
            }
        }

        //veg
        binding?.switch2?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                    bundle.putString("var_FnB_food_type","veg")
                    GoogleAnalytics.hitEvent(this, "FnB_food_type", bundle)
                }catch (e:Exception){
                    e.printStackTrace()
                }


                menuType = 1
                binding?.switch3?.isChecked = false
                bestSellerType = "Veg"
                val layoutManagerCrew2 =
                    GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                allFoodAdapter = AllFoodAdapter(
                    getFilterAllMfl(true, menuType, categoryName), this, this, ""
                )
                binding?.recyclerView21?.adapter = allFoodAdapter
                binding?.recyclerView21?.setHasFixedSize(true)
                binding?.recyclerView21?.layoutManager = layoutManagerCrew2

//                catFilterBestSeller
                val layoutManagerCrew =
                    GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                bestSellerFoodAdapter = BestSellerFoodAdapter(
                    getFilterBestSellerList(true, menuType, bestSellerType), this, this
                )
                binding?.recyclerView19?.layoutManager = layoutManagerCrew
                binding?.recyclerView19?.adapter = bestSellerFoodAdapter
                binding?.recyclerView19?.setHasFixedSize(true)

            } else {
                if (binding?.switch2?.isChecked == false && binding?.switch3?.isChecked == false) {
                    bestSellerType = "ALL"
                    menuType = 0
                    val layoutManagerCrew2 =
                        GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                    allFoodAdapter = AllFoodAdapter(
                        getFilterAllMfl(true, menuType, categoryName), this, this, ""
                    )
                    binding?.recyclerView21?.adapter = allFoodAdapter
                    binding?.recyclerView21?.setHasFixedSize(true)
                    binding?.recyclerView21?.layoutManager = layoutManagerCrew2

//                catFilterBestSeller
                    val layoutManagerCrew =
                        GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                    bestSellerFoodAdapter = BestSellerFoodAdapter(
                        getFilterBestSellerList(
                            true, menuType, bestSellerType
                        ), this, this
                    )
                    binding?.recyclerView19?.layoutManager = layoutManagerCrew
                    binding?.recyclerView19?.adapter = bestSellerFoodAdapter
                    binding?.recyclerView19?.setHasFixedSize(true)

                }
            }
        }
        //Non veg
        binding?.switch3?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                    bundle.putString("var_FnB_food_type","non-veg")
                    GoogleAnalytics.hitEvent(this, "FnB_food_type", bundle)
                }catch (e:Exception){
                    e.printStackTrace()
                }

                menuType = 2
                binding?.switch2?.isChecked = false
                bestSellerType = "Veg"

                val layoutManagerCrew2 =
                    GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                allFoodAdapter = AllFoodAdapter(
                    getFilterAllMfl(true, menuType, categoryName), this, this, ""
                )
                binding?.recyclerView21?.adapter = allFoodAdapter
                binding?.recyclerView21?.setHasFixedSize(true)
                binding?.recyclerView21?.layoutManager = layoutManagerCrew2

                //catFilterBestSeller
                val layoutManagerCrew =
                    GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                bestSellerFoodAdapter = BestSellerFoodAdapter(
                    getFilterBestSellerList(true, menuType, bestSellerType), this, this
                )
                binding?.recyclerView19?.layoutManager = layoutManagerCrew
                binding?.recyclerView19?.adapter = bestSellerFoodAdapter
                binding?.recyclerView19?.setHasFixedSize(true)


            } else {
                if (binding?.switch2?.isChecked == false && binding?.switch3?.isChecked == false) {
                    bestSellerType = "ALL"

                    menuType = 0
                    val layoutManagerCrew2 =
                        GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                    allFoodAdapter = AllFoodAdapter(
                        getFilterAllMfl(true, menuType, categoryName), this, this, ""
                    )
                    binding?.recyclerView21?.adapter = allFoodAdapter
                    binding?.recyclerView21?.setHasFixedSize(true)
                    binding?.recyclerView21?.layoutManager = layoutManagerCrew2

//                catFilterBestSeller
                    val layoutManagerCrew =
                        GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                    bestSellerFoodAdapter = BestSellerFoodAdapter(
                        getFilterBestSellerList(
                            true, menuType, bestSellerType
                        ), this, this
                    )
                    binding?.recyclerView19?.layoutManager = layoutManagerCrew
                    binding?.recyclerView19?.adapter = bestSellerFoodAdapter
                    binding?.recyclerView19?.setHasFixedSize(true)

                }
            }
        }
    }

    //Category By Name
    override fun categoryClick(comingSoonItem: FoodResponse.Output.Cat) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                bundle.putString("var_FnB_category",comingSoonItem.name)
            GoogleAnalytics.hitEvent(this, "FnB_category", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }


        categoryName = comingSoonItem.name
        if (comingSoonItem.name == "ALL") {
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            allFoodAdapter = AllFoodAdapter(
                getFilterAllMfl(true, menuType, comingSoonItem.name), this, this, ""
            )
            binding?.recyclerView21?.adapter = allFoodAdapter
            binding?.recyclerView21?.setHasFixedSize(true)
            binding?.recyclerView21?.layoutManager = layoutManagerCrew
        } else {
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            allFoodAdapter = AllFoodAdapter(
                getFilterAllMfl(true, menuType, comingSoonItem.name), this, this, ""
            )
            binding?.recyclerView21?.adapter = allFoodAdapter
            binding?.recyclerView21?.setHasFixedSize(true)
            binding?.recyclerView21?.layoutManager = layoutManagerCrew
        }
    }

    // Add Food  BestSeller Action Manage
    override fun foodBestImageClick(comingSoonItem: FoodResponse.Output.Bestseller) {
        bottomDialogAddFood(comingSoonItem)
    }

    override fun addFood(comingSoonItem: FoodResponse.Output.Bestseller, position: Int) {
        if (itemCount >= limitCount) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                seatMessage,
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            masterId = comingSoonItem.r[0].id.toString()
            var num = comingSoonItem.qt
            num += 1
            comingSoonItem.qt = num
            updateBestSellerCartList(comingSoonItem)
            cartData()
        }
    }

    private fun updateBestSellerCartList(comingSoonItem: FoodResponse.Output.Bestseller) {
        if (cartModel.size == 0) {
            cartModel.add(
                CartModel(
                    comingSoonItem.r[0].id,
                    comingSoonItem.nm,
                    comingSoonItem.mi,
                    comingSoonItem.qt,
                    comingSoonItem.dp,
                    comingSoonItem.veg,
                    comingSoonItem.r[0].ho,
                    comingSoonItem.mid.toString(),
                )
            )
        } else {
            if (itemExist(comingSoonItem)) {
                for (item in cartModel) {
                    if (item.id == comingSoonItem.r[0].id) {
                        if (comingSoonItem.qt == 0) {
                            cartModel.remove(item)
                        } else {
                            item.quantity = comingSoonItem.qt

                        }
                        break
                    }
                }
            } else {
                cartModel.add(
                    CartModel(
                        comingSoonItem.r[0].id,
                        comingSoonItem.nm,
                        comingSoonItem.mi,
                        comingSoonItem.qt,
                        comingSoonItem.dp,
                        comingSoonItem.veg,
                        comingSoonItem.r[0].ho,
                        comingSoonItem.mid.toString()
                    )
                )
            }
        }
    }

    private fun itemExist(foodItem: FoodResponse.Output.Bestseller): Boolean {

        for (item in cartModel) {
            if (item.id == foodItem.r[0].id) {
                return true
            }
        }
        return false
    }

    override fun addFoodPlus(comingSoonItem: FoodResponse.Output.Bestseller, position: Int) {
        var num = comingSoonItem.qt
        if (itemCount >= limitCount) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                seatMessage,
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num += 1
            comingSoonItem.qt = num
            updateHomeCartList(comingSoonItem)
            cartData()
        }
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun bottomDialogAddFood(comingSoonItem: FoodResponse.Output.Bestseller) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val bindingBottom = FoodBottomAddFoodBinding.inflate(inflater)
        dialog.setContentView(bindingBottom.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        if (comingSoonItem.r[0].wt != null && comingSoonItem.r[0].wt !="" && comingSoonItem.r[0].en != null && comingSoonItem.r[0].en != "") {
            bindingBottom.cal.text = comingSoonItem.r[0].wt + "  •  " + comingSoonItem.r[0].en
            bindingBottom.cal.show()
        } else if (comingSoonItem.r[0].wt != null && comingSoonItem.r[0].wt != "") {
            bindingBottom.cal.text = comingSoonItem.r[0].wt
            bindingBottom.cal.show()
        } else if (comingSoonItem.r[0].en != null && comingSoonItem.r[0].en != "") {
            bindingBottom.cal.text = comingSoonItem.r[0].wt
            bindingBottom.cal.show()
        } else {
            bindingBottom.cal.hide()
        }
        if (comingSoonItem.r[0].fa != null && comingSoonItem.r[0].fa != "") {
            bindingBottom.fa.text = "Allergens " + comingSoonItem.r[0].fa
            bindingBottom.fa.show()
        } else {
            bindingBottom.fa.hide()
        }
//Mange Veg Non-Veg
        if (comingSoonItem.veg) {
            bindingBottom.imageView69.setImageDrawable(this.getDrawable(R.drawable.veg_ic))
        } else {
            bindingBottom.imageView69.setImageDrawable(this.getDrawable(R.drawable.nonveg_ic))
        }
        //Image View
        Glide.with(this)
            .load(comingSoonItem.mi)
            .error(R.drawable.placeholder_horizental)
            .into(bindingBottom.imageView64)

//        //title
        bindingBottom.textView137.text = comingSoonItem.nm
//        //price
        bindingBottom.textView150.text =
            getString(R.string.currency) + " " + Constant.DECIFORMAT.format(comingSoonItem.dp / 100.0)
//manageUI
        if (comingSoonItem.qt > 0) {
            bindingBottom.textView309.hide()
            bindingBottom.consAddUi.show()
            bindingBottom.uiPlusMinus.foodCount.text = comingSoonItem.qt.toString()
        } else {
            bindingBottom.textView309.show()
            bindingBottom.consAddUi.hide()
        }

        bindingBottom.textView309.setOnClickListener {
            if (comingSoonItem.r.size > 1) {
                dialog.dismiss()
                bottomDialogCart(comingSoonItem.r, comingSoonItem.nm,comingSoonItem.cid.toString())
            } else {
                masterId = comingSoonItem.mid.toString()
                var num = comingSoonItem.qt
                num += 1
                comingSoonItem.qt = num
                updateBestSellerCartList(comingSoonItem)
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()
                bindingBottom.textView309.hide()
                bindingBottom.consAddUi.show()
                cartData()
            }
        }

        //Add Food
        bindingBottom.uiPlusMinus.plus.setOnClickListener {
            var num = comingSoonItem.qt
            if (itemCount >= limitCount) {
                val dialog1 = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    seatMessage,
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog1.show()
            } else {
                num += 1
                comingSoonItem.qt = num
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()
                updateHomeCartList(comingSoonItem)
                cartData()
            }
        }

        // Subtract Food
        bindingBottom.uiPlusMinus.minus.setOnClickListener {
            var num = comingSoonItem.qt
            if (num < 0 || num == 0) {
                val dialog2 = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.min_item_msz),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog2.show()
            } else {
                num -= 1
                comingSoonItem.qt = num
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()
                if (comingSoonItem.qt == 0) {
                    bindingBottom.textView309.show()
                    bindingBottom.consAddUi.hide()
                }
                updateHomeCartList(comingSoonItem)
                cartData()
            }
        }

    }

    override fun addFoodMinus(comingSoonItem: FoodResponse.Output.Bestseller, position: Int) {
        var num = comingSoonItem.qt
        if (num < 0 || num == 0) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.min_item_msz),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num -= 1
            comingSoonItem.qt = num
            updateHomeCartList(comingSoonItem)
            cartData()
        }
    }

    private fun getFilterBestSellerList(
        category: Boolean,
        menuType: Int,
        bestSellerType: String
    ): List<FoodResponse.Output.Bestseller> {
        val categoryFilterNew = ArrayList<FoodResponse.Output.Bestseller>()
        when (menuType) {
            0 -> {
                if (bestSellerType == "ALL") {
                    for (data in catFilterBestSeller) {
                        categoryFilterNew.add(data)
                    }
                } else {
                    for (data in catFilterBestSeller) {
                        if (data.veg == category) categoryFilterNew.add(data)
                    }
                }
            }
            1 -> {
                if (bestSellerType == "ALL") {
                    for (data in catFilterBestSeller) {
                        categoryFilterNew.add(data)
                    }
                } else {
                    for (data in catFilterBestSeller) {
                        if (data.veg == category && data.veg) {
                            categoryFilterNew.add(data)
                        }
                    }
                }
            }
            2 -> {
                if (bestSellerType == "ALL") {
                    for (data in catFilterBestSeller) {
                        categoryFilterNew.add(data)
                    }
                } else {
                    for (data in catFilterBestSeller) {
                        if (!data.veg == category && !data.veg) {
                            categoryFilterNew.add(data)
                        }
                    }
                }
            }
            else -> {
                if (bestSellerType == "ALL") {
                    for (data in catFilterBestSeller) {
                        categoryFilterNew.add(data)
                    }
                } else {
                    for (data in catFilterBestSeller) {
                        if (data.veg == category) categoryFilterNew.add(data)
                    }
                }
            }
        }
        return categoryFilterNew
    }


    override fun bestSellerDialogAddFood(
        comingSoonItem: List<FoodResponse.Output.Bestseller.R>, position: String,cid:String
    ) {
        bottomDialogCart(comingSoonItem, position,cid)
    }

    private fun updateHomeCartList(comingSoonItem: FoodResponse.Output.Bestseller) {
        if (cartModel.size == 0) {
            cartModel.add(
                CartModel(
                    comingSoonItem.r[0].id,
                    comingSoonItem.nm,
                    comingSoonItem.mi,
                    comingSoonItem.qt,
                    comingSoonItem.dp,
                    comingSoonItem.veg,
                    comingSoonItem.r[0].ho,
                    comingSoonItem.mid.toString()
                )
            )
        } else {
            if (itemExist(comingSoonItem)) {
                for (item in cartModel) {
                    if (item.id == comingSoonItem.r[0].id) {
                        if (comingSoonItem.qt == 0) {
                            cartModel.remove(item)
                        } else {
                            item.quantity = comingSoonItem.qt

                        }
                        break
                    }
                }
            } else {
                cartModel.add(
                    CartModel(
                        comingSoonItem.r[0].id,
                        comingSoonItem.nm,
                        comingSoonItem.mi,
                        comingSoonItem.qt,
                        comingSoonItem.dp,
                        comingSoonItem.veg,
                        comingSoonItem.r[0].ho,
                        comingSoonItem.mid.toString()
                    )
                )
            }
        }
    }

    override fun categoryFoodDialog(
        comingSoonItem: List<FoodResponse.Output.Bestseller.R>, title: String,cid:String
    ) {
        bottomDialogCart(comingSoonItem, title,cid)
    }

    override fun filterBtFoodClick(comingSoonItem: FoodResponse.Output.Bestseller.R,list:List<FoodResponse.Output.Bestseller.R>) {
        if ((itemCount+getPopCount(popupFood))>= limitCount) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                seatMessage,
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            var num = comingSoonItem.qt
            num += 1
            comingSoonItem.qt = num
            updateFoodItemAmt(list as ArrayList<FoodResponse.Output.Bestseller.R>)
        }
    }

    private fun getPopCount(popupFood: ArrayList<FoodResponse.Output.Bestseller.R>): Int {
        var count = 0
        if (popupFood!=null) {
            for (data in popupFood) {
                count += data.qt
            }
        }

        return count
    }

    override fun filterBtFoodPlus(comingSoonItem: FoodResponse.Output.Bestseller.R,list:List<FoodResponse.Output.Bestseller.R>) {
        var num = comingSoonItem.qt
        if ((itemCount+getPopCount(popupFood)) >= limitCount) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                seatMessage,
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num += 1
            comingSoonItem.qt = num
        }
        updateFoodItemAmt(list as ArrayList<FoodResponse.Output.Bestseller.R>)
    }

    override fun filterBtFoodMinus(comingSoonItem: FoodResponse.Output.Bestseller.R,list:List<FoodResponse.Output.Bestseller.R>) {
        var num = comingSoonItem.qt
        if (num < 0 || num == 0) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.min_item_msz),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num -= 1
            comingSoonItem.qt = num
        }
        updateFoodItemAmt(list as ArrayList<FoodResponse.Output.Bestseller.R>)
    }

    private fun updateBottomFoodCartList(comingSoonItem: FoodResponse.Output.Bestseller.R) {
        if (cartModel.size == 0) {
            cartModel.add(
                CartModel(
                    comingSoonItem.id,
                    comingSoonItem.h,
                    comingSoonItem.i,
                    comingSoonItem.qt,
                    comingSoonItem.dp,
                    comingSoonItem.veg,
                    comingSoonItem.ho,
                    masterId!!
                )
            )
        } else {
            if (updateBottomItemExist(comingSoonItem)) {
                for (item in cartModel) {
                    if (item.id == comingSoonItem.id) {
                        if (comingSoonItem.qt == 0) {
                            cartModel.remove(item)
                        } else {
                            item.quantity = comingSoonItem.qt

                        }
                        break
                    }
                }
            } else {
                cartModel.add(
                    CartModel(
                        comingSoonItem.id,
                        comingSoonItem.h,
                        comingSoonItem.i,
                        comingSoonItem.qt,
                        comingSoonItem.dp,
                        comingSoonItem.veg,
                        comingSoonItem.ho,
                        masterId!!
                    )
                )
            }
        }
    }

    private fun updateBottomItemExist(foodItem: FoodResponse.Output.Bestseller.R): Boolean {
        for (item in cartModel) {
            if (item.id == foodItem.id) {
                return true
            }
        }
        return false
    }

    //Calculate Total Item Price
    private fun calculateTotal(): Int {
        var totalPrice = 0
        for (data in cartModel) {
            totalPrice += data.price * data.quantity
        }
        return totalPrice
    }

    //Cart Action Manage
    override fun cartFoodPlus(comingSoonItem: CartModel, position: Int) {
        var num = comingSoonItem.quantity
        if (itemCount >= limitCount) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
               seatMessage,
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num += 1
            comingSoonItem.quantity = num
            updateCartFoodCartList(comingSoonItem)
            cartData()
           // bestSellerFoodAdapter?.notifyDataSetChanged()
        }
    }

    override fun cartFoodMinus(comingSoonItem: CartModel, position: Int) {
        var num = comingSoonItem.quantity
        if (num < 0 || num == 0) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.min_item_msz),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num -= 1
            comingSoonItem.quantity = num
            updateCartFoodCartList(comingSoonItem)
            cartData()
        }
        //bestSellerFoodAdapter?.notifyDataSetChanged()
    }

    private fun removeCartItem(item: CartModel) {
        for (data in cartModel) {
            if (data.id == item.id) {
                cartModel.remove(data)
                break
            }
        }
        bestSellerFoodAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged", "UseCompatLoadingForDrawables", "SetTextI18n")
    private fun cartData() {
        bestSellerFoodAdapter?.notifyDataSetChanged()
        bottomFoodAdapter?.notifyDataSetChanged()
        allFoodAdapter?.notifyDataSetChanged()
        previousFoodAdapter?.notifyDataSetChanged()

        val newLayoutParams: ConstraintLayout.LayoutParams =
            binding?.constraintLayout30?.layoutParams as ConstraintLayout.LayoutParams
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels


        if (cartModel.isEmpty()) {
            cartShow = false
            binding?.constraintLayout30?.hide()
            if (BOOK_TYPE != "FOOD")
            binding?.textView374?.show()

        } else {
            if (!cartShow) {
                newLayoutParams.height = height / 5
                binding?.constraintLayout30?.layoutParams = newLayoutParams
                binding?.constraintLayout30?.show()
                binding?.textView374?.hide()
            }
            binding?.textView149?.setOnClickListener {
                cartShow = if (!cartShow) {
                    binding?.textView149?.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.food_arrow_down,
                        0
                    )
                    binding?.constraintLayout112?.show()
                    newLayoutParams.height = height

                    binding?.constraintLayout30?.layoutParams = newLayoutParams
                    binding?.constraintLayout30?.setBackgroundColor(getColor(R.color.transparent2))
                    true
                } else {
                    binding?.textView149?.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.food_arrow_up,
                        0
                    )
                    binding?.constraintLayout112?.hide()
                    newLayoutParams.height = height/5

                    binding?.constraintLayout30?.layoutParams = newLayoutParams
                    binding?.constraintLayout30?.setBackgroundColor(getColor(R.color.transparent1))

                    false
                }
            }

            //total Price
            itemCheckPriceCart = calculateTotal()
            binding?.textView148?.text =
                getString(R.string.currency) + Constant.DECIFORMAT.format(itemCheckPriceCart / 100.0)
            binding?.textView149?.text = cartModel.size.toString() + " " + getString(R.string.items)


            if (!cartShow) {
                println("cartShow--->$cartShow")

                val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                val cartAdapter = CartAdapter(cartModel, this, this)
                binding?.recyclerView41?.layoutManager = layoutManager
                binding?.recyclerView41?.adapter = cartAdapter
            }

            itemCount = getItemCount(cartModel)

            binding?.constraintLayout30?.setOnClickListener {
                newLayoutParams.height = height/5
                binding?.constraintLayout30?.setBackgroundColor(getColor(R.color.transparent1))
                binding?.constraintLayout30?.layoutParams = newLayoutParams
                binding?.constraintLayout112?.hide()
                cartShow = false
                binding?.textView149?.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.food_arrow_up,
                    0
                )
            }

        }
    }



    private fun updateCartFoodCartList(recyclerData: CartModel) {
        try {
            for (item in cartModel) {
                if (item.id == recyclerData.id) {
                    if (recyclerData.quantity == 0) {
                        removeCartItem(item)
                        break
                    } else {
                        item.quantity = recyclerData.quantity
                        break
                    }
                }
            }

            updateCartMainList(recyclerData)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCartMainList(recyclerData: CartModel) {
        for (item in foodResponse!!.bestsellers) {
            if (item.r.size>1) {
                for (data in item.r) {
                    if (recyclerData.id == data.id) {
                        data.qt = recyclerData.quantity
                        break
                    }
                }
            }else{
                if (recyclerData.id == item.r[0].id) {
                    item.qt = recyclerData.quantity
                    break
                }
            }
        }
        cartData()
        updateMainList(catFilterBestSeller)
        bestSellerFoodAdapter?.notifyDataSetChanged()
    }

    // All  Food Mfl Action Manage

    private fun updateCategoryFoodCartList(comingSoonItem: FoodResponse.Output.Mfl) {
        if (cartModel.size == 0) {
            cartModel.add(
                CartModel(
                    comingSoonItem.r[0].id,
                    comingSoonItem.nm,
                    comingSoonItem.mi,
                    comingSoonItem.qt,
                    comingSoonItem.dp,
                    comingSoonItem.veg,
                    comingSoonItem.r[0].ho,
                    comingSoonItem.mid.toString()
                )
            )
        } else {
            if (updateCategoryItemExist(comingSoonItem)) {
                for (item in cartModel) {
                    if (item.id == comingSoonItem.r[0].id) {
                        if (comingSoonItem.qt == 0) {
                            cartModel.remove(item)
                        } else {
                            item.quantity = comingSoonItem.qt

                        }
                        break
                    }
                }
            } else {
                cartModel.add(
                    CartModel(
                        comingSoonItem.r[0].id,
                        comingSoonItem.nm,
                        comingSoonItem.mi,
                        comingSoonItem.qt,
                        comingSoonItem.dp,
                        comingSoonItem.veg,
                        comingSoonItem.r[0].ho,
                        comingSoonItem.mid.toString()
                    )
                )
            }
        }
    }

    private fun updateCategoryItemExist(foodItem: FoodResponse.Output.Mfl): Boolean {
        for (item in cartModel) {
            if (item.id == foodItem.r[0].id) {
                return true
            }
        }
        return false
    }

    //Mfl All Food
    private fun getFilterAllMfl(category: Boolean, menuType: Int, name: String): ArrayList<FoodResponse.Output.Mfl> {
        val categoryFilterNew = ArrayList<FoodResponse.Output.Mfl>()
        when (menuType) {
            0 -> {
                if (name == "ALL") {
                    for (data in catFilter) {
                        categoryFilterNew.add(data)
                    }
                } else {
                    for (data in catFilter) {
                        if (data.ctn == name) {
                            categoryFilterNew.add(data)
                        }
                    }
                }

            }
            1 -> {
                for (data in catFilter) {
                    if (name == "ALL") {
                        if (data.veg == category && data.veg) {
                            categoryFilterNew.add(data)
                        }
                    } else {
                        if (data.ctn == name) {
                            if (data.veg == category && data.veg) {
                                categoryFilterNew.add(data)
                            }
                        }
                    }
                }
            }
            2 -> {
                for (data in catFilter) {
                    if (name == "ALL") {
                        if (!data.veg == category && !data.veg) {
                            categoryFilterNew.add(data)
                        }
                    } else {
                        if (data.ctn == name) {
                            if (!data.veg == category && !data.veg) {
                                categoryFilterNew.add(data)
                            }
                        }
                    }

                }
            }
            else -> {
                for (data in catFilter) {
                    if (name == "ALL") {
                        if (data.veg == category) categoryFilterNew.add(data)
                    }
                    if (data.ctn == name) {
                        if (data.veg == category) categoryFilterNew.add(data)
                    }

                }

            }

        }
        if (categoryFilterNew.size==0){
            toast("There are no non veg food in $name")
        }
        return categoryFilterNew
    }

    //Mfl All Food
    override fun categoryFoodClick(comingSoonItem: FoodResponse.Output.Mfl) {
        if (itemCount >= limitCount) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                seatMessage,
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()

        }else{
            var num = comingSoonItem.qt
            num += 1
            comingSoonItem.qt = num
            updateCategoryFoodCartList(comingSoonItem)
            cartData()
        }
    }

    override fun categoryFoodImageClick(comingSoonItem: FoodResponse.Output.Mfl) {
        bottomDialogAllFood(comingSoonItem)
    }

    override fun categoryFoodPlus(comingSoonItem: FoodResponse.Output.Mfl, position: Int) {
        if (itemCount >= limitCount) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                seatMessage,
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            comingSoonItem.qt = comingSoonItem.qt+1
            updateCategoryFoodCartList(comingSoonItem)
            cartData()
        }
    }

    override fun categoryFoodMinus(comingSoonItem: FoodResponse.Output.Mfl, position: Int) {
        var num = comingSoonItem.qt
        if (num < 0 || num == 0) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.min_item_msz),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num -= 1
            comingSoonItem.qt = num
            updateCategoryFoodCartList(comingSoonItem)
            cartData()
        }
    }

    private fun bottomDialogAllFood(comingSoonItem: FoodResponse.Output.Mfl) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val bindingBottom = FoodBottomAddFoodBinding.inflate(inflater)
        dialog.setContentView(bindingBottom.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
//Manage Veg-NonVeg
        if (comingSoonItem.veg) {
            bindingBottom.imageView69.setImageDrawable(getDrawable(R.drawable.veg_ic))
        } else {
            bindingBottom.imageView69.setImageDrawable(getDrawable(R.drawable.nonveg_ic))
        }
        //Image View
        Glide.with(this).load(comingSoonItem.mi).error(R.drawable.app_icon)
            .into(bindingBottom.imageView64)

        //title
        bindingBottom.textView137.text = comingSoonItem.nm

        //price
        bindingBottom.textView150.text =
            getString(R.string.currency) + " " + Constant.DECIFORMAT.format(comingSoonItem.dp / 100.0)

        if (comingSoonItem.r[0].wt != null && comingSoonItem.r[0].wt !="" && comingSoonItem.r[0].en != null && comingSoonItem.r[0].en != "") {
            bindingBottom.cal.text = comingSoonItem.r[0].wt + "  •  " + comingSoonItem.r[0].en
            bindingBottom.cal.show()
        } else if (comingSoonItem.r[0].wt != null && comingSoonItem.r[0].wt != "") {
            bindingBottom.cal.text = comingSoonItem.r[0].wt
            bindingBottom.cal.show()
        } else if (comingSoonItem.r[0].en != null && comingSoonItem.r[0].en != "") {
            bindingBottom.cal.text = comingSoonItem.r[0].wt
            bindingBottom.cal.show()
        } else {
            bindingBottom.cal.hide()
        }

        if (comingSoonItem.r[0].fa != null && comingSoonItem.r[0].fa != "") {
            bindingBottom.fa.text = "Allergens " + comingSoonItem.r[0].fa
            bindingBottom.fa.show()
        } else {
            bindingBottom.fa.hide()
        }

        //manageUI
        if (comingSoonItem.qt != 0) {
            bindingBottom.textView309.hide()
            bindingBottom.consAddUi.show()
            bindingBottom.uiPlusMinus.foodCount.text = comingSoonItem.qt.toString()
        } else {
            bindingBottom.textView309.show()
            bindingBottom.consAddUi.hide()
        }

        bindingBottom.textView309.setOnClickListener {
            if (comingSoonItem.r.size > 1) {
                dialog.dismiss()
                bottomDialogCart(comingSoonItem.r, comingSoonItem.nm,comingSoonItem.cid.toString())
            } else {
                var num = comingSoonItem.qt
                num += 1
                comingSoonItem.qt = num
                updateCategoryFoodCartList(comingSoonItem)
                cartData()
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()

                bindingBottom.textView309.hide()
                bindingBottom.consAddUi.show()
            }
        }

        //Add Food
        bindingBottom.uiPlusMinus.plus.setOnClickListener {
            var num = comingSoonItem.qt
            if (itemCount >= limitCount) {
                val dialog2 = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    seatMessage,
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog2.show()
            } else {
                num += 1
                comingSoonItem.qt = num
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()
                updateCategoryFoodCartList(comingSoonItem)
                cartData()
            }
        }

        // Subtract Food
        bindingBottom.uiPlusMinus.minus.setOnClickListener {
            var num = comingSoonItem.qt
            if (num < 0 || num == 0) {
                val dialog2 = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.min_item_msz),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog2.show()
            } else {
                num -= 1
                comingSoonItem.qt = num
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()

                if (comingSoonItem.qt == 0) {
                    bindingBottom.textView309.show()
                    bindingBottom.consAddUi.hide()
                }
                updateCategoryFoodCartList(comingSoonItem)
                cartData()
            }
        }

    }

    private fun bottomDialogCart(
        comingSoonItem: List<FoodResponse.Output.Bestseller.R>, titleTxt: String,cid:String
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.food_bottom_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView22)
        val cancel = dialog.findViewById<ImageView>(R.id.imageView71)
        val proceed = dialog.findViewById<TextView>(R.id.textView5)
        val title = dialog.findViewById<TextView>(R.id.textView143)
         amtViewFood = dialog.findViewById<RelativeLayout>(R.id.amtViewFood)
         amtFood = dialog.findViewById<TextView>(R.id.amtFood)

        val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        bottomFoodAdapter = BottomFoodAdapter(getCustomFoodList(comingSoonItem), this, this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = bottomFoodAdapter

        title.text = titleTxt

        proceed.text = getString(R.string.proceed)
        cancel.setOnClickListener {
            updateCardFromProceed(comingSoonItem,0,cid)
            dialog.dismiss()
        }

        proceed.setOnClickListener {
            updateCardFromProceed(comingSoonItem,1,cid)
            cartData()
            dialog.dismiss()
            bestSellerFoodAdapter?.notifyDataSetChanged()
            allFoodAdapter?.notifyDataSetChanged()
        }

        updateFoodItemAmt(comingSoonItem as ArrayList<FoodResponse.Output.Bestseller.R>)

        popupFood = comingSoonItem
    }



    override fun onBackPressed() {
        Constant.SeatBack = 1
        onBackPressedDispatcher.onBackPressed()
        authViewModel.cancelTrans(CINEMA_ID, TRANSACTION_ID, BOOKING_ID)
    }

    //Internet Check
    private fun broadcastIntent() {
        registerReceiver(
            broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }


    //Popup Banners
    private fun initBanner(bannerModels: ArrayList<BookingResponse.Output.Pu>) {
        bannerShow += 1
        bannerModelsMain = bannerModels
        if (bannerModels.isNotEmpty()) {
            binding?.rlBanner?.show()
            binding?.bannerLayout?.includeStoryLayout?.stories?.setStoriesCount(bannerModels.size) // <- set stories
            binding?.bannerLayout?.includeStoryLayout?.stories?.setStoryDuration(5000L) // <- set a story duration
            binding?.bannerLayout?.includeStoryLayout?.stories?.setStoriesListener(this) // <- set listener
            binding?.bannerLayout?.includeStoryLayout?.stories?.startStories() // <- start progress
            counterStory = 0
            if (!TextUtils.isEmpty(bannerModels[counterStory].i)) {
                Picasso.get().load(bannerModels[counterStory].i)
                    .into(binding?.bannerLayout?.includeStoryLayout?.ivBanner!!, object : Callback {
                        override fun onSuccess() {
                            binding?.rlBanner?.show()
                            //  storiesProgressView.startStories(); // <- start progress
                        }

                        override fun onError(e: Exception?) {}
                    })
            }

            binding?.bannerLayout?.includeStoryLayout?.reverse?.setOnClickListener { binding?.bannerLayout?.includeStoryLayout?.stories?.reverse() }
            binding?.bannerLayout?.includeStoryLayout?.reverse?.setOnTouchListener(onTouchListener)
            showButton(bannerModels[0])
            binding?.bannerLayout?.includeStoryLayout?.skip?.setOnClickListener { binding?.bannerLayout?.includeStoryLayout?.stories?.skip() }
            binding?.bannerLayout?.includeStoryLayout?.skip?.setOnTouchListener(onTouchListener)
            binding?.bannerLayout?.tvButton?.setOnClickListener {
                binding?.rlBanner?.hide()
                if (bannerModels.size > 0 && bannerModels[counterStory].type.equals(
                        "image", ignoreCase = true
                    )
//                        .equalsIgnoreCase("image")
                ) {
                    if (bannerModels[counterStory].redirectView.equals(
                            "DEEPLINK", ignoreCase = true
                        )
//                        equalsIgnoreCase("DEEPLINK")
                    ) {
                        if (bannerModels[counterStory].redirect_url.equals("", ignoreCase = true)) {
                            if (bannerModels[counterStory].redirect_url.lowercase(Locale.ROOT)
                                    .contains("/loyalty/home")
                            ) {
//                                if (context is PCLandingActivity) (context as PCLandingActivity).PriviegeFragment(
//                                    "C"
//                                )
                            } else {
//                                .replaceAll("https", "app").replaceAll("http", "app")
                                val intent = Intent(
                                    Intent.ACTION_VIEW, Uri.parse(
                                        bannerModels[counterStory].redirect_url.replace(
                                            "https", "app"
                                        )
                                    )
                                )
                                startActivity(intent)
                            }
                        }
                    } else if (bannerModels[counterStory].redirect_url.equals(
                            "INAPP", ignoreCase = true
                        )
                    ) {
                        if (bannerModels[counterStory].redirect_url.equals("", ignoreCase = true)

                        ) {
//                            val intent = Intent(context, PrivacyActivity::class.java)
//                            intent.putExtra("url", bannerModels[counterStory].getRedirect_url())
//                            intent.putExtra(PCConstants.IS_FROM, 2000)
//                            intent.putExtra("title", bannerModels[counterStory].getName())
//                            startActivity(intent)
                        }
                    } else if (bannerModels[counterStory].redirect_url.equals(
                            "WEB", ignoreCase = true
                        )
//                            .equalsIgnoreCase("WEB")
                    ) {
                        if (bannerModels[counterStory].redirect_url.equals("", ignoreCase = true)
//                                .equalsIgnoreCase("")
                        ) {
//                            val intent = Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse(bannerModels[counterStory].redirect_url)
//                                        activity.startActivity(intent)
                        }
                    }
                }
            }
            (this.findViewById(R.id.bannerLayout) as RelativeLayout).show()

            binding?.bannerLayout?.ivPlay?.setOnClickListener {
                binding?.rlBanner?.hide()
                if (bannerModels.size > 0 && bannerModels[counterStory].type.equals(
                        "video", ignoreCase = true
                    )
                ) {

                }
            }

            binding?.bannerLayout?.ivCross?.setOnClickListener {
                binding?.rlBanner?.hide()
            }

        } else {
            binding?.rlBanner?.hide()
        }
    }

    private fun showButton(bannerModel: BookingResponse.Output.Pu) {
        if (bannerModel.type.contains("video", ignoreCase = true)) {
            binding?.bannerLayout?.ivPlay?.show()
            binding?.bannerLayout?.tvButton?.hide()
        } else if (bannerModel.type.contains(
                "image", ignoreCase = true
            ) && bannerModel.redirect_url.equals("", ignoreCase = true)
        ) {
            binding?.bannerLayout?.ivPlay?.hide()
            binding?.bannerLayout?.tvButton?.text = bannerModel.buttonText
            binding?.bannerLayout?.tvButton?.show()
        } else {
            binding?.bannerLayout?.ivPlay?.hide()
            binding?.bannerLayout?.tvButton?.hide()
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                binding?.bannerLayout?.includeStoryLayout?.stories?.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                binding?.bannerLayout?.includeStoryLayout?.stories?.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }

    override fun onNext() {
        try {
            if (!TextUtils.isEmpty(bannerModelsMain[counterStory].i)) {
                ++counterStory
                showButton(bannerModelsMain[counterStory])
                binding?.bannerLayout?.includeStoryLayout?.ivBanner?.let {
                    Glide.with(this).load(bannerModelsMain[counterStory].i).into(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPrev() {
        if (counterStory - 1 < 0) return
        if (!TextUtils.isEmpty(bannerModelsMain[counterStory].i)) {
            --counterStory
            showButton(bannerModelsMain[counterStory])
            binding?.bannerLayout?.includeStoryLayout?.ivBanner?.let {
                Glide.with(this).load(bannerModelsMain[counterStory].i).into(it)
            }
        }
    }

    override fun onComplete() {
        binding?.bannerLayout?.includeStoryLayout?.stories?.destroy()
        binding?.bannerLayout?.includeStoryLayout?.stories?.startStories()
        currentPage = 0
        binding?.rlBanner?.hide()
    }

    // Place Holder
    private fun updatePH(phd: ArrayList<HomeResponse.Ph>) {
        if (phd != null && phd.size > 0) {
            binding?.include41?.placeHolderView?.show()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            binding?.include41?.recyclerPromotion?.layoutManager = layoutManager
            binding?.include41?.recyclerPromotion?.onFlingListener = null
            snapHelper.attachToRecyclerView(binding?.include41?.recyclerPromotion!!)
            binding?.include41?.recyclerPromotion?.layoutManager = layoutManager
            val adapter = PromotionAdapter(this, phd)
            binding?.include41?.recyclerPromotion?.adapter = adapter
            if (phd.size > 1) {
                val speedScroll = 5000
                val handler = Handler()
                val runnable: Runnable = object : Runnable {
                    var count = 0
                    var flag = true
                    override fun run() {
                        if (count < adapter.itemCount) {
                            if (count == adapter.itemCount - 1) {
                                flag = false
                            } else if (count == 0) {
                                flag = true
                            }
                            if (flag) count++ else count--
                            binding?.include41?.recyclerPromotion?.smoothScrollToPosition(
                                count
                            )
                            handler.postDelayed(this, speedScroll.toLong())
                        }
                    }
                }
                handler.postDelayed(runnable, speedScroll.toLong())
            }
        } else {
            binding?.include41?.placeHolderView?.hide()
        }
    }

    override fun onResume() {
        super.onResume()

        updateList()
    }

    private fun updateList() {
        println("SUMMERYBACK--->${foodCartModel.size}")

        if (SUMMERYBACK==1){
            cartModel = foodCartModel
            //cart
            if (cartModel.size==0){
                for (item in foodResponse!!.bestsellers) {
                    if (item.r.size>1) {
                        for (data in item.r) {
                            data.qt = 0
                        }

                    }else{
                        item.qt = 0
                    }
                }
                cartData()
                updateMainList(catFilterBestSeller)
                bestSellerFoodAdapter?.notifyDataSetChanged()
            }else {
                for (item in cartModel) {
                    updateCartMainList(item)
                }
            }
        }

    }

    private fun updateMainList(catFilterBestSeller2: ArrayList<FoodResponse.Output.Bestseller>) {
        try {
            for (item in catFilterBestSeller2){
                for (item2 in cartModel){
                    for (data in item.r){
                        if (item2.id == data.id){
                            data.qt = item2.quantity
                            item.qt = getFoodQTCount(item.r)
                            break
                        }
                    }
                }
            }
            for (item in foodResponse!!.mfl){
                for (item2 in cartModel){
                    for (data in item.r){
                        if (item2.id == data.id){
                            data.qt = item2.quantity
                            item.qt = getFoodQTCount(item.r)
                            break
                        }
                    }
                }
            }
            cartData()

        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
    }


    // Get Updated List From Cart to popup
    private fun getCustomFoodList(list:List<FoodResponse.Output.Bestseller.R>):List<FoodResponse.Output.Bestseller.R>{
        for (data in list){
            for (cartData in cartModel){
                if (data.id == cartData.id){
                    data.qt = cartData.quantity
                }
            }
        }
        return list
    }

    // Update Cart from proceed btn
    private fun updateCardFromProceed(comingSoonItem: List<FoodResponse.Output.Bestseller.R>, type: Int, cid: String) {
        if (type==1) {
            for (data in comingSoonItem) {
                if (checkInCart(data)) {
                } else {
                    if (data.qt > 0)
                        cartModel.add(
                            CartModel(
                                data.id,
                                data.h,
                                data.i,
                                data.qt,
                                data.dp,
                                data.veg,
                                data.ho,
                                data.masterItemId.toString()
                            )
                        )
                }
            }
            updateMainListQt(cid,comingSoonItem)
        }else{
            for (data in comingSoonItem){
                data.qt = 0
            }

        }
    }

    private fun updateMainListQt(cid: String, comingSoonItem: List<FoodResponse.Output.Bestseller.R>) {
        for (data in foodResponse!!.mfl){
            if (data.cid == cid.toInt()){
                data.qt = getFoodQTCount(comingSoonItem)
                break
            }
        }

        for (data in foodResponse!!.bestsellers){
            if (data.cid == cid.toInt()){
                data.qt = getFoodQTCount(comingSoonItem)
                break
            }
        }
    }

    private fun checkInCart(food:FoodResponse.Output.Bestseller.R): Boolean {
        var exist = false
        for (data in cartModel){
            println("checkData....>${data.id}----${food.id}----${food.qt}")
            if (data.id == food.id){
                data.quantity = food.qt
                exist = true
                break
            }
        }
        return exist
    }

    // Update Food Amt in Custom food popup

    private fun updateFoodItemAmt(food:ArrayList<FoodResponse.Output.Bestseller.R>){
        var price = 0.0
        for (data in food){
            if (data.qt>0){
                price += (data.qt.toDouble() * data.dp.toDouble())
            }
        }
        if (price>0 && amtViewFood!=null){
            amtViewFood?.show()
            amtFood?.text = (price/100.0).toString()
        }else{
            amtViewFood?.hide()
        }

    }

}