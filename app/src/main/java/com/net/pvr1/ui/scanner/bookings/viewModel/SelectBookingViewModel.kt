package com.net.pvr1.ui.scanner.bookings.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.home.fragment.more.prefrence.response.PreferenceResponse
import com.net.pvr1.ui.scanner.response.GetFoodResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectBookingViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //GetCode
    val userResponseLiveData: LiveData<NetworkResult<PreferenceResponse>>
        get() = userRepository.getCodeResponseLiveData

    fun getCode(
        cid: String, cineTypeQR: String
    ) {
        viewModelScope.launch {
            userRepository.getCode(cid, cineTypeQR)
        }
    }

    //food Outlet
    val foodOutletResponseLiveData: LiveData<NetworkResult<GetFoodResponse>>
        get() = userRepository.foodOutletResponseLiveData

    fun foodOutlet(
        userid: String,
        ccode: String,
        bookingid: String,
        booking_id: String,
        cbookid: String,
        type: String,
        transid: String,
        audi: String,
        seat: String,
        qr: String,
        iserv: String,
        isSpi: String
    ) {
        viewModelScope.launch {
            userRepository.foodOutlet(
                userid,
                ccode,
                bookingid,
                booking_id,
                cbookid,
                type,
                transid,
                audi,
                seat,
                qr,
                iserv,
                isSpi
            )
        }
    }


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

    //CheckUserLocation
    val userLocationLiveData: LiveData<NetworkResult<PreferenceResponse>>
        get() = userRepository.userLocationResponseLiveData

    fun userLocation(
        userid: String, lat: String, lng: String, city: String
    ) {
        viewModelScope.launch {
            userRepository.userLocation(userid, lat,lng,city)
        }
    }


}