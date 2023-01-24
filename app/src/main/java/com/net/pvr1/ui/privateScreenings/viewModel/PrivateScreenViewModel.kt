package com.net.pvr1.ui.privateScreenings.viewModel

import androidx.lifecycle.ViewModel
import com.net.pvr1.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivateScreenViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

//    val userResponseLiveData: LiveData<NetworkResult<OfferResponse>>
//        get() = userRepository.offerResponseLiveData
//
//    fun offer(did: String) {
//        viewModelScope.launch {
//            userRepository.offer(did, userId, isSpi)
//        }
//    }

}