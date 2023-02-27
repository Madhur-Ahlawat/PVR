package com.net.pvr.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.databinding.ItemPaymentPrivlegeBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.payment.response.CouponResponse
import com.net.pvr.utils.Constant
import com.net.pvr.utils.show

class CouponAdapter(
    private var nowShowingList: ArrayList<CouponResponse.Output.Voucher>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
    private val preferences: PreferenceManager,
) :
    RecyclerView.Adapter<CouponAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemPaymentPrivlegeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaymentPrivlegeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.ex.text = "Valid till " + this.exf
                val itd: String = this.itd
                val values = itd.split("\\|").toTypedArray()
                binding.price.text = Html.fromHtml(Html.fromHtml(values[0]).toString())

                //  holder.vocDetailsTextView.setText();
                if (values.size > 1) {
                    binding.subPrice.show()
                    binding.subPrice.text = values[1]
                } else {
                    binding.subPrice.visibility = View.INVISIBLE
                }
                binding.voucherName.text = this.nm
                binding.code.text = this.cd

                if (!this.display_value) {
                    binding.ivSubs.setImageDrawable(context.resources.getDrawable(R.drawable.ic_back_gray))
                }

                var isunlimitedvoucher = false

                if (this.type == ("SUBSCRIPTION")) {
                    if (preferences.getString(Constant.SharedPreference.pcities)
                            .contains(preferences.getCityName())
                    ) {
                        binding.ivSubs.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.bannerprivv
                            )
                        )
                        binding.parrentView.isEnabled = true
                    } else {
                        binding.ivSubs.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.subs_gray
                            )
                        )
                        binding.parrentView.isEnabled = false
                    }
                    binding.price.visibility = View.INVISIBLE
                    binding.price.textSize = 10f
                    binding.code.setTextColor(context.resources.getColor(R.color.white))
                    // code.setText(getResources().getString(R.string.subscription_nvoucher));
                    binding.code.isSingleLine = false
                    //                                            ex.setText("\n\n\nValid till " + voucherNewCombineLists.get(i).getVoucherVo().getExf());
                    binding.ex.text = "Valid till " + this.exf
                    binding.code.text = "" + this.cd
                    binding.code.textSize = 12f
                    binding.ex.setTextColor(context.resources.getColor(R.color.yellow_text))
                    binding.voucherName.setTextColor(context.resources.getColor(R.color.white))
                    binding.voucherName.visibility = View.INVISIBLE
                    isunlimitedvoucher = true
                    binding.ivImageSubs.setVisibility(View.GONE)
                }

                var accrual = false
                if (!isunlimitedvoucher) {
                    val new_voucher_type: String = this.new_voucher_type
                    binding.voucherName.text = this.tpt
                    if (new_voucher_type != null && new_voucher_type.equals(
                            "accrual",
                            ignoreCase = true
                        )
                    ) {
                        accrual = true
                        binding.newVoucherType.text = "Accrual Voucher"
                        var scheme = 0
                        try {
                            scheme = this.sc
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }
                        when (this.tp) {
                            "C" -> {
                                if (scheme == 2 || scheme == 35 || scheme == 27) {
                                    binding.ivSubs.setImageResource(R.drawable.voucher_popcorn)
                                    binding.newVoucherType.text = ""
                                    binding.ex.setTextColor(context.resources.getColor(R.color.pvr_dark_black))
                                    binding.price.setTextColor(context.resources.getColor(R.color.pvr_dark_black))
                                    binding.code.setTextColor(context.resources.getColor(R.color.pvr_dark_black))
                                } else binding.ivSubs.setImageResource(R.drawable.voucher_2)
                                binding.main.background =
                                    context.resources.getDrawable(R.drawable.pymnt_vc_f)
                            }
                            "D" -> {
                                binding.ivSubs.setImageResource(R.drawable.voucher_1)
                                binding.main.background =
                                    context.resources.getDrawable(R.drawable.pymnt_vc_t)
                            }
                            "T" -> {
                                binding.ivSubs.setImageResource(R.drawable.voucher_3)
                                binding.main.background =
                                    context.resources.getDrawable(R.drawable.pynt_vc_f_t)
                            }
                        }
                        //ivSubs.setImageResource(R.drawable.voucher_1);
                    } else binding.newVoucherType.text = new_voucher_type
                }
                if (null != this.tp && this.tp != "") {
                    var scheme = 0
                    try {
                        scheme = this.sc
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                    when (this.tp) {
                        "C" -> {
                            if (!isunlimitedvoucher && !accrual) {
                                if (scheme == 2 || scheme == 35 || scheme == 27) {
                                    binding.ivSubs.setImageResource(R.drawable.voucher_popcorn)
                                    binding.newVoucherType.text = ""
                                    binding.ex.setTextColor(context.resources.getColor(R.color.pvr_dark_black))
                                    binding.price.setTextColor(context.resources.getColor(R.color.pvr_dark_black))
                                    binding.code.setTextColor(context.resources.getColor(R.color.pvr_dark_black))
                                } else binding.ivSubs.setImageResource(R.drawable.voucher_2)
                            }
                            binding.main.background =
                                context.resources.getDrawable(R.drawable.pymnt_vc_f)
                        }
                        "D" -> {
                            if (!isunlimitedvoucher && !accrual) binding.ivSubs.setImageResource(R.drawable.voucher_1)
                            binding.main.background =
                                context.resources.getDrawable(R.drawable.pymnt_vc_t)
                        }
                        "T" -> {
                            if (!isunlimitedvoucher && !accrual) binding.ivSubs.setImageResource(R.drawable.voucher_3)
                            binding.main.background =
                                context.resources.getDrawable(R.drawable.pynt_vc_f_t)
                        }
                    }
                }

                binding.parrentView.setOnClickListener { view ->
                    if (this.display_value) {
                        listener.couponClick(this,position,binding)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }



    interface RecycleViewItemClickListenerCity {
        fun couponClick(
            data: CouponResponse.Output.Voucher,
            position: Int,
            binding: ItemPaymentPrivlegeBinding
        )
    }

}