package com.net.pvr1.ui.selectCity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.net.pvr1.R;
import com.net.pvr1.ui.selectCity.response.SelectCityResponse;
import java.util.*;
import kotlin.collections.ArrayList;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0003%&\'Ba\u0012\u0016\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00020\u00060\u0005j\b\u0012\u0004\u0012\u00020\u0006`\u0007\u0012\u0016\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\t0\u0005j\b\u0012\u0004\u0012\u00020\t`\u0007\u0012\u001a\u0010\n\u001a\u0016\u0012\u0004\u0012\u00020\t\u0018\u00010\u0005j\n\u0012\u0004\u0012\u00020\t\u0018\u0001`\u0007\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u00a2\u0006\u0002\u0010\u000fJ\b\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0018\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00022\u0006\u0010 \u001a\u00020\u001cH\u0016J\u0018\u0010!\u001a\u00020\u00022\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u001cH\u0016R*\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\t0\u0005j\b\u0012\u0004\u0012\u00020\t`\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R%\u0010\n\u001a\u0016\u0012\u0004\u0012\u00020\t\u0018\u00010\u0005j\n\u0012\u0004\u0012\u00020\t\u0018\u0001`\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001e\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00020\u00060\u0005j\b\u0012\u0004\u0012\u00020\u0006`\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter$MyViewHolderSearchCity;", "Landroid/widget/Filterable;", "selectCityList", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output$Ot;", "Lkotlin/collections/ArrayList;", "cityList", "", "filteredList", "context", "Landroid/content/Context;", "listner", "Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter$RecycleViewItemClickListener;", "(Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;Landroid/content/Context;Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter$RecycleViewItemClickListener;)V", "getCityList", "()Ljava/util/ArrayList;", "setCityList", "(Ljava/util/ArrayList;)V", "getFilteredList", "getListner", "()Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter$RecycleViewItemClickListener;", "setListner", "(Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter$RecycleViewItemClickListener;)V", "getFilter", "Landroid/widget/Filter;", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "MyViewHolderSearchCity", "RecycleViewItemClickListener", "UserFilter", "app_debug"})
public final class SearchCityAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter.MyViewHolderSearchCity> implements android.widget.Filterable {
    private java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> selectCityList;
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<java.lang.Object> cityList;
    @org.jetbrains.annotations.Nullable()
    private final java.util.ArrayList<java.lang.Object> filteredList = null;
    private android.content.Context context;
    @org.jetbrains.annotations.NotNull()
    private com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter.RecycleViewItemClickListener listner;
    
    public SearchCityAdapter(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> selectCityList, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<java.lang.Object> cityList, @org.jetbrains.annotations.Nullable()
    java.util.ArrayList<java.lang.Object> filteredList, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter.RecycleViewItemClickListener listner) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<java.lang.Object> getCityList() {
        return null;
    }
    
    public final void setCityList(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<java.lang.Object> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.ArrayList<java.lang.Object> getFilteredList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter.RecycleViewItemClickListener getListner() {
        return null;
    }
    
    public final void setListner(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter.RecycleViewItemClickListener p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter.MyViewHolderSearchCity onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter.MyViewHolderSearchCity holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.widget.Filter getFilter() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter$MyViewHolderSearchCity;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "otherCityName", "Landroid/widget/TextView;", "getOtherCityName", "()Landroid/widget/TextView;", "setOtherCityName", "(Landroid/widget/TextView;)V", "app_debug"})
    public static final class MyViewHolderSearchCity extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView otherCityName;
        
        public MyViewHolderSearchCity(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getOtherCityName() {
            return null;
        }
        
        public final void setOtherCityName(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0016\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00020\u00060\u0005j\b\u0012\u0004\u0012\u00020\u0006`\u00072\u0006\u0010\b\u001a\u00020\tH&\u00a8\u0006\n"}, d2 = {"Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter$RecycleViewItemClickListener;", "", "onItemClickCitySearch", "", "city", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output$Ot;", "Lkotlin/collections/ArrayList;", "position", "", "app_debug"})
    public static abstract interface RecycleViewItemClickListener {
        
        public abstract void onItemClickCitySearch(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> city, int position);
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0016\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00020\u00060\u0005j\b\u0012\u0004\u0012\u00020\u0006`\u0007\u0012\u0016\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\t0\u0005j\b\u0012\u0004\u0012\u00020\t`\u0007\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0014J\u001c\u0010\u0017\u001a\u00020\u00182\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0019\u001a\u0004\u0018\u00010\u0014H\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR*\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00020\u00060\u0005j\b\u0012\u0004\u0012\u00020\u0006`\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\r\"\u0004\b\u000f\u0010\u0010R*\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\t0\u0005j\b\u0012\u0004\u0012\u00020\t`\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\r\"\u0004\b\u0012\u0010\u0010\u00a8\u0006\u001a"}, d2 = {"Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter$UserFilter;", "Landroid/widget/Filter;", "adapter", "Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter;", "originalList", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "otList", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output$Ot;", "(Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter;Ljava/util/ArrayList;Ljava/util/ArrayList;)V", "filteredList", "getFilteredList", "()Ljava/util/ArrayList;", "getOriginalList", "setOriginalList", "(Ljava/util/ArrayList;)V", "getOtList", "setOtList", "performFiltering", "Landroid/widget/Filter$FilterResults;", "constraint", "", "publishResults", "", "results", "app_debug"})
    static final class UserFilter extends android.widget.Filter {
        private final com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter adapter = null;
        @org.jetbrains.annotations.NotNull()
        private java.util.ArrayList<java.lang.Object> originalList;
        @org.jetbrains.annotations.NotNull()
        private java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> otList;
        @org.jetbrains.annotations.NotNull()
        private final java.util.ArrayList<java.lang.Object> filteredList = null;
        
        public UserFilter(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter adapter, @org.jetbrains.annotations.NotNull()
        java.util.ArrayList<java.lang.Object> originalList, @org.jetbrains.annotations.NotNull()
        java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> otList) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.ArrayList<java.lang.Object> getOriginalList() {
            return null;
        }
        
        public final void setOriginalList(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<java.lang.Object> p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> getOtList() {
            return null;
        }
        
        public final void setOtList(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.ArrayList<java.lang.Object> getFilteredList() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        protected android.widget.Filter.FilterResults performFiltering(@org.jetbrains.annotations.NotNull()
        java.lang.CharSequence constraint) {
            return null;
        }
        
        @android.annotation.SuppressLint(value = {"NotifyDataSetChanged"})
        @java.lang.Override()
        protected void publishResults(@org.jetbrains.annotations.Nullable()
        java.lang.CharSequence constraint, @org.jetbrains.annotations.Nullable()
        android.widget.Filter.FilterResults results) {
        }
    }
}