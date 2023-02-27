package com.net.pvr.ui.food.old.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.food.old.reponse.OldFoodResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OldFoodViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<OldFoodResponse>>
        get() = userRepository.oldFoodResponseLiveData

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
            userRepository.oldFoodLayout(userid,ccode,bookingid,cbookid,transid,type,audi,seat,city,qr,iserv,isSpi)
        }
    }

}