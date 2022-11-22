package com.net.pvr1.ui.webView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.*;
import androidx.appcompat.app.AppCompatActivity;
import com.net.pvr1.databinding.ActivityWebViewBinding;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0010B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u0003J\b\u0010\f\u001a\u00020\nH\u0002J\u0012\u0010\r\u001a\u00020\n2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0014R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/net/pvr1/ui/webView/WebViewActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/net/pvr1/databinding/ActivityWebViewBinding;", "from", "", "get", "title", "loadWebData", "", "url", "movedNext", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "JavaScriptInterface", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class WebViewActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.net.pvr1.databinding.ActivityWebViewBinding binding;
    private java.lang.String from = "";
    private java.lang.String title = "";
    private java.lang.String get = "";
    
    public WebViewActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movedNext() {
    }
    
    @android.annotation.SuppressLint(value = {"SetJavaScriptEnabled"})
    private final void loadWebData(java.lang.String url) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0007R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004\u00a8\u0006\r"}, d2 = {"Lcom/net/pvr1/ui/webView/WebViewActivity$JavaScriptInterface;", "", "mContext", "Landroid/app/Activity;", "(Landroid/app/Activity;)V", "getMContext", "()Landroid/app/Activity;", "setMContext", "paymentResponse", "", "response", "", "pageName", "app_debug"})
    public static final class JavaScriptInterface {
        @org.jetbrains.annotations.NotNull()
        private android.app.Activity mContext;
        
        public JavaScriptInterface(@org.jetbrains.annotations.NotNull()
        android.app.Activity mContext) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.app.Activity getMContext() {
            return null;
        }
        
        public final void setMContext(@org.jetbrains.annotations.NotNull()
        android.app.Activity p0) {
        }
        
        @android.webkit.JavascriptInterface()
        public final void paymentResponse(@org.jetbrains.annotations.NotNull()
        java.lang.String response, @org.jetbrains.annotations.NotNull()
        java.lang.String pageName) {
        }
    }
}