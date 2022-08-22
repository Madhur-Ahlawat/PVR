package com.net.pvr1.ui.searchHome.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.models.request.UserRequest
import com.net.pvr1.models.response.UserResponse
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeSearchViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val cityResponseLiveData: LiveData<NetworkResult<SelectCityResponse>>
    get() = userRepository.citiesResponseLiveData

    fun selectCity(lat: String,lng: String,userid: String,isSpi: String, srilanka: String) {
        viewModelScope.launch {
            userRepository.selectCity(lat,lng,userid,isSpi,srilanka)
        }
    }

}