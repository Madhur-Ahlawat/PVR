package com.net.pvr1.ui.home.fragment.more.eVoucher.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.comingSoon.response.CommingSoonResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EVoucherViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    //Fragment ComingSoon
    val userResponseLiveData: LiveData<NetworkResult<CommingSoonResponse>>
        get() = userRepository.comingSoonResponseLiveData

    fun comingSoon(city: String, genre: String, lang: String, userid: String) {
        viewModelScope.launch {
            userRepository.comingSoon(city,genre,lang,userid)
        }
    }
}