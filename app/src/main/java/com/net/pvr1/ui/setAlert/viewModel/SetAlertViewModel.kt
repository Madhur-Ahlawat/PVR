package com.net.pvr1.ui.setAlert.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr1.utils.NetworkResult
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
}