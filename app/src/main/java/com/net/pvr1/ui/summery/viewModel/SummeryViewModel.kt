package com.net.pvr1.ui.summery.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.food.response.CancelTransResponse
import com.net.pvr1.ui.summery.response.ExtendTimeResponse
import com.net.pvr1.ui.summery.response.SetDonationResponse
import com.net.pvr1.ui.summery.response.SummeryResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummeryViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //Summery Details
    val liveDataScope: LiveData<NetworkResult<SummeryResponse>>
        get() = userRepository.summerResponseLiveData

    fun summery(transid: String, cinemacode: String, userid: String, bookingid: String) {
        viewModelScope.launch {
            userRepository.summerLayout(transid, cinemacode, userid, bookingid)
        }
    }


    //seatWithFood
    val seatWithFoodDataScope: LiveData<NetworkResult<SummeryResponse>>
        get() = userRepository.seatWithFoodResponseLiveData

    fun seatWithFood(
        foods: String,
        transid: String,
        cinemacode: String,
        userId: String,
        qr: String,
        infosys: String,
        isSpi: String,
        seat: String,
        audi: String
    ) {
        viewModelScope.launch {
            userRepository.seatWithFoodLayout(
                foods,
                transid,
                cinemacode,
                userId,
                qr,
                infosys,
                isSpi,
                seat,
                audi
            )
        }
    }


    //Set Donation
    val setDonationDataScope: LiveData<NetworkResult<SetDonationResponse>>
        get() = userRepository.setDonationResponseLiveData

    fun setDonation(
        bookingid: String,
        transid: String,
        isDonate: Boolean,
        istDonate: Boolean,
        isSpi: String
    ) {
        viewModelScope.launch {
            userRepository.setDonationLayout(bookingid, transid, isDonate, istDonate, isSpi)
        }
    }

    //    Cancel trans
    val cancelTransResponseLiveData: LiveData<NetworkResult<CancelTransResponse>>
        get() = userRepository.cancelTransResponseLiveData

    fun cancelTrans(
        cinemacode: String, transid: String, bookingid: String
    ) {
        viewModelScope.launch {
            userRepository.cancelTrans(cinemacode, transid, bookingid)
        }
    }

//extendTime
    val extendTimeLiveData: LiveData<NetworkResult<ExtendTimeResponse>>
        get() = userRepository.extendTimeResponseLiveData

    fun extendTime(transid: String, bookingid: String, cinemacode: String) {
        viewModelScope.launch {
            userRepository.extendTime(transid,bookingid,cinemacode)
        }
    }



}