package com.net.pvr.ui.payment.paytmpostpaid.viewModel

import android.icu.util.CurrencyAmount
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.payment.mobikwik.response.MobiKwikCheckSumResponse
import com.net.pvr.ui.payment.mobikwik.response.MobiKwikPayResponse
import com.net.pvr.ui.payment.mobikwik.response.MobikwikOTPResponse
import com.net.pvr.ui.payment.response.PaytmHmacResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaytmPostPaidViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    //getBalance
    val liveDataBalanceScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.postpaidHmacResponseLiveData

    fun getBalance(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.postpaidHmac(userid, bookingid, booktype, transid)
        }
    }

    //POST PAID PAY
    val liveDatapayScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.postpaidPAYResponseLiveData

    fun postPaidPay(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.postpaidPay(userid, bookingid, booktype, transid)
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


    //POST AIRTEL PAY
    val airtelPayHmacOldScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.airtelPayResponseLiveData

    fun airtelPay(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.airtelPay(userid, bookingid, booktype, transid)
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

    /************* PAYTM WALLET ******************/

    //getBalance
    val liveDataWalletScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.paytmWalletHmacResponseLiveData

    fun getWalletHmac(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.paytmWalletHmac(userid, bookingid, booktype, transid)
        }
    }

    //POST PAID SEND OTP
    val liveWalletsendOTPScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.paytmWalletSendOTPResponseLiveData

    fun walletSendOTP(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        mobile: String,
        email: String
    ) {
        viewModelScope.launch {
            userRepository.paytmWalletSendOTP(userid, bookingid, booktype, transid,mobile,email)
        }
    }


    //POST PAID VERIFY OTP
    val liveWalletverifyOTPScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.paytmWalletVerifyOTPResponseLiveData

    fun walletVerifYOTP(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        otp: String,
        state_text: String
    ) {
        viewModelScope.launch {
            userRepository.paytmWalletVerifYOTP(userid, bookingid, booktype, transid,otp,state_text)
        }
    }

    //POST PAID VERIFY OTP
    val liveWalletMakePaymentScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.paytmWalletMakePaymentResponseLiveData

    fun walletMakePayment(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.paytmWalletMakePayment(userid, bookingid, booktype, transid)
        }
    }

    //POST PAID PAY
    val liveWalletpayScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.walletpaidPAYResponseLiveData

    fun walletPay(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.walletPay(userid, bookingid, booktype, transid)
        }
    }


    /***********     MOBIKWIK API  ***************/
    val mobikwikOTPpayScope: LiveData<NetworkResult<MobikwikOTPResponse>> get() = userRepository.mobikwikOTPResponseLiveData
    fun mobikwikOTP(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        mobile:String
    ) {
        viewModelScope.launch {
            userRepository.mobikwikOTP(userid, bookingid, booktype, transid,mobile)
        }
    }

    val mobikwikPaypayScope: LiveData<NetworkResult<MobiKwikPayResponse>> get() = userRepository.mobikwikPayResponseLiveData
    fun mobikwikPay(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        mobile:String,
        otp:String,
        cinemacode:String
    ) {
        viewModelScope.launch {
            userRepository.mobikwikPay(userid, bookingid, booktype, transid,mobile,otp,cinemacode)
        }
    }


    val mobikwikCheckSumpayScope: LiveData<NetworkResult<MobiKwikCheckSumResponse>> get() = userRepository.mobikwikCheckSumResponseLiveData
    fun mobikwikCheckSum(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        mobile:String,
        otp:String
    ) {
        viewModelScope.launch {
            userRepository.mobikwikCheckSum(userid, bookingid, booktype, transid,mobile,otp)
        }
    }


    val mobikwikCreateWalletpayScope: LiveData<NetworkResult<MobiKwikPayResponse>> get() = userRepository.mobikwikCreateWalletResponseLiveData
    fun mobikwikCreateWallet(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        mobile:String,
        otp:String,
        email: String
    ) {
        viewModelScope.launch {
            userRepository.mobikwikCreateWallet(userid, bookingid, booktype, transid,mobile,otp,email)
        }
    }

    /****************** FREECHARGE PAY ****************/
    val freechargeOTPpayScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.freechargeOTPResponseLiveData
    fun freechargeOTP(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
    ) {
        viewModelScope.launch {
            userRepository.freechargeOTP(userid, transid, booktype, bookingid)
        }
    }

    val freechargeLoginpayScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.freechargeLoginResponseLiveData
    fun freechargeLogin(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        otp:String,
        otpId:String
    ) {
        viewModelScope.launch {
            userRepository.freechargeLogin(userid, transid, booktype, bookingid,otp,otpId)
        }
    }

    val freechargeResendpayScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.freechargeResendResponseLiveData
    fun freechargeResend(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        otpId:String
    ) {
        viewModelScope.launch {
            userRepository.freechargeResend(userid, transid, booktype, bookingid,otpId)
        }
    }

    val freechargeDetailpayScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.freechargeDetailResponseLiveData
    fun freechargeDetail(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.freechargeDetail(userid, transid, booktype, bookingid)
        }
    }

    val freechargePaymentpayScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.freechargePaymentResponseLiveData
    fun freechargePayment(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.freechargePayment(userid, transid, booktype, bookingid)
        }
    }

    val freechargeAddMoneypayScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.freechargeAddMoneyResponseLiveData
    fun freechargeAddMoney(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        amount: String
    ) {
        viewModelScope.launch {
            userRepository.freechargeAddMoney(userid, transid, booktype, bookingid,amount)
        }
    }

}