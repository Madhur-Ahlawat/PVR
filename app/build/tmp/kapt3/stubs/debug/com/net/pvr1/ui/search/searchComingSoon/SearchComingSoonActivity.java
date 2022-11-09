package com.net.pvr1.ui.search.searchComingSoon;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivitySearchComingSoonBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.search.searchComingSoon.adapter.SearchComingSoonAdapter;
import com.net.pvr1.ui.search.searchComingSoon.viewModel.ComingSoonSearchViewModel;
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\"\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0015J\u0012\u0010\u0017\u001a\u00020\u00112\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\u0016\u0010\u001a\u001a\u00020\u00112\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cH\u0016J\u0010\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020 H\u0002J\b\u0010!\u001a\u00020\u0011H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/net/pvr1/ui/search/searchComingSoon/SearchComingSoonActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/search/searchComingSoon/adapter/SearchComingSoonAdapter$RecycleViewItemClickListenerCity;", "()V", "REQUEST_CODE_SPEECH_INPUT", "", "authViewModel", "Lcom/net/pvr1/ui/search/searchComingSoon/viewModel/ComingSoonSearchViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/search/searchComingSoon/viewModel/ComingSoonSearchViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivitySearchComingSoonBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "movedNext", "", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSearchMovie", "selectCityList", "", "Lcom/net/pvr1/ui/search/searchHome/response/HomeSearchResponse$Output$M;", "retrieveData", "output", "Lcom/net/pvr1/ui/search/searchHome/response/HomeSearchResponse$Output;", "search", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class SearchComingSoonActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.search.searchComingSoon.adapter.SearchComingSoonAdapter.RecycleViewItemClickListenerCity {
    private final int REQUEST_CODE_SPEECH_INPUT = 1;
    private com.net.pvr1.databinding.ActivitySearchComingSoonBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    
    public SearchComingSoonActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.search.searchComingSoon.viewModel.ComingSoonSearchViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movedNext() {
    }
    
    private final void search() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse.Output output) {
    }
    
    @java.lang.Override()
    @java.lang.Deprecated()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    @java.lang.Override()
    public void onSearchMovie(@org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse.Output.M> selectCityList) {
    }
}