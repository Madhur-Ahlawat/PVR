package com.net.pvr1

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playVideo("AQubo_u_jec")

        getYoutubeVideoId("https://www.youtube.com/watch?v=AQubo_u_jec")
    }


    private fun getYoutubeVideoId(youtubeUrl: String): String? {
        Toast.makeText(this@MainActivity, "error2", Toast.LENGTH_SHORT).show()
        var vId: String? = null
        val pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher: Matcher = pattern.matcher(youtubeUrl)

        if (matcher.matches()) {
            vId = matcher.group(1)
            println("youtubeUrlCheck-->${vId}")
            Toast.makeText(this@MainActivity, "error3${vId}", Toast.LENGTH_SHORT).show()

            playVideo("AQubo_u_jec")
        }
        return vId
    }

    private fun playVideo(vid: String) {
        val youtubeFragment =
            fragmentManager.findFragmentById(R.id.youtube) as YouTubePlayerFragment
        youtubeFragment.initialize("AIzaSyAD0AZTCMULaXpQfulSKNWuLlLl-nbdSf0",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    // do any work here to cue video, play video, etc.
                    Toast.makeText(this@MainActivity, "error6", Toast.LENGTH_SHORT).show()

                    youTubePlayer.cueVideo(vid)
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                    Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
                }
            })
    }
}