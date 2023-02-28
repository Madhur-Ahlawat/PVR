package com.net.pvr.api

import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr.ui.cinemaSession.cinemaDetails.response.CinemaDetailsResponse
import com.net.pvr.ui.cinemaSession.response.CinemaNearTheaterResponse
import com.net.pvr.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr.ui.food.old.reponse.OldFoodResponse
import com.net.pvr.ui.food.response.CancelTransResponse
import com.net.pvr.ui.food.response.FoodResponse
import com.net.pvr.ui.giftCard.response.ActiveGCResponse
import com.net.pvr.ui.giftCard.response.GiftCardDetailResponse
import com.net.pvr.ui.giftCard.response.GiftCardListResponse
import com.net.pvr.ui.giftCard.response.UploadImageGC
import com.net.pvr.ui.home.formats.response.FormatResponse
import com.net.pvr.ui.home.fragment.cinema.response.CinemaPreferenceResponse
import com.net.pvr.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr.ui.home.fragment.comingSoon.response.CommingSoonResponse
import com.net.pvr.ui.home.fragment.home.response.FeedbackDataResponse
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.home.fragment.home.response.NextBookingResponse
import com.net.pvr.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr.ui.home.fragment.more.contactUs.response.ContactUsResponse
import com.net.pvr.ui.home.fragment.more.eVoucher.response.VoucherListResponse
import com.net.pvr.ui.home.fragment.more.experience.response.ExperienceDetailsResponse
import com.net.pvr.ui.home.fragment.more.experience.response.ExperienceResponse
import com.net.pvr.ui.home.fragment.more.offer.offerDetails.response.OfferDetailsResponse
import com.net.pvr.ui.home.fragment.more.offer.response.MOfferResponse
import com.net.pvr.ui.home.fragment.more.offer.response.OfferResponse
import com.net.pvr.ui.home.fragment.more.prefrence.response.PreferenceResponse
import com.net.pvr.ui.home.fragment.more.response.DeleteAlertResponse
import com.net.pvr.ui.home.fragment.more.response.ProfileResponse
import com.net.pvr.ui.home.fragment.more.response.WhatsAppOptStatus
import com.net.pvr.ui.home.fragment.privilege.response.LoyaltyDataResponse
import com.net.pvr.ui.home.fragment.privilege.response.PassportPlanResponse
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr.ui.location.selectCity.response.SelectCityResponse
import com.net.pvr.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr.ui.login.response.LoginResponse
import com.net.pvr.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr.ui.myBookings.response.FoodTicketResponse
import com.net.pvr.ui.myBookings.response.GiftCardResponse
import com.net.pvr.ui.myBookings.response.ParkingResponse
import com.net.pvr.ui.payment.mobikwik.response.MobiKwikCheckSumResponse
import com.net.pvr.ui.payment.mobikwik.response.MobiKwikPayResponse
import com.net.pvr.ui.payment.mobikwik.response.MobikwikOTPResponse
import com.net.pvr.ui.payment.response.*
import com.net.pvr.ui.scanner.response.GetFoodResponse
import com.net.pvr.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr.ui.seatLayout.response.InitResponse
import com.net.pvr.ui.seatLayout.response.ReserveSeatResponse
import com.net.pvr.ui.seatLayout.response.SeatResponse
import com.net.pvr.ui.splash.response.SplashResponse
import com.net.pvr.ui.summery.response.AddFoodResponse
import com.net.pvr.ui.summery.response.ExtendTimeResponse
import com.net.pvr.ui.summery.response.SetDonationResponse
import com.net.pvr.ui.summery.response.SummeryResponse
import com.net.pvr.ui.ticketConfirmation.response.TicketBookedResponse
import com.net.pvr.ui.watchList.response.WatchListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    @POST("api/v2/user/login")
    suspend fun loginMobile(
        @Query("mobile") mobile: String,
        @Query("city") city: String,
        @Query("cname") cName: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<LoginResponse>

    @POST("api/contactus")
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

    @POST("api/v2/user/verify")
    suspend fun otpVerify(
        @Query("mobile") mobile: String,
        @Header("X-Token") token: String,
        @Query("cname") cName: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ResisterResponse>

    @POST("api/loyalty/vouchers")
    suspend fun getCoupons(
        @Header("X-Token") hash: String,
        @Query("mobile") mobile: String,
        @Query("city") city: String,
        @Query("userid") userid: String,
        @Query("timestamp") timestamp: String,
        @Query("status") status: String,
        @Query("pay") pay: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<CouponResponse>

    @POST("api/payment/loyaltypromo")
    suspend fun loyaltyPromo(
        @Query("promocode") promocode: String,
        @Query("userid") userid: String,
        @Query("booktype") booktype: String,
        @Query("bookingid") bookingid: String,
        @Query("transid") transid: String,
        @Query("loyalitytype") loyalitytype: String,
        @Query("unlimitedvoucher") unlimitedvoucher: String,
        @Query("voucheramt") voucheramt: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<LoyaltyVocherApply>

    @POST("api/payment/paytmex/hmac")
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


    @POST("api/payment/paytmex/nbbanks")
    suspend fun bankList(
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<BankListResponse>

    // Recurring HMAC
    @POST("api/payment/recurring/passport/hmac")
    suspend fun recurringPaytmHMAC(
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

    @POST("api/trans/getpaymode")
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

    @POST("api/v2/user/register")
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

    @POST("api/content/comingsoon/v2")
    suspend fun comingSoon(
        @Query("city") city: String,
        @Query("genre") genre: String,
        @Query("lang") lang: String,
        @Query("userid") userid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CommingSoonResponse>

    @POST("api/payment/e-voucher/get/my-voucher")
    suspend fun myVoucher(
        @Query("userid") userid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<VoucherListResponse>

    @POST("api/content/alltheater")
    suspend fun cinema(
        @Query("city") city: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("userid") userid: String,
        @Query("searchtxt") searchTxt: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CinemaResponse>

    @POST("api/user/setprefrenences")
    suspend fun cinemaPreference(
        @Query("userid") userid: String,
        @Query("id") id: String,
        @Query("is_like") is_like: Boolean,
        @Query("type") type: String,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CinemaPreferenceResponse>

    @POST("api/history/giftcard")
    suspend fun giftCard(
        @Query("userid") userid: String,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<GiftCardResponse>

    @POST("api/history/history-new/myticket/v2")
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

    @POST("resendconfirm")
    suspend fun resendMail(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("type") tpye: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
    ): Response<FoodTicketResponse>

    @POST("api/content/homepage/offer/discovery")
    suspend fun offer(
        @Query("city") city: String,
        @Query("userid") userid: String,
        @Query("did") did: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
    ): Response<OfferResponse>

    @POST("api/user/editprofile")
    suspend fun editProfile(
        @Query("userid") userid: String,
        @Query("email") email: String,
        @Query("mobile") mobile: String,
        @Query("name") name: String,
        @Query("dob") dob: String,
        @Query("gender") gender: String,
        @Query("mstatus") mstatus: String,
        @Query("doa") doa: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
    ): Response<ProfileResponse>

    @POST("api/deals/moffers")
    suspend fun mOfferList(
        @Query("did") did: String,
        @Query("city") city: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
    ): Response<MOfferResponse>

    @POST("api/deals/mdetail")
    suspend fun offerDetails(
        @Query("id") id: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String,
    ): Response<OfferDetailsResponse>

    @POST("api/content/cities")
    suspend fun selectCity(
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("userid") userid: String,
        @Query("isSpi") isSpi: String,
        @Query("srilanka") srilanka: String
    ): Response<SelectCityResponse>

    @POST("api/content/nowshowingnew2")
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

    @POST("api/v2/user/optin/check")
    suspend fun whatsappOpt(
        @Query("userid") userid: String,
        @Query("mobile") mobile: String,
        @Query("timestamp") timestamp: String,
        @Header("X-Token") token: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<WhatsAppOptStatus>

    @POST("api/v2/user/optin")
    suspend fun whatsappOptIn(
        @Query("userid") userid: String,
        @Query("mobile") mobile: String,
        @Query("timestamp") timestamp: String,
        @Header("X-Token") token: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<WhatsAppOptStatus>

    @POST("api/v2/user/optout")
    suspend fun whatsappOptOut(
        @Query("userid") userid: String,
        @Query("mobile") mobile: String,
        @Query("timestamp") timestamp: String,
        @Header("X-Token") token: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<WhatsAppOptStatus>

    @POST("api/user/profile")
    suspend fun userProfile(
        @Query("city") city: String,
        @Query("userid") userid: String,
        @Query("timestamp") timestamp: String,
        @Header("X-Token") token: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
    ): Response<ProfileResponse>

    @POST("api/user/getprefrenences")
    suspend fun preference(
        @Query("city") city: String,
        @Query("userid") userid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PreferenceResponse>

    @POST("api/content/getccode")
    suspend fun getCode(
        @Query("cid") cid: String,
        @Query("cinetypeQR") cinetypeQR: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PreferenceResponse>

    @POST("api/content/checkuserlocation")
    suspend fun userLocation(
        @Query("userid") userid: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("city") city: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PreferenceResponse>

    @POST("api/food/getfoods-outlet")
    suspend fun foodOutlet(
        @Query("userid") userid: String,
        @Query("ccode") ccode: String,
        @Query("bookingid") bookingid: String,
        @Query("booking_id") booking_id: String,
        @Query("cbookid") cbookid: String,
        @Query("type") type: String,
        @Query("transid") transid: String,
        @Query("audi") audi: String,
        @Query("seat") seat: String,
        @Query("qr") qr: String,
        @Query("iserv") iserv: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<GetFoodResponse>

    /************************ Privilege ************************/

    @POST("api/loyalty/home")
    suspend fun privilegeHome(
        @Query("mobile") mobile: String,
        @Query("city") city: String,
        @Query("av") av: String,
        @Query("pt") pt: String
    ): Response<PrivilegeHomeResponse>


    @POST("api/loyalty/data")
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


    @POST("api/offervoucher/loyalty-subscription/v2/plans")
    suspend fun passportPlans(
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("av") av: String,
        @Query("pt") pt: String,
        @Query("did") did: String
    ): Response<PassportPlanResponse>


    // Passport Save
    @POST("api/offervoucher/loyalty-subscription/v2/save")
    suspend fun savePassport(
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("fname") fname: String,
        @Query("lname") lname: String,
        @Query("subsplan") subsplan: String,
        @Query("dob") dob: String,
        @Query("gender") gender: String,
        @Query("scheme") scheme: String,
        @Query("price") price: String,
        @Query("mobile") mobile: String,
        @Query("email") email: String,
        @Query("av") av: String,
        @Query("pt") pt: String,
        @Query("did") did: String
    ): Response<PassportPlanResponse>

    @POST("api/offervoucher/loyalty-subscription/genrate-new-order")
    suspend fun genrateNewOrder(
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("scheme") scheme: String,
        @Query("bookingid") bookingid: String,
        @Query("retrycount") retrycount: String,
        @Query("reason") reason: String,
        @Query("av") av: String,
        @Query("pt") pt: String,
        @Query("did") did: String
    ): Response<PassportPlanResponse>


    @POST("api/history/nextbooking")
    suspend fun nextBooking(
        @Query("userid") userid: String,
        @Query("did") did: String,
        @Query("av") av: String,
        @Query("pt") pt: String
    ): Response<NextBookingResponse>

    @POST("api/content/search")
    suspend fun homeSearch(
        @Query("city") city: String,
        @Query("text") text: String,
        @Query("searchFilter") searchFilter: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<HomeSearchResponse>

    @POST("api/content/getmovie")
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

    @POST("api/content/getmovie")
    suspend fun commingSoon(
        @Query("type") type: String,
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("mid") mcode: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<MovieDetailsResponse>

    @POST("api/v1/movie-alert/get-alert")
    suspend fun movieAlert(
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("mcode") mcode: String,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<MovieDetailsResponse>

    @POST("api/content/csessionsfilters")
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

    @POST("api/content/msessionsnewfilters")
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
        @Query("userid") userid: String,
        @Query("lang") lang: String,
        @Query("format") format: String,
        @Query("price") price: String,
        @Query("hc") hc: String,
        @Query("time") time: String,
        @Query("cinetype") cinetype: String,
        @Query("special") special: String
    ): Response<BookingResponse>

    @POST("api/content/movietheater")
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

    @POST("api/content/nearcinetheater")
    suspend fun nearTheatre(
        @Query("city") city: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String?,
        @Query("cid") cid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CinemaNearTheaterResponse>

    @POST("api/content/cinedetail/{id}")
    suspend fun cinemaDetails(
        @Path("id") cid: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String?,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CinemaDetailsResponse>

    @POST("api/trans/getseatlayoutnew/{cinemacode}/{sessionid}")
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

    @POST("api/trans/reserveseats")
    suspend fun reserveSeatLayout(
        @Query("reserve") reserve: String,
        @Query("qr") qr: String,
        @Query("srilanka") srilanka: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ReserveSeatResponse>

    @POST("api/trans/initTrans/{cinemacode}/{sessionid}")
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

    @POST("api/v2/food/getfoods")
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

    @POST("canceltrans")
    suspend fun cancelTrans(
        @Query("cinemacode") cinemacode: String,
        @Query("transid") transid: String,
        @Query("bookingid") bookingid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CancelTransResponse>

    @POST("api/food/getfoods")
    suspend fun oldFood(
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
    ): Response<OldFoodResponse>

    @POST("api/trans/tckdetails")
    suspend fun summery(
        @Query("transid") transid: String,
        @Query("cinemacode") cinemacode: String,
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("qr") qr: String,
        @Query("srilanka") srilanka: String,
        @Query("infosys") infosys: String,
        @Query("isSpi") isSpi: String,
        @Query("doreq") doreq: String,
        @Query("change") change: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<SummeryResponse>


    @POST("api/food/fooddetails")
    suspend fun foodSummary(
        @Query("pktransid") pktransid: String,
        @Query("userid") userid: String,
        @Query("qr") qr: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<SummeryResponse>


    @POST("api/trans/ticketBooked")
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

    @POST("api/content/specialcine")
    suspend fun formats(
        @Query("type") type: String,
        @Query("city") city: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<FormatResponse>

    @POST("api/content/formats")
    suspend fun experience(
        @Query("city") city: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ExperienceResponse>

  @POST("api/content/getformat")
    suspend fun experienceDetails(
        @Query("city") city: String,
        @Query("type") type: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ExperienceDetailsResponse>

    @POST("api/trans/setfood")
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


    @POST("api/trans/setdonation")
    suspend fun setDonation(
        @Query("bookingid") bookingid: String,
        @Query("transid") transid: String,
        @Query("isDonate") isDonate: Boolean,
        @Query("istDonate") istDonate: Boolean,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<SetDonationResponse>

    @POST("api/content/splashtxt")
    suspend fun splash(
        @Query("city") city: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<SplashResponse>

    @POST("api/v1/movie-alert/get-all-alert")
    suspend fun watchList(
        @Query("userid") userid: String,
        @Query("city") city: String,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<WatchListResponse>

    @POST("api/v1/movie-alert/delete-alert")
    suspend fun deleteAlert(
        @Query("userid") userid: String,
        @Query("mcode") mcode: String,
        @Query("city") city: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<DeleteAlertResponse>

    @POST("api/food/savefoods")
    suspend fun addFood(
        @Query("userid") userid: String,
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
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<AddFoodResponse>


    @POST("api/content/alltheater")
    suspend fun bookingRetrieval(
        @Query("city") city: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("userid") userid: String,
        @Query("searchtxt") searchtxt: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<BookingRetrievalResponse>

    @POST("api/v1/movie-alert/set-alert")
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

    @POST("api/payment/paytmex/upistatus")
    suspend fun upiStatus(
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<UPIStatusResponse>


    //PhonpePe Api Call
    @POST("api/payment/phonepe/signature")
    suspend fun phonepeHmac(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PhonepeHmacResponse>

    @POST("api/payment/phonepe/paydone")
    suspend fun phonepeStatus(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<UPIStatusResponse>


    /**    Paytm Wallet Api        **/

    @POST("api/payment/paytm/balance")
    suspend fun paytmWalletHmac(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    @POST("api/payment/paytm/resend")
    suspend fun paytmWalletSendOTP(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("mobile") mobile: String,
        @Query("spi") spi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    @POST("api/payment/paytm/otpvalidate")
    suspend fun paytmWalletVerifYOTP(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("otp") otp: String,
        @Query("state") state_text: String,
        @Query("spi") spi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    @POST("api/payment/paytm/withdraw")
    suspend fun paytmWalletMakePayment(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    @POST("api/payment/paytm/hmac")
    suspend fun walletPaidPay(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("unpaid") unpaid: String,
        @Query("isSpi") spi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

/*************     PAYTM PostPaid    *****************/

    @POST("api/payment/paytmpp/hmac")
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


    @POST("api/payment/paytmpp/pay")
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

    @POST("api/payment/paytmpp/sendotp")
    suspend fun postPaidSendOTP(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("spi") spi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    @POST("api/payment/paytmpp/verifyotp")
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


    @POST("api/payment/paytm/withdraw")
    suspend fun postPaidMakePayment(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    /***   AIRTEL PAY      *****/

    @POST("api/payment/airtel/airtelhmac")
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

    @POST("api/payment/promocode")
    suspend fun promoCode(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("promocode") promocode: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>


    @POST("api/payment/pcrollback")
    suspend fun removePromoCode(
        @Query("mobile") mobile: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("av") version: String,
        @Query("pt") pt: String,
        @Query("did") did: String
    ): Response<PaytmHmacResponse>


    /***   GYFTER CODE       *****/

    @POST("api/payment/gyft")
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

    @POST("api/payment/zaggle/pay")
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

    @POST("api/payment/qccard")
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

    @POST("api/payment/accentive")
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

    @POST("api/payment/starpass")
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

    @POST("api/payment/mcoupon")
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

    @POST("api/payment/hyatt/otp")
    suspend fun sendOTPHYATT(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("mobile") mobile: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<PaytmHmacResponse>

    @POST("api/payment/hyatt")
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


    @POST("api/payment/cred/check")
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


    @POST("api/payment/cred/hmac")
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


    @POST("api/payment/cred/status")
    suspend fun credStatus(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<UPIStatusResponse>


    /*******************      Passport Recurring     *****************/

    @POST("api/payment/recurring/passport/init")
    suspend fun recurringInit(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<RecurringInitResponse>

    @POST("api/payment/recurring/passport/bin/check")
    suspend fun recurringBinCheck(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("token") token: String,
        @Query("bin") bin: String,
        @Query("vpa") vpa: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<RecurringInitResponse>

    // promo list

    @POST("api/deals/list")
    suspend fun getPromoList(
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<PromoCodeList>

    /*************      PARKING       ***************/
    @POST("api/history/parking")
    suspend fun bookParking(
        @Query("bookingid") bookingid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<ParkingResponse>

    @POST("api/history/showparking")
    suspend fun viewParking(
        @Query("bookingid") bookingid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<ParkingResponse>

    /***********         Ticket From History     ***************/

    @POST("api/history/foodstatus")
    suspend fun fnbTicket(
        @Query("bookid") bookingid: String,
        @Query("userid") userid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<TicketBookedResponse>


    @POST("api/history/myticketsingle")
    suspend fun singleTicket(
        @Query("bookingid") bookingid: String,
        @Query("userid") userid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<TicketBookedResponse>

    /*****************   GIFT CARD         ********************/

    @POST("getgiftcard1")
    suspend fun giftCardMain(
        @Query("sWidth") sWidth: String,
        @Query("platform") platform: String,
        @Query("infosys") infosys: String,
        @Query("av") version: String,
        @Query("pt") platform1: String
    ): Response<GiftCardListResponse>

    @POST("api/history/giftcard/list")
    suspend fun activeGiftCard(
        @Query("userid") userid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String,
    ): Response<ActiveGCResponse>

    @POST("api/history/giftcard/reedem/history")
    suspend fun getDetailGiftCard(
        @Query("userid") userid: String,
        @Query("giftcardid") giftcardid: String,
        @Query("pin") pin: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String,
    ): Response<ActiveGCResponse>

    @POST("api/giftcard/savegiftcard")
    suspend fun saveGiftCard(
        @Query("rName") rName: String,
        @Query("rEmail") rEmail: String,
        @Query("rMobile") rMobile: String,
        @Query("gc_channel") gc_channel: String,
        @Query("gtype") gtype: String,
        @Query("pkGiftId") pkGiftId: String,
        @Query("pincode") pincode: String,
        @Query("personalMessage") personalMessage: String,
        @Query("delAddress") delAddress: String,
        @Query("denomination") denomination: String,
        @Query("quantity") quantity: String,
        @Query("userEmail") userEmail: String,
        @Query("ifSelf") ifSelf: String,
        @Query("totalAmount") totalAmount: String,
        @Query("customImage") customImage: String,
        @Query("customName") customName: String,
        @Query("infosys") infosys: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String,
    ): Response<UploadImageGC>

    @Multipart
    @POST("api/giftcard/save/image")
    suspend fun uploadGCImage(
        @Part fileImage: MultipartBody.Part,
        @Part("name") name: RequestBody
    ): Response<UploadImageGC>


    @POST("api/giftcard/purchasedetails")
    suspend fun giftcardDetails(
        @Query("gcorderid") gcorderid: String,
        @Query("userid") userid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<GiftCardDetailResponse>

    @POST("api/trans/extendtrans")
    suspend fun extendTime(
        @Query("transid") transid: String,
        @Query("bookingid") bookingid: String,
        @Query("cinemacode") cinemacode: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ExtendTimeResponse>

    /*********   MOBIKWIK API *****************/

    // mobikwik otp
    @POST("api/payment/mobikwik/otp")
    suspend fun mobikwikOTP(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("mobile") mobile: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<MobikwikOTPResponse>

    // mobikwik pay
    @POST("api/payment/mobikwik/pay")
    suspend fun mobikwikPAY(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("mobile") mobile: String,
        @Query("otp") otp: String,
        @Query("cinemacode") cinemacode: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<MobiKwikPayResponse>

    // mobikwik checksum
    @POST("api/payment/mobikwik/checksum")
    suspend fun mobikwikChecksum(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("mobile") mobile: String,
        @Query("otp") otp: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<MobiKwikCheckSumResponse>

    // mobikwik Create Wallet
    @POST("api/payment/mobikwik/createwallet")
    suspend fun mobikwikCreateWallet(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("mobile") mobile: String,
        @Query("otp") otp: String,
        @Query("email") email: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<MobiKwikPayResponse>

    /****************** BANK OFFER ****************/
    @POST("api/payment/bin-offer/discount")
    suspend fun bankOffer(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("booktype") booktype: String,
        @Query("transid") transid: String,
        @Query("schemeid") schemeid: String,
        @Query("cardNo") cardNo: String,
        @Query("bindiscount") bindiscount: String,
        @Query("ptype") ptype: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<PaytmHmacResponse>

    /****************** FEEDBACK FOR MOVIE AND CINEMA ****************/

    @POST("api/common/feedback/get")
    suspend fun getFeedBackData(
        @Query("userid") userid: String,
        @Query("type") type: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<FeedbackDataResponse>

    @POST("api/common/feedback/set")
    suspend fun setFeedBackData(
        @Query("userid") userid: String,
        @Query("type") type: String,
        @Query("code") code: String,
        @Query("text") text: String,
        @Query("tags") tags: String,
        @Query("comments") comments: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<FeedbackDataResponse>


    /****************** FREECHARGE PAY ****************/

    @POST("api/payment/freecharge/otp")
    suspend fun freechargeOTP(
        @Query("userid") userid: String,
        @Query("transid") transid: String,
        @Query("booktype") booktype: String,
        @Query("bookingid") bookingid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<PaytmHmacResponse>

    @POST("api/payment/freecharge/otp/login")
    suspend fun freechargeLogin(
        @Query("userid") userid: String,
        @Query("transid") transid: String,
        @Query("booktype") booktype: String,
        @Query("bookingid") bookingid: String,
        @Query("otp") otp: String,
        @Query("otpId") otpId: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<PaytmHmacResponse>

    @POST("api/payment/freecharge/otp/resend")
    suspend fun freechargeResend(
        @Query("userid") userid: String,
        @Query("transid") transid: String,
        @Query("booktype") booktype: String,
        @Query("bookingid") bookingid: String,
        @Query("otpId") otpId: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<PaytmHmacResponse>

    @POST("api/payment/freecharge/user/detail")
    suspend fun freechargeDetail(
        @Query("userid") userid: String,
        @Query("transid") transid: String,
        @Query("booktype") booktype: String,
        @Query("bookingid") bookingid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<PaytmHmacResponse>


    @POST("api/payment/freecharge/wallet/payment")
    suspend fun freechargePayment(
        @Query("userid") userid: String,
        @Query("transid") transid: String,
        @Query("booktype") booktype: String,
        @Query("bookingid") bookingid: String,
        @Query("unpaid") unpaid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<PaytmHmacResponse>

    @POST("api/payment/freecharge/add/money")
    suspend fun freechargeAddMoney(
        @Query("userid") userid: String,
        @Query("transid") transid: String,
        @Query("booktype") booktype: String,
        @Query("bookingid") bookingid: String,
        @Query("amount") amount: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<PaytmHmacResponse>



/****************** GO DIGITAL ****************/
    @POST("api/content/create/promo")
    suspend fun augOffer(
        @Query("userid") userid: String,
        @Query("lng") lng: String,
        @Query("lat") lat: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<PaytmHmacResponse>

/****************** JUS PAY ****************/
    @POST("api/trans/extendtrans")
    suspend fun initJusPay(
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did") did: String
    ): Response<JusPayInitResponse>





}