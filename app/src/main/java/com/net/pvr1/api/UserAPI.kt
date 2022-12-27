package com.net.pvr1.api

import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.ui.formats.response.FormatResponse
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaPreferenceResponse
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr1.ui.home.fragment.more.contactUs.response.ContactUsResponse
import com.net.pvr1.ui.home.fragment.more.offer.response.MOfferResponse
import com.net.pvr1.ui.home.fragment.more.offer.response.OfferResponse
import com.net.pvr1.ui.home.fragment.more.prefrence.response.PreferenceResponse
import com.net.pvr1.ui.home.fragment.more.response.DeleteAlertResponse
import com.net.pvr1.ui.home.fragment.more.response.WhatsAppOptStatus
import com.net.pvr1.ui.home.fragment.privilege.response.LoyaltyDataResponse
import com.net.pvr1.ui.home.fragment.privilege.response.PassportPlanResponse
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.ui.location.selectCity.response.SelectCityResponse
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.ui.myBookings.response.GiftCardResponse
import com.net.pvr1.ui.payment.response.*
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr1.ui.seatLayout.response.InitResponse
import com.net.pvr1.ui.seatLayout.response.ReserveSeatResponse
import com.net.pvr1.ui.seatLayout.response.SeatResponse
import com.net.pvr1.ui.splash.response.SplashResponse
import com.net.pvr1.ui.summery.response.AddFoodResponse
import com.net.pvr1.ui.summery.response.SetDonationResponse
import com.net.pvr1.ui.summery.response.SummeryResponse
import com.net.pvr1.ui.ticketConfirmation.response.TicketBookedResponse
import com.net.pvr1.ui.watchList.response.WatchListResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAPI {
    @POST("v2/user/login")
    suspend fun loginMobile(
        @Query("mobile") mobile: String,
        @Query("city") city: String,
        @Query("cname") cName: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<LoginResponse>

    @POST("contactus")
    suspend fun contactUs(
        @Query("comment") comment: String,
        @Query("email") email: String,
        @Query("mobile") mobile: String,
        @Query("did") did: String,
        @Query("mobile") isSpi: String,
        @Query("ctype") ctype: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ContactUsResponse>

    @POST("v2/user/verify")
    suspend fun otpVerify(
        @Query("mobile") mobile: String,
        @Header("X-Token") token: String,
        @Query("cname") cName: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ResisterResponse>

    @POST("loyalty/getcoupons")
    suspend fun getCoupons(
        @Query("mobile") mobile: String,
        @Query("city") city: String,
        @Query("status") status: String,
        @Query("pay") pay: Boolean,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CouponResponse>

    @POST("payment/paytmex/hmac")
    suspend fun paytmHMAC(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("transid") transid: String,
        @Query("unpaid") unpaid: Boolean,
        @Query("cardNo") cardNo: String,
        @Query("booktype") booktype: String,
        @Query("ptype") ptype: String,
        @Query("isSpi") isSpi: String,
        @Query("binOffer") binOffer: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    @POST("trans/getpaymode")
    suspend fun payMode(
        @Query("cinemacode") cinemacode: String,
        @Query("booktype") booktype: String,
        @Query("userid") userid: String,
        @Query("mobile") mobile: String,
        @Query("type") type: String,
        @Query("isSpi") isSpi: String,
        @Query("srilanka") srilanka: String,
        @Query("unpaid") unpaid: Boolean,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaymentResponse>

    @POST("v2/user/register")
    suspend fun resister(
        @Header("X-Token") hash: String,
        @Query("email") email: String,
        @Query("name") name: String,
        @Query("mobile") mobile: String,
        @Query("otp") otp: String,
        @Query("city") city: String,
        @Query("cname") cname: Boolean,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ResisterResponse>

    @POST("content/comingsoon/v2")
    suspend fun comingSoon(
        @Query("city") city: String,
        @Query("genre") genre: String,
        @Query("lang") lang: String,
        @Query("userid") userid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CommingSoonResponse>

    @POST("content/alltheater")
    suspend fun cinema(
        @Query("city") city: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("userid") userid: String,
        @Query("searchtxt") searchTxt: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CinemaResponse>

    @POST("user/setprefrenences")
    suspend fun cinemaPreference(
        @Query("userid") userid: String,
        @Query("id") id: String,
        @Query("is_like") is_like: Boolean,
        @Query("type") type: String,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CinemaPreferenceResponse>

    @POST("history/giftcard")
    suspend fun giftCard(
        @Query("userid") userid: String,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<GiftCardResponse>

    @POST("history/history-new/myticket")
    suspend fun foodTicket(
        @Query("userid") userid: String,
        @Query("did") did: String,
        @Query("srilanka") sriLanka: String,
        @Query("city") city: String,
        @Query("isSpi") isSpi: String,
        @Query("past") past: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
    ): Response<FoodTicketResponse>

    @POST("deals/mobile")
    suspend fun offer(
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
    ): Response<OfferResponse>


    @POST("deals/moffers")
    suspend fun mOfferList(
        @Query("did") did: String,
        @Query("city") city: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
    ): Response<MOfferResponse>

    @POST("deals/mdetail")
    suspend fun offerDetails(
        @Query("id") id: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String,
    ): Response<OfferResponse>

    @POST("content/cities")
    suspend fun selectCity(
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("userid") userid: String,
        @Query("isSpi") isSpi: String,
        @Query("srilanka") srilanka: String
    ): Response<SelectCityResponse>

    @POST("content/nowshowingnew2")
    suspend fun home(
        @Query("city") city: String,
        @Query("av") av: String,
        @Query("pt") pt: String,
        @Query("dtmsource") dtmsource: String,
        @Query("userid") userid: String,
        @Query("mobile") mobile: String,
        @Query("upbooking") upbooking: Boolean,
        @Query("srilanka") srilanka: String,
        @Query("type") type: String,
        @Query("lng") lng: String,
        @Query("gener") gener: String,
        @Query("spShow") spShow: String,
        @Query("isSpi") isSpi: String,
    ): Response<HomeResponse>

    @POST("v2/user/optin/check")
    suspend fun whatsappOpt(
        @Query("userid") userid: String,
        @Query("mobile") mobile: String,
        @Query("timestamp") timestamp: String,
        @Header("X-Token") token: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<WhatsAppOptStatus>

    @POST("v2/user/optin")
    suspend fun whatsappOptIn(
        @Query("userid") userid: String,
        @Query("mobile") mobile: String,
        @Query("timestamp") timestamp: String,
        @Header("X-Token") token: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<WhatsAppOptStatus>

    @POST("v2/user/optout")
    suspend fun whatsappOptOut(
        @Query("userid") userid: String,
        @Query("mobile") mobile: String,
        @Query("timestamp") timestamp: String,
        @Header("X-Token") token: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<WhatsAppOptStatus>

    @POST("user/getprefrenences")
    suspend fun preference(
        @Query("city") city: String,
        @Query("userid") userid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PreferenceResponse>

    /***********************   Privilege  API'S     ******************/

    @POST("loyalty/home")
    suspend fun privilegeHome(
        @Query("mobile") mobile: String,
        @Query("city") city: String,
        @Query("av") av: String,
        @Query("pt") pt: String
    ): Response<PrivilegeHomeResponse>

    @POST("loyalty/data")
    suspend fun loyaltyData(
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("mobile") mobile: String,
        @Query("timestamp") timestamp: String,
        @Header("X-Token") token: String,
        @Query("av") av: String,
        @Query("pt") pt: String,
        @Query("did") did: String
    ): Response<LoyaltyDataResponse>


    @POST("offervoucher/loyalty-subscription/v2/plans")
    suspend fun passportPlans(
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("av") av: String,
        @Query("pt") pt: String,
        @Query("did") did: String
    ): Response<PassportPlanResponse>

    @POST("history/nextbooking")
    suspend fun nextBooking(
        @Query("userid") userid: String,
        @Query("did") did: String,
        @Query("av") av: String,
        @Query("pt") pt: String
    ): Response<PrivilegeHomeResponse>

    @POST("content/search")
    suspend fun homeSearch(
        @Query("city") city: String,
        @Query("text") text: String,
        @Query("searchFilter") searchFilter: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<HomeSearchResponse>

    @POST("content/getmovie")
    suspend fun movieDetails(
        @Query("city") city: String,
        @Query("mid") mid: String,
        @Query("av") version: String,
        @Query("type") type: String,
        @Query("pt") platform: String,
        @Query("userid") userid: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("isSpi") isSpi: String,
        @Query("srilanka") srilanka: String
    ): Response<MovieDetailsResponse>

    @POST("content/getmovie")
    suspend fun commingSoon(
        @Query("type") type: String,
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("mid") mcode: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<MovieDetailsResponse>


    @POST("v1/movie-alert/get-alert")
    suspend fun movieAlert(
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("mcode") mcode: String,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<MovieDetailsResponse>

    @POST("content/csessionsfilters")
    suspend fun cinemaSession(
        @Query("city") city: String,
        @Query("cid") cid: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("userid") userid: String,
        @Query("date") date: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("lang") lang: String,
        @Query("format") format: String,
        @Query("price") price: String,
        @Query("time") time: String,
        @Query("hc") hc: String,
        @Query("cc") cc: String,
        @Query("ad") ad: String,
        @Query("qr") qr: String,
        @Query("cinetype") cinetype: String,
        @Query("cinetypeQR") cinetypeQR: String
    ): Response<CinemaSessionResponse>

    @POST("content/msessionsnew")
    suspend fun bookingSession(
        @Query("city") city: String,
        @Query("mid") mid: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("date") date: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("isSpi") isSpi: String,
        @Query("srilanka") srilanka: String,
        @Query("userid") userid: String
    ): Response<BookingResponse>

    @POST("content/movietheater")
    suspend fun bookingTheatre(
        @Query("city") city: String,
        @Query("cid") cid: String,
        @Query("userid") lat: String,
        @Query("mid") mid: String,
        @Query("mlanguage") lng: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("isSpi") isSpi: String
    ): Response<BookingTheatreResponse>

    @POST("content/nearcinetheater")
    suspend fun nearTheatre(
        @Query("city") city: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String?,
        @Query("cid") cid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CinemaNearTheaterResponse>

    @POST("trans/getseatlayoutnew/{cinemacode}/{sessionid}")
    suspend fun seatLayout(
        @Path("cinemacode") cinemacode: String,
        @Path("sessionid") sessionid: String,
        @Query("dtmsource") dtmsource: String,
        @Query("partnerid") partnerid: String,
        @Query("cdate") cdate: String,
        @Query("bundle") bundle: Boolean,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<SeatResponse>

    @POST("trans/reserveseats")
    suspend fun reserveSeatLayout(
        @Query("reserve") reserve: String,
        @Query("qr") qr: String,
        @Query("srilanka") srilanka: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ReserveSeatResponse>

    @POST("trans/initTrans/{cinemacode}/{sessionid}")
    suspend fun initTransSeatLayout(
        @Path("cinemacode") cinemacode: String,
        @Path("sessionid") sessionid: String,
        @Query("dtmsource") dtmsource: String,
        @Query("partnerid") partnerid: String,
        @Query("screenid") screenid: String,
        @Query("adultSeats") adultSeats: String,
        @Query("childSeats") childSeats: String,
        @Query("isMembersDay") isMembersDay: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<InitResponse>

    @POST("v2/food/getfoods")
    suspend fun food(
        @Query("userid") userid: String,
        @Query("ccode") ccode: String,
        @Query("bookingid") bookingid: String,
        @Query("cbookid") cbookid: String,
        @Query("transid") transid: String,
        @Query("type") type: String,
        @Query("audi") audi: String,
        @Query("seat") seat: String,
        @Query("city") city: String,
        @Query("qr") qr: String,
        @Query("iserv") iserv: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<FoodResponse>

    @POST("trans/tckdetails")
    suspend fun summery(
        @Query("transid") transid: String,
        @Query("cinemacode") cinemacode: String,
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("qr") qr: String,
        @Query("srilanka") srilanka: String,
        @Query("infosys") infosys: String,
        @Query("isSpi") isSpi: String,
        @Query("doreq") doreq: Boolean,
        @Query("change") change: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<SummeryResponse>


    @POST("trans/ticketBooked")
    suspend fun ticketConfirmation(
        @Query("booktype") booktype: String,
        @Query("bookingid") bookingid: String,
        @Query("userid") userid: String,
        @Query("qr") qr: String,
        @Query("srilanka") srilanka: String,
        @Query("infosys") infosys: String,
        @Query("isSpi") isSpi: String,
        @Query("change") change: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<TicketBookedResponse>

    @POST("content/specialcine")
    suspend fun formats(
        @Query("type") type: String,
        @Query("city") city: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<FormatResponse>

    @POST("trans/setfood")
    suspend fun ticketWithFood(
        @Query("foods") foods: String,
        @Query("transid") transid: String,
        @Query("cinemacode") cinemacode: String,
        @Query("userid") userId: String,
        @Query("qr") qr: String,
        @Query("infosys") infosys: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("seat") seat: String,
        @Query("audi") audi: String
    ): Response<SummeryResponse>


    @POST("trans/setdonation")
    suspend fun setDonation(
        @Query("bookingid") bookingid: String,
        @Query("transid") transid: String,
        @Query("isDonate") isDonate: Boolean,
        @Query("istDonate") istDonate: Boolean,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<SetDonationResponse>

    @POST("content/splashtxt")
    suspend fun splash(
        @Query("city") city: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<SplashResponse>

    @POST("v1/movie-alert/get-all-alert")
    suspend fun watchList(
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<WatchListResponse>

    @POST("v1/movie-alert/delete-alert")
    suspend fun deleteAlert(
        @Query("userid") userid: String,
        @Query("mcode") mcode: String,
        @Query("city") city: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<DeleteAlertResponse>

    @POST("food/savefoods")
    suspend fun addFood(
        @Query("cinemacode") cinemacode: String,
        @Query("fb_totalprice") fb_totalprice: String,
        @Query("fb_itemStrDescription") fb_itemStrDescription: String,
        @Query("pickupdate") pickupdate: String,
        @Query("cbookid") cbookid: String,
        @Query("audi") audi: String,
        @Query("seat") seat: String,
        @Query("type") type: String,
        @Query("infosys") infosys: String,
        @Query("qr") qr: String,
        @Query("isSpi") isSpi: String,
        @Query("srilanka") srilanka: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<AddFoodResponse>

    @POST("/PVRCinemasCMS/getgiftcard1")
    suspend fun giftCardMain(
        @Query("sWidth") sWidth: String,
        @Query("platform") platform: String,
        @Query("infosys") infosys: String,
        @Query("av") version: String,
        @Query("pt") platform1: String
    ): Response<GiftCardResponse>

    @POST("content/alltheater")
    suspend fun bookingRetrieval(
        @Query("city") city: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("userid") userid: String,
        @Query("searchtxt") searchtxt: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<BookingRetrievalResponse>

    @POST("v1/movie-alert/set-alert")
    suspend fun setAlert(
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("mcode") mcode: String,
        @Query("cinema") cinema: String,
        @Query("whtsapp") whtsapp: String,
        @Query("pushnotify") pushnotify: String,
        @Query("sms") sms: String,
        @Query("email") email: String,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<BookingRetrievalResponse>

    @POST("payment/paytmex/upistatus")
    suspend fun upiStatus(
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<UPIStatusResponse>


    //PhonpePe Api Call
    @POST("payment/phonepe/signature")
    suspend fun phonepeHmac(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PhonepeHmacResponse>

    @POST("payment/phonepe/paydone")
    suspend fun phonepeStatus(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<UPIStatusResponse>


    /**    Paytm PostPaid Api        **/

    @POST("payment/paytmpp/hmac")
    suspend fun postPaidHmac(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("unpaid") unpaid: String,
        @Query("spi") spi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    @POST("payment/paytmpp/pay")
    suspend fun postPaidPay(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("unpaid") unpaid: String,
        @Query("isSpi") spi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    @POST("payment/paytmpp/sendotp")
    suspend fun postPaidSendOTP(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("spi") spi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    @POST("payment/paytmpp/verifyotp")
    suspend fun postPaidVerifYOTP(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("otp") otp: String,
        @Query("spi") spi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    @POST("payment/paytm/withdraw")
    suspend fun postPaidMakePayment(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    @POST("payment/paytm/hmac")
    suspend fun paytmHmacOld(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    /***   AIRTEL PAY      *****/

    @POST("payment/airtel/airtelhmac")
    suspend fun airtelPay(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("servicetype") servicetype: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    /***   PROMO CODE       *****/

    @POST("payment/promocode")
    suspend fun promoCode(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("promocode") promocode: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    /***   GYFTER CODE       *****/

    @POST("payment/gyft")
    suspend fun promoGyft(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("promocode") promocode: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    /***   Zaggle CARD       *****/

    @POST("payment/zaggle/pay")
    suspend fun zagglePay(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("cardnumber") cardnumber: String,
        @Query("otporpin") otporpin: String,
        @Query("type") type: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    /***   GIFT CARD       *****/

    @POST("payment/qccard")
    suspend fun giftCardRedeem(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("gccardnum") gccardnum: String,
        @Query("gcpinnum") gcpinnum: String,
        @Query("type") type: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    /***   ACCENTIVE_PROMO       *****/

    @POST("payment/accentive")
    suspend fun accentivePromo(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("promocode") promocode: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    /***   STARPASS       *****/

    @POST("payment/starpass")
    suspend fun starpass(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("cinemacode") cinemacode: String,
        @Query("starpasses") starpasses: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    /***   MCOUPON       *****/

    @POST("payment/mcoupon")
    suspend fun mcoupon(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("cinemacode") cinemacode: String,
        @Query("mccard") mccard: String,
        @Query("mcmobile") mcmobile: String,
        @Query("mCoupons") mCoupons: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    /***   HYATT       *****/

    @POST("payment/hyatt/otp")
    suspend fun sendOTPHYATT(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("mobile") mobile: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    @POST("payment/hyatt")
    suspend fun verifyOTPHYATT(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("mobile") mobile: String,
        @Query("otp") otp: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    /********      CRED           ****************/


    @POST("payment/cred/check")
    suspend fun credCheck(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("unpaid") unpaid: String,
        @Query("cred_present") cred_present: String,
        @Query("spi") spi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<UPIStatusResponse>


    @POST("payment/cred/hmac")
    suspend fun credHmac(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("unpaid") unpaid: String,
        @Query("cred_present") cred_present: String,
        @Query("spi") spi: String,
        @Query("ptype") ptype: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    @POST("payment/cred/status")
    suspend fun credStatus(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<UPIStatusResponse>

}