package com.net.pvr1.ui.payment.promoCode.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr1.ui.payment.response.CouponResponse
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.ui.payment.response.PaytmHmacResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromoCodeViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    //PROMOCODE
    val livePromoCodeScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.promoCodeResponseLiveData

    fun promoCode(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        promocode:String
    ) {
        viewModelScope.launch {
            userRepository.promoCode(userid, bookingid, booktype, transid,promocode)
        }
    }

    //POST GYTER
    val liveDatapromoGyftScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.promoGyftResponseLiveData

    fun promoGyft(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        promocode: String
    ) {
        viewModelScope.launch {
            userRepository.promoGyft(userid, bookingid, booktype, transid,promocode)
        }
    }

    //POST accentive
    val accentivePromoOTPScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.accentivePromoResponseLiveData

    fun accentivePromo(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        promocode: String

    ) {
        viewModelScope.launch {
            userRepository.accentivePromo(userid, bookingid, booktype, transid,promocode)
        }
    }


    //POST HYATT SEND OTP
    val hyattSendOTPScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.hyattSendOTPResponseLiveData

    fun hyattSendOTP(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        mobile: String
    ) {
        viewModelScope.launch {
            userRepository.hyattSendOTP(userid, bookingid, booktype, transid,mobile)
        }
    }

    //POST HYATT VERIFY OTP
    val hyattVerifyOTPScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.verfyOTPHYATTResponseLiveData

    fun hyattVerifyOTP(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        mobile: String,
        otp: String
    ) {
        viewModelScope.launch {
            userRepository.verfyOTPHYATT(userid, bookingid, booktype, transid,mobile,otp)
        }
    }

    //POST PAID VERIFY OTP
    val liveDatapaytmHmacOldScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.paytmHmacOldResponseLiveData

    fun postPaidpaytmHmacOld(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.paytmHmacOld(userid, bookingid, booktype, transid)
        }
    }

    //E_pay_Latter
    val liveDataEPAYScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.paytmHmaResponseLiveData

    fun ePayLatter(
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
        viewModelScope.launch {
            userRepository.paytmHMAC(userid, bookingid, transid, unpaid, cardNo,booktype,ptype,isSpi,binOffer)
        }
    }

}