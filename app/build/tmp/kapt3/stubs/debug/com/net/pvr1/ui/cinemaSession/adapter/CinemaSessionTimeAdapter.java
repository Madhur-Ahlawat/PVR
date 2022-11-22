package com.net.pvr1.ui.cinemaSession.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;
import com.net.pvr1.databinding.ItemCinemaDetailsShowTimeBinding;
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse;
import com.net.pvr1.ui.seatLayout.SeatLayoutActivity;
import com.net.pvr1.utils.Constant;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0014B%\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010\n\u001a\u00020\u000bH\u0016J\u001c\u0010\f\u001a\u00020\r2\n\u0010\u000e\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u000bH\u0016J\u001c\u0010\u0010\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000bH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionTimeAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionTimeAdapter$ViewHolder;", "nowShowingList", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaSessionResponse$Child$Mv$Ml$S;", "Lkotlin/collections/ArrayList;", "context", "Landroid/content/Context;", "(Ljava/util/ArrayList;Landroid/content/Context;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ViewHolder", "app_debug"})
public final class CinemaSessionTimeAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionTimeAdapter.ViewHolder> {
    private java.util.ArrayList<com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse.Child.Mv.Ml.S> nowShowingList;
    private android.content.Context context;
    
    public CinemaSessionTimeAdapter(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse.Child.Mv.Ml.S> nowShowingList, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionTimeAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionTimeAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionTimeAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/net/pvr1/databinding/ItemCinemaDetailsShowTimeBinding;", "(Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionTimeAdapter;Lcom/net/pvr1/databinding/ItemCinemaDetailsShowTimeBinding;)V", "getBinding", "()Lcom/net/pvr1/databinding/ItemCinemaDetailsShowTimeBinding;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.net.pvr1.databinding.ItemCinemaDetailsShowTimeBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.databinding.ItemCinemaDetailsShowTimeBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.net.pvr1.databinding.ItemCinemaDetailsShowTimeBinding getBinding() {
            return null;
        }
    }
}