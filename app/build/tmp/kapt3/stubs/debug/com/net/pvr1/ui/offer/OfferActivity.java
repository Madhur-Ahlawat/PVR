package com.net.pvr1.ui.offer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityOfferBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.HomeActivity;
import com.net.pvr1.ui.offer.adapter.OfferAdapter;
import com.net.pvr1.ui.offer.offerDetails.OfferDetailsActivity;
import com.net.pvr1.ui.offer.response.OfferResponse;
import com.net.pvr1.ui.offer.viewModel.OfferViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u000fH\u0002J\u0012\u0010\u0013\u001a\u00020\u000f2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0014J\u0016\u0010\u0016\u001a\u00020\u000f2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00110\u0018H\u0002R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/net/pvr1/ui/offer/OfferActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/offer/adapter/OfferAdapter$Direction;", "()V", "authViewModel", "Lcom/net/pvr1/ui/offer/viewModel/OfferViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/offer/viewModel/OfferViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityOfferBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "offerClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/offer/response/OfferResponse$Output;", "offerDataLoad", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "retrieveData", "output", "", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class OfferActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.offer.adapter.OfferAdapter.Direction {
    private com.net.pvr1.databinding.ActivityOfferBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    
    public OfferActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.offer.viewModel.OfferViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void offerDataLoad() {
    }
    
    private final void retrieveData(java.util.List<com.net.pvr1.ui.offer.response.OfferResponse.Output> output) {
    }
    
    @java.lang.Override()
    public void offerClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.offer.response.OfferResponse.Output comingSoonItem) {
    }
}