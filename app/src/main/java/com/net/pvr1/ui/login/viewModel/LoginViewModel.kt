package com.net.pvr1.ui.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.summery.response.ExtendTimeResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<LoginResponse>>
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


}