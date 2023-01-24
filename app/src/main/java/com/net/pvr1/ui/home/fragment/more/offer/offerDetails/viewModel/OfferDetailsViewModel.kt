package com.net.pvr1.ui.home.fragment.more.offer.offerDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.more.offer.offerDetails.response.OfferDetailsResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferDetailsViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<OfferDetailsResponse>>
    get() = userRepository.offerDetailsResponseLiveData

    fun offerDetails(id:String,did:String) {
        viewModelScope.launch {
            userRepository.offerDetails(id,did)
        }
    }

}