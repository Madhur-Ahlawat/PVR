package com.net.pvr1.di.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import javax.inject.Inject;
import javax.inject.Singleton;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0017\u001a\u00020\u0018J\u0010\u0010\u0019\u001a\u00020\u00182\b\u0010\u001a\u001a\u0004\u0018\u00010\u0010J\u0010\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u001dH\u0007J\u0012\u0010\u001e\u001a\u0004\u0018\u00010\u00102\b\u0010\u001a\u001a\u0004\u0018\u00010\u0010J\u0010\u0010\u001f\u001a\u00020 2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0010J\b\u0010!\u001a\u0004\u0018\u00010\u0000J\u0010\u0010\"\u001a\u00020#2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0010J\u0010\u0010$\u001a\u00020%2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0010J\u0012\u0010&\u001a\u0004\u0018\u00010\u00102\b\u0010\u001a\u001a\u0004\u0018\u00010\u0010J\u0018\u0010\'\u001a\u00020\u00182\b\u0010\u001a\u001a\u0004\u0018\u00010\u00102\u0006\u0010(\u001a\u00020 J\u0018\u0010)\u001a\u00020\u00182\b\u0010\u001a\u001a\u0004\u0018\u00010\u00102\u0006\u0010(\u001a\u00020#J\u001f\u0010*\u001a\u00020\u00182\b\u0010\u001a\u001a\u0004\u0018\u00010\u00102\b\u0010(\u001a\u0004\u0018\u00010%\u00a2\u0006\u0002\u0010+J\u001a\u0010,\u001a\u00020\u00182\b\u0010\u001a\u001a\u0004\u0018\u00010\u00102\b\u0010(\u001a\u0004\u0018\u00010\u0010R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u0010X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012\u00a8\u0006-"}, d2 = {"Lcom/net/pvr1/di/preference/AppPreferences;", "", "()V", "editor", "Landroid/content/SharedPreferences$Editor;", "getEditor", "()Landroid/content/SharedPreferences$Editor;", "setEditor", "(Landroid/content/SharedPreferences$Editor;)V", "preference", "Landroid/content/SharedPreferences;", "getPreference", "()Landroid/content/SharedPreferences;", "setPreference", "(Landroid/content/SharedPreferences;)V", "preferenceName", "", "getPreferenceName", "()Ljava/lang/String;", "setPreferenceName", "(Ljava/lang/String;)V", "tag", "getTag", "clearData", "", "clearString", "key", "consultantPreferences", "ctx", "Landroid/content/Context;", "getBitmap", "getBoolean", "", "getInstance", "getInt", "", "getLong", "", "getString", "putBoolean", "value", "putInt", "putLong", "(Ljava/lang/String;Ljava/lang/Long;)V", "putString", "app_debug"})
@javax.inject.Singleton()
public final class AppPreferences {
    @org.jetbrains.annotations.NotNull()
    private java.lang.String preferenceName = "PiInsite";
    @org.jetbrains.annotations.Nullable()
    private android.content.SharedPreferences preference;
    @org.jetbrains.annotations.Nullable()
    private android.content.SharedPreferences.Editor editor;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tag = "ConsultantPreferences=======";
    
    @javax.inject.Inject()
    public AppPreferences() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPreferenceName() {
        return null;
    }
    
    public final void setPreferenceName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.content.SharedPreferences getPreference() {
        return null;
    }
    
    public final void setPreference(@org.jetbrains.annotations.Nullable()
    android.content.SharedPreferences p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.content.SharedPreferences.Editor getEditor() {
        return null;
    }
    
    public final void setEditor(@org.jetbrains.annotations.Nullable()
    android.content.SharedPreferences.Editor p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTag() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.net.pvr1.di.preference.AppPreferences getInstance() {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"CommitPrefEdits"})
    @javax.inject.Inject()
    public final void consultantPreferences(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
    }
    
    public final void clearData() {
    }
    
    public final void clearString(@org.jetbrains.annotations.Nullable()
    java.lang.String key) {
    }
    
    public final void putString(@org.jetbrains.annotations.Nullable()
    java.lang.String key, @org.jetbrains.annotations.Nullable()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getBitmap(@org.jetbrains.annotations.Nullable()
    java.lang.String key) {
        return null;
    }
    
    public final void putInt(@org.jetbrains.annotations.Nullable()
    java.lang.String key, int value) {
    }
    
    public final void putBoolean(@org.jetbrains.annotations.Nullable()
    java.lang.String key, boolean value) {
    }
    
    public final boolean getBoolean(@org.jetbrains.annotations.Nullable()
    java.lang.String key) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getString(@org.jetbrains.annotations.Nullable()
    java.lang.String key) {
        return null;
    }
    
    public final int getInt(@org.jetbrains.annotations.Nullable()
    java.lang.String key) {
        return 0;
    }
    
    public final void putLong(@org.jetbrains.annotations.Nullable()
    java.lang.String key, @org.jetbrains.annotations.Nullable()
    java.lang.Long value) {
    }
    
    public final long getLong(@org.jetbrains.annotations.Nullable()
    java.lang.String key) {
        return 0L;
    }
}