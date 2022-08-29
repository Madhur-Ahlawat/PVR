package com.net.pvr1.ui.seatLayout.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.models.request.UserRequest
import com.net.pvr1.models.response.UserResponse
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.offer.response.OfferResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeatLayoutViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<OfferResponse>>
    get() = userRepository.offerResponseLiveData

    fun offer(did:String) {
        viewModelScope.launch {
            userRepository.offer(did)
        }
    }

}