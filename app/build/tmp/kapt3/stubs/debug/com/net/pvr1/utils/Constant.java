package com.net.pvr1.utils;

import android.app.Activity;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import com.net.pvr1.R;
import java.net.MalformedURLException;
import java.net.URL;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u0005\u00a2\u0006\u0002\u0010\u0002J2\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0010\u0010\u000f\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0010\u001a\u00020\fJ&\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ&\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u00162\u000e\u0010\u0017\u001a\n\u0018\u00010\u0018j\u0004\u0018\u0001`\u00192\u0006\u0010\u001a\u001a\u00020\b\u00a8\u0006\u001c"}, d2 = {"Lcom/net/pvr1/utils/Constant;", "", "()V", "addClickablePartTextViewResizable", "Landroid/text/SpannableStringBuilder;", "strSpanned", "Landroid/text/Spanned;", "tv", "Landroid/widget/TextView;", "maxLine", "", "spanableText", "", "viewMore", "", "extractYoutubeId", "s", "makeTextViewResizable", "", "expandText", "spannableText", "activityContext", "Landroid/app/Activity;", "stringBuilder", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "tvCensorLang", "Companion", "app_debug"})
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
    
    public Constant() {
        super();
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
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0016\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\n\"\u0004\b\u001b\u0010\fR\u000e\u0010\u001c\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/net/pvr1/utils/Constant$Companion;", "", "()V", "ADDRESS", "", "CITY_NAME", "COUNTRY", "DISPLAY", "", "getDISPLAY", "()I", "setDISPLAY", "(I)V", "DISTRICT", "IS_LOGIN", "LOCAITY", "LOCATION_DATA_EXTRA", "POST_CODE", "RECEVIER", "STATE", "SUCCESS_CODE", "SUCCESS_RESULT", "merchandise", "platform", "pvrCare", "select_pos", "getSelect_pos", "setSelect_pos", "status", "version", "app_debug"})
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
    }
}