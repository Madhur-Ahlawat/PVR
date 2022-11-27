package com.net.pvr1.ui.food;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityFoodBinding;
import com.net.pvr1.databinding.FoodBottomAddFoodBinding;
import com.net.pvr1.databinding.FoodCartDialogBinding;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.food.adapter.*;
import com.net.pvr1.ui.food.response.FoodResponse;
import com.net.pvr1.ui.food.viewModel.FoodViewModel;
import com.net.pvr1.ui.summery.SummeryActivity;
import com.net.pvr1.utils.*;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u00aa\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000e\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u00052\u00020\u0006B\u0005\u00a2\u0006\u0002\u0010\u0007J\u0018\u00103\u001a\u0002042\u0006\u00105\u001a\u00020\u001b2\u0006\u00106\u001a\u00020\u001fH\u0016J\u0018\u00107\u001a\u0002042\u0006\u00105\u001a\u00020\u001b2\u0006\u00106\u001a\u00020\u001fH\u0016J\u0018\u00108\u001a\u0002042\u0006\u00105\u001a\u00020\u001b2\u0006\u00106\u001a\u00020\u001fH\u0016J\u001e\u00109\u001a\u0002042\f\u00105\u001a\b\u0012\u0004\u0012\u00020;0:2\u0006\u00106\u001a\u00020\u0011H\u0016J\u0010\u0010<\u001a\u0002042\u0006\u00105\u001a\u00020\u001bH\u0003J\u0010\u0010=\u001a\u0002042\u0006\u00105\u001a\u00020\u0019H\u0003J\u001e\u0010>\u001a\u0002042\f\u00105\u001a\b\u0012\u0004\u0012\u00020;0:2\u0006\u0010?\u001a\u00020\u0011H\u0002J\b\u0010@\u001a\u00020\u001fH\u0002J\b\u0010A\u001a\u000204H\u0003J\b\u0010B\u001a\u000204H\u0003J\u0018\u0010C\u001a\u0002042\u0006\u00105\u001a\u00020\u00162\u0006\u00106\u001a\u00020\u001fH\u0016J\u0018\u0010D\u001a\u0002042\u0006\u00105\u001a\u00020\u00162\u0006\u00106\u001a\u00020\u001fH\u0016J\u0010\u0010E\u001a\u0002042\u0006\u00105\u001a\u00020#H\u0016J\u0010\u0010F\u001a\u0002042\u0006\u00105\u001a\u00020\u0019H\u0016J\u001e\u0010G\u001a\u0002042\f\u00105\u001a\b\u0012\u0004\u0012\u00020;0:2\u0006\u0010H\u001a\u00020\u0011H\u0016J\u0010\u0010I\u001a\u0002042\u0006\u00105\u001a\u00020\u0019H\u0016J\u0018\u0010J\u001a\u0002042\u0006\u00105\u001a\u00020\u00192\u0006\u00106\u001a\u00020\u001fH\u0016J\u0018\u0010K\u001a\u0002042\u0006\u00105\u001a\u00020\u00192\u0006\u00106\u001a\u00020\u001fH\u0016J\u0010\u0010L\u001a\u0002042\u0006\u00105\u001a\u00020;H\u0016J\u0010\u0010M\u001a\u0002042\u0006\u00105\u001a\u00020;H\u0016J\u0010\u0010N\u001a\u0002042\u0006\u00105\u001a\u00020;H\u0016J\u0010\u0010O\u001a\u0002042\u0006\u00105\u001a\u00020\u001bH\u0016J\b\u0010P\u001a\u000204H\u0002J0\u0010Q\u001a\u0012\u0012\u0004\u0012\u00020\u00190\u0015j\b\u0012\u0004\u0012\u00020\u0019`\u00172\u0006\u0010R\u001a\u00020S2\u0006\u0010(\u001a\u00020\u001f2\u0006\u0010T\u001a\u00020\u0011H\u0002J&\u0010U\u001a\b\u0012\u0004\u0012\u00020\u001b0:2\u0006\u0010R\u001a\u00020S2\u0006\u0010(\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010V\u001a\u00020S2\u0006\u0010W\u001a\u00020\u001bH\u0002J\b\u0010X\u001a\u000204H\u0002J\u0012\u0010Y\u001a\u0002042\b\u0010Z\u001a\u0004\u0018\u00010[H\u0014J\u0010\u0010\\\u001a\u0002042\u0006\u0010]\u001a\u00020\u0016H\u0002J\u0010\u0010^\u001a\u0002042\u0006\u0010_\u001a\u00020!H\u0002J\u0010\u0010`\u001a\u0002042\u0006\u00105\u001a\u00020\u001bH\u0002J\u0010\u0010a\u001a\u0002042\u0006\u00105\u001a\u00020;H\u0002J\u0010\u0010b\u001a\u00020S2\u0006\u0010W\u001a\u00020;H\u0002J\u0010\u0010c\u001a\u0002042\u0006\u0010d\u001a\u00020\u0016H\u0002J\u0010\u0010e\u001a\u0002042\u0006\u0010d\u001a\u00020\u0016H\u0003J\u0010\u0010f\u001a\u0002042\u0006\u00105\u001a\u00020\u0019H\u0002J\u0010\u0010g\u001a\u00020S2\u0006\u0010W\u001a\u00020\u0019H\u0002J\u0010\u0010h\u001a\u0002042\u0006\u00105\u001a\u00020\u001bH\u0002R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u00020\u00160\u0015j\b\u0012\u0004\u0012\u00020\u0016`\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u001d\u001a\u0016\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u0015j\n\u0012\u0004\u0012\u00020\u0019\u0018\u0001`\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\"\u001a\u0012\u0012\u0004\u0012\u00020#0\u0015j\b\u0012\u0004\u0012\u00020#`\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u0004\u0018\u00010&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\'\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010)\u001a\u00020*8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u0010\u0010/\u001a\u0004\u0018\u000100X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00101\u001a\u0004\u0018\u000102X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006i"}, d2 = {"Lcom/net/pvr1/ui/food/FoodActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/food/adapter/SubFoodBestSellerAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/food/adapter/CategoryAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/food/adapter/AllFoodAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/food/adapter/SubAllFoodAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/food/adapter/CartAdapter$RecycleViewItemClickListenerCity;", "()V", "allFoodAdapter", "Lcom/net/pvr1/ui/food/adapter/AllFoodAdapter;", "authViewModel", "Lcom/net/pvr1/ui/food/viewModel/FoodViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/food/viewModel/FoodViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "bestSellerType", "", "binding", "Lcom/net/pvr1/databinding/ActivityFoodBinding;", "cartModel", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/food/CartModel;", "Lkotlin/collections/ArrayList;", "catFilter", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Mfl;", "catFilterBestSeller", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Bestseller;", "categoryName", "filterResponse", "foodLimit", "", "foodResponse", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output;", "foodResponseCategory", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Cat;", "itemCheckPriceCart", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "masterId", "menuType", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "subAllFoodAdapter", "Lcom/net/pvr1/ui/food/adapter/SubAllFoodAdapter;", "subFoodBestSellerAdapter", "Lcom/net/pvr1/ui/food/adapter/SubFoodBestSellerAdapter;", "addFood", "", "comingSoonItem", "position", "addFoodMinus", "addFoodPlus", "bestSellerDialogAddFood", "", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Bestseller$R;", "bottomDialogAddFood", "bottomDialogAllFood", "bottomDialogCart", "titleTxt", "calculateTotal", "cartData", "cartDialog", "cartFoodMinus", "cartFoodPlus", "categoryClick", "categoryFoodClick", "categoryFoodDialog", "title", "categoryFoodImageClick", "categoryFoodMinus", "categoryFoodPlus", "filterBtFoodClick", "filterBtFoodMinus", "filterBtFoodPlus", "foodBestImageClick", "foodDetails", "getFilterAllMfl", "category", "", "name", "getFilterBestSellerList", "itemExist", "foodItem", "movedNext", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "removeCartItem", "item", "retrieveData", "output", "updateBestSellerCartList", "updateBottomFoodCartList", "updateBottomItemExist", "updateCartFoodCartList", "recyclerData", "updateCartMainList", "updateCategoryFoodCartList", "updateCategoryItemExist", "updateHomeCartList", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class FoodActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.food.adapter.SubFoodBestSellerAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.food.adapter.CategoryAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.food.adapter.AllFoodAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.food.adapter.SubAllFoodAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.food.adapter.CartAdapter.RecycleViewItemClickListenerCity {
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    private com.net.pvr1.databinding.ActivityFoodBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final java.util.ArrayList<com.net.pvr1.ui.food.CartModel> cartModel = null;
    private java.util.ArrayList<com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl> filterResponse;
    private java.util.ArrayList<com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl> catFilter;
    private java.util.ArrayList<com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller> catFilterBestSeller;
    private com.net.pvr1.ui.food.response.FoodResponse.Output foodResponse;
    private java.util.ArrayList<com.net.pvr1.ui.food.response.FoodResponse.Output.Cat> foodResponseCategory;
    private com.net.pvr1.ui.food.adapter.SubFoodBestSellerAdapter subFoodBestSellerAdapter;
    private com.net.pvr1.ui.food.adapter.SubAllFoodAdapter subAllFoodAdapter;
    private com.net.pvr1.ui.food.adapter.AllFoodAdapter allFoodAdapter;
    private java.lang.String masterId = "0";
    private java.lang.String categoryName = "ALL";
    private int itemCheckPriceCart = 0;
    private int menuType = 0;
    private java.lang.String bestSellerType = "ALL";
    private int foodLimit = 0;
    
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
    
    private final void movedNext() {
    }
    
    @java.lang.Override()
    public void categoryClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Cat comingSoonItem) {
    }
    
    @java.lang.Override()
    public void foodBestImageClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem) {
    }
    
    @java.lang.Override()
    public void addFood(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem, int position) {
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
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    private final void bottomDialogAddFood(com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem) {
    }
    
    @java.lang.Override()
    public void addFoodMinus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem, int position) {
    }
    
    private final java.util.List<com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller> getFilterBestSellerList(boolean category, int menuType, java.lang.String bestSellerType) {
        return null;
    }
    
    @java.lang.Override()
    public void bestSellerDialogAddFood(@org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R> comingSoonItem, @org.jetbrains.annotations.NotNull()
    java.lang.String position) {
    }
    
    private final void updateHomeCartList(com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller comingSoonItem) {
    }
    
    @java.lang.Override()
    public void categoryFoodDialog(@org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R> comingSoonItem, @org.jetbrains.annotations.NotNull()
    java.lang.String title) {
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
    
    private final int calculateTotal() {
        return 0;
    }
    
    @java.lang.Override()
    public void cartFoodPlus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.CartModel comingSoonItem, int position) {
    }
    
    @java.lang.Override()
    public void cartFoodMinus(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.CartModel comingSoonItem, int position) {
    }
    
    private final void removeCartItem(com.net.pvr1.ui.food.CartModel item) {
    }
    
    @android.annotation.SuppressLint(value = {"NotifyDataSetChanged", "UseCompatLoadingForDrawables", "SetTextI18n"})
    private final void cartData() {
    }
    
    @android.annotation.SuppressLint(value = {"SetTextI18n", "UseCompatLoadingForDrawables"})
    private final void cartDialog() {
    }
    
    private final void updateCartFoodCartList(com.net.pvr1.ui.food.CartModel recyclerData) {
    }
    
    @android.annotation.SuppressLint(value = {"NotifyDataSetChanged"})
    private final void updateCartMainList(com.net.pvr1.ui.food.CartModel recyclerData) {
    }
    
    private final void bottomDialogCart(java.util.List<com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R> comingSoonItem, java.lang.String titleTxt) {
    }
    
    private final void updateCategoryFoodCartList(com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl comingSoonItem) {
    }
    
    private final boolean updateCategoryItemExist(com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl foodItem) {
        return false;
    }
    
    private final java.util.ArrayList<com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl> getFilterAllMfl(boolean category, int menuType, java.lang.String name) {
        return null;
    }
    
    @java.lang.Override()
    public void categoryFoodClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl comingSoonItem) {
    }
    
    @java.lang.Override()
    public void categoryFoodImageClick(@org.jetbrains.annotations.NotNull()
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
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    private final void bottomDialogAllFood(com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl comingSoonItem) {
    }
}