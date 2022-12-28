package com.net.pvr1.ui.profile.userDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.more.response.ProfileResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<ProfileResponse>>
        get() = userRepository.editProfileResponseLiveData

    fun editProfile(userid:String,email:String,mobile:String,name:String,dob:String,gender:String,mstatus:String,doa:String) {
        viewModelScope.launch {
            userRepository.editProfile(userid,email,mobile,name,dob,gender,mstatus,doa)
        }
    }

}