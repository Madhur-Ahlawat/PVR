package com.net.pvr1.api

import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.login.response.LoginResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface UserAPI {

    @POST("v2/user/login")
    suspend fun loginMobile(
        @Query("mobile") mobile: String,
        @Query("city") city: String,
        @Query("cname") cName: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<LoginResponse>


    @POST("v2/user/verify")
    suspend fun otpVerify(
        @Query("mobile") mobile: String,
        @Query("token") token: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<LoginResponse>

    @POST("content/comingsoon/v2")
    suspend fun comingSoon(
        @Query("city") city: String,
        @Query("genre") genre: String,
        @Query("lang") lang: String,
        @Query("userid") userid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CommingSoonResponse>

    @POST("content/alltheater")
    suspend fun cinema(
        @Query("city") city: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("searchtxt") searchTxt: String,
        @Query("userid") userid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CinemaResponse>

}