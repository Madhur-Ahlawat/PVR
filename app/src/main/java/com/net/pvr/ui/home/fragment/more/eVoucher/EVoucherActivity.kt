package com.net.pvr.ui.home.fragment.more.eVoucher

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr.R
import com.net.pvr.databinding.ActivityEvoucherBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.more.eVoucher.adapter.VoucherListAdapter
import com.net.pvr.ui.home.fragment.more.eVoucher.details.EVoucherDetailsActivity
import com.net.pvr.ui.home.fragment.more.eVoucher.response.VoucherListResponse
import com.net.pvr.ui.home.fragment.more.eVoucher.viewModel.EVoucherViewModel
import com.net.pvr.utils.Constant
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EVoucherActivity : AppCompatActivity(),VoucherListAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityEvoucherBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: EVoucherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvoucherBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        authViewModel.myVouchers(preferences.getUserId())

        //title
        binding?.include51?.textView108?.text = getString(R.string.vouchers)
        vouchers()
        movedNext()
    }

    private fun movedNext() {
//        backPress
        binding?.include51?.imageView58?.setOnClickListener {
            finish()
        }

        binding?.imageView112?.setOnClickListener {
            val intent = Intent(this@EVoucherActivity, EVoucherDetailsActivity::class.java)
            startActivity(intent)
        }
    }
    private fun vouchers() {
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

    private fun retrieveData(output: VoucherListResponse.Output) {
        val layoutManager = LinearLayoutManager(this)
        binding?.recyclerView62?.layoutManager = layoutManager
        val termsAdapter = VoucherListAdapter(this, output.ev, this)
        binding?.recyclerView62?.adapter = termsAdapter
    }

    override fun itemClick(ev: VoucherListResponse.Output.Ev) {

    }

}