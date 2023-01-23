package com.net.pvr1.ui.home.fragment.more.bookingRetrieval.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingRetrievalViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<BookingRetrievalResponse>>
        get() = userRepository.bookingRetrievalResponseLiveData

    fun bookingRetrieval(
        city: String,
        lat : String,
        lng: String,
        userid: String,
        searchtxt: String
    ) {
        viewModelScope.launch {
            userRepository.bookingRetrievalLayout(city,lat,lng,userid,searchtxt)
        }
    }

}