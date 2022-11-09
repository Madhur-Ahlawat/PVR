package com.net.pvr1.utils;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import java.lang.Exception;
import java.util.*;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J@\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u000eH\u0002J\u0012\u0010\u0014\u001a\u00020\n2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0015R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\u0017"}, d2 = {"Lcom/net/pvr1/utils/FetchAddressIntentServices;", "Landroid/app/IntentService;", "()V", "resultReceiver", "Landroid/os/ResultReceiver;", "getResultReceiver", "()Landroid/os/ResultReceiver;", "setResultReceiver", "(Landroid/os/ResultReceiver;)V", "devliverResultToRecevier", "", "resultcode", "", "address", "", "locality", "district", "state", "country", "postcode", "onHandleIntent", "intent", "Landroid/content/Intent;", "app_debug"})
public final class FetchAddressIntentServices extends android.app.IntentService {
    @org.jetbrains.annotations.Nullable()
    private android.os.ResultReceiver resultReceiver;
    
    public FetchAddressIntentServices() {
        super(null);
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.os.ResultReceiver getResultReceiver() {
        return null;
    }
    
    public final void setResultReceiver(@org.jetbrains.annotations.Nullable()
    android.os.ResultReceiver p0) {
    }
    
    @java.lang.Override()
    @java.lang.Deprecated()
    protected void onHandleIntent(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
    }
    
    private final void devliverResultToRecevier(int resultcode, java.lang.String address, java.lang.String locality, java.lang.String district, java.lang.String state, java.lang.String country, java.lang.String postcode) {
    }
}