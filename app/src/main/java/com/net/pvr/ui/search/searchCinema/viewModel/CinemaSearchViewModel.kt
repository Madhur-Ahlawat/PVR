package com.net.pvr.ui.search.searchCinema.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CinemaSearchViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    val homeSearchLiveData: LiveData<NetworkResult<HomeSearchResponse>>
        get() = userRepository.searchResponseLiveData

    fun cinemaSearch(city: String,text: String,searchFilter: String,lat: String, lng: String) {
        viewModelScope.launch {
            userRepository.homeSearchData(city,text,searchFilter,lat, lng)
        }
    }


}