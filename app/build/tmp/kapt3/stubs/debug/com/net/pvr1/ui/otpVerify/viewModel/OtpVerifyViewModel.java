package com.net.pvr1.ui.otpVerify.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.net.pvr1.repository.UserRepository;
import com.net.pvr1.ui.login.response.LoginResponse;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@dagger.hilt.android.lifecycle.HiltViewModel()
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0010"}, d2 = {"Lcom/net/pvr1/ui/otpVerify/viewModel/OtpVerifyViewModel;", "Landroidx/lifecycle/ViewModel;", "userRepository", "Lcom/net/pvr1/repository/UserRepository;", "(Lcom/net/pvr1/repository/UserRepository;)V", "userResponseLiveData", "Landroidx/lifecycle/LiveData;", "Lcom/net/pvr1/utils/NetworkResult;", "Lcom/net/pvr1/ui/login/response/LoginResponse;", "getUserResponseLiveData", "()Landroidx/lifecycle/LiveData;", "otpVerify", "", "mobile", "", "token", "app_debug"})
public final class OtpVerifyViewModel extends androidx.lifecycle.ViewModel {
    private final com.net.pvr1.repository.UserRepository userRepository = null;
    
    @javax.inject.Inject()
    public OtpVerifyViewModel(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.repository.UserRepository userRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.net.pvr1.utils.NetworkResult<com.net.pvr1.ui.login.response.LoginResponse>> getUserResponseLiveData() {
        return null;
    }
    
    public final void otpVerify(@org.jetbrains.annotations.NotNull()
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    java.lang.String token) {
    }
}