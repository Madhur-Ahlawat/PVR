package com.net.pvr1.ui.home.fragment.home.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.net.pvr1.repository.UserRepository;
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@dagger.hilt.android.lifecycle.HiltViewModel()
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J^\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u001a"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/home/viewModel/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "userRepository", "Lcom/net/pvr1/repository/UserRepository;", "(Lcom/net/pvr1/repository/UserRepository;)V", "userResponseLiveData", "Landroidx/lifecycle/LiveData;", "Lcom/net/pvr1/utils/NetworkResult;", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse;", "getUserResponseLiveData", "()Landroidx/lifecycle/LiveData;", "home", "", "city", "", "dtmsource", "userid", "mobile", "upbooking", "", "srilanka", "type", "lng", "gener", "spShow", "isSpi", "app_debug"})
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    private final com.net.pvr1.repository.UserRepository userRepository = null;
    
    @javax.inject.Inject()
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.repository.UserRepository userRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.home.fragment.home.response.HomeResponse>> getUserResponseLiveData() {
        return null;
    }
    
    public final void home(@org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String dtmsource, @org.jetbrains.annotations.NotNull()
    java.lang.String userid, @org.jetbrains.annotations.NotNull()
    java.lang.String mobile, boolean upbooking, @org.jetbrains.annotations.NotNull()
    java.lang.String srilanka, @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String lng, @org.jetbrains.annotations.NotNull()
    java.lang.String gener, @org.jetbrains.annotations.NotNull()
    java.lang.String spShow, @org.jetbrains.annotations.NotNull()
    java.lang.String isSpi) {
    }
}