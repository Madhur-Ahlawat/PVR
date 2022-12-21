package com.net.pvr1.ui.watchList.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.more.response.DeleteAlertResponse
import com.net.pvr1.ui.watchList.response.WatchListResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
//watchList
    val liveDataScope: LiveData<NetworkResult<WatchListResponse>>
    get() = userRepository.watchListResponseLiveData

    fun watchlist(userid:String,city: String,did: String) {
        viewModelScope.launch {
            userRepository.watchListLayout(userid,city,did)
        }
    }

    val deleteAlertLiveDataScope: LiveData<NetworkResult<DeleteAlertResponse>>
    get() = userRepository.deleteAlertResponseLiveData

    fun deleteAlert(userid:String,mcode: String,city: String) {
        viewModelScope.launch {
            userRepository.deleteAlertLayout(userid,mcode,city)
        }
    }


}