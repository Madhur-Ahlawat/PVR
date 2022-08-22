package com.net.pvr1.ui.otpVerify;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityOtpVerifyBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.HomeActivity;
import com.net.pvr1.ui.login.response.LoginResponse;
import com.net.pvr1.ui.otpVerify.viewModel.OtpVerifyViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import com.net.pvr1.utils.SmsBroadcastReceiver;
import com.net.pvr1.utils.SmsBroadcastReceiver.SmsBroadcastReceiverListener;
import dagger.hilt.android.AndroidEntryPoint;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u0010J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0010H\u0002J\b\u0010\u001a\u001a\u00020\u0018H\u0002J\"\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u001d\u001a\u00020\u00042\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0015J\u0012\u0010 \u001a\u00020\u00182\b\u0010!\u001a\u0004\u0018\u00010\"H\u0014J\b\u0010#\u001a\u00020\u0018H\u0014J\b\u0010$\u001a\u00020\u0018H\u0014J\b\u0010%\u001a\u00020\u0018H\u0002J\b\u0010&\u001a\u00020\u0018H\u0002J\u0012\u0010\'\u001a\u00020\u00182\b\u0010(\u001a\u0004\u0018\u00010)H\u0002J\b\u0010*\u001a\u00020\u0018H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/net/pvr1/ui/otpVerify/OtpVerifyActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "User_Content", "", "authViewModel", "Lcom/net/pvr1/ui/otpVerify/viewModel/OtpVerifyViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/otpVerify/viewModel/OtpVerifyViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityOtpVerifyBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "mobile", "", "preferences", "Lcom/net/pvr1/di/preference/AppPreferences;", "smsBroadcastReceiver", "Lcom/net/pvr1/utils/SmsBroadcastReceiver;", "getHash", "text", "getOtpFromMessage", "", "message", "movedNext", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onStart", "onStop", "otpVerify", "registerBroadcastReceiver", "retrieveData", "output", "Lcom/net/pvr1/ui/login/response/LoginResponse$Output;", "startSmsUserConsent", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class OtpVerifyActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.net.pvr1.di.preference.AppPreferences preferences;
    private com.net.pvr1.databinding.ActivityOtpVerifyBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private java.lang.String mobile = "";
    private final int User_Content = 200;
    private com.net.pvr1.utils.SmsBroadcastReceiver smsBroadcastReceiver;
    
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
    
    private final void startSmsUserConsent() {
    }
    
    private final void movedNext() {
    }
    
    private final void otpVerify() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.login.response.LoginResponse.Output output) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @kotlin.jvm.Throws(exceptionClasses = {java.lang.Exception.class})
    public final java.lang.String getHash(@org.jetbrains.annotations.NotNull()
    java.lang.String text) throws java.lang.Exception {
        return null;
    }
    
    @java.lang.Override()
    @java.lang.Deprecated()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void getOtpFromMessage(java.lang.String message) {
    }
    
    private final void registerBroadcastReceiver() {
    }
    
    @java.lang.Override()
    protected void onStart() {
    }
    
    @java.lang.Override()
    protected void onStop() {
    }
}