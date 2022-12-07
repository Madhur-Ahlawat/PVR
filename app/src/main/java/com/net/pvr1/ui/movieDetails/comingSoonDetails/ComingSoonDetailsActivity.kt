package com.net.pvr1.ui.movieDetails.comingSoonDetails

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.net.pvr1.BuildConfig
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityComingSoonDetailsBinding
import com.net.pvr1.ui.bookingSession.BookingActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.home.adapter.PromotionAdapter
import com.net.pvr1.ui.movieDetails.comingSoonDetails.viewModels.ComingSoonDetailsViewModel
import com.net.pvr1.ui.movieDetails.nowShowing.adapter.*
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.ui.setAlert.SetAlertActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ComingSoonDetailsActivity : AppCompatActivity(),
    CastAdapter.RecycleViewItemClickListener,
    CrewAdapter.RecycleViewItemClickListener,
    MusicVideoAdapter.RecycleViewItemClickListener,
    TrailerAdapter.RecycleViewItemClickListener,
    TrailerTrsAdapter.RecycleViewItemClickListener,
    MusicVideoTrsAdapter.RecycleViewItemClickListener {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityComingSoonDetailsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ComingSoonDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComingSoonDetailsBinding.inflate(layoutInflater, null, false)
        setContentView(binding?.root)
        movieDetails()
        movieAlert()

        authViewModel.movieDetails(
            "UP",
            preferences.getUserId().toString(),
            "Delhi-NCR",
            intent.getStringExtra("mid").toString()
        )
    }


    private fun movieDetails() {
        authViewModel.movieDetailsLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        authViewModel.movieAlert(
                            preferences.getUserId().toString(),
                            "Delhi-NCR",
                            intent.getStringExtra("mid").toString(),
                            Constant().getDeviceId(this)
                        )
                        retrieveData(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun movieAlert() {
        authViewModel.movieAlertLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
//                        retrieveData(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: MovieDetailsResponse.Output) {
        //Promotion
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding?.recyclerView37)
        val gridLayoutSlider =
            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerView37?.layoutManager = gridLayoutSlider
        binding?.recyclerView37?.adapter =
            Constant.PlaceHolder?.ph?.let { PromotionAdapter(this, it) }

        //Image
        Glide.with(this)
            .load(output.wit)
            .error(R.drawable.app_icon)
            .into(binding?.imageView26!!)

        if (output.mb != null && output.mb.name != null) {
            //Cast
            if (output.mb.cast != null) {
                val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val castAdapter = CastAdapter(output.mb.cast, this, this)
                binding?.recyclerView4?.layoutManager = layoutManager
                binding?.recyclerView4?.adapter = castAdapter
            }
            //Crew
            if (output.mb.crew.isNotEmpty()) {
                val layoutManagerCrew =
                    GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val crewAdapter = CrewAdapter(output.mb.crew, this, this)
                binding?.recyclerCrew?.layoutManager = layoutManagerCrew
                binding?.recyclerCrew?.adapter = crewAdapter
            }
            //trailer
            if (output.trs.isNotEmpty()) {
                binding?.textView69?.show()
                val layoutManagerTrailer =
                    GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val trailerAdapter = TrailerTrsAdapter(output.trs, this, this)
                binding?.recyclerView5?.layoutManager = layoutManagerTrailer
                binding?.recyclerView5?.adapter = trailerAdapter

            } else {

                if (output.mb.videos.isNotEmpty()) {
                    binding?.textView69?.show()
                    val layoutManagerTrailer =
                        GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                    val trailerAdapter = TrailerAdapter(output.mb.videos, this, this)
                    binding?.recyclerView5?.layoutManager = layoutManagerTrailer
                    binding?.recyclerView5?.adapter = trailerAdapter
                } else {
                    binding?.textView69?.hide()
                }
            }
            //MusicVideo
            if (output.trs.isNotEmpty()) {
                toast("1")
                binding?.textView70?.show()
                val layoutManagerTrailer =
                    GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val trailerAdapter = MusicVideoTrsAdapter(output.trs, this, this)
                binding?.recyclerMusic?.layoutManager = layoutManagerTrailer
                binding?.recyclerMusic?.adapter = trailerAdapter

            } else {
                if (output.mb.tracks.isNotEmpty()) {
                    binding?.textView70?.show()
                    val layoutManagerTrailer =
                        GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                    val trailerAdapter = MusicVideoAdapter(output.mb.tracks[0].roles, this, this)
                    binding?.recyclerMusic?.layoutManager = layoutManagerTrailer
                    binding?.recyclerMusic?.adapter = trailerAdapter
                } else {
                    binding?.textView70?.hide()

                }
            }
        }
        //Trailer
        if (output.t.isNullOrEmpty()) {
            binding?.imageView29?.hide()
        } else {
            binding?.imageView29?.show()
        }
        

        binding?.imageView29?.setOnClickListener {
            val intent = Intent(this@ComingSoonDetailsActivity, PlayerActivity::class.java)
            intent.putExtra("trailerUrl", output.t)
            startActivity(intent)
        }
        //Share
        binding?.imageView28?.setOnClickListener {
            val appPackageName = BuildConfig.APPLICATION_ID
            val appName = getString(R.string.app_name)
            val shareBodyText = "https://play.google.com/store/apps/details?id=$appPackageName"

            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, appName)
                putExtra(Intent.EXTRA_TEXT, shareBodyText)
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }
        //Back
        binding?.imageView27?.setOnClickListener {
            finish()
        }
        //Title
        binding?.textView55?.text = output.n
        //Genre
        binding?.textView56?.text = output.genre
        //Release Data
        binding?.textView58?.text = output.mopeningdate
        //Language
        val commaSeparatedString = output.lngs.joinToString { it }
        binding?.textView60?.text = commaSeparatedString
        //Description
        binding?.textView66?.text = output.p
        binding?.textView66?.let { Constant().makeTextViewResizable(it, 4, "See More", true) }
        //FilmType
        binding?.textView75?.text = output.tag
        //Language
        binding?.textView76?.text = output.lng
        //ColorInfo
        binding?.textView77?.text = output.sm
        //SoundMix
        binding?.textView79?.text = output.p
        //MovedNext
        binding?.include?.textView5?.setOnClickListener {
            val intent = Intent(this@ComingSoonDetailsActivity, BookingActivity::class.java)
            intent.putExtra("mid", output.id)
            startActivity(intent)
        }


        //movie Alert
        binding?.constraintLayout89?.setOnClickListener {
            startActivity(Intent(this@ComingSoonDetailsActivity, SetAlertActivity::class.java))
        }
    }

    override fun castClick(comingSoonItem: MovieDetailsResponse.Mb.Cast) {

    }

    override fun crewClick(comingSoonItem: MovieDetailsResponse.Mb.Crew) {

    }

    override fun musicVideo(comingSoonItem: MovieDetailsResponse.Mb.Crew.Role) {
    }

    override fun trailerClick(comingSoonItem: MovieDetailsResponse.Mb.Video) {

    }

    override fun trailerTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {

    }

    override fun musicVideoTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {

    }

}