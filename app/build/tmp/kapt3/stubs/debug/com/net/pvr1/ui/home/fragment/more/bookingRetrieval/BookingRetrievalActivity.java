package com.net.pvr1.ui.home.fragment.more.bookingRetrieval;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityBookingRetrievalBinding;
import com.net.pvr1.ui.bookingSession.response.BookingResponse;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalAdapter;
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse;
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.viewModel.BookingRetrievalViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import com.net.pvr1.utils.PreferenceManager;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0012\u0010\u0019\u001a\u00020\u00152\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\u0010\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001eH\u0002R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000e\u001a\u00020\u000f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001f"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/BookingRetrievalActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalAdapter$RecycleViewItemClickListener;", "()V", "authViewModel", "Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/viewModel/BookingRetrievalViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/viewModel/BookingRetrievalViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityBookingRetrievalBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "bookingRetrievalApi", "", "dateClick", "comingSoonItem", "Lcom/net/pvr1/ui/bookingSession/response/BookingResponse$Output$Dy;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "retrieveData", "output", "Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/response/BookingRetrievalResponse$Output;", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class BookingRetrievalActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalAdapter.RecycleViewItemClickListener {
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    private com.net.pvr1.databinding.ActivityBookingRetrievalBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    
    public BookingRetrievalActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.utils.PreferenceManager getPreferences() {
        return null;
    }
    
    public final void setPreferences(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.utils.PreferenceManager p0) {
    }
    
    private final com.net.pvr1.ui.home.fragment.more.bookingRetrieval.viewModel.BookingRetrievalViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void bookingRetrievalApi() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse.Output output) {
    }
    
    @java.lang.Override()
    public void dateClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.bookingSession.response.BookingResponse.Output.Dy comingSoonItem) {
    }
}