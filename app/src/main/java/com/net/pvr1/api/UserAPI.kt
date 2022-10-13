package com.net.pvr1.api

import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.movieDetails.response.MovieDetailsResponse
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.ui.myBookings.response.GiftCardResponse
import com.net.pvr1.ui.offer.response.OfferResponse
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr1.ui.seatLayout.response.InitResponse
import com.net.pvr1.ui.seatLayout.response.ReserveSeatResponse
import com.net.pvr1.ui.seatLayout.response.SeatResponse
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import com.net.pvr1.ui.summery.response.AddFoodResponse
import com.net.pvr1.ui.summery.response.SummeryResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
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
        @Header("X-Token") token: String,
        @Query("cname") cName: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ResisterResponse>

    @POST("v2/user/register")
    suspend fun resister(
        @Header("X-Token") hash: String,
        @Query("email") email: String,
        @Query("name") name: String,
        @Query("mobile") mobile: String,
        @Query("otp") otp: String,
        @Query("city") city: String,
        @Query("cname") cname: Boolean,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ResisterResponse>

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

    @POST("deals/mdetail")
    suspend fun offerDetails(
        @Query("id") id: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("did")did: String,
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
        @Query("av") version: String,
        @Query("pt") platform: String,
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

    @POST("content/msessionsnew")
    suspend fun bookingSession(
        @Query("city") city: String,
        @Query("mid") mid: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("date") date: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("isSpi") isSpi: String,
        @Query("srilanka") srilanka: String,
        @Query("userid") userid: String
    ): Response<BookingResponse>

    @POST("content/movietheater")
    suspend fun bookingTheatre(
        @Query("city") city: String,
        @Query("cid") mid: String,
        @Query("userid") lat: String,
        @Query("mid") lng: String,
        @Query("av") version: String,
        @Query("pt") platform: String,
        @Query("isSpi") isSpi: String
    ): Response<BookingTheatreResponse>

    @POST("content/nearcinetheater")
    suspend fun nearTheatre(
        @Query("city") city: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String?,
        @Query("cid") cid: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<CinemaNearTheaterResponse>

    @POST("trans/getseatlayoutnew/{cinemacode}/{sessionid}")
    suspend fun seatLayout(
        @Path("cinemacode") cinemacode: String,
        @Path("sessionid") sessionid: String,
        @Query("dtmsource") dtmsource: String,
        @Query("partnerid") partnerid: String,
        @Query("cdate") cdate: String,
        @Query("bundle") bundle: Boolean,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<SeatResponse>

    @POST("trans/reserveseats")
    suspend fun reserveSeatLayout(
        @Query("reserve") reserve: String,
        @Query("qr") qr: String,
        @Query("srilanka") srilanka: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<ReserveSeatResponse>

    @POST("trans/initTrans/{cinemacode}/{sessionid}")
    suspend fun initTransSeatLayout(
        @Path("cinemacode") cinemacode: String,
        @Path("sessionid") sessionid: String,
        @Query("dtmsource") dtmsource: String,
        @Query("partnerid") partnerid: String,
        @Query("screenid") screenid: String,
        @Query("adultSeats") adultSeats: String,
        @Query("childSeats") childSeats: String,
        @Query("oldBookingId") oldBookingId: String,
        @Query("isMembersDay") isMembersDay: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<InitResponse>

    @POST("v2/food/getfoods")
    suspend fun food(
        @Query("userid") userid: String,
        @Query("ccode") ccode: String,
        @Query("bookingid") bookingid: String,
        @Query("cbookid") cbookid: String,
        @Query("transid") transid: String,
        @Query("type") type: String,
        @Query("audi") audi: String,
        @Query("seat") seat: String,
        @Query("city") city: String,
        @Query("qr") qr: String,
        @Query("iserv") iserv: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<FoodResponse>

    @POST("trans/tckdetails")
    suspend fun summery(
        @Query("transid") transid: String,
        @Query("cinemacode") cinemacode: String,
        @Query("userid") userid: String,
        @Query("bookingid") bookingid: String,
        @Query("qr") qr: String,
        @Query("srilanka") srilanka: String,
        @Query("infosys") infosys: String,
        @Query("isSpi") isSpi: String,
        @Query("doreq") doreq: Boolean,
        @Query("oldBookingId") oldBookingId: String,
        @Query("change") change: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<SummeryResponse>

    @POST("trans/tckdetails")
    suspend fun addFood(
        @Query("foods")  foods: String,
        @Query("transid")  transid: String,
        @Query("cinemacode") cinemacode: String,
        @Query("qr") qr: String,
        @Query("infosys") infosys: String,
        @Query("isSpi") isSpi: String,
        @Query("av") version: String,
        @Query("pt") platform: String
    ): Response<AddFoodResponse>

    @POST("/PVRCinemasCMS/getgiftcard1  ")
    suspend fun giftCardMain(
        @Query("sWidth")sWidth: String,
        @Query("platform") platform: String,
        @Query("infosys") infosys: String,
        @Query("av") version: String,
        @Query("platform") platform1: String
    ): Response<com.net.pvr1.ui.giftCard.response.GiftCardResponse>

}