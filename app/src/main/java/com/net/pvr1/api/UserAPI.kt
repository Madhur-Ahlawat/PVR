package com.net.pvr1.api

import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.movieDetails.response.MovieDetailsResponse
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.ui.myBookings.response.GiftCardResponse
import com.net.pvr1.ui.offer.response.OfferResponse
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
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

    @POST("history/giftcard")
    suspend fun giftCard(
        @Query("userid") userid: String,
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<GiftCardResponse>

    @POST("history/history-new/myticket")
    suspend fun foodTicket(
        @Query("userid") userid: String,
        @Query("did") did: String,
        @Query("srilanka") sriLanka: String,
        @Query("city") city: String,
        @Query("isSpi") isSpi: String,
        @Query("past") past: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
    ): Response<FoodTicketResponse>

    @POST("deals/mobile")
    suspend fun offer(
        @Query("did") did: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
    ): Response<OfferResponse>

    @POST("content/cities")
    suspend fun selectCity(
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("userid") userid: String,
        @Query("isSpi") isSpi: String,
        @Query("srilanka") srilanka: String
    ): Response<SelectCityResponse>

    @POST("content/nowshowingnew2")
    suspend fun home(
        @Query("city") city: String,
        @Query("av") av: String,
        @Query("pt") pt: String,
        @Query("dtmsource") dtmsource: String,
        @Query("userid") userid: String,
        @Query("mobile") mobile: String,
        @Query("upbooking") upbooking: Boolean,
        @Query("srilanka") srilanka: String,
        @Query("type") type: String,
        @Query("lng") lng: String,
        @Query("gener") gener: String,
        @Query("spShow") spShow: String,
        @Query("isSpi") isSpi: String,
    ): Response<HomeResponse>

    @POST("content/search")
    suspend fun homeSearch(
        @Query("city") city: String,
        @Query("text") text: String,
        @Query("searchFilter") searchFilter: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<HomeSearchResponse>

    @POST("content/getmovie")
    suspend fun movieDetails(
        @Query("city") city: String,
        @Query("mid") mid: String,
        @Query("av") version: String,
        @Query("type") type: String,
        @Query("pt") platform: String,
        @Query("userid") userid: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("isSpi") isSpi: String,
        @Query("srilanka") srilanka: String
    ): Response<MovieDetailsResponse>

    @POST("content/csessionsfilters")
    suspend fun cinemaSession(
        @Query("city") city: String,
        @Query("cid") cid: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("userid") userid: String,
        @Query("date") date: String,
        @Query("version") version: String,
        @Query("platform") platform: String,
        @Query("lang") lang: String,
        @Query("format") format: String,
        @Query("price") price: String,
        @Query("time") time: String,
        @Query("hc") hc: String,
        @Query("cc") cc: String,
        @Query("ad") ad: String,
        @Query("qr") qr: String,
        @Query("cinetype") cinetype: String,
        @Query("cinetypeQR") cinetypeQR: String
    ): Response<CinemaSessionResponse>

}