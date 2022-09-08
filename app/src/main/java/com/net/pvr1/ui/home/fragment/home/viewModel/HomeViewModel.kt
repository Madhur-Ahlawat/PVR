package com.net.pvr1.ui.home.fragment.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.offer.response.OfferResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<HomeResponse>>
        get() = userRepository.homeResponseLiveData

    fun home(
        city: String,
        dtmsource: String,
        userid: String,
        mobile: String,
        upbooking: Boolean,
        srilanka: String,
        type : String,
        lng: String,
        gener: String,
        spShow: String,
        isSpi: String
    ) {
        viewModelScope.launch {
            userRepository.homeData(city,dtmsource,userid,mobile,upbooking,srilanka
            ,type,lng,gener,spShow,isSpi)
        }
    }

    val userResponseOfferLiveData: LiveData<NetworkResult<OfferResponse>>
        get() = userRepository.offerDetailsResponseLiveData

    fun offer(id:String) {
        viewModelScope.launch {
            userRepository.offer(id)
        }
    }

}