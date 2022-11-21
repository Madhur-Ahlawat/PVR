package com.net.pvr1.ui.summery.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.net.pvr1.repository.UserRepository;
import com.net.pvr1.ui.summery.response.AddFoodResponse;
import com.net.pvr1.ui.summery.response.SummeryResponse;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@dagger.hilt.android.lifecycle.HiltViewModel()
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0012\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004Jn\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u0013JF\u0010 \u001a\u00020\u00112\u0006\u0010!\u001a\u00020\u00132\u0006\u0010\"\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0013J&\u0010#\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010$\u001a\u00020\u0013R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\nR\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/net/pvr1/ui/summery/viewModel/SummeryViewModel;", "Landroidx/lifecycle/ViewModel;", "userRepository", "Lcom/net/pvr1/repository/UserRepository;", "(Lcom/net/pvr1/repository/UserRepository;)V", "foodLiveDataScope", "Landroidx/lifecycle/LiveData;", "Lcom/net/pvr1/utils/NetworkResult;", "Lcom/net/pvr1/ui/summery/response/AddFoodResponse;", "getFoodLiveDataScope", "()Landroidx/lifecycle/LiveData;", "liveDataScope", "Lcom/net/pvr1/ui/summery/response/SummeryResponse;", "getLiveDataScope", "seatWithFoodDataScope", "getSeatWithFoodDataScope", "food", "", "userid", "", "cinemacode", "fb_totalprice", "fb_itemStrDescription", "pickupdate", "cbookid", "audi", "seat", "type", "infosys", "qr", "isSpi", "srilanka", "seatWithFood", "foods", "transid", "summery", "bookingid", "app_debug"})
public final class SummeryViewModel extends androidx.lifecycle.ViewModel {
    private final com.net.pvr1.repository.UserRepository userRepository = null;
    
    @javax.inject.Inject()
    public SummeryViewModel(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.repository.UserRepository userRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.summery.response.SummeryResponse>> getLiveDataScope() {
        return null;
    }
    
    public final void summery(@org.jetbrains.annotations.NotNull()
    java.lang.String transid, @org.jetbrains.annotations.NotNull()
    java.lang.String cinemacode, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String bookingid) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.summery.response.AddFoodResponse>> getFoodLiveDataScope() {
        return null;
    }
    
    public final void food(@org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
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
    java.lang.String srilanka) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.summery.response.SummeryResponse>> getSeatWithFoodDataScope() {
        return null;
    }
    
    public final void seatWithFood(@org.jetbrains.annotations.NotNull()
    java.lang.String foods, @org.jetbrains.annotations.NotNull()
    java.lang.String transid, @org.jetbrains.annotations.NotNull()
    java.lang.String cinemacode, @org.jetbrains.annotations.NotNull()
    java.lang.String qr, @org.jetbrains.annotations.NotNull()
    java.lang.String infosys, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi, @org.jetbrains.annotations.NotNull()
    java.lang.String seat, @org.jetbrains.annotations.NotNull()
    java.lang.String audi) {
    }
}