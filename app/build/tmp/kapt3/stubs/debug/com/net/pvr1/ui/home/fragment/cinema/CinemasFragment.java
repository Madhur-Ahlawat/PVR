package com.net.pvr1.ui.home.fragment.cinema;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.FragmentCinemasBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter;
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse;
import com.net.pvr1.ui.home.fragment.cinema.viewModel.CinemaViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0011\u001a\u00020\u0012H\u0002J&\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u0010\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0010\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u001a\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u00142\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u0010\u0010!\u001a\u00020\u00122\u0006\u0010\"\u001a\u00020#H\u0002R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/cinema/CinemasFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Direction;", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Location;", "()V", "authViewModel", "Lcom/net/pvr1/ui/home/fragment/cinema/viewModel/CinemaViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/home/fragment/cinema/viewModel/CinemaViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/FragmentCinemasBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "preferences", "Lcom/net/pvr1/di/preference/AppPreferences;", "cinemaApi", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDirectionClick", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output$C;", "onLocationClick", "onViewCreated", "view", "retrieveData", "output", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output;", "app_debug"})
public final class CinemasFragment extends androidx.fragment.app.Fragment implements com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Direction, com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Location {
    private com.net.pvr1.databinding.FragmentCinemasBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.di.preference.AppPreferences preferences;
    
    public CinemasFragment() {
        super();
    }
    
    private final com.net.pvr1.ui.home.fragment.cinema.viewModel.CinemaViewModel getAuthViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void cinemaApi() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output output) {
    }
    
    @java.lang.Override()
    public void onDirectionClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C comingSoonItem) {
    }
    
    @java.lang.Override()
    public void onLocationClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C comingSoonItem) {
    }
}