package com.net.pvr.ui.payment.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.net.pvr.repository.UserRepository
import com.net.pvr.ui.payment.response.*
import com.net.pvr.ui.summery.response.ExtendTimeResponse
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //voucher
    val userResponseLiveData: LiveData<NetworkResult<CouponResponse>>
        get() = userRepository.couponsResponseLiveData

    fun voucher(token: String,mobile: String, city: String, userid: String, timestamp: String) {
        viewModelScope.launch {
            userRepository.coupons(token,mobile,  city,userid,timestamp)
        }
    }

    //voucher Apply
    val voucherApplyLiveData: LiveData<NetworkResult<LoyaltyVocherApply>>
        get() = userRepository.voucherApplyResponseLiveData

    fun voucherApply(promocode: String, userid: String, booktype: String, bookingid: String, transid: String, loyalitytype: String, unlimitedvoucher: String, voucheramt: String) {
        viewModelScope.launch {
            userRepository.voucherApply(promocode,userid,  booktype,bookingid,transid,loyalitytype,unlimitedvoucher,voucheramt)
        }
    }

    //Promo list
    val promoListLiveData: LiveData<NetworkResult<PromoCodeList>>
        get() = userRepository.promoListResponseLiveData

    fun promoList() {
        viewModelScope.launch {
            userRepository.promoList()
        }
    }

    // APPLY PROMOCODE
    val livePromoCodeScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.promoCodeResponseLiveData

    fun promoCode(
        userid: String,
        bookingid: String,
        transid: String,
        booktype: String,
        promocode:String
    ) {
        viewModelScope.launch {
            userRepository.promoCode(userid, bookingid, booktype, transid,promocode)
        }
    }

    // REMOVE PROMOCODE
    val removePromoCodeScope: LiveData<NetworkResult<PaytmHmacResponse>> get() = userRepository.removePromoResponseLiveData

    fun removePromo(
        mobile: String,
        bookingid: String,
        booktype: String
    ) {
        viewModelScope.launch {
            userRepository.removePromo(mobile, bookingid, booktype)
        }
    }

    //payMode
    val payModeResponseLiveData: LiveData<NetworkResult<PaymentResponse>>
        get() = userRepository.payModeResponseLiveData

    fun payMode(
        cinemacode: String,
        booktype: String,
        userid: String,
        mobile: String,
        type: String,
        isSpi: String,
        srilanka: String,
        unpaid: Boolean
    ) {
        viewModelScope.launch {
            userRepository.payMode(cinemacode, booktype, userid, mobile, type, isSpi, srilanka,unpaid)
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


    //paytm
    val liveBankDataScope: LiveData<NetworkResult<BankListResponse>> get() = userRepository.paytmBankListResponseLiveData

    fun bankList() {
        viewModelScope.launch {
            userRepository.bankList()
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


    //Recurring Init
    val recurringInitLiveDataScope: LiveData<NetworkResult<RecurringInitResponse>> get() = userRepository.recurringInitResponseLiveData

    fun recurringInit(
        userid: String,
        bookingid: String
    ) {
        viewModelScope.launch {
            userRepository.recurringInit(userid, bookingid)
        }
    }

    fun cancelTrans(
        cinemacode: String,
        transid: String,
        bookingid: String
    ) {
        viewModelScope.launch {
            userRepository.cancelTrans(cinemacode,transid,bookingid)
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

}