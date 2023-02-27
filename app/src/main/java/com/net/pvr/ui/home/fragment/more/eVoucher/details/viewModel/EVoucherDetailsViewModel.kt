package com.net.pvr.ui.home.fragment.more.eVoucher.details.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.fragment.comingSoon.response.CommingSoonResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EVoucherDetailsViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    //Fragment ComingSoon
    val userResponseLiveData: LiveData<NetworkResult<CommingSoonResponse>>
        get() = userRepository.comingSoonResponseLiveData

    fun comingSoon(city: String, genre: String, lang: String, userid: String) {
        viewModelScope.launch {
            userRepository.comingSoon(city,genre,lang,userid)
        }
    }
}