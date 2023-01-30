package com.net.pvr1.ui.food.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.utils.NetworkResult
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