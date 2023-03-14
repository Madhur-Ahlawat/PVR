package com.net.pvr.ui.movieDetails.comingSoonDetails.setAlert.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr.ui.home.fragment.more.response.WhatsAppOptStatus
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetAlertViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {


    //Alert List
    val userResponseLiveData: LiveData<NetworkResult<BookingRetrievalResponse>>
        get() = userRepository.bookingRetrievalResponseLiveData

    fun allTheater(
        city: String,
        lat : String,
        lng: String,
        userid: String,
        searchtxt: String
    ) {
        viewModelScope.launch {
            userRepository.bookingRetrievalLayout(city,lat,lng,userid,searchtxt)
        }
    }
//setAlert
    val setAlertResponseLiveData: LiveData<NetworkResult<BookingRetrievalResponse>>
        get() = userRepository.setAlertResponseLiveData

    fun setAlert(
        userid: String,
        city: String,
        mcode : String,
        cinema: String,
        whtsapp: String,
        pushnotify: String,
        sms: String,
        email: String,
        did: String
    ) {
        viewModelScope.launch {
            userRepository.setAlertLayout(userid,city,mcode,cinema,whtsapp,pushnotify,sms,email,did)
        }
    }


    //opt Check
    val optResponseLiveData: LiveData<NetworkResult<WhatsAppOptStatus>>
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

}