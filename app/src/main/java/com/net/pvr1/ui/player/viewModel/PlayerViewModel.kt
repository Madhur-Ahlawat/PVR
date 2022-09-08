package com.net.pvr1.ui.player.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.otpVerify.response.ResisterResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<ResisterResponse>>
    get() = userRepository.otpVerifyResponseLiveData

    fun otpVerify(mobile: String,token: String) {
        viewModelScope.launch {
            userRepository.otpVerify(mobile,token)
        }
    }

}