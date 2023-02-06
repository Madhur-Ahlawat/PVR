package com.net.pvr1.ui.login.otpVerify.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.summery.response.ExtendTimeResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpVerifyViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<ResisterResponse>>
        get() = userRepository.otpVerifyResponseLiveData

    fun otpVerify(mobile: String, token: String) {
        viewModelScope.launch {
            userRepository.otpVerify(mobile, token)
        }
    }

    val userResponseResLiveData: LiveData<NetworkResult<ResisterResponse>>
        get() = userRepository.resisterResponseLiveData

    fun resister(
        hash: String,
        name: String,
        email: String,
        mobile: String,
        otp: String,
        city: String,
        cname: Boolean
    ) {
        viewModelScope.launch {
            userRepository.resister(
                hash,
                email, name, mobile, otp, city, cname
            )
        }
    }
//Resend Otp
    val resendResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = userRepository.userResponseLiveData

    fun loginMobileUser(mobile: String, city: String, cName: String) {
        viewModelScope.launch {
            userRepository.loginMobile(mobile,city,cName)
        }
    }

    fun cancelTrans(
        cinemacode: String, transid: String, bookingid: String
    ) {
        viewModelScope.launch {
            userRepository.cancelTrans(cinemacode, transid, bookingid)
        }
    }



    //extend Time

    val extendTimeLiveData: LiveData<NetworkResult<ExtendTimeResponse>>
        get() = userRepository.extendTimeResponseLiveData

    fun extendTime(transid: String, bookingid: String, cinemacode: String) {
        viewModelScope.launch {
            userRepository.extendTime(transid,bookingid,cinemacode)
        }
    }

    //Loyalty Home

    val privilegeHomeResponseLiveData: LiveData<NetworkResult<PrivilegeHomeResponse>>
        get() = userRepository.privilegeHomeResponseLiveData

    fun privilegeHome(
        mobile: String,
        city: String
    ) {
        viewModelScope.launch {
            userRepository.privilegeHomeData(mobile,city)
        }
    }
}