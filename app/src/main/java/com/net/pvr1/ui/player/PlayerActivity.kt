package com.net.pvr1.ui.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPlayerBinding
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.utils.printLog
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class PlayerActivity : AppCompatActivity(),YouTubePlayer.OnInitializedListener {
    private var binding: ActivityPlayerBinding? = null
    private var apiKey = "AIzaSyBHqqCEPXq1hU3nEcsBgROgwGOsiSDZlfc"
    private var isSingleVideo = true
    private var trailerUrl = ""
    private var youtubeVideoCode=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        trailerUrl = intent.getStringExtra("trailerUrl").toString()
        printLog("trailerUrl--->${trailerUrl}")

        if (trailerUrl != "" && (trailerUrl.contains("v=") || trailerUrl.contains(
                "list="
            )) || trailerUrl.contains(
                "https://youtu.be"
            )
        ) {
            isSingleVideo = trailerUrl.contains("v=")
            getVideoCode(trailerUrl.trim { it <= ' ' })
            val youtubeFragment =
                fragmentManager.findFragmentById(R.id.youtubeFragment) as YouTubePlayerFragment
            youtubeFragment.initialize(apiKey, this)

        } else {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.video_not_avail),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                 finish()
                },
                negativeClick = {
                })
            dialog.show()
        }

    }

    private fun getVideoCode(youtubeUrl: String) {
        val videoCode: Array<String>
        try {
            videoCode = if (youtubeUrl.contains("v=")) {
                youtubeUrl.split("v=").toTypedArray()
            } else {
                youtubeUrl.split("list=").toTypedArray()
            }
            youtubeVideoCode = videoCode[1]
            println("videoCodeConstant--->${youtubeVideoCode}---->${videoCode[1]}")
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {

        printLog("videoCode---->${youtubeVideoCode}--->isSingleVideo${isSingleVideo}")
        if (!wasRestored) {
            if (isSingleVideo) {
                printLog("----2>")
                player?.loadVideo(youtubeVideoCode)
            } else {
                printLog("----1>")
                player?.loadPlaylist(youtubeVideoCode)
            }
            printLog("---->")
            // Hiding player controls
            player?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        val dialog = OptionDialog(this,
            R.mipmap.ic_launcher,
            R.string.app_name,
            p1.toString(),
            positiveBtnText = R.string.ok,
            negativeBtnText = R.string.no,
            positiveClick = {
                finish()
            },
            negativeClick = {
            })
        dialog.show()
    }
}