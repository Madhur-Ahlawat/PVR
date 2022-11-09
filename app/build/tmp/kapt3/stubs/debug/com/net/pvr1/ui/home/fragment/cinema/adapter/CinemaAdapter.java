package com.net.pvr1.ui.home.fragment.cinema.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.databinding.CinemaItemBinding;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse;
import com.net.pvr1.ui.login.LoginActivity;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0004\u001c\u001d\u001e\u001fB=\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u001e\u0010\u0014\u001a\u00020\u00152\n\u0010\u0016\u001a\u00060\u0002R\u00020\u00002\b\b\u0001\u0010\u0017\u001a\u00020\u0013H\u0016J\u001c\u0010\u0018\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0013H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$ViewHolder;", "nowShowingList", "", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output$C;", "context", "Landroid/content/Context;", "listener", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Direction;", "preference", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$SetPreference;", "location", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Location;", "isLogin", "", "(Ljava/util/List;Landroid/content/Context;Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Direction;Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$SetPreference;Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Location;Z)V", "rowIndex", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "Direction", "Location", "SetPreference", "ViewHolder", "app_debug"})
public final class CinemaAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.ViewHolder> {
    private java.util.List<com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C> nowShowingList;
    private android.content.Context context;
    private com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Direction listener;
    private com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.SetPreference preference;
    private com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Location location;
    private boolean isLogin;
    private boolean rowIndex = false;
    
    public CinemaAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C> nowShowingList, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Direction listener, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.SetPreference preference, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Location location, boolean isLogin) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.ViewHolder holder, @android.annotation.SuppressLint(value = {"RecyclerView"})
    int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/net/pvr1/databinding/CinemaItemBinding;", "(Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter;Lcom/net/pvr1/databinding/CinemaItemBinding;)V", "getBinding", "()Lcom/net/pvr1/databinding/CinemaItemBinding;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.net.pvr1.databinding.CinemaItemBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.databinding.CinemaItemBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.net.pvr1.databinding.CinemaItemBinding getBinding() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Direction;", "", "onDirectionClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output$C;", "app_debug"})
    public static abstract interface Direction {
        
        public abstract void onDirectionClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C comingSoonItem);
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Location;", "", "onLocationClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output$C;", "app_debug"})
    public static abstract interface Location {
        
        public abstract void onLocationClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C comingSoonItem);
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$SetPreference;", "", "onPreferenceClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output$C;", "rowIndex", "", "app_debug"})
    public static abstract interface SetPreference {
        
        public abstract void onPreferenceClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C comingSoonItem, boolean rowIndex);
    }
}