package com.net.pvr1.ui.home.fragment.cinema.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\u0017\u0018\u0019B+\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\u000eH\u0016J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u000eH\u0016J\u0018\u0010\u0013\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000eH\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$MyViewHolderNowShowing;", "nowShowingList", "", "Lcom/net/pvr1/ui/home/fragment/cinema/response/CinemaResponse$Output$C;", "context", "Landroid/content/Context;", "listener", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Direction;", "location", "Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Location;", "(Ljava/util/List;Landroid/content/Context;Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Direction;Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$Location;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "Direction", "Location", "MyViewHolderNowShowing", "app_debug"})
public final class CinemaAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.MyViewHolderNowShowing> {
    private java.util.List<com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C> nowShowingList;
    private android.content.Context context;
    private com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Direction listener;
    private com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Location location;
    
    public CinemaAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse.Output.C> nowShowingList, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Direction listener, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.Location location) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.MyViewHolderNowShowing onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.cinema.adapter.CinemaAdapter.MyViewHolderNowShowing holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0011\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u000e\"\u0004\b\u0019\u0010\u0010R\u001a\u0010\u001a\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0014\"\u0004\b\u001c\u0010\u0016R\u001a\u0010\u001d\u001a\u00020\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010#\u001a\u00020\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010 \"\u0004\b%\u0010\"R\u001a\u0010&\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\'\u0010\u0014\"\u0004\b(\u0010\u0016R\u001a\u0010)\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u0014\"\u0004\b+\u0010\u0016R\u001a\u0010,\u001a\u00020-X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101R\u001a\u00102\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u000e\"\u0004\b4\u0010\u0010R\u001a\u00105\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u0014\"\u0004\b7\u0010\u0016R\u001a\u00108\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b9\u0010\u000e\"\u0004\b:\u0010\u0010R\u001a\u0010;\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b<\u0010\u000e\"\u0004\b=\u0010\u0010\u00a8\u0006>"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/cinema/adapter/CinemaAdapter$MyViewHolderNowShowing;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "checkFev", "Landroid/widget/CheckBox;", "getCheckFev", "()Landroid/widget/CheckBox;", "setCheckFev", "(Landroid/widget/CheckBox;)V", "cinemaLocation", "Landroid/widget/TextView;", "getCinemaLocation", "()Landroid/widget/TextView;", "setCinemaLocation", "(Landroid/widget/TextView;)V", "direction", "Landroid/widget/LinearLayout;", "getDirection", "()Landroid/widget/LinearLayout;", "setDirection", "(Landroid/widget/LinearLayout;)V", "distance", "getDistance", "setDistance", "food", "getFood", "setFood", "image", "Landroid/widget/ImageView;", "getImage", "()Landroid/widget/ImageView;", "setImage", "(Landroid/widget/ImageView;)V", "imageLocation", "getImageLocation", "setImageLocation", "kiloMeter", "getKiloMeter", "setKiloMeter", "metro", "getMetro", "setMetro", "movieList", "Landroidx/recyclerview/widget/RecyclerView;", "getMovieList", "()Landroidx/recyclerview/widget/RecyclerView;", "setMovieList", "(Landroidx/recyclerview/widget/RecyclerView;)V", "multipleCinema", "getMultipleCinema", "setMultipleCinema", "parking", "getParking", "setParking", "show", "getShow", "setShow", "title", "getTitle", "setTitle", "app_debug"})
    public static final class MyViewHolderNowShowing extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView title;
        @org.jetbrains.annotations.NotNull()
        private android.widget.ImageView image;
        @org.jetbrains.annotations.NotNull()
        private android.widget.CheckBox checkFev;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView multipleCinema;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView cinemaLocation;
        @org.jetbrains.annotations.NotNull()
        private android.widget.ImageView imageLocation;
        @org.jetbrains.annotations.NotNull()
        private android.widget.LinearLayout parking;
        @org.jetbrains.annotations.NotNull()
        private android.widget.LinearLayout food;
        @org.jetbrains.annotations.NotNull()
        private android.widget.LinearLayout metro;
        @org.jetbrains.annotations.NotNull()
        private android.widget.LinearLayout kiloMeter;
        @org.jetbrains.annotations.NotNull()
        private android.widget.LinearLayout direction;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView show;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView distance;
        @org.jetbrains.annotations.NotNull()
        private androidx.recyclerview.widget.RecyclerView movieList;
        
        public MyViewHolderNowShowing(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTitle() {
            return null;
        }
        
        public final void setTitle(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getImage() {
            return null;
        }
        
        public final void setImage(@org.jetbrains.annotations.NotNull()
        android.widget.ImageView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.CheckBox getCheckFev() {
            return null;
        }
        
        public final void setCheckFev(@org.jetbrains.annotations.NotNull()
        android.widget.CheckBox p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getMultipleCinema() {
            return null;
        }
        
        public final void setMultipleCinema(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getCinemaLocation() {
            return null;
        }
        
        public final void setCinemaLocation(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getImageLocation() {
            return null;
        }
        
        public final void setImageLocation(@org.jetbrains.annotations.NotNull()
        android.widget.ImageView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getParking() {
            return null;
        }
        
        public final void setParking(@org.jetbrains.annotations.NotNull()
        android.widget.LinearLayout p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getFood() {
            return null;
        }
        
        public final void setFood(@org.jetbrains.annotations.NotNull()
        android.widget.LinearLayout p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getMetro() {
            return null;
        }
        
        public final void setMetro(@org.jetbrains.annotations.NotNull()
        android.widget.LinearLayout p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getKiloMeter() {
            return null;
        }
        
        public final void setKiloMeter(@org.jetbrains.annotations.NotNull()
        android.widget.LinearLayout p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getDirection() {
            return null;
        }
        
        public final void setDirection(@org.jetbrains.annotations.NotNull()
        android.widget.LinearLayout p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getShow() {
            return null;
        }
        
        public final void setShow(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getDistance() {
            return null;
        }
        
        public final void setDistance(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.recyclerview.widget.RecyclerView getMovieList() {
            return null;
        }
        
        public final void setMovieList(@org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView p0) {
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
}