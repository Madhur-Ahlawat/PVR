package com.net.pvr.ui.ticketConfirmation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.fragment.home.response.FeedbackDataResponse
import com.net.pvr.ui.myBookings.response.ParkingResponse
import com.net.pvr.ui.ticketConfirmation.response.TicketBookedResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketConfirmationViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //Ticket Confirmation
    val liveDataScope: LiveData<NetworkResult<TicketBookedResponse>>
        get() = userRepository.ticketConfirmationResponseLiveData

    fun ticketConfirmation(
        booktype: String,
        bookingid: String,
        userid: String,
        qr: String,
        srilanka: String,
        infosys: String,
        isSpi: String,
        oldBookingId: String,
        change: String
    ) {
        viewModelScope.launch {
            userRepository.ticketConfirmationLiveDataLayout(
                booktype,
                bookingid,
                userid,
                qr,
                srilanka,
                infosys,
                isSpi,
                oldBookingId,
                change
            )
        }
    }

    //Single Ticket Confirmation
    val singleTicketDataScope: LiveData<NetworkResult<TicketBookedResponse>>
        get() = userRepository.singleTicketResponseLiveData

    fun singleTicket(
        bookingid: String,
        userid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.singleTicket(
                bookingid,
                userid,
                booktype
            )
        }
    }

    //Food Ticket Confirmation
    val fnbTicketDataScope: LiveData<NetworkResult<TicketBookedResponse>>
        get() = userRepository.fnbTicketResponseLiveData

    fun fnbTicket(
        bookingid: String,
        userid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.fnbTicket(
                bookingid,
                userid,
                booktype
            )
        }
    }

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
    }

    /****************** FEEDBACK FOR MOVIE AND CINEMA ****************/
    val getFeedBackDataResponseLiveData: LiveData<NetworkResult<FeedbackDataResponse>>
        get() = userRepository.getFeedBackDataResponseLiveData

    fun getFeedBackData(
        userid: String,
        type: String
    ) {
        viewModelScope.launch {
            userRepository.getFeedBackData(userid,type)
        }
    }

    val setFeedBackDataResponseLiveData: LiveData<NetworkResult<FeedbackDataResponse>>
        get() = userRepository.setFeedBackDataResponseLiveData

    fun setFeedBackData(
        userId: String, type: String,code:String,text: String,tags:String,comment: String
    ) {
        viewModelScope.launch {
            userRepository.setFeedBackData(userId,type,code,text,tags,comment)
        }
    }

}