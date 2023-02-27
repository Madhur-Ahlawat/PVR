package com.net.pvr.ui.bookingSession.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<BookingResponse>>
    get() = userRepository.bookingSessionResponseLiveData

    fun bookingTicket(city:String,mid:String,lat:String,lng:String,date:String,isSpi:String,srilanka:String,userid:String,lang: String,
                      format: String,
                      price: String,
                      hc: String,
                      time: String,
                      cinetype: String,
                      special: String) {
        viewModelScope.launch {
            userRepository.bookingTicket(city,mid,lat,lng,date,isSpi,srilanka,userid,lang,format,price,hc,time,cinetype,special)
        }
    }


    val userResponseTheatreLiveData: LiveData<NetworkResult<BookingTheatreResponse>>
    get() = userRepository.bookingTheatreResponseLiveData

    fun bookingTheatre(city:String,cid:String,userid:String,mid:String,lng:String,isSpi:String) {
        viewModelScope.launch {
            userRepository.bookingTheatre(city,cid,userid,mid,lng,isSpi)
        }
    }


}