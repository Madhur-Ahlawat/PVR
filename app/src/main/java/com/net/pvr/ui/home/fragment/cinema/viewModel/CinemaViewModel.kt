package com.net.pvr.ui.home.fragment.cinema.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr.ui.home.fragment.cinema.response.CinemaPreferenceResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CinemaViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<CinemaResponse>>
    get() = userRepository.cinemaResponseLiveData

    fun cinema(city: String, lat: String, lng: String, userid: String, searchTxt: String) {
        viewModelScope.launch {
            userRepository.cinema(city,lat,lng,userid,searchTxt)
        }
    }

    val cinemaPreferenceResponseLiveData: LiveData<NetworkResult<CinemaPreferenceResponse>>
        get() = userRepository.cinemaPreferenceResponseLiveData

   fun  cinemaPreference(userid: String, id: String, is_like: Boolean, type: String, did: String) {
        viewModelScope.launch {
            userRepository.cinemaPreference(userid,id,is_like,type,did)
        }
    }

}