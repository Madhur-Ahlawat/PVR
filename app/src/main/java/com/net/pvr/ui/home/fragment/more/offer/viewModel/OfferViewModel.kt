package com.net.pvr.ui.home.fragment.more.offer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.fragment.more.offer.response.MOfferResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<MOfferResponse>> get() = userRepository.mOfferListResponseLiveData

    fun offer(did: String,city: String) {
        viewModelScope.launch {
            userRepository.mOfferList(did,city)
        }
    }

}