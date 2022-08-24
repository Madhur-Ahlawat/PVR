package com.net.pvr1.ui.search.searchHome.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeSearchViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val homeSearchLiveData: LiveData<NetworkResult<HomeSearchResponse>>
    get() = userRepository.searchResponseLiveData

    fun homeSearch(city: String,text: String,searchFilter: String,lat: String, lng: String) {
        viewModelScope.launch {
            userRepository.homeSearchData(city,text,searchFilter,lat, lng)
        }
    }

}