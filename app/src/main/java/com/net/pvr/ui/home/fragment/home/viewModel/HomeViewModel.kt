package com.net.pvr.ui.home.fragment.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.fragment.home.response.FeedbackDataResponse
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.home.fragment.home.response.NextBookingResponse
import com.net.pvr.ui.home.fragment.more.offer.response.OfferResponse
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    val userResponseLiveData: LiveData<NetworkResult<HomeResponse>>
        get() = userRepository.homeResponseLiveData

    fun home(
        city: String, dtmsource: String, userid: String, mobile: String, upbooking: Boolean, srilanka: String, type : String, lng: String, gener: String, spShow: String, isSpi: String
    ) {
        viewModelScope.launch { userRepository.homeData(city,dtmsource,userid,mobile,upbooking,srilanka
            ,type,lng,gener,spShow,isSpi)
        }
    }


    //offer
    val offerLiveData: LiveData<NetworkResult<OfferResponse>>
        get() = userRepository.offerResponseLiveData

    fun offer(city:String,userId:String,did:String,isSpi:String) {
        viewModelScope.launch {
            userRepository.offer(city,userId,did,isSpi)
        }
    }

    //hideOffer
    val hideOfferLiveData: LiveData<NetworkResult<OfferResponse>>
        get() = userRepository.hideOfferResponseLiveData

    fun hideOffer(city:String,userId:String,did:String,isSpi:String) {
        viewModelScope.launch {
            userRepository.hideOffer(city,userId,did,isSpi)
        }
    }

    //Privilege
    val privilegeHomeResponseLiveData: LiveData<NetworkResult<PrivilegeHomeResponse>>
        get() = userRepository.privilegeHomeResponseLiveData

    fun privilegeHome(
        mobile: String,
        city: String
    ) {
        viewModelScope.launch {
            userRepository.privilegeHomeData(mobile,city)
        }
    }

    //nextBooking
    val nextBookingResponseLiveData: LiveData<NetworkResult<NextBookingResponse>>
        get() = userRepository.nextBookingResponseLiveData

    fun nextBooking(
        userid: String,
        did: String
    ) {
        viewModelScope.launch {
            userRepository.nextBookingData(userid,did)
        }
    }

    /****************** FEEDBACK FOR MOVIE AND CINEMA ****************/
    val getFeedBackDataResponseLiveData: LiveData<NetworkResult<FeedbackDataResponse>>
        get() = userRepository.getFeedBackDataResponseLiveData

    fun getFeedBackData(
        userid: String,
        type: String
    ) {
        viewModelScope.launch {
            userRepository.getFeedBackData(userid,type)
        }
    }

    val setFeedBackDataResponseLiveData: LiveData<NetworkResult<FeedbackDataResponse>>
        get() = userRepository.setFeedBackDataResponseLiveData

    fun setFeedBackData(
        userId: String, type: String,code:String,text: String,tags:String,comment: String
    ) {
        viewModelScope.launch {
            userRepository.setFeedBackData(userId,type,code,text,tags,comment)
        }
    }

}