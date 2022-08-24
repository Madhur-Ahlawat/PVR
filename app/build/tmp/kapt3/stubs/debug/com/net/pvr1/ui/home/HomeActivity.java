package com.net.pvr1.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityHomeBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.home.fragment.*;
import com.net.pvr1.ui.home.fragment.cinema.CinemasFragment;
import com.net.pvr1.ui.home.fragment.commingSoon.ComingSoonFragment;
import com.net.pvr1.ui.home.fragment.home.HomeFragment;
import com.net.pvr1.ui.login.viewModel.LoginViewModel;
import com.net.pvr1.ui.search.searchHome.SearchHomeActivity;
import com.net.pvr1.ui.selectCity.SelectCityActivity;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0002J\u0012\u0010\u000f\u001a\u00020\u000e2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0014J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\u000eH\u0002R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/net/pvr1/ui/home/HomeActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "authViewModel", "Lcom/net/pvr1/ui/login/viewModel/LoginViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/login/viewModel/LoginViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityHomeBinding;", "preferences", "Lcom/net/pvr1/di/preference/AppPreferences;", "movedNext", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "setCurrentFragment", "Landroidx/fragment/app/FragmentTransaction;", "fragment", "Landroidx/fragment/app/Fragment;", "switchFragment", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class HomeActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.net.pvr1.di.preference.AppPreferences preferences;
    private com.net.pvr1.databinding.ActivityHomeBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    
    public HomeActivity() {
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
    
    private final void switchFragment() {
    }
    
    private final androidx.fragment.app.FragmentTransaction setCurrentFragment(androidx.fragment.app.Fragment fragment) {
        return null;
    }
}