package com.net.pvr1.ui.summery.coupons.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.summery.response.AddFoodResponse
import com.net.pvr1.ui.summery.response.SummeryResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CouponsViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //Summery Details
    val liveDataScope: LiveData<NetworkResult<SummeryResponse>>
        get() = userRepository.summerResponseLiveData

    fun summery(transid: String, cinemacode: String, userid: String, bookingid: String) {
        viewModelScope.launch {
            userRepository.summerLayout(transid, cinemacode, userid, bookingid)
        }
    }

    //Food
    val foodLiveDataScope: LiveData<NetworkResult<AddFoodResponse>>
        get() = userRepository.foodAddResponseLiveData

    fun food(
        userid: String,
        cinemacode: String,
        fb_totalprice: String,
        fb_itemStrDescription: String,
        pickupdate: String,
        cbookid: String,
        audi: String,
        seat: String,
        type: String,
        infosys: String,
        qr: String,
        isSpi: String,
        srilanka: String,
    ) {
        viewModelScope.launch {
            userRepository.foodAddLayout(
                cinemacode,
                fb_totalprice,
                fb_itemStrDescription,
                pickupdate,
                cbookid,
                audi,
                seat,
                type,
                infosys,
                qr,
                isSpi,
                srilanka
            )
        }
    }

    //seatWithFood
    val seatWithFoodDataScope: LiveData<NetworkResult<SummeryResponse>>
        get() = userRepository.seatWithFoodResponseLiveData

    fun seatWithFood(foods: String, transid: String, cinemacode: String, qr: String, infosys: String, isSpi: String) {
        viewModelScope.launch {
            userRepository.seatWithFoodLayout(foods, transid, cinemacode, qr,infosys,isSpi)
        }
    }
}