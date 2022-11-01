package com.net.pvr1.ui.player

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityNowShowingBinding
import com.net.pvr1.databinding.ActivityPlayerBinding
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.printLog
import com.net.pvr1.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern

@Suppress("DEPRECATION")
@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {
    private var binding: ActivityPlayerBinding? = null
    private var apiKey = "AIzaSyBHqqCEPXq1hU3nEcsBgROgwGOsiSDZlfc"
    var isSingleVideo = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        var trailerUrl = intent.getStringExtra("trailerUrl")
        printLog("trailerUrl--->${trailerUrl}")

        if (trailerUrl != null && trailerUrl != "" && (trailerUrl.contains("v=") || trailerUrl.contains(
                "list="
            )) || trailerUrl?.contains(
                "https://youtu.be"
            )!!
        ) {
            trailerUrl = trailerUrl.contains("v=").toString()
            Constant().getVideoCode(trailerUrl.trim { it <= ' ' })

            // Initializing video player with developer key
//            playVideo(trailerUrl)
            getYoutubeVideoId(trailerUrl)

//            binding?.youtubeFragment?.initialize(apiKey, this)
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

//        getYoutubeVideoId(trailerUrl)
    }

    private fun getYoutubeVideoId(youtubeUrl: String): String? {
        toast("hello${youtubeUrl}")
        var vId: String? = null
        val pattern = Pattern.compile(
            "^https?://www.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher: Matcher = pattern.matcher(youtubeUrl)
        if (matcher.matches()) {
            vId = matcher.group(1)
            printLog("youtubeUrlCheck-->${vId}")
            playVideo(vId)
            toast("1")

        }else{
            toast("2")
        }
        return vId
    }

    private fun playVideo(vid: String) {
        val youtubeFragment =
            fragmentManager.findFragmentById(R.id.youtubeFragment) as YouTubePlayerFragment
        youtubeFragment.initialize(apiKey,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    // do any work here to cue video, play video, etc.

                    youTubePlayer.cueVideo(vid)
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                }
            })
    }


//    override fun onInitializationSuccess(
//        p0: YouTubePlayer.Provider?,
//        player: YouTubePlayer?,
//        wasRestored: Boolean
//    ) {
//        if (!wasRestored && Constant().youtubeVideoCode != null) {
//            if (isSingleVideo) {
//                player?.loadVideo(Constant().youtubeVideoCode)
//            } else {
//                player?.loadPlaylist(Constant().youtubeVideoCode)
//            }
//            // Hiding player controls
//            player?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
//        }
//    }
//
//    override fun onInitializationFailure(
//        p0: YouTubePlayer.Provider?,
//        p1: YouTubeInitializationResult?
//    ) {
//        toast("$p1")
//    }


}