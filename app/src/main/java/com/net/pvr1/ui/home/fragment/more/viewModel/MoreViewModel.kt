package com.net.pvr1.ui.home.fragment.more.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.more.response.WhatsAppOptStatus
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
   //opt Check
    val userResponseLiveData: LiveData<NetworkResult<WhatsAppOptStatus>>
        get() = userRepository.whatsappOptResponseLiveData

    fun whatsappOpt(
        userid:String,mobile:String,timestamp:String,token:String
    ) {
        viewModelScope.launch { userRepository.whatsappOpt(userid,mobile,timestamp,token)
        }
    }
    //opt Check In
    val whatsappOptInLiveData: LiveData<NetworkResult<WhatsAppOptStatus>>
        get() = userRepository.whatsappOptResponseInLiveData

    fun whatsappOptIn(
        userid:String,mobile:String,timestamp:String,token:String
    ) {
        viewModelScope.launch { userRepository.whatsappOptIn(userid,mobile,timestamp,token)
        }
    }


    //opt Check Out
    val whatsappOptOutLiveData: LiveData<NetworkResult<WhatsAppOptStatus>>
        get() = userRepository.whatsappOptResponseOutLiveData

    fun whatsappOptOut(
        userid:String,mobile:String,timestamp:String,token:String
    ) {
        viewModelScope.launch { userRepository.whatsappOptOut(userid,mobile,timestamp,token)
        }
    }

}