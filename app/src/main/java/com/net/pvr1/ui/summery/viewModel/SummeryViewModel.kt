package com.net.pvr1.ui.summery.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr1.repository.UserRepository
import com.net.pvr1.ui.food.response.CancelTransResponse
import com.net.pvr1.ui.payment.response.JusPayInitResponse
import com.net.pvr1.ui.payment.response.PaytmHmacResponse
import com.net.pvr1.ui.payment.response.PhonepeHmacResponse
import com.net.pvr1.ui.payment.response.UPIStatusResponse
import com.net.pvr1.ui.summery.response.ExtendTimeResponse
import com.net.pvr1.ui.summery.response.SetDonationResponse
import com.net.pvr1.ui.summery.response.SummeryResponse
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummeryViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //Summery Details
    val liveDataScope: LiveData<NetworkResult<SummeryResponse>>
        get() = userRepository.summerResponseLiveData

    fun summery(transid: String, cinemacode: String, userid: String, bookingid: String) {
        viewModelScope.launch {
            userRepository.summerLayout(transid, cinemacode, userid, bookingid)
        }
    }


    //seatWithFood
    val seatWithFoodDataScope: LiveData<NetworkResult<SummeryResponse>>
        get() = userRepository.seatWithFoodResponseLiveData

    fun seatWithFood(
        foods: String,
        transid: String,
        cinemacode: String,
        userId: String,
        qr: String,
        infosys: String,
        isSpi: String,
        seat: String,
        audi: String
    ) {
        viewModelScope.launch {
            userRepository.seatWithFoodLayout(
                foods,
                transid,
                cinemacode,
                userId,
                qr,
                infosys,
                isSpi,
                seat,
                audi
            )
        }
    }


    //Set Donation
    val setDonationDataScope: LiveData<NetworkResult<SetDonationResponse>>
        get() = userRepository.setDonationResponseLiveData

    fun setDonation(
        bookingid: String,
        transid: String,
        isDonate: Boolean,
        istDonate: Boolean,
        isSpi: String
    ) {
        viewModelScope.launch {
            userRepository.setDonationLayout(bookingid, transid, isDonate, istDonate, isSpi)
        }
    }

    //    Cancel trans
    val cancelTransResponseLiveData: LiveData<NetworkResult<CancelTransResponse>>
        get() = userRepository.cancelTransResponseLiveData

    fun cancelTrans(
        cinemacode: String, transid: String, bookingid: String
    ) {
        viewModelScope.launch {
            userRepository.cancelTrans(cinemacode, transid, bookingid)
        }
    }

//extendTime
    val extendTimeLiveData: LiveData<NetworkResult<ExtendTimeResponse>>
        get() = userRepository.extendTimeResponseLiveData

    fun extendTime(transid: String, bookingid: String, cinemacode: String) {
        viewModelScope.launch {
            userRepository.extendTime(transid,bookingid,cinemacode)
        }
    }

    //Upi Status
    val upiStatusResponseLiveData: LiveData<NetworkResult<UPIStatusResponse>>
        get() = userRepository.upiStatusResponseLiveData

    fun upiStatus(
        bookingid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.upiStatus(bookingid, booktype)
        }
    }

    //paytm
    val livePDataScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.paytmHmaResponseLiveData

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


    //Phonepe Hmac
    val phonepeLiveDataScope: LiveData<NetworkResult<PhonepeHmacResponse>> get() = userRepository.phonepeHmacResponseLiveData

    fun phonepeHMAC(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
    ) {
        viewModelScope.launch {
            userRepository.phonepeHmac(userid, bookingid,booktype,transid)
        }
    }

    //Phonepe Status
    val phonepeStatusLiveDataScope: LiveData<NetworkResult<UPIStatusResponse>> get() = userRepository.phonepeStatusResponseLiveData

    fun phonepeStatus(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
    ) {
        viewModelScope.launch {
            userRepository.phonepeStatus(userid, bookingid,booktype,transid)
        }
    }

    //CRED Check
    val credCheckLiveDataScope: LiveData<NetworkResult<UPIStatusResponse>> get() = userRepository.credCheckResponseLiveData

    fun credCheck(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        unpaid: String,
        cred_present: String,
        spi: String,
    ) {
        viewModelScope.launch {
            userRepository.credCheck(userid, bookingid,booktype,transid,unpaid,cred_present,spi)
        }
    }//CRED Hmac
    val credHmacLiveDataScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.credHmacResponseLiveData

    fun credHmac(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
        unpaid: String,
        cred_present: String,
        spi: String,
        ptype: String
    ) {
        viewModelScope.launch {
            userRepository.credHmac(userid, bookingid,booktype,transid,unpaid,cred_present,spi,ptype)
        }
    }

    //CRED Status
    val credStatusLiveDataScope: LiveData<NetworkResult<UPIStatusResponse>> get() = userRepository.credStatusResponseLiveData

    fun credStatus(
        userid: String,
        bookingid: String,
        booktype: String,
        transid: String,
    ) {
        viewModelScope.launch {
            userRepository.credStatus(userid, bookingid,booktype,transid)
        }
    }


    //JsuPay Init
    val initJusPayLiveData: LiveData<NetworkResult<JusPayInitResponse>>
        get() = userRepository.initJusPayResponseLiveData

    fun initJusPay(userId: String, bookingid: String) {
        viewModelScope.launch {
            userRepository.initJusPay(userId,bookingid)
        }
    }

}