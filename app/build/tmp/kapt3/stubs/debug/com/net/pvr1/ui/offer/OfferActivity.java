package com.net.pvr1.ui.offer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityOfferBinding;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.offer.adapter.OffersAdapter;
import com.net.pvr1.ui.offer.response.LocalOfferList;
import com.net.pvr1.ui.offer.response.MOfferResponse;
import com.net.pvr1.ui.offer.viewModel.OfferViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import com.net.pvr1.utils.PreferenceManager;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J0\u0010 \u001a\u0012\u0012\u0004\u0012\u00020\u00110\u0013j\b\u0012\u0004\u0012\u00020\u0011`\u00152\u0016\u0010!\u001a\u0012\u0012\u0004\u0012\u00020\"0\u0013j\b\u0012\u0004\u0012\u00020\"`\u0015H\u0002J\b\u0010#\u001a\u00020$H\u0002J\u0012\u0010%\u001a\u00020$2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0014J8\u0010(\u001a\u00020$2\u0016\u0010\u0010\u001a\u0012\u0012\u0004\u0012\u00020\u00110\u0013j\b\u0012\u0004\u0012\u00020\u0011`\u00152\u0016\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u00140\u0013j\b\u0012\u0004\u0012\u00020\u0014`\u0015H\u0002R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R*\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u00140\u0013j\b\u0012\u0004\u0012\u00020\u0014`\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001e\u0010\u001a\u001a\u00020\u001b8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001f\u00a8\u0006)"}, d2 = {"Lcom/net/pvr1/ui/offer/OfferActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "authViewModel", "Lcom/net/pvr1/ui/offer/viewModel/OfferViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/offer/viewModel/OfferViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityOfferBinding;", "catList", "", "", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "offerList", "Lcom/net/pvr1/ui/offer/response/LocalOfferList;", "phList", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/offer/response/MOfferResponse$Output$Ph;", "Lkotlin/collections/ArrayList;", "getPhList", "()Ljava/util/ArrayList;", "setPhList", "(Ljava/util/ArrayList;)V", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "getList", "offers", "Lcom/net/pvr1/ui/offer/response/MOfferResponse$Output$Offer;", "offerDataLoad", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "updateAdapter", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class OfferActivity extends androidx.appcompat.app.AppCompatActivity {
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    private com.net.pvr1.databinding.ActivityOfferBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<com.net.pvr1.ui.offer.response.MOfferResponse.Output.Ph> phList;
    private java.util.List<java.lang.String> catList;
    private java.util.List<com.net.pvr1.ui.offer.response.LocalOfferList> offerList;
    
    public OfferActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.utils.PreferenceManager getPreferences() {
        return null;
    }
    
    public final void setPreferences(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.utils.PreferenceManager p0) {
    }
    
    private final com.net.pvr1.ui.offer.viewModel.OfferViewModel getAuthViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<com.net.pvr1.ui.offer.response.MOfferResponse.Output.Ph> getPhList() {
        return null;
    }
    
    public final void setPhList(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.net.pvr1.ui.offer.response.MOfferResponse.Output.Ph> p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void offerDataLoad() {
    }
    
    private final void updateAdapter(java.util.ArrayList<com.net.pvr1.ui.offer.response.LocalOfferList> offerList, java.util.ArrayList<com.net.pvr1.ui.offer.response.MOfferResponse.Output.Ph> phList) {
    }
    
    private final java.util.ArrayList<com.net.pvr1.ui.offer.response.LocalOfferList> getList(java.util.ArrayList<com.net.pvr1.ui.offer.response.MOfferResponse.Output.Offer> offers) {
        return null;
    }
}