package com.net.pvr1.ui.payment.cardDetails.viewModel

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
class CardDetailsViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    //voucher
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

}