package com.net.pvr1.ui.offer

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityOfferBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.offer.adapter.OfferAdapter
import com.net.pvr1.ui.offer.offerDetails.OfferDetailsActivity
import com.net.pvr1.ui.offer.response.OfferResponse
import com.net.pvr1.ui.offer.viewModel.OfferViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import com.net.pvr1.utils.printLog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OfferActivity : AppCompatActivity(),OfferAdapter.Direction {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityOfferBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: OfferViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        offerDataLoad()
        binding?.include4?.textView108?.text=getString(R.string.offers)
        authViewModel.offer(Constant().getDeviceId(this))

    }

    private fun offerDataLoad() {
        authViewModel.userResponseLiveData.observe(this) {
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: List<OfferResponse.Output>) {
        printLog("Details--->${output}")
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val comingSoonMovieAdapter = OfferAdapter(output, this, this)
        binding?.recyclerView?.layoutManager = gridLayout
        binding?.recyclerView?.adapter = comingSoonMovieAdapter
    }

    override fun offerClick(comingSoonItem: OfferResponse.Output) {
        val intent = Intent(this@OfferActivity, OfferDetailsActivity::class.java)
        intent.putExtra("id",comingSoonItem.id)
        startActivity(intent)
    }

}