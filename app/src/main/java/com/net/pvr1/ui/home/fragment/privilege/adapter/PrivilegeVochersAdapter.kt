package com.net.pvr1.ui.home.fragment.privilege.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.net.pvr1.R
import com.net.pvr1.databinding.VoucherListBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.home.fragment.privilege.response.LoyaltyDataResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


@Suppress("DEPRECATION")
class PrivilegeVochersAdapter(
    private var nowShowingList: ArrayList<LoyaltyDataResponse.Voucher>,
    private var context: Activity,
    private val preferences: PreferenceManager,
    private val listener: RecycleViewItemClickListener
) : RecyclerView.Adapter<PrivilegeVochersAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: VoucherListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VoucherListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForColorStateLists",
        "UseCompatLoadingForDrawables"
    )
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        // holder
        val cardData = nowShowingList[position]
        holder.binding.qrValue.text =  cardData.cd
        holder.binding.qrValueGray.text =  cardData.cd
        holder.binding.redeemDate.text =  "Valid till " +cardData.ex
        holder.binding.redeemDateGray.text =  "Valid till " +cardData.ex
        holder.binding.qrCodeImgGray.hide()
        when (cardData.st) {
            "V" -> {
                holder.binding.rlsubsMain.hide()
                holder.binding.relativeLayoutGray.hide()
                holder.binding.relativeLayout.show()
                holder.binding.ivRedeemed.hide()
                holder.binding.vocDetails.setTextColor(context.resources.getColor(R.color.white))
                holder.binding.vocData.setTextColor(context.resources.getColor(R.color.white))
                holder.binding.qrValue.setTextColor(context.resources.getColor(R.color.white))
                holder.binding.redeemDate.setTextColor(context.resources.getColor(R.color.white))
                holder.binding.textLabel.setBackground(context.resources.getDrawable(R.drawable.gradient_free_snacks))
                if (cardData.voucher_type.isNotEmpty() && cardData.voucher_type!=""){
                    var infotext = false
                    val new_voucher_type = cardData.new_voucher_type
                    if (new_voucher_type != null && new_voucher_type.equals("accrual", ignoreCase = true)) {
                        holder.binding.vocType.text = "Accrual Voucher"
                        if (cardData.tp == "d") {
                            holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_1)
                        } else if (cardData.tp == "c") {
                            if (cardData.sc == 27) {
                                infotext = true
                                holder.binding.vocType.text = ""
                                holder.binding.redeemDate.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.vocDetails.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.qrValue.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_popcorn)
                            } else if (cardData.sc == 35) {
                                infotext = true
                                holder.binding.vocType.text = ""
                                holder.binding.vocDetails.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.redeemDate.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.qrValue.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_popcorn)
                            } else if (cardData.sc == 35) {
                                infotext = true
                                holder.binding.vocType.text = ""
                                holder.binding.redeemDate.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.vocDetails.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.qrValue.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_popcorn)
                            } else holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_2)
                        } else if (cardData.tp == "t") {
                            holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_3)
                        } else if (cardData.tp == "3") {
                            holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_4)
                        }
                    } else {
                        holder.binding.vocType.text = new_voucher_type
                        if (cardData.tp == "d") {
                            holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_3)
                        } else if (cardData.tp == "c") {
                            if (cardData.sc == 27) {
                                infotext = true
                                holder.binding.vocType.text = ""
                                holder.binding.redeemDate.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.vocDetails.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.qrValue.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_popcorn)
                            } else if (cardData.sc == 35) {
                                infotext = true
                                holder.binding.vocType.setText("")
                                holder.binding.vocDetails.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.redeemDate.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.qrValue.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.relativeLayout.setBackground(context.resources.getDrawable(R.drawable.voucher_popcorn))
                            } else if (cardData.sc == 2) {
                                infotext = true
                                holder.binding.vocType.text = ""
                                holder.binding.redeemDate.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.vocDetails.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.qrValue.setTextColor(context.resources.getColor(R.color.h2color))
                                holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_popcorn)
                            } else holder.binding.relativeLayout.background = context.resources.getDrawable(R.drawable.voucher_2)
                        } else if (cardData.tp == "t") {
                            holder.binding.relativeLayout.setBackground(context.resources.getDrawable(R.drawable.voucher_3))
                        } else if (cardData.tp == "3") {
                            holder.binding.relativeLayout.setBackground(context.resources.getDrawable(R.drawable.voucher_4))
                        }
                    }
                    val info: String = cardData.info

                    if (info == null || info.isEmpty()) {
                        holder.binding.vocTerms.hide()
                        holder.binding.vocTermsDummy.hide()
                        holder.binding.vocTermsDummy.setOnLongClickListener(null)
                        holder.binding.vocTermsDummy.setOnClickListener(null)
                    } else {
                        if (info == null || info.isEmpty()) {
                            holder.binding.vocTerms.hide()
                            holder.binding.vocTermsDummy.setTextColor(Color.parseColor("#000000"))
                            holder.binding.vocTermsDummy.setOnLongClickListener(null)
                            holder.binding.vocTermsDummy.setOnClickListener(null)
                        } else {
                            if (infotext) {
                                holder.binding.vocTerms.hide()
                                holder.binding.vocTermsDummy.hide()
                                holder.binding.vocTermsDummy.setTextColor(Color.parseColor("#9C9602"))
                            } else {
                                holder.binding.vocTerms.show()
                                holder.binding.vocTermsDummy.show()
                                holder.binding.vocTermsDummy.setTextColor(Color.parseColor("#000000"))
                            }
                        }
                        holder.binding.vocTermsDummy.setOnLongClickListener(OnLongClickListener { //                            ToolTipWindow tipWindow = new ToolTipWindow(context, ToolTipWindow.DRAW_TOP, info);
//                            tipWindow.showToolTip(holder.ivVocTerms, ToolTipWindow.DRAW_ARROW_DEFAULT_CENTER, true);
                            //showATDialog(context, info)
                            false
                        })
                        holder.binding.vocTermsDummy.setOnClickListener(View.OnClickListener { //                            ToolTipWindow tipWindow = new ToolTipWindow(context, ToolTipWindow.DRAW_TOP, info);
//                            tipWindow.showToolTip(holder.ivVocTerms, ToolTipWindow.DRAW_ARROW_DEFAULT_CENTER, true);
                            //showATDialog(context, info)
                        })
                    }
                }

                /* if (voucherNewCombineLists.get(position).getVoucherVo().getTp() != null) {

                    if (voucherNewCombineLists.get(position).getVoucherVo().getTp().equalsIgnoreCase("d")) {
                        holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.snacks_background));
                    } else if (voucherNewCombineLists.get(position).getVoucherVo().getTp().equalsIgnoreCase("c")) {

                        holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.food_background));

                    } else if (voucherNewCombineLists.get(position).getVoucherVo().getTp().equalsIgnoreCase("t")) {

                        holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.food_background));
                    }
                }*/holder.binding.tvDaysleft.hide()

//                if (cardData.expiryDate > 1
//                ) holder.tvDaysleft.setText(
//                    voucherNewCombineLists.get(position).getVoucherVo().getExDays() + " days left"
//                ) else holder.tvDaysleft.setText(
//                    voucherNewCombineLists.get(position).getVoucherVo().getExDays() + " day left"
//                )

                holder.binding.qrCodeImg.show()
                generateQrcode(holder.binding.qrCodeImg)
                    .execute(cardData.cd)
            }
            "SUBSCRIPTION" -> {
                holder.binding.rlsubsMain.show()
                holder.binding.relativeLayoutGray.hide()
                holder.binding.relativeLayout.hide()

                var rlsubs_main: RelativeLayout
                var ivRedeemed_subs: ImageView
                var tvVoucherCode: TextView
                var tvDate: TextView

                holder.binding.ivRedeemedSubs.show()
                if (cardData.st == "E")
                    holder.binding.ivRedeemedSubs.setImageDrawable(context.resources.getDrawable(R.drawable.expired))
                holder.binding.tvVoucherCode.text = "" + cardData.cd

                if (cardData != null && !TextUtils.isEmpty(cardData.ex)) {
                    holder.binding.tvDate.text = "Valid till " + cardData.ex
                    holder.binding.tvDate.show()
                } else holder.binding.tvDate.hide()
            }
            else -> {
                holder.binding.rlsubsMain.hide()
                holder.binding.relativeLayoutGray.show()
                holder.binding.relativeLayout.hide()
                if (cardData.voucher_type != null) {
                    val new_voucher_type = cardData.new_voucher_type
                    if (new_voucher_type != null && new_voucher_type.equals("accrual", ignoreCase = true))
                        holder.binding.vocType.text = "Accrual Voucher" else
                        holder.binding.vocType.text = new_voucher_type
                    val info: String =cardData.info
                    if (info == null || info.isEmpty())
                        holder.binding.vocTerms.hide()
                    else {
                        holder.binding.vocTerms.show()
                        holder.binding.vocTerms.setOnClickListener(View.OnClickListener { })
                    }
                    if (cardData.tp == "d") {
                        holder.binding.relativeLayoutGray.background = context.resources.getDrawable(R.drawable.voucher_ticket_grey)
                    } else if (cardData.tp == "c") {
                        if (cardData.sc == 27) {
                            holder.binding.vocType.text = ""
                            holder.binding.redeemDateGray.setTextColor(context.resources.getColor(R.color.h2color))
                            holder.binding.vocDetailsGray.setTextColor(context.resources.getColor(R.color.h2color))
                            holder.binding.qrValueGray.setTextColor(context.resources.getColor(R.color.h2color))
                            holder.binding.relativeLayoutGray.background = context.resources.getDrawable(R.drawable.voucher_popcorn_grey)
                        } else if (cardData.sc == 35) {
                            holder.binding.vocType.text = ""
                            holder.binding.vocDetailsGray.setTextColor(context.resources.getColor(R.color.h2color))
                            holder.binding.redeemDateGray.setTextColor(context.resources.getColor(R.color.h2color))
                            holder.binding.qrValueGray.setTextColor(context.resources.getColor(R.color.h2color))
                            holder.binding.relativeLayoutGray.background = context.resources.getDrawable(R.drawable.voucher_popcorn_grey)
                        } else if (cardData.sc == 2) {
                            holder.binding.vocType.text = ""
                            holder.binding.redeemDateGray.setTextColor(context.resources.getColor(R.color.h2color))
                            holder.binding.vocDetailsGray.setTextColor(context.resources.getColor(R.color.h2color))
                            holder.binding.qrValueGray.setTextColor(context.resources.getColor(R.color.h2color))
                            holder.binding.relativeLayoutGray.background = context.resources.getDrawable(R.drawable.voucher_popcorn_grey)
                        } else holder.binding.relativeLayoutGray.background = context.resources.getDrawable(R.drawable.voucher_f_n_b_grey)
                    } else if (cardData.tp == "t") {
                        holder.binding.relativeLayoutGray.background = context.resources.getDrawable(R.drawable.voucher_ticket_grey)
                    } else if (cardData.tp == "27") {
                        holder.binding.relativeLayoutGray.background = context.resources.getDrawable(R.drawable.voucher_popcorn_grey)
                    } else if (cardData.tp == "35") {
                        holder.binding.relativeLayoutGray.background = context.resources.getDrawable(R.drawable.voucher_popcorn_grey)
                    }
                }

                holder.binding.ivRedeemed.show()
                if (cardData.st == "E")
                    holder.binding.ivRedeemed.setImageDrawable(context.resources.getDrawable(R.drawable.expired))

                holder.binding.qrCodeImgGray.hide()
                holder.binding.textLabelGray.setTextColor(context.resources.getColor(R.color.white))
                holder.binding.vocDetailsGray.setTextColor(context.resources.getColor(R.color.gray))
                holder.binding.vocDataGray.setTextColor(context.resources.getColor(R.color.gray))
                holder.binding.qrValueGray.setTextColor(context.resources.getColor(R.color.gray))
                holder.binding.redeemDateGray.setTextColor(context.resources.getColor(R.color.gray))
                holder.binding.textLabelGray.background = context.resources.getDrawable(R.drawable.gradient_free_snacks_gray)
            }
        }

        holder.binding.textLabel.text = cardData.tpt
        holder.binding.textLabelGray.text = cardData.tpt

        val itd: String = cardData.itd
        val values = itd.split("\\|").toTypedArray()
        holder.binding.vocDetails.text = Html.fromHtml(Html.fromHtml(values[0]).toString())
        holder.binding.vocDetailsGray.text = Html.fromHtml(Html.fromHtml(values[0]).toString())

        //  holder.vocDetailsTextView.setText();


        //  holder.vocDetailsTextView.setText();
        if (values.size > 1) {
            holder.binding.vocData.show()
            holder.binding.vocDataGray.show()
            holder.binding.vocData.text = values[1]
            holder.binding.vocDataGray.text = values[1]
        } else {
            holder.binding.vocData.hide()
            holder.binding.vocData.text = cardData.itd
        }
        //createQR(voucherNewCombineLists.get(position).getVoucherVo().getCd(),holder.qrCodeImageView);


        //createQR(voucherNewCombineLists.get(position).getVoucherVo().getCd(),holder.qrCodeImageView);
        holder.itemView.setOnClickListener {
//            if (holder.qrCodeImageView.getVisibility() == View.VISIBLE) openDialog(
//                unUsedVoucherNewCombineLists,
//                position
//            )
        }

//        if (position == voucherNewCombineLists.size - 1) holder.LastView.setVisibility(View.VISIBLE)

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun onItemClick()
    }

    class generateQrcode(var bmImage: ImageView) :
        AsyncTask<String?, Void?, Bitmap?>() {
        override fun onPostExecute(result: Bitmap?) {
            bmImage.setImageBitmap(result)
        }

        companion object {
            const val WIDTH = 400
        }

        override fun doInBackground(vararg params: String?): Bitmap? {
            val Value = params[0]
            val writer = QRCodeWriter()
            var bmp: Bitmap? = null
            try {
                val bitMatrix = writer.encode(Value, BarcodeFormat.QR_CODE, 250, 250)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val pixels = IntArray(width * height)
                for (y in 0 until height) {
                    val offset = y * width
                    for (x in 0 until width) {
                        pixels[offset + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                    }
                }
                bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bmp.setPixels(pixels, 0, width, 0, 0, width, height)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
            return bmp
        }
    }


}