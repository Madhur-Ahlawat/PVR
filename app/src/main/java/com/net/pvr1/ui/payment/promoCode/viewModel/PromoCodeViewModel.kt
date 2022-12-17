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

    //getBalance
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

    //POST PAID PAY
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

    //POST PAID SEND OTP
    val liveDatasendOTPScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.postpaidSendOTPResponseLiveData

    fun postPaidSendOTP(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.postPaidSendOTP(userid, bookingid, booktype, transid)
        }
    }


    //POST PAID VERIFY OTP
    val liveDataverifyOTPScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.postpaidVerifyOTPResponseLiveData

    fun postPaidVerifYOTP(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        otp: String
    ) {
        viewModelScope.launch {
            userRepository.postPaidVerifYOTP(userid, bookingid, booktype, transid,otp)
        }
    }

    //POST PAID VERIFY OTP
    val liveDataMakePaymentScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.postpaidMakePaymentResponseLiveData

    fun postPaidMakePayment(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.postPaidMakePayment(userid, bookingid, booktype, transid)
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