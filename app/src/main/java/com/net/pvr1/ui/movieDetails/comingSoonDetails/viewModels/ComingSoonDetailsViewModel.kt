package com.net.pvr1.ui.movieDetails.comingSoonDetails.viewModels

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
class ComingSoonDetailsViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {


    //Activity ComingSoon
    val movieDetailsLiveData: LiveData<NetworkResult<MovieDetailsResponse>>
        get() = userRepository.commingSoonResponseLiveData

    fun movieDetails(type:String,userid: String,city: String,mcode: String) {
        viewModelScope.launch {
            userRepository.commingSoonData(type,userid,city,mcode)
        }
    }
//Movie Alert
    val movieAlertLiveData: LiveData<NetworkResult<MovieDetailsResponse>>
        get() = userRepository.movieAlertResponseLiveData

    fun movieAlert(userid: String,city: String,mcode: String,did: String) {
        viewModelScope.launch {
            userRepository.movieAlertData(userid,city,mcode,did)
        }
    }


}