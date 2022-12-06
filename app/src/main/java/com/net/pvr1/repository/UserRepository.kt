package com.net.pvr1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.net.pvr1.api.UserAPI
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
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    //Login
    private val _userResponseLiveData = MutableLiveData<NetworkResult<LoginResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = _userResponseLiveData

    suspend fun loginMobile(mobile: String, city: String, cName: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.loginMobile(mobile, city, cName, Constant.version, Constant.platform)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //ContactUs
    private val contactUsLiveData = MutableLiveData<NetworkResult<ContactUsResponse>>()
    val contactUsResponseLiveData: LiveData<NetworkResult<ContactUsResponse>>
        get() = contactUsLiveData

    suspend fun contactUsUser(
        comment: String,
        email: String,
        mobile: String,
        did: String,
        isSpi: String,
        ctype: String
    ) {
        contactUsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.contactUs(
            comment,
            email,
            mobile,
            did,
            isSpi,
            ctype,
            Constant.version,
            Constant.platform
        )
        contactUs(response)
    }

    private fun contactUs(response: Response<ContactUsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            contactUsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            contactUsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            contactUsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Otp Verify
    private val otpVerifyLiveData = MutableLiveData<NetworkResult<ResisterResponse>>()
    val otpVerifyResponseLiveData: LiveData<NetworkResult<ResisterResponse>>
        get() = otpVerifyLiveData

    suspend fun otpVerify(mobile: String, token: String) {
        otpVerifyLiveData.postValue(NetworkResult.Loading())
        val response =
            userAPI.otpVerify(mobile, token, "INDIA", Constant.version, Constant.platform)
        verifyResponse(response)
    }

    private fun verifyResponse(response: Response<ResisterResponse>) {
        if (response.isSuccessful && response.body() != null) {
            otpVerifyLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            otpVerifyLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            otpVerifyLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //getcoupons
    private val couponsLiveData = MutableLiveData<NetworkResult<CouponResponse>>()
    val couponsResponseLiveData: LiveData<NetworkResult<CouponResponse>>
        get() = couponsLiveData

    suspend fun coupons(mobile: String, city: String, status: String, pay: Boolean, did: String) {
        otpVerifyLiveData.postValue(NetworkResult.Loading())
        val response =
            userAPI.getCoupons(mobile, city, status, pay, did, Constant.version, Constant.platform)
        couponsResponse(response)
    }


    private fun couponsResponse(response: Response<CouponResponse>) {
        if (response.isSuccessful && response.body() != null) {
            couponsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            couponsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            couponsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //paytm HMAC
    private val paytmHmacLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val paytmHmaResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = paytmHmacLiveData

    suspend fun paytmHMAC(
        userid: String,
        bookingid: String,
        transid: String,
        unpaid: Boolean,
        cardNo: String,
        booktype: String,
        ptype: String,
        isSpi: String,
        binOffer: String
    ) {
        paytmHmacLiveData.postValue(NetworkResult.Loading())
        val response =
            userAPI.paytmHMAC(userid,bookingid,transid,unpaid,cardNo,booktype,ptype,isSpi,binOffer,Constant.version, Constant.platform)
        paytmHmacResponse(response)
    }

    private fun paytmHmacResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            paytmHmacLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            paytmHmacLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            paytmHmacLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //payMode
    private val payModeLiveData = MutableLiveData<NetworkResult<PaymentResponse>>()
    val payModeResponseLiveData: LiveData<NetworkResult<PaymentResponse>>
        get() = payModeLiveData

    suspend fun payMode(
        cinemacode: String,
        booktype: String,
        userid: String,
        mobile: String,
        type: String,
        isSpi: String,
        srilanka: String,
        unpaid: Boolean
    ) {
        payModeLiveData.postValue(NetworkResult.Loading())
        val response =
            userAPI.payMode(
                cinemacode,
                booktype,
                userid,
                mobile,
                type,
                isSpi,
                srilanka,
                unpaid,
                Constant.version,
                Constant.platform
            )
        payModeResponse(response)
    }

    private fun payModeResponse(response: Response<PaymentResponse>) {
        if (response.isSuccessful && response.body() != null) {
            payModeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            payModeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            payModeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Resister
    private val resisterLiveData = MutableLiveData<NetworkResult<ResisterResponse>>()
    val resisterResponseLiveData: LiveData<NetworkResult<ResisterResponse>>
        get() = resisterLiveData

    suspend fun resister(
        hash: String,
        email: String,
        name: String,
        mobile: String,
        otp: String,
        city: String,
        cname: Boolean
    ) {
        otpVerifyLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.resister(
            hash,
            email,
            name,
            mobile,
            otp,
            city,
            cname,
            Constant.version,
            Constant.platform
        )
        resisterResponse(response)
    }

    private fun resisterResponse(response: Response<ResisterResponse>) {
        if (response.isSuccessful && response.body() != null) {
            resisterLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            resisterLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            resisterLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //ComingSoon
    private val comingSoonLiveData = MutableLiveData<NetworkResult<CommingSoonResponse>>()
    val comingSoonResponseLiveData: LiveData<NetworkResult<CommingSoonResponse>>
        get() = comingSoonLiveData

    suspend fun comingSoon(city: String, genre: String, lang: String, userid: String) {
        comingSoonLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.comingSoon(
            city, genre, lang, userid, Constant.version,
            Constant.platform
        )
        comingSoonResponse(response)
    }

    private fun comingSoonResponse(response: Response<CommingSoonResponse>) {
        if (response.isSuccessful && response.body() != null) {
            comingSoonLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            comingSoonLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            comingSoonLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Cinema
    private val cinemaLiveData = MutableLiveData<NetworkResult<CinemaResponse>>()
    val cinemaResponseLiveData: LiveData<NetworkResult<CinemaResponse>>
        get() = cinemaLiveData

    suspend fun cinema(city: String, lat: String, lng: String, userid: String, searchTxt: String) {
        cinemaLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.cinema(
            city, lat, lng, userid, searchTxt, Constant.version,
            Constant.platform
        )
        cinemaResponse(response)
    }

    private fun cinemaResponse(response: Response<CinemaResponse>) {
        if (response.isSuccessful && response.body() != null) {
            cinemaLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            cinemaLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            cinemaLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //cinemaPreference
    private val cinemaPreferenceLiveData = MutableLiveData<NetworkResult<PreferenceResponse>>()
    val cinemaPreferenceResponseLiveData: LiveData<NetworkResult<PreferenceResponse>>
        get() = cinemaPreferenceLiveData

    suspend fun cinemaPreference(
        userid: String,
        id: String,
        is_like: Boolean,
        type: String,
        did: String
    ) {
        cinemaPreferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.cinemaPreference(
            userid, id, is_like, type, did, Constant.version, Constant.platform
        )
        cinemaPreferenceResponse(response)
    }

    private fun cinemaPreferenceResponse(response: Response<PreferenceResponse>) {
        if (response.isSuccessful && response.body() != null) {
            cinemaPreferenceLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            cinemaPreferenceLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            cinemaPreferenceLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //GiftCard
    private val giftCardLiveData = MutableLiveData<NetworkResult<GiftCardResponse>>()
    val giftCardResponseLiveData: LiveData<NetworkResult<GiftCardResponse>>
        get() = giftCardLiveData

    suspend fun giftCard(userId: String, did: String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.giftCard(
            userId, did, Constant.version,
            Constant.platform
        )
        giftCardResponse(response)
    }

    private fun giftCardResponse(response: Response<GiftCardResponse>) {
        if (response.isSuccessful && response.body() != null) {
            giftCardLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            giftCardLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            giftCardLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //FoodTicket
    private val foodTicketLiveData = MutableLiveData<NetworkResult<FoodTicketResponse>>()
    val foodTicketResponseLiveData: LiveData<NetworkResult<FoodTicketResponse>>
        get() = foodTicketLiveData

    suspend fun foodTicket(
        userId: String,
        did: String,
        sriLanka: String,
        city: String,
        isSpi: String,
        past: String
    ) {
        foodTicketLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.foodTicket(
            userId, did, sriLanka, city, isSpi, past, Constant.version,
            Constant.platform
        )
        foodTicketResponse(response)
    }

    private fun foodTicketResponse(response: Response<FoodTicketResponse>) {
        if (response.isSuccessful && response.body() != null) {
            foodTicketLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            foodTicketLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            foodTicketLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Offer
    private val offerLiveData = MutableLiveData<NetworkResult<OfferResponse>>()
    val offerResponseLiveData: LiveData<NetworkResult<OfferResponse>>
        get() = offerLiveData

    suspend fun offer(did: String) {
        offerLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.offer(did, Constant.version, Constant.platform)
        offerResponse(response)
    }

    private fun offerResponse(response: Response<OfferResponse>) {
        if (response.isSuccessful && response.body() != null) {
            offerLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            offerLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            offerLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Offer List
    private val mOfferListLiveData = MutableLiveData<NetworkResult<MOfferResponse>>()
    val mOfferListResponseLiveData: LiveData<NetworkResult<MOfferResponse>>
        get() = mOfferListLiveData

    suspend fun mOfferList(did: String, city: String) {
        mOfferListLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.mOfferList(did, city, Constant.version, Constant.platform)
        mOfferListResponse(response)
    }

    private fun mOfferListResponse(response: Response<MOfferResponse>) {
        if (response.isSuccessful && response.body() != null) {
            mOfferListLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            mOfferListLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            mOfferListLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Offer Details
    private val offerDetailsLiveData = MutableLiveData<NetworkResult<OfferResponse>>()
    val offerDetailsResponseLiveData: LiveData<NetworkResult<OfferResponse>>
        get() = offerDetailsLiveData

    suspend fun offerDetails(id: String, did: String) {
        offerDetailsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.offerDetails(id, Constant.version, Constant.platform, did)
        offerDetailsResponse(response)
    }

    private fun offerDetailsResponse(response: Response<OfferResponse>) {
        if (response.isSuccessful && response.body() != null) {
            offerDetailsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            offerDetailsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            offerDetailsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // Select City
    private val selectCityLiveData = MutableLiveData<NetworkResult<SelectCityResponse>>()
    val citiesResponseLiveData: LiveData<NetworkResult<SelectCityResponse>>
        get() = selectCityLiveData

    suspend fun selectCity(
        lat: String,
        lng: String,
        userid: String,
        isSpi: String,
        srilanka: String
    ) {
        selectCityLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.selectCity(
            lat,
            lng,
            Constant.version,
            Constant.platform,
            userid,
            isSpi,
            srilanka
        )
        selectCityResponse(response)
    }

    private fun selectCityResponse(response: Response<SelectCityResponse>) {
        if (response.isSuccessful && response.body() != null) {
            selectCityLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            selectCityLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            selectCityLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // Home Data
    private val homeLiveData = MutableLiveData<NetworkResult<HomeResponse>>()
    val homeResponseLiveData: LiveData<NetworkResult<HomeResponse>>
        get() = homeLiveData

    suspend fun homeData(
        city: String,
        dtmsource: String,
        userid: String,
        mobile: String,
        upbooking: Boolean,
        srilanka: String,
        type: String,
        lng: String,
        gener: String,
        spShow: String,
        isSpi: String
    ) {
        selectCityLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.home(
            city, Constant.version, Constant.platform, dtmsource, userid, mobile,
            upbooking, srilanka, type, lng, gener, spShow, isSpi
        )
        homeResponse(response)
    }

    private fun homeResponse(response: Response<HomeResponse>) {
        if (response.isSuccessful && response.body() != null) {
            homeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            homeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            homeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // Privilege Home
    private val privilegeHomeLiveData = MutableLiveData<NetworkResult<PrivilegeHomeResponse>>()
    val privilegeHomeResponseLiveData: LiveData<NetworkResult<PrivilegeHomeResponse>>
        get() = privilegeHomeLiveData

    suspend fun privilegeHomeData(mobile: String, city: String) {
        privilegeHomeLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.privilegeHome(
            mobile,
            city, Constant.version, Constant.platform
        )
        privilegeHomeResponse(response)
    }

    private fun privilegeHomeResponse(response: Response<PrivilegeHomeResponse>) {
        if (response.isSuccessful && response.body() != null) {
            privilegeHomeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            privilegeHomeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            privilegeHomeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Search
    private val homeSearchLiveData = MutableLiveData<NetworkResult<HomeSearchResponse>>()
    val searchResponseLiveData: LiveData<NetworkResult<HomeSearchResponse>>
        get() = homeSearchLiveData

    suspend fun homeSearchData(
        city: String,
        text: String,
        searchFilter: String,
        lat: String,
        lng: String
    ) {
        homeSearchLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.homeSearch(
            city,
            text,
            searchFilter,
            lat,
            lng,
            Constant.version,
            Constant.platform
        )
        homeSearchResponse(response)
    }

    private fun homeSearchResponse(response: Response<HomeSearchResponse>) {
        if (response.isSuccessful && response.body() != null) {
            homeSearchLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            homeSearchLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            homeSearchLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //MovieDetails
    private val movieDetailsLiveData = MutableLiveData<NetworkResult<MovieDetailsResponse>>()
    val movieDetailsResponseLiveData: LiveData<NetworkResult<MovieDetailsResponse>>
        get() = movieDetailsLiveData

    suspend fun movieDetailsData(
        city: String,
        mid: String,
        type: String,
        userid: String,
        lat: String,
        lng: String,
        isSpi: String,
        srilanka: String
    ) {
        movieDetailsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.movieDetails(
            city,
            mid,
            Constant.version,
            type,
            Constant.platform,
            userid,
            lat,
            lng,
            isSpi,
            srilanka
        )
        movieDetailsResponse(response)
    }

    private fun movieDetailsResponse(response: Response<MovieDetailsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            movieDetailsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            movieDetailsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            movieDetailsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //CommingSoon
    private val commingSoonLiveData = MutableLiveData<NetworkResult<MovieDetailsResponse>>()
    val commingSoonResponseLiveData: LiveData<NetworkResult<MovieDetailsResponse>>
        get() = commingSoonLiveData

    suspend fun commingSoonData(
        type: String,
        userid: String,
        city: String,
        mcode: String
    ) {
        movieDetailsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.commingSoon(
            type,
            userid,
            city,
            mcode,
            Constant.version,
            Constant.platform
        )
        commingSoonResponse(response)
    }

    private fun commingSoonResponse(response: Response<MovieDetailsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            commingSoonLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            commingSoonLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            commingSoonLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Movie Alert
    private val movieAlertLiveData = MutableLiveData<NetworkResult<MovieDetailsResponse>>()
    val movieAlertResponseLiveData: LiveData<NetworkResult<MovieDetailsResponse>>
        get() = movieAlertLiveData

    suspend fun movieAlertData(
        userid: String,
        city: String,
        mcode: String,
        did: String
    ) {
        movieAlertLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.movieAlert(
            userid,
            city,
            mcode,
            did,
            Constant.version,
            Constant.platform
        )
        movieAlertResponse(response)
    }

    private fun movieAlertResponse(response: Response<MovieDetailsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            movieAlertLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            movieAlertLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            movieAlertLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //CinemaSession
    private val cinemaSessionLiveData = MutableLiveData<NetworkResult<CinemaSessionResponse>>()
    val cinemaSessionResponseLiveData: LiveData<NetworkResult<CinemaSessionResponse>>
        get() = cinemaSessionLiveData

    suspend fun cinemaSessionData(
        city: String,
        cid: String,
        lat: String,
        lng: String,
        userid: String,
        date: String,
        lang: String,
        format: String,
        price: String,
        time: String,
        hc: String,
        cc: String,
        ad: String,
        qr: String,
        cinetype: String,
        cinetypeQR: String
    ) {
        cinemaSessionLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.cinemaSession(
            city,
            cid,
            lat,
            lng,
            userid,
            date,
            Constant.version,
            Constant.platform,
            lang,
            format,
            price,
            time,
            hc,
            cc,
            ad,
            qr,
            cinetype,
            cinetypeQR
        )
        cinemaSessionResponse(response)
    }

    private fun cinemaSessionResponse(response: Response<CinemaSessionResponse>) {
        if (response.isSuccessful && response.body() != null) {
            cinemaSessionLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            cinemaSessionLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            cinemaSessionLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //BookingTicket
    private val bookingSessionLiveData = MutableLiveData<NetworkResult<BookingResponse>>()
    val bookingSessionResponseLiveData: LiveData<NetworkResult<BookingResponse>>
        get() = bookingSessionLiveData

    suspend fun bookingTicket(
        city: String,
        mid: String,
        lat: String,
        lng: String,
        date: String,
        isSpi: String,
        srilanka: String,
        userid: String
    ) {
        cinemaSessionLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.bookingSession(
            city, mid, lat, lng, date, Constant.version, Constant.platform, isSpi, srilanka, userid

        )
        bookingSessionResponse(response)
    }

    private fun bookingSessionResponse(response: Response<BookingResponse>) {
        if (response.isSuccessful && response.body() != null) {
            bookingSessionLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            bookingSessionLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            bookingSessionLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //BookingTheatre
    private val bookingTheatreLiveData = MutableLiveData<NetworkResult<BookingTheatreResponse>>()
    val bookingTheatreResponseLiveData: LiveData<NetworkResult<BookingTheatreResponse>>
        get() = bookingTheatreLiveData

    suspend fun bookingTheatre(
        city: String,
        cid: String,
        userid: String,
        mid: String,
        lng: String,
        isSpi: String
    ) {
        cinemaSessionLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.bookingTheatre(
            city, cid, userid, mid, lng, Constant.version, Constant.platform, isSpi
        )
        bookingTheatreResponse(response)
    }

    private fun bookingTheatreResponse(response: Response<BookingTheatreResponse>) {
        if (response.isSuccessful && response.body() != null) {
            bookingTheatreLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            bookingTheatreLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            bookingTheatreLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //NearbyTheatre
    private val nearTheaterLiveData = MutableLiveData<NetworkResult<CinemaNearTheaterResponse>>()
    val nearTheaterResponseLiveData: LiveData<NetworkResult<CinemaNearTheaterResponse>>
        get() = nearTheaterLiveData

    suspend fun nearTheater(city: String, lat: String, lng: String?, cid: String) {
        nearTheaterLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.nearTheatre(
            city, lat, lng, cid, Constant.version, Constant.platform
        )
        nearTheatreResponse(response)
    }

    private fun nearTheatreResponse(response: Response<CinemaNearTheaterResponse>) {
        if (response.isSuccessful && response.body() != null) {
            nearTheaterLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            nearTheaterLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            nearTheaterLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //reserveSeat
    private val reserveSeatLiveData = MutableLiveData<NetworkResult<ReserveSeatResponse>>()
    val reserveSeatResponseLiveData: LiveData<NetworkResult<ReserveSeatResponse>>
        get() = reserveSeatLiveData

    suspend fun reserveSeatLayout(
        reserve: String,
    ) {
        reserveSeatLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.reserveSeatLayout(
            reserve, "no", "no", "no", Constant.version, Constant.platform
        )
        reserveSeatResponse(response)
    }

    private fun reserveSeatResponse(response: Response<ReserveSeatResponse>) {
        if (response.isSuccessful && response.body() != null) {
            reserveSeatLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            reserveSeatLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            reserveSeatLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //initTrans
    private val initTransSeatLiveData = MutableLiveData<NetworkResult<InitResponse>>()
    val initTransSeatResponseLiveData: LiveData<NetworkResult<InitResponse>>
        get() = initTransSeatLiveData

    suspend fun initTransSeatLayout(
        cinemacode: String,
        sessionid: String
    ) {
        initTransSeatLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.initTransSeatLayout(
            cinemacode,
            sessionid,
            "",
            "",
            "",
            "",
            "",
            "no",
            "no",
            Constant.version,
            Constant.platform
        )
        initTransSeatResponse(response)
    }

    private fun initTransSeatResponse(response: Response<InitResponse>) {
        if (response.isSuccessful && response.body() != null) {
            initTransSeatLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            initTransSeatLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            initTransSeatLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //SeatLayout
    private val seatLiveData = MutableLiveData<NetworkResult<SeatResponse>>()
    val seatResponseLiveData: LiveData<NetworkResult<SeatResponse>>
        get() = seatLiveData

    suspend fun seatLayout(
        cinemacode: String,
        sessionid: String,
        dtmsource: String,
        partnerid: String,
        cdate: String,
        bundle: Boolean,
        isSpi: String
    ) {
        seatLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.seatLayout(
            cinemacode,
            sessionid,
            dtmsource,
            partnerid,
            cdate,
            bundle,
            isSpi,
            Constant.version,
            Constant.platform
        )
        seatLayoutResponse(response)
    }

    private fun seatLayoutResponse(response: Response<SeatResponse>) {
        if (response.isSuccessful && response.body() != null) {
            seatLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            seatLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            seatLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Food
    private val foodLiveData = MutableLiveData<NetworkResult<FoodResponse>>()
    val foodResponseLiveData: LiveData<NetworkResult<FoodResponse>>
        get() = foodLiveData

    suspend fun foodLayout(
        userid: String,
        ccode: String,
        bookingid: String,
        cbookid: String,
        transid: String,
        type: String,
        audi: String,
        seat: String,
        city: String,
        qr: String,
        iserv: String,
        isSpi: String
    ) {
        seatLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.food(
            userid,
            ccode,
            bookingid,
            cbookid,
            transid,
            type,
            audi,
            seat,
            city,
            qr,
            iserv,
            isSpi,
            Constant.version,
            Constant.platform
        )
        foodLayoutResponse(response)
    }

    private fun foodLayoutResponse(response: Response<FoodResponse>) {
        if (response.isSuccessful && response.body() != null) {
            foodLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            foodLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            foodLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Summery
    private val summerLiveData = MutableLiveData<NetworkResult<SummeryResponse>>()
    val summerResponseLiveData: LiveData<NetworkResult<SummeryResponse>>
        get() = summerLiveData

    suspend fun summerLayout(
        transid: String,
        cinemacode: String,
        userid: String,
        bookingid: String
    ) {
        formatsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.summery(
            transid,
            cinemacode,
            userid,
            bookingid,
            "no",
            "no",
            "",
            "NO",
            false,
            "",
            Constant.version,
            Constant.platform
        )
        summeryLayoutResponse(response)
    }

    private fun summeryLayoutResponse(response: Response<SummeryResponse>) {
        if (response.isSuccessful && response.body() != null) {
            summerLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            summerLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            summerLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Ticket Confirmation

    private val ticketConfirmationLiveData = MutableLiveData<NetworkResult<TicketBookedResponse>>()
    val ticketConfirmationResponseLiveData: LiveData<NetworkResult<TicketBookedResponse>>
        get() = ticketConfirmationLiveData

    suspend fun ticketConfirmationLiveDataLayout(
        booktype: String,
        bookingid: String,
        userid: String,
        qr: String,
        srilanka: String,
        infosys: String,
        isSpi: String,
        oldBookingId: String,
        change: String
    ) {
        ticketConfirmationLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.ticketConfirmation(
            booktype,
            bookingid,
            userid,
            qr,
            srilanka,
            infosys,
            isSpi,
            change,
            Constant.version,
            Constant.platform
        )
        ticketConfirmationLiveDataLayoutResponse(response)
    }

    private fun ticketConfirmationLiveDataLayoutResponse(response: Response<TicketBookedResponse>) {
        if (response.isSuccessful && response.body() != null) {
            ticketConfirmationLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            ticketConfirmationLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            ticketConfirmationLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }

    }


    //formats
    private val formatsLiveData = MutableLiveData<NetworkResult<FormatResponse>>()
    val formatsResponseLiveData: LiveData<NetworkResult<FormatResponse>>
        get() = formatsLiveData

    suspend fun formatsLayout(type: String, city: String, isSpi: String) {
        formatsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.formats(
            type,
            city,
            isSpi,
            Constant.version,
            Constant.platform
        )
        formatsLayoutResponse(response)
    }

    private fun formatsLayoutResponse(response: Response<FormatResponse>) {
        if (response.isSuccessful && response.body() != null) {
            formatsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            formatsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            formatsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Seat With Food
    private val seatWithFoodLiveData = MutableLiveData<NetworkResult<SummeryResponse>>()
    val seatWithFoodResponseLiveData: LiveData<NetworkResult<SummeryResponse>>
        get() = seatWithFoodLiveData

    suspend fun seatWithFoodLayout(
        foods: String,
        transid: String,
        cinemacode: String,
        userId: String,
        qr: String,
        infosys: String,
        isSpi: String,
        seat: String,
        audi: String
    ) {
        seatWithFoodLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.ticketWithFood(
            foods,
            transid,
            cinemacode,
            userId,
            qr,
            infosys,
            isSpi,
            Constant.version,
            Constant.platform,
            seat,
            audi
        )
        seatWithFoodLayoutResponse(response)
    }

    private fun seatWithFoodLayoutResponse(response: Response<SummeryResponse>) {
        if (response.isSuccessful && response.body() != null) {
            seatWithFoodLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            seatWithFoodLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            seatWithFoodLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Set Donation
    private val setDonationLiveData = MutableLiveData<NetworkResult<SetDonationResponse>>()
    val setDonationResponseLiveData: LiveData<NetworkResult<SetDonationResponse>>
        get() = setDonationLiveData

    suspend fun setDonationLayout(
        bookingid: String,
        transid: String,
        isDonate: Boolean,
        istDonate: Boolean,
        isSpi: String
    ) {
        setDonationLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.setDonation(
            bookingid,
            transid,
            isDonate,
            istDonate,
            isSpi,
            Constant.version,
            Constant.platform
        )
        setDonationLayoutResponse(response)
    }

    private fun setDonationLayoutResponse(response: Response<SetDonationResponse>) {
        if (response.isSuccessful && response.body() != null) {
            setDonationLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            setDonationLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            setDonationLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Splash
    private val splashLiveData = MutableLiveData<NetworkResult<SplashResponse>>()
    val splashResponseLiveData: LiveData<NetworkResult<SplashResponse>>
        get() = splashLiveData

    suspend fun splashLayout(
        city: String
    ) {
        splashLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.splash(
            city,
            "NO",
            Constant.version,
            Constant.platform
        )
        splashLayoutResponse(response)
    }

    private fun splashLayoutResponse(response: Response<SplashResponse>) {
        if (response.isSuccessful && response.body() != null) {
            splashLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            splashLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            splashLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Food Reserve
    private val foodAddLiveData = MutableLiveData<NetworkResult<AddFoodResponse>>()
    val foodAddResponseLiveData: LiveData<NetworkResult<AddFoodResponse>>
        get() = foodAddLiveData

    suspend fun foodAddLayout(
        cinemacode: String,
        fb_totalprice: String,
        fb_itemStrDescription: String,
        pickupdate: String,
        cbookid: String,
        audi: String,
        seat: String,
        type: String,
        infosys: String,
        qr: String,
        isSpi: String,
        srilanka: String
    ) {
        foodAddLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.addFood(
            cinemacode,
            fb_totalprice,
            fb_itemStrDescription,
            pickupdate,
            cbookid,
            audi,
            seat,
            type,
            infosys,
            qr,
            isSpi,
            srilanka,
            Constant.version,
            Constant.platform
        )
        foodAddLayoutResponse(response)
    }

    private fun foodAddLayoutResponse(response: Response<AddFoodResponse>) {
        if (response.isSuccessful && response.body() != null) {
            foodAddLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            foodAddLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            foodAddLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //GiftCardMain
    private val giftCardMainLiveData =
        MutableLiveData<NetworkResult<GiftCardResponse>>()
    val giftCardMainResponseLiveData: LiveData<NetworkResult<GiftCardResponse>>
        get() = giftCardMainLiveData

    suspend fun giftCardMainLayout(sWidth: String, infosys: String) {
        giftCardMainLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.giftCardMain(
            sWidth, Constant.platform, infosys, Constant.version, Constant.platform
        )
        giftCardMainLayoutResponse(response)
    }

    private fun giftCardMainLayoutResponse(response: Response<GiftCardResponse>) {
        if (response.isSuccessful && response.body() != null) {
            giftCardMainLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            giftCardMainLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            giftCardMainLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //BookingRetrieval
    private val bookingRetrievalLiveData =
        MutableLiveData<NetworkResult<BookingRetrievalResponse>>()
    val bookingRetrievalResponseLiveData: LiveData<NetworkResult<BookingRetrievalResponse>>
        get() = bookingRetrievalLiveData

    suspend fun bookingRetrievalLayout(
        city: String,
        lat: String,
        lng: String,
        userid: String,
        searchtxt: String
    ) {
        bookingRetrievalLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.bookingRetrieval(
            city, lat, lng, userid, searchtxt, Constant.version, Constant.platform
        )
        bookingRetrievalLayoutResponse(response)
    }

    private fun bookingRetrievalLayoutResponse(response: Response<BookingRetrievalResponse>) {
        if (response.isSuccessful && response.body() != null) {
            bookingRetrievalLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            bookingRetrievalLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            bookingRetrievalLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


}