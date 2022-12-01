package com.net.pvr1.ui.payment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr1.ui.payment.response.CouponResponse
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    //voucher
    val userResponseLiveData: LiveData<NetworkResult<CouponResponse>> get() = userRepository.couponsResponseLiveData

    fun coupons(
        mobile: String,
        city: String,
        status: String,
        pay: Boolean,
        did: String
    ) {
        viewModelScope.launch {
            userRepository.coupons(mobile, city, status, pay, did)
        }
    }

    //payMode
    val payResponseLiveData: LiveData<NetworkResult<PaymentResponse>> get() = userRepository.payModeResponseLiveData

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
            userRepository.payMode(
                cinemacode,
                booktype,
                userid,
                mobile,
                type,
                isSpi,
                srilanka,
                unpaid
            )
        }
    }

}