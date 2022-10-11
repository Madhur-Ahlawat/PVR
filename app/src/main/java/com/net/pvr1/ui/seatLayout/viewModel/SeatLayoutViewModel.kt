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
    //SeatLayout
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
    //reserve Seat
    val reserveSeatResponseLiveData: LiveData<NetworkResult<SeatResponse>>
        get() = userRepository.reserveSeatResponseLiveData
    fun reserveSeat(
        reserve: String
    ) {
        viewModelScope.launch {
            userRepository.reserveSeatLayout(reserve)
        }
    }
    //initTrans
    val initTransResponseLiveData: LiveData<NetworkResult<SeatResponse>>
        get() = userRepository.initTransSeatResponseLiveData

    fun initTransSeat(
        cinemacode: String,
        sessionid: String
    ) {
        viewModelScope.launch {
            userRepository.initTransSeatLayout(cinemacode,sessionid)
        }
    }
}