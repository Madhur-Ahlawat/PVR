package com.net.pvr.ui.giftCard.activateGiftCard.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.giftCard.response.*
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ActivateGiftCardViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<GiftCardListResponse>>
    get() = userRepository.giftCardMainResponseLiveData

    fun giftCards(sWidth:String,infosys:String) {
        viewModelScope.launch {
            userRepository.giftCardMainLayout(sWidth,infosys)
        }
    }


    //GiftCard
    val activeGCResponseLiveData: LiveData<NetworkResult<ActiveGCResponse>>
        get() = userRepository.activegiftCardResponseLiveData

    fun activeGiftCard(userId: String) {
        viewModelScope.launch {
            userRepository.activeGiftCard(userId)
        }
    }

    //Redeem GiftCard
    val redeemGCResponseLiveData: LiveData<NetworkResult<GiftcardDetailsResponse>>
        get() = userRepository.redeemGiftCardResponseLiveData

    fun redeemGC(userId: String,giftcardid:String,pin:String) {
        viewModelScope.launch {
            userRepository.redeemGiftCard(userId, giftcardid,pin)
        }
    }

    // GiftCard Details
    val detailsGCResponseLiveData: LiveData<NetworkResult<GiftCardDetailResponse>>
        get() = userRepository.detailGiftCardResponseLiveData

    fun detailGiftCard(giftcardid:String,userId: String) {
        viewModelScope.launch {
            userRepository.detailGiftCard( giftcardid,userId)
        }
    }

    //Upload GiftCard
    val uploadGCResponseLiveData: LiveData<NetworkResult<UploadImageGC>>
        get() = userRepository.uploadGiftCardResponseLiveData

    fun uploadGiftCard(image: MultipartBody.Part, name: RequestBody, time: RequestBody, userId: RequestBody, userNo: RequestBody,token:String) {
        viewModelScope.launch {
            userRepository.uploadGiftCard(image, name,time,userId,userNo,token)
        }
    }

    //Save GiftCard
    val saveGCResponseLiveData: LiveData<NetworkResult<UploadImageGC>>
        get() = userRepository.saveGiftCardResponseLiveData

    fun saveGiftCard(rName: String,rEmail: String,rMobile: String,gc_channel: String,gtype: String,
                     pkGiftId: String,pincode: String,personalMessage: String,delAddress: String,denomination: String,quantity: String,userEmail: String
                     ,ifSelf: String,totalAmount: String,customImage: String,customName:String) {
        viewModelScope.launch {
            userRepository.saveGiftCard(rName, rEmail ,rMobile,gc_channel,gtype,pkGiftId,pincode,personalMessage,delAddress,denomination,quantity,userEmail,ifSelf,totalAmount,customImage,customName)
        }
    }


    //Update GiftCard
    val updateGCResponseLiveData: LiveData<NetworkResult<UploadImageGC>>
        get() = userRepository.updateGiftCardResponseLiveData

    fun updateGiftCard(userId: String,pkGiftId: String) {
        viewModelScope.launch {
            userRepository.updateGiftCard(userId,pkGiftId)
        }
    }

    //Update GiftCard
    val reUploadGCResponseLiveData: LiveData<NetworkResult<UploadImageGC>>
        get() = userRepository.reUploadGiftCardResponseLiveData

    fun reUpload(userId: String,customImage: String,customName: String,pkGiftId: String) {
        viewModelScope.launch {
            userRepository.reUploadGiftCard(userId,customImage,customName,pkGiftId)
        }
    }

}