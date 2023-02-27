package com.net.pvr.ui.player.viewModel

import androidx.lifecycle.ViewModel
import com.net.pvr.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

//    val userResponseLiveData: LiveData<NetworkResult<ResisterResponse>>
//    get() = userRepository.otpVerifyResponseLiveData
//
//    fun otpVerify(mobile: String,token: String) {
//        viewModelScope.launch {
//            userRepository.otpVerify(mobile,token)
//        }
//    }

}