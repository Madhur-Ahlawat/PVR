package com.net.pvr1.ui.profile.userDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.more.prefrence.response.PreferenceResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferenceViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    val userResponseLiveData: LiveData<NetworkResult<PreferenceResponse>>
        get() = userRepository.preferenceResponseLiveData

    fun preference(city:String, userid:String
    ) {
        viewModelScope.launch { userRepository.preference(city,userid)
        }
    }

}