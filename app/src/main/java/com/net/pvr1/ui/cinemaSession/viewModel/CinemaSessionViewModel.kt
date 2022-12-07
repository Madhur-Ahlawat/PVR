package com.net.pvr1.ui.cinemaSession.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.home.fragment.cinema.response.PreferenceResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CinemaSessionViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    // cinema
    val cinemaSessionLiveData: LiveData<NetworkResult<CinemaSessionResponse>>
        get() = userRepository.cinemaSessionResponseLiveData

    fun cinemaSession(
        city: String,
        cid: String,
        lat: String,
        lng: String,
        userid: String,
        date: String,
        lang: String,
        format: String,
        price: String,
        time: String,
        hc: String,
        cc: String,
        ad: String,
        qr: String,
        cinetype: String,
        cinetypeQR: String,
    ) {
        viewModelScope.launch {
            userRepository.cinemaSessionData(
                city,
                cid,
                lat,
                lng,
                userid,
                date,
                lang,
                format,
                price,
                time,
                hc,
                cc,
                ad,
                qr,
                cinetype,
                cinetypeQR
            )
        }
    }

// near theater

    val cinemaSessionNearTheaterLiveData: LiveData<NetworkResult<CinemaNearTheaterResponse>>
        get() = userRepository.nearTheaterResponseLiveData

    fun cinemaNearTheater(
        city: String,
        lat:  String,
        lng: String,
        cid: String
    ) {
        viewModelScope.launch {
            userRepository.nearTheater(
                city,
                lat,
                lng,
                cid
            )
        }
    }


    //preference
    val cinemaPreferenceResponseLiveData: LiveData<NetworkResult<PreferenceResponse>>
        get() = userRepository.cinemaPreferenceResponseLiveData

    fun  cinemaPreference(userid: String, id: String, is_like: Boolean, type: String, did: String) {
        viewModelScope.launch {
            userRepository.cinemaPreference(userid,id,is_like,type,did)
        }
    }
}