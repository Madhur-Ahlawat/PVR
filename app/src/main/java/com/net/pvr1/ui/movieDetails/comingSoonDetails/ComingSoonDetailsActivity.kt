package com.net.pvr1.ui.movieDetails.comingSoonDetails

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityComingSoonDetailsBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.movieDetails.comingSoonDetails.adapter.ComDetailsPhAdapter
import com.net.pvr1.ui.movieDetails.comingSoonDetails.setAlert.SetAlertActivity
import com.net.pvr1.ui.movieDetails.comingSoonDetails.viewModels.ComingSoonDetailsViewModel
import com.net.pvr1.ui.movieDetails.nowShowing.adapter.*
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.ui.watchList.WatchListActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ComingSoonDetailsActivity : AppCompatActivity(),
    MusicVideoAdapter.RecycleViewItemClickListener,
    TrailerAdapter.RecycleViewItemClickListener,
    TrailerTrsAdapter.RecycleViewItemClickListener,
    MusicVideoTrsAdapter.RecycleViewItemClickListener {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityComingSoonDetailsBinding? = null
    private var loader: LoaderDialog? = null
    private  var cinemaId=""
    private val authViewModel: ComingSoonDetailsViewModel by viewModels()


    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComingSoonDetailsBinding.inflate(layoutInflater, null, false)
        setContentView(binding?.root)
        manageFunction()
    }

    private fun manageFunction() {

        authViewModel.movieDetails(
            "UP",
            preferences.getUserId(),
            preferences.getCityName(),
            intent.getStringExtra("mid").toString()
        )

        cinemaId= intent.getStringExtra("mid").toString()


        //internet Check
        broadcastReceiver = NetworkReceiver()
        broadcastIntent()

        Constant().appBarHide(this)
        movieDetails()
        movieAlert()
    }


    private fun movieDetails() {
        authViewModel.movieDetailsLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        authViewModel.movieAlert(
                            preferences.getUserId(),
                            preferences.getCityName(),
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
                    loader = LoaderDialog(R.string.pleaseWait)
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
                    retrieveAlertData(it.data.output)
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }
    }


    private fun retrieveAlertData(output: MovieDetailsResponse.Output) {
        binding?.subscribe?.setOnClickListener {
            val intent = Intent(this@ComingSoonDetailsActivity, WatchListActivity::class.java)
            startActivity(intent)
        }

        //movie Alert
        binding?.constraintLayout89?.setOnClickListener {
            val intent = Intent(this@ComingSoonDetailsActivity, SetAlertActivity::class.java)
            intent.putExtra("cid",cinemaId)
            startActivity(intent)
        }

        if (output!=null) {
            binding?.wishlist?.show()
            binding?.ivWishlist?.show()
            binding?.ivWishlist?.show()
            binding?.constraintLayout89?.hide()
            binding?.subscribe?.show()
            binding?.ivWishlist?.background =
                ContextCompat.getDrawable(this, R.drawable.ic_wishlist_yellow)
        } else {
            binding?.wishlist?.show()
            binding?.ivWishlist?.show()
            binding?.constraintLayout89?.show()
            binding?.subscribe?.hide()
            binding?.ivWishlist?.background =
                ContextCompat.getDrawable(this, R.drawable.ic_wishlist_white)
        }

    }

    private fun retrieveData(output: MovieDetailsResponse.Output) {
        //shimmer
        binding?.constraintLayout148?.hide()
        //uiShow
        binding?.nestedScrollView?.show()

        //Promotion
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding?.include40?.recyclerPromotion)
        val gridLayoutSlider =
            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.include40?.recyclerPromotion?.layoutManager = gridLayoutSlider
        val adapter = ComDetailsPhAdapter(this, output.ph)
        binding?.include40?.recyclerPromotion?.adapter = adapter

        //Image
        Glide.with(this)
            .load(output.wit)
            .error(R.drawable.app_icon)
            .into(binding?.imageView26!!)

        if (output.mb != null && output.mb.name != null) {

            //Cast
            if (output.mb.cast.isNotEmpty()) {
                binding?.cast?.show()
                binding?.textView67?.show()
                val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val castAdapter = CastAdapter(output.mb.cast, this)
                binding?.recyclerView4?.layoutManager = layoutManager
                binding?.recyclerView4?.adapter = castAdapter
            } else {
                binding?.cast?.hide()
            }

            //Crew
            if (output.mb.crew.isNotEmpty()) {
                binding?.crew?.show()
                binding?.textView68?.show()
                val layoutManagerCrew =
                    GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val crewAdapter = CrewAdapter(getUpdatedList(output.mb.crew), this)
                binding?.recyclerCrew?.layoutManager = layoutManagerCrew
                binding?.recyclerCrew?.adapter = crewAdapter
            } else {
                binding?.crew?.hide()
            }

            //condition check for Trailer and Music video
            val trailerList: ArrayList<MovieDetailsResponse.Trs> = ArrayList()
            val musicVideoList: ArrayList<MovieDetailsResponse.Trs> = ArrayList()

            for (item in output.trs) {
                if (item.ty == "MUSIC") {
                    musicVideoList.addAll(output.trs)
                } else {
                    trailerList.addAll(output.trs)
                }
            }

            //trailer
            if (trailerList.size != 0) {
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(binding?.recyclerView5)
                val layoutManagerTrailer =
                    GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val trailerAdapter = TrailerTrsAdapter(trailerList, this, this)
                binding?.recyclerView5?.layoutManager = layoutManagerTrailer
                binding?.recyclerView5?.adapter = trailerAdapter
            } else {
                if (output.mb.videos.isNotEmpty()) {
                    val snapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(binding?.recyclerView5)
                    binding?.trailer?.show()
                    binding?.textView69?.show()
                    val layoutManagerTrailer =
                        GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                    val trailerAdapter = TrailerAdapter(output.mb.videos, this, this)
                    binding?.recyclerView5?.layoutManager = layoutManagerTrailer
                    binding?.recyclerView5?.adapter = trailerAdapter
                } else {
                    binding?.trailer?.hide()
                }
            }

        //MusicVideo
            if (musicVideoList.size != 0) {
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(binding?.recyclerMusic)
                binding?.musicVideo?.show()
                binding?.textView70?.show()
                val layoutManagerTrailer =
                    GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val trailerAdapter = MusicVideoTrsAdapter(musicVideoList, this, this)
                binding?.recyclerMusic?.layoutManager = layoutManagerTrailer
                binding?.recyclerMusic?.adapter = trailerAdapter
            } else {
                if (output.mb != null && output.mb.name != null) {
                    if (output.mb.tracks.isNotEmpty()) {
                        val snapHelper = PagerSnapHelper()
                        snapHelper.attachToRecyclerView(binding?.recyclerMusic)
                        binding?.textView70?.show()
                        binding?.musicVideo?.show()
                        val layoutManagerTrailer =
                            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                        val trailerAdapter = MusicVideoAdapter(output.mb.tracks[0].roles, this, this)
                        binding?.recyclerMusic?.layoutManager = layoutManagerTrailer
                        binding?.recyclerMusic?.adapter = trailerAdapter
                    } else {
                        binding?.musicVideo?.hide()
                    }
                }
            }

        }

        //Trailer
        if (output.t.isEmpty()) {
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
            Constant().shareData(this,"","")
        }

        //Back
        binding?.imageView27?.setOnClickListener {
            finish()
        }

        //Title
        binding?.textView55?.text = output.n
        //Genre
        binding?.textView56?.text = output.othergenres
        //Release Data
        binding?.textView58?.text = output.mopeningdate
        //Language
        val commaSeparatedString = output.lngs.joinToString { it }
        binding?.textView60?.text = commaSeparatedString
        //Description
        binding?.textView66?.text = output.p
        if (binding?.textView66?.lineCount!! > 2) {
            Constant().makeTextViewResizable(binding?.textView66!!, 4, "Read More", true)
        }

        //FilmType
        binding?.textView75?.text = output.tag
        //Language
        binding?.textView76?.text = output.lng
        //ColorInfo
        binding?.textView77?.text = output.sm
        //SoundMix
        binding?.textView79?.text = output.p

    }

    override fun musicVideo(comingSoonItem: MovieDetailsResponse.Mb.Crew.Role) {
        val intent = Intent(this@ComingSoonDetailsActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.url)
        startActivity(intent)

    }

    override fun trailerClick(comingSoonItem: MovieDetailsResponse.Mb.Video) {
        val intent = Intent(this@ComingSoonDetailsActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.url)
        startActivity(intent)
    }

    override fun trailerTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {
        val intent = Intent(this@ComingSoonDetailsActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.t)
        startActivity(intent)
    }

    override fun musicVideoTrsClick(comingSoonItem: MovieDetailsResponse.Trs) {
        val intent = Intent(this@ComingSoonDetailsActivity, PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.t)
        startActivity(intent)
    }

    //Internet Check
    private fun broadcastIntent() {
        registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun getUpdatedList(crew: ArrayList<MovieDetailsResponse.Mb.Crew>): List<MovieDetailsResponse.Mb.Crew.Role> {
        val list = ArrayList<MovieDetailsResponse.Mb.Crew.Role>()
        val listRole = ArrayList<MovieDetailsResponse.Mb.Crew.Role>()
        val listSupport = ArrayList<MovieDetailsResponse.Mb.Crew.Role>()
        val newList = ArrayList<MovieDetailsResponse.Mb.Crew.Role>()

        for (data in crew){
            list.addAll(data.roles)
        }
        for (data in listRole){
            if (data.role == "Director") {
                listRole.add(data)
            }else{
                listSupport.add(data)
            }
        }

        newList.addAll(listRole)
        newList.addAll(listSupport)



        return newList
    }

}