package com.net.pvr1.ui.login;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityLoginBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.login.response.LoginResponse;
import com.net.pvr1.ui.login.viewModel.LoginViewModel;
import com.net.pvr1.ui.otpVerify.OtpVerifyActivity;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0002J\"\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00042\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\u0012\u0010\u0019\u001a\u00020\u00122\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\u0012\u0010\u001c\u001a\u00020\u00122\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/net/pvr1/ui/login/LoginActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "CREDENTIAL_PICKER_REQUEST", "", "authViewModel", "Lcom/net/pvr1/ui/login/viewModel/LoginViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/login/viewModel/LoginViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityLoginBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "preferences", "Lcom/net/pvr1/di/preference/AppPreferences;", "loginApi", "", "movedNext", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "retrieveData", "output", "Lcom/net/pvr1/ui/login/response/LoginResponse$Output;", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class LoginActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.net.pvr1.di.preference.AppPreferences preferences;
    private com.net.pvr1.databinding.ActivityLoginBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    private final int CREDENTIAL_PICKER_REQUEST = 1;
    
    public LoginActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.login.viewModel.LoginViewModel getAuthViewModel() {
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
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
}