package com.net.pvr1.ui.home.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u0015\u0016B#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0016J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\fH\u0016J\u0018\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeCinemaCategoryAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeCinemaCategoryAdapter$MyViewHolderNowShowing;", "context", "Landroid/content/Context;", "nowShowingList", "", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Mfi;", "listener", "Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeCinemaCategoryAdapter$RecycleViewItemClickListener;", "(Landroid/content/Context;Ljava/util/List;Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeCinemaCategoryAdapter$RecycleViewItemClickListener;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "MyViewHolderNowShowing", "RecycleViewItemClickListener", "app_debug"})
public final class HomeCinemaCategoryAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.home.fragment.home.adapter.HomeCinemaCategoryAdapter.MyViewHolderNowShowing> {
    private android.content.Context context;
    private java.util.List<com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mfi> nowShowingList;
    private com.net.pvr1.ui.home.fragment.home.adapter.HomeCinemaCategoryAdapter.RecycleViewItemClickListener listener;
    
    public HomeCinemaCategoryAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mfi> nowShowingList, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.home.adapter.HomeCinemaCategoryAdapter.RecycleViewItemClickListener listener) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.home.fragment.home.adapter.HomeCinemaCategoryAdapter.MyViewHolderNowShowing onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.home.adapter.HomeCinemaCategoryAdapter.MyViewHolderNowShowing holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016\u00a8\u0006\u0017"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeCinemaCategoryAdapter$MyViewHolderNowShowing;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "btText", "Landroidx/constraintlayout/widget/ConstraintLayout;", "getBtText", "()Landroidx/constraintlayout/widget/ConstraintLayout;", "setBtText", "(Landroidx/constraintlayout/widget/ConstraintLayout;)V", "image", "Landroid/widget/ImageView;", "getImage", "()Landroid/widget/ImageView;", "setImage", "(Landroid/widget/ImageView;)V", "textView", "Landroid/widget/TextView;", "getTextView", "()Landroid/widget/TextView;", "setTextView", "(Landroid/widget/TextView;)V", "app_debug"})
    public static final class MyViewHolderNowShowing extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private android.widget.ImageView image;
        @org.jetbrains.annotations.NotNull()
        private androidx.constraintlayout.widget.ConstraintLayout btText;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView textView;
        
        public MyViewHolderNowShowing(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getImage() {
            return null;
        }
        
        public final void setImage(@org.jetbrains.annotations.NotNull()
        android.widget.ImageView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.constraintlayout.widget.ConstraintLayout getBtText() {
            return null;
        }
        
        public final void setBtText(@org.jetbrains.annotations.NotNull()
        androidx.constraintlayout.widget.ConstraintLayout p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTextView() {
            return null;
        }
        
        public final void setTextView(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeCinemaCategoryAdapter$RecycleViewItemClickListener;", "", "onCategoryClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Mfi;", "app_debug"})
    public static abstract interface RecycleViewItemClickListener {
        
        public abstract void onCategoryClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mfi comingSoonItem);
    }
}