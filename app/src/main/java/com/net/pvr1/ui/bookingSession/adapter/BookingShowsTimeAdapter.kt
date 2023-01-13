package com.net.pvr1.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemCinemaDetailsShowTimeBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.seatLayout.SeatLayoutActivity
import com.net.pvr1.utils.Constant.Companion.CINEMA_ID
import com.net.pvr1.utils.Constant.Companion.OfferDialogImage
import com.net.pvr1.utils.Constant.Companion.SESSION_ID
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class BookingShowsTimeAdapter(
    private var nowShowingList: ArrayList<BookingResponse.Output.Cinema.Child.Sw.S>,
    private var context: Context,
    private var ccid: String
) :
    RecyclerView.Adapter<BookingShowsTimeAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCinemaDetailsShowTimeBinding) :
        RecyclerView.ViewHolder(binding.root)

    private  var rowIndex:Int=0
    private var sidText: String = ""
    private var ccText: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaDetailsShowTimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                val colorCode = "#" + this.cc

                binding.textView96.setTextColor(Color.parseColor(colorCode))
                binding.imageView48.setColorFilter(Color.parseColor(colorCode))
                binding.imageView49.setColorFilter(Color.parseColor(colorCode))

                //between 0-255
                val alpha = 10
                val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor(colorCode), alpha)
                val alphaNew2 = alphaColor.toString().substring(0, alphaColor.toString().length - 1)
                val alphaNew3 = "#$alphaNew2"
                val gd = GradientDrawable()
                gd.setColor(Color.parseColor(alphaNew3))
                gd.cornerRadius = 10f
                gd.setStroke(2, Color.parseColor(colorCode))
                binding.cardView10.setBackgroundDrawable(gd)

                //Language
                binding.textView96.text = this.st
                SESSION_ID = this.sid.toString()
                CINEMA_ID = ccid

                //Handicap
                if (this.hc){
                    binding.constraintLayout133.show()
                }else{
                    binding.constraintLayout133.hide()
                }

                holder.itemView.setOnClickListener {
                    rowIndex=position
                    if (this.ss != 0 && this.ss != 3) {
                        if (this.ba) {
                            showOfferDialog()
                        } else {
                            sidText = this.sid.toString()
                            ccText = this.cc
                            val intent = Intent(context, SeatLayoutActivity::class.java)
                            intent.putExtra("clickPosition", rowIndex.toString())
                            intent.putExtra("shows", nowShowingList)
                            intent.putExtra("skip", "true")
                            context.startActivity(intent)

                        }

                    }
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showOfferDialog() {
        val progressDialog = nowShowingList[0].prs
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.booking_offer_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        val ticket = dialog.findViewById<TextView>(R.id.textView258)
        val food = dialog.findViewById<TextView>(R.id.textView261)
        val offerPrice = dialog.findViewById<TextView>(R.id.textView264)
        val totalPrice = dialog.findViewById<TextView>(R.id.textView268)
        val skip = dialog.findViewById<TextView>(R.id.textView266)
        val image = dialog.findViewById<ImageView>(R.id.imageView108)
        val applyOffer = dialog.findViewById<TextView>(R.id.textView267)

        Glide.with(context)
            .load(OfferDialogImage)
            .into(image)

        offerPrice.paintFlags = offerPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        val offerPriceText =
            (progressDialog[0].p.toDouble().roundToInt() + progressDialog[0].bv.toDouble()
                .roundToInt())
        ticket.text = context.getString(R.string.currency) + progressDialog[0].p
        food.text = context.getString(R.string.currency) + progressDialog[0].bv
        offerPrice.text = context.getString(R.string.currency) + offerPriceText
        totalPrice.text = context.getString(R.string.currency) + progressDialog[0].bp
        val discountPrice=offerPriceText - progressDialog[0].bp.toInt()


        skip.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(context, SeatLayoutActivity::class.java)
            intent.putExtra("clickPosition", rowIndex.toString())
            intent.putExtra("shows", nowShowingList)
            intent.putExtra("skip", "true")
            context.startActivity(intent)

        }

        applyOffer.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(context, SeatLayoutActivity::class.java)
            intent.putExtra("clickPosition", rowIndex.toString())
            intent.putExtra("shows", nowShowingList)
            intent.putExtra("discountPrice", discountPrice.toString())
            intent.putExtra("skip", "false")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

}