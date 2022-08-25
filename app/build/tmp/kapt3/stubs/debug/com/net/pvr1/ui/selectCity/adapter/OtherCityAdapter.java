package com.net.pvr1.ui.selectCity.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.net.pvr1.R;
import com.net.pvr1.ui.selectCity.response.SelectCityResponse;
import kotlin.collections.ArrayList;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u001a\u001bB-\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0011H\u0016J\u0018\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0011H\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/net/pvr1/ui/selectCity/adapter/OtherCityAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/net/pvr1/ui/selectCity/adapter/OtherCityAdapter$MyViewHolderNowShowing;", "selectCityList", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output$Ot;", "Lkotlin/collections/ArrayList;", "context", "Landroid/content/Context;", "listner", "Lcom/net/pvr1/ui/selectCity/adapter/OtherCityAdapter$RecycleViewItemClickListenerCity;", "(Ljava/util/ArrayList;Landroid/content/Context;Lcom/net/pvr1/ui/selectCity/adapter/OtherCityAdapter$RecycleViewItemClickListenerCity;)V", "getListner", "()Lcom/net/pvr1/ui/selectCity/adapter/OtherCityAdapter$RecycleViewItemClickListenerCity;", "setListner", "(Lcom/net/pvr1/ui/selectCity/adapter/OtherCityAdapter$RecycleViewItemClickListenerCity;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "MyViewHolderNowShowing", "RecycleViewItemClickListenerCity", "app_debug"})
public final class OtherCityAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter.MyViewHolderNowShowing> {
    private java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> selectCityList;
    private android.content.Context context;
    @org.jetbrains.annotations.NotNull()
    private com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter.RecycleViewItemClickListenerCity listner;
    
    public OtherCityAdapter(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> selectCityList, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter.RecycleViewItemClickListenerCity listner) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter.RecycleViewItemClickListenerCity getListner() {
        return null;
    }
    
    public final void setListner(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter.RecycleViewItemClickListenerCity p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter.MyViewHolderNowShowing onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter.MyViewHolderNowShowing holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/net/pvr1/ui/selectCity/adapter/OtherCityAdapter$MyViewHolderNowShowing;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "otherCityName", "Landroid/widget/TextView;", "getOtherCityName", "()Landroid/widget/TextView;", "setOtherCityName", "(Landroid/widget/TextView;)V", "app_debug"})
    public static final class MyViewHolderNowShowing extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView otherCityName;
        
        public MyViewHolderNowShowing(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getOtherCityName() {
            return null;
        }
        
        public final void setOtherCityName(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0016\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00020\u00060\u0005j\b\u0012\u0004\u0012\u00020\u0006`\u00072\u0006\u0010\b\u001a\u00020\tH&\u00a8\u0006\n"}, d2 = {"Lcom/net/pvr1/ui/selectCity/adapter/OtherCityAdapter$RecycleViewItemClickListenerCity;", "", "onItemClickCityOtherCity", "", "city", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output$Ot;", "Lkotlin/collections/ArrayList;", "position", "", "app_debug"})
    public static abstract interface RecycleViewItemClickListenerCity {
        
        public abstract void onItemClickCityOtherCity(@org.jetbrains.annotations.NotNull()
        java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> city, int position);
    }
}