package com.net.pvr1.ui.myBookings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityMyBookingBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse;
import com.net.pvr1.ui.myBookings.adapter.FoodTicketAdapter;
import com.net.pvr1.ui.myBookings.adapter.GiftCardAdapter;
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse;
import com.net.pvr1.ui.myBookings.response.GiftCardResponse;
import com.net.pvr1.ui.myBookings.viewModel.MyBookingViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0011H\u0002J\b\u0010\u0013\u001a\u00020\u0011H\u0002J\u0012\u0010\u0014\u001a\u00020\u00112\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\u0010\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u001eH\u0002R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/net/pvr1/ui/myBookings/MyBookingsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/myBookings/adapter/GiftCardAdapter$Direction;", "()V", "authViewModel", "Lcom/net/pvr1/ui/myBookings/viewModel/MyBookingViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/myBookings/viewModel/MyBookingViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityMyBookingBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "preferences", "Lcom/net/pvr1/di/preference/AppPreferences;", "foodTicket", "", "giftCard", "movedNext", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "resend", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output$C;", "retrieveFoodTicketData", "output", "Lcom/net/pvr1/ui/myBookings/response/FoodTicketResponse$Output;", "retrieveGiftCardData", "Lcom/net/pvr1/ui/myBookings/response/GiftCardResponse$Output;", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class MyBookingsActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.myBookings.adapter.GiftCardAdapter.Direction {
    private com.net.pvr1.di.preference.AppPreferences preferences;
    private com.net.pvr1.databinding.ActivityMyBookingBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    
    public MyBookingsActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.myBookings.viewModel.MyBookingViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movedNext() {
    }
    
    private final void giftCard() {
    }
    
    private final void foodTicket() {
    }
    
    private final void retrieveFoodTicketData(com.net.pvr1.ui.myBookings.response.FoodTicketResponse.Output output) {
    }
    
    private final void retrieveGiftCardData(com.net.pvr1.ui.myBookings.response.GiftCardResponse.Output output) {
    }
    
    @java.lang.Override()
    public void resend(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C comingSoonItem) {
    }
}