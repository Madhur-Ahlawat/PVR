package com.net.pvr.ui.cinemaSession.cinemaDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.cinemaSession.cinemaDetails.response.CinemaDetailsResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CinemaDetailsViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val authModel: LiveData<NetworkResult<CinemaDetailsResponse>>
        get() = userRepository.cinemaDetailsResponseLiveData

    fun cinemaDetails(
        cid: String,
        lat: String,
        lang: String
    ) {
        viewModelScope.launch {
            userRepository.cinemaDetails(
                cid,lat,lang
            )
        }
    }

}