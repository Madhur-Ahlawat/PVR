package com.net.pvr1.ui.player

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.*
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPlayerBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.player.viewModel.PlayerViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.printLog
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {
//    @Inject
//    lateinit var preferences: AppPreferences
    private var binding: ActivityPlayerBinding? = null
    private val authViewModel: PlayerViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var APIKEY = "AIzaSyBHqqCEPXq1hU3nEcsBgROgwGOsiSDZlfc"
    var isSingleVideo = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        var trailerUrl = intent.getStringExtra("trailerUrl")
        getYoutubeVideoId(trailerUrl)

        if (trailerUrl != null && trailerUrl != "" && (trailerUrl.contains("v=") || trailerUrl.contains(
                "list="
            )) || trailerUrl?.contains(
                "https://youtu.be"
            )!!
        ) {
            trailerUrl = trailerUrl.contains("v=").toString()
            Constant().getVideoCode(trailerUrl.trim { it <= ' ' })

            // Initializing video player with developer key
            binding?.youtubeFragment?.initialize(APIKEY, this)
        } else {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.video_not_avail),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
        }

    }

    private fun getYoutubeVideoId(youtubeUrl: String?): String? {
        var vId: String? = null
        val pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher: Matcher = pattern.matcher(youtubeUrl)
        if (matcher.matches()) {
            vId = matcher.group(1)
            printLog("youtubeUrlCheck-->${vId}")
        }
        return vId
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        if (!wasRestored && Constant().youtubeVideoCode != null) {
            if (isSingleVideo) {
                player?.loadVideo(Constant().youtubeVideoCode)
            } else {
                player?.loadPlaylist(Constant().youtubeVideoCode)
            }
            // Hiding player controls
            player?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        TODO("Not yet implemented")
    }


}