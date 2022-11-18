package com.net.pvr1.ui.summery;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivitySummeryBinding;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.food.CartModel;
import com.net.pvr1.ui.seatLayout.adapter.AddFoodCartAdapter;
import com.net.pvr1.ui.summery.adapter.SeatListAdapter;
import com.net.pvr1.ui.summery.response.SummeryResponse;
import com.net.pvr1.ui.summery.viewModel.SummeryViewModel;
import com.net.pvr1.utils.*;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000eH\u0016J\b\u0010\u001d\u001a\u00020\u001bH\u0002J\u0010\u0010\u001e\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000eH\u0016J\b\u0010\u001f\u001a\u00020\u001bH\u0002J\u0012\u0010 \u001a\u00020\u001b2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0014J\u0010\u0010#\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020\u000eH\u0002J\u0010\u0010%\u001a\u00020\u001b2\u0006\u0010&\u001a\u00020\'H\u0003J\b\u0010(\u001a\u00020\u001bH\u0002J\b\u0010)\u001a\u00020\u001bH\u0002J\b\u0010*\u001a\u00020\u001bH\u0002J\u0010\u0010+\u001a\u00020\u001b2\u0006\u0010,\u001a\u00020\u000eH\u0002R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\f\u001a\u0012\u0012\u0004\u0012\u00020\u000e0\rj\b\u0012\u0004\u0012\u00020\u000e`\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0014\u001a\u00020\u00158\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019\u00a8\u0006-"}, d2 = {"Lcom/net/pvr1/ui/summery/SummeryActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/seatLayout/adapter/AddFoodCartAdapter$RecycleViewItemClickListenerCity;", "()V", "authViewModel", "Lcom/net/pvr1/ui/summery/viewModel/SummeryViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/summery/viewModel/SummeryViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivitySummeryBinding;", "cartModel", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/food/CartModel;", "Lkotlin/collections/ArrayList;", "itemDescription", "", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "decreaseFoodClick", "", "comingSoonItem", "foodCart", "increaseFoodClick", "movedNext", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "removeCartItem", "item", "retrieveData", "output", "Lcom/net/pvr1/ui/summery/response/SummeryResponse$Output;", "saveFood", "seatWithFood", "summeryDetails", "updateCartFoodCartList", "recyclerData", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class SummeryActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.seatLayout.adapter.AddFoodCartAdapter.RecycleViewItemClickListenerCity {
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    private com.net.pvr1.databinding.ActivitySummeryBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    private java.util.ArrayList<com.net.pvr1.ui.food.CartModel> cartModel;
    private java.lang.String itemDescription = "";
    
    public SummeryActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.utils.PreferenceManager getPreferences() {
        return null;
    }
    
    public final void setPreferences(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.utils.PreferenceManager p0) {
    }
    
    private final com.net.pvr1.ui.summery.viewModel.SummeryViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void foodCart() {
    }
    
    private final void movedNext() {
    }
    
    private final void seatWithFood() {
    }
    
    private final void saveFood() {
    }
    
    private final void summeryDetails() {
    }
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    private final void retrieveData(com.net.pvr1.ui.summery.response.SummeryResponse.Output output) {
    }
    
    @java.lang.Override()
    public void increaseFoodClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.CartModel comingSoonItem) {
    }
    
    @java.lang.Override()
    public void decreaseFoodClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.CartModel comingSoonItem) {
    }
    
    private final void updateCartFoodCartList(com.net.pvr1.ui.food.CartModel recyclerData) {
    }
    
    private final void removeCartItem(com.net.pvr1.ui.food.CartModel item) {
    }
}