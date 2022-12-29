package com.net.pvr1.ui.home.fragment.privilege.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.models.request.UserRequest
import com.net.pvr1.models.response.UserResponse
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.privilege.response.LoyaltyDataResponse
import com.net.pvr1.ui.home.fragment.privilege.response.PassportPlanResponse
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivilegeLoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<LoginResponse>>
    get() = userRepository.userResponseLiveData

    fun loginMobileUser(mobile: String, city: String, cName: String) {
        viewModelScope.launch {
            userRepository.loginMobile(mobile,city,cName)
        }
    }

    val passportPlanLiveData: LiveData<NetworkResult<PassportPlanResponse>>
    get() = userRepository.passportPlanResponseLiveData

    fun passportPlans(userId: String, city: String) {
        viewModelScope.launch {
            userRepository.passportPlans(userId,city)
        }
    }


    val loyaltyDataLiveData: LiveData<NetworkResult<LoyaltyDataResponse>>
    get() = userRepository.loyaltyDataResponseLiveData

    fun loyaltyData(userId: String, city: String,mobile: String,timestamp:String,token:String) {
        viewModelScope.launch {
            userRepository.loyaltyData(userId,city,mobile, timestamp,token)
        }
    }

}