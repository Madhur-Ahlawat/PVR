package com.net.pvr1.ui.payment;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.net.pvr1.databinding.ActivityPaymentBinding;
import com.net.pvr1.databinding.ActivityPlayerBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.player.viewModel.PlayerViewModel;
import com.net.pvr1.utils.PreferenceManager;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\r\u001a\u00020\u000e8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0017"}, d2 = {"Lcom/net/pvr1/ui/payment/PaymentActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "authViewModel", "Lcom/net/pvr1/ui/player/viewModel/PlayerViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/player/viewModel/PlayerViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityPaymentBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class PaymentActivity extends androidx.appcompat.app.AppCompatActivity {
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    private com.net.pvr1.databinding.ActivityPaymentBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    
    public PaymentActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.utils.PreferenceManager getPreferences() {
        return null;
    }
    
    public final void setPreferences(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.utils.PreferenceManager p0) {
    }
    
    private final com.net.pvr1.ui.player.viewModel.PlayerViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
}