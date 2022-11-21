package com.net.pvr1.ui.formats.viewModel

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
class FormatsViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //formats Details
    val liveDataScope: LiveData<NetworkResult<SummeryResponse>>
        get() = userRepository.summerResponseLiveData

    fun summery(transid: String, cinemacode: String, userid: String, bookingid: String) {
        viewModelScope.launch {
            userRepository.summerLayout(transid, cinemacode, userid, bookingid)
        }
    }


}