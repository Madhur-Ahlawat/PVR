package com.net.pvr1.ui.login.otpVerify.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.net.pvr1.repository.UserRepository;
import com.net.pvr1.ui.login.response.LoginResponse;
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@dagger.hilt.android.lifecycle.HiltViewModel()
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010J>\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\n\u00a8\u0006\u001a"}, d2 = {"Lcom/net/pvr1/ui/login/otpVerify/viewModel/OtpVerifyViewModel;", "Landroidx/lifecycle/ViewModel;", "userRepository", "Lcom/net/pvr1/repository/UserRepository;", "(Lcom/net/pvr1/repository/UserRepository;)V", "userResponseLiveData", "Landroidx/lifecycle/LiveData;", "Lcom/net/pvr1/utils/NetworkResult;", "Lcom/net/pvr1/ui/login/otpVerify/response/ResisterResponse;", "getUserResponseLiveData", "()Landroidx/lifecycle/LiveData;", "userResponseResLiveData", "getUserResponseResLiveData", "otpVerify", "", "mobile", "", "token", "resister", "hash", "name", "email", "otp", "city", "cname", "", "app_debug"})
public final class OtpVerifyViewModel extends androidx.lifecycle.ViewModel {
    private final com.net.pvr1.repository.UserRepository userRepository = null;
    
    @javax.inject.Inject()
    public OtpVerifyViewModel(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.repository.UserRepository userRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.otpVerify.response.ResisterResponse>> getUserResponseLiveData() {
        return null;
    }
    
    public final void otpVerify(@org.jetbrains.annotations.NotNull()
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    java.lang.String token) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.otpVerify.response.ResisterResponse>> getUserResponseResLiveData() {
        return null;
    }
    
    public final void resister(@org.jetbrains.annotations.NotNull()
    java.lang.String hash, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    java.lang.String otp, @org.jetbrains.annotations.NotNull()
    java.lang.String city, boolean cname) {
    }
}