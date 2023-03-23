package com.net.pvr.ui.myBookings

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityMyBookingBinding
import com.net.pvr.databinding.ActivityPurchaseEvoucherDetailsBinding
import com.net.pvr.databinding.ExperienceDialogBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.bookingSession.MovieSessionActivity
import com.net.pvr.ui.bookingSession.adapter.BookingTheatreAdapter
import com.net.pvr.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.food.FoodActivity
import com.net.pvr.ui.giftCard.GiftCardActivity
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.fragment.more.eVoucher.EVoucherActivity
import com.net.pvr.ui.home.fragment.more.eVoucher.purchaseDetails.PurchaseEVoucherDetailsActivity
import com.net.pvr.ui.myBookings.adapter.FoodTicketChildAdapter
import com.net.pvr.ui.myBookings.adapter.GiftCardAdapter
import com.net.pvr.ui.myBookings.adapter.VoucherListAdapter
import com.net.pvr.ui.myBookings.response.FoodTicketResponse
import com.net.pvr.ui.myBookings.response.GiftCardResponse
import com.net.pvr.ui.myBookings.response.MyVoucherList
import com.net.pvr.ui.myBookings.viewModel.MyBookingViewModel
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.Constant
import com.net.pvr.utils.Constant.Companion.QR
import com.net.pvr.utils.NetworkResult
import com.net.pvr.utils.ga.GoogleAnalytics
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import dagger.hilt.android.AndroidEntryPoint
import java.lang.String
import java.util.*
import javax.inject.Inject
import kotlin.getValue
import kotlin.toString

@AndroidEntryPoint
class MyBookingsActivity : AppCompatActivity(),
    GiftCardAdapter.RecycleViewItemClickListener,
    FoodTicketChildAdapter.RecycleViewItemClickListener,
    BookingTheatreAdapter.RecycleViewItemClickListener ,
    VoucherListAdapter.RecycleViewItemClickListener{

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityMyBookingBinding? = null
    private var loader: LoaderDialog? = null
    private var ticketPastList = ArrayList<FoodTicketResponse.Output.C>()
    private val authViewModel: MyBookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookingBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        //FoodCall
        authViewModel.foodTicket(
            preferences.getUserId(),
            Constant().getDeviceId(this),
            "",
            preferences.getCityName(),
            "",
            "NO"
        )

        bookParking()
        showParking()
        resendMail()
        giftCard()
        foodTicket()
        movedNext()
        movies()
        voucher()

        binding?.ivPastDrop?.setOnClickListener {
            if (ticketPastList.size > 0) {
                binding?.ivPastDrop?.hide()
                binding?.ivPastDropUp?.show()
                binding?.pastTicketListView?.show()
                binding?.tvShowAll?.show()
            } else {
                val intent = Intent(this, PastBookingsActivity::class.java)
                startActivity(intent)
            }

            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Bookings")
//                bundle.putString("var_experiences_banner", comingSoonItem.name)
                GoogleAnalytics.hitEvent(this, "my_bookings_previous_booking", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        binding?.ivPastDropUp?.setOnClickListener {
            binding?.pastTicketListView?.hide()
            binding?.tvShowAll?.hide()
            binding?.ivPastDropUp?.hide()
            binding?.ivPastDrop?.show()


            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Bookings")
//                bundle.putString("var_experiences_banner", comingSoonItem.name)
                GoogleAnalytics.hitEvent(this, "my_bookings_previous_booking", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        binding?.tvShowAll?.setOnClickListener {
            val intent = Intent(this, PastBookingsActivity::class.java)
            startActivity(intent)
        }
    }

    //Item Action
    private fun movedNext() {

        //Back Click
        binding?.include2?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //title
        binding?.include2?.textView108?.text = getString(R.string.booking_history)

        //GiftCard Action
        binding?.textView52?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Bookings")
//                bundle.putString("var_experiences_banner", comingSoonItem.name)
                GoogleAnalytics.hitEvent(this, "my_bookings_previous_gift_card ", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            //quantity
            binding?.textView3?.hide()

            //GiftCard Api Call
            authViewModel.giftCard(preferences.getUserId(), Constant().getDeviceId(this))

            //gift Card
            binding?.textView52?.textSize = 14F
            binding?.textView52?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
            binding?.textView52?.setTextColor(getColor(R.color.black))

            //  food
            binding?.textView4?.textSize = 12F
            binding?.textView4?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)
            binding?.textView4?.setTextColor(getColor(R.color.black_40))


            //view ticket food
            binding?.view17?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.gray)
            //giftCard
            binding?.view18?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.yellow)

            //  Voucher
            binding?.textView419?.textSize = 14F
            binding?.textView419?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)
            binding?.textView419?.setTextColor(getColor(R.color.black_40))


            //view Voucher
            binding?.view244?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.gray)


        }

        //Ticket & Food Action
        binding?.textView4?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Bookings")
//                bundle.putString("var_experiences_banner", comingSoonItem.name)
                GoogleAnalytics.hitEvent(this, "my_bookings_previous_food", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            //quantity
            binding?.textView3?.show()

            //Ticket & Food Api Call
            authViewModel.foodTicket(
                preferences.getUserId(),
                Constant().getDeviceId(this),
                "",
                preferences.getCityName(),
                "",
                "NO"
            )


            //gift Card
            binding?.textView52?.textSize = 12F
            binding?.textView52?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)
            binding?.textView52?.setTextColor(getColor(R.color.black_40))

            //  food
            binding?.textView4?.textSize = 14F
            binding?.textView4?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
            binding?.textView4?.setTextColor(getColor(R.color.black))


            //view ticket food
            binding?.view17?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.yellow)
            //giftCard
            binding?.view18?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.gray)



            //  Voucher
            binding?.textView419?.textSize = 14F
            binding?.textView419?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)
            binding?.textView419?.setTextColor(getColor(R.color.black_40))


            //view Voucher
            binding?.view244?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.gray)

        }

        //Vouchers
        binding?.voucher?.setOnClickListener {
            //gift Card
            binding?.textView52?.textSize = 12F
            binding?.textView52?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)
            binding?.textView52?.setTextColor(getColor(R.color.black_40))

            //giftCard
            binding?.view18?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.gray)

            //  food
            binding?.textView4?.textSize = 12F
            binding?.textView4?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)
            binding?.textView4?.setTextColor(getColor(R.color.black_40))

            //view ticket food
            binding?.view17?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.gray)



            //  Voucher
            binding?.textView419?.textSize = 14F
            binding?.textView419?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
            binding?.textView419?.setTextColor(getColor(R.color.black))


            //view Voucher
            binding?.view244?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.yellow)

            authViewModel.voucher(preferences.getUserId())
        }

    }

    //Gift Card Api
    private fun giftCard() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveGiftCardData(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    //Food With Ticket Api
    private fun foodTicket() {
        authViewModel.ticketFoodLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveFoodTicketData(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun resendMail() {
        authViewModel.resendMailLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        println("hello")
                    } else {
                        val dialog = OptionDialog(this,
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun bookParking() {
        authViewModel.bookParkResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val data =
                            it.data.output.url + "?partner_name=" + it.data.output.partner_name + "&booking_id=" + it.data.output.booking_id + "&location_id=" + it.data.output.location_id + "&date=" + it.data.output.date + "&time=" + it.data.output.time + "&duration=" + it.data.output.duration + "&hmac=" + it.data.output.hmac

                        val intent = Intent(this, WebViewActivity::class.java)
                        intent.putExtra("title", "View Parking")
                        intent.putExtra("from", "parking")
                        intent.putExtra("getUrl", data)
                        startActivity(intent)
                    } else {
                        val dialog = OptionDialog(this,
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun showParking() {
        authViewModel.showParkingResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val intent = Intent(this, WebViewActivity::class.java)
                        intent.putExtra("title", "View Parking")
                        intent.putExtra("from", "parking")
                        intent.putExtra("getUrl", it.data.output.url)
                        startActivity(intent)
                    } else {
                        val dialog = OptionDialog(this,
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveTheatreData(output: BookingTheatreResponse.Output) {
        if (output.m.isEmpty()) {
            binding?.llAlsoPlaying?.hide()
        } else {
            binding?.llAlsoPlaying?.show()
            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val bookingTheatreAdapter = BookingTheatreAdapter(output.m, this, this)
            binding?.rvAlsoPlaying?.layoutManager = gridLayout2
            binding?.rvAlsoPlaying?.adapter = bookingTheatreAdapter
        }
    }

    private fun movies() {
        authViewModel.userResponseTheatreLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveTheatreData(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    //Food With Ticket Response
    private fun retrieveFoodTicketData(output: FoodTicketResponse.Output) {
        //title
        binding?.ticketView?.show()
        binding?.giftView?.hide()
        binding?.textView3?.text = getString(R.string.upcomingBooking) + output.c.size
        if (output.c.size > 0) {
            binding?.llAlsoPlaying?.show()
            authViewModel.bookingTheatre(
                preferences.getCityName(),
                "",
                preferences.getUserId(),
                output.c[0].mc,
                output.c[0].lg,
                "NO"
            )
        } else {
            binding?.llAlsoPlaying?.hide()
        }

        ticketPastList = output.p
        if (output.c.size>0) {
            binding?.textView3?.show()
            binding?.recyclerMyBooking?.show()
            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val foodTicketAdapter = FoodTicketChildAdapter(getUpdatedList(output.c), this, false, this)
            binding?.recyclerMyBooking?.layoutManager = gridLayout2
            binding?.recyclerMyBooking?.adapter = foodTicketAdapter
        }else{
            binding?.textView3?.hide()
            binding?.recyclerMyBooking?.hide()
        }

        if (output.p.size>0) {
            binding?.llPastBooking?.show()
            val gridLayoutPast = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val pastTicketAdapter = FoodTicketChildAdapter(getUpdatedList(output.p), this, true, this)
            binding?.pastTicketListView?.layoutManager = gridLayoutPast
            binding?.pastTicketListView?.adapter = pastTicketAdapter
        }else{
            if (output.c.size>0){
                binding?.llPastBooking?.show()

                binding?.pastTicketListView?.hide()
                binding?.tvShowAll?.show()
            }else{
                binding?.llPastBooking?.hide()

            }
        }

        if (output.c.size == 0 && output.p.size ==0){
            binding?.noData?.show()
            binding?.ticketView?.hide()
        }else{
            binding?.noData?.hide()
            binding?.ticketView?.show()
        }
        binding?.noTitle?.text = "No Bookings Available"
        binding?.noSubTitle?.text = "Movie bookings and F&B order will appear here"
        binding?.buttonProceed?.text = "Book Movie"
        binding?.noDataImg?.setImageResource(R.drawable.cinema_no_data)

        binding?.buttonProceed?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUpdatedList(p: ArrayList<FoodTicketResponse.Output.C>): ArrayList<FoodTicketResponse.Output.C> {
        var newListFood = ArrayList<FoodTicketResponse.Output.C>()
        var newList = ArrayList<FoodTicketResponse.Output.C>()
        for (data in p){
            if (data.is_only_fd){
                newListFood.add(data)
            }
        }
        if (newListFood.size>0){
            for (data in newListFood){
                if (checkId(data,p)){
                    p.remove(data)
                }
            }
        }
        return p
    }

    private fun checkId(data: FoodTicketResponse.Output.C, newList: ArrayList<FoodTicketResponse.Output.C>): Boolean {
        var flag = false
        for (item in newList){
            if (data.abi != ""){
                if (data.abi == item.bi){
                    item.food = data.food
                    flag = true
                }else{
                    return false
                }
            }else {
                return false
            }
        }
        return flag
    }

    override fun onResume() {
        super.onResume()
        if (Constant.BACK_TO_BOOKING){
            authViewModel.foodTicket(
                preferences.getUserId(),
                Constant().getDeviceId(this),
                "",
                preferences.getCityName(),
                "",
                "NO"
            )
        }
    }

    //GiftCard
    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveGiftCardData(output: GiftCardResponse.Output) {
        binding?.giftView?.show()
        binding?.ticketView?.hide()
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val giftCardAdapter = GiftCardAdapter(output.gc, this, this)
        binding?.recyclerMyGift?.layoutManager = gridLayout2
        binding?.recyclerMyGift?.adapter = giftCardAdapter

        if (output.gc.isEmpty()){
            binding?.noData?.show()
            binding?.giftView?.hide()
        }else{
            binding?.noData?.hide()
            binding?.giftView?.show()
        }
        binding?.noTitle?.text = "No Gift Cards Available"
        binding?.buttonProceed?.text = "Buy Gift Cards"
        binding?.noSubTitle?.text = "Your Gift Cards will appear here"
        binding?.noDataImg?.setImageResource(R.drawable.gift_card_no_data)

        binding?.buttonProceed?.setOnClickListener {
            val intent = Intent(this, GiftCardActivity::class.java)
            startActivity(intent)
        }
    }

    override fun resend(comingSoonItem: GiftCardResponse.Output.Gc) {
        authViewModel.resendMail(
            preferences.getUserId(), comingSoonItem.id.replace("ID: ",""), "GIFTCARD"
        )
    }

    override fun addFood(data: FoodTicketResponse.Output.C) {
        QR = "NO"
        Constant.AUDI= ""
        Constant.SEAT= ""
        val intent = Intent(this, FoodActivity::class.java)
        intent.putExtra("from", "pcOrdrsnc")
        intent.putExtra("NF", data.nf)
        Constant.CINEMA_ID = data.cid
        Constant.BOOKING_ID = data.bi
        Constant.BOOK_TYPE = "FOOD"
        intent.putExtra("SEATS", String.valueOf(data.seats.split(",").size))
        startActivity(intent)
    }

    override fun onParkingClick(data: FoodTicketResponse.Output.C) {
        if (data.parkbooking) {
            authViewModel.showParking(data.bi)
        } else {
            authViewModel.bookParking(data.bi)
        }
    }

    override fun onDirectionClick(data: FoodTicketResponse.Output.C) {
        Constant().openMap(this,data.ltd,data.lngt)
    }

    override fun theatreClick(comingSoonItem: BookingTheatreResponse.Output.M) {
        val intent = Intent(this, MovieSessionActivity::class.java)
        intent.putExtra("mid", comingSoonItem.m)
        startActivity(intent)
    }




    ////////////////////////////////////  Voucher  //////////////////////////////////////


    private fun voucher() {
        authViewModel.userResponseVoucherLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

                        retrieveVoucher(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveVoucher(output: MyVoucherList.Output) {
        if (output.ev.size == 0 && output.ev.size ==0){
            binding?.noData?.show()
            binding?.ticketView?.hide()
        }else{
            binding?.noData?.hide()
            binding?.ticketView?.show()
        }
        binding?.noTitle?.text = "No Vouchers Available"
        binding?.noSubTitle?.text = "Your Vouchers will appear here"
        binding?.buttonProceed?.text = "Get Vouchers"
        binding?.noDataImg?.setImageResource(R.drawable.no_vouchers)

        binding?.buttonProceed?.setOnClickListener {
            val intent = Intent(this, EVoucherActivity::class.java)
            startActivity(intent)
        }

//        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
//        val giftCardAdapter = VoucherListAdapter(output.gc, this, this)
//        binding?.recyclerMyGift?.layoutManager = gridLayout2
//        binding?.recyclerMyGift?.adapter = giftCardAdapter

    }

    override fun voucherBook(comingSoonItem: GiftCardResponse.Output.Gc) {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val purchaseVoucher = ActivityPurchaseEvoucherDetailsBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<FrameLayout> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(purchaseVoucher.root)
        dialog.show()

        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()

//        val intent = Intent(this, PurchaseEVoucherDetailsActivity::class.java)
//        startActivity(intent)
    }


}