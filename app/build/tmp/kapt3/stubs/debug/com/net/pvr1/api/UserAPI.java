package com.net.pvr1.api;

import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse;
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse;
import com.net.pvr1.ui.login.response.LoginResponse;
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse;
import com.net.pvr1.ui.myBookings.response.GiftCardResponse;
import com.net.pvr1.ui.offer.response.OfferResponse;
import com.net.pvr1.ui.selectCity.response.SelectCityResponse;
import retrofit2.Response;
import retrofit2.http.POST;
import retrofit2.http.Query;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J]\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\u00062\b\b\u0001\u0010\t\u001a\u00020\u00062\b\b\u0001\u0010\n\u001a\u00020\u00062\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJS\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0010\u001a\u00020\u00062\b\b\u0001\u0010\u0011\u001a\u00020\u00062\b\b\u0001\u0010\n\u001a\u00020\u00062\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012Jg\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u00032\b\b\u0001\u0010\n\u001a\u00020\u00062\b\b\u0001\u0010\u0015\u001a\u00020\u00062\b\b\u0001\u0010\u0016\u001a\u00020\u00062\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0017\u001a\u00020\u00062\b\b\u0001\u0010\u0018\u001a\u00020\u00062\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J?\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u00032\b\b\u0001\u0010\n\u001a\u00020\u00062\b\b\u0001\u0010\u0015\u001a\u00020\u00062\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001cJI\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\u00032\b\b\u0001\u0010\u001f\u001a\u00020\u00062\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010 \u001a\u00020\u00062\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010!J5\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u00032\b\b\u0001\u0010\u0015\u001a\u00020\u00062\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$J?\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001e0\u00032\b\b\u0001\u0010\u001f\u001a\u00020\u00062\b\b\u0001\u0010&\u001a\u00020\u00062\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001cJ]\u0010\'\u001a\b\u0012\u0004\u0012\u00020(0\u00032\b\b\u0001\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\u00062\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\u00062\b\b\u0001\u0010\n\u001a\u00020\u00062\b\b\u0001\u0010\u0017\u001a\u00020\u00062\b\b\u0001\u0010)\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\r\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006*"}, d2 = {"Lcom/net/pvr1/api/UserAPI;", "", "cinema", "Lretrofit2/Response;", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse;", "city", "", "lat", "lng", "searchTxt", "userid", "version", "platform", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "comingSoon", "Lcom/net/pvr1/ui/home/fragment/commingSoon/response/CommingSoonResponse;", "genre", "lang", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "foodTicket", "Lcom/net/pvr1/ui/myBookings/response/FoodTicketResponse;", "did", "sriLanka", "isSpi", "past", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "giftCard", "Lcom/net/pvr1/ui/myBookings/response/GiftCardResponse;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loginMobile", "Lcom/net/pvr1/ui/login/response/LoginResponse;", "mobile", "cName", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "offer", "Lcom/net/pvr1/ui/offer/response/OfferResponse;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "otpVerify", "token", "selectCity", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse;", "srilanka", "app_debug"})
public abstract interface UserAPI {
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "v2/user/login")
    public abstract java.lang.Object loginMobile(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "mobile")
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "city")
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "cname")
    java.lang.String cName, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "av")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "pt")
    java.lang.String platform, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.net.pvr1.ui.login.response.LoginResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "v2/user/verify")
    public abstract java.lang.Object otpVerify(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "mobile")
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "token")
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "av")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "pt")
    java.lang.String platform, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.net.pvr1.ui.login.response.LoginResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "content/comingsoon/v2")
    public abstract java.lang.Object comingSoon(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "city")
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "genre")
    java.lang.String genre, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "lang")
    java.lang.String lang, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "userid")
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "av")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "pt")
    java.lang.String platform, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "content/alltheater")
    public abstract java.lang.Object cinema(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "city")
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "lat")
    java.lang.String lat, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "lng")
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "searchtxt")
    java.lang.String searchTxt, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "userid")
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "av")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "pt")
    java.lang.String platform, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "history/giftcard")
    public abstract java.lang.Object giftCard(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "userid")
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "did")
    java.lang.String did, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "av")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "pt")
    java.lang.String platform, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.net.pvr1.ui.myBookings.response.GiftCardResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "history/history-new/myticket")
    public abstract java.lang.Object foodTicket(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "userid")
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "did")
    java.lang.String did, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "srilanka")
    java.lang.String sriLanka, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "city")
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "isSpi")
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "past")
    java.lang.String past, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "av")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "pt")
    java.lang.String platform, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.net.pvr1.ui.myBookings.response.FoodTicketResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "deals/mobile")
    public abstract java.lang.Object offer(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "did")
    java.lang.String did, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "av")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "pt")
    java.lang.String platform, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.net.pvr1.ui.offer.response.OfferResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "content/cities")
    public abstract java.lang.Object selectCity(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "lat")
    java.lang.String lat, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "lng")
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "av")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "pt")
    java.lang.String platform, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "userid")
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "isSpi")
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "srilanka")
    java.lang.String srilanka, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.net.pvr1.ui.selectCity.response.SelectCityResponse>> continuation);
}