package com.net.pvr1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.net.pvr1.api.UserAPI
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse
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
import com.net.pvr1.ui.seatLayout.response.SeatResponse
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

    suspend fun loginMobile(mobile: String, city: String, cName: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.loginMobile(mobile, city, cName, Constant.version, Constant.platform)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Otp Verify
    private val otpVerifyLiveData = MutableLiveData<NetworkResult<LoginResponse>>()
    val otpVerifyResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = otpVerifyLiveData

    suspend fun otpVerify(mobile: String, token: String) {
        otpVerifyLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.otpVerify(mobile, token, Constant.version, Constant.platform)
        verifyResponse(response)
    }

    private fun verifyResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful && response.body() != null) {
            otpVerifyLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            otpVerifyLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            otpVerifyLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //ComingSoon
    private val comingSoonLiveData = MutableLiveData<NetworkResult<CommingSoonResponse>>()
    val comingSoonResponseLiveData: LiveData<NetworkResult<CommingSoonResponse>>
        get() = comingSoonLiveData

    suspend fun comingSoon(city: String, genre: String, lang: String, userid: String) {
        comingSoonLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.comingSoon(
            city, genre, lang, userid, Constant.version,
            Constant.platform
        )
        comingSoonResponse(response)
    }

    private fun comingSoonResponse(response: Response<CommingSoonResponse>) {
        if (response.isSuccessful && response.body() != null) {
            comingSoonLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            comingSoonLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            comingSoonLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Cinema
    private val cinemaLiveData = MutableLiveData<NetworkResult<CinemaResponse>>()
    val cinemaResponseLiveData: LiveData<NetworkResult<CinemaResponse>>
        get() = cinemaLiveData

    suspend fun cinema(city: String, lat: String, lng: String, userid: String, searchTxt: String) {
        cinemaLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.cinema(
            city, lat, lng, userid, searchTxt, Constant.version,
            Constant.platform
        )
        cinemaResponse(response)
    }

    private fun cinemaResponse(response: Response<CinemaResponse>) {
        if (response.isSuccessful && response.body() != null) {
            cinemaLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            cinemaLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            cinemaLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //GiftCard
    private val giftCardLiveData = MutableLiveData<NetworkResult<GiftCardResponse>>()
    val giftCardResponseLiveData: LiveData<NetworkResult<GiftCardResponse>>
        get() = giftCardLiveData

    suspend fun giftCard(userId: String, did: String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.giftCard(
            userId, did, Constant.version,
            Constant.platform
        )
        giftCardResponse(response)
    }

    private fun giftCardResponse(response: Response<GiftCardResponse>) {
        if (response.isSuccessful && response.body() != null) {
            giftCardLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            giftCardLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
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
        val response = userAPI.foodTicket(
            userId, did, sriLanka, city, isSpi, past, Constant.version,
            Constant.platform
        )
        foodTicketResponse(response)
    }

    private fun foodTicketResponse(response: Response<FoodTicketResponse>) {
        if (response.isSuccessful && response.body() != null) {
            foodTicketLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            foodTicketLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            foodTicketLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Offer
    private val offerLiveData = MutableLiveData<NetworkResult<OfferResponse>>()
    val offerResponseLiveData: LiveData<NetworkResult<OfferResponse>>
        get() = offerLiveData

    suspend fun offer(did: String) {
        giftCardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.offer(did, Constant.version, Constant.platform)
        offerResponse(response)
    }

    private fun offerResponse(response: Response<OfferResponse>) {
        if (response.isSuccessful && response.body() != null) {
            offerLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            offerLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            offerLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    // Select City
    private val selectCityLiveData = MutableLiveData<NetworkResult<SelectCityResponse>>()
    val citiesResponseLiveData: LiveData<NetworkResult<SelectCityResponse>>
        get() = selectCityLiveData

    suspend fun selectCity(
        lat: String,
        lng: String,
        userid: String,
        isSpi: String,
        srilanka: String
    ) {
        selectCityLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.selectCity(
            lat,
            lng,
            Constant.version,
            Constant.platform,
            userid,
            isSpi,
            srilanka
        )
        selectCityResponse(response)
    }

    private fun selectCityResponse(response: Response<SelectCityResponse>) {
        if (response.isSuccessful && response.body() != null) {
            selectCityLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            selectCityLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
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
        val response = userAPI.home(
            city, Constant.version, Constant.platform, dtmsource, userid, mobile,
            upbooking, srilanka, type, lng, gener, spShow, isSpi
        )
        homeResponse(response)
    }

    private fun homeResponse(response: Response<HomeResponse>) {
        if (response.isSuccessful && response.body() != null) {
            homeLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            homeLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            homeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //Search
    private val homeSearchLiveData = MutableLiveData<NetworkResult<HomeSearchResponse>>()
    val searchResponseLiveData: LiveData<NetworkResult<HomeSearchResponse>>
        get() = homeSearchLiveData

    suspend fun homeSearchData(
        city: String,
        text: String,
        searchFilter: String,
        lat: String,
        lng: String
    ) {
        homeSearchLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.homeSearch(
            city,
            text,
            searchFilter,
            lat,
            lng,
            Constant.version,
            Constant.platform
        )
        homeSearchResponse(response)
    }

    private fun homeSearchResponse(response: Response<HomeSearchResponse>) {
        if (response.isSuccessful && response.body() != null) {
            homeSearchLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            homeSearchLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            homeSearchLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //MovieDetails
    private val movieDetailsLiveData = MutableLiveData<NetworkResult<MovieDetailsResponse>>()
    val movieDetailsResponseLiveData: LiveData<NetworkResult<MovieDetailsResponse>>
        get() = movieDetailsLiveData

    suspend fun movieDetailsData(
        city: String,
        mid: String,
        type: String,
        userid: String,
        lat: String,
        lng: String,
        isSpi: String,
        srilanka: String
    ) {
        movieDetailsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.movieDetails(
            city,
            mid,
            Constant.version,
            type,
            Constant.platform,
            userid,
            lat,
            lng,
            isSpi,
            srilanka
        )
        movieDetailsResponse(response)
    }

    private fun movieDetailsResponse(response: Response<MovieDetailsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            movieDetailsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            movieDetailsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            movieDetailsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //CinemaSession
    private val cinemaSessionLiveData = MutableLiveData<NetworkResult<CinemaSessionResponse>>()
    val cinemaSessionResponseLiveData: LiveData<NetworkResult<CinemaSessionResponse>>
        get() = cinemaSessionLiveData

    suspend fun cinemaSessionData(
        city: String,
        cid: String,
        lat: String,
        lng: String,
        userid: String,
        date: String,
        lang: String,
        format: String,
        price: String,
        time: String,
        hc: String,
        cc: String,
        ad: String,
        qr: String,
        cinetype: String,
        cinetypeQR: String
    ) {
        cinemaSessionLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.cinemaSession(
            city,
            cid,
            lat,
            lng,
            userid,
            date,
            Constant.version,
            Constant.platform,
            lang,
            format,
            price,
            time,
            hc,
            cc,
            ad,
            qr,
            cinetype,
            cinetypeQR
        )
        cinemaSessionResponse(response)
    }

    private fun cinemaSessionResponse(response: Response<CinemaSessionResponse>) {
        if (response.isSuccessful && response.body() != null) {
            cinemaSessionLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            cinemaSessionLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            cinemaSessionLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //BookingTicket
    private val bookingSessionLiveData = MutableLiveData<NetworkResult<BookingResponse>>()
    val bookingSessionResponseLiveData: LiveData<NetworkResult<BookingResponse>>
        get() = bookingSessionLiveData

    suspend fun bookingTicket(
        city: String,
        mid: String,
        lat: String,
        lng: String,
        date: String,
        isSpi: String,
        srilanka: String,
        userid: String
    ) {
        cinemaSessionLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.bookingSession(
            city, mid, lat, lng, date, Constant.version, Constant.platform, isSpi, srilanka, userid

        )
        bookingSessionResponse(response)
    }

    private fun bookingSessionResponse(response: Response<BookingResponse>) {
        if (response.isSuccessful && response.body() != null) {
            bookingSessionLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            bookingSessionLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            bookingSessionLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //BookingTheatre
    private val bookingTheatreLiveData = MutableLiveData<NetworkResult<BookingTheatreResponse>>()
    val bookingTheatreResponseLiveData: LiveData<NetworkResult<BookingTheatreResponse>>
        get() = bookingTheatreLiveData

    suspend fun bookingTheatre(
        city: String,
        cid: String,
        userid: String,
        mid: String,
        isSpi: String
    ) {
        cinemaSessionLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.bookingTheatre(
            city, cid, userid, mid, Constant.version, Constant.platform, isSpi
        )
        bookingTheatreResponse(response)
    }

    private fun bookingTheatreResponse(response: Response<BookingTheatreResponse>) {
        if (response.isSuccessful && response.body() != null) {
            bookingTheatreLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            bookingTheatreLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            bookingTheatreLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //NearbyTheatre
    private val nearTheaterLiveData = MutableLiveData<NetworkResult<CinemaNearTheaterResponse>>()
    val nearTheaterResponseLiveData: LiveData<NetworkResult<CinemaNearTheaterResponse>>
        get() = nearTheaterLiveData

    suspend fun nearTheater(city: String, lat: String, lng: String?, cid: String) {
        nearTheaterLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.nearTheatre(
            city, lat, lng, cid, Constant.version, Constant.platform
        )
        nearTheatreResponse(response)
    }

    private fun nearTheatreResponse(response: Response<CinemaNearTheaterResponse>) {
        if (response.isSuccessful && response.body() != null) {
            nearTheaterLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            nearTheaterLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            nearTheaterLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    //SeatLayout
    private val seatLiveData = MutableLiveData<NetworkResult<SeatResponse>>()
    val seatResponseLiveData: LiveData<NetworkResult<SeatResponse>>
        get() = seatLiveData

    suspend fun seatLayout(
        cinemacode: String,
        sessionid: String,
        dtmsource: String,
        partnerid: String,
        cdate: String,
        bundle: Boolean,
        isSpi: String
    ) {
        seatLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.seatLayout(
           cinemacode,sessionid,dtmsource,partnerid,cdate,bundle,isSpi, Constant.version, Constant.platform
        )
        seatLayoutResponse(response)
    }

    private fun seatLayoutResponse(response: Response<SeatResponse>) {
        if (response.isSuccessful && response.body() != null) {
            seatLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            seatLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            seatLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


}