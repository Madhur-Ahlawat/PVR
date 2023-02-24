package com.net.pvr1.ui.payment.cardDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.home.fragment.privilege.response.PassportPlanResponse
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr1.ui.payment.response.*
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDetailsViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    //voucher
    val liveDataScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.paytmHmaResponseLiveData

    fun paytmHMAC(
        userid: String,
        bookingid: String,
        transid: String,
        unpaid: Boolean,
        cardNo: String,
        booktype: String,
        ptype: String,
        isSpi: String,
        binOffer: String
    ) {
        viewModelScope.launch {
            userRepository.paytmHMAC(userid, bookingid, transid, unpaid, cardNo,booktype,ptype,isSpi,binOffer)
        }
    }

    val liveDataRScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.paytmRHmacResponseLiveData

    fun paytmRHMAC(
        userid: String,
        bookingid: String,
        transid: String,
        unpaid: Boolean,
        cardNo: String,
        booktype: String,
        ptype: String,
        isSpi: String,
        binOffer: String
    ) {
        viewModelScope.launch {
            userRepository.paytmRHMAC(userid, bookingid, transid, unpaid, cardNo,booktype,ptype,isSpi,binOffer)
        }
    }


    //Recurring BIN Check
    val recurringBinLiveDataScope: LiveData<NetworkResult<RecurringInitResponse>> get() = userRepository.recurringBinResponseLiveData

    fun recurringBinCheck(
        userid: String,
        bookingid: String,
        token: String,
        bin: String,
        vpa: String
    ) {
        viewModelScope.launch {
            userRepository.recurringBinCheck(userid, bookingid, token, bin, vpa)
        }
    }

    //Recurring Generate New Order
    val recurringNewLiveDataScope: LiveData<NetworkResult<PassportPlanResponse>> get() = userRepository.passportGenerateResponseLiveData

    fun passportGenerate(
        userId: String, city: String, scheme: String, bookingid: String, retrycount: String, reason: String
    ) {
        viewModelScope.launch {
            userRepository.passportGenerate(userId, city, scheme, bookingid, retrycount,reason)
        }
    }

    //paytm
    val liveBankDataScope: LiveData<NetworkResult<BankListResponse>> get() = userRepository.paytmBankListResponseLiveData

    fun bankList() {
        viewModelScope.launch {
            userRepository.bankList()
        }
    }


    //Bank Offer
    val bankOfferDataScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.bankOfferResponseLiveData

    fun bankOffer( userid: String,
                   bookingid: String,
                   transid: String,
                   booktype: String,
                   scheem:String,
                   cardNo:String,binOffer:String,paymentType:String) {
        viewModelScope.launch {
            userRepository.bankOffer(userid,bookingid,transid,booktype,scheem,cardNo, binOffer,paymentType)
        }
    }

}