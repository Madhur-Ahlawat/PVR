package com.net.pvr1.ui.enableLocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityEnableLocationBinding;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.enableLocation.viewModel.EnableLocationViewModel;
import com.net.pvr1.utils.Constant;
import com.net.pvr1.utils.FetchAddressIntentServices;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Suppress(names = {"DEPRECATION"})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0001#B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\u0013H\u0002J\b\u0010\u0017\u001a\u00020\u0013H\u0002J\b\u0010\u0018\u001a\u00020\u0013H\u0002J\u0012\u0010\u0019\u001a\u00020\u00132\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J-\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00042\u000e\u0010\u001e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\u001f2\u0006\u0010 \u001a\u00020!H\u0016\u00a2\u0006\u0002\u0010\"R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/net/pvr1/ui/enableLocation/EnableLocationActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "PERMISSION_REQUEST_CODE", "", "authViewModel", "Lcom/net/pvr1/ui/enableLocation/viewModel/EnableLocationViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/enableLocation/viewModel/EnableLocationViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityEnableLocationBinding;", "latitude", "", "longitude", "resultReceiver", "Landroid/os/ResultReceiver;", "fetchAddressFromLocation", "", "location", "Landroid/location/Location;", "getLatLong", "loadLocation", "movedNext", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onRequestPermissionsResult", "requestCode", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "AddressResultReceiver", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class EnableLocationActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.net.pvr1.databinding.ActivityEnableLocationBinding binding;
    private final kotlin.Lazy authViewModel$delegate = null;
    private final int PERMISSION_REQUEST_CODE = 1;
    private android.os.ResultReceiver resultReceiver;
    private java.lang.String latitude = "";
    private java.lang.String longitude = "";
    
    public EnableLocationActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.enableLocation.viewModel.EnableLocationViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movedNext() {
    }
    
    private final void loadLocation() {
    }
    
    private final void getLatLong() {
    }
    
    private final void fetchAddressFromLocation(android.location.Location location) {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0014\u00a8\u0006\u000b"}, d2 = {"Lcom/net/pvr1/ui/enableLocation/EnableLocationActivity$AddressResultReceiver;", "Landroid/os/ResultReceiver;", "handler", "Landroid/os/Handler;", "(Landroid/os/Handler;)V", "onReceiveResult", "", "resultCode", "", "resultData", "Landroid/os/Bundle;", "app_debug"})
    static final class AddressResultReceiver extends android.os.ResultReceiver {
        
        public AddressResultReceiver(@org.jetbrains.annotations.Nullable()
        android.os.Handler handler) {
            super(null);
        }
        
        @java.lang.Override()
        protected void onReceiveResult(int resultCode, @org.jetbrains.annotations.NotNull()
        android.os.Bundle resultData) {
        }
    }
}