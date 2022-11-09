package com.net.pvr1.ui.home.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u0015\u0016B#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0016J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\fH\u0016J\u0018\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeMoviesAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeMoviesAdapter$MyViewHolderNowShowing;", "context", "Landroid/content/Context;", "nowShowingList", "", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Mv;", "listener", "Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeMoviesAdapter$RecycleViewItemClickListener;", "(Landroid/content/Context;Ljava/util/List;Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeMoviesAdapter$RecycleViewItemClickListener;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "MyViewHolderNowShowing", "RecycleViewItemClickListener", "app_debug"})
public final class HomeMoviesAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.home.fragment.home.adapter.HomeMoviesAdapter.MyViewHolderNowShowing> {
    private android.content.Context context;
    private java.util.List<com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mv> nowShowingList;
    private com.net.pvr1.ui.home.fragment.home.adapter.HomeMoviesAdapter.RecycleViewItemClickListener listener;
    
    public HomeMoviesAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mv> nowShowingList, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.home.adapter.HomeMoviesAdapter.RecycleViewItemClickListener listener) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.home.fragment.home.adapter.HomeMoviesAdapter.MyViewHolderNowShowing onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.home.adapter.HomeMoviesAdapter.MyViewHolderNowShowing holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0014\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000e\"\u0004\b\u0013\u0010\u0010R\u001a\u0010\u0014\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\b\"\u0004\b\u0016\u0010\nR\u001a\u0010\u0017\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\b\"\u0004\b\u0019\u0010\nR\u001a\u0010\u001a\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\b\"\u0004\b\u001c\u0010\nR\u001a\u0010\u001d\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\b\"\u0004\b\u001f\u0010\n\u00a8\u0006 "}, d2 = {"Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeMoviesAdapter$MyViewHolderNowShowing;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "book", "Landroid/widget/TextView;", "getBook", "()Landroid/widget/TextView;", "setBook", "(Landroid/widget/TextView;)V", "dots", "Landroid/widget/ImageView;", "getDots", "()Landroid/widget/ImageView;", "setDots", "(Landroid/widget/ImageView;)V", "image", "getImage", "setImage", "language", "getLanguage", "setLanguage", "rating", "getRating", "setRating", "title", "getTitle", "setTitle", "type", "getType", "setType", "app_debug"})
    public static final class MyViewHolderNowShowing extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private android.widget.ImageView image;
        @org.jetbrains.annotations.NotNull()
        private android.widget.ImageView dots;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView title;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView rating;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView type;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView language;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView book;
        
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
        public final android.widget.ImageView getDots() {
            return null;
        }
        
        public final void setDots(@org.jetbrains.annotations.NotNull()
        android.widget.ImageView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTitle() {
            return null;
        }
        
        public final void setTitle(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getRating() {
            return null;
        }
        
        public final void setRating(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getType() {
            return null;
        }
        
        public final void setType(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getLanguage() {
            return null;
        }
        
        public final void setLanguage(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getBook() {
            return null;
        }
        
        public final void setBook(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0007"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/home/adapter/HomeMoviesAdapter$RecycleViewItemClickListener;", "", "onBookClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Mv;", "onMoviesClick", "app_debug"})
    public static abstract interface RecycleViewItemClickListener {
        
        public abstract void onMoviesClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mv comingSoonItem);
        
        public abstract void onBookClick(@org.jetbrains.annotations.NotNull()
        com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Mv comingSoonItem);
    }
}