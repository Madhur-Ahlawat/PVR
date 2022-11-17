package com.net.pvr1.ui.home.fragment.home.adapter;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse;
import com.net.pvr1.utils.Constant;

@kotlin.Suppress(names = {"DEPRECATION"})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0016B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\u0002\u0010\bJ\b\u0010\r\u001a\u00020\fH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\fH\u0017J\u0018\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/home/adapter/PromotionAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/home/fragment/home/adapter/PromotionAdapter$MovieViewHolder;", "context", "Landroid/app/Activity;", "movies", "", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Ph;", "(Landroid/app/Activity;Ljava/util/List;)V", "displayMetrics", "Landroid/util/DisplayMetrics;", "screenWidth", "", "getItemCount", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "MovieViewHolder", "app_debug"})
public final class PromotionAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.home.fragment.home.adapter.PromotionAdapter.MovieViewHolder> {
    private final android.app.Activity context = null;
    private final java.util.List<com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Ph> movies = null;
    private android.util.DisplayMetrics displayMetrics;
    private int screenWidth = 0;
    
    public PromotionAdapter(@org.jetbrains.annotations.NotNull()
    android.app.Activity context, @org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Ph> movies) {
        super();
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.home.fragment.home.adapter.PromotionAdapter.MovieViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.home.adapter.PromotionAdapter.MovieViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/home/adapter/PromotionAdapter$MovieViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "promotionImage", "Landroid/widget/ImageView;", "getPromotionImage", "()Landroid/widget/ImageView;", "setPromotionImage", "(Landroid/widget/ImageView;)V", "app_debug"})
    public static final class MovieViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private android.widget.ImageView promotionImage;
        
        public MovieViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getPromotionImage() {
            return null;
        }
        
        public final void setPromotionImage(@org.jetbrains.annotations.NotNull()
        android.widget.ImageView p0) {
        }
    }
}