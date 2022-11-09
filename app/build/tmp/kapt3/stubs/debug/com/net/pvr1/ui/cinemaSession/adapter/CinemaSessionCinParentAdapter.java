package com.net.pvr1.ui.cinemaSession.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ItemCinemaDetailsMoviesBinding;
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\u0016\u0017B#\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\r\u001a\u00020\fH\u0016J\u001e\u0010\u000e\u001a\u00020\u000f2\n\u0010\u0010\u001a\u00060\u0002R\u00020\u00002\b\b\u0001\u0010\u0011\u001a\u00020\fH\u0016J\u001c\u0010\u0012\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\fH\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionCinParentAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionCinParentAdapter$ViewHolder;", "nowShowingList", "", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaSessionResponse$Child;", "context", "Landroid/content/Context;", "listener", "Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionCinParentAdapter$RecycleViewItemClickListenerCity;", "(Ljava/util/List;Landroid/content/Context;Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionCinParentAdapter$RecycleViewItemClickListenerCity;)V", "rowIndex", "", "getItemCount", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "RecycleViewItemClickListenerCity", "ViewHolder", "app_debug"})
public final class CinemaSessionCinParentAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionCinParentAdapter.ViewHolder> {
    private java.util.List<com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse.Child> nowShowingList;
    private android.content.Context context;
    private com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionCinParentAdapter.RecycleViewItemClickListenerCity listener;
    private int rowIndex = 0;
    
    public CinemaSessionCinParentAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse.Child> nowShowingList, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionCinParentAdapter.RecycleViewItemClickListenerCity listener) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionCinParentAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionCinParentAdapter.ViewHolder holder, @android.annotation.SuppressLint(value = {"RecyclerView"})
    int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionCinParentAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/net/pvr1/databinding/ItemCinemaDetailsMoviesBinding;", "(Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionCinParentAdapter;Lcom/net/pvr1/databinding/ItemCinemaDetailsMoviesBinding;)V", "getBinding", "()Lcom/net/pvr1/databinding/ItemCinemaDetailsMoviesBinding;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.net.pvr1.databinding.ItemCinemaDetailsMoviesBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.databinding.ItemCinemaDetailsMoviesBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.net.pvr1.databinding.ItemCinemaDetailsMoviesBinding getBinding() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/net/pvr1/ui/cinemaSession/adapter/CinemaSessionCinParentAdapter$RecycleViewItemClickListenerCity;", "", "cinemaClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/cinemaSession/response/CinemaSessionResponse$Child;", "app_debug"})
    public static abstract interface RecycleViewItemClickListenerCity {
        
        public abstract void cinemaClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse.Child comingSoonItem);
    }
}