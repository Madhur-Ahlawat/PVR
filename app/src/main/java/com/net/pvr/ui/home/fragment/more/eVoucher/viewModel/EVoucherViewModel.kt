package com.net.pvr.ui.home.fragment.more.eVoucher.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.fragment.more.eVoucher.response.VoucherListResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EVoucherViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
////my Voucher
    val userResponseLiveData: LiveData<NetworkResult<VoucherListResponse>>
        get() = userRepository.myVoucherResponseLiveData

    fun myVouchers(userid: String) {
        viewModelScope.launch {
            userRepository.myVoucher(userid)
        }
    }

    //myE Voucher
    val userResponseEVoucherLiveData: LiveData<NetworkResult<VoucherListResponse>>
        get() = userRepository.myeVoucherResponseLiveData

    fun myEVouchers(cname: String,cat: String) {
        viewModelScope.launch {
            userRepository.myEVoucher(cname,cat)
        }
    }



}