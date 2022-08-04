package com.net.pvr1.utils;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import com.net.pvr1.R;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0005\u00a2\u0006\u0002\u0010\u0002J&\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u000e\u0010\u0007\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\t2\u0006\u0010\n\u001a\u00020\u000b\u00a8\u0006\r"}, d2 = {"Lcom/net/pvr1/utils/Constant;", "", "()V", "spannableText", "", "activityContext", "Landroid/app/Activity;", "stringBuilder", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "tvCensorLang", "Landroid/widget/TextView;", "Companion", "app_debug"})
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
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String IS_LOGIN = "is_login";
    private static int DISPLAY = 1;
    
    public Constant() {
        super();
    }
    
    public final void spannableText(@org.jetbrains.annotations.NotNull()
    android.app.Activity activityContext, @org.jetbrains.annotations.Nullable()
    java.lang.StringBuilder stringBuilder, @org.jetbrains.annotations.NotNull()
    android.widget.TextView tvCensorLang) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/net/pvr1/utils/Constant$Companion;", "", "()V", "DISPLAY", "", "getDISPLAY", "()I", "setDISPLAY", "(I)V", "IS_LOGIN", "", "SUCCESS_CODE", "merchandise", "platform", "pvrCare", "status", "version", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final int getDISPLAY() {
            return 0;
        }
        
        public final void setDISPLAY(int p0) {
        }
    }
}