package com.net.pvr1.ui.myBookings.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.ui.myBookings.response.GiftCardResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBookingViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
//GiftCard
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

}