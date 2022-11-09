package com.net.pvr1.ui.selectCity.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.ui.selectCity.response.SelectCityResponse;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u001f B-\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\b\u0010\u0016\u001a\u00020\rH\u0016J\u001a\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00022\b\b\u0001\u0010\u001a\u001a\u00020\rH\u0016J\u0018\u0010\u001b\u001a\u00020\u00022\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\rH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/net/pvr1/ui/selectCity/adapter/SelectCityAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/selectCity/adapter/SelectCityAdapter$MyViewHolderNowShowing;", "selectCityList", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output$Pc;", "Lkotlin/collections/ArrayList;", "context", "Landroid/app/Activity;", "listner", "Lcom/net/pvr1/ui/selectCity/adapter/SelectCityAdapter$RecycleViewItemClickListenerSelectCity;", "(Ljava/util/ArrayList;Landroid/app/Activity;Lcom/net/pvr1/ui/selectCity/adapter/SelectCityAdapter$RecycleViewItemClickListenerSelectCity;)V", "height", "", "getListner", "()Lcom/net/pvr1/ui/selectCity/adapter/SelectCityAdapter$RecycleViewItemClickListenerSelectCity;", "setListner", "(Lcom/net/pvr1/ui/selectCity/adapter/SelectCityAdapter$RecycleViewItemClickListenerSelectCity;)V", "rowIndex", "setHeight", "setWidth", "width", "getItemCount", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "MyViewHolderNowShowing", "RecycleViewItemClickListenerSelectCity", "app_debug"})
public final class SelectCityAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter.MyViewHolderNowShowing> {
    private java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Pc> selectCityList;
    private android.app.Activity context;
    @org.jetbrains.annotations.NotNull()
    private com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter.RecycleViewItemClickListenerSelectCity listner;
    private int rowIndex = 0;
    private int width = 0;
    private int height = 0;
    private int setWidth = 0;
    private int setHeight = 0;
    
    public SelectCityAdapter(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Pc> selectCityList, @org.jetbrains.annotations.NotNull()
    android.app.Activity context, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter.RecycleViewItemClickListenerSelectCity listner) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter.RecycleViewItemClickListenerSelectCity getListner() {
        return null;
    }
    
    public final void setListner(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter.RecycleViewItemClickListenerSelectCity p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter.MyViewHolderNowShowing onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter.MyViewHolderNowShowing holder, @android.annotation.SuppressLint(value = {"RecyclerView"})
    int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000e\"\u0004\b\u0017\u0010\u0010\u00a8\u0006\u0018"}, d2 = {"Lcom/net/pvr1/ui/selectCity/adapter/SelectCityAdapter$MyViewHolderNowShowing;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "cityName", "Landroid/widget/TextView;", "getCityName", "()Landroid/widget/TextView;", "setCityName", "(Landroid/widget/TextView;)V", "clickOhk", "Landroid/widget/ImageView;", "getClickOhk", "()Landroid/widget/ImageView;", "setClickOhk", "(Landroid/widget/ImageView;)V", "constraintLayout", "Landroidx/constraintlayout/widget/ConstraintLayout;", "getConstraintLayout", "()Landroidx/constraintlayout/widget/ConstraintLayout;", "imageSelectCity", "getImageSelectCity", "setImageSelectCity", "app_debug"})
    public static final class MyViewHolderNowShowing extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView cityName;
        @org.jetbrains.annotations.NotNull()
        private android.widget.ImageView imageSelectCity;
        @org.jetbrains.annotations.NotNull()
        private android.widget.ImageView clickOhk;
        @org.jetbrains.annotations.NotNull()
        private final androidx.constraintlayout.widget.ConstraintLayout constraintLayout = null;
        
        public MyViewHolderNowShowing(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getCityName() {
            return null;
        }
        
        public final void setCityName(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getImageSelectCity() {
            return null;
        }
        
        public final void setImageSelectCity(@org.jetbrains.annotations.NotNull()
        android.widget.ImageView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getClickOhk() {
            return null;
        }
        
        public final void setClickOhk(@org.jetbrains.annotations.NotNull()
        android.widget.ImageView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.constraintlayout.widget.ConstraintLayout getConstraintLayout() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0016\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00020\u00060\u0005j\b\u0012\u0004\u0012\u00020\u0006`\u00072\u0006\u0010\b\u001a\u00020\tH&\u00a8\u0006\n"}, d2 = {"Lcom/net/pvr1/ui/selectCity/adapter/SelectCityAdapter$RecycleViewItemClickListenerSelectCity;", "", "onItemClickCityImgCity", "", "city", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output$Pc;", "Lkotlin/collections/ArrayList;", "position", "", "app_debug"})
    public static abstract interface RecycleViewItemClickListenerSelectCity {
        
        public abstract void onItemClickCityImgCity(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Pc> city, int position);
    }
}