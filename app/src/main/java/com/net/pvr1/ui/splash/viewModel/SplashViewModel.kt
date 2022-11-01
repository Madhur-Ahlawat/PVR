package com.net.pvr1.ui.splash.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.splash.response.SplashResponse
import com.net.pvr1.ui.summery.response.AddFoodResponse
import com.net.pvr1.ui.summery.response.SummeryResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
//Splash
    val liveDataScope: LiveData<NetworkResult<SplashResponse>>
    get() = userRepository.splashResponseLiveData

    fun splash(city: String) {
        viewModelScope.launch {
            userRepository.splashLayout(city)
        }
    }
}