package com.net.pvr1.ui.bookingSession;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityBookingBinding;
import com.net.pvr1.ui.bookingSession.adapter.BookingShowsDaysAdapter;
import com.net.pvr1.ui.bookingSession.adapter.BookingShowsLanguageAdapter;
import com.net.pvr1.ui.bookingSession.adapter.BookingShowsParentAdapter;
import com.net.pvr1.ui.bookingSession.adapter.BookingTheatreAdapter;
import com.net.pvr1.ui.bookingSession.response.BookingResponse;
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse;
import com.net.pvr1.ui.bookingSession.viewModel.BookingViewModel;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.utils.*;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004B\u0005\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001c\u001a\u00020\u001bH\u0002J\u0010\u0010\u001d\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0012\u0010 \u001a\u00020\u001b2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0014J\u0010\u0010#\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020\u000fH\u0002J\u0010\u0010%\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020&H\u0002J\u0010\u0010\'\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020(H\u0016J\u0010\u0010)\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020*H\u0016R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0014\u001a\u00020\u00158\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019\u00a8\u0006+"}, d2 = {"Lcom/net/pvr1/ui/bookingSession/BookingActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/bookingSession/adapter/BookingShowsDaysAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/bookingSession/adapter/BookingShowsLanguageAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/bookingSession/adapter/BookingTheatreAdapter$RecycleViewItemClickListener;", "()V", "authViewModel", "Lcom/net/pvr1/ui/bookingSession/viewModel/BookingViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/bookingSession/viewModel/BookingViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityBookingBinding;", "daySessionResponse", "Lcom/net/pvr1/ui/bookingSession/response/BookingResponse$Output;", "daysClick", "", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "bookingTheatreDataLoad", "", "bookingTicketDataLoad", "languageClick", "comingSoonItem", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "retrieveData", "output", "retrieveTheatreData", "Lcom/net/pvr1/ui/bookingSession/response/BookingTheatreResponse$Output;", "showsDaysClick", "Lcom/net/pvr1/ui/bookingSession/response/BookingResponse$Output$Dy;", "theatreClick", "Lcom/net/pvr1/ui/bookingSession/response/BookingTheatreResponse$Output$M;", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class BookingActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.bookingSession.adapter.BookingShowsDaysAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.bookingSession.adapter.BookingShowsLanguageAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.bookingSession.adapter.BookingTheatreAdapter.RecycleViewItemClickListener {
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    private com.net.pvr1.databinding.ActivityBookingBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private com.net.pvr1.ui.bookingSession.response.BookingResponse.Output daySessionResponse;
    private boolean daysClick = false;
    
    public BookingActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.utils.PreferenceManager getPreferences() {
        return null;
    }
    
    public final void setPreferences(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.utils.PreferenceManager p0) {
    }
    
    private final com.net.pvr1.ui.bookingSession.viewModel.BookingViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void bookingTicketDataLoad() {
    }
    
    private final void bookingTheatreDataLoad() {
    }
    
    private final void retrieveTheatreData(com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse.Output output) {
    }
    
    private final void retrieveData(com.net.pvr1.ui.bookingSession.response.BookingResponse.Output output) {
    }
    
    @java.lang.Override()
    public void showsDaysClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.bookingSession.response.BookingResponse.Output.Dy comingSoonItem) {
    }
    
    @java.lang.Override()
    public void languageClick(@org.jetbrains.annotations.NotNull()
    java.lang.String comingSoonItem) {
    }
    
    @java.lang.Override()
    public void theatreClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse.Output.M comingSoonItem) {
    }
}