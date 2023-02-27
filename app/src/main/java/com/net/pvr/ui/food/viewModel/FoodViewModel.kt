package com.net.pvr.ui.food.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.food.response.FoodResponse
import com.net.pvr.ui.summery.response.AddFoodResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<FoodResponse>>
        get() = userRepository.foodResponseLiveData

    fun food(
        userid: String,
        ccode: String,
        bookingid: String,
        cbookid: String,
        transid: String,
        type: String,
        audi: String,
        seat: String,
        city: String,
        qr: String,
        iserv: String,
        isSpi: String
    ) {
        viewModelScope.launch {
            userRepository.foodLayout(userid,ccode,bookingid,cbookid,transid,type,audi,seat,city,qr,iserv,isSpi)
        }
    }


    val saveFoodResponseLiveData: LiveData<NetworkResult<AddFoodResponse>>
        get() = userRepository.foodAddResponseLiveData

    fun saveFood(
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
            userRepository.foodAddLayout(userid,cinemacode,
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
                srilanka)
        }
    }

    fun cancelTrans(
        cinemacode: String,
        transid: String,
        bookingid: String
    ) {
        viewModelScope.launch {
            userRepository.cancelTrans(cinemacode,transid,bookingid)
        }
    }

}