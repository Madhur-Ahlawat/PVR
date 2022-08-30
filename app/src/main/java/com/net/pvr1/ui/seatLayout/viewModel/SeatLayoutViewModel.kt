package com.net.pvr1.ui.seatLayout.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.seatLayout.response.SeatResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeatLayoutViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<SeatResponse>>
        get() = userRepository.seatResponseLiveData

    fun seatLayout(
        cinemacode: String,
        sessionid: String,
        dtmsource: String,
        partnerid: String,
        cdate: String,
        bundle: Boolean,
        isSpi: String
    ) {
        viewModelScope.launch {
            userRepository.seatLayout(cinemacode,sessionid,dtmsource,partnerid,cdate,bundle,isSpi)
        }
    }

}