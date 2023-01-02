package com.net.pvr1.ui.formats

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityFormatsBinding
import com.net.pvr1.ui.bookingSession.BookingActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.formats.adapter.FormatCategoryAdapter
import com.net.pvr1.ui.formats.adapter.FormatMoviesAdapter
import com.net.pvr1.ui.formats.response.FormatResponse
import com.net.pvr1.ui.formats.viewModel.FormatsViewModel
import com.net.pvr1.ui.movieDetails.nowShowing.NowShowingActivity
import com.net.pvr1.ui.webView.WebViewActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormatsActivity : AppCompatActivity() ,FormatCategoryAdapter.RecycleViewItemClickListener,FormatMoviesAdapter.RecycleViewItemClickListener{
    private var binding: ActivityFormatsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: FormatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormatsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        //title
        binding?.toolbar?.textView108?.text=intent.getStringExtra("format").toString()

        authViewModel.formats(intent.getStringExtra("format").toString(), "Delhi-NCR", "no")
        formats()
    }

    private fun formats() {
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

    private fun retrieveData(output: FormatResponse.Output) {
        //category

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding?.recyclerView39)
        val gridLayout =
            GridLayoutManager(this@FormatsActivity, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerView39?.layoutManager = LinearLayoutManager(this@FormatsActivity)
        val adapter = FormatCategoryAdapter(output.ph, this, this)
        binding?.recyclerView39?.layoutManager = gridLayout
        binding?.recyclerView39?.adapter = adapter

        //movie
        val gridLayout2 =
            GridLayoutManager(this@FormatsActivity, 2, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView40?.layoutManager = LinearLayoutManager(this@FormatsActivity)
        val adapter2 = FormatMoviesAdapter(this, output.cinemas.m, this)
        binding?.recyclerView40?.layoutManager = gridLayout2
        binding?.recyclerView40?.adapter = adapter2
    }


    override fun onMoviesClick(comingSoonItem: FormatResponse.Output.Cinemas.M) {
        val intent = Intent(this, NowShowingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun onBookClick(comingSoonItem: FormatResponse.Output.Cinemas.M) {
        val intent = Intent(this, BookingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.id)
        startActivity(intent)
    }

    override fun categoryClick(comingSoonItem: FormatResponse.Output.Ph) {
        if (comingSoonItem.redirect_url!="") {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("title", comingSoonItem.name)
            intent.putExtra("from", "format")
            intent.putExtra("getUrl", comingSoonItem.redirect_url.toString())
            startActivity(intent)
        }
    }

}