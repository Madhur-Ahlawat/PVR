package com.net.pvr1.ui.cinemaSession.cinemaDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.cinemaSession.cinemaDetails.response.CinemaDetailsResponse
import com.net.pvr1.utils.NetworkResult
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