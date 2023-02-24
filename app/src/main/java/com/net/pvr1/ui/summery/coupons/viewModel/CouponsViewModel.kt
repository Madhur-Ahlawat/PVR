package com.net.pvr1.ui.summery.coupons.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.summery.response.AddFoodResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CouponsViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

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
            userRepository.foodAddLayout(userid,
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

}