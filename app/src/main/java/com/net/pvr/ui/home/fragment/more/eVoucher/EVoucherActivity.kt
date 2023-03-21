package com.net.pvr.ui.home.fragment.more.eVoucher

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr.R
import com.net.pvr.databinding.ActivityEvoucherBinding
import com.net.pvr.databinding.EvoucherSearchDialogBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.more.eVoucher.adapter.VoucherListAdapter
import com.net.pvr.ui.home.fragment.more.eVoucher.details.EVoucherDetailsActivity
import com.net.pvr.ui.home.fragment.more.eVoucher.response.VoucherListResponse
import com.net.pvr.ui.home.fragment.more.eVoucher.viewModel.EVoucherViewModel
import com.net.pvr.ui.search.searchHome.adapter.SearchHomeCinemaAdapter
import com.net.pvr.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class EVoucherActivity : AppCompatActivity(), VoucherListAdapter.RecycleViewItemClickListener,
    EVoucherSearchAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityEvoucherBinding? = null
    private var loader: LoaderDialog? = null

    private val authViewModel: EVoucherViewModel by viewModels()
    private var voucherListResponse: ArrayList<VoucherListResponse.Output.Ev> = ArrayList()


    private var searchVoucherAdapter: EVoucherSearchAdapter? = null
    private var voucherListAdapter: VoucherListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvoucherBinding.inflate(layoutInflater, null, false)
        setContentView(binding?.root)

        manageFunction()
    }

    private fun manageFunction() {
        //title
        binding?.include51?.textView108?.text = getString(R.string.vouchers)

        authViewModel.myEVouchers("", "")

        myEVouchers()
        movedNext()
    }

    private fun movedNext() {
//        backPress
        binding?.include51?.imageView58?.setOnClickListener {
            finish()
        }

        binding?.imageView112?.setOnClickListener {

        }

    }


    private fun myEVouchers() {
        authViewModel.userResponseEVoucherLiveData.observe(this@EVoucherActivity) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result) {
                        voucherListResponse = it.data.output.ev
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
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView62?.layoutManager = layoutManager
        voucherListAdapter = VoucherListAdapter(this, output.ev, this)
        binding?.recyclerView62?.adapter = voucherListAdapter

        //search Dialog
        binding?.imageView22?.setOnClickListener {
            searchDialog(output)
        }
    }

    override fun itemClick(ev: VoucherListResponse.Output.Ev) {
        val intent = Intent(this@EVoucherActivity, EVoucherDetailsActivity::class.java)
        intent.putExtra("list",voucherListResponse)
        intent.putExtra("category",ev.voucherCategory)
        intent.putExtra("name", ev.binDiscountName)
        intent.putExtra("endDate", ev.endDate.toString())
        intent.putExtra("image", ev.imageUrl1)
        intent.putExtra("shortDesc", ev.shortDesc)
        startActivity(intent)
    }

    override fun onItemClick(ev : VoucherListResponse.Output.Ev) {
//        val intent = Intent(this@EVoucherActivity, EVoucherDetailsActivity::class.java)
//        intent.putExtra("name", ev.binDiscountName)
//        intent.putExtra("endDate", ev.endDate.toString())
//        intent.putExtra("image", ev.imageUrl1)
//        intent.putExtra("shortDesc", ev.shortDesc)
//        startActivity(intent)
    }

    private fun searchDialog(output: VoucherListResponse.Output) {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        val inflater = LayoutInflater.from(this)
        val bindingSearch = EvoucherSearchDialogBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<FrameLayout> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(bindingSearch.root)
        dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        //Category
        val gridLayout =
            GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        bindingSearch.recyclerView60.layoutManager = LinearLayoutManager(this)
        searchVoucherAdapter= EVoucherSearchAdapter(output.ev, this, this)
        bindingSearch.recyclerView60.layoutManager = gridLayout
        bindingSearch.recyclerView60.adapter = searchVoucherAdapter

//        cancel Hide
        bindingSearch.include55.cancelBtn.hide()

        bindingSearch.include55.editTextTextPersonName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
//                filter(s.toString())
            }
        })


    }

//    private fun filter(text: String) {
//        val filtered: ArrayList<VoucherListResponse.Output.Ev> = java.util.ArrayList()
//        val filtered1: ArrayList<VoucherListResponse.Output.Ev> = java.util.ArrayList()
//        for (item in voucherListResponse!!) {
//            if (item.voucherCategory.lowercase(Locale.getDefault())
//                    .contains(text.lowercase(Locale.getDefault()))
//            ) {
//                filtered.add(item)
//            }
//        }
//        searchVoucherAdapter?.filterVoucherSearchList(filtered1)
//        voucherListAdapter?.filterCinemaList(filtered1)
//
//    }
//

}