package com.net.pvr1.ui.home.fragment.cinema;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.FragmentCinemasBinding;
import com.net.pvr1.ui.cinemaSession.CinemaSessionActivity;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter;
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse;
import com.net.pvr1.ui.home.fragment.cinema.viewModel.CinemaViewModel;
import com.net.pvr1.ui.search.searchCinema.SearchCinemaActivity;
import com.net.pvr1.utils.*;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004B\u0005\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\u0017H\u0002J&\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\u0010\u0010!\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020#H\u0016J\u0010\u0010$\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020#H\u0016J\u0018\u0010%\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020#2\u0006\u0010&\u001a\u00020\'H\u0016J\u001a\u0010(\u001a\u00020\u00172\u0006\u0010)\u001a\u00020\u001a2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\u0010\u0010*\u001a\u00020\u00172\u0006\u0010+\u001a\u00020,H\u0002J\b\u0010-\u001a\u00020\u0017H\u0002R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0010\u001a\u00020\u00118\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015\u00a8\u0006."}, d2 = {"Lcom/net/pvr1/ui/home/fragment/cinema/CinemasFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Direction;", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Location;", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$SetPreference;", "()V", "authViewModel", "Lcom/net/pvr1/ui/home/fragment/cinema/viewModel/CinemaViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/home/fragment/cinema/viewModel/CinemaViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/FragmentCinemasBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "cinemaApi", "", "movedNext", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDirectionClick", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output$C;", "onLocationClick", "onPreferenceClick", "rowIndex", "", "onViewCreated", "view", "retrieveData", "output", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output;", "setPreference", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class CinemasFragment extends androidx.fragment.app.Fragment implements com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Direction, com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Location, com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.SetPreference {
    private com.net.pvr1.databinding.FragmentCinemasBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    
    public CinemasFragment() {
        super();
    }
    
    private final com.net.pvr1.ui.home.fragment.cinema.viewModel.CinemaViewModel getAuthViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.utils.PreferenceManager getPreferences() {
        return null;
    }
    
    public final void setPreferences(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.utils.PreferenceManager p0) {
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
    
    private final void movedNext() {
    }
    
    private final void cinemaApi() {
    }
    
    private final void setPreference() {
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
    
    @java.lang.Override()
    public void onPreferenceClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C comingSoonItem, boolean rowIndex) {
    }
}