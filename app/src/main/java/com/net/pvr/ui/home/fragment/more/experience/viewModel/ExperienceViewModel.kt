package com.net.pvr.ui.home.fragment.more.experience.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.fragment.more.experience.response.ExperienceDetailsResponse
import com.net.pvr.ui.home.fragment.more.experience.response.ExperienceResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    //formats Details
    val liveDataScope: LiveData<NetworkResult<ExperienceResponse>>
        get() = userRepository.experienceResponseLiveData

    fun experience(city: String) {
        viewModelScope.launch {
            userRepository.experienceLayout(city)
        }
    }

    //experience Details
    val experienceLiveDataScope: LiveData<NetworkResult<ExperienceDetailsResponse>>
        get() = userRepository.experienceDetailsResponseLiveData

    fun experienceDetails(city: String,type: String) {
        viewModelScope.launch {
            userRepository.experienceDetailsLayout(city,type)
        }
    }
}