package com.net.pvr1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.net.pvr1.api.UserAPI
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.ui.myBookings.response.GiftCardResponse
import com.net.pvr1.ui.offer.response.OfferResponse
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

//Login
    private val _userResponseLiveData = MutableLiveData<NetworkResult<LoginResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = _userResponseLiveData

    suspend fun loginMobile(mobile:String, city: String, cName: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response =userAPI.loginMobile(mobile,city,cName,Constant.version,Constant.platform)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
    //Otp Verify
    private val otpVerifyLiveData = MutableLiveData<NetworkResult<LoginResponse>>()
    val otpVerifyResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = otpVerifyLiveData

    suspend fun otpVerify(mobile:String,token:String) {
        otpVerifyLiveData.postValue(NetworkResult.Loading())
        val response =userAPI.otpVerify(mobile,token,Constant.version,Constant.platform)
        verifyResponse(response)
    }

    private fun verifyResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful && response.body() != null) {
            otpVerifyLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            otpVerifyLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            otpVerifyLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //ComingSoon
    private val comingSoonLiveData = MutableLiveData<NetworkResult<CommingSoonResponse>>()
    val comingSoonResponseLiveData: LiveData<NetworkResult<CommingSoonResponse>>
        get() = comingSoonLiveData

    suspend fun comingSoon(city: String, genre: String, lang: String, userid: String) {
        comingSoonLiveData.postValue(NetworkResult.Loading())
        val response =userAPI.comingSoon(city,genre,lang,userid, Constant.version,
            Constant.platform
        )
        comingSoonResponse(response)
    }

    private fun comingSoonResponse(response: Response<CommingSoonResponse>) {
        if (response.isSuccessful && response.body() != null) {
            comingSoonLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            comingSoonLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            comingSoonLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Cinema
    private val cinemaLiveData = MutableLiveData<NetworkResult<CinemaResponse>>()
    val cinemaResponseLiveData: LiveData<NetworkResult<CinemaResponse>>
        get() = cinemaLiveData

    suspend fun cinema(city: String, lat: String, lng: String, userid: String, searchTxt: String) {
        cinemaLiveData.postValue(NetworkResult.Loading())
        val response =userAPI.cinema(city,lat, lng,userid,searchTxt, Constant.version,
            Constant.platform
        )
        cinemaResponse(response)
    }

    private fun cinemaResponse(response: Response<CinemaResponse>) {
        if (response.isSuccessful && response.body() != null) {
            cinemaLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            cinemaLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            cinemaLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

  //GiftCard
    private val giftCardLiveData = MutableLiveData<NetworkResult<GiftCardResponse>>()
    val giftCardResponseLiveData: LiveData<NetworkResult<GiftCardResponse>>
        get() = giftCardLiveData

    suspend fun giftCard(userId: String, did: String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response =userAPI.giftCard(userId,did, Constant.version,
            Constant.platform
        )
        giftCardResponse(response)
    }

    private fun giftCardResponse(response: Response<GiftCardResponse>) {
        if (response.isSuccessful && response.body() != null) {
            giftCardLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            giftCardLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            giftCardLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

  //FoodTicket
    private val foodTicketLiveData = MutableLiveData<NetworkResult<FoodTicketResponse>>()
    val foodTicketResponseLiveData: LiveData<NetworkResult<FoodTicketResponse>>
        get() = foodTicketLiveData

    suspend fun foodTicket(
        userId: String,
        did: String,
        sriLanka: String,
        city: String,
        isSpi: String,
        past: String
    ) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response =userAPI.foodTicket(userId,did,sriLanka,city,isSpi,past, Constant.version,
            Constant.platform
        )
        foodTicketResponse(response)
    }

    private fun foodTicketResponse(response: Response<FoodTicketResponse>) {
        if (response.isSuccessful && response.body() != null) {
            foodTicketLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            foodTicketLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            foodTicketLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

  //Offer
    private val offerLiveData = MutableLiveData<NetworkResult<OfferResponse>>()
    val offerResponseLiveData: LiveData<NetworkResult<OfferResponse>>
        get() = offerLiveData

    suspend fun offer(did: String, ) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response =userAPI.offer(did, Constant.version,Constant.platform)
        offerResponse(response)
    }

    private fun offerResponse(response: Response<OfferResponse>) {
        if (response.isSuccessful && response.body() != null) {
            offerLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            offerLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            offerLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // Select City
    private val selectCityLiveData = MutableLiveData<NetworkResult<SelectCityResponse>>()
    val citiesResponseLiveData: LiveData<NetworkResult<SelectCityResponse>>
        get() = selectCityLiveData

    suspend fun selectCity(lat: String,lng: String,userid: String,isSpi: String, srilanka: String) {
        selectCityLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.selectCity(lat,lng,Constant.version,Constant.platform,userid,isSpi,srilanka)
        selectCityResponse(response)
    }

    private fun selectCityResponse(response: Response<SelectCityResponse>) {
        if (response.isSuccessful && response.body() != null) {
            selectCityLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            selectCityLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            selectCityLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // Home Data
    private val homeLiveData = MutableLiveData<NetworkResult<HomeResponse>>()
    val homeResponseLiveData: LiveData<NetworkResult<HomeResponse>>
        get() = homeLiveData

    suspend fun homeData(
        city: String,
        dtmsource: String,
        userid: String,
        mobile: String,
        upbooking: Boolean,
        srilanka: String,
        type: String,
        lng: String,
        gener: String,
        spShow: String,
        isSpi: String
    ) {
        selectCityLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.home(city,Constant.version,Constant.platform,dtmsource,userid,mobile,
        upbooking,srilanka,type,lng,gener,spShow,isSpi)
        homeResponse(response)
    }

    private fun homeResponse(response: Response<HomeResponse>) {
        if (response.isSuccessful && response.body() != null) {
            homeLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            homeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            homeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}