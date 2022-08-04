package com.net.pvr1.ui.selectCity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivitySelectCityBinding;
import com.net.pvr1.di.preference.AppPreferences;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter;
import com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter;
import com.net.pvr1.ui.selectCity.response.SelectCityResponse;
import com.net.pvr1.ui.selectCity.viewModel.SelectCityViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.NetworkResult;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\u0010\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0018\u001a\u00020\u0010H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0019"}, d2 = {"Lcom/net/pvr1/ui/selectCity/SelectCityActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/net/pvr1/databinding/ActivitySelectCityBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "preferences", "Lcom/net/pvr1/di/preference/AppPreferences;", "selectCityViewModel", "Lcom/net/pvr1/ui/selectCity/viewModel/SelectCityViewModel;", "getSelectCityViewModel", "()Lcom/net/pvr1/ui/selectCity/viewModel/SelectCityViewModel;", "selectCityViewModel$delegate", "Lkotlin/Lazy;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "retrieveData", "output", "Lcom/net/pvr1/ui/selectCity/response/SelectCityResponse$Output;", "retrieveOtherCityData", "retrieveSearchData", "selectCity", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class SelectCityActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.net.pvr1.di.preference.AppPreferences preferences;
    private com.net.pvr1.databinding.ActivitySelectCityBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy selectCityViewModel$delegate = null;
    
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
    
    private final void selectCity() {
    }
    
    private final void retrieveSearchData(com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output output) {
    }
    
    private final void retrieveOtherCityData(com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output output) {
    }
    
    private final void retrieveData(com.net.pvr1.ui.selectCity.response.SelectCityResponse.Output output) {
    }
}