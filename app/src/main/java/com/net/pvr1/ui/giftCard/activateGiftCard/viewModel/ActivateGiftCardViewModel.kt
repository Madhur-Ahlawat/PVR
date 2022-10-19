package com.net.pvr1.ui.giftCard.activateGiftCard.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.giftCard.response.GiftCardResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivateGiftCardViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<com.net.pvr1.ui.myBookings.response.GiftCardResponse>>
    get() = userRepository.giftCardMainResponseLiveData

    fun offer(sWidth:String,infosys:String) {
        viewModelScope.launch {
            userRepository.giftCardMainLayout(sWidth,infosys)
        }
    }

}