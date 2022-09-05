package com.net.pvr1.ui.home.fragment.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentHomeBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.home.fragment.home.adapter.*
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr1.ui.movieDetails.MovieDetailsActivity
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.ui.selectCity.SelectCityActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.Constant.Companion.select_pos
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import java.util.*
import kotlin.math.abs


class HomeFragment : Fragment(), HomeCinemaCategoryAdapter.RecycleViewItemClickListener,
    HomeSliderAdapter.RecycleViewItemClickListener,
    HomePromotionAdapter.RecycleViewItemClickListener,
    HomeMoviesAdapter.RecycleViewItemClickListener,
    HomeTrailerAdapter.RecycleViewItemClickListener {

    private var binding: FragmentHomeBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel by activityViewModels<HomeViewModel>()
    private lateinit var preferences: AppPreferences

    private var currentPage = 0
    private var timer: Timer? = null
    private val delayMS: Long = 3000 //delay in milliseconds before task is to be executed
    private val periodMS: Long = 3000 // time in milliseconds between successive task executions.


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = AppPreferences()
        authViewModel.home("Delhi-NCR", "", "0", "0", true, "no", "", "ALL", "ALL", "ALL", "no")
        (requireActivity().findViewById(R.id.notify) as ImageView).show()
        (requireActivity().findViewById(R.id.locationBtn) as ImageView).show()
        (requireActivity().findViewById(R.id.textView2) as TextView).show()
        (requireActivity().findViewById(R.id.subTitle) as TextView).show()
        (requireActivity().findViewById(R.id.txtCity) as TextView).show()
        (requireActivity().findViewById(R.id.txtCity) as TextView).setOnClickListener {
            val intent = Intent(requireActivity(), SelectCityActivity::class.java)
            startActivity(intent)
        }

        movedNext()
        homeApi()
    }

    private fun movedNext() {

        binding?.txtTrailers?.setOnClickListener {
            binding?.txtTrailers?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            binding?.txtMusic?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.textColorGray))
            binding?.view33?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow))
            binding?.view34?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))

            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1)
            binding?.view34?.layoutParams = layoutParams

            val layoutParams2 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 2)
            binding?.view33?.layoutParams = layoutParams2

        }

        binding?.txtMusic?.setOnClickListener {
            binding?.txtMusic?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            binding?.txtTrailers?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.textColorGray))
            binding?.view33?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))
            binding?.view34?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow))

            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1)
            binding?.view33?.layoutParams = layoutParams

            val layoutParams2 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 2)
            binding?.view34?.layoutParams = layoutParams2

        }

    }

    private fun homeApi() {
        authViewModel.userResponseLiveData.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output)
                    } else {
                        val dialog = OptionDialog(requireActivity(),
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
                    val dialog = OptionDialog(requireContext(),
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
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: HomeResponse.Output) {
        //Category
        val gridLayout =
            GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerCinemaCat?.layoutManager = LinearLayoutManager(context)
        val adapter = HomeCinemaCategoryAdapter(requireActivity(), output.mfi, this)
        binding?.recyclerCinemaCat?.layoutManager = gridLayout
        binding?.recyclerCinemaCat?.adapter = adapter

        //Slider
        val gridLayoutSlider =
            GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerViewSlider?.layoutManager = LinearLayoutManager(context)
        val adapterSlider = HomeSliderAdapter(requireActivity(), output.mv, this)
        binding?.recyclerViewSlider?.layoutManager = gridLayoutSlider
        binding?.recyclerViewSlider?.adapter = adapterSlider

        //Promotion
        binding?.recyclerPromotion?.adapter =
            HomePromotionPagerAdapter(requireActivity(), output.mfi, binding?.recyclerPromotion)
//        binding?.promotionPosition?.setupWithViewPager(binding?.recyclerPromotion, true)
        /*After setting the adapter use the timer */
        val pagerLength = output.mv.size
//
//        val runnable = Runnable {
//
//            if (currentPage == pagerLength) {
//                currentPage = 0
//            }
//            binding?.recyclerPromotion?.setCurrentItem(currentPage++, true)
//        }
//        val handler = Handler(Looper.getMainLooper())
//        handler.postDelayed(runnable, 3000)

//        val handler = Handler()
//        val update = Runnable {
//            if (currentPage == pagerLength) {
//                currentPage = 0
//            }
//            binding?.recyclerPromotion?.setCurrentItem(currentPage++, true)
//        }

//        timer = Timer()
//        timer!!.schedule(object : TimerTask() {
//            override fun run() {
//                handler.post(runnable)
//            }
//        }, delayMS, periodMS)

        binding?.recyclerPromotion?.offscreenPageLimit = 3
        binding?.recyclerPromotion?.clipChildren = false
        binding?.recyclerPromotion?.clipToPadding = false
        binding?.recyclerPromotion?.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val transfer = CompositePageTransformer()

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx


        transfer.addTransformer(MarginPageTransformer(40))
        transfer.addTransformer(object : com.github.islamkhsh.viewpager2.ViewPager2.PageTransformer,
            ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                // println("rowIndex---->1-$position")
                page.translationX = -pageTranslationX * position

                select_pos = position.toInt()
                val r = 1 - abs(position)
                page.scaleY = (0.85f + r * 0.14f)
            }

        })
        binding?.recyclerPromotion?.setPageTransformer(transfer)

        binding?.recyclerPromotion?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

        })

        //Movies
        val gridLayoutMovies =
            GridLayoutManager(context, 2)
        binding?.recyclerMovies?.layoutManager = LinearLayoutManager(context)
        val adapterMovies = HomeMoviesAdapter(requireActivity(), output.mv, this)
        binding?.recyclerMovies?.layoutManager = gridLayoutMovies
        binding?.recyclerMovies?.adapter = adapterMovies

        //Trailer
        val gridLayoutTrailer =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerTrailer?.layoutManager = LinearLayoutManager(context)
        val adapterTrailer = HomeTrailerAdapter(requireActivity(), output.mv, this)
        binding?.recyclerTrailer?.layoutManager = gridLayoutTrailer
        binding?.recyclerTrailer?.adapter = adapterTrailer

    }

    override fun onCategoryClick(comingSoonItem: HomeResponse.Mfi) {
        val intent = Intent(requireActivity(), MovieDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.name)
        startActivity(intent)
    }

    override fun onSliderBookClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), MovieDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun onPromotionClick(comingSoonItem: HomeResponse.Mfi) {
        val intent = Intent(requireActivity(), MovieDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.name)
        startActivity(intent)
    }

    override fun onMoviesClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), MovieDetailsActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun onTrailerClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.mtrailerurl)
        startActivity(intent)
    }


}