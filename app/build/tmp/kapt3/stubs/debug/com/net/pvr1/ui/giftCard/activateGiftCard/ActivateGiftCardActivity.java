package com.net.pvr1.ui.giftCard.activateGiftCard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityActivateGiftCardBinding;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.giftCard.activateGiftCard.adapter.ActivateGiftCardAdapter;
import com.net.pvr1.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel;
import com.net.pvr1.ui.giftCard.response.GiftCardResponse;
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Suppress(names = {"DEPRECATION"})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0012\u0010\u0012\u001a\u00020\u000f2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0014J\u0010\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/net/pvr1/ui/giftCard/activateGiftCard/ActivateGiftCardActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/giftCard/activateGiftCard/adapter/ActivateGiftCardAdapter$RecycleViewItemClickListener;", "()V", "authViewModel", "Lcom/net/pvr1/ui/giftCard/activateGiftCard/viewModel/ActivateGiftCardViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/giftCard/activateGiftCard/viewModel/ActivateGiftCardViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityActivateGiftCardBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "activateGiftCard", "", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output$C;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "retrieveData", "output", "Lcom/net/pvr1/ui/giftCard/response/GiftCardResponse$Output;", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class ActivateGiftCardActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.giftCard.activateGiftCard.adapter.ActivateGiftCardAdapter.RecycleViewItemClickListener {
    private com.net.pvr1.databinding.ActivityActivateGiftCardBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    
    public ActivateGiftCardActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void activateGiftCard() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.giftCard.response.GiftCardResponse.Output output) {
    }
    
    @java.lang.Override()
    public void activateGiftCard(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C comingSoonItem) {
    }
}