package com.net.pvr.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.net.pvr.api.UserAPI
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr.ui.cinemaSession.cinemaDetails.response.CinemaDetailsResponse
import com.net.pvr.ui.cinemaSession.response.CinemaNearTheaterResponse
import com.net.pvr.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr.ui.food.old.reponse.OldFoodResponse
import com.net.pvr.ui.food.response.CancelTransResponse
import com.net.pvr.ui.food.response.FoodResponse
import com.net.pvr.ui.giftCard.response.*
import com.net.pvr.ui.home.formats.response.FormatResponse
import com.net.pvr.ui.home.fragment.cinema.response.CinemaPreferenceResponse
import com.net.pvr.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr.ui.home.fragment.comingSoon.response.CommingSoonResponse
import com.net.pvr.ui.home.fragment.home.response.FeedbackDataResponse
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.home.fragment.home.response.NextBookingResponse
import com.net.pvr.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr.ui.home.fragment.more.contactUs.response.ContactUsResponse
import com.net.pvr.ui.home.fragment.more.eVoucher.details.response.SaveEVoucherResponse
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
import com.net.pvr.ui.home.fragment.privilege.response.PassportHistory
import com.net.pvr.ui.home.fragment.privilege.response.PassportPlanResponse
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr.ui.home.inCinemaMode.response.GetBookingResponse
import com.net.pvr.ui.home.inCinemaMode.response.GetInCinemaResponse
import com.net.pvr.ui.home.inCinemaMode.response.InCinemaHomeResponse
import com.net.pvr.ui.location.selectCity.response.SelectCityResponse
import com.net.pvr.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr.ui.login.response.LoginResponse
import com.net.pvr.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr.ui.myBookings.response.FoodTicketResponse
import com.net.pvr.ui.myBookings.response.GiftCardResponse
import com.net.pvr.ui.myBookings.response.MyVoucherList
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
import com.net.pvr.utils.Constant
import com.net.pvr.utils.NetworkResult
import com.net.pvr.utils.printLog
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
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
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //ContactUs
    private val contactUsLiveData = MutableLiveData<NetworkResult<ContactUsResponse>>()
    val contactUsResponseLiveData: LiveData<NetworkResult<ContactUsResponse>>
        get() = contactUsLiveData

    suspend fun contactUsUser(
        comment: String, email: String, mobile: String, did: String, isSpi: String, ctype: String
    ) {
        contactUsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.contactUs(
            comment, email, mobile, did, isSpi, ctype, Constant.version, Constant.platform
        )
        contactUs(response)
    }

    private fun contactUs(response: Response<ContactUsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            contactUsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                contactUsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                contactUsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
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
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                otpVerifyLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                otpVerifyLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            otpVerifyLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //getcoupons
    private val couponsLiveData = MutableLiveData<NetworkResult<CouponResponse>>()
    val couponsResponseLiveData: LiveData<NetworkResult<CouponResponse>>
        get() = couponsLiveData

    suspend fun coupons(token: String,mobile: String, city: String, userid: String, timestamp: String) {
        couponsLiveData.postValue(NetworkResult.Loading())
        val response =
            userAPI.getCoupons(token,mobile, city,userid, timestamp , "V", "true", Constant.version, Constant.platform,Constant.getDid())
        couponsResponse(response)
    }


    private fun couponsResponse(response: Response<CouponResponse>) {
        if (response.isSuccessful && response.body() != null) {
            couponsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                couponsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                couponsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            couponsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

//Apply Loyalty Vouchers
    private val voucherApplyLiveData = MutableLiveData<NetworkResult<LoyaltyVocherApply>>()
    val voucherApplyResponseLiveData: LiveData<NetworkResult<LoyaltyVocherApply>>
        get() = voucherApplyLiveData

    suspend fun voucherApply(promocode: String, userid: String, booktype: String, bookingid: String, transid: String, loyalitytype: String, unlimitedvoucher: String, voucheramt: String) {
        voucherApplyLiveData.postValue(NetworkResult.Loading())
        val response =
            userAPI.loyaltyPromo(promocode,userid, booktype,bookingid,transid,loyalitytype ,unlimitedvoucher,voucheramt, Constant.version, Constant.platform,Constant.getDid())
        voucherApplyResponse(response)
    }


    private fun voucherApplyResponse(response: Response<LoyaltyVocherApply>) {
        if (response.isSuccessful && response.body() != null) {
            voucherApplyLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            voucherApplyLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            voucherApplyLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
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
        val response = userAPI.paytmHMAC(
            userid,
            bookingid,
            transid,
            unpaid,
            cardNo,
            booktype,
            ptype,
            isSpi,
            binOffer,
            Constant.version,
            Constant.platform
        )
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


    //paytm HMAC
    private val paytmBankListLiveData = MutableLiveData<NetworkResult<BankListResponse>>()
    val paytmBankListResponseLiveData: LiveData<NetworkResult<BankListResponse>>
        get() = paytmBankListLiveData

    suspend fun bankList() {
        paytmBankListLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.bankList(
            Constant.version,
            Constant.platform,
            Constant.getDid()
        )
        paytmBankListResponse(response)
    }

    private fun paytmBankListResponse(response: Response<BankListResponse>) {
        if (response.isSuccessful && response.body() != null) {
            paytmBankListLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            paytmBankListLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            paytmBankListLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

 //paytm Recurring HMAC
    private val paytmRHmacLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val paytmRHmacResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = paytmRHmacLiveData

    suspend fun paytmRHMAC(
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
        paytmRHmacLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.recurringPaytmHMAC(
            userid,
            bookingid,
            transid,
            unpaid,
            cardNo,
            booktype,
            ptype,
            isSpi,
            binOffer,
            Constant.version,
            Constant.platform
        )
        paytmRHmacResponse(response)
    }

    private fun paytmRHmacResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            paytmRHmacLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            paytmRHmacLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            paytmRHmacLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
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
        val response = userAPI.payMode(
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
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                payModeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                payModeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } else {
            payModeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Upi Status
    private val upiStatusLiveData = MutableLiveData<NetworkResult<UPIStatusResponse>>()
    val upiStatusResponseLiveData: LiveData<NetworkResult<UPIStatusResponse>>
        get() = upiStatusLiveData

    suspend fun upiStatus(bookingid: String, booktype: String) {
        upiStatusLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.upiStatus(
            bookingid, booktype, Constant.version, Constant.platform
        )
        upiStatusResponse(response)
    }

    private fun upiStatusResponse(response: Response<UPIStatusResponse>) {
        if (response.isSuccessful && response.body() != null) {
            upiStatusLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            upiStatusLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            upiStatusLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
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
        resisterLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.resister(
            hash, email, name, mobile, otp, city, "", Constant.version, Constant.platform
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
            city, genre, lang, userid, Constant.version, Constant.platform
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
    //my Voucher
    private val myVoucherLiveData = MutableLiveData<NetworkResult<VoucherListResponse>>()
    val myVoucherResponseLiveData: LiveData<NetworkResult<VoucherListResponse>>
        get() = myVoucherLiveData

    suspend fun myVoucher(userid: String) {
        myVoucherLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.myVoucher( userid, Constant.version, Constant.platform)
        myVoucherResponse(response)
    }

    private fun myVoucherResponse(response: Response<VoucherListResponse>) {
        if (response.isSuccessful && response.body() != null) {
            myVoucherLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            myVoucherLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            myVoucherLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //myE Voucher
    private val myeVoucherLiveData = MutableLiveData<NetworkResult<VoucherListResponse>>()
    val myeVoucherResponseLiveData: LiveData<NetworkResult<VoucherListResponse>>
        get() = myeVoucherLiveData

    suspend fun myEVoucher(cname: String,cat: String) {
        myeVoucherLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.myeVoucher( cname, Constant.version, Constant.platform)
        myEVoucherResponse(response)
    }

    private fun myEVoucherResponse(response: Response<VoucherListResponse>) {
        if (response.isSuccessful && response.body() != null) {
            myeVoucherLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            myeVoucherLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            myeVoucherLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //saveEVoucher
    private val saveEVoucherLiveData = MutableLiveData<NetworkResult<SaveEVoucherResponse>>()
    val saveEVoucherResponseLiveData: LiveData<NetworkResult<SaveEVoucherResponse>>
        get() = saveEVoucherLiveData

    suspend fun saveEVoucher(userid: String,quantity: String, did: String) {
        saveEVoucherLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.saveEVoucher( userid,quantity,did, Constant.version, Constant.platform)
        saveEVoucherResponse(response)
    }

    private fun saveEVoucherResponse(response: Response<SaveEVoucherResponse>) {
        if (response.isSuccessful && response.body() != null) {
            saveEVoucherLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            saveEVoucherLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            saveEVoucherLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Cinema
    private val cinemaLiveData = MutableLiveData<NetworkResult<CinemaResponse>>()
    val cinemaResponseLiveData: LiveData<NetworkResult<CinemaResponse>>
        get() = cinemaLiveData

    suspend fun cinema(city: String, lat: String, lng: String, userid: String, searchTxt: String) {
        cinemaLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.cinema(
            city, lat, lng, userid, searchTxt, Constant.version, Constant.platform
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
    private val cinemaPreferenceLiveData =
        MutableLiveData<NetworkResult<CinemaPreferenceResponse>>()
    val cinemaPreferenceResponseLiveData: LiveData<NetworkResult<CinemaPreferenceResponse>>
        get() = cinemaPreferenceLiveData

    suspend fun cinemaPreference(
        userid: String, id: String, is_like: Boolean, type: String, did: String
    ) {
        cinemaPreferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.cinemaPreference(
            userid, id, is_like, type, did, Constant.version, Constant.platform
        )
        cinemaPreferenceResponse(response)
    }

    private fun cinemaPreferenceResponse(response: Response<CinemaPreferenceResponse>) {
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
            userId, did, Constant.version, Constant.platform
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
    //Active GiftCard
    private val activegiftCardLiveData = MutableLiveData<NetworkResult<ActiveGCResponse>>()
    val activegiftCardResponseLiveData: LiveData<NetworkResult<ActiveGCResponse>>
        get() = activegiftCardLiveData

    suspend fun activeGiftCard(userId: String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.activeGiftCard(
            userId, Constant.version, Constant.platform,Constant.getDid()
        )
        activeGiftCardResponse(response)
    }

    private fun activeGiftCardResponse(response: Response<ActiveGCResponse>) {
        if (response.isSuccessful && response.body() != null) {
            activegiftCardLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            activegiftCardLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            activegiftCardLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Redeem GiftCard
    private val redeemGiftCardLiveData = MutableLiveData<NetworkResult<GiftcardDetailsResponse>>()
    val redeemGiftCardResponseLiveData: LiveData<NetworkResult<GiftcardDetailsResponse>>
        get() = redeemGiftCardLiveData

    suspend fun redeemGiftCard(userId: String,giftcardid: String,pin: String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.getDetailGiftCard(
            userId, giftcardid ,pin,Constant.version, Constant.platform,Constant.getDid()
        )
        redeemGiftCardResponse(response)
    }

    private fun redeemGiftCardResponse(response: Response<GiftcardDetailsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            redeemGiftCardLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            redeemGiftCardLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            redeemGiftCardLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // GiftCard Details
    private val detailGiftCardLiveData = MutableLiveData<NetworkResult<GiftCardDetailResponse>>()
    val detailGiftCardResponseLiveData: LiveData<NetworkResult<GiftCardDetailResponse>>
        get() = detailGiftCardLiveData

    suspend fun detailGiftCard(giftcardid: String,userid: String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.giftcardDetails(giftcardid ,userid,Constant.version, Constant.platform,Constant.getDid())
        detailGiftCardResponse(response)
    }

    private fun detailGiftCardResponse(response: Response<GiftCardDetailResponse>) {
        if (response.isSuccessful && response.body() != null) {
            detailGiftCardLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            detailGiftCardLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            detailGiftCardLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Save GiftCard
    private val saveGiftCardLiveData = MutableLiveData<NetworkResult<UploadImageGC>>()
    val saveGiftCardResponseLiveData: LiveData<NetworkResult<UploadImageGC>>
        get() = saveGiftCardLiveData


    suspend fun saveGiftCard(rName: String,rEmail: String,rMobile: String,gc_channel: String,gtype: String,
                             pkGiftId: String,pincode: String,personalMessage: String,delAddress: String,denomination: String,quantity: String,userEmail: String
                             ,ifSelf: String,totalAmount: String,customImage: String,customName:String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.saveGiftCard(rName, rEmail ,rMobile,gc_channel,gtype,pkGiftId,pincode,personalMessage,delAddress,denomination,quantity,userEmail,ifSelf,totalAmount,customImage,customName,"","NO",Constant.version, Constant.platform,Constant.getDid())
        saveGiftCardResponse(response)
    }

    private fun saveGiftCardResponse(response: Response<UploadImageGC>) {
        if (response.isSuccessful && response.body() != null) {
            saveGiftCardLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            saveGiftCardLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            saveGiftCardLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


 //UPLOAD GiftCard
    private val uploadGiftCardLiveData = MutableLiveData<NetworkResult<UploadImageGC>>()
    val uploadGiftCardResponseLiveData: LiveData<NetworkResult<UploadImageGC>>
        get() = uploadGiftCardLiveData

    suspend fun uploadGiftCard(image: MultipartBody.Part,name: RequestBody, time: RequestBody, userId: RequestBody, userNo: RequestBody,token:String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.uploadGCImage(image,name,time,userId,userNo,token)
        uploadGiftCardResponse(response)
    }

    private fun uploadGiftCardResponse(response: Response<UploadImageGC>) {
        if (response.isSuccessful && response.body() != null) {
            uploadGiftCardLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            uploadGiftCardLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            uploadGiftCardLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Update GiftCard
    private val updateGiftCardLiveData = MutableLiveData<NetworkResult<UploadImageGC>>()
    val updateGiftCardResponseLiveData: LiveData<NetworkResult<UploadImageGC>>
        get() = updateGiftCardLiveData

    suspend fun updateGiftCard(userId: String,pkGiftId: String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.generateGeneric(userId,pkGiftId,Constant.version, Constant.platform,Constant.getDid())
        updateGiftCardResponse(response)
    }

    private fun updateGiftCardResponse(response: Response<UploadImageGC>) {
        if (response.isSuccessful && response.body() != null) {
            updateGiftCardLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            updateGiftCardLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            updateGiftCardLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Reupload GiftCard
    private val reUploadGiftCardLiveData = MutableLiveData<NetworkResult<UploadImageGC>>()
    val reUploadGiftCardResponseLiveData: LiveData<NetworkResult<UploadImageGC>>
        get() = reUploadGiftCardLiveData

    suspend fun reUploadGiftCard(userId: String,customImage: String,customName: String,pkGiftId: String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.reUpload(userId,customImage,customName,pkGiftId,Constant.version, Constant.platform,Constant.getDid())
        reUploadGiftCardResponse(response)
    }

    private fun reUploadGiftCardResponse(response: Response<UploadImageGC>) {
        if (response.isSuccessful && response.body() != null) {
            reUploadGiftCardLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            reUploadGiftCardLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            reUploadGiftCardLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //FoodTicket
    private val foodTicketLiveData = MutableLiveData<NetworkResult<FoodTicketResponse>>()
    val foodTicketResponseLiveData: LiveData<NetworkResult<FoodTicketResponse>>
        get() = foodTicketLiveData

    suspend fun foodTicket(
        userId: String, did: String, sriLanka: String, city: String, isSpi: String, past: String
    ) {
        foodTicketLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.foodTicket(
            userId, did, sriLanka, city, isSpi, past, Constant.version, Constant.platform
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

    //resend Mail
    private val resendMailLiveData = MutableLiveData<NetworkResult<FoodTicketResponse>>()
    val resendMailResponseLiveData: LiveData<NetworkResult<FoodTicketResponse>>
        get() = resendMailLiveData

    suspend fun resendMail(
        userId: String, bookingId: String, type: String
    ) {
        resendMailLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.resendMail(
            userId, bookingId,type, Constant.version, Constant.platform
        )
        resendMailResponse(response)
    }

    private fun resendMailResponse(response: Response<FoodTicketResponse>) {
        if (response.isSuccessful && response.body() != null) {
            resendMailLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            resendMailLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            resendMailLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Offer
    private val offerLiveData = MutableLiveData<NetworkResult<OfferResponse>>()
    val offerResponseLiveData: LiveData<NetworkResult<OfferResponse>>
        get() = offerLiveData

    suspend fun offer(city: String, userId: String, did: String, isSpi: String) {
        offerLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.offer(city,userId,did,isSpi, Constant.version, Constant.platform)
        offerResponse(response)
    }

    private fun offerResponse(response: Response<OfferResponse>) {
        if (response.isSuccessful && response.body() != null) {
            offerLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                offerLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                offerLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            offerLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Hide Offer
    private val hideOfferLiveData = MutableLiveData<NetworkResult<ResponseBody>>()
    val hideOfferResponseLiveData: LiveData<NetworkResult<ResponseBody>>
        get() = hideOfferLiveData

    suspend fun hideOffer(city: String, userId: String, did: String, isSpi: String) {
        hideOfferLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.hideOffer(city,userId,Constant.getDid(),isSpi, Constant.version, Constant.platform)
        hideOfferResponse(response)
    }

    private fun hideOfferResponse(response: Response<ResponseBody>) {
        if (response.isSuccessful && response.body() != null) {
            hideOfferLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            hideOfferLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            hideOfferLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //EditProfile

    private val editProfileLiveData = MutableLiveData<NetworkResult<ProfileResponse>>()
    val editProfileResponseLiveData: LiveData<NetworkResult<ProfileResponse>>
        get() = editProfileLiveData

    suspend fun editProfile(
        userid: String,
        email: String,
        mobile: String,
        name: String,
        dob: String,
        gender: String,
        mstatus: String,
        doa: String
    ) {
        editProfileLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.editProfile(userid,email,mobile,name,dob,gender,mstatus,doa, Constant.version, Constant.platform)
        editProfileResponse(response)
    }

    private fun editProfileResponse(response: Response<ProfileResponse>) {
        if (response.isSuccessful && response.body() != null) {
            editProfileLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            editProfileLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            editProfileLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
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
    private val offerDetailsLiveData = MutableLiveData<NetworkResult<OfferDetailsResponse>>()
    val offerDetailsResponseLiveData: LiveData<NetworkResult<OfferDetailsResponse>>
        get() = offerDetailsLiveData

    suspend fun offerDetails(id: String, did: String) {
        offerDetailsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.offerDetails(id, Constant.version, Constant.platform, did)
        offerDetailsResponse(response)
    }

    private fun offerDetailsResponse(response: Response<OfferDetailsResponse>) {
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
        lat: String, lng: String, userid: String, isSpi: String, srilanka: String
    ) {
        selectCityLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.selectCity(
            lat, lng, Constant.version, Constant.platform, userid, isSpi, srilanka
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
        homeLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.home(
            city,
            Constant.version,
            Constant.platform,
            dtmsource,
            userid,
            mobile,
            upbooking,
            srilanka,
            type,
            lng,
            gener,
            spShow,
            isSpi
        )
        homeResponse(response)
    }

    private fun homeResponse(response: Response<HomeResponse>) {
        if (response.isSuccessful && response.body() != null) {
            homeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                homeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                e.printStackTrace()
            }
        } else {
            homeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /************   Privilege USER REPO        *******************/

    // Privilege Home
    private val privilegeHomeLiveData = MutableLiveData<NetworkResult<PrivilegeHomeResponse>>()
    val privilegeHomeResponseLiveData: LiveData<NetworkResult<PrivilegeHomeResponse>>
        get() = privilegeHomeLiveData

    suspend fun privilegeHomeData(mobile: String, city: String) {
        privilegeHomeLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.privilegeHome(
            mobile, city, Constant.version, Constant.platform
        )
        privilegeHomeResponse(response)
    }

    private fun privilegeHomeResponse(response: Response<PrivilegeHomeResponse>) {
        if (response.isSuccessful && response.body() != null) {
            privilegeHomeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = ""
            try {
                val errorObj = JSONObject(response.errorBody()?.charStream()?.readText())
                privilegeHomeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                privilegeHomeLiveData.postValue(NetworkResult.Error(response.errorBody()?.charStream()?.readText().toString()))
            }
        } else {
            privilegeHomeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    // Privilege Home
    private val loyaltyDataLiveData = MutableLiveData<NetworkResult<LoyaltyDataResponse>>()
    val loyaltyDataResponseLiveData: LiveData<NetworkResult<LoyaltyDataResponse>>
        get() = loyaltyDataLiveData

    suspend fun loyaltyData(userId: String, city: String,mobile: String, timestamp: String,token: String) {
        loyaltyDataLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.loyaltyData(
            userId, city, mobile,timestamp,token,Constant.version, Constant.platform,Constant.getDid()
        )
        loyaltyDataResponse(response)
    }

    private fun loyaltyDataResponse(response: Response<LoyaltyDataResponse>) {
        if (response.isSuccessful && response.body() != null) {
            loyaltyDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = ""
            try {
                val errorObj = JSONObject(response.errorBody()?.charStream()?.readText())
                loyaltyDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                loyaltyDataLiveData.postValue(NetworkResult.Error(response.errorBody()?.charStream()?.readText().toString()))
            }
        } else {
            loyaltyDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    // Passport Plan
    private val passportPlanLiveData = MutableLiveData<NetworkResult<PassportPlanResponse>>()
    val passportPlanResponseLiveData: LiveData<NetworkResult<PassportPlanResponse>>
        get() = passportPlanLiveData

    suspend fun passportPlans(userId: String, city: String) {
        passportPlanLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.passportPlans(
            userId, city, Constant.version, Constant.platform, Constant.getDid()
        )
        passportPlanResponse(response)
    }

    private fun passportPlanResponse(response: Response<PassportPlanResponse>) {
        if (response.isSuccessful && response.body() != null) {
            passportPlanLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()?.charStream()?.readText())
                passportPlanLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                passportPlanLiveData.postValue(NetworkResult.Error(response.errorBody()?.charStream()?.readText().toString()))
            }
        } else {
            passportPlanLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // Passport History
    private val passportHistoryLiveData = MutableLiveData<NetworkResult<PassportHistory>>()
    val passportHistoryResponseLiveData: LiveData<NetworkResult<PassportHistory>>
        get() = passportHistoryLiveData

    suspend fun passportHistory(userId: String, mobile: String) {
        passportHistoryLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.passportHistory(
            userId, mobile, Constant.version, Constant.platform, Constant.getDid()
        )
        passportHistoryResponse(response)
    }

    private fun passportHistoryResponse(response: Response<PassportHistory>) {
        if (response.isSuccessful && response.body() != null) {
            passportHistoryLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()?.charStream()?.readText())
                passportHistoryLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                passportHistoryLiveData.postValue(NetworkResult.Error(response.errorBody()?.charStream()?.readText().toString()))
            }
        } else {
            passportHistoryLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    // Passport Cancel
    private val passportCancelLiveData = MutableLiveData<NetworkResult<PassportHistory>>()
    val passportCancelResponseLiveData: LiveData<NetworkResult<PassportHistory>>
        get() = passportCancelLiveData

    suspend fun passportCancel(userId: String, reason: String,voucher: String) {
        passportCancelLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.passportCancel(
            userId, reason,voucher, Constant.version, Constant.platform, Constant.getDid()
        )
        passportCancelResponse(response)
    }

    private fun passportCancelResponse(response: Response<PassportHistory>) {
        if (response.isSuccessful && response.body() != null) {
            passportCancelLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()?.charStream()?.readText())
                passportCancelLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                passportCancelLiveData.postValue(NetworkResult.Error(response.errorBody()?.charStream()?.readText().toString()))
            }
        } else {
            passportCancelLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // Passport Save
    private val passportSaveLiveData = MutableLiveData<NetworkResult<PassportPlanResponse>>()
    val passportSaveResponseLiveData: LiveData<NetworkResult<PassportPlanResponse>>
        get() = passportSaveLiveData

    suspend fun passportSave(userId: String, city: String, fname: String, lname: String, subsplan: String, dob: String, gender: String, scheme: String, price: String, mobile: String, email: String) {
        passportSaveLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.savePassport(
            userId, city,fname,lname,subsplan,dob,gender,scheme,price,mobile,email, Constant.version, Constant.platform, Constant.getDid())
        passportSaveResponse(response)
    }

    private fun passportSaveResponse(response: Response<PassportPlanResponse>) {
        if (response.isSuccessful && response.body() != null) {
            passportSaveLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()?.charStream()?.readText())
                passportSaveLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                passportSaveLiveData.postValue(NetworkResult.Error(response.errorBody()?.charStream()?.readText().toString()))
            }
        } else {
            passportSaveLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    // Privilege Save
    private val enrollPrivilegeLiveData = MutableLiveData<NetworkResult<PassportPlanResponse>>()
    val enrollPrivilegeResponseLiveData: LiveData<NetworkResult<PassportPlanResponse>>
        get() = enrollPrivilegeLiveData

    suspend fun enrollPrivilege(userId: String, city: String, fname: String, lname: String,  dob: String, gender: String, email: String) {
        enrollPrivilegeLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.enrollPrivilege(
            userId, city,fname,lname,dob,gender,email, Constant.version, Constant.platform, Constant.getDid())
        enrollPrivilegeResponse(response)
    }

    private fun enrollPrivilegeResponse(response: Response<PassportPlanResponse>) {
        if (response.isSuccessful && response.body() != null) {
            enrollPrivilegeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()?.charStream()?.readText())
                enrollPrivilegeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                enrollPrivilegeLiveData.postValue(NetworkResult.Error(response.errorBody()?.charStream()?.readText().toString()))
            }
        } else {
            enrollPrivilegeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    // Generate Order
    private val passportGenerateLiveData = MutableLiveData<NetworkResult<PassportPlanResponse>>()
    val passportGenerateResponseLiveData: LiveData<NetworkResult<PassportPlanResponse>>
        get() = passportGenerateLiveData

    suspend fun passportGenerate(userId: String, city: String, scheme: String, bookingid: String, retrycount: String, reason: String) {
        passportGenerateLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.genrateNewOrder(
            userId, city,scheme, bookingid,retrycount, reason , Constant.version, Constant.platform, Constant.getDid())
        passportGenerateResponse(response)
    }

    private fun passportGenerateResponse(response: Response<PassportPlanResponse>) {
        if (response.isSuccessful && response.body() != null) {
            passportGenerateLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()?.charStream()?.readText())
                passportGenerateLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                passportGenerateLiveData.postValue(NetworkResult.Error(response.errorBody()?.charStream()?.readText().toString()))
            }
        } else {
            passportGenerateLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // NextBooking
    private val nextBookingLiveData = MutableLiveData<NetworkResult<NextBookingResponse>>()
    val nextBookingResponseLiveData: LiveData<NetworkResult<NextBookingResponse>>
        get() = nextBookingLiveData

    suspend fun nextBookingData(userid: String, did: String) {
        nextBookingLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.nextBooking(
            userid, did, Constant.version, Constant.platform
        )
        nextBookingResponse(response)
    }

    private fun nextBookingResponse(response: Response<NextBookingResponse>) {
        if (response.isSuccessful && response.body() != null) {
            nextBookingLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                nextBookingLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e: JSONException){
                e.printStackTrace()
            }
        } else {
            nextBookingLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Search
    private val homeSearchLiveData = MutableLiveData<NetworkResult<HomeSearchResponse>>()
    val searchResponseLiveData: LiveData<NetworkResult<HomeSearchResponse>>
        get() = homeSearchLiveData

    suspend fun homeSearchData(
        city: String, text: String, searchFilter: String, lat: String, lng: String
    ) {
        homeSearchLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.homeSearch(
            city, text, searchFilter, lat, lng, Constant.version, Constant.platform
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
            city, mid, Constant.version, type, Constant.platform, userid, lat, lng, isSpi, srilanka
        )
        movieDetailsResponse(response)
    }

    private fun movieDetailsResponse(response: Response<MovieDetailsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            movieDetailsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                movieDetailsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                movieDetailsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            movieDetailsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //CommingSoon
    private val commingSoonLiveData = MutableLiveData<NetworkResult<MovieDetailsResponse>>()
    val commingSoonResponseLiveData: LiveData<NetworkResult<MovieDetailsResponse>>
        get() = commingSoonLiveData

    suspend fun commingSoonData(
        type: String, userid: String, city: String, mcode: String
    ) {
        movieDetailsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.commingSoon(
            type, userid, city, mcode, Constant.version, Constant.platform
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
        userid: String, city: String, mcode: String, did: String
    ) {
        movieAlertLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.movieAlert(
            userid, city, mcode, did, Constant.version, Constant.platform
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

//cinema Details
    private val cinemaDetailsLiveData = MutableLiveData<NetworkResult<CinemaDetailsResponse>>()
    val cinemaDetailsResponseLiveData: LiveData<NetworkResult<CinemaDetailsResponse>>
        get() = cinemaDetailsLiveData

    suspend fun cinemaDetails(cid: String,lat:String,lang:String) {
        cinemaDetailsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.cinemaDetails(cid,lat,lang, Constant.version, Constant.platform
        )
        cinemaDetailsResponse(response)
    }

    private fun cinemaDetailsResponse(response: Response<CinemaDetailsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            cinemaDetailsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            cinemaDetailsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            cinemaDetailsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
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
        userid: String,
        lang: String,
        format: String,
        price: String,
        hc: String,
        time: String,
        cinetype: String,
        special: String
    ) {
        bookingSessionLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.bookingSession(
            city, mid, lat, lng, date, Constant.version, Constant.platform, isSpi, srilanka, userid,lang,format,price,hc,time,cinetype,special

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
        city: String, cid: String, userid: String, mid: String, lng: String, isSpi: String
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





    //Vouchers
    private val vouchersLiveData = MutableLiveData<NetworkResult<MyVoucherList>>()
    val vouchersResponseLiveData: LiveData<NetworkResult<MyVoucherList>>
        get() = vouchersLiveData

    suspend fun vouchers(
        userid: String
    ) {
        vouchersLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.myVouchers(
            userid, Constant.version, Constant.platform)
        vouchersResponse(response)
    }

    private fun vouchersResponse(response: Response<MyVoucherList>) {
        if (response.isSuccessful && response.body() != null) {
            vouchersLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            vouchersLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            vouchersLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
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
        cinemacode: String, sessionid: String
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
        foodLiveData.postValue(NetworkResult.Loading())
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

    //Old Food
    private val oldfoodLiveData = MutableLiveData<NetworkResult<OldFoodResponse>>()
    val oldFoodResponseLiveData: LiveData<NetworkResult<OldFoodResponse>>
        get() = oldfoodLiveData

    suspend fun oldFoodLayout(
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
        oldfoodLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.oldFood(
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
        oldFoodLayoutResponse(response)
    }

    private fun oldFoodLayoutResponse(response: Response<OldFoodResponse>) {
        if (response.isSuccessful && response.body() != null) {
            oldfoodLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            oldfoodLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            oldfoodLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //cancelTrans
    private val cancelTransLiveData = MutableLiveData<NetworkResult<CancelTransResponse>>()
    val  cancelTransResponseLiveData: LiveData<NetworkResult<CancelTransResponse>>
        get() = cancelTransLiveData

    suspend fun cancelTrans(cinemacode: String, transid: String, bookingid: String) {
        cancelTransLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.cancelTrans(
            cinemacode,
            transid,
            bookingid,
            Constant.version,
            Constant.platform
        )
        cancelTransResponse(response)
    }

    private fun  cancelTransResponse(response: Response<CancelTransResponse>) {
        if (response.isSuccessful && response.body() != null) {
            cancelTransLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            cancelTransLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            cancelTransLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


 //cancelBooking
    private val cancelBookingLiveData = MutableLiveData<NetworkResult<CancelTransResponse>>()
    val  cancelBookingResponseLiveData: LiveData<NetworkResult<CancelTransResponse>>
        get() = cancelBookingLiveData

    suspend fun cancelBooking(userid: String, bookingid: String) {
        cancelBookingLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.cancelBooking(
            userid,
            bookingid,
            Constant.version,
            Constant.platform,
            Constant.getDid()
        )
        cancelBookingResponse(response)
    }

    private fun  cancelBookingResponse(response: Response<CancelTransResponse>) {
        if (response.isSuccessful && response.body() != null) {
            cancelBookingLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                cancelBookingLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                cancelBookingLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            cancelBookingLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Summery
    private val summerLiveData = MutableLiveData<NetworkResult<SummeryResponse>>()
    val summerResponseLiveData: LiveData<NetworkResult<SummeryResponse>>
        get() = summerLiveData

    suspend fun summerLayout(
        transid: String, cinemacode: String, userid: String, bookingid: String, donation: String
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
            donation,
            "",
            Constant.version,
            Constant.platform
        )
        summeryLayoutResponse(response)
    }

    suspend fun foodDetails(userid: String, bookingid: String) {
        formatsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.foodSummary(
            bookingid,
            userid,
            Constant.QR,
            Constant.version,
            Constant.platform,
            Constant.getDid()
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
            type, city, isSpi, Constant.version, Constant.platform
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


//    Experience
    private val experienceLiveData = MutableLiveData<NetworkResult<ExperienceResponse>>()
    val  experienceResponseLiveData: LiveData<NetworkResult<ExperienceResponse>>
        get() = experienceLiveData
    suspend fun  experienceLayout(city: String) {
        experienceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.experience(
            city, Constant.version, Constant.platform
        )
        experienceLayoutResponse(response)
    }

    private fun  experienceLayoutResponse(response: Response<ExperienceResponse>) {
        if (response.isSuccessful && response.body() != null) {
            experienceLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            experienceLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            experienceLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

//    ExperienceDetails
    private val experienceDetailsLiveData = MutableLiveData<NetworkResult<ExperienceDetailsResponse>>()
    val  experienceDetailsResponseLiveData: LiveData<NetworkResult<ExperienceDetailsResponse>>
        get() = experienceDetailsLiveData
    suspend fun  experienceDetailsLayout(city: String, type: String) {
        experienceDetailsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.experienceDetails(
            city,type, Constant.version, Constant.platform
        )
        experienceDetailsLayoutResponse(response)
    }
    private fun  experienceDetailsLayoutResponse(response: Response<ExperienceDetailsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            experienceDetailsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            experienceDetailsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            experienceDetailsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
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
        bookingid: String, transid: String, isDonate: Boolean, istDonate: Boolean, isSpi: String
    ) {
        setDonationLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.setDonation(
            bookingid, transid, isDonate, istDonate, isSpi, Constant.version, Constant.platform
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
            city, "NO", Constant.version, Constant.platform
        )
        splashLayoutResponse(response)
    }

    private fun splashLayoutResponse(response: Response<SplashResponse>) {
        if (response.isSuccessful && response.body() != null) {
            splashLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()?.charStream()?.readText())
                splashLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                e.printStackTrace()
                if (response.errorBody()?.charStream()?.readText() == ""){
                    splashLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
                }else {
                    printLog(response.errorBody()?.charStream()?.readText())
                    splashLiveData.postValue(
                        NetworkResult.Error(
                            response.errorBody()?.charStream()?.readText().toString()
                        )
                    )
                }
            }
        } else {
            splashLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //watchList
    private val watchListLiveData = MutableLiveData<NetworkResult<WatchListResponse>>()
    val watchListResponseLiveData: LiveData<NetworkResult<WatchListResponse>>
        get() = watchListLiveData

    suspend fun watchListLayout(
        userid: String, city: String, did: String
    ) {
        watchListLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.watchList(
            userid, city, did, Constant.version, Constant.platform
        )
        watchListLayoutResponse(response)
    }

    private fun watchListLayoutResponse(response: Response<WatchListResponse>) {
        if (response.isSuccessful && response.body() != null) {
            watchListLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            watchListLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            watchListLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //DELETE ALERT
    private val deleteAlertLiveData = MutableLiveData<NetworkResult<DeleteAlertResponse>>()
    val deleteAlertResponseLiveData: LiveData<NetworkResult<DeleteAlertResponse>>
        get() = deleteAlertLiveData

    suspend fun deleteAlertLayout(userid: String, mcode: String, city: String) {
        deleteAlertLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.deleteAlert(
            userid, mcode, city, Constant.version, Constant.platform
        )
        deleteAlertResponse(response)
    }

    private fun deleteAlertResponse(response: Response<DeleteAlertResponse>) {
        if (response.isSuccessful && response.body() != null) {
            deleteAlertLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            deleteAlertLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            deleteAlertLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Food Reserve
    private val foodAddLiveData = MutableLiveData<NetworkResult<AddFoodResponse>>()
    val foodAddResponseLiveData: LiveData<NetworkResult<AddFoodResponse>>
        get() = foodAddLiveData

    suspend fun foodAddLayout(
        userid: String,
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
        val response = userAPI.addFood(userid,
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
            Constant.platform,
            Constant.getDid()
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
    private val giftCardMainLiveData = MutableLiveData<NetworkResult<GiftCardListResponse>>()
    val giftCardMainResponseLiveData: LiveData<NetworkResult<GiftCardListResponse>>
        get() = giftCardMainLiveData

    suspend fun giftCardMainLayout(sWidth: String, infosys: String) {
        giftCardMainLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.giftCardMain(
            sWidth, Constant.platform, infosys, Constant.version, Constant.platform
        )
        giftCardMainLayoutResponse(response)
    }

    private fun giftCardMainLayoutResponse(response: Response<GiftCardListResponse>) {
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
        city: String, lat: String, lng: String, userid: String, searchtxt: String
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

    //setAlert
    private val setAlertLiveData = MutableLiveData<NetworkResult<BookingRetrievalResponse>>()
    val setAlertResponseLiveData: LiveData<NetworkResult<BookingRetrievalResponse>>
        get() = setAlertLiveData

    suspend fun setAlertLayout(
        userid: String,
        city: String,
        mcode: String,
        cinema: String,
        whtsapp: String,
        pushnotify: String,
        sms: String,
        email: String,
        did: String
    ) {
        setAlertLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.setAlert(
            userid,
            city,
            mcode,
            cinema,
            whtsapp,
            pushnotify,
            sms,
            email,
            did,
            Constant.version,
            Constant.platform
        )
        setAlertLayoutResponse(response)
    }

    private fun setAlertLayoutResponse(response: Response<BookingRetrievalResponse>) {
        if (response.isSuccessful && response.body() != null) {
            setAlertLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            setAlertLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            setAlertLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    //Phone Pe Payment Section

    //Phonepe Hmac
    private val phonepeHmacLiveData = MutableLiveData<NetworkResult<PhonepeHmacResponse>>()
    val phonepeHmacResponseLiveData: LiveData<NetworkResult<PhonepeHmacResponse>>
        get() = phonepeHmacLiveData

    suspend fun phonepeHmac(userid: String, bookingid: String, booktype: String, transid: String) {
        phonepeHmacLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.phonepeHmac(
            userid, bookingid, booktype, transid, Constant.version, Constant.platform
        )
        phonepeHmacResponse(response)
    }

    private fun phonepeHmacResponse(response: Response<PhonepeHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            phonepeHmacLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            phonepeHmacLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            phonepeHmacLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private val phonepeStatusLiveData = MutableLiveData<NetworkResult<UPIStatusResponse>>()
    val phonepeStatusResponseLiveData: LiveData<NetworkResult<UPIStatusResponse>>
        get() = phonepeStatusLiveData

    suspend fun phonepeStatus(
        userid: String, bookingid: String, booktype: String, transid: String
    ) {
        phonepeStatusLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.phonepeStatus(
            userid, bookingid, booktype, transid, Constant.version, Constant.platform
        )
        phonepeStatusResponse(response)
    }

    private fun phonepeStatusResponse(response: Response<UPIStatusResponse>) {
        if (response.isSuccessful && response.body() != null) {
            phonepeStatusLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            phonepeStatusLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            phonepeStatusLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /**    Paytm PostPaid Api        **/

// Paytm PostPaid Hmac
    private val postpaidHmacLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val postpaidHmacResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = postpaidHmacLiveData

    suspend fun postpaidHmac(
        userid: String, bookingid: String, booktype: String, transid: String
    ) {
        postpaidHmacLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.postPaidHmac(
            userid,
            bookingid,
            booktype,
            transid,
            "false",
            "false",
            Constant.version,
            Constant.platform
        )
        postpaidHmacResponse(response)
    }

    private fun postpaidHmacResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            postpaidHmacLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            postpaidHmacLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            postpaidHmacLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    // Paytm PostPaid PAY
    private val postpaidPAYLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val postpaidPAYResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = postpaidPAYLiveData

    suspend fun postpaidPay(
        userid: String, bookingid: String, booktype: String, transid: String
    ) {
        postpaidPAYLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.postPaidPay(
            userid,
            bookingid,
            booktype,
            transid,
            "false",
            "false",
            Constant.version,
            Constant.platform
        )
        postpaidPAYResponse(response)
    }

    private fun postpaidPAYResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            postpaidPAYLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            postpaidPAYLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            postpaidPAYLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    // Paytm PostPaid SEND OTP
    private val postpaidSendOTPLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val postpaidSendOTPResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = postpaidSendOTPLiveData

    suspend fun postPaidSendOTP(
        userid: String, bookingid: String, booktype: String, transid: String
    ) {
        postpaidSendOTPLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.postPaidSendOTP(
            userid, bookingid, booktype, transid, "false", Constant.version, Constant.platform
        )
        postpaidSendOTPResponse(response)
    }

    private fun postpaidSendOTPResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            postpaidSendOTPLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            postpaidSendOTPLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            postpaidSendOTPLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // Paytm PostPaid VERIFY OTP
    private val postpaidVerifyOTPLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val postpaidVerifyOTPResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = postpaidVerifyOTPLiveData

    suspend fun postPaidVerifYOTP(
        userid: String, bookingid: String, booktype: String, transid: String, otp: String
    ) {
        postpaidVerifyOTPLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.postPaidVerifYOTP(
            userid, bookingid, booktype, transid, otp, "false", Constant.version, Constant.platform
        )
        postpaidVerifyOTPResponse(response)
    }

    private fun postpaidVerifyOTPResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            postpaidVerifyOTPLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            postpaidVerifyOTPLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            postpaidVerifyOTPLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    // Paytm PostPaid MakePayment
    private val postpaidMakePaymentLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val postpaidMakePaymentResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = postpaidMakePaymentLiveData

    suspend fun postPaidMakePayment(
        userid: String, bookingid: String, booktype: String, transid: String
    ) {
        postpaidMakePaymentLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.postPaidMakePayment(
            userid, bookingid, booktype, transid, Constant.version, Constant.platform
        )
        postpaidMakePaymentResponse(response)
    }

    private fun postpaidMakePaymentResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            postpaidMakePaymentLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            postpaidMakePaymentLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            postpaidMakePaymentLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    /**    Paytm Wallet Api        **/

// Paytm PostPaid Hmac
    private val paytmWalletHmacLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val paytmWalletHmacResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = paytmWalletHmacLiveData

    suspend fun paytmWalletHmac(
        userid: String, bookingid: String, booktype: String, transid: String
    ) {
        paytmWalletHmacLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.paytmWalletHmac(
            userid,
            bookingid,
            booktype,
            transid,
            Constant.version,
            Constant.platform
        )
        paytmWalletHmacResponse(response)
    }

    private fun paytmWalletHmacResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            paytmWalletHmacLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            paytmWalletHmacLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            paytmWalletHmacLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    // Paytm paytmWallet SEND OTP
    private val paytmWalletSendOTPLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val paytmWalletSendOTPResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = paytmWalletSendOTPLiveData

    suspend fun paytmWalletSendOTP(
        userid: String, bookingid: String, booktype: String, transid: String,mobile:String,email:String
    ) {
        paytmWalletSendOTPLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.paytmWalletSendOTP(
            userid, bookingid, booktype, transid,mobile,email, "false", Constant.version, Constant.platform
        )
        paytmWalletSendOTPResponse(response)
    }

    private fun paytmWalletSendOTPResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            paytmWalletSendOTPLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            paytmWalletSendOTPLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            paytmWalletSendOTPLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // Paytm paytmWallet VERIFY OTP
    private val paytmWalletVerifyOTPLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val paytmWalletVerifyOTPResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = paytmWalletVerifyOTPLiveData

    suspend fun paytmWalletVerifYOTP(
        userid: String, bookingid: String, booktype: String, transid: String, otp: String, state_text: String
    ) {
        paytmWalletVerifyOTPLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.paytmWalletVerifYOTP(
            userid, bookingid, booktype, transid, otp,state_text, "false", Constant.version, Constant.platform
        )
        paytmWalletVerifyOTPResponse(response)
    }

    private fun paytmWalletVerifyOTPResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            paytmWalletVerifyOTPLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            paytmWalletVerifyOTPLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            paytmWalletVerifyOTPLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    // Paytm paytmWallet MakePayment
    private val paytmWalletMakePaymentLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val paytmWalletMakePaymentResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = paytmWalletMakePaymentLiveData

    suspend fun paytmWalletMakePayment(
        userid: String, bookingid: String, booktype: String, transid: String
    ) {
        paytmWalletMakePaymentLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.paytmWalletMakePayment(
            userid, bookingid, booktype, transid, Constant.version, Constant.platform
        )
        paytmWalletMakePaymentResponse(response)
    }

    private fun paytmWalletMakePaymentResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            paytmWalletMakePaymentLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            paytmWalletMakePaymentLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            paytmWalletMakePaymentLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    // Paytm Wallet PAY
    private val walletpaidPAYLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val walletpaidPAYResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = walletpaidPAYLiveData

    suspend fun walletPay(
        userid: String, bookingid: String, booktype: String, transid: String
    ) {
        walletpaidPAYLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.walletPaidPay(
            userid,
            bookingid,
            booktype,
            transid,
            "false",
            "false",
            Constant.version,
            Constant.platform
        )
        walletpaidPAYResponse(response)
    }

    private fun walletpaidPAYResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            walletpaidPAYLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            walletpaidPAYLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            walletpaidPAYLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    /*******  Airtel PAY      ****************/

    private val airtelPayLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val airtelPayResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = airtelPayLiveData

    suspend fun airtelPay(
        userid: String, bookingid: String, booktype: String, transid: String
    ) {
        airtelPayLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.airtelPay(
            userid, bookingid, booktype, transid, "WT", Constant.version, Constant.platform
        )
        airtelPayResponse(response)
    }

    private fun airtelPayResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            airtelPayLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            airtelPayLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            airtelPayLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /*******  promoCode      ****************/


    private val promoCodeLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val promoCodeResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = promoCodeLiveData

    suspend fun promoCode(
        userid: String, bookingid: String, booktype: String, transid: String, promocode: String
    ) {
        promoCodeLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.promoCode(
            userid, bookingid, booktype, transid, promocode, Constant.version, Constant.platform
        )
        promoCodeResponse(response)
    }

    private fun promoCodeResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            promoCodeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            promoCodeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            promoCodeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private val removePromoLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val removePromoResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = removePromoLiveData

    suspend fun removePromo(
        mobile: String, bookingid: String, booktype: String
    ) {
        removePromoLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.removePromoCode(
            mobile, bookingid, booktype, Constant.version, Constant.platform,Constant.getDid()
        )
        removePromoResponse(response)
    }

    private fun removePromoResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            removePromoLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            removePromoLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            removePromoLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    /*******  promoGyft      ****************/


    private val promoGyftLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val promoGyftResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = promoGyftLiveData

    suspend fun promoGyft(
        userid: String, bookingid: String, booktype: String, transid: String, promocode: String
    ) {
        promoGyftLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.promoGyft(
            userid, bookingid, booktype, transid, promocode, Constant.version, Constant.platform
        )
        promoGyftResponse(response)
    }

    private fun promoGyftResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            promoGyftLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            promoGyftLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            promoGyftLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    /*******  Zaggle Pay      ****************/

    private val zaggalePayLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val zaggalePayResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = zaggalePayLiveData

    suspend fun zaggalePay(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        cardNo: String,
        pin: String,
        type: String
    ) {
        zaggalePayLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.zagglePay(
            userid,
            bookingid,
            booktype,
            transid,
            cardNo,
            pin,
            type,
            Constant.version,
            Constant.platform
        )
        zaggalePayResponse(response)
    }

    private fun zaggalePayResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            zaggalePayLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            zaggalePayLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            zaggalePayLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    /*******  Gift Card      ****************/

    private val giftCardRedeemLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val giftCardRedeemResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = giftCardRedeemLiveData

    suspend fun giftCardRedeem(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        cardNo: String,
        pin: String,
        type: String
    ) {
        giftCardRedeemLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.giftCardRedeem(
            userid,
            bookingid,
            booktype,
            transid,
            cardNo,
            pin,
            type,
            Constant.version,
            Constant.platform
        )
        giftCardRedeemResponse(response)
    }

    private fun giftCardRedeemResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            giftCardRedeemLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            giftCardRedeemLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            giftCardRedeemLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    /*******  ACCENTIVE_PROMO     ****************/

    private val accentivePromoLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val accentivePromoResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = accentivePromoLiveData

    suspend fun accentivePromo(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        promocode: String,
    ) {
        accentivePromoLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.accentivePromo(
            userid, bookingid, booktype, transid, promocode, Constant.version, Constant.platform
        )
        accentivePromoResponse(response)
    }

    private fun accentivePromoResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            accentivePromoLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            accentivePromoLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            accentivePromoLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /*******  STARPASS     ****************/

    private val starpassLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val starpassResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = starpassLiveData

    suspend fun starpass(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        cinemacode: String,
        starpasses: String,
    ) {
        starpassLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.starpass(
            userid,
            bookingid,
            booktype,
            transid,
            cinemacode,
            starpasses,
            Constant.version,
            Constant.platform
        )
        starpassResponse(response)
    }

    private fun starpassResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            starpassLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            starpassLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            starpassLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /*******  MCOUPON     ****************/

    private val mcouponLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val mcouponResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = mcouponLiveData

    suspend fun mcoupon(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        cinemacode: String,
        mccard: String,
        mcmobile: String,
        mCoupons: String,
    ) {
        mcouponLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.mcoupon(
            userid,
            bookingid,
            transid,
            booktype,
            cinemacode,
            mccard,
            mcmobile,
            mCoupons,
            Constant.version,
            Constant.platform
        )
        mcouponResponse(response)
    }

    private fun mcouponResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            mcouponLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            mcouponLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            mcouponLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /*******  HYATT     ****************/

    private val hyattSendOTPLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val hyattSendOTPResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = hyattSendOTPLiveData

    suspend fun hyattSendOTP(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        mobile: String,
    ) {
        hyattSendOTPLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.sendOTPHYATT(
            userid, bookingid, booktype, transid, mobile, Constant.version, Constant.platform
        )
        hyattSendOTPResponse(response)
    }

    private fun hyattSendOTPResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            hyattSendOTPLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            hyattSendOTPLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            hyattSendOTPLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val verfyOTPHYATTLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val verfyOTPHYATTResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = verfyOTPHYATTLiveData

    suspend fun verfyOTPHYATT(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        mobile: String,
        otp: String
    ) {
        verfyOTPHYATTLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.verifyOTPHYATT(
            userid, bookingid, booktype, transid, mobile, otp, Constant.version, Constant.platform
        )
        verfyOTPHYATTResponse(response)
    }

    private fun verfyOTPHYATTResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            verfyOTPHYATTLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            verfyOTPHYATTLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            verfyOTPHYATTLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //More whatsapp Opt Check

    private val whatsappOptLiveData = MutableLiveData<NetworkResult<WhatsAppOptStatus>>()
    val whatsappOptResponseLiveData: LiveData<NetworkResult<WhatsAppOptStatus>>
        get() = whatsappOptLiveData

    suspend fun whatsappOpt(userid: String, mobile: String, timestamp: String, token: String) {
        whatsappOptLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.whatsappOpt(
            userid,
            mobile,
            timestamp,
            token,
            Constant.version,
            Constant.platform,
            "123"
        )
        whatsappOptData(response)
    }

    private fun whatsappOptData(response: Response<WhatsAppOptStatus>) {
        if (response.isSuccessful && response.body() != null) {
            whatsappOptLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            whatsappOptLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            whatsappOptLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //    whatsapp Opt CheckIn
    private val whatsappOptInLiveData = MutableLiveData<NetworkResult<WhatsAppOptStatus>>()
    val whatsappOptResponseInLiveData: LiveData<NetworkResult<WhatsAppOptStatus>>
        get() = whatsappOptInLiveData

    suspend fun whatsappOptIn(userid: String, mobile: String, timestamp: String, token: String) {
        whatsappOptInLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.whatsappOptIn(
            userid,
            mobile,
            timestamp,
            token,
            Constant.version,
            Constant.platform,
            "123"
        )
        whatsappOptInData(response)
    }

    private fun whatsappOptInData(response: Response<WhatsAppOptStatus>) {
        if (response.isSuccessful && response.body() != null) {
            whatsappOptInLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            whatsappOptInLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            whatsappOptInLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //    whatsapp Opt CheckOut
    private val whatsappOptOutLiveData = MutableLiveData<NetworkResult<WhatsAppOptStatus>>()
    val whatsappOptResponseOutLiveData: LiveData<NetworkResult<WhatsAppOptStatus>>
        get() = whatsappOptOutLiveData

    suspend fun whatsappOptOut(userid: String, mobile: String, timestamp: String, token: String) {
        whatsappOptOutLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.whatsappOptOut(
            userid,
            mobile,
            timestamp,
            token,
            Constant.version,
            Constant.platform,
            "123"
        )
        whatsappOptOutData(response)
    }

    private fun whatsappOptOutData(response: Response<WhatsAppOptStatus>) {
        if (response.isSuccessful && response.body() != null) {
            whatsappOptOutLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            whatsappOptOutLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            whatsappOptOutLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
//User Profile

    private val userProfileLiveData = MutableLiveData<NetworkResult<ProfileResponse>>()
    val userProfileResponseOutLiveData: LiveData<NetworkResult<ProfileResponse>>
        get() = userProfileLiveData

    suspend fun userProfile(city: String, userid: String, timestamp: String, token: String) {
        whatsappOptOutLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.userProfile(
            city,
            userid,
            timestamp,
            token,
            Constant.version,
            Constant.platform,
        )
        userProfileData(response)
    }

    private fun userProfileData(response: Response<ProfileResponse>) {
        if (response.isSuccessful && response.body() != null) {
            userProfileLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            userProfileLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            userProfileLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val preferenceLiveData = MutableLiveData<NetworkResult<PreferenceResponse>>()
    val preferenceResponseLiveData: LiveData<NetworkResult<PreferenceResponse>>
        get() = preferenceLiveData

    suspend fun preference(city: String, userid: String) {
        preferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.preference(city, userid, Constant.version, Constant.platform)
        preferenceData(response)
    }

    private fun preferenceData(response: Response<PreferenceResponse>) {
        if (response.isSuccessful && response.body() != null) {
            preferenceLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            preferenceLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            preferenceLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    /*****************       CRED       **************/
    private val credCheckLiveData = MutableLiveData<NetworkResult<UPIStatusResponse>>()
    val credCheckResponseLiveData: LiveData<NetworkResult<UPIStatusResponse>>
        get() = credCheckLiveData

    suspend fun credCheck(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        unpaid: String,
        cred_present: String,
        spi: String
    ) {
        credCheckLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.credCheck(
            userid,
            bookingid,
            booktype,
            transid,
            unpaid,
            cred_present,
            spi,
            Constant.version,
            Constant.platform
        )
        credCheckResponse(response)
    }

    private fun credCheckResponse(response: Response<UPIStatusResponse>) {
        if (response.isSuccessful && response.body() != null) {
            credCheckLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            credCheckLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            credCheckLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val credHmacLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val credHmacResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = credHmacLiveData

    suspend fun credHmac(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        unpaid: String,
        cred_present: String,
        spi: String,
        ptype: String
    ) {
        credHmacLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.credHmac(
            userid,
            bookingid,
            booktype,
            transid,
            unpaid,
            cred_present,
            spi,
            ptype,
            Constant.version,
            Constant.platform
        )
        credHmacResponse(response)
    }

    private fun credHmacResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            credHmacLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            credHmacLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            credHmacLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val credStatusLiveData = MutableLiveData<NetworkResult<UPIStatusResponse>>()
    val credStatusResponseLiveData: LiveData<NetworkResult<UPIStatusResponse>>
        get() = credStatusLiveData

    suspend fun credStatus(
        userid: String, bookingid: String, booktype: String, transid: String
    ) {
        credStatusLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.credStatus(
            userid, bookingid, booktype, transid, Constant.version, Constant.platform
        )
        credStatusResponse(response)
    }

    private fun credStatusResponse(response: Response<UPIStatusResponse>) {
        if (response.isSuccessful && response.body() != null) {
            credStatusLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            credStatusLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            credStatusLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    ///////////////Booking ////////////////////

    //getCode
    private val getCodeLiveData = MutableLiveData<NetworkResult<PreferenceResponse>>()
    val getCodeResponseLiveData: LiveData<NetworkResult<PreferenceResponse>>
        get() = getCodeLiveData

    suspend fun getCode(cid: String, cineTypeQR: String) {
        preferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.getCode(cid, cineTypeQR, Constant.version, Constant.platform)
        getCodeData(response)
    }

    private fun getCodeData(response: Response<PreferenceResponse>) {
        if (response.isSuccessful && response.body() != null) {
            getCodeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            getCodeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            getCodeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //UserLocation
    private val userLocationLiveData = MutableLiveData<NetworkResult<PreferenceResponse>>()
    val userLocationResponseLiveData: LiveData<NetworkResult<PreferenceResponse>>
        get() = userLocationLiveData

    suspend fun userLocation(userid: String, lat: String, lng: String, city: String) {
        userLocationLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.userLocation(userid, lat,lng,city, Constant.version, Constant.platform)
        userLocationData(response)
    }

    private fun userLocationData(response: Response<PreferenceResponse>) {
        if (response.isSuccessful && response.body() != null) {
            userLocationLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            userLocationLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            userLocationLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //foodOutlet
    private val foodOutletLiveData = MutableLiveData<NetworkResult<GetFoodResponse>>()
    val foodOutletResponseLiveData: LiveData<NetworkResult<GetFoodResponse>>
        get() = foodOutletLiveData

    suspend fun foodOutlet(
        userid: String,
        ccode: String,
        bookingid: String,
        booking_id: String,
        cbookid: String,
        type: String,
        transid: String,
        audi: String,
        seat: String,
        qr: String,
        iserv: String,
        isSpi: String
    ) {
        foodOutletLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.foodOutlet(userid, ccode,bookingid,booking_id,cbookid,type,transid,audi,seat,qr,iserv,isSpi, Constant.version, Constant.platform)
        foodOutletData(response)
    }

    private fun foodOutletData(response: Response<GetFoodResponse>) {
        if (response.isSuccessful && response.body() != null) {
            foodOutletLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            foodOutletLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            foodOutletLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /**************** Passport Recurring    ****************/

    private val recurringInitLiveData = MutableLiveData<NetworkResult<RecurringInitResponse>>()
    val recurringInitResponseLiveData: LiveData<NetworkResult<RecurringInitResponse>>
        get() = recurringInitLiveData

    suspend fun recurringInit(userid: String, bookingid: String) {
        preferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.recurringInit(userid, bookingid, Constant.version, Constant.platform,Constant.getDid())
        recurringInitData(response)
    }

    private fun recurringInitData(response: Response<RecurringInitResponse>) {
        if (response.isSuccessful && response.body() != null) {
            recurringInitLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            recurringInitLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            recurringInitLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private val recurringBinLiveData = MutableLiveData<NetworkResult<RecurringInitResponse>>()
    val recurringBinResponseLiveData: LiveData<NetworkResult<RecurringInitResponse>>
        get() = recurringBinLiveData

    suspend fun recurringBinCheck(userid: String, bookingid: String, token: String, bin: String, vpa: String) {
        preferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.recurringBinCheck(userid, bookingid,token,bin.trim().replace(" ".toRegex(),""),vpa ,Constant.version, Constant.platform,Constant.getDid())
        recurringBinData(response)
    }

    private fun recurringBinData(response: Response<RecurringInitResponse>) {
        if (response.isSuccessful && response.body() != null) {
            recurringBinLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            recurringBinLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            recurringBinLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

   //   Promo code list

    private val promoListLiveData = MutableLiveData<NetworkResult<PromoCodeList>>()
    val promoListResponseLiveData: LiveData<NetworkResult<PromoCodeList>>
        get() = promoListLiveData

    suspend fun promoList() {
        preferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.getPromoList(Constant.version, Constant.platform,Constant.getDid())
        promoListData(response)
    }

    private fun promoListData(response: Response<PromoCodeList>) {
        if (response.isSuccessful && response.body() != null) {
            promoListLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            promoListLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            promoListLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    /***********   Parking       ***********************/

    private val bookParkingLiveData = MutableLiveData<NetworkResult<ParkingResponse>>()
    val bookParkingResponseLiveData: LiveData<NetworkResult<ParkingResponse>>
        get() = bookParkingLiveData

    suspend fun bookParking(bookingid: String) {
        preferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.bookParking(bookingid,Constant.version, Constant.platform,Constant.getDid())
        bookParkingData(response)
    }

    private fun bookParkingData(response: Response<ParkingResponse>) {
        if (response.isSuccessful && response.body() != null) {
            bookParkingLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            bookParkingLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            bookParkingLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val showParkingLiveData = MutableLiveData<NetworkResult<ParkingResponse>>()
    val showParkingResponseLiveData: LiveData<NetworkResult<ParkingResponse>>
        get() = showParkingLiveData

    suspend fun showParking(bookingid: String) {
        preferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.viewParking(bookingid,Constant.version, Constant.platform,Constant.getDid())
        showParkingData(response)
    }

    private fun showParkingData(response: Response<ParkingResponse>) {
        if (response.isSuccessful && response.body() != null) {
            showParkingLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            showParkingLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            showParkingLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    /************         TICKET FROM HISTORY      ***********/
    private val fnbTicketLiveData = MutableLiveData<NetworkResult<TicketBookedResponse>>()
    val fnbTicketResponseLiveData: LiveData<NetworkResult<TicketBookedResponse>>
        get() = fnbTicketLiveData

    suspend fun fnbTicket(bookingid: String,userid: String,booktype: String) {
        preferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.fnbTicket(bookingid,userid,booktype,"0",Constant.version, Constant.platform,Constant.getDid())
        foodTicketData(response)
    }

    private fun foodTicketData(response: Response<TicketBookedResponse>) {
        if (response.isSuccessful && response.body() != null) {
            fnbTicketLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            fnbTicketLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            fnbTicketLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val singleTicketLiveData = MutableLiveData<NetworkResult<TicketBookedResponse>>()
    val singleTicketResponseLiveData: LiveData<NetworkResult<TicketBookedResponse>>
        get() = singleTicketLiveData

    suspend fun singleTicket(bookingid: String,userid: String,booktype: String) {
        preferenceLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.singleTicket(bookingid,userid,booktype,"0",Constant.version, Constant.platform,Constant.getDid())
        singleTicketData(response)
    }

    private fun singleTicketData(response: Response<TicketBookedResponse>) {
        if (response.isSuccessful && response.body() != null) {
            singleTicketLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            singleTicketLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            singleTicketLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private val extendTimeLiveData = MutableLiveData<NetworkResult<ExtendTimeResponse>>()
    val extendTimeResponseLiveData: LiveData<NetworkResult<ExtendTimeResponse>>
        get() = extendTimeLiveData

    suspend fun extendTime(transid: String, bookingid: String, cinemacode: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.extendTime(transid, bookingid, cinemacode, Constant.version, Constant.platform)
        extendTimeResponse(response)
    }

    private fun extendTimeResponse(response: Response<ExtendTimeResponse>) {
        if (response.isSuccessful && response.body() != null) {
            extendTimeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            extendTimeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            extendTimeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    /***************** JUS PAY ***************************/


    private val initJusPayLiveData = MutableLiveData<NetworkResult<JusPayInitResponse>>()
    val initJusPayResponseLiveData: LiveData<NetworkResult<JusPayInitResponse>>
        get() = initJusPayLiveData

    suspend fun initJusPay(userId: String, bookingid: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.initJusPay(userId, bookingid, Constant.version, Constant.platform,Constant.getDid())
        initJusPayResponse(response)
    }

    private fun initJusPayResponse(response: Response<JusPayInitResponse>) {
        if (response.isSuccessful && response.body() != null) {
            initJusPayLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            initJusPayLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            initJusPayLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
    /***************** GO DIGITAL ***************************/


    private val augOfferLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val augOfferResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = augOfferLiveData

    suspend fun augOffer(userId: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.augOffer(userId, "0.0", "0.0",Constant.version, Constant.platform,Constant.getDid())
        augOfferResponse(response)
    }

    private fun augOfferResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            augOfferLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            augOfferLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            augOfferLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private val capLiveData = MutableLiveData<NetworkResult<CaptchaResponse>>()
    val capResponseLiveData: LiveData<NetworkResult<CaptchaResponse>>
        get() = capLiveData

    suspend fun verifyResponse(secret: String,response: String) {
        capLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.verifyResponse(secret,response)
        capOfferResponse(response)
    }

    private fun capOfferResponse(response: Response<CaptchaResponse>) {
        if (response.isSuccessful && response.body() != null) {
            capLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            capLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            capLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /***************     MOBIKWIK API   ***************/

    private val mobikwikOTPLiveData = MutableLiveData<NetworkResult<MobikwikOTPResponse>>()
    val mobikwikOTPResponseLiveData: LiveData<NetworkResult<MobikwikOTPResponse>>
        get() = mobikwikOTPLiveData

    suspend fun mobikwikOTP(userId: String, bookingid: String, transid: String, booktype: String,mobile: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.mobikwikOTP(userId, bookingid,transid,booktype,mobile, Constant.version, Constant.platform,Constant.getDid())
        mobikwikOTPResponse(response)
    }

    private fun mobikwikOTPResponse(response: Response<MobikwikOTPResponse>) {
        if (response.isSuccessful && response.body() != null) {
            mobikwikOTPLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            mobikwikOTPLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            mobikwikOTPLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private val mobikwikPayLiveData = MutableLiveData<NetworkResult<MobiKwikPayResponse>>()
    val mobikwikPayResponseLiveData: LiveData<NetworkResult<MobiKwikPayResponse>>
        get() = mobikwikPayLiveData

    suspend fun mobikwikPay(userId: String, bookingid: String, transid: String, booktype: String,mobile: String,otp: String,cinemacode: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.mobikwikPAY(userId, bookingid,transid,booktype,mobile,otp,cinemacode, Constant.version, Constant.platform,Constant.getDid())
        mobikwikPayResponse(response)
    }

    private fun mobikwikPayResponse(response: Response<MobiKwikPayResponse>) {
        if (response.isSuccessful && response.body() != null) {
            mobikwikPayLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            mobikwikPayLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            mobikwikPayLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private val mobikwikCreateWalletLiveData = MutableLiveData<NetworkResult<MobiKwikPayResponse>>()
    val mobikwikCreateWalletResponseLiveData: LiveData<NetworkResult<MobiKwikPayResponse>>
        get() = mobikwikCreateWalletLiveData

    suspend fun mobikwikCreateWallet(userId: String, bookingid: String, transid: String, booktype: String,mobile: String,otp: String,email: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.mobikwikCreateWallet(userId, bookingid,transid,booktype,mobile,otp,email, Constant.version, Constant.platform,Constant.getDid())
        mobikwikCreateWalletResponse(response)
    }

    private fun mobikwikCreateWalletResponse(response: Response<MobiKwikPayResponse>) {
        if (response.isSuccessful && response.body() != null) {
            mobikwikCreateWalletLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            mobikwikCreateWalletLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            mobikwikCreateWalletLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private val mobikwikCheckSumLiveData = MutableLiveData<NetworkResult<MobiKwikCheckSumResponse>>()
    val mobikwikCheckSumResponseLiveData: LiveData<NetworkResult<MobiKwikCheckSumResponse>>
        get() = mobikwikCheckSumLiveData

    suspend fun mobikwikCheckSum(userId: String, bookingid: String, transid: String, booktype: String,mobile: String,otp: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.mobikwikChecksum(userId, bookingid,transid,booktype,mobile,otp, Constant.version, Constant.platform,Constant.getDid())
        mobikwikCheckSumResponse(response)
    }

    private fun mobikwikCheckSumResponse(response: Response<MobiKwikCheckSumResponse>) {
        if (response.isSuccessful && response.body() != null) {
            mobikwikCheckSumLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            mobikwikCheckSumLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            mobikwikCheckSumLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /***********   BANK OFFER ************/

    private val bankOfferLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val bankOfferResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = bankOfferLiveData

    suspend fun bankOffer(userId: String, bookingid: String, transid: String, booktype: String,scheme: String,cardNo: String,binOffer: String,paymentType: String) {
        bankOfferLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.bankOffer(userId, bookingid,booktype,transid,scheme,cardNo.replace(" ".toRegex(),""),binOffer,paymentType, Constant.version, Constant.platform,Constant.getDid())
        bankOfferResponse(response)
    }

    private fun bankOfferResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            bankOfferLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            bankOfferLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            bankOfferLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /***************** FEEDBACK FOR MOVIE AND CINEMA ***************************/


    private val getFeedBackDataLiveData = MutableLiveData<NetworkResult<FeedbackDataResponse>>()
    val getFeedBackDataResponseLiveData: LiveData<NetworkResult<FeedbackDataResponse>>
        get() = getFeedBackDataLiveData

    suspend fun getFeedBackData(userId: String, type: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.getFeedBackData(userId, type, Constant.version, Constant.platform,Constant.getDid())
        getFeedBackDataResponse(response)
    }

    private fun getFeedBackDataResponse(response: Response<FeedbackDataResponse>) {
        if (response.isSuccessful && response.body() != null) {
            getFeedBackDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            getFeedBackDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            getFeedBackDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val setFeedBackDataLiveData = MutableLiveData<NetworkResult<FeedbackDataResponse>>()
    val setFeedBackDataResponseLiveData: LiveData<NetworkResult<FeedbackDataResponse>>
        get() = setFeedBackDataLiveData

    suspend fun setFeedBackData(userId: String, type: String,code:String,text: String,tags:String,comment: String,ccode:String,bookingid: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.setFeedBackData(userId, type,code,text,tags,comment,ccode,bookingid, Constant.version, Constant.platform,Constant.getDid())
        setFeedBackDataResponse(response)
    }

    private fun setFeedBackDataResponse(response: Response<FeedbackDataResponse>) {
        if (response.isSuccessful && response.body() != null) {
            setFeedBackDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            setFeedBackDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            setFeedBackDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /****************** FREECHARGE PAY ****************/
    private val freechargeOTPLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val freechargeOTPResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = freechargeOTPLiveData

    suspend fun freechargeOTP(userId: String, transid: String,booktype:String,bookingid: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.freechargeOTP(userId, transid,booktype,bookingid, Constant.version, Constant.platform,Constant.getDid())
        freechargeOTPResponse(response)
    }

    private fun freechargeOTPResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            freechargeOTPLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            freechargeOTPLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            freechargeOTPLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val freechargeLoginLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val freechargeLoginResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = freechargeLoginLiveData

    suspend fun freechargeLogin(userId: String, transid: String,booktype:String,bookingid: String,otp: String,otpId: String) {
        freechargeLoginLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.freechargeLogin(userId, transid,booktype,bookingid,otp,otpId, Constant.version, Constant.platform,Constant.getDid())
        freechargeLoginResponse(response)
    }

    private fun freechargeLoginResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            freechargeLoginLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                freechargeLoginLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                freechargeLoginLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            freechargeLoginLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val freechargeResendLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val freechargeResendResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = freechargeResendLiveData

    suspend fun freechargeResend(userId: String, transid: String,booktype:String,bookingid: String,otpId: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.freechargeResend(userId, transid,booktype,bookingid,otpId, Constant.version, Constant.platform,Constant.getDid())
        freechargeResendResponse(response)
    }

    private fun freechargeResendResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            freechargeResendLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                freechargeResendLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                freechargeResendLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            freechargeResendLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val freechargeDetailLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val freechargeDetailResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = freechargeDetailLiveData

    suspend fun freechargeDetail(userId: String, transid: String,booktype:String,bookingid: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.freechargeDetail(userId, transid,booktype,bookingid, Constant.version, Constant.platform,Constant.getDid())
        freechargeDetailResponse(response)
    }

    private fun freechargeDetailResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            freechargeDetailLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                freechargeDetailLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                freechargeDetailLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            freechargeDetailLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private val freechargePaymentLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val freechargePaymentResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = freechargePaymentLiveData

    suspend fun freechargePayment(userId: String, transid: String,booktype:String,bookingid: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.freechargePayment(userId, transid,booktype,bookingid,"false" ,Constant.version, Constant.platform,Constant.getDid())
        freechargePaymentResponse(response)
    }

    private fun freechargePaymentResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            freechargePaymentLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                freechargePaymentLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:JSONException){
                freechargePaymentLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            freechargePaymentLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private val freechargeAddMoneyLiveData = MutableLiveData<NetworkResult<PaytmHmacResponse>>()
    val freechargeAddMoneyResponseLiveData: LiveData<NetworkResult<PaytmHmacResponse>>
        get() = freechargeAddMoneyLiveData

    suspend fun freechargeAddMoney(userId: String, transid: String,booktype:String,bookingid: String,amount: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.freechargeAddMoney(userId, transid,booktype,bookingid,amount, Constant.version, Constant.platform,Constant.getDid())
        freechargeAddMoneyResponse(response)
    }

    private fun freechargeAddMoneyResponse(response: Response<PaytmHmacResponse>) {
        if (response.isSuccessful && response.body() != null) {
            freechargeAddMoneyLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                freechargeAddMoneyLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }catch (e:JSONException){
                freechargeAddMoneyLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
        } else {
            freechargeAddMoneyLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /**********
     * In Cinema Home
     */

    //In Cinema Home
    private val _inCinemaHomeResponseLiveData =
        MutableLiveData<NetworkResult<InCinemaHomeResponse>>()
    val inCinemaHomeResponseLiveData: LiveData<NetworkResult<InCinemaHomeResponse>>
        get() = _inCinemaHomeResponseLiveData

    suspend fun getInCinemaHome(userId: String, city: String) {
        _inCinemaHomeResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.inCinemaHome(
            userId,
            city,
            Constant.getDid(),
            Constant.platform,
            Constant.getDid()
        )
        inCinemaHomeResponse(response)
    }

    private fun inCinemaHomeResponse(response: Response<InCinemaHomeResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _inCinemaHomeResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _inCinemaHomeResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } catch (e: JSONException) {
                _inCinemaHomeResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            _inCinemaHomeResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    /***
     * In cinema with booking ID
     */

    private val _getInCinemaResponseLiveData = MutableLiveData<NetworkResult<GetInCinemaResponse>>()
    val getInCinemaResponseLiveData: LiveData<NetworkResult<GetInCinemaResponse>>
        get() = _getInCinemaResponseLiveData

    private val _getBookingResponseLiveData = MutableLiveData<NetworkResult<GetBookingResponse>>()
    val getBookingResponseLiveData: LiveData<NetworkResult<GetBookingResponse>>
        get() = _getBookingResponseLiveData

    suspend fun getInCinema(userId: String, city: String) {
        _getInCinemaResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.getInCinemaBookingID(
            userid = userId,
            city = city,
            version = Constant.version,
            platform = Constant.platform,
            did = Constant.getDid()
        )
        processInCinemaResponse(response)
    }

    suspend fun getBooking(bookingId: String,city: String) {
        _getBookingResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.getBooking(bookingId=bookingId,city=city, platform = Constant.platform, version = Constant.version)
        processBookingResponse(response)
    }

    private fun processInCinemaResponse(response: Response<GetInCinemaResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _getInCinemaResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _getInCinemaResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } catch (e: JSONException) {
                _getInCinemaResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            _getInCinemaResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
    private fun processBookingResponse(response: Response<GetBookingResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _getBookingResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _getBookingResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } catch (e: JSONException) {
                _getBookingResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            _getBookingResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

}