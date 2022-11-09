package com.net.pvr1.ui.seatLayout.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.net.pvr1.repository.UserRepository;
import com.net.pvr1.ui.seatLayout.response.InitResponse;
import com.net.pvr1.ui.seatLayout.response.ReserveSeatResponse;
import com.net.pvr1.ui.seatLayout.response.SeatResponse;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@dagger.hilt.android.lifecycle.HiltViewModel()
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014J\u000e\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0014J>\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u0014R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\n\u00a8\u0006\u001f"}, d2 = {"Lcom/net/pvr1/ui/seatLayout/viewModel/SeatLayoutViewModel;", "Landroidx/lifecycle/ViewModel;", "userRepository", "Lcom/net/pvr1/repository/UserRepository;", "(Lcom/net/pvr1/repository/UserRepository;)V", "initTransResponseLiveData", "Landroidx/lifecycle/LiveData;", "Lcom/net/pvr1/utils/NetworkResult;", "Lcom/net/pvr1/ui/seatLayout/response/InitResponse;", "getInitTransResponseLiveData", "()Landroidx/lifecycle/LiveData;", "reserveSeatResponseLiveData", "Lcom/net/pvr1/ui/seatLayout/response/ReserveSeatResponse;", "getReserveSeatResponseLiveData", "userResponseLiveData", "Lcom/net/pvr1/ui/seatLayout/response/SeatResponse;", "getUserResponseLiveData", "initTransSeat", "", "cinemacode", "", "sessionid", "reserveSeat", "reserve", "seatLayout", "dtmsource", "partnerid", "cdate", "bundle", "", "isSpi", "app_debug"})
public final class SeatLayoutViewModel extends androidx.lifecycle.ViewModel {
    private final com.net.pvr1.repository.UserRepository userRepository = null;
    
    @javax.inject.Inject()
    public SeatLayoutViewModel(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.repository.UserRepository userRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.seatLayout.response.SeatResponse>> getUserResponseLiveData() {
        return null;
    }
    
    public final void seatLayout(@org.jetbrains.annotations.NotNull()
    java.lang.String cinemacode, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionid, @org.jetbrains.annotations.NotNull()
    java.lang.String dtmsource, @org.jetbrains.annotations.NotNull()
    java.lang.String partnerid, @org.jetbrains.annotations.NotNull()
    java.lang.String cdate, boolean bundle, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.seatLayout.response.ReserveSeatResponse>> getReserveSeatResponseLiveData() {
        return null;
    }
    
    public final void reserveSeat(@org.jetbrains.annotations.NotNull()
    java.lang.String reserve) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.seatLayout.response.InitResponse>> getInitTransResponseLiveData() {
        return null;
    }
    
    public final void initTransSeat(@org.jetbrains.annotations.NotNull()
    java.lang.String cinemacode, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionid) {
    }
}