package com.net.pvr1.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityOtpVerifyBinding
import com.net.pvr1.databinding.ActivityPlayerBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.otpVerify.viewModel.OtpVerifyViewModel
import com.net.pvr1.ui.player.viewModel.PlayerViewModel
import com.net.pvr1.utils.printLog
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityPlayerBinding? = null
    private val authViewModel: PlayerViewModel by viewModels()
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        val trailerUrl = intent.getStringExtra("trailerUrl")
        getYoutubeVideoId(trailerUrl)
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
            playVideo(vId)
        }
        return vId
    }

    private fun playVideo(vid: Any) {
        val youtubeFragment =
            fragmentManager.findFragmentById(R.id.youtubeFragment) as YouTubePlayerFragment
        youtubeFragment.initialize("AIzaSyAD0AZTCMULaXpQfulSKNWuLlLl-nbdSf0",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    // do any work here to cue video, play video, etc.
                    youTubePlayer.cueVideo(vid.toString())
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                }
            })
    }

}