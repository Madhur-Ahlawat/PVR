package com.net.pvr1.ui.bookingSession.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<BookingResponse>>
    get() = userRepository.bookingSessionResponseLiveData

    fun bookingTicket(city:String,mid:String,lat:String,lng:String,date:String,isSpi:String,srilanka:String,userid:String,) {
        viewModelScope.launch {
            userRepository.bookingTicket(city,mid,lat,lng,date,isSpi,srilanka,userid)
        }
    }

    val userResponseTheatreLiveData: LiveData<NetworkResult<BookingTheatreResponse>>
    get() = userRepository.bookingTheatreResponseLiveData

    fun bookingTheatre(city:String,cid:String,userid:String,mid:String,isSpi:String) {
        viewModelScope.launch {
            userRepository.bookingTheatre(city,cid,userid,mid,isSpi)
        }
    }


}