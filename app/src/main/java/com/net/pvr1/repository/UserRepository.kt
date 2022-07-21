package com.net.pvr1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.net.pvr1.api.UserAPI
import com.net.pvr1.ui.login.response.LoginResponse
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

}