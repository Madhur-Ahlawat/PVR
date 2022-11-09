package com.net.pvr1.ui.movieDetails.nowShowing;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.bumptech.glide.Glide;
import com.net.pvr1.BuildConfig;
import com.net.pvr1.R;
import com.net.pvr1.databinding.ActivityNowShowingBinding;
import com.net.pvr1.ui.bookingSession.BookingActivity;
import com.net.pvr1.ui.dailogs.LoaderDialog;
import com.net.pvr1.ui.dailogs.OptionDialog;
import com.net.pvr1.ui.movieDetails.nowShowing.adapter.*;
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse;
import com.net.pvr1.ui.movieDetails.nowShowing.viewModel.MovieDetailsViewModel;
import com.net.pvr1.ui.player.PlayerActivity;
import com.net.pvr1.utils.*;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u00052\u00020\u00062\u00020\u0007B\u0005\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0014H\u0002J\u0010\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u001bH\u0016J\u0010\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u001dH\u0016J\u0012\u0010\u001e\u001a\u00020\u00142\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0014J\u0010\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020#H\u0002J\u0010\u0010$\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020%H\u0016J\u0010\u0010&\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u001dH\u0016R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/net/pvr1/ui/movieDetails/nowShowing/NowShowingActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/net/pvr1/ui/movieDetails/nowShowing/adapter/CastAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/movieDetails/nowShowing/adapter/CrewAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/movieDetails/nowShowing/adapter/MusicVideoAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/movieDetails/nowShowing/adapter/TrailerAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/movieDetails/nowShowing/adapter/TrailerTrsAdapter$RecycleViewItemClickListener;", "Lcom/net/pvr1/ui/movieDetails/nowShowing/adapter/MusicVideoTrsAdapter$RecycleViewItemClickListener;", "()V", "authViewModel", "Lcom/net/pvr1/ui/movieDetails/nowShowing/viewModel/MovieDetailsViewModel;", "getAuthViewModel", "()Lcom/net/pvr1/ui/movieDetails/nowShowing/viewModel/MovieDetailsViewModel;", "authViewModel$delegate", "Lkotlin/Lazy;", "binding", "Lcom/net/pvr1/databinding/ActivityNowShowingBinding;", "loader", "Lcom/net/pvr1/ui/dailogs/LoaderDialog;", "castClick", "", "comingSoonItem", "Lcom/net/pvr1/ui/movieDetails/nowShowing/response/MovieDetailsResponse$Mb$Cast;", "crewClick", "Lcom/net/pvr1/ui/movieDetails/nowShowing/response/MovieDetailsResponse$Mb$Crew;", "movieDetails", "musicVideo", "Lcom/net/pvr1/ui/movieDetails/nowShowing/response/MovieDetailsResponse$Mb$Crew$Role;", "musicVideoTrsClick", "Lcom/net/pvr1/ui/movieDetails/nowShowing/response/MovieDetailsResponse$Trs;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "retrieveData", "output", "Lcom/net/pvr1/ui/movieDetails/nowShowing/response/MovieDetailsResponse$Output;", "trailerClick", "Lcom/net/pvr1/ui/movieDetails/nowShowing/response/MovieDetailsResponse$Mb$Video;", "trailerTrsClick", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class NowShowingActivity extends androidx.appcompat.app.AppCompatActivity implements com.net.pvr1.ui.movieDetails.nowShowing.adapter.CastAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.movieDetails.nowShowing.adapter.CrewAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.movieDetails.nowShowing.adapter.MusicVideoAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.movieDetails.nowShowing.adapter.TrailerAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.movieDetails.nowShowing.adapter.TrailerTrsAdapter.RecycleViewItemClickListener, com.net.pvr1.ui.movieDetails.nowShowing.adapter.MusicVideoTrsAdapter.RecycleViewItemClickListener {
    private com.net.pvr1.databinding.ActivityNowShowingBinding binding;
    private com.net.pvr1.ui.dailogs.LoaderDialog loader;
    private final kotlin.Lazy authViewModel$delegate = null;
    
    public NowShowingActivity() {
        super();
    }
    
    private final com.net.pvr1.ui.movieDetails.nowShowing.viewModel.MovieDetailsViewModel getAuthViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void movieDetails() {
    }
    
    private final void retrieveData(com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse.Output output) {
    }
    
    @java.lang.Override()
    public void castClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse.Mb.Cast comingSoonItem) {
    }
    
    @java.lang.Override()
    public void crewClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse.Mb.Crew comingSoonItem) {
    }
    
    @java.lang.Override()
    public void musicVideo(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse.Mb.Crew.Role comingSoonItem) {
    }
    
    @java.lang.Override()
    public void trailerClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse.Mb.Video comingSoonItem) {
    }
    
    @java.lang.Override()
    public void trailerTrsClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse.Trs comingSoonItem) {
    }
    
    @java.lang.Override()
    public void musicVideoTrsClick(@org.jetbrains.annotations.NotNull()
    com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse.Trs comingSoonItem) {
    }
}