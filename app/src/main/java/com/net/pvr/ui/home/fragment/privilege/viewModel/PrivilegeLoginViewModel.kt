package com.net.pvr.ui.home.fragment.privilege.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.home.fragment.privilege.response.LoyaltyDataResponse
import com.net.pvr.ui.home.fragment.privilege.response.PassportPlanResponse
import com.net.pvr.ui.login.response.LoginResponse
import com.net.pvr.utils.NetworkResult
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


    val passportSaveLiveData: LiveData<NetworkResult<PassportPlanResponse>>
    get() = userRepository.passportSaveResponseLiveData

    fun passportSave(userId: String, city: String, fname: String, lname: String, subsplan: String, dob: String, gender: String, scheme: String, price: String, mobile: String, email: String) {
        viewModelScope.launch {
            userRepository.passportSave(userId,city,fname,lname,subsplan,dob,gender,scheme,price,mobile,email)
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