package com.net.pvr1.utils;

import android.content.Context;
import android.content.SharedPreferences;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000f\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\r\u001a\u00020\u000eJ\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010J\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\u0010J\b\u0010\u0014\u001a\u0004\u0018\u00010\u0010J\b\u0010\u0015\u001a\u0004\u0018\u00010\u0010J\b\u0010\u0016\u001a\u0004\u0018\u00010\u0010J\u000e\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u0010J\u000e\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u0012J\u000e\u0010\u001a\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u0010J\u000e\u0010\u001c\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u0010J\u000e\u0010\u001e\u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020\u0010J\u000e\u0010 \u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u0010R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/net/pvr1/utils/PreferenceManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "editor", "Landroid/content/SharedPreferences$Editor;", "getEditor", "()Landroid/content/SharedPreferences$Editor;", "setEditor", "(Landroid/content/SharedPreferences$Editor;)V", "prefs", "Landroid/content/SharedPreferences;", "clearData", "", "geMobileNumber", "", "getIsLogin", "", "getSaveDob", "getToken", "getUserId", "getUserName", "saveDob", "checkLogin", "saveIsLogin", "saveMobileNumber", "mobileNum", "saveToken", "token", "saveUserId", "userId", "saveUserName", "app_debug"})
public final class PreferenceManager {
    @org.jetbrains.annotations.Nullable()
    private android.content.SharedPreferences.Editor editor;
    private android.content.SharedPreferences prefs;
    
    @javax.inject.Inject()
    public PreferenceManager(@org.jetbrains.annotations.NotNull()
    @dagger.hilt.android.qualifiers.ApplicationContext()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.content.SharedPreferences.Editor getEditor() {
        return null;
    }
    
    public final void setEditor(@org.jetbrains.annotations.Nullable()
    android.content.SharedPreferences.Editor p0) {
    }
    
    public final void saveToken(@org.jetbrains.annotations.NotNull()
    java.lang.String token) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getToken() {
        return null;
    }
    
    public final void saveUserName(@org.jetbrains.annotations.NotNull()
    java.lang.String token) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserName() {
        return null;
    }
    
    public final void saveUserId(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserId() {
        return null;
    }
    
    public final void saveMobileNumber(@org.jetbrains.annotations.NotNull()
    java.lang.String mobileNum) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String geMobileNumber() {
        return null;
    }
    
    public final void saveIsLogin(boolean checkLogin) {
    }
    
    public final boolean getIsLogin() {
        return false;
    }
    
    public final void saveDob(@org.jetbrains.annotations.NotNull()
    java.lang.String checkLogin) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSaveDob() {
        return null;
    }
    
    public final void clearData() {
    }
}