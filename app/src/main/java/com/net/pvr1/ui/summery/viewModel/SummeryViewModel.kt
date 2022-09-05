package com.net.pvr1.ui.summery.viewModel

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
class SummeryViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val liveDataScope: LiveData<NetworkResult<FoodResponse>>
    get() = userRepository.summerResponseLiveData

    fun summery(bookingid: String,transid: String,isDonate: Boolean,istDonate: Boolean, isSpi: String) {
        viewModelScope.launch {
            userRepository.summerLayout(bookingid,transid,isDonate,istDonate,isSpi)
        }
    }

}