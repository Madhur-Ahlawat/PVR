package com.net.pvr1.ui.dailogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import com.net.pvr1.R;
import com.net.pvr1.databinding.DialogOptionBinding;
import com.net.pvr1.utils.Constant;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001Ba\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0003\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\b\u0001\u0010\t\u001a\u00020\u0005\u0012\b\b\u0001\u0010\n\u001a\u00020\u0005\u0012\u0010\b\u0002\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\f\u0012\u0010\b\u0002\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\f\u00a2\u0006\u0002\u0010\u000fJ\u0012\u0010\u0012\u001a\u00020\r2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0014J\b\u0010\u0015\u001a\u00020\rH\u0016R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/net/pvr1/ui/dailogs/OptionDialog;", "Landroid/app/Dialog;", "context", "Landroid/content/Context;", "illustrationRes", "", "title", "subtitle", "", "positiveBtnText", "negativeBtnText", "positiveClick", "Lkotlin/Function0;", "", "negativeClick", "(Landroid/content/Context;IILjava/lang/String;IILkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "binding", "Lcom/net/pvr1/databinding/DialogOptionBinding;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDetachedFromWindow", "app_debug"})
public final class OptionDialog extends android.app.Dialog {
    private final int illustrationRes = 0;
    private final int title = 0;
    private final java.lang.String subtitle = null;
    private final int positiveBtnText = 0;
    private final int negativeBtnText = 0;
    private final kotlin.jvm.functions.Function0<kotlin.Unit> positiveClick = null;
    private final kotlin.jvm.functions.Function0<kotlin.Unit> negativeClick = null;
    private com.net.pvr1.databinding.DialogOptionBinding binding;
    
    public OptionDialog(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @androidx.annotation.DrawableRes()
    int illustrationRes, @androidx.annotation.StringRes()
    int title, @org.jetbrains.annotations.NotNull()
    java.lang.String subtitle, @androidx.annotation.StringRes()
    int positiveBtnText, @androidx.annotation.StringRes()
    int negativeBtnText, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> positiveClick, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> negativeClick) {
        super(null);
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onDetachedFromWindow() {
    }
}