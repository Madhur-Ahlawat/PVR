package com.net.pvr1.ui.cinemaSession;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityCinemaSessionBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionCinParentAdapter;
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionDaysAdapter;
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionLanguageAdapter;
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionNearTheaterAdapter;
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse;
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse;
import com.net.pvr1.ui.cinemaSession.viewModel.CinemaSessionViewModel;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.fragment.home.adapter.PromotionAdapter;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u0005B\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0012H\u0002J\b\u0010\u0016\u001a\u00020\u0012H\u0002J\u0010\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0018H\u0016J\u0010\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u001aH\u0016J\u0010\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u001cH\u0016J\u0012\u0010\u001e\u001a\u00020\u00122\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0014J\u0010\u0010!\u001a\u00020\u00122\u0006\u0010\"\u001a\u00020#H\u0002J\u0010\u0010$\u001a\u00020\u00122\u0006\u0010\"\u001a\u00020%H\u0002R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\nR\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/net/pvr1/ui/cinemaSession/CinemaSessionActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionDaysAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionLanguageAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionCinParentAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionNearTheaterAdapter$RecycleViewItemClickListenerCity;", "()V", "authViewModel", "Lcom/net/pvr1/ui/cinemaSession/viewModel/CinemaSessionViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/cinemaSession/viewModel/CinemaSessionViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityCinemaSessionBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "cinemaClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaSessionResponse$Child;", "cinemaNearTheaterLoad", "cinemaSessionDataLoad", "dateClick", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaSessionResponse$Output$Bd;", "languageClick", "", "nearTheaterClick", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaNearTheaterResponse$Output$C;", "nearTheaterDirectionClick", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "retrieveData", "output", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaSessionResponse$Output;", "retrieveTheaterData", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaNearTheaterResponse$Output;", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class CinemaSessionActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionDaysAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionLanguageAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionCinParentAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionNearTheaterAdapter.RecycleViewItemClickListenerCity {
    private com.net.pvr1.databinding.ActivityCinemaSessionBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    
    public CinemaSessionActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.cinemaSession.viewModel.CinemaSessionViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void cinemaSessionDataLoad() {
    }
    
    private final void cinemaNearTheaterLoad() {
    }
    
    private final void retrieveTheaterData(com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse.Output output) {
    }
    
    private final void retrieveData(com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse.Output output) {
    }
    
    @java.lang.Override()
    public void dateClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse.Output.Bd comingSoonItem) {
    }
    
    @java.lang.Override()
    public void languageClick(@org.jetbrains.annotations.NotNull()
    java.lang.String comingSoonItem) {
    }
    
    @java.lang.Override()
    public void cinemaClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse.Child comingSoonItem) {
    }
    
    @java.lang.Override()
    public void nearTheaterClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse.Output.C comingSoonItem) {
    }
    
    @java.lang.Override()
    public void nearTheaterDirectionClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse.Output.C comingSoonItem) {
    }
}