package com.net.pvr.ui.giftCard.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.giftCard.response.GiftCardListResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiftCardViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<GiftCardListResponse>>
    get() = userRepository.giftCardMainResponseLiveData

    fun giftCards(sWidth:String,infosys:String) {
        viewModelScope.launch {
            userRepository.giftCardMainLayout(sWidth,infosys)
        }
    }

}