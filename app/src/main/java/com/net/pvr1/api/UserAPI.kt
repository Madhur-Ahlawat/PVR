package com.net.pvr1.api

import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.contactUs.response.ContactUsResponse
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.ui.formats.response.FormatResponse
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.home.fragment.cinema.response.PreferenceResponse
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.ui.myBookings.response.GiftCardResponse
import com.net.pvr1.ui.offer.response.MOfferResponse
import com.net.pvr1.ui.offer.response.OfferResponse
import com.net.pvr1.ui.payment.response.CouponResponse
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.ui.payment.response.PaytmHmacResponse
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr1.ui.seatLayout.response.InitResponse
import com.net.pvr1.ui.seatLayout.response.ReserveSeatResponse
import com.net.pvr1.ui.seatLayout.response.SeatResponse
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import com.net.pvr1.ui.splash.response.SplashResponse
import com.net.pvr1.ui.summery.response.AddFoodResponse
import com.net.pvr1.ui.summery.response.SetDonationResponse
import com.net.pvr1.ui.summery.response.SummeryResponse
import com.net.pvr1.ui.ticketConfirmation.response.TicketBookedResponse
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
        @Query("userid")  userid: String,
        @Query("bookingid")   bookingid: String,
        @Query("transid")  transid: String,
        @Query("unpaid")  unpaid: Boolean,
        @Query("cardNo")  cardNo: String,
        @Query("booktype")  booktype: String,
        @Query("ptype")  ptype: String,
        @Query("isSpi")  isSpi: String,
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
        @Query("searchtxt") searchTxt: String,
        @Query("userid") userid: String,
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
    ): Response<PreferenceResponse>

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

    @POST("loyalty/home")
    suspend fun privilegeHome(
        @Query("mobile") mobile: String,
        @Query("city") city: String,
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
      @Query("bookingid")  bookingid: String,
      @Query("transid") transid: String,
      @Query("isDonate") isDonate: Boolean,
      @Query("istDonate")  istDonate: Boolean,
      @Query("isSpi")  isSpi: String,
      @Query("av")    version: String,
      @Query("pt")   platform: String
  ): Response<SetDonationResponse>

    @POST("content/splashtxt")
    suspend fun splash(
        @Query("city") city: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<SplashResponse>

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

}