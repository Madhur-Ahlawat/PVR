package com.net.pvr1.ui.movieDetails.nowShowing.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val movieDetailsLiveData: LiveData<NetworkResult<MovieDetailsResponse>>
    get() = userRepository.movieDetailsResponseLiveData


    fun movieDetails(city: String,mid: String,type: String,userid: String,lat: String, lng: String, isSpi: String, srilanka: String,) {
        viewModelScope.launch {
            userRepository.movieDetailsData(city,mid,type,userid,lat, lng,isSpi,srilanka)
        }
    }

}