package com.net.pvr1.ui.payment.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.ui.payment.response.PaytmHmacResponse
import com.net.pvr1.ui.payment.response.PhonepeHmacResponse
import com.net.pvr1.ui.payment.response.UPIStatusResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //voucher
//    val userResponseLiveData: LiveData<NetworkResult<CouponResponse>>
//        get() = userRepository.voucherResponseLiveData
//
//    fun voucher(
//        mobile: String,
//        userid: String,
//        city: String,
//        status: String,
//        pay: String,
//        did: String,
//        timestamp: String
//    ) {
//        viewModelScope.launch {
//            userRepository.coupons(mobile, userid, city, status, pay, did, timestamp)
//        }
//    }

    //payMode
    val payModeResponseLiveData: LiveData<NetworkResult<PaymentResponse>>
        get() = userRepository.payModeResponseLiveData

    fun payMode(
        cinemacode: String,
        booktype: String,
        userid: String,
        mobile: String,
        type: String,
        isSpi: String,
        srilanka: String,
        unpaid: Boolean
    ) {
        viewModelScope.launch {
            userRepository.payMode(cinemacode, booktype, userid, mobile, type, isSpi, srilanka,unpaid)
        }
    }

    //Upi Status
    val upiStatusResponseLiveData: LiveData<NetworkResult<UPIStatusResponse>>
        get() = userRepository.upiStatusResponseLiveData

    fun upiStatus(
        bookingid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.upiStatus(bookingid, booktype)
        }
    }

    //paytm
    val liveDataScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.paytmHmaResponseLiveData

    fun paytmHMAC(
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


    //Phonepe Hmac
    val phonepeLiveDataScope: LiveData<NetworkResult<PhonepeHmacResponse>> get() = userRepository.phonepeHmacResponseLiveData

    fun phonepeHMAC(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
    ) {
        viewModelScope.launch {
            userRepository.phonepeHmac(userid, bookingid,booktype,transid)
        }
    }

    //Phonepe Status
    val phonepeStatusLiveDataScope: LiveData<NetworkResult<UPIStatusResponse>> get() = userRepository.phonepeStatusResponseLiveData

    fun phonepeStatus(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
    ) {
        viewModelScope.launch {
            userRepository.phonepeStatus(userid, bookingid,booktype,transid)
        }
    }

    //CRED Check
    val credCheckLiveDataScope: LiveData<NetworkResult<UPIStatusResponse>> get() = userRepository.credCheckResponseLiveData

    fun credCheck(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        unpaid: String,
        cred_present: String,
        spi: String,
    ) {
        viewModelScope.launch {
            userRepository.credCheck(userid, bookingid,booktype,transid,unpaid,cred_present,spi)
        }
    }//CRED Hmac
    val credHmacLiveDataScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.credHmacResponseLiveData

    fun credHmac(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        unpaid: String,
        cred_present: String,
        spi: String,
        ptype: String
    ) {
        viewModelScope.launch {
            userRepository.credHmac(userid, bookingid,booktype,transid,unpaid,cred_present,spi,ptype)
        }
    }//CRED Status
    val credStatusLiveDataScope: LiveData<NetworkResult<UPIStatusResponse>> get() = userRepository.credStatusResponseLiveData

    fun credStatus(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
    ) {
        viewModelScope.launch {
            userRepository.credStatus(userid, bookingid,booktype,transid)
        }
    }
}