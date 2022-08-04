package com.net.pvr1.ui.otpVerify;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.net.pvr1.databinding.ActivityOtpVerifyBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.login.response.LoginResponse;
import com.net.pvr1.ui.onBoarding.LandingActivity;
import com.net.pvr1.ui.otpVerify.viewModel.OtpVerifyViewModel;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.AndroidEntryPoint;
import java.security.MessageDigest;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0010\u001a\u00020\fJ\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0002J\u0012\u0010\u0014\u001a\u00020\u00122\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\u0012\u0010\u0017\u001a\u00020\u00122\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/net/pvr1/ui/otpVerify/OtpVerifyActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "authViewModel", "Lcom/net/pvr1/ui/otpVerify/viewModel/OtpVerifyViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/otpVerify/viewModel/OtpVerifyViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityOtpVerifyBinding;", "mobile", "", "preferences", "Lcom/net/pvr1/di/preference/AppPreferences;", "getHash", "text", "loginApi", "", "movedNext", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "retrieveData", "output", "Lcom/net/pvr1/ui/login/response/LoginResponse$Output;", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class OtpVerifyActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.net.pvr1.di.preference.AppPreferences preferences;
    private com.net.pvr1.databinding.ActivityOtpVerifyBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private java.lang.String mobile = "";
    
    public OtpVerifyActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.otpVerify.viewModel.OtpVerifyViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movedNext() {
    }
    
    private final void loginApi() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.login.response.LoginResponse.Output output) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @kotlin.jvm.Throws(exceptionClasses = {java.lang.Exception.class})
    public final java.lang.String getHash(@org.jetbrains.annotations.NotNull()
    java.lang.String text) throws java.lang.Exception {
        return null;
    }
}