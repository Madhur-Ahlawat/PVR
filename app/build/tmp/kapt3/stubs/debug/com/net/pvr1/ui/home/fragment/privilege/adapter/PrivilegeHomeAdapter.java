package com.net.pvr1.ui.home.fragment.privilege.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.databinding.PrivilegeHomeItemBinding;
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\u0016\u0017B+\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\tH\u0016J\u001c\u0010\u000e\u001a\u00020\u000f2\n\u0010\u0010\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0011\u001a\u00020\tH\u0017J\u001c\u0010\u0012\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\tH\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/privilege/adapter/PrivilegeHomeAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/home/fragment/privilege/adapter/PrivilegeHomeAdapter$ViewHolder;", "nowShowingList", "", "Lcom/net/pvr1/ui/home/fragment/privilege/response/PrivilegeHomeResponse$Output$Pinfo;", "context", "Landroid/content/Context;", "type", "", "listener", "Lcom/net/pvr1/ui/home/fragment/privilege/adapter/PrivilegeHomeAdapter$RecycleViewItemClickListener;", "(Ljava/util/List;Landroid/content/Context;ILcom/net/pvr1/ui/home/fragment/privilege/adapter/PrivilegeHomeAdapter$RecycleViewItemClickListener;)V", "getItemCount", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "RecycleViewItemClickListener", "ViewHolder", "app_debug"})
public final class PrivilegeHomeAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeAdapter.ViewHolder> {
    private java.util.List<com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse.Output.Pinfo> nowShowingList;
    private android.content.Context context;
    private int type;
    private com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeAdapter.RecycleViewItemClickListener listener;
    
    public PrivilegeHomeAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse.Output.Pinfo> nowShowingList, @org.jetbrains.annotations.NotNull()
    android.content.Context context, int type, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeAdapter.RecycleViewItemClickListener listener) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/privilege/adapter/PrivilegeHomeAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/net/pvr1/databinding/PrivilegeHomeItemBinding;", "(Lcom/net/pvr1/ui/home/fragment/privilege/adapter/PrivilegeHomeAdapter;Lcom/net/pvr1/databinding/PrivilegeHomeItemBinding;)V", "getBinding", "()Lcom/net/pvr1/databinding/PrivilegeHomeItemBinding;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.net.pvr1.databinding.PrivilegeHomeItemBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.databinding.PrivilegeHomeItemBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.net.pvr1.databinding.PrivilegeHomeItemBinding getBinding() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/privilege/adapter/PrivilegeHomeAdapter$RecycleViewItemClickListener;", "", "privilegeHomeClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/privilege/response/PrivilegeHomeResponse$Output$Pinfo;", "app_debug"})
    public static abstract interface RecycleViewItemClickListener {
        
        public abstract void privilegeHomeClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse.Output.Pinfo comingSoonItem);
    }
}