package com.net.pvr1.ui.food;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityFoodBinding;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.food.adapter.*;
import com.net.pvr1.ui.food.response.FoodResponse;
import com.net.pvr1.ui.food.viewModel.FoodViewModel;
import com.net.pvr1.ui.summery.SummeryActivity;
import com.net.pvr1.utils.*;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u00aa\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u000e\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u00052\u00020\u0006B\u0005\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u000201H\u0016J\u0018\u00102\u001a\u00020-2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u000201H\u0016J\u0018\u00103\u001a\u00020-2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u000201H\u0016J\u001e\u00104\u001a\u00020-2\f\u0010.\u001a\b\u0012\u0004\u0012\u000206052\u0006\u00107\u001a\u00020#H\u0016J\u001e\u00108\u001a\u00020-2\f\u0010.\u001a\b\u0012\u0004\u0012\u000206052\u0006\u00109\u001a\u00020#H\u0002J\b\u0010:\u001a\u00020-H\u0003J\u0018\u0010;\u001a\u00020-2\u0006\u0010.\u001a\u00020\u00122\u0006\u00100\u001a\u000201H\u0016J\u0018\u0010<\u001a\u00020-2\u0006\u0010.\u001a\u00020\u00122\u0006\u00100\u001a\u000201H\u0016J\u0010\u0010=\u001a\u00020-2\u0006\u0010.\u001a\u00020>H\u0016J\u0010\u0010?\u001a\u00020-2\u0006\u0010.\u001a\u00020\u001bH\u0016J\u001e\u0010@\u001a\u00020-2\f\u0010.\u001a\b\u0012\u0004\u0012\u000206052\u0006\u00107\u001a\u00020#H\u0016J\u0018\u0010A\u001a\u00020-2\u0006\u0010.\u001a\u00020\u001b2\u0006\u00100\u001a\u000201H\u0016J\u0018\u0010B\u001a\u00020-2\u0006\u0010.\u001a\u00020\u001b2\u0006\u00100\u001a\u000201H\u0016J\u0010\u0010C\u001a\u00020-2\u0006\u0010.\u001a\u000206H\u0016J\u0010\u0010D\u001a\u00020-2\u0006\u0010.\u001a\u000206H\u0016J\u0010\u0010E\u001a\u00020-2\u0006\u0010.\u001a\u000206H\u0016J\u0010\u0010F\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0016J\b\u0010G\u001a\u00020-H\u0002J \u0010H\u001a\u0012\u0012\u0004\u0012\u00020\u001b0\u0011j\b\u0012\u0004\u0012\u00020\u001b`\u00132\u0006\u0010I\u001a\u00020#H\u0002J\u0010\u0010J\u001a\u00020+2\u0006\u0010K\u001a\u00020/H\u0002J\b\u0010L\u001a\u00020-H\u0002J\u0012\u0010M\u001a\u00020-2\b\u0010N\u001a\u0004\u0018\u00010OH\u0014J\u0010\u0010P\u001a\u00020-2\u0006\u0010Q\u001a\u00020\u0012H\u0002J\u0010\u0010R\u001a\u00020-2\u0006\u0010S\u001a\u00020\u001fH\u0002J\u0010\u0010T\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0002J\u0010\u0010U\u001a\u00020-2\u0006\u0010.\u001a\u000206H\u0002J\u0010\u0010V\u001a\u00020+2\u0006\u0010K\u001a\u000206H\u0002J\u0010\u0010W\u001a\u00020-2\u0006\u0010X\u001a\u00020\u0012H\u0002J\u0010\u0010Y\u001a\u00020-2\u0006\u0010X\u001a\u00020\u0012H\u0002J\u0010\u0010Z\u001a\u00020-2\u0006\u0010.\u001a\u00020\u001bH\u0002J\u0010\u0010[\u001a\u00020+2\u0006\u0010K\u001a\u00020\u001bH\u0002J\u0010\u0010\\\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0002R\u001b\u0010\b\u001a\u00020\t8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0010\u001a\u0012\u0012\u0004\u0012\u00020\u00120\u0011j\b\u0012\u0004\u0012\u00020\u0012`\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u001a\u001a\u0016\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u0011j\n\u0012\u0004\u0012\u00020\u001b\u0018\u0001`\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010$\u001a\u00020%8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\'\"\u0004\b(\u0010)R\u000e\u0010*\u001a\u00020+X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006]"}, d2 = {"Lcom/net/pvr1/ui/food/FoodActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/food/adapter/FoodBestSellerAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/food/adapter/CategoryAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/food/adapter/FilterAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/food/adapter/FilterBottomAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/food/adapter/CartAdapter$RecycleViewItemClickListenerCity;", "()V", "authViewModel", "Lcom/net/pvr1/ui/food/viewModel/FoodViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/food/viewModel/FoodViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityFoodBinding;", "cartModel", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/food/CartModel;", "Lkotlin/collections/ArrayList;", "categoryAdapter", "Lcom/net/pvr1/ui/food/adapter/CategoryAdapter;", "filterAdapter", "Lcom/net/pvr1/ui/food/adapter/FilterAdapter;", "filterBottomAdapter", "Lcom/net/pvr1/ui/food/adapter/FilterBottomAdapter;", "filterResponse", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Mfl;", "foodBestSellerAdapter", "Lcom/net/pvr1/ui/food/adapter/FoodBestSellerAdapter;", "foodResponse", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "masterId", "", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "up", "", "addFood", "", "comingSoonItem", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Bestseller;", "position", "", "addFoodMinus", "addFoodPlus", "bestSellerDialog", "", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Bestseller$R;", "title", "bottomDialog", "titleTxt", "cartData", "cartFoodMinus", "cartFoodPlus", "categoryClick", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Cat;", "categoryFoodClick", "categoryFoodDialog", "categoryFoodMinus", "categoryFoodPlus", "filterBtFoodClick", "filterBtFoodMinus", "filterBtFoodPlus", "foodBestImageClick", "foodDetails", "getFilterCategoryList", "category", "itemExist", "foodItem", "movedNext", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "removeCartItem", "item", "retrieveData", "output", "updateBestSellerCartList", "updateBottomFoodCartList", "updateBottomItemExist", "updateCartFoodCartList", "recyclerData", "updateCartMainList", "updateCategoryFoodCartList", "updateCategoryItemExist", "updateHomeCartList", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class FoodActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.food.adapter.FoodBestSellerAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.food.adapter.CategoryAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.food.adapter.FilterAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.food.adapter.FilterBottomAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.food.adapter.CartAdapter.RecycleViewItemClickListenerCity {
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    private com.net.pvr1.databinding.ActivityFoodBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final java.util.ArrayList<com.net.pvr1.ui.food.CartModel> cartModel = null;
    private java.util.ArrayList<com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl> filterResponse;
    private com.net.pvr1.ui.food.response.FoodResponse.Output foodResponse;
    private boolean up = false;
    private com.net.pvr1.ui.food.adapter.FoodBestSellerAdapter foodBestSellerAdapter;
    private com.net.pvr1.ui.food.adapter.CategoryAdapter categoryAdapter;
    private com.net.pvr1.ui.food.adapter.FilterAdapter filterAdapter;
    private com.net.pvr1.ui.food.adapter.FilterBottomAdapter filterBottomAdapter;
    private java.lang.String masterId = "0";
    
    public FoodActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.utils.PreferenceManager getPreferences() {
        return null;
    }
    
    public final void setPreferences(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.utils.PreferenceManager p0) {
    }
    
    private final com.net.pvr1.ui.food.viewModel.FoodViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void foodDetails() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.food.response.FoodResponse.Output output) {
    }
    
    @java.lang.Override()
    public void foodBestImageClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem) {
    }
    
    @java.lang.Override()
    public void addFood(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem, int position) {
    }
    
    private final void movedNext() {
    }
    
    private final void updateBestSellerCartList(com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem) {
    }
    
    private final boolean itemExist(com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller foodItem) {
        return false;
    }
    
    @java.lang.Override()
    public void addFoodPlus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem, int position) {
    }
    
    @java.lang.Override()
    public void addFoodMinus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem, int position) {
    }
    
    @java.lang.Override()
    public void bestSellerDialog(@org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R> comingSoonItem, @org.jetbrains.annotations.NotNull()
    java.lang.String title) {
    }
    
    private final void updateHomeCartList(com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem) {
    }
    
    @java.lang.Override()
    public void categoryClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Cat comingSoonItem) {
    }
    
    private final java.util.ArrayList<com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl> getFilterCategoryList(java.lang.String category) {
        return null;
    }
    
    @java.lang.Override()
    public void categoryFoodClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl comingSoonItem) {
    }
    
    @java.lang.Override()
    public void categoryFoodPlus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl comingSoonItem, int position) {
    }
    
    @java.lang.Override()
    public void categoryFoodMinus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl comingSoonItem, int position) {
    }
    
    @java.lang.Override()
    public void categoryFoodDialog(@org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R> comingSoonItem, @org.jetbrains.annotations.NotNull()
    java.lang.String title) {
    }
    
    private final void updateCategoryFoodCartList(com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl comingSoonItem) {
    }
    
    private final boolean updateCategoryItemExist(com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl foodItem) {
        return false;
    }
    
    private final void bottomDialog(java.util.List<com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R> comingSoonItem, java.lang.String titleTxt) {
    }
    
    @java.lang.Override()
    public void filterBtFoodClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R comingSoonItem) {
    }
    
    @java.lang.Override()
    public void filterBtFoodPlus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R comingSoonItem) {
    }
    
    @java.lang.Override()
    public void filterBtFoodMinus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R comingSoonItem) {
    }
    
    private final void updateBottomFoodCartList(com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R comingSoonItem) {
    }
    
    private final boolean updateBottomItemExist(com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R foodItem) {
        return false;
    }
    
    @android.annotation.SuppressLint(value = {"UseCompatLoadingForDrawables"})
    private final void cartData() {
    }
    
    @java.lang.Override()
    public void cartFoodPlus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.CartModel comingSoonItem, int position) {
    }
    
    @java.lang.Override()
    public void cartFoodMinus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.CartModel comingSoonItem, int position) {
    }
    
    private final void updateCartFoodCartList(com.net.pvr1.ui.food.CartModel recyclerData) {
    }
    
    private final void updateCartMainList(com.net.pvr1.ui.food.CartModel recyclerData) {
    }
    
    private final void removeCartItem(com.net.pvr1.ui.food.CartModel item) {
    }
}