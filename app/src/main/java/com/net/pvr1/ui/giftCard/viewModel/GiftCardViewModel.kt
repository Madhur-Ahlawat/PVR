package com.net.pvr1.ui.giftCard.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.giftCard.response.GiftCardListResponse
import com.net.pvr1.utils.NetworkResult
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