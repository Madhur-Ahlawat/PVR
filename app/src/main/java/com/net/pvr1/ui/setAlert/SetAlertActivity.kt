package com.net.pvr1.ui.setAlert

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySetAlertBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr1.ui.setAlert.adapter.AlertTheaterAdapter
import com.net.pvr1.ui.setAlert.viewModel.SetAlertViewModel
import com.net.pvr1.ui.watchList.WatchListActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetAlertActivity : AppCompatActivity(), AlertTheaterAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivitySetAlertBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: SetAlertViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlertBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        authViewModel.allTheater("Chennai", "", "", preferences.getUserId().toString(), "")
        allTheater()
        movedNext()

//        alert_dialog
    }

    private fun movedNext() {
        binding?.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                authViewModel.allTheater(
                    "Chennai",
                    "",
                    "",
                    preferences.getUserId().toString(),
                    s.toString()
                )
            }
        })

        binding?.include14?.textView5?.setOnClickListener {
            startActivity(Intent(this@SetAlertActivity, WatchListActivity::class.java))
        }
    }

    private fun allTheater() {
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
                            positiveClick = {},
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: BookingRetrievalResponse.Output) {
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView32?.layoutManager = LinearLayoutManager(this)
        val adapter = AlertTheaterAdapter(output.c, this)
        binding?.recyclerView32?.layoutManager = gridLayout
        binding?.recyclerView32?.adapter = adapter
    }

    override fun dateClick(comingSoonItem: BookingRetrievalResponse.Output.C) {

    }

}