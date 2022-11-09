package com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.net.pvr1.databinding.RetrivalItemChildBinding;
import com.net.pvr1.ui.bookingSession.response.BookingResponse;
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\u0013\u0014B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\nH\u0016J\u001c\u0010\u000b\u001a\u00020\f2\n\u0010\r\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u000e\u001a\u00020\nH\u0016J\u001c\u0010\u000f\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\nH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalChildAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalChildAdapter$ViewHolder;", "context", "Landroid/content/Context;", "nowShowingList", "", "Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/response/BookingRetrievalResponse$Output$Child;", "(Landroid/content/Context;Ljava/util/List;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "RecycleViewItemClickListener", "ViewHolder", "app_debug"})
public final class BookingRetrievalChildAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalChildAdapter.ViewHolder> {
    private android.content.Context context;
    private java.util.List<com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse.Output.Child> nowShowingList;
    
    public BookingRetrievalChildAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse.Output.Child> nowShowingList) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalChildAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalChildAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalChildAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/net/pvr1/databinding/RetrivalItemChildBinding;", "(Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalChildAdapter;Lcom/net/pvr1/databinding/RetrivalItemChildBinding;)V", "getBinding", "()Lcom/net/pvr1/databinding/RetrivalItemChildBinding;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.net.pvr1.databinding.RetrivalItemChildBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.databinding.RetrivalItemChildBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.net.pvr1.databinding.RetrivalItemChildBinding getBinding() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/more/bookingRetrieval/adapter/BookingRetrievalChildAdapter$RecycleViewItemClickListener;", "", "dateClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/bookingSession/response/BookingResponse$Output$Dy;", "app_debug"})
    public static abstract interface RecycleViewItemClickListener {
        
        public abstract void dateClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.bookingSession.response.BookingResponse.Output.Dy comingSoonItem);
    }
}