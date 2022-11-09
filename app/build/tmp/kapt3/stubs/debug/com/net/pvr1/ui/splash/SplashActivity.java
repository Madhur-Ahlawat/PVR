package com.net.pvr1.ui.splash;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.net.pvr1.MainActivity;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivitySplashBinding;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.HomeActivity;
import com.net.pvr1.ui.login.LoginActivity;
import com.net.pvr1.ui.onBoarding.LandingActivity;
import com.net.pvr1.ui.splash.response.SplashResponse;
import com.net.pvr1.ui.splash.viewModel.SplashViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import com.net.pvr1.utils.PreferenceManager;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@android.annotation.SuppressLint(value = {"CustomSplashScreen"})
@kotlin.Suppress(names = {"DEPRECATION"})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010#\u001a\u00020\u0012H\u0002J\b\u0010$\u001a\u00020%H\u0002J\b\u0010\u0015\u001a\u00020%H\u0002J\u0012\u0010&\u001a\u00020%2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0014J\u0010\u0010)\u001a\u00020%2\u0006\u0010*\u001a\u00020+H\u0002J\b\u0010,\u001a\u00020%H\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0017\u001a\u00020\u00188\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"\u00a8\u0006-"}, d2 = {"Lcom/net/pvr1/ui/splash/SplashActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "MyPREFERENCES", "", "getMyPREFERENCES", "()Ljava/lang/String;", "OnBoardingClick", "getOnBoardingClick", "authViewModel", "Lcom/net/pvr1/ui/splash/viewModel/SplashViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/splash/viewModel/SplashViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivitySplashBinding;", "clickOnBoarding", "", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "networkDialog", "Landroid/app/Dialog;", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "sharedpreferences", "Landroid/content/SharedPreferences;", "getSharedpreferences", "()Landroid/content/SharedPreferences;", "setSharedpreferences", "(Landroid/content/SharedPreferences;)V", "isConnected", "movedNext", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "retrieveData", "output", "Lcom/net/pvr1/ui/splash/response/SplashResponse$Output;", "summeryDetails", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class SplashActivity extends androidx.appcompat.app.AppCompatActivity {
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    private com.net.pvr1.databinding.ActivitySplashBinding binding;
    private android.app.Dialog networkDialog;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String MyPREFERENCES = "MyPrefs";
    @org.jetbrains.annotations.Nullable()
    private android.content.SharedPreferences sharedpreferences;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String OnBoardingClick = "Name";
    private boolean clickOnBoarding = false;
    
    public SplashActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.utils.PreferenceManager getPreferences() {
        return null;
    }
    
    public final void setPreferences(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.utils.PreferenceManager p0) {
    }
    
    private final com.net.pvr1.ui.splash.viewModel.SplashViewModel getAuthViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMyPREFERENCES() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.content.SharedPreferences getSharedpreferences() {
        return null;
    }
    
    public final void setSharedpreferences(@org.jetbrains.annotations.Nullable()
    android.content.SharedPreferences p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOnBoardingClick() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movedNext() {
    }
    
    private final boolean isConnected() {
        return false;
    }
    
    private final void networkDialog() {
    }
    
    private final void summeryDetails() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.splash.response.SplashResponse.Output output) {
    }
}