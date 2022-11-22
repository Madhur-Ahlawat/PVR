package com.net.pvr1.ui.cinemaSession.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ItemCinemaDetailsCinemasBinding;
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse;

@kotlin.Suppress(names = {"DEPRECATION"})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\u0018\u0019B#\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000f\u001a\u00020\u000eH\u0016J\u001c\u0010\u0010\u001a\u00020\u00112\n\u0010\u0012\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u000eH\u0016J\u001c\u0010\u0014\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000eH\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionNearTheaterAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionNearTheaterAdapter$ViewHolder;", "nowShowingList", "", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaNearTheaterResponse$Output$C;", "context", "Landroid/app/Activity;", "listener", "Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionNearTheaterAdapter$RecycleViewItemClickListenerCity;", "(Ljava/util/List;Landroid/app/Activity;Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionNearTheaterAdapter$RecycleViewItemClickListenerCity;)V", "displayMetrics", "Landroid/util/DisplayMetrics;", "screenWidth", "", "getItemCount", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "RecycleViewItemClickListenerCity", "ViewHolder", "app_debug"})
public final class CinemaSessionNearTheaterAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionNearTheaterAdapter.ViewHolder> {
    private java.util.List<com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse.Output.C> nowShowingList;
    private android.app.Activity context;
    private com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionNearTheaterAdapter.RecycleViewItemClickListenerCity listener;
    private final android.util.DisplayMetrics displayMetrics = null;
    private int screenWidth = 0;
    
    public CinemaSessionNearTheaterAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse.Output.C> nowShowingList, @org.jetbrains.annotations.NotNull()
    android.app.Activity context, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionNearTheaterAdapter.RecycleViewItemClickListenerCity listener) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionNearTheaterAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionNearTheaterAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionNearTheaterAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/net/pvr1/databinding/ItemCinemaDetailsCinemasBinding;", "(Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionNearTheaterAdapter;Lcom/net/pvr1/databinding/ItemCinemaDetailsCinemasBinding;)V", "getBinding", "()Lcom/net/pvr1/databinding/ItemCinemaDetailsCinemasBinding;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.net.pvr1.databinding.ItemCinemaDetailsCinemasBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.databinding.ItemCinemaDetailsCinemasBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.net.pvr1.databinding.ItemCinemaDetailsCinemasBinding getBinding() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0007"}, d2 = {"Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionNearTheaterAdapter$RecycleViewItemClickListenerCity;", "", "nearTheaterClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaNearTheaterResponse$Output$C;", "nearTheaterDirectionClick", "app_debug"})
    public static abstract interface RecycleViewItemClickListenerCity {
        
        public abstract void nearTheaterClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse.Output.C comingSoonItem);
        
        public abstract void nearTheaterDirectionClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse.Output.C comingSoonItem);
    }
}