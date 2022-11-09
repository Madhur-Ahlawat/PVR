package com.net.pvr1.ui.food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ItemFoodFilterBinding;
import com.net.pvr1.ui.food.response.FoodResponse;
import com.net.pvr1.utils.Constant;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\u0016\u0017B-\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\rH\u0016J\u001c\u0010\u000e\u001a\u00020\u000f2\n\u0010\u0010\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0011\u001a\u00020\rH\u0016J\u001c\u0010\u0012\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\rH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/net/pvr1/ui/food/adapter/FilterAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/food/adapter/FilterAdapter$ViewHolder;", "nowShowingList", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Mfl;", "Lkotlin/collections/ArrayList;", "context", "Landroid/content/Context;", "listener", "Lcom/net/pvr1/ui/food/adapter/FilterAdapter$RecycleViewItemClickListenerCity;", "(Ljava/util/ArrayList;Landroid/content/Context;Lcom/net/pvr1/ui/food/adapter/FilterAdapter$RecycleViewItemClickListenerCity;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "RecycleViewItemClickListenerCity", "ViewHolder", "app_debug"})
public final class FilterAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.food.adapter.FilterAdapter.ViewHolder> {
    private java.util.ArrayList<com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl> nowShowingList;
    private android.content.Context context;
    private com.net.pvr1.ui.food.adapter.FilterAdapter.RecycleViewItemClickListenerCity listener;
    
    public FilterAdapter(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl> nowShowingList, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.adapter.FilterAdapter.RecycleViewItemClickListenerCity listener) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.food.adapter.FilterAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.food.adapter.FilterAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/net/pvr1/ui/food/adapter/FilterAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/net/pvr1/databinding/ItemFoodFilterBinding;", "(Lcom/net/pvr1/ui/food/adapter/FilterAdapter;Lcom/net/pvr1/databinding/ItemFoodFilterBinding;)V", "getBinding", "()Lcom/net/pvr1/databinding/ItemFoodFilterBinding;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.net.pvr1.databinding.ItemFoodFilterBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.databinding.ItemFoodFilterBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.net.pvr1.databinding.ItemFoodFilterBinding getBinding() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u001e\u0010\u0006\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nH&J\u0018\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\rH&J\u0018\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\rH&\u00a8\u0006\u000f"}, d2 = {"Lcom/net/pvr1/ui/food/adapter/FilterAdapter$RecycleViewItemClickListenerCity;", "", "categoryFoodClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Mfl;", "categoryFoodDialog", "", "Lcom/net/pvr1/ui/food/response/FoodResponse$Output$Bestseller$R;", "title", "", "categoryFoodMinus", "position", "", "categoryFoodPlus", "app_debug"})
    public static abstract interface RecycleViewItemClickListenerCity {
        
        public abstract void categoryFoodClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl comingSoonItem);
        
        public abstract void categoryFoodPlus(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl comingSoonItem, int position);
        
        public abstract void categoryFoodMinus(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.food.response.FoodResponse.Output.Mfl comingSoonItem, int position);
        
        public abstract void categoryFoodDialog(@org.jetbrains.annotations.NotNull()
        java.util.List<com.net.pvr1.ui.food.response.FoodResponse.Output.Bestseller.R> comingSoonItem, @org.jetbrains.annotations.NotNull()
        java.lang.String title);
    }
}