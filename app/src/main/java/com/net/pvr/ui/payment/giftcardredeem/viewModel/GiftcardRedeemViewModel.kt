package com.net.pvr.ui.payment.giftcardredeem.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.payment.response.PaytmHmacResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiftcardRedeemViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    //getBalance
    val livezagglePayScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.zaggalePayResponseLiveData

    fun zagglePay(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        cardNo: String,
        pin:String,
        type:String
    ) {
        viewModelScope.launch {
            userRepository.zaggalePay(userid, bookingid, booktype, transid,cardNo,pin,type)
        }
    }

    //POST PAID PAY
    val liveDataGiftCardRedeemScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.giftCardRedeemResponseLiveData

    fun giftCardRedeem(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        cardNo: String,
        pin:String,
        type:String
    ) {
        viewModelScope.launch {
            userRepository.giftCardRedeem(userid, bookingid, booktype, transid,cardNo,pin,type)
        }
    }
  //POST PAID PAY
    val capGiftCardRedeemScope: LiveData<NetworkResult<String>> get() = userRepository.capResponseLiveData

    fun verifyResponse(
        secret: String,response: String
    ) {
        viewModelScope.launch {
            userRepository.verifyResponse(secret, response)
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