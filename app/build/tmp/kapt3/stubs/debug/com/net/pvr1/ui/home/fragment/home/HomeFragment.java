package com.net.pvr1.ui.home.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import com.net.pvr1.R;
import com.net.pvr1.databinding.FragmentHomeBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.HomeActivity;
import com.net.pvr1.ui.home.fragment.home.adapter.*;
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse;
import com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel;
import com.net.pvr1.ui.movieDetails.MovieDetailsActivity;
import com.net.pvr1.ui.player.PlayerActivity;
import com.net.pvr1.ui.selectCity.SelectCityActivity;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import java.util.*;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0082\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u00052\u00020\u0006B\u0005\u00a2\u0006\u0002\u0010\u0007J\b\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001cH\u0002J\u0010\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020 H\u0016J&\u0010!\u001a\u0004\u0018\u00010\"2\u0006\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016J\u0010\u0010)\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020*H\u0016J\u0010\u0010+\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020 H\u0016J\u0010\u0010,\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020*H\u0016J\u0010\u0010-\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020*H\u0016J\u001a\u0010.\u001a\u00020\u001c2\u0006\u0010/\u001a\u00020\"2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016J\u0010\u00100\u001a\u00020\u001c2\u0006\u00101\u001a\u000202H\u0002R\u001b\u0010\b\u001a\u00020\t8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0013X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/home/HomeFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeCinemaCategoryAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeSliderAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/home/fragment/home/adapter/HomePromotionAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeMoviesAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeTrailerAdapter$RecycleViewItemClickListener;", "()V", "authViewModel", "Lcom/net/pvr1/ui/home/fragment/home/viewModel/HomeViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/home/fragment/home/viewModel/HomeViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/FragmentHomeBinding;", "currentPage", "", "delayMS", "", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "periodMS", "preferences", "Lcom/net/pvr1/di/preference/AppPreferences;", "timer", "Ljava/util/Timer;", "homeApi", "", "movedNext", "onCategoryClick", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Mfi;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onMoviesClick", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Mv;", "onPromotionClick", "onSliderBookClick", "onTrailerClick", "onViewCreated", "view", "retrieveData", "output", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Output;", "app_debug"})
public final class HomeFragment extends androidx.fragment.app.Fragment implements com.net.pvr1.ui.home.fragment.home.adapter.HomeCinemaCategoryAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.home.fragment.home.adapter.HomeSliderAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.home.fragment.home.adapter.HomePromotionAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.home.fragment.home.adapter.HomeMoviesAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.home.fragment.home.adapter.HomeTrailerAdapter.RecycleViewItemClickListener {
    private com.net.pvr1.databinding.FragmentHomeBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.di.preference.AppPreferences preferences;
    private int currentPage = 0;
    private java.util.Timer timer;
    private final long delayMS = 3000L;
    private final long periodMS = 3000L;
    
    public HomeFragment() {
        super();
    }
    
    private final com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel getAuthViewModel() {
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
    
    private final void homeApi() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Output output) {
    }
    
    @java.lang.Override()
    public void onCategoryClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mfi comingSoonItem) {
    }
    
    @java.lang.Override()
    public void onSliderBookClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mv comingSoonItem) {
    }
    
    @java.lang.Override()
    public void onPromotionClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mfi comingSoonItem) {
    }
    
    @java.lang.Override()
    public void onMoviesClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mv comingSoonItem) {
    }
    
    @java.lang.Override()
    public void onTrailerClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mv comingSoonItem) {
    }
}