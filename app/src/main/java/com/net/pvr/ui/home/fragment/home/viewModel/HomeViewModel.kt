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
import com.net.pvr.ui.home.inCinemaMode.response.GetBookingResponse
import com.net.pvr.ui.home.inCinemaMode.response.GetInCinemaResponse
import com.net.pvr.ui.home.inCinemaMode.response.InCinemaHomeResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
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

    //offer
    val offerHideLiveData: LiveData<NetworkResult<ResponseBody>>
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
        userId: String, type: String,code:String,text: String,tags:String,comment: String,ccode:String,bookingId:String
    ) {
        viewModelScope.launch {
            userRepository.setFeedBackData(userId,type,code,text,tags,comment,ccode,bookingId)
        }
    }

    val getInCinemaLiveDataHome: LiveData<NetworkResult<InCinemaHomeResponse>>
        get() = userRepository.inCinemaHomeResponseLiveData

    val getInCinemaLiveData: LiveData<NetworkResult<GetInCinemaResponse>>
        get() = userRepository.getInCinemaResponseLiveData
    val getBookingLiveData: LiveData<NetworkResult<GetBookingResponse>>
        get() = userRepository.getBookingResponseLiveData

    fun getInCinema(
        userid: String,
        city: String
    ) {
        viewModelScope.launch {
            userRepository.getInCinema(userid, city)
        }
    }
    fun getBooking(
        bookingId: String,city: String
    ) {
        viewModelScope.launch {
            userRepository.getBooking(bookingId,city)
        }
    }

    fun getInCinemaHome(
        userid: String,
        city: String
    ) {
        viewModelScope.launch {
            userRepository.getInCinemaHome(userid, city)
        }
    }

}