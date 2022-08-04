package com.net.pvr1.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.net.pvr1.api.UserAPI;
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse;
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse;
import com.net.pvr1.ui.login.response.LoginResponse;
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse;
import com.net.pvr1.ui.myBookings.response.GiftCardResponse;
import com.net.pvr1.ui.offer.response.OfferResponse;
import com.net.pvr1.ui.selectCity.response.SelectCityResponse;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import org.json.JSONObject;
import retrofit2.Response;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u001f\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J9\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020+2\u0006\u0010-\u001a\u00020+2\u0006\u0010.\u001a\u00020+2\u0006\u0010/\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100J\u0016\u00101\u001a\u00020)2\f\u00102\u001a\b\u0012\u0004\u0012\u00020\n03H\u0002J1\u00104\u001a\u00020)2\u0006\u0010*\u001a\u00020+2\u0006\u00105\u001a\u00020+2\u0006\u00106\u001a\u00020+2\u0006\u0010.\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00107J\u0016\u00108\u001a\u00020)2\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u001303H\u0002JA\u00109\u001a\u00020)2\u0006\u0010:\u001a\u00020+2\u0006\u0010;\u001a\u00020+2\u0006\u0010<\u001a\u00020+2\u0006\u0010*\u001a\u00020+2\u0006\u0010=\u001a\u00020+2\u0006\u0010>\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010?J\u0016\u0010@\u001a\u00020)2\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u001703H\u0002J!\u0010A\u001a\u00020)2\u0006\u0010:\u001a\u00020+2\u0006\u0010;\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010BJ\u0016\u0010C\u001a\u00020)2\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u001b03H\u0002J\u0016\u0010D\u001a\u00020)2\f\u00102\u001a\b\u0012\u0004\u0012\u00020\b03H\u0002J)\u0010E\u001a\u00020)2\u0006\u0010F\u001a\u00020+2\u0006\u0010*\u001a\u00020+2\u0006\u0010G\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010HJ\u0019\u0010I\u001a\u00020)2\u0006\u0010;\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010JJ\u0016\u0010K\u001a\u00020)2\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u001f03H\u0002J!\u0010L\u001a\u00020)2\u0006\u0010F\u001a\u00020+2\u0006\u0010M\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010BJ9\u0010N\u001a\u00020)2\u0006\u0010,\u001a\u00020+2\u0006\u0010-\u001a\u00020+2\u0006\u0010.\u001a\u00020+2\u0006\u0010=\u001a\u00020+2\u0006\u0010O\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100J\u0016\u0010P\u001a\u00020)2\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u001003H\u0002J\u0016\u0010Q\u001a\u00020)2\f\u00102\u001a\b\u0012\u0004\u0012\u00020\b03H\u0002R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u001d\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u000eR\u001a\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u000eR\u001a\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u000eR\u001a\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u000eR\u001a\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u000eR\u001a\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b$\u0010\u000eR\u001a\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\'\u0010\u000e\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006R"}, d2 = {"Lcom/net/pvr1/repository/UserRepository;", "", "userAPI", "Lcom/net/pvr1/api/UserAPI;", "(Lcom/net/pvr1/api/UserAPI;)V", "_userResponseLiveData", "Landroidx/lifecycle/MutableLiveData;", "Lcom/net/pvr1/utils/NetworkResult;", "Lcom/net/pvr1/ui/login/response/LoginResponse;", "cinemaLiveData", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse;", "cinemaResponseLiveData", "Landroidx/lifecycle/LiveData;", "getCinemaResponseLiveData", "()Landroidx/lifecycle/LiveData;", "citiesResponseLiveData", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse;", "getCitiesResponseLiveData", "comingSoonLiveData", "Lcom/net/pvr1/ui/home/fragment/commingSoon/response/CommingSoonResponse;", "comingSoonResponseLiveData", "getComingSoonResponseLiveData", "foodTicketLiveData", "Lcom/net/pvr1/ui/myBookings/response/FoodTicketResponse;", "foodTicketResponseLiveData", "getFoodTicketResponseLiveData", "giftCardLiveData", "Lcom/net/pvr1/ui/myBookings/response/GiftCardResponse;", "giftCardResponseLiveData", "getGiftCardResponseLiveData", "offerLiveData", "Lcom/net/pvr1/ui/offer/response/OfferResponse;", "offerResponseLiveData", "getOfferResponseLiveData", "otpVerifyLiveData", "otpVerifyResponseLiveData", "getOtpVerifyResponseLiveData", "selectCityLiveData", "userResponseLiveData", "getUserResponseLiveData", "cinema", "", "city", "", "lat", "lng", "userid", "searchTxt", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cinemaResponse", "response", "Lretrofit2/Response;", "comingSoon", "genre", "lang", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "comingSoonResponse", "foodTicket", "userId", "did", "sriLanka", "isSpi", "past", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "foodTicketResponse", "giftCard", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "giftCardResponse", "handleResponse", "loginMobile", "mobile", "cName", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "offer", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "offerResponse", "otpVerify", "token", "selectCity", "srilanka", "selectCityResponse", "verifyResponse", "app_debug"})
public final class UserRepository {
    private final com.net.pvr1.api.UserAPI userAPI = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.response.LoginResponse>> _userResponseLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.response.LoginResponse>> otpVerifyLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse>> comingSoonLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse>> cinemaLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.myBookings.response.GiftCardResponse>> giftCardLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.myBookings.response.FoodTicketResponse>> foodTicketLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.offer.response.OfferResponse>> offerLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.selectCity.response.SelectCityResponse>> selectCityLiveData = null;
    
    @javax.inject.Inject()
    public UserRepository(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.api.UserAPI userAPI) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.response.LoginResponse>> getUserResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loginMobile(@org.jetbrains.annotations.NotNull()
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String cName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void handleResponse(retrofit2.Response<com.net.pvr1.ui.login.response.LoginResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.response.LoginResponse>> getOtpVerifyResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object otpVerify(@org.jetbrains.annotations.NotNull()
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void verifyResponse(retrofit2.Response<com.net.pvr1.ui.login.response.LoginResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse>> getComingSoonResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object comingSoon(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String genre, @org.jetbrains.annotations.NotNull()
    java.lang.String lang, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void comingSoonResponse(retrofit2.Response<com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse>> getCinemaResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object cinema(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String lat, @org.jetbrains.annotations.NotNull()
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String searchTxt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void cinemaResponse(retrofit2.Response<com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.myBookings.response.GiftCardResponse>> getGiftCardResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object giftCard(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String did, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void giftCardResponse(retrofit2.Response<com.net.pvr1.ui.myBookings.response.GiftCardResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.myBookings.response.FoodTicketResponse>> getFoodTicketResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object foodTicket(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String did, @org.jetbrains.annotations.NotNull()
    java.lang.String sriLanka, @org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    java.lang.String past, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void foodTicketResponse(retrofit2.Response<com.net.pvr1.ui.myBookings.response.FoodTicketResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.offer.response.OfferResponse>> getOfferResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object offer(@org.jetbrains.annotations.NotNull()
    java.lang.String did, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void offerResponse(retrofit2.Response<com.net.pvr1.ui.offer.response.OfferResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.selectCity.response.SelectCityResponse>> getCitiesResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object selectCity(@org.jetbrains.annotations.NotNull()
    java.lang.String lat, @org.jetbrains.annotations.NotNull()
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    java.lang.String srilanka, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void selectCityResponse(retrofit2.Response<com.net.pvr1.ui.selectCity.response.SelectCityResponse> response) {
    }
}