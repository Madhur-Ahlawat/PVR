package com.net.pvr1.ui.home.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentHomeBinding
import com.net.pvr1.ui.bookingSession.BookingActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.formats.FormatsActivity
import com.net.pvr1.ui.home.fragment.home.adapter.*
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr1.ui.movieDetails.nowShowing.NowShowingActivity
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.ui.search.searchHome.SearchHomeActivity
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.PlaceHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeCinemaCategoryAdapter.RecycleViewItemClickListener,
    HomeSliderAdapter.RecycleViewItemClickListener,
    HomePromotionAdapter.RecycleViewItemClickListener,
    HomeMoviesAdapter.RecycleViewItemClickListener,
    HomeTrailerAdapter.RecycleViewItemClickListener {

    private var binding: FragmentHomeBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel by activityViewModels<HomeViewModel>()

    @Inject
    lateinit var preferences: PreferenceManager

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
        (requireActivity().findViewById(R.id.include) as ConstraintLayout).show()
        (requireActivity().findViewById(R.id.notify) as ImageView).show()
        (requireActivity().findViewById(R.id.locationBtn) as ImageView).show()
        (requireActivity().findViewById(R.id.scanQr) as ImageView).show()
        (requireActivity().findViewById(R.id.searchBtn) as ImageView).show()
        (requireActivity().findViewById(R.id.searchCinema) as ImageView).hide()
        (requireActivity().findViewById(R.id.searchBtn) as ImageView).setOnClickListener {
            if (isAdded) {
                val intent = Intent(requireActivity(), SearchHomeActivity::class.java)
                startActivity(intent)
            }
        }
        authViewModel.home(
            preferences.getCityName(),
            "",
            preferences.getUserId(),
            preferences.geMobileNumber(),
            true,
            "no",
            "",
            "ALL",
            "ALL",
            "ALL",
            "no"
        )
        movedNext()
        homeApi()
    }

    private fun movedNext() {
        binding?.txtTrailers?.setOnClickListener {
            binding?.txtTrailers?.setTextColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.black
                )
            )

            binding?.txtMusic?.setTextColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.textColorGray
                )
            )

            binding?.view33?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.yellow
                )
            )

            binding?.view34?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.gray
                )
            )

        }

        binding?.txtMusic?.setOnClickListener {
            binding?.txtMusic?.setTextColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.black
                )
            )
            binding?.txtTrailers?.setTextColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.textColorGray
                )
            )
            binding?.view33?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.gray
                )
            )
            binding?.view34?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.yellow
                )
            )

        }

    }

    private fun homeApi() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        loader?.dismiss()
                        retrieveData(it.data.output)
                    } else {
                        val dialog = OptionDialog(requireActivity(),
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
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
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    println("loadingHome--->")
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }


    private fun retrieveData(output: HomeResponse.Output) {
        PlaceHolder = output
        if (isAdded) {
            //Category
            val gridLayout =
                GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
            binding?.recyclerCinemaCat?.layoutManager = LinearLayoutManager(context)
            val adapter = HomeCinemaCategoryAdapter(requireActivity(), output.mfi, this)
            binding?.recyclerCinemaCat?.layoutManager = gridLayout
            binding?.recyclerCinemaCat?.adapter = adapter

        }

        //Slider
        if (isAdded) {

            binding?.recyclerViewSlider?.onFlingListener = null
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding?.recyclerViewSlider)
            val gridLayoutSlider =
                GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
            binding?.recyclerViewSlider?.layoutManager = LinearLayoutManager(context)
            val adapterSlider = HomeSliderAdapter(requireActivity(), output.mv, this)
            binding?.recyclerViewSlider?.layoutManager = gridLayoutSlider
            binding?.recyclerViewSlider?.adapter = adapterSlider

        }

        //Promotion
        val gridLayoutSlider =
            GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerPromotion?.layoutManager = gridLayoutSlider
        binding?.recyclerPromotion?.adapter = PromotionAdapter(requireActivity(), output.ph)


        //Movies
        val gridLayoutMovies = GridLayoutManager(context, 2)
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

//        binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
//        filter_fab.setOnClickListener(View.OnClickListener {
//            if (output != null) {
//                val gFilter = GenericFilterHome()
//                val filterPoints = HashMap<String, ArrayList<String>>()
//                filterPoints[PCConstants.FilterType.LANG_FILTER] = output.getMlng()
//                filterPoints[PCConstants.FilterType.GENERE_FILTER] = output.getMgener()
//                filterPoints[PCConstants.FilterType.FORMAT_FILTER] = ArrayList()
//                filterPoints[PCConstants.FilterType.ACCESSABILITY_FILTER] =
//                    ArrayList(Arrays.asList(*arrayOf("Subtitle")))
//                filterPoints[PCConstants.FilterType.PRICE_FILTER] = ArrayList()
//                filterPoints[PCConstants.FilterType.SHOWTIME_FILTER] = ArrayList()
//                filterPoints[PCConstants.FilterType.CINEMA_FORMAT] = ArrayList()
//                filterPoints[PCConstants.FilterType.SPECIAL_SHOW] = ArrayList()
//                gFilter.openFilters(
//                    context,
//                    "Home",
//                    onButtonSelected,
//                    appliedFilterType,
//                    appliedFilterItem,
//                    filterPoints
//                )
//            }
//        })

    }

    override fun onCategoryClick(comingSoonItem: HomeResponse.Mfi) {
        val intent = Intent(requireActivity(), FormatsActivity::class.java)
        intent.putExtra("format", comingSoonItem.name)
        startActivity(intent)
    }


    override fun onSliderBookClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), NowShowingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun onPromotionClick(comingSoonItem: HomeResponse.Mfi) {
        val intent = Intent(requireActivity(), NowShowingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.name)
        startActivity(intent)
    }

    override fun onMoviesClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), NowShowingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun onBookClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), BookingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)

    }

    override fun onTrailerClick(comingSoonItem: HomeResponse.Mv) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java)
        intent.putExtra("trailerUrl", comingSoonItem.mtrailerurl)
        startActivity(intent)
    }

}