package com.net.pvr1.ui.contactUs.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.contactUs.response.ContactUsResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<ContactUsResponse>>
        get() = userRepository.contactUsResponseLiveData

    fun contactUs(
        comment: String,
        email: String,
        mobile: String,
        did: String,
        isSpi: String,
        ctype: String
    ) {
        viewModelScope.launch {
            userRepository.contactUsUser(comment, email, mobile, did, isSpi, ctype)
        }
    }

}