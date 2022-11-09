package com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.net.pvr1.databinding.RetrievalItemBinding;
import com.net.pvr1.ui.bookingSession.response.BookingResponse;
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\u0016\u0017B-\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0016\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0006j\b\u0012\u0004\u0012\u00020\u0007`\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\rH\u0016J\u001c\u0010\u000e\u001a\u00020\u000f2\n\u0010\u0010\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0011\u001a\u00020\rH\u0016J\u001c\u0010\u0012\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\rH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0006j\b\u0012\u0004\u0012\u00020\u0007`\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalAdapter$ViewHolder;", "context", "Landroid/content/Context;", "nowShowingList", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/response/BookingRetrievalResponse$Output$C;", "Lkotlin/collections/ArrayList;", "listener", "Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalAdapter$RecycleViewItemClickListener;", "(Landroid/content/Context;Ljava/util/ArrayList;Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalAdapter$RecycleViewItemClickListener;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "RecycleViewItemClickListener", "ViewHolder", "app_debug"})
public final class BookingRetrievalAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalAdapter.ViewHolder> {
    private android.content.Context context;
    private java.util.ArrayList<com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse.Output.C> nowShowingList;
    private com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalAdapter.RecycleViewItemClickListener listener;
    
    public BookingRetrievalAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse.Output.C> nowShowingList, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalAdapter.RecycleViewItemClickListener listener) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/net/pvr1/databinding/RetrievalItemBinding;", "(Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalAdapter;Lcom/net/pvr1/databinding/RetrievalItemBinding;)V", "getBinding", "()Lcom/net/pvr1/databinding/RetrievalItemBinding;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.net.pvr1.databinding.RetrievalItemBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.databinding.RetrievalItemBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.net.pvr1.databinding.RetrievalItemBinding getBinding() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalAdapter$RecycleViewItemClickListener;", "", "dateClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/bookingSession/response/BookingResponse$Output$Dy;", "app_debug"})
    public static abstract interface RecycleViewItemClickListener {
        
        public abstract void dateClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.bookingSession.response.BookingResponse.Output.Dy comingSoonItem);
    }
}