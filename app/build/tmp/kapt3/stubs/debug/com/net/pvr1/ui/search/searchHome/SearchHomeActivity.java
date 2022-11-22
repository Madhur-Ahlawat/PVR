package com.net.pvr1.ui.search.searchHome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivitySearchHomeBinding;
import com.net.pvr1.ui.cinemaSession.CinemaSessionActivity;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.movieDetails.nowShowing.NowShowingActivity;
import com.net.pvr1.ui.search.searchHome.adapter.SearchHomeCinemaAdapter;
import com.net.pvr1.ui.search.searchHome.adapter.SearchHomeMovieAdapter;
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse;
import com.net.pvr1.ui.search.searchHome.viewModel.HomeSearchViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.*;

@kotlin.Suppress(names = {"NAME_SHADOWING"})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\"\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0015J\u0012\u0010\u001a\u001a\u00020\u00142\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0014J\u0010\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0010\u0010 \u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0010\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020#H\u0016J\u0010\u0010$\u001a\u00020\u00142\u0006\u0010%\u001a\u00020&H\u0002J\b\u0010\'\u001a\u00020\u0014H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\nR\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/net/pvr1/ui/search/searchHome/SearchHomeActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/search/searchHome/adapter/SearchHomeMovieAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/search/searchHome/adapter/SearchHomeCinemaAdapter$RecycleViewItemClickListenerCity;", "()V", "REQUEST_CODE_SPEECH_INPUT", "", "authViewModel", "Lcom/net/pvr1/ui/search/searchHome/viewModel/HomeSearchViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/search/searchHome/viewModel/HomeSearchViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivitySearchHomeBinding;", "cinemaClick", "", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "movedNext", "", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSearchCinema", "selectCityItemList", "Lcom/net/pvr1/ui/search/searchHome/response/HomeSearchResponse$Output$T;", "onSearchCinemaDirection", "onSearchMovie", "selectCityList", "Lcom/net/pvr1/ui/search/searchHome/response/HomeSearchResponse$Output$M;", "retrieveData", "output", "Lcom/net/pvr1/ui/search/searchHome/response/HomeSearchResponse$Output;", "search", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class SearchHomeActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.search.searchHome.adapter.SearchHomeMovieAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.search.searchHome.adapter.SearchHomeCinemaAdapter.RecycleViewItemClickListenerCity {
    private com.net.pvr1.databinding.ActivitySearchHomeBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private final int REQUEST_CODE_SPEECH_INPUT = 1;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private boolean cinemaClick = false;
    
    public SearchHomeActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.search.searchHome.viewModel.HomeSearchViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movedNext() {
    }
    
    @java.lang.Override()
    @java.lang.Deprecated()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void search() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse.Output output) {
    }
    
    @java.lang.Override()
    public void onSearchCinema(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse.Output.T selectCityItemList) {
    }
    
    @java.lang.Override()
    public void onSearchCinemaDirection(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse.Output.T selectCityItemList) {
    }
    
    @java.lang.Override()
    public void onSearchMovie(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse.Output.M selectCityList) {
    }
}