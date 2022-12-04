package com.net.pvr1.ui.ticketConfirmation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.summery.response.SummeryResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketConfirmationViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //Summery Details
    val liveDataScope: LiveData<NetworkResult<SummeryResponse>>
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

}