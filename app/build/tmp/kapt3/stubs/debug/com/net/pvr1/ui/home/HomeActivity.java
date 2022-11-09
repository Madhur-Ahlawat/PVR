package com.net.pvr1.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityHomeBinding;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.home.adapter.HomeOfferAdapter;
import com.net.pvr1.ui.home.fragment.cinema.CinemasFragment;
import com.net.pvr1.ui.home.fragment.commingSoon.ComingSoonFragment;
import com.net.pvr1.ui.home.fragment.home.HomeFragment;
import com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel;
import com.net.pvr1.ui.home.fragment.more.MoreFragment;
import com.net.pvr1.ui.home.fragment.privilege.PrivilegeFragment;
import com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeAdapter;
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse;
import com.net.pvr1.ui.offer.response.OfferResponse;
import com.net.pvr1.ui.search.searchHome.SearchHomeActivity;
import com.net.pvr1.ui.selectCity.SelectCityActivity;
import com.net.pvr1.utils.*;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0019\u001a\u00020\u001aH\u0002J\u0010\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u0011H\u0016J\b\u0010\u001d\u001a\u00020\u001aH\u0002J\b\u0010\u001e\u001a\u00020\u001aH\u0016J\u0012\u0010\u001f\u001a\u00020\u001a2\b\u0010 \u001a\u0004\u0018\u00010!H\u0014J\b\u0010\"\u001a\u00020\u001aH\u0002J\b\u0010#\u001a\u00020\u001aH\u0002J\u0010\u0010$\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020%H\u0016J\u0010\u0010&\u001a\u00020\u001a2\u0006\u0010\'\u001a\u00020(H\u0002J \u0010)\u001a\u00020\u001a2\u0016\u0010\'\u001a\u0012\u0012\u0004\u0012\u00020\u00110\u0010j\b\u0012\u0004\u0012\u00020\u0011`\u0012H\u0002J\u0010\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-H\u0002J\b\u0010.\u001a\u00020\u001aH\u0002J\b\u0010/\u001a\u00020\u001aH\u0002R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u000f\u001a\u0016\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u0010j\n\u0012\u0004\u0012\u00020\u0011\u0018\u0001`\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0013\u001a\u00020\u00148\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018\u00a8\u00060"}, d2 = {"Lcom/net/pvr1/ui/home/HomeActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/home/adapter/HomeOfferAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/home/fragment/privilege/adapter/PrivilegeHomeAdapter$RecycleViewItemClickListener;", "()V", "authViewModel", "Lcom/net/pvr1/ui/home/fragment/home/viewModel/HomeViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/home/fragment/home/viewModel/HomeViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityHomeBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "offerResponse", "Ljava/util/ArrayList;", "Lcom/net/pvr1/ui/offer/response/OfferResponse$Output;", "Lkotlin/collections/ArrayList;", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "movedNext", "", "offerClick", "comingSoonItem", "offerDataLoad", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "privilegeDataLoad", "privilegeDialog", "privilegeHomeClick", "Lcom/net/pvr1/ui/home/fragment/privilege/response/PrivilegeHomeResponse$Output$Pinfo;", "privilegeRetrieveData", "output", "Lcom/net/pvr1/ui/home/fragment/privilege/response/PrivilegeHomeResponse$Output;", "retrieveData", "setCurrentFragment", "Landroidx/fragment/app/FragmentTransaction;", "fragment", "Landroidx/fragment/app/Fragment;", "showOfferDialog", "switchFragment", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class HomeActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.home.adapter.HomeOfferAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeAdapter.RecycleViewItemClickListener {
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    private com.net.pvr1.databinding.ActivityHomeBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private java.util.ArrayList<com.net.pvr1.ui.offer.response.OfferResponse.Output> offerResponse;
    
    public HomeActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.utils.PreferenceManager getPreferences() {
        return null;
    }
    
    public final void setPreferences(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.utils.PreferenceManager p0) {
    }
    
    private final com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movedNext() {
    }
    
    private final void switchFragment() {
    }
    
    private final void showOfferDialog() {
    }
    
    private final void privilegeDialog() {
    }
    
    private final androidx.fragment.app.FragmentTransaction setCurrentFragment(androidx.fragment.app.Fragment fragment) {
        return null;
    }
    
    private final void offerDataLoad() {
    }
    
    private final void privilegeDataLoad() {
    }
    
    private final void privilegeRetrieveData(com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse.Output output) {
    }
    
    private final void retrieveData(java.util.ArrayList<com.net.pvr1.ui.offer.response.OfferResponse.Output> output) {
    }
    
    @java.lang.Override()
    public void offerClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.offer.response.OfferResponse.Output comingSoonItem) {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    @java.lang.Override()
    public void privilegeHomeClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse.Output.Pinfo comingSoonItem) {
    }
}