package com.net.pvr1.ui.selectCity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivitySelectCityBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter;
import com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter;
import com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter;
import com.net.pvr1.ui.selectCity.response.SelectCityResponse;
import com.net.pvr1.ui.selectCity.viewModel.SelectCityViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.AndroidEntryPoint;
import kotlin.collections.ArrayList;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004B\u0005\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u001a\u001a\u00020\u001bH\u0002J\u0012\u0010\u001c\u001a\u00020\u001b2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0014J(\u0010\u001f\u001a\u00020\u001b2\u0016\u0010 \u001a\u0012\u0012\u0004\u0012\u00020!0\tj\b\u0012\u0004\u0012\u00020!`\u00112\u0006\u0010\"\u001a\u00020#H\u0016J(\u0010$\u001a\u00020\u001b2\u0016\u0010 \u001a\u0012\u0012\u0004\u0012\u00020\u00100\tj\b\u0012\u0004\u0012\u00020\u0010`\u00112\u0006\u0010\"\u001a\u00020#H\u0016J(\u0010%\u001a\u00020\u001b2\u0016\u0010 \u001a\u0012\u0012\u0004\u0012\u00020\u00100\tj\b\u0012\u0004\u0012\u00020\u0010`\u00112\u0006\u0010\"\u001a\u00020#H\u0016J\u0010\u0010&\u001a\u00020\u001b2\u0006\u0010\'\u001a\u00020(H\u0002J\b\u0010)\u001a\u00020\u001bH\u0002R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u0012\u0012\u0004\u0012\u00020\u00100\tj\b\u0012\u0004\u0012\u00020\u0010`\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0014\u001a\u00020\u00158BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006*"}, d2 = {"Lcom/net/pvr1/ui/selectCity/SelectCityActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/selectCity/adapter/OtherCityAdapter$RecycleViewItemClickListenerCity;", "Lcom/net/pvr1/ui/selectCity/adapter/SelectCityAdapter$RecycleViewItemClickListenerSelectCity;", "()V", "binding", "Lcom/net/pvr1/databinding/ActivitySelectCityBinding;", "dataList", "Ljava/util/ArrayList;", "", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "otherCityAdapter", "Lcom/net/pvr1/ui/selectCity/adapter/SearchCityAdapter;", "otherCityList", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output$Ot;", "Lkotlin/collections/ArrayList;", "preferences", "Lcom/net/pvr1/di/preference/AppPreferences;", "selectCityViewModel", "Lcom/net/pvr1/ui/selectCity/viewModel/SelectCityViewModel;", "getSelectCityViewModel", "()Lcom/net/pvr1/ui/selectCity/viewModel/SelectCityViewModel;", "selectCityViewModel$delegate", "Lkotlin/Lazy;", "movedNext", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onItemClickCityImgCity", "city", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output$Pc;", "position", "", "onItemClickCityOtherCity", "onItemClickCitySearch", "retrieveData", "output", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output;", "selectCity", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class SelectCityActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter.RecycleViewItemClickListenerCity, com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter.RecycleViewItemClickListenerSelectCity {
    private com.net.pvr1.di.preference.AppPreferences preferences;
    private com.net.pvr1.databinding.ActivitySelectCityBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy selectCityViewModel$delegate = null;
    private java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> otherCityList;
    private com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter otherCityAdapter;
    private java.util.ArrayList<java.lang.Object> dataList;
    
    public SelectCityActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.selectCity.viewModel.SelectCityViewModel getSelectCityViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movedNext() {
    }
    
    private final void selectCity() {
    }
    
    @java.lang.Override()
    public void onItemClickCitySearch(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> city, int position) {
    }
    
    private final void retrieveData(com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output output) {
    }
    
    @java.lang.Override()
    public void onItemClickCityOtherCity(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Ot> city, int position) {
    }
    
    @java.lang.Override()
    public void onItemClickCityImgCity(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output.Pc> city, int position) {
    }
}