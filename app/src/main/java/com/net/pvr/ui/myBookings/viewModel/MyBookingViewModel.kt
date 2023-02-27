package com.net.pvr.ui.myBookings.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr.ui.myBookings.response.FoodTicketResponse
import com.net.pvr.ui.myBookings.response.GiftCardResponse
import com.net.pvr.ui.myBookings.response.ParkingResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBookingViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    //Book Parking
    val bookParkResponseLiveData: LiveData<NetworkResult<ParkingResponse>>
    get() = userRepository.bookParkingResponseLiveData

    fun bookParking(bookingId: String) {
        viewModelScope.launch {
            userRepository.bookParking(bookingId)
        }
    }

    //View Parking
    val showParkingResponseLiveData: LiveData<NetworkResult<ParkingResponse>>
    get() = userRepository.showParkingResponseLiveData

    fun showParking(bookingId: String) {
        viewModelScope.launch {
            userRepository.showParking(bookingId)
        }
    }//GiftCard
    val userResponseLiveData: LiveData<NetworkResult<GiftCardResponse>>
    get() = userRepository.giftCardResponseLiveData

    fun giftCard(userId: String, did: String) {
        viewModelScope.launch {
            userRepository.giftCard(userId,did)
        }
    }
    //Ticket & Food
    val ticketFoodLiveData: LiveData<NetworkResult<FoodTicketResponse>>
        get() = userRepository.foodTicketResponseLiveData

    fun foodTicket(userId: String, did: String, sriLanka: String, city: String, isSpi: String, past: String) {
        viewModelScope.launch {
            userRepository.foodTicket(userId,did,sriLanka,city,isSpi,past)
        }
    }

    //resend Otp
    val resendMailLiveData: LiveData<NetworkResult<FoodTicketResponse>>
        get() = userRepository.resendMailResponseLiveData

    fun resendMail(userId: String, bookingId: String, type: String) {
        viewModelScope.launch {
            userRepository.resendMail(userId,bookingId,type)
        }
    }

    // Movies

    val userResponseTheatreLiveData: LiveData<NetworkResult<BookingTheatreResponse>>
        get() = userRepository.bookingTheatreResponseLiveData

    fun bookingTheatre(city:String,cid:String,userid:String,mid:String,lng:String,isSpi:String) {
        viewModelScope.launch {
            userRepository.bookingTheatre(city,cid,userid,mid,lng,isSpi)
        }
    }

}