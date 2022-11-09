package com.net.pvr1.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.net.pvr1.api.UserAPI;
import com.net.pvr1.ui.bookingSession.response.BookingResponse;
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse;
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse;
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse;
import com.net.pvr1.ui.food.response.FoodResponse;
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse;
import com.net.pvr1.ui.home.fragment.cinema.response.PreferenceResponse;
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse;
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse;
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse;
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse;
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse;
import com.net.pvr1.ui.login.response.LoginResponse;
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse;
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse;
import com.net.pvr1.ui.myBookings.response.GiftCardResponse;
import com.net.pvr1.ui.offer.response.OfferResponse;
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse;
import com.net.pvr1.ui.seatLayout.response.InitResponse;
import com.net.pvr1.ui.seatLayout.response.ReserveSeatResponse;
import com.net.pvr1.ui.seatLayout.response.SeatResponse;
import com.net.pvr1.ui.selectCity.response.SelectCityResponse;
import com.net.pvr1.ui.splash.response.SplashResponse;
import com.net.pvr1.ui.summery.response.AddFoodResponse;
import com.net.pvr1.ui.summery.response.SummeryResponse;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import org.json.JSONObject;
import retrofit2.Response;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0080\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\br\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J>\u0010|\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u0080\u0001\u001a\u00020\u007f2\u0007\u0010\u0081\u0001\u001a\u00020\u007f2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0007\u0010\u0083\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u0084\u0001J\u0019\u0010\u0085\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020\n0\u0087\u0001H\u0002J\u0019\u0010\u0088\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020\u00100\u0087\u0001H\u0002J?\u0010\u0089\u0001\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u008a\u0001\u001a\u00020\u007f2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0007\u0010\u008b\u0001\u001a\u00020\u007f2\u0007\u0010\u008c\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u0084\u0001J\u0019\u0010\u008d\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020\u00140\u0087\u0001H\u0002JZ\u0010\u008e\u0001\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u008b\u0001\u001a\u00020\u007f2\u0007\u0010\u0080\u0001\u001a\u00020\u007f2\u0007\u0010\u0081\u0001\u001a\u00020\u007f2\u0007\u0010\u008f\u0001\u001a\u00020\u007f2\u0007\u0010\u008c\u0001\u001a\u00020\u007f2\u0007\u0010\u0090\u0001\u001a\u00020\u007f2\u0007\u0010\u0082\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u0091\u0001J?\u0010\u0092\u0001\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u0080\u0001\u001a\u00020\u007f2\u0007\u0010\u0081\u0001\u001a\u00020\u007f2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0007\u0010\u0093\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u0084\u0001JA\u0010\u0094\u0001\u001a\u00020}2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0007\u0010\u0095\u0001\u001a\u00020\u007f2\b\u0010\u0096\u0001\u001a\u00030\u0097\u00012\u0007\u0010\u0098\u0001\u001a\u00020\u007f2\u0007\u0010\u0099\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u009a\u0001J\u0019\u0010\u009b\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020\u001a0\u0087\u0001H\u0002J\u0019\u0010\u009c\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020\u00180\u0087\u0001H\u0002J\u00a2\u0001\u0010\u009d\u0001\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u008a\u0001\u001a\u00020\u007f2\u0007\u0010\u0080\u0001\u001a\u00020\u007f2\u0007\u0010\u0081\u0001\u001a\u00020\u007f2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0007\u0010\u008f\u0001\u001a\u00020\u007f2\u0007\u0010\u009e\u0001\u001a\u00020\u007f2\u0007\u0010\u009f\u0001\u001a\u00020\u007f2\u0007\u0010\u00a0\u0001\u001a\u00020\u007f2\u0007\u0010\u00a1\u0001\u001a\u00020\u007f2\u0007\u0010\u00a2\u0001\u001a\u00020\u007f2\u0007\u0010\u00a3\u0001\u001a\u00020\u007f2\u0007\u0010\u00a4\u0001\u001a\u00020\u007f2\u0007\u0010\u00a5\u0001\u001a\u00020\u007f2\u0007\u0010\u00a6\u0001\u001a\u00020\u007f2\u0007\u0010\u00a7\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00a8\u0001J\u0019\u0010\u00a9\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020 0\u0087\u0001H\u0002J6\u0010\u00aa\u0001\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u00ab\u0001\u001a\u00020\u007f2\u0007\u0010\u009e\u0001\u001a\u00020\u007f2\u0007\u0010\u0082\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00ac\u0001J\u0019\u0010\u00ad\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020\'0\u0087\u0001H\u0002J6\u0010\u00ae\u0001\u001a\u00020}2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u00af\u0001\u001a\u00020\u007f2\u0007\u0010\u0099\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00ac\u0001J\u0019\u0010\u00b0\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020+0\u0087\u0001H\u0002J\u007f\u0010\u00b1\u0001\u001a\u00020}2\u0007\u0010\u00b2\u0001\u001a\u00020\u007f2\u0007\u0010\u00b3\u0001\u001a\u00020\u007f2\u0007\u0010\u00b4\u0001\u001a\u00020\u007f2\u0007\u0010\u00b5\u0001\u001a\u00020\u007f2\u0007\u0010\u00b6\u0001\u001a\u00020\u007f2\u0007\u0010\u00b7\u0001\u001a\u00020\u007f2\u0007\u0010\u00b8\u0001\u001a\u00020\u007f2\u0007\u0010\u0098\u0001\u001a\u00020\u007f2\u0007\u0010\u00b9\u0001\u001a\u00020\u007f2\u0007\u0010\u00a5\u0001\u001a\u00020\u007f2\u0007\u0010\u008c\u0001\u001a\u00020\u007f2\u0007\u0010\u0090\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00ba\u0001J\u0019\u0010\u00bb\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020/0\u0087\u0001H\u0002J~\u0010\u00bc\u0001\u001a\u00020}2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0007\u0010\u00bd\u0001\u001a\u00020\u007f2\u0007\u0010\u00be\u0001\u001a\u00020\u007f2\u0007\u0010\u00b6\u0001\u001a\u00020\u007f2\u0007\u0010\u00bf\u0001\u001a\u00020\u007f2\u0007\u0010\u0098\u0001\u001a\u00020\u007f2\u0007\u0010\u00b7\u0001\u001a\u00020\u007f2\u0007\u0010\u00b8\u0001\u001a\u00020\u007f2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u00a5\u0001\u001a\u00020\u007f2\u0007\u0010\u00c0\u0001\u001a\u00020\u007f2\u0007\u0010\u008c\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00ba\u0001J\u0019\u0010\u00c1\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u0002030\u0087\u0001H\u0002JH\u0010\u00c2\u0001\u001a\u00020}2\u0007\u0010\u00c3\u0001\u001a\u00020\u007f2\u0007\u0010\u0099\u0001\u001a\u00020\u007f2\u0007\u0010\u00c4\u0001\u001a\u00020\u007f2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u008c\u0001\u001a\u00020\u007f2\u0007\u0010\u00c5\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00c6\u0001J\u0019\u0010\u00c7\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u0002070\u0087\u0001H\u0002J%\u0010\u00c8\u0001\u001a\u00020}2\u0007\u0010\u00c3\u0001\u001a\u00020\u007f2\u0007\u0010\u0099\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00c9\u0001J%\u0010\u00ca\u0001\u001a\u00020}2\u0007\u0010\u00cb\u0001\u001a\u00020\u007f2\u0007\u0010\u00b9\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00c9\u0001J\u0019\u0010\u00cc\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020;0\u0087\u0001H\u0002J\u0019\u0010\u00cd\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020;0\u0087\u0001H\u0002J\u0019\u0010\u00ce\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020\b0\u0087\u0001H\u0002Jv\u0010\u00cf\u0001\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u00d0\u0001\u001a\u00020\u007f2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0007\u0010\u00d1\u0001\u001a\u00020\u007f2\b\u0010\u00d2\u0001\u001a\u00030\u0097\u00012\u0007\u0010\u0090\u0001\u001a\u00020\u007f2\u0007\u0010\u0098\u0001\u001a\u00020\u007f2\u0007\u0010\u0081\u0001\u001a\u00020\u007f2\u0007\u0010\u00d3\u0001\u001a\u00020\u007f2\u0007\u0010\u00d4\u0001\u001a\u00020\u007f2\u0007\u0010\u008c\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00d5\u0001J\u0019\u0010\u00d6\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020B0\u0087\u0001H\u0002J?\u0010\u00d7\u0001\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u00d8\u0001\u001a\u00020\u007f2\u0007\u0010\u00d9\u0001\u001a\u00020\u007f2\u0007\u0010\u0080\u0001\u001a\u00020\u007f2\u0007\u0010\u0081\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u0084\u0001J\u0019\u0010\u00da\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020F0\u0087\u0001H\u0002J%\u0010\u00db\u0001\u001a\u00020}2\u0007\u0010\u00b2\u0001\u001a\u00020\u007f2\u0007\u0010\u00dc\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00c9\u0001J\u0019\u0010\u00dd\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020H0\u0087\u0001H\u0002J-\u0010\u00de\u0001\u001a\u00020}2\u0007\u0010\u00d1\u0001\u001a\u00020\u007f2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u00df\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00e0\u0001JZ\u0010\u00e1\u0001\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u008b\u0001\u001a\u00020\u007f2\u0007\u0010\u0098\u0001\u001a\u00020\u007f2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0007\u0010\u0080\u0001\u001a\u00020\u007f2\u0007\u0010\u0081\u0001\u001a\u00020\u007f2\u0007\u0010\u008c\u0001\u001a\u00020\u007f2\u0007\u0010\u0090\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u0091\u0001J\u0019\u0010\u00e2\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020+0\u0087\u0001H\u0002J8\u0010\u00e3\u0001\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007f2\u0007\u0010\u0080\u0001\u001a\u00020\u007f2\t\u0010\u0081\u0001\u001a\u0004\u0018\u00010\u007f2\u0007\u0010\u008a\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00ac\u0001J\u0019\u0010\u00e4\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020O0\u0087\u0001H\u0002J\u001c\u0010\u00e5\u0001\u001a\u00020}2\u0007\u0010\u0099\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00e6\u0001J%\u0010\u00e7\u0001\u001a\u00020}2\u0007\u0010\u0095\u0001\u001a\u00020\u007f2\u0007\u0010\u0099\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00c9\u0001J\u0019\u0010\u00e8\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020S0\u0087\u0001H\u0002J\u0019\u0010\u00e9\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020S0\u0087\u0001H\u0002J%\u0010\u00ea\u0001\u001a\u00020}2\u0007\u0010\u00d1\u0001\u001a\u00020\u007f2\u0007\u0010\u00eb\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00c9\u0001J$\u0010\u00ec\u0001\u001a\u00020}2\u0007\u0010\u00d1\u0001\u001a\u00020\u007f2\u0006\u0010~\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00c9\u0001J\u0019\u0010\u00ed\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020^0\u0087\u0001H\u0002J\u001c\u0010\u00ee\u0001\u001a\u00020}2\u0007\u0010\u00ef\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00e6\u0001J\u0019\u0010\u00f0\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020b0\u0087\u0001H\u0002JR\u0010\u00f1\u0001\u001a\u00020}2\u0007\u0010\u00f2\u0001\u001a\u00020\u007f2\u0007\u0010\u00f3\u0001\u001a\u00020\u007f2\u0007\u0010\u00f4\u0001\u001a\u00020\u007f2\u0007\u0010\u00d1\u0001\u001a\u00020\u007f2\u0007\u0010\u00f5\u0001\u001a\u00020\u007f2\u0006\u0010~\u001a\u00020\u007f2\b\u0010\u00f6\u0001\u001a\u00030\u0097\u0001H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00f7\u0001J\u0019\u0010\u00f8\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020Z0\u0087\u0001H\u0002JS\u0010\u00f9\u0001\u001a\u00020}2\u0007\u0010\u00b2\u0001\u001a\u00020\u007f2\u0007\u0010\u00dc\u0001\u001a\u00020\u007f2\u0007\u0010\u00d0\u0001\u001a\u00020\u007f2\u0007\u0010\u00fa\u0001\u001a\u00020\u007f2\u0007\u0010\u00fb\u0001\u001a\u00020\u007f2\b\u0010\u00fc\u0001\u001a\u00030\u0097\u00012\u0007\u0010\u008c\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00fd\u0001J\u0019\u0010\u00fe\u0001\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020k0\u0087\u0001H\u0002JI\u0010\u00ff\u0001\u001a\u00020}2\u0007\u0010\u0080\u0002\u001a\u00020\u007f2\u0007\u0010\u00bf\u0001\u001a\u00020\u007f2\u0007\u0010\u00b2\u0001\u001a\u00020\u007f2\u0007\u0010\u00a5\u0001\u001a\u00020\u007f2\u0007\u0010\u00b9\u0001\u001a\u00020\u007f2\u0007\u0010\u008c\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00c6\u0001J\u0019\u0010\u0081\u0002\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020o0\u0087\u0001H\u0002J@\u0010\u0082\u0002\u001a\u00020}2\u0007\u0010\u0080\u0001\u001a\u00020\u007f2\u0007\u0010\u0081\u0001\u001a\u00020\u007f2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0007\u0010\u008c\u0001\u001a\u00020\u007f2\u0007\u0010\u0090\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u0084\u0001J\u0019\u0010\u0083\u0002\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020$0\u0087\u0001H\u0002J\u001b\u0010\u0084\u0002\u001a\u00020}2\u0006\u0010~\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00e6\u0001J\u0019\u0010\u0085\u0002\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020t0\u0087\u0001H\u0002J7\u0010\u0086\u0002\u001a\u00020}2\u0007\u0010\u00bf\u0001\u001a\u00020\u007f2\u0007\u0010\u00b2\u0001\u001a\u00020\u007f2\u0007\u0010\u0082\u0001\u001a\u00020\u007f2\u0007\u0010\u00be\u0001\u001a\u00020\u007fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u00ac\u0001J\u0019\u0010\u0087\u0002\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020o0\u0087\u0001H\u0002J\u0019\u0010\u0088\u0002\u001a\u00020}2\u000e\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u00020Z0\u0087\u0001H\u0002R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u000eR\u001a\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u000eR\u001a\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u000eR\u001d\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\u001e\u0010\u000eR\u001a\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\"\u0010\u000eR\u001d\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020$0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b%\u0010\u000eR\u001a\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b)\u0010\u000eR\u001a\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020+0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020+0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b-\u0010\u000eR\u001a\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020/0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020/0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b1\u0010\u000eR\u001a\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002030\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u00104\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002030\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b5\u0010\u000eR\u001a\u00106\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002070\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002070\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b9\u0010\u000eR\u001a\u0010:\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020;0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020;0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020;0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b>\u0010\u000eR\u001d\u0010?\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020;0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b@\u0010\u000eR\u001a\u0010A\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020B0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010C\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020B0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bD\u0010\u000eR\u001a\u0010E\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020F0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010G\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020H0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010I\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020H0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bJ\u0010\u000eR\u001a\u0010K\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020+0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010L\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020+0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bM\u0010\u000eR\u001a\u0010N\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020O0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010P\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020O0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bQ\u0010\u000eR\u001a\u0010R\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020S0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010T\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020S0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bU\u0010\u000eR\u001a\u0010V\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020S0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010W\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020S0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bX\u0010\u000eR\u001a\u0010Y\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020Z0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010[\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020Z0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b\\\u0010\u000eR\u001a\u0010]\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020^0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010_\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020^0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b`\u0010\u000eR\u001a\u0010a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020b0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bd\u0010\u000eR\u001a\u0010e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020Z0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020Z0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bg\u0010\u000eR\u001d\u0010h\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020F0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bi\u0010\u000eR\u001a\u0010j\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020k0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010l\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020k0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bm\u0010\u000eR\u001a\u0010n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020o0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010p\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020o0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bq\u0010\u000eR\u001a\u0010r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020$0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010s\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020t0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010u\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020t0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\bv\u0010\u000eR\u001a\u0010w\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020o0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010x\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020o0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\by\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010z\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\f8F\u00a2\u0006\u0006\u001a\u0004\b{\u0010\u000e\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0089\u0002"}, d2 = {"Lcom/net/pvr1/repository/UserRepository;", "", "userAPI", "Lcom/net/pvr1/api/UserAPI;", "(Lcom/net/pvr1/api/UserAPI;)V", "_userResponseLiveData", "Landroidx/lifecycle/MutableLiveData;", "Lcom/net/pvr1/utils/NetworkResult;", "Lcom/net/pvr1/ui/login/response/LoginResponse;", "bookingRetrievalLiveData", "Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/response/BookingRetrievalResponse;", "bookingRetrievalResponseLiveData", "Landroidx/lifecycle/LiveData;", "getBookingRetrievalResponseLiveData", "()Landroidx/lifecycle/LiveData;", "bookingSessionLiveData", "Lcom/net/pvr1/ui/bookingSession/response/BookingResponse;", "bookingSessionResponseLiveData", "getBookingSessionResponseLiveData", "bookingTheatreLiveData", "Lcom/net/pvr1/ui/bookingSession/response/BookingTheatreResponse;", "bookingTheatreResponseLiveData", "getBookingTheatreResponseLiveData", "cinemaLiveData", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse;", "cinemaPreferenceLiveData", "Lcom/net/pvr1/ui/home/fragment/cinema/response/PreferenceResponse;", "cinemaPreferenceResponseLiveData", "getCinemaPreferenceResponseLiveData", "cinemaResponseLiveData", "getCinemaResponseLiveData", "cinemaSessionLiveData", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaSessionResponse;", "cinemaSessionResponseLiveData", "getCinemaSessionResponseLiveData", "citiesResponseLiveData", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse;", "getCitiesResponseLiveData", "comingSoonLiveData", "Lcom/net/pvr1/ui/home/fragment/commingSoon/response/CommingSoonResponse;", "comingSoonResponseLiveData", "getComingSoonResponseLiveData", "commingSoonLiveData", "Lcom/net/pvr1/ui/movieDetails/nowShowing/response/MovieDetailsResponse;", "commingSoonResponseLiveData", "getCommingSoonResponseLiveData", "foodAddLiveData", "Lcom/net/pvr1/ui/summery/response/AddFoodResponse;", "foodAddResponseLiveData", "getFoodAddResponseLiveData", "foodLiveData", "Lcom/net/pvr1/ui/food/response/FoodResponse;", "foodResponseLiveData", "getFoodResponseLiveData", "foodTicketLiveData", "Lcom/net/pvr1/ui/myBookings/response/FoodTicketResponse;", "foodTicketResponseLiveData", "getFoodTicketResponseLiveData", "giftCardLiveData", "Lcom/net/pvr1/ui/myBookings/response/GiftCardResponse;", "giftCardMainLiveData", "giftCardMainResponseLiveData", "getGiftCardMainResponseLiveData", "giftCardResponseLiveData", "getGiftCardResponseLiveData", "homeLiveData", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse;", "homeResponseLiveData", "getHomeResponseLiveData", "homeSearchLiveData", "Lcom/net/pvr1/ui/search/searchHome/response/HomeSearchResponse;", "initTransSeatLiveData", "Lcom/net/pvr1/ui/seatLayout/response/InitResponse;", "initTransSeatResponseLiveData", "getInitTransSeatResponseLiveData", "movieDetailsLiveData", "movieDetailsResponseLiveData", "getMovieDetailsResponseLiveData", "nearTheaterLiveData", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaNearTheaterResponse;", "nearTheaterResponseLiveData", "getNearTheaterResponseLiveData", "offerDetailsLiveData", "Lcom/net/pvr1/ui/offer/response/OfferResponse;", "offerDetailsResponseLiveData", "getOfferDetailsResponseLiveData", "offerLiveData", "offerResponseLiveData", "getOfferResponseLiveData", "otpVerifyLiveData", "Lcom/net/pvr1/ui/login/otpVerify/response/ResisterResponse;", "otpVerifyResponseLiveData", "getOtpVerifyResponseLiveData", "privilegeHomeLiveData", "Lcom/net/pvr1/ui/home/fragment/privilege/response/PrivilegeHomeResponse;", "privilegeHomeResponseLiveData", "getPrivilegeHomeResponseLiveData", "reserveSeatLiveData", "Lcom/net/pvr1/ui/seatLayout/response/ReserveSeatResponse;", "reserveSeatResponseLiveData", "getReserveSeatResponseLiveData", "resisterLiveData", "resisterResponseLiveData", "getResisterResponseLiveData", "searchResponseLiveData", "getSearchResponseLiveData", "seatLiveData", "Lcom/net/pvr1/ui/seatLayout/response/SeatResponse;", "seatResponseLiveData", "getSeatResponseLiveData", "seatWithFoodLiveData", "Lcom/net/pvr1/ui/summery/response/SummeryResponse;", "seatWithFoodResponseLiveData", "getSeatWithFoodResponseLiveData", "selectCityLiveData", "splashLiveData", "Lcom/net/pvr1/ui/splash/response/SplashResponse;", "splashResponseLiveData", "getSplashResponseLiveData", "summerLiveData", "summerResponseLiveData", "getSummerResponseLiveData", "userResponseLiveData", "getUserResponseLiveData", "bookingRetrievalLayout", "", "city", "", "lat", "lng", "userid", "searchtxt", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "bookingRetrievalLayoutResponse", "response", "Lretrofit2/Response;", "bookingSessionResponse", "bookingTheatre", "cid", "mid", "isSpi", "bookingTheatreResponse", "bookingTicket", "date", "srilanka", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cinema", "searchTxt", "cinemaPreference", "id", "is_like", "", "type", "did", "(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cinemaPreferenceResponse", "cinemaResponse", "cinemaSessionData", "lang", "format", "price", "time", "hc", "cc", "ad", "qr", "cinetype", "cinetypeQR", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cinemaSessionResponse", "comingSoon", "genre", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "comingSoonResponse", "commingSoonData", "mcode", "commingSoonResponse", "foodAddLayout", "cinemacode", "fb_totalprice", "fb_itemStrDescription", "pickupdate", "cbookid", "audi", "seat", "infosys", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "foodAddLayoutResponse", "foodLayout", "ccode", "bookingid", "transid", "iserv", "foodLayoutResponse", "foodTicket", "userId", "sriLanka", "past", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "foodTicketResponse", "giftCard", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "giftCardMainLayout", "sWidth", "giftCardMainLayoutResponse", "giftCardResponse", "handleResponse", "homeData", "dtmsource", "mobile", "upbooking", "gener", "spShow", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "homeResponse", "homeSearchData", "text", "searchFilter", "homeSearchResponse", "initTransSeatLayout", "sessionid", "initTransSeatResponse", "loginMobile", "cName", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "movieDetailsData", "movieDetailsResponse", "nearTheater", "nearTheatreResponse", "offer", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "offerDetails", "offerDetailsResponse", "offerResponse", "otpVerify", "token", "privilegeHomeData", "privilegeHomeResponse", "reserveSeatLayout", "reserve", "reserveSeatResponse", "resister", "hash", "email", "name", "otp", "cname", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resisterResponse", "seatLayout", "partnerid", "cdate", "bundle", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "seatLayoutResponse", "seatWithFoodLayout", "foods", "seatWithFoodLayoutResponse", "selectCity", "selectCityResponse", "splashLayout", "splashLayoutResponse", "summerLayout", "summeryLayoutResponse", "verifyResponse", "app_debug"})
public final class UserRepository {
    private final com.net.pvr1.api.UserAPI userAPI = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.response.LoginResponse>> _userResponseLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.otpVerify.response.ResisterResponse>> otpVerifyLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.otpVerify.response.ResisterResponse>> resisterLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse>> comingSoonLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse>> cinemaLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.cinema.response.PreferenceResponse>> cinemaPreferenceLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.myBookings.response.GiftCardResponse>> giftCardLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.myBookings.response.FoodTicketResponse>> foodTicketLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.offer.response.OfferResponse>> offerLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.offer.response.OfferResponse>> offerDetailsLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.selectCity.response.SelectCityResponse>> selectCityLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.home.response.HomeResponse>> homeLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse>> privilegeHomeLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse>> homeSearchLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse>> movieDetailsLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse>> commingSoonLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse>> cinemaSessionLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.bookingSession.response.BookingResponse>> bookingSessionLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse>> bookingTheatreLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse>> nearTheaterLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.seatLayout.response.ReserveSeatResponse>> reserveSeatLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.seatLayout.response.InitResponse>> initTransSeatLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.seatLayout.response.SeatResponse>> seatLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.food.response.FoodResponse>> foodLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.summery.response.SummeryResponse>> summerLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.summery.response.SummeryResponse>> seatWithFoodLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.splash.response.SplashResponse>> splashLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.summery.response.AddFoodResponse>> foodAddLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.myBookings.response.GiftCardResponse>> giftCardMainLiveData = null;
    private final androidx.lifecycle.MutableLiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse>> bookingRetrievalLiveData = null;
    
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
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.otpVerify.response.ResisterResponse>> getOtpVerifyResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object otpVerify(@org.jetbrains.annotations.NotNull()
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void verifyResponse(retrofit2.Response<com.net.pvr1.ui.login.otpVerify.response.ResisterResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.otpVerify.response.ResisterResponse>> getResisterResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object resister(@org.jetbrains.annotations.NotNull()
    java.lang.String hash, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    java.lang.String otp, @org.jetbrains.annotations.NotNull()
    java.lang.String city, boolean cname, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void resisterResponse(retrofit2.Response<com.net.pvr1.ui.login.otpVerify.response.ResisterResponse> response) {
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
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.cinema.response.PreferenceResponse>> getCinemaPreferenceResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object cinemaPreference(@org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String id, boolean is_like, @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String did, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void cinemaPreferenceResponse(retrofit2.Response<com.net.pvr1.ui.home.fragment.cinema.response.PreferenceResponse> response) {
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
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.offer.response.OfferResponse>> getOfferDetailsResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object offerDetails(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String did, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void offerDetailsResponse(retrofit2.Response<com.net.pvr1.ui.offer.response.OfferResponse> response) {
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
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.home.response.HomeResponse>> getHomeResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object homeData(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String dtmsource, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String mobile, boolean upbooking, @org.jetbrains.annotations.NotNull()
    java.lang.String srilanka, @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    java.lang.String gener, @org.jetbrains.annotations.NotNull()
    java.lang.String spShow, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void homeResponse(retrofit2.Response<com.net.pvr1.ui.home.fragment.home.response.HomeResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse>> getPrivilegeHomeResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object privilegeHomeData(@org.jetbrains.annotations.NotNull()
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void privilegeHomeResponse(retrofit2.Response<com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse>> getSearchResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object homeSearchData(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    java.lang.String searchFilter, @org.jetbrains.annotations.NotNull()
    java.lang.String lat, @org.jetbrains.annotations.NotNull()
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void homeSearchResponse(retrofit2.Response<com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse>> getMovieDetailsResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object movieDetailsData(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String mid, @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String lat, @org.jetbrains.annotations.NotNull()
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    java.lang.String srilanka, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void movieDetailsResponse(retrofit2.Response<com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse>> getCommingSoonResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object commingSoonData(@org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String mcode, @org.jetbrains.annotations.NotNull()
    java.lang.String did, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void commingSoonResponse(retrofit2.Response<com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse>> getCinemaSessionResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object cinemaSessionData(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String cid, @org.jetbrains.annotations.NotNull()
    java.lang.String lat, @org.jetbrains.annotations.NotNull()
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.lang.String lang, @org.jetbrains.annotations.NotNull()
    java.lang.String format, @org.jetbrains.annotations.NotNull()
    java.lang.String price, @org.jetbrains.annotations.NotNull()
    java.lang.String time, @org.jetbrains.annotations.NotNull()
    java.lang.String hc, @org.jetbrains.annotations.NotNull()
    java.lang.String cc, @org.jetbrains.annotations.NotNull()
    java.lang.String ad, @org.jetbrains.annotations.NotNull()
    java.lang.String qr, @org.jetbrains.annotations.NotNull()
    java.lang.String cinetype, @org.jetbrains.annotations.NotNull()
    java.lang.String cinetypeQR, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void cinemaSessionResponse(retrofit2.Response<com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.bookingSession.response.BookingResponse>> getBookingSessionResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object bookingTicket(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String mid, @org.jetbrains.annotations.NotNull()
    java.lang.String lat, @org.jetbrains.annotations.NotNull()
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    java.lang.String srilanka, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void bookingSessionResponse(retrofit2.Response<com.net.pvr1.ui.bookingSession.response.BookingResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse>> getBookingTheatreResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object bookingTheatre(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String cid, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String mid, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void bookingTheatreResponse(retrofit2.Response<com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse>> getNearTheaterResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object nearTheater(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String lat, @org.jetbrains.annotations.Nullable()
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    java.lang.String cid, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void nearTheatreResponse(retrofit2.Response<com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.seatLayout.response.ReserveSeatResponse>> getReserveSeatResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object reserveSeatLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String reserve, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void reserveSeatResponse(retrofit2.Response<com.net.pvr1.ui.seatLayout.response.ReserveSeatResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.seatLayout.response.InitResponse>> getInitTransSeatResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object initTransSeatLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String cinemacode, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionid, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void initTransSeatResponse(retrofit2.Response<com.net.pvr1.ui.seatLayout.response.InitResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.seatLayout.response.SeatResponse>> getSeatResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object seatLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String cinemacode, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionid, @org.jetbrains.annotations.NotNull()
    java.lang.String dtmsource, @org.jetbrains.annotations.NotNull()
    java.lang.String partnerid, @org.jetbrains.annotations.NotNull()
    java.lang.String cdate, boolean bundle, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void seatLayoutResponse(retrofit2.Response<com.net.pvr1.ui.seatLayout.response.SeatResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.food.response.FoodResponse>> getFoodResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object foodLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String ccode, @org.jetbrains.annotations.NotNull()
    java.lang.String bookingid, @org.jetbrains.annotations.NotNull()
    java.lang.String cbookid, @org.jetbrains.annotations.NotNull()
    java.lang.String transid, @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String audi, @org.jetbrains.annotations.NotNull()
    java.lang.String seat, @org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String qr, @org.jetbrains.annotations.NotNull()
    java.lang.String iserv, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void foodLayoutResponse(retrofit2.Response<com.net.pvr1.ui.food.response.FoodResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.summery.response.SummeryResponse>> getSummerResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object summerLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String transid, @org.jetbrains.annotations.NotNull()
    java.lang.String cinemacode, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String bookingid, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void summeryLayoutResponse(retrofit2.Response<com.net.pvr1.ui.summery.response.SummeryResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.summery.response.SummeryResponse>> getSeatWithFoodResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object seatWithFoodLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String foods, @org.jetbrains.annotations.NotNull()
    java.lang.String transid, @org.jetbrains.annotations.NotNull()
    java.lang.String cinemacode, @org.jetbrains.annotations.NotNull()
    java.lang.String qr, @org.jetbrains.annotations.NotNull()
    java.lang.String infosys, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void seatWithFoodLayoutResponse(retrofit2.Response<com.net.pvr1.ui.summery.response.SummeryResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.splash.response.SplashResponse>> getSplashResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object splashLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void splashLayoutResponse(retrofit2.Response<com.net.pvr1.ui.splash.response.SplashResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.summery.response.AddFoodResponse>> getFoodAddResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object foodAddLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String cinemacode, @org.jetbrains.annotations.NotNull()
    java.lang.String fb_totalprice, @org.jetbrains.annotations.NotNull()
    java.lang.String fb_itemStrDescription, @org.jetbrains.annotations.NotNull()
    java.lang.String pickupdate, @org.jetbrains.annotations.NotNull()
    java.lang.String cbookid, @org.jetbrains.annotations.NotNull()
    java.lang.String audi, @org.jetbrains.annotations.NotNull()
    java.lang.String seat, @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String infosys, @org.jetbrains.annotations.NotNull()
    java.lang.String qr, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    java.lang.String srilanka, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void foodAddLayoutResponse(retrofit2.Response<com.net.pvr1.ui.summery.response.AddFoodResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.myBookings.response.GiftCardResponse>> getGiftCardMainResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object giftCardMainLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String sWidth, @org.jetbrains.annotations.NotNull()
    java.lang.String infosys, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void giftCardMainLayoutResponse(retrofit2.Response<com.net.pvr1.ui.myBookings.response.GiftCardResponse> response) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse>> getBookingRetrievalResponseLiveData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object bookingRetrievalLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String lat, @org.jetbrains.annotations.NotNull()
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String searchtxt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final void bookingRetrievalLayoutResponse(retrofit2.Response<com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse> response) {
    }
}