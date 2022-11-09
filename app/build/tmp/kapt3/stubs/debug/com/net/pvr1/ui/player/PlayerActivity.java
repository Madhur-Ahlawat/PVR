package com.net.pvr1.ui.player;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityNowShowingBinding;
import com.net.pvr1.databinding.ActivityPlayerBinding;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.utils.Constant;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@kotlin.Suppress(names = {"DEPRECATION"})
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\f\u001a\u0004\u0018\u00010\u00042\u0006\u0010\r\u001a\u00020\u0004H\u0002J\u0012\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0014J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\t\"\u0004\b\n\u0010\u000b\u00a8\u0006\u0014"}, d2 = {"Lcom/net/pvr1/ui/player/PlayerActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "apiKey", "", "binding", "Lcom/net/pvr1/databinding/ActivityPlayerBinding;", "isSingleVideo", "", "()Z", "setSingleVideo", "(Z)V", "getYoutubeVideoId", "youtubeUrl", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "playVideo", "vid", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class PlayerActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.net.pvr1.databinding.ActivityPlayerBinding binding;
    private java.lang.String apiKey = "AIzaSyBHqqCEPXq1hU3nEcsBgROgwGOsiSDZlfc";
    private boolean isSingleVideo = true;
    
    public PlayerActivity() {
        super();
    }
    
    public final boolean isSingleVideo() {
        return false;
    }
    
    public final void setSingleVideo(boolean p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final java.lang.String getYoutubeVideoId(java.lang.String youtubeUrl) {
        return null;
    }
    
    private final void playVideo(java.lang.String vid) {
    }
}