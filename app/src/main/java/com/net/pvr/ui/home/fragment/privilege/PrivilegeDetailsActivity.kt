package com.net.pvr.ui.home.fragment.privilege

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr.R
import com.net.pvr.databinding.ActivityPassportTransBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.privilege.adapter.PrivilegeVochersAdapter
import com.net.pvr.ui.home.fragment.privilege.adapter.TransHistoryAdapter
import com.net.pvr.ui.home.fragment.privilege.response.PassportHistory
import com.net.pvr.ui.home.fragment.privilege.viewModel.PrivilegeLoginViewModel
import com.net.pvr.utils.Constant
import com.net.pvr.utils.NetworkResult
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PrivilegeDetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPassportTransBinding? = null
    private val authViewModel: PrivilegeLoginViewModel by viewModels()
    private var loader: LoaderDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPassportTransBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        if (preferences.getBoolean(Constant.SharedPreference.LOYALITY_CANCEL)
        ) {
            binding?.cancelSubs?.show()
        } else {
            binding?.cancelSubs?.hide()
        }
        authViewModel.passportHistory(preferences.getUserId(),preferences.geMobileNumber())
        passportHistory()
        manageTab()
    }

    private fun manageTab() {
        binding?.details?.setBackgroundResource(R.drawable.bottom_bar_yellow)
        binding?.history?.setBackgroundResource(0)
        binding?.history?.setTextColor(Color.parseColor("#7A7A7A"))
        binding?.details?.setTextColor(Color.parseColor("#000000"))
        binding?.historyList?.hide()
        binding?.topView?.hide()
        binding?.history?.setOnClickListener(View.OnClickListener {
            binding?.history?.setBackgroundResource(R.drawable.bottom_bar_yellow)
            binding?.details?.setBackgroundResource(0)
            binding?.details?.setTextColor(Color.parseColor("#7A7A7A"))
            binding?.history?.setTextColor(Color.parseColor("#000000"))
            binding?.historyList?.show()
            binding?.topView?.hide()
        })
        binding?.details?.setOnClickListener(View.OnClickListener {
            binding?.details?.setBackgroundResource(R.drawable.bottom_bar_yellow)
            binding?.history?.setBackgroundResource(0)
            binding?.history?.setTextColor(Color.parseColor("#7A7A7A"))
            binding?.details?.setTextColor(Color.parseColor("#000000"))
            binding?.historyList?.hide()
            binding?.topView?.show()
        })
    }

    private fun passportHistory() {
        authViewModel.passportHistoryDataLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output != null) {
                            SetData(it.data.output)
                            val voucherText = it.data.output.cd
                            binding?.cancelSubs?.setOnClickListener {
                                showConfirmationDialog(voucherText)
                            }
                            binding?.topView?.show()
                            if (it.data.output.history != null && it.data.output.history.isNotEmpty()) {
                                binding?.llBOOK?.hide()
                                binding?.recyclerView?.show()
                                //tvHis.setVisibility(View.VISIBLE);
                                binding?.tvHis?.text = "Transaction History (" + it.data.output.history.size + ")"
                                val transHistoryAdapter =
                                    TransHistoryAdapter(it.data.output.history, this)
                                binding?.recyclerView?.adapter = transHistoryAdapter
                            } else {
                                binding?.llBOOK?.show()
                                binding?.recyclerView?.hide()
                                binding?.tvHis?.hide()
                            }
                        }
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }

    }
    private fun passportCancel() {
        authViewModel.passportHistoryDataLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }

    }

    private fun SetData(output: PassportHistory.Output) {
        binding?.rlsubsMain?.show()
        // tvVoucherCode.setText(output.getCd());
        binding?.tvVoucherCodeNew?.text = output.cd
        binding?.tvDateNew?.text = output.expiryDate
        if (output.renewDate != null && output.renewDate.isNotEmpty() ) {
//            String time= GetDate("MMM dd yyyy", "dd MMM, yyyy",output.getRenewDate());
            binding?.renewData?.text = output.renewDate
            binding?.renewDataView?.show()
        } else {
            binding?.renewDataView?.hide()
        }
        if (output.purchaseDate != null && output.purchaseDate.isNotEmpty()) {
            binding?.purchaseView?.show()
            //            String time= GetDate("MMM dd yyyy HH:mm a", "dd MMM, yyyy",output.getPurchaseDate());
            binding?.tvVoucherCode?.text = output.purchaseDate
        } else {
            binding?.purchaseView?.hide()
        }
        if (output.purchaseAmount != null && output.purchaseAmount.isNotEmpty()) {
            binding?.subsAmtView?.show()
            binding?.tvVouchertext1?.text = "₹" + output.purchaseAmount
        } else {
            binding?.subsAmtView?.hide()
        }
        binding?.totalVisits?.text = output.redeemed.toString() + "/" + output.can_redeem
        if (!TextUtils.isEmpty(output.qr)) {
            /*   Picasso.with(Subscription_Trans.this)
                        .load(output.getQr())
                        .placeholder(R.drawable.ic_subsqr)
                        .into(ivsubsqr);
*/
            PrivilegeVochersAdapter.generateQrcode(binding?.ivsubsqr!!).execute(
                output.qr
            )
        }
        //            String time= GetDate("MMM,dd yyyy HH:mmaa", "dd MMM, yyyy",output.getExpiryDate());
        binding?.tvDate?.text = output.expiryDate
//            if (output.getRedeemed()<1){
//                tvVoucherCode.setText("0");
//                tvVouchertext.setText(output.getCanRedeem()+"");
//
//            }
//            else{
//                 tvVoucherCode.setText(output.getRedeemed().toString());
//                tvVouchertext.setText((output.getCanRedeem()-output.getRedeemed())+"" );
//
//            }
        // tvVouchertext.setText(/*(*//*output.getCanRedeem()-output.getRedeemed())*//*" visit available and "*/ +output.getRedeemed() +" visit used" );
    }

    private fun showConfirmationDialog(voucher: String) {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_confirmation)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCancelable(false)
        val cancel_subs = dialog.findViewById<TextView>(R.id.cancel_subs)
        val btn_confirmation = dialog.findViewById<TextView>(R.id.btn_confirmation)
        cancel_subs!!.setOnClickListener { v: View? ->
//            callCancelAPI(
//                dialog,
//                "",
//                voucher
//            )
            authViewModel.passportCancel(preferences.getUserId(),"",voucher)
            passportCancel()
        }
        btn_confirmation!!.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.show()
    }


}