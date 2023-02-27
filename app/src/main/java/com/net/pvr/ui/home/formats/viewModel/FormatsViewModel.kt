package com.net.pvr.ui.home.formats.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.formats.response.FormatResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormatsViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //formats Details
    val liveDataScope: LiveData<NetworkResult<FormatResponse>>
        get() = userRepository.formatsResponseLiveData

    fun formats(type: String, city: String, isSpi: String) {
        viewModelScope.launch {
            userRepository.formatsLayout(type, city, isSpi)
        }
    }


}