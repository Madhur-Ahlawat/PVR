package com.net.pvr1.ui.home.fragment.privilege;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import com.net.pvr1.R;
import com.net.pvr1.databinding.FragmentPrivilegeBinding;
import com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeAdapter;
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse;
import com.net.pvr1.utils.*;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\f\u001a\u00020\rH\u0002J&\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\u001a\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\u0010\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\u001aH\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000b\u00a8\u0006\u001b"}, d2 = {"Lcom/net/pvr1/ui/home/fragment/privilege/PrivilegeFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/net/pvr1/ui/home/fragment/privilege/adapter/PrivilegeHomeAdapter$RecycleViewItemClickListener;", "()V", "binding", "Lcom/net/pvr1/databinding/FragmentPrivilegeBinding;", "preferences", "Lcom/net/pvr1/utils/PreferenceManager;", "getPreferences", "()Lcom/net/pvr1/utils/PreferenceManager;", "setPreferences", "(Lcom/net/pvr1/utils/PreferenceManager;)V", "notLoginCategoryDataLoad", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "privilegeHomeClick", "comingSoonItem", "Lcom/net/pvr1/ui/home/fragment/privilege/response/PrivilegeHomeResponse$Output$Pinfo;", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class PrivilegeFragment extends androidx.fragment.app.Fragment implements com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeHomeAdapter.RecycleViewItemClickListener {
    private com.net.pvr1.databinding.FragmentPrivilegeBinding binding;
    @javax.inject.Inject()
    public com.net.pvr1.utils.PreferenceManager preferences;
    
    public PrivilegeFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.net.pvr1.utils.PreferenceManager getPreferences() {
        return null;
    }
    
    public final void setPreferences(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.utils.PreferenceManager p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void notLoginCategoryDataLoad() {
    }
    
    @java.lang.Override()
    public void privilegeHomeClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse.Output.Pinfo comingSoonItem) {
    }
}