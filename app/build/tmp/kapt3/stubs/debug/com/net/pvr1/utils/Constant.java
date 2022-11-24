package com.net.pvr1.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.net.pvr1.R;
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse;
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

@kotlin.Suppress(names = {"DEPRECATION"})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 22\u00020\u0001:\u00012B\u0005\u00a2\u0006\u0002\u0010\u0002J0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0016\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018J\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001a\u001a\u00020\u0004J\u000e\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u001cJ\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001e\u001a\u00020\u0010J\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0004J\u000e\u0010\"\u001a\u00020 2\u0006\u0010#\u001a\u00020\u001cJ&\u0010$\u001a\u00020 2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010%\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0013J\u001e\u0010&\u001a\u00020 2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\'\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0004J\u0010\u0010)\u001a\u0004\u0018\u00010\u00042\u0006\u0010*\u001a\u00020\u0016J\u0010\u0010+\u001a\u0004\u0018\u00010\u00042\u0006\u0010*\u001a\u00020\u0016J&\u0010,\u001a\u00020 2\u0006\u0010-\u001a\u00020\u001c2\u000e\u0010.\u001a\n\u0018\u00010/j\u0004\u0018\u0001`02\u0006\u00101\u001a\u00020\u000eR\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u00063"}, d2 = {"Lcom/net/pvr1/utils/Constant;", "", "()V", "youtubeVideoCode", "", "getYoutubeVideoCode", "()Ljava/lang/String;", "setYoutubeVideoCode", "(Ljava/lang/String;)V", "addClickablePartTextViewResizable", "Landroid/text/SpannableStringBuilder;", "strSpanned", "Landroid/text/Spanned;", "tv", "Landroid/widget/TextView;", "maxLine", "", "spanableText", "viewMore", "", "convertDpToPixel", "dp", "", "context", "Landroid/content/Context;", "extractYoutubeId", "s", "getDeviceId", "Landroid/app/Activity;", "getURLForResource", "resourceId", "getVideoCode", "", "youtubeUrl", "hideKeyboard", "activity", "makeTextViewResizable", "expandText", "openMap", "lat", "lang", "removeTrailingZeroFormater", "d", "removeTrailingZeroFormatter", "spannableText", "activityContext", "stringBuilder", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "tvCensorLang", "Companion", "app_debug"})
public final class Constant {
    @org.jetbrains.annotations.NotNull()
    public static final com.net.pvr1.utils.Constant.Companion Companion = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String platform = "ANDROID";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String version = "11.3";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String status = "success";
    public static final int SUCCESS_CODE = 10001;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String pvrCare = "https://www.pvrcinemas.com/pvrstatic/pvr-care/";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String merchandise = "https://pvr.macmerise.com/?user_agent=pvr";
    private static int select_pos = 0;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String IS_LOGIN = "is_login";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CITY_NAME = "city_name";
    private static int DISPLAY = 1;
    public static final int SUCCESS_RESULT = 1;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String RECEVIER = "packageName.RECEVIER";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LOCATION_DATA_EXTRA = "packageName.LOCATION_DATA_EXTRA";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ADDRESS = "packageName.ADDRESS";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LOCAITY = "packageName.LOCAITY";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COUNTRY = "packageName.COUNTRY";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DISTRICT = "packageName.DISTRICT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String POST_CODE = "packageName.POST_CODE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATE = "packageName.STATE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PREFS_TOKEN_FILE = "prefs_token_file";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_NAME = "user_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_EMAIL = "user_email";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_TOKEN = "user_token";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_DOB = "user_dob";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_MO_NUMBER = "user_mo";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_ID = "user_id";
    public static final int SEAT_AVAILABEL = 1;
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String TRANSACTION_ID = "0";
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String OfferDialogImage = "0";
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String BOOKING_ID = "0";
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String SESSION_ID = "0";
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String CINEMA_ID = "0";
    public static final boolean ON_BOARDING_CLICK = false;
    public static final int SEAT_BOOKED = 2;
    public static final int SEAT_SELECTED = 3;
    public static final int HATCHBACK = 5;
    public static final int SEAT_SOCIAL_DISTANCING = 4;
    public static final int SEAT_SELECTED_HATCHBACK = 1137;
    public static final int SEAT_SELECTED_SUV = 1136;
    public static final int SEAT_SELECTED_BIKE = 1138;
    public static final int SUV = 6;
    public static final int BIKE = 7;
    public static final int BIKE_SEAT_BOOKED = 8;
    @org.jetbrains.annotations.Nullable()
    private static com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse.Output PrivilegeHomeResponseConst;
    @org.jetbrains.annotations.Nullable()
    private static com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Output PlaceHolder;
    @org.jetbrains.annotations.NotNull()
    private static java.text.DecimalFormat DECIFORMAT;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String youtubeVideoCode;
    
    public Constant() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDeviceId(@org.jetbrains.annotations.NotNull()
    android.app.Activity context) {
        return null;
    }
    
    public final void spannableText(@org.jetbrains.annotations.NotNull()
    android.app.Activity activityContext, @org.jetbrains.annotations.Nullable()
    java.lang.StringBuilder stringBuilder, @org.jetbrains.annotations.NotNull()
    android.widget.TextView tvCensorLang) {
    }
    
    private final android.text.SpannableStringBuilder addClickablePartTextViewResizable(android.text.Spanned strSpanned, android.widget.TextView tv, int maxLine, java.lang.String spanableText, boolean viewMore) {
        return null;
    }
    
    public final void makeTextViewResizable(@org.jetbrains.annotations.NotNull()
    android.widget.TextView tv, int maxLine, @org.jetbrains.annotations.NotNull()
    java.lang.String expandText, boolean viewMore) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String extractYoutubeId(@org.jetbrains.annotations.NotNull()
    java.lang.String s) {
        return null;
    }
    
    public final int convertDpToPixel(float dp, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String removeTrailingZeroFormatter(float d) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String removeTrailingZeroFormater(float d) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getYoutubeVideoCode() {
        return null;
    }
    
    public final void setYoutubeVideoCode(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public final void getVideoCode(@org.jetbrains.annotations.NotNull()
    java.lang.String youtubeUrl) {
    }
    
    public final void openMap(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String lat, @org.jetbrains.annotations.NotNull()
    java.lang.String lang) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getURLForResource(int resourceId) {
        return null;
    }
    
    public final void hideKeyboard(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b%\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\n\"\u0004\b\u000f\u0010\fR\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010$\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\n\"\u0004\b&\u0010\fR\u000e\u0010\'\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u001c\u0010)\u001a\u0004\u0018\u00010*X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u001c\u0010/\u001a\u0004\u0018\u000100X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u000e\u00105\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010=\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b>\u0010\n\"\u0004\b?\u0010\fR\u000e\u0010@\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010D\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bE\u0010\n\"\u0004\bF\u0010\fR\u000e\u0010G\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010P\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bQ\u0010\u001a\"\u0004\bR\u0010\u001cR\u000e\u0010S\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006U"}, d2 = {"Lcom/net/pvr1/utils/Constant$Companion;", "", "()V", "ADDRESS", "", "BIKE", "", "BIKE_SEAT_BOOKED", "BOOKING_ID", "getBOOKING_ID", "()Ljava/lang/String;", "setBOOKING_ID", "(Ljava/lang/String;)V", "CINEMA_ID", "getCINEMA_ID", "setCINEMA_ID", "CITY_NAME", "COUNTRY", "DECIFORMAT", "Ljava/text/DecimalFormat;", "getDECIFORMAT", "()Ljava/text/DecimalFormat;", "setDECIFORMAT", "(Ljava/text/DecimalFormat;)V", "DISPLAY", "getDISPLAY", "()I", "setDISPLAY", "(I)V", "DISTRICT", "HATCHBACK", "IS_LOGIN", "LOCAITY", "LOCATION_DATA_EXTRA", "ON_BOARDING_CLICK", "", "OfferDialogImage", "getOfferDialogImage", "setOfferDialogImage", "POST_CODE", "PREFS_TOKEN_FILE", "PlaceHolder", "Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Output;", "getPlaceHolder", "()Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Output;", "setPlaceHolder", "(Lcom/net/pvr1/ui/home/fragment/home/response/HomeResponse$Output;)V", "PrivilegeHomeResponseConst", "Lcom/net/pvr1/ui/home/fragment/privilege/response/PrivilegeHomeResponse$Output;", "getPrivilegeHomeResponseConst", "()Lcom/net/pvr1/ui/home/fragment/privilege/response/PrivilegeHomeResponse$Output;", "setPrivilegeHomeResponseConst", "(Lcom/net/pvr1/ui/home/fragment/privilege/response/PrivilegeHomeResponse$Output;)V", "RECEVIER", "SEAT_AVAILABEL", "SEAT_BOOKED", "SEAT_SELECTED", "SEAT_SELECTED_BIKE", "SEAT_SELECTED_HATCHBACK", "SEAT_SELECTED_SUV", "SEAT_SOCIAL_DISTANCING", "SESSION_ID", "getSESSION_ID", "setSESSION_ID", "STATE", "SUCCESS_CODE", "SUCCESS_RESULT", "SUV", "TRANSACTION_ID", "getTRANSACTION_ID", "setTRANSACTION_ID", "USER_DOB", "USER_EMAIL", "USER_ID", "USER_MO_NUMBER", "USER_NAME", "USER_TOKEN", "merchandise", "platform", "pvrCare", "select_pos", "getSelect_pos", "setSelect_pos", "status", "version", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final int getSelect_pos() {
            return 0;
        }
        
        public final void setSelect_pos(int p0) {
        }
        
        public final int getDISPLAY() {
            return 0;
        }
        
        public final void setDISPLAY(int p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getTRANSACTION_ID() {
            return null;
        }
        
        public final void setTRANSACTION_ID(@org.jetbrains.annotations.NotNull()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getOfferDialogImage() {
            return null;
        }
        
        public final void setOfferDialogImage(@org.jetbrains.annotations.NotNull()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getBOOKING_ID() {
            return null;
        }
        
        public final void setBOOKING_ID(@org.jetbrains.annotations.NotNull()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getSESSION_ID() {
            return null;
        }
        
        public final void setSESSION_ID(@org.jetbrains.annotations.NotNull()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getCINEMA_ID() {
            return null;
        }
        
        public final void setCINEMA_ID(@org.jetbrains.annotations.NotNull()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse.Output getPrivilegeHomeResponseConst() {
            return null;
        }
        
        public final void setPrivilegeHomeResponseConst(@org.jetbrains.annotations.Nullable()
        com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse.Output p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Output getPlaceHolder() {
            return null;
        }
        
        public final void setPlaceHolder(@org.jetbrains.annotations.Nullable()
        com.net.pvr1.ui.home.fragment.home.response.HomeResponse.Output p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.text.DecimalFormat getDECIFORMAT() {
            return null;
        }
        
        public final void setDECIFORMAT(@org.jetbrains.annotations.NotNull()
        java.text.DecimalFormat p0) {
        }
    }
}