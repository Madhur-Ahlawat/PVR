package com.net.pvr1.ui.home.fragment.more.experience

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityExperienceBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.more.experience.adapter.ExperienceAdapter
import com.net.pvr1.ui.home.fragment.more.experience.model.ExperienceResponse
import com.net.pvr1.ui.home.fragment.more.experience.viewModel.ExperienceViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExperienceActivity : AppCompatActivity(),ExperienceAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityExperienceBinding? = null
    private val authViewModel: ExperienceViewModel by viewModels()
    private var loader: LoaderDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExperienceBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        experience()
    }
    private fun experience() {
        authViewModel.liveDataScope.observe(this) {
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
                                finish()
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: ExperienceResponse.Output.Format) {
//        val snapHelper = PagerSnapHelper()
//        snapHelper.attachToRecyclerView(binding?.recyclerView61)
//        val gridLayout =
//            GridLayoutManager(this@ExperienceActivity, 1, GridLayoutManager.HORIZONTAL, false)
//        binding?.recyclerView61?.layoutManager = LinearLayoutManager(this@ExperienceActivity)
//        val adapter = ExperienceAdapter(output, this, this)
//        binding?.recyclerView61?.layoutManager = gridLayout
//        binding?.recyclerView61?.adapter = adapter
    }

    override fun itemClick(comingSoonItem: ExperienceResponse.Output.Format) {

    }

}