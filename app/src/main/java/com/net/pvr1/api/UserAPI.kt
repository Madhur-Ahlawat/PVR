package com.net.pvr1.api

import com.net.pvr1.ui.login.response.LoginResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface UserAPI {

    @POST("user/login")
    suspend fun loginMobile(
        @Query("mobile") mobile: String,
        @Query("city") city: String,
        @Query("cname") cName: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<LoginResponse>

    @POST("user/verify")
    suspend fun otpVerify(
        @Query("mobile") mobile: String,
        @Query("token") token: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<LoginResponse>

}