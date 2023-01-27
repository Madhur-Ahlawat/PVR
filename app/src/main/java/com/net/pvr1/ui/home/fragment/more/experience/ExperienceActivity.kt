package com.net.pvr1.ui.home.fragment.more.experience

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityExperienceBinding
import com.net.pvr1.databinding.ExperienceDetailsDialogBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.formats.FormatsActivity
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.home.fragment.more.experience.adapter.ExperienceAdapter
import com.net.pvr1.ui.home.fragment.more.experience.response.ExperienceDetailsResponse
import com.net.pvr1.ui.home.fragment.more.experience.response.ExperienceResponse
import com.net.pvr1.ui.home.fragment.more.experience.viewModel.ExperienceViewModel
import com.net.pvr1.ui.ticketConfirmation.adapter.TicketPlaceHolderAdapter
import com.net.pvr1.ui.webView.WebViewActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExperienceActivity : AppCompatActivity(), ExperienceAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityExperienceBinding? = null
    private val authViewModel: ExperienceViewModel by viewModels()
    private var loader: LoaderDialog? = null

    private var dialog: BottomSheetDialog? = null

    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExperienceBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        experience()
        experienceDetails()
        movedNext()
        getShimmerData()
        broadcastIntent()

        //internet Check
        broadcastReceiver = NetworkReceiver()

        authViewModel.experience(preferences.getCityName())
    }
    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    private fun movedNext() {
//        Back Click
        binding?.include45?.imageView58?.setOnClickListener {
            finish()
        }
//        title
        binding?.include45?.textView108?.text = getString(R.string.experience)
    }

    private fun experience() {
        authViewModel.liveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                                finish()
                            },
                            negativeClick = {})
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
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun experienceDetails() {
        authViewModel.experienceLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveDetailsData(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                                finish()
                            },
                            negativeClick = {})
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
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveDetailsData(output: ExperienceDetailsResponse.Output) {
        experienceDetails(output)
    }

    private fun retrieveData(output: ExperienceResponse.Output) {
//        shimmer
        binding?.constraintLayout145?.hide()

//        list
        binding?.recyclerView61?.show()

        val gridLayout =
            GridLayoutManager(this@ExperienceActivity, 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView61?.layoutManager = LinearLayoutManager(this@ExperienceActivity)
        val adapter = ExperienceAdapter(output.formats, this, this)
        binding?.recyclerView61?.layoutManager = gridLayout
        binding?.recyclerView61?.adapter = adapter
    }

    override fun itemPlayerClick(comingSoonItem: ExperienceResponse.Output.Format) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("title", comingSoonItem.name)
        intent.putExtra("from", "Experience")
        intent.putExtra("getUrl", comingSoonItem.rurl)
        startActivity(intent)
    }

    override fun itemClick(comingSoonItem: ExperienceResponse.Output.Format) {
        authViewModel.experienceDetails(preferences.getCityName(), comingSoonItem.type)
    }

    private fun experienceDetails(comingSoonItem: ExperienceDetailsResponse.Output) {
        dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val inflater = LayoutInflater.from(this)
        val bindingBottom = ExperienceDetailsDialogBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<FrameLayout>? = dialog?.behavior
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        dialog?.setContentView(bindingBottom.root)

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.show()


        //Image View
        Glide.with(this).load(comingSoonItem.format.logoUrl)
            .error(R.drawable.placeholder_horizental).into(bindingBottom.imageView173)

        //title
        bindingBottom.textView391.text = comingSoonItem.format.text

        //desc
        bindingBottom.textView392.text = comingSoonItem.format.description


        if (comingSoonItem.book) {
            bindingBottom.textView5.show()
        } else {
            bindingBottom.textView5.hide()
        }

        bindingBottom.textView5.setOnClickListener {
            val intent = Intent(this, FormatsActivity::class.java)
            intent.putExtra("format", comingSoonItem.format.name)
            startActivity(intent)
        }
        //Promotion
        if (comingSoonItem.ph.isNotEmpty()) updatePH(comingSoonItem.ph, bindingBottom)

    }

    private fun updatePH(
        phd: ArrayList<HomeResponse.Ph>?, bindingBottom: ExperienceDetailsDialogBinding
    ) {
        if (phd != null && phd.size > 0) {
            bindingBottom.include46.placeHolderView.show()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            bindingBottom.include46.recyclerPromotion.layoutManager = layoutManager
            bindingBottom.include46.recyclerPromotion.onFlingListener = null
            snapHelper.attachToRecyclerView(bindingBottom.include46.recyclerPromotion)
            bindingBottom.include46.recyclerPromotion.layoutManager = layoutManager
            val adapter = TicketPlaceHolderAdapter(this, phd)
            bindingBottom.include46.recyclerPromotion.adapter = adapter
            if (phd.size > 1) {
                val speedScroll = 5000
                val handler = Handler()
                val runnable: Runnable = object : Runnable {
                    var count = 0
                    var flag = true
                    override fun run() {
                        if (count < adapter.itemCount) {
                            if (count == adapter.itemCount - 1) {
                                flag = false
                            } else if (count == 0) {
                                flag = true
                            }
                            if (flag) count++ else count--
                            bindingBottom.include46.recyclerPromotion.smoothScrollToPosition(
                                count
                            )
                            handler.postDelayed(this, speedScroll.toLong())
                        }
                    }
                }
                handler.postDelayed(runnable, speedScroll.toLong())
            }
        } else {
            bindingBottom.include46.placeHolderView.hide()
        }
    }

    //Internet Check
    private fun broadcastIntent() {
        registerReceiver(
            broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }


}