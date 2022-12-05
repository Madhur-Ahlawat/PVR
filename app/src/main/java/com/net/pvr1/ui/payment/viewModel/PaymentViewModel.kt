package com.net.pvr1.ui.payment.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.payment.response.PaymentResponse
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
}