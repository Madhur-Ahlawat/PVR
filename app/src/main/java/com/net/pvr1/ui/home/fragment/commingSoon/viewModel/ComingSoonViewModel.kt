package com.net.pvr1.ui.home.fragment.commingSoon.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComingSoonViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    //Fragment ComingSoon
    val userResponseLiveData: LiveData<NetworkResult<CommingSoonResponse>>
        get() = userRepository.comingSoonResponseLiveData

    fun comingSoon(city: String, genre: String, lang: String, userid: String) {
        viewModelScope.launch {
            userRepository.comingSoon(city,genre,lang,userid)
        }
    }


    //Activity ComingSoon
    val movieDetailsLiveData: LiveData<NetworkResult<MovieDetailsResponse>>
        get() = userRepository.commingSoonResponseLiveData

    fun movieDetails(userid: String,city: String,mcode: String,did: String) {
        viewModelScope.launch {
            userRepository.commingSoonData(userid,city,mcode,did)
        }
    }


}