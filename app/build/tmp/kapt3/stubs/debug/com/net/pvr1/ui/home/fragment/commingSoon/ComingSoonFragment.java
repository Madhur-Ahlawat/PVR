package com.net.pvr1.ui.home.fragment.commingSoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.FragmentComingSoonBinding;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.fragment.commingSoon.adapter.ComingSoonMovieAdapter;
import com.net.pvr1.ui.home.fragment.commingSoon.adapter.LanguageAdapter;
import com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse;
import com.net.pvr1.ui.home.fragment.commingSoon.viewModel.ComingSoonViewModel;
import com.net.pvr1.ui.movieDetails.commingSoon.ComingSoonActivity;
import com.net.pvr1.ui.search.searchComingSoon.SearchComingSoonActivity;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0002J&\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\u0010\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u001fH\u0016J\u001a\u0010 \u001a\u00020\u00122\u0006\u0010!\u001a\u00020\u00152\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\u0010\u0010\"\u001a\u00020\u00122\u0006\u0010#\u001a\u00020$H\u0002R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/commingSoon/ComingSoonFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/net/pvr1/ui/home/fragment/commingSoon/adapter/LanguageAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/home/fragment/commingSoon/adapter/ComingSoonMovieAdapter$VideoPlay;", "()V", "authViewModel", "Lcom/net/pvr1/ui/home/fragment/commingSoon/viewModel/ComingSoonViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/home/fragment/commingSoon/viewModel/ComingSoonViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/FragmentComingSoonBinding;", "checkLogin", "", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "comingSoonApi", "", "movedNext", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDateClick", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/commingSoon/response/CommingSoonResponse$Output$Movy;", "", "onViewCreated", "view", "retrieveData", "output", "Lcom/net/pvr1/ui/home/fragment/commingSoon/response/CommingSoonResponse$Output;", "app_debug"})
public final class ComingSoonFragment extends androidx.fragment.app.Fragment implements com.net.pvr1.ui.home.fragment.commingSoon.adapter.LanguageAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.home.fragment.commingSoon.adapter.ComingSoonMovieAdapter.VideoPlay {
    private com.net.pvr1.databinding.FragmentComingSoonBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    private boolean checkLogin = false;
    
    public ComingSoonFragment() {
        super();
    }
    
    private final com.net.pvr1.ui.home.fragment.commingSoon.viewModel.ComingSoonViewModel getAuthViewModel() {
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
    
    private final void movedNext() {
    }
    
    private final void comingSoonApi() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse.Output output) {
    }
    
    @java.lang.Override()
    public void onDateClick(@org.jetbrains.annotations.NotNull()
    java.lang.Object comingSoonItem) {
    }
    
    @java.lang.Override()
    public void onDateClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.commingSoon.response.CommingSoonResponse.Output.Movy comingSoonItem) {
    }
}