package com.net.pvr1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.net.pvr1.api.UserAPI
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.ln

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

}