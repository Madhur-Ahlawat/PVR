package com.net.pvr.ui.home.fragment.more.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.fragment.more.response.ProfileResponse
import com.net.pvr.ui.home.fragment.more.response.WhatsAppOptStatus
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    //opt Check
    val userResponseLiveData: LiveData<NetworkResult<WhatsAppOptStatus>>
        get() = userRepository.whatsappOptResponseLiveData

    fun whatsappOpt(
        userid: String, mobile: String, timestamp: String, token: String
    ) {
        viewModelScope.launch {
            userRepository.whatsappOpt(userid, mobile, timestamp, token)
        }
    }

    //opt Check In
    val whatsappOptInLiveData: LiveData<NetworkResult<WhatsAppOptStatus>>
        get() = userRepository.whatsappOptResponseInLiveData

    fun whatsappOptIn(
        userid: String, mobile: String, timestamp: String, token: String
    ) {
        viewModelScope.launch {
            userRepository.whatsappOptIn(userid, mobile, timestamp, token)
        }
    }


    //opt Check Out
    val whatsappOptOutLiveData: LiveData<NetworkResult<WhatsAppOptStatus>>
        get() = userRepository.whatsappOptResponseOutLiveData

    fun whatsappOptOut(
        userid: String, mobile: String, timestamp: String, token: String
    ) {
        viewModelScope.launch {
            userRepository.whatsappOptOut(userid, mobile, timestamp, token)
        }
    }

    //UserProfile
    val userProfileLiveData: LiveData<NetworkResult<ProfileResponse>>
        get() = userRepository.userProfileResponseOutLiveData

    fun userProfile(
        city: String, userid: String, timestamp: String, token: String
    ) {
        viewModelScope.launch {
            userRepository.userProfile(city, userid, timestamp,token)
        }
    }


}