package com.net.pvr.ui.cinemaSession.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ItemCinemaDetailsShowTimeBinding
import com.net.pvr.ui.bookingSession.MovieSessionActivity
import com.net.pvr.ui.bookingSession.MovieSessionActivity.Companion.btnc
import com.net.pvr.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.home.HomeFragment
import com.net.pvr.ui.seatLayout.SeatLayoutActivity
import com.net.pvr.utils.Constant
import com.net.pvr.utils.ga.GoogleAnalytics
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


@Suppress("DEPRECATION")
class CinemaSessionTimeAdapter(
    private var nowShowingList: ArrayList<CinemaSessionResponse.Child.Mv.Ml.S>,
    private var context: Activity,
    private var cinemaId: String?,
    private var ccn: String,
    private var adlt: Boolean,
    private var at: String,
    private var mih: String,
    private var i: Int
) :
    RecyclerView.Adapter<CinemaSessionTimeAdapter.ViewHolder>() {

    private var sidText: String = ""
    private var ccText: String = ""
    private var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    inner class ViewHolder(val binding: ItemCinemaDetailsShowTimeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaDetailsShowTimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                val itemWidth = (((screenWidth) / (4)))
                val layoutParams = ConstraintLayout.LayoutParams(
                    itemWidth,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
//                layoutParams.marginEnd = Constant().convertDpToPixel(13f, context)
//                layoutParams.bottomMargin = Constant().convertDpToPixel(13f, context)
//                holder.itemView.layoutParams = layoutParams

               // binding.mainView.layoutParams = layoutParams
                //Language
                binding.textView96.text = this.st
                val colorCode = "#" + this.cc

                binding.textView96.setTextColor(Color.parseColor(colorCode))
                binding.imageView48.setColorFilter(Color.parseColor(colorCode))
                binding.imageView49.setColorFilter(Color.parseColor(colorCode))

                val alpha = 10
                val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor(colorCode), alpha)
                val alphaNew2 = alphaColor.toString().substring(0, alphaColor.toString().length - 1)
                val alphaNew3 = "#$alphaNew2"
                val gd = GradientDrawable()
                gd.setColor(Color.parseColor(alphaNew3))
                gd.cornerRadius = 10f
                gd.setStroke(2, Color.parseColor(colorCode))
                binding.cardView10.setBackgroundDrawable(gd)

                //handicap
                if (this.hc) {
                    binding.imageView48.show()
                } else {
                    binding.imageView48.hide()
                }

                if (!TextUtils.isEmpty(this.comm)) {
                    binding.imageView49.show()
                    when (this.comm) {
                        ("RST") -> {
                            binding.imageView49.setImageResource(R.drawable.line_arrow)
                        }
                        ("CC") -> {
                            binding.imageView49.setImageResource(R.drawable.ic_cc_gray)
                        }
                        ("AD") -> {
                            binding.imageView49.setImageResource(R.drawable.ic_audio_icon_gray)
                        }
                        else -> {
                            binding.imageView49.hide()
                        }
                    }
                }else{
                    binding.imageView49.hide()
                }

                if (!TextUtils.isEmpty(this.txt) && this.txt == ("sens")) {
                    binding.imageView50.show()
                    binding.imageView50.setImageResource(R.drawable.ic_sens_icon_gray)
                }else{
                    binding.imageView50.hide()
                }

                binding.imageView48.setColorFilter(Color.parseColor(colorCode))
                binding.imageView49.setColorFilter(Color.parseColor(colorCode))
                binding.imageView50.setColorFilter(Color.parseColor(colorCode))

                Constant.SESSION_ID = this.sid.toString()
                Constant.CINEMA_ID = cinemaId.toString()
                HomeFragment.mcId = this.mc


                if (this.ss!=0&&this.ss!=3)
                holder.itemView.setOnClickListener {
//                    val intent = Intent(context, SeatLayoutActivity::class.java)
//                    intent.putExtra("from", "cinema")
//                    intent.putExtra("skip", "true")
//                    intent.putExtra("CinemaShows", nowShowingList)
//                    intent.putExtra("position", position.toString())
//                    context.startActivity(intent)
                    if (this.ss != 0 && this.ss != 3) {
                        if (this.ba) {
                            try {
                                showOfferDialog(this.sid, this.cc, this.at, position)
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        } else {
                            sidText = this.sid.toString()
                            ccText = this.cc
                            HomeFragment.mcId = this.mc
                            val intent = Intent(context, SeatLayoutActivity::class.java)
                            intent.putExtra("clickPosition", position.toString())
                            intent.putExtra("shows", nowShowingList)
                            intent.putExtra("skip", "true")
                            intent.putExtra("from", "cinema")
                            intent.putExtra("skip", "true")
                            if (adlt) {
                                val dialog = OptionDialog(context,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    context.getString(R.string.adult_msz),
                                    positiveBtnText = R.string.accept,
                                    negativeBtnText = R.string.cancel,
                                    positiveClick = {
                                        if (at != "") {
                                            val dialog = OptionDialog(context,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                at,
                                                positiveBtnText = R.string.accept,
                                                negativeBtnText = R.string.cancel,
                                                positiveClick = {
                                                    if (this.at != "") {
                                                        val dialog = OptionDialog(context,
                                                            R.mipmap.ic_launcher,
                                                            R.string.app_name,
                                                            this.at,
                                                            positiveBtnText = R.string.accept,
                                                            negativeBtnText = R.string.cancel,
                                                            positiveClick = {
                                                                context.startActivity(intent)
                                                            },
                                                            negativeClick = {})
                                                        dialog.show()
                                                    } else {
                                                        context.startActivity(intent)
                                                    }
                                                },
                                                negativeClick = {})
                                            dialog.show()
                                        } else {
                                            if (this.at != "") {
                                                val dialog = OptionDialog(context,
                                                    R.mipmap.ic_launcher,
                                                    R.string.app_name,
                                                    this.at,
                                                    positiveBtnText = R.string.accept,
                                                    negativeBtnText = R.string.cancel,
                                                    positiveClick = {
                                                        context.startActivity(intent)
                                                    },
                                                    negativeClick = {})
                                                dialog.show()
                                            } else {
                                                context.startActivity(intent)
                                            }
                                        }
                                    },
                                    negativeClick = {})
                                dialog.show()

                            } else {
                                if (at != "") {
                                    val dialog = OptionDialog(context,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        at,
                                        positiveBtnText = R.string.accept,
                                        negativeBtnText = R.string.cancel,
                                        positiveClick = {
                                            if (this.at != "") {
                                                val dialog = OptionDialog(context,
                                                    R.mipmap.ic_launcher,
                                                    R.string.app_name,
                                                    this.at,
                                                    positiveBtnText = R.string.accept,
                                                    negativeBtnText = R.string.cancel,
                                                    positiveClick = {
                                                        context.startActivity(intent)
                                                    },
                                                    negativeClick = {})
                                                dialog.show()
                                            } else {
                                                context.startActivity(intent)
                                            }
                                        },
                                        negativeClick = {})
                                    dialog.show()
                                } else {
                                    if (this.at != "") {
                                        val dialog = OptionDialog(context,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            this.at,
                                            positiveBtnText = R.string.accept,
                                            negativeBtnText = R.string.cancel,
                                            positiveClick = {
                                                context.startActivity(intent)
                                            },
                                            negativeClick = {})
                                        dialog.show()
                                    } else {
                                        context.startActivity(intent)
                                    }
                                }
                            }

                        }

                    }
                }

                holder.itemView.setOnLongClickListener {

                    showChangeLangDialogNew(
                        context,
                        ccn,
                        this,
                        holder.itemView
                    )

                    return@setOnLongClickListener true
                }
            }
        }

    }

    override fun getItemCount(): Int {
        println("getItemCount----$i-----${nowShowingList.size}")

        return if (i==3){
            if (nowShowingList.size<3){
                nowShowingList.size
            }else{
                3
            }
        }else{
            nowShowingList.size
        }


    }

    private fun showChangeLangDialogNew(
        context: Context,
        ccn: String,
        item: CinemaSessionResponse.Child.Mv.Ml.S,
        itemView: View
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        val priceDaos: List<CinemaSessionResponse.Child.Mv.Ml.S.Pr> = item.prs
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.show_detail_dialog, null)
        dialogBuilder.setView(dialogView)

        val linearLayout = dialogView.findViewById<LinearLayout>(R.id.mainView)

        val layout1 = LinearLayout(context)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layout1.layoutParams = params
        layout1.orientation = LinearLayout.HORIZONTAL

//CinemaName

//CinemaName
        val cinema_name = TextView(context)
        val params1 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params1.setMargins(0, 10, 0, 20)
        params1.gravity = Gravity.TOP
        params1.weight = 1.0f
        cinema_name.isSingleLine = true
        cinema_name.ellipsize = TextUtils.TruncateAt.END
        cinema_name.layoutParams = params1
        cinema_name.text = ccn
        cinema_name.setTextAppearance(context, R.style.text_black)
        layout1.addView(cinema_name)

//ShowTime

//ShowTime
        val time = TextView(context)
        val params2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params2.weight = 3.5f
        params2.gravity = Gravity.TOP
        params2.setMargins(0, 18, 0, 20)
        time.layoutParams = params2
        time.setTextAppearance(context, R.style.text_gray_gift)
        time.text = item.st
        layout1.addView(time)
        linearLayout.addView(layout1)

//Create ShowType

//Create ShowType
        for (i in priceDaos.indices) {
            val priceDao: CinemaSessionResponse.Child.Mv.Ml.S.Pr = priceDaos[i]
            val layout = LinearLayout(context)
            val params4 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            layout.layoutParams = params4
            layout.orientation = LinearLayout.HORIZONTAL
            val params3 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params3.weight = 1.0f
            params3.gravity = Gravity.TOP
            params3.setMargins(0, 40, 0, 0)
            val title = TextView(context)
            title.layoutParams = params3
            title.isSingleLine = true
            // title.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            title.ellipsize = TextUtils.TruncateAt.END
            title.setTextAppearance(context, R.style.text_black)
            // title.setTypeface(title.getTypeface(), Typeface.BOLD);
            title.setText(priceDao.n)
            layout.addView(title)
            val params5 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params5.weight = 3.5f
            params5.gravity = Gravity.TOP
            params5.setMargins(0, 40, 0, 0)
            val value = TextView(context)
            value.layoutParams = params5
            value.setTextAppearance(context, R.style.text_gray_gift)
            // value.setTypeface(value.getTypeface(), Typeface.BOLD);
            value.text = "₹" + priceDao.p + ".0"
            layout.addView(value)
            linearLayout.addView(layout)
        }

        if (item.et != ("")) {
            val et_layout1 = LinearLayout(context)
            val et_params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            et_layout1.layoutParams = et_params
            et_layout1.orientation = LinearLayout.HORIZONTAL
            val et_text = TextView(context)
            val params_et_text = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params_et_text.setMargins(0, 40, 0, 0)
            params_et_text.gravity = Gravity.TOP
            params_et_text.weight = 1.0f
            et_text.isSingleLine = true
            et_text.ellipsize = TextUtils.TruncateAt.END
            et_text.layoutParams = params_et_text
            et_text.text = "Show end time approx"
            et_text.setTextAppearance(context, R.style.text_black)
            et_layout1.addView(et_text)
            val et = TextView(context)
            val paramset = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            paramset.weight = 3.5f
            paramset.gravity = Gravity.TOP
            paramset.setMargins(0, 40, 0, 0)
            et.layoutParams = paramset
            et.setTextAppearance(context, R.style.text_gray_gift)
            et.text = item.et
            et_layout1.addView(et)
            linearLayout.addView(et_layout1)
        }
// dialogBuilder.setMessage("Enter text below");


        // dialogBuilder.setMessage("Enter text below");
        val b = dialogBuilder.create()
        dialogView.setOnClickListener {
            b.cancel()
        }
        b.requestWindowFeature(Window.FEATURE_NO_TITLE)
        b.setCancelable(true)
        b.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showOfferDialog(sid: Int, cc: String,newAt:String,position: Int) {
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
        val viewTnc = dialog.findViewById<LinearLayout>(R.id.viewTnc)
        val tncData = dialog.findViewById<LinearLayout>(R.id.tncData)
        val arrow = dialog.findViewById<ImageView>(R.id.arrow)

        println("btnc--->${btnc}----${mih}")

        Glide.with(context)
            .load(mih)
            .error(R.drawable.placeholder_horizental)
            .placeholder(R.drawable.placeholder_horizental)
            .into(image)

        viewTnc.setOnClickListener {
            if (tncData.visibility == View.VISIBLE){
                tncData.hide()
                arrow.rotation = 360f
            }else{
                tncData.show()
                arrow.rotation = 180f
            }
        }

        if (!TextUtils.isEmpty(MovieSessionActivity.btnc)) {
            val st = StringTokenizer(MovieSessionActivity.btnc, "|")

            while (st.hasMoreElements()) {
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.dynamic_bullet_row, tncData, false)
                val tvMessage:TextView = view.findViewById(R.id.tvMessage1) as TextView
                val tvDot:TextView = view.findViewById(R.id.tvDot1) as TextView
                tvDot.setTextColor(context.resources.getColor(R.color.gray_))
                tvMessage.setTextColor(context.resources.getColor(R.color.gray_))
                tvMessage.text = st.nextElement().toString()
                tvDot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5f)
                tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                tncData.addView(view)
            }
        }

//        offerPrice.paintFlags = offerPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        var discountPrice = 0
        try {
            offerPrice.paintFlags = offerPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            for (data in progressDialog) {
                ticket.text = context.getString(R.string.currency) + data.p
                food.text = context.getString(R.string.currency) + data.bv
                totalPrice.text = context.getString(R.string.currency) + data.bp

                val offerPriceText =
                    (data.p.toDouble().roundToInt() + data.bv.toDouble().roundToInt())

                offerPrice.text = context.getString(R.string.currency) + offerPriceText
                discountPrice = offerPriceText - data.bp.toInt()
                break
            }
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            discountPrice = 0
        }



        skip.setOnClickListener {
            dialog.dismiss()
            sidText = sid.toString()
            ccText = cc

            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
//                                bundle.putString("var_FnB_food_type","veg")
                GoogleAnalytics.hitEvent(context, "book_apply_offer_skip", bundle)
                GoogleAnalytics.hitEvent(context, "movie_show_time", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            val intent = Intent(context, SeatLayoutActivity::class.java)
            intent.putExtra("clickPosition", position.toString())
            intent.putExtra("shows", nowShowingList)
            intent.putExtra("skip", "true")
            intent.putExtra("from", "cinema")
            if (adlt) {
                val dialog = OptionDialog(context,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    context.getString(R.string.adult_msz),
                    positiveBtnText = R.string.accept,
                    negativeBtnText = R.string.cancel,
                    positiveClick = {
                        if (at != "") {
                            val dialog = OptionDialog(context,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                at,
                                positiveBtnText = R.string.accept,
                                negativeBtnText = R.string.cancel,
                                positiveClick = {
                                    if (newAt != "") {
                                        val dialog = OptionDialog(context,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            newAt,
                                            positiveBtnText = R.string.accept,
                                            negativeBtnText = R.string.cancel,
                                            positiveClick = {
                                                // Hit Event
                                                try {
                                                    val bundle = Bundle()
                                                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                                                    bundle.putString("var_book_movie_disclaimer","accept")
                                                    GoogleAnalytics.hitEvent(context, "book_movie_disclaimer", bundle)
                                                }catch (e:Exception){
                                                    e.printStackTrace()
                                                }
                                                context.startActivity(intent)
                                            },
                                            negativeClick = {})
                                        dialog.show()
                                    } else {
                                        context.startActivity(intent)
                                    }
                                },
                                negativeClick = {
                                    // Hit Event
                                    try {
                                        val bundle = Bundle()
                                        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                                        bundle.putString("var_book_movie_disclaimer","cancel")
                                        GoogleAnalytics.hitEvent(context, "book_movie_disclaimer", bundle)
                                    }catch (e:Exception){
                                        e.printStackTrace()
                                    }
                                })
                            dialog.show()
                        } else {
                            if (newAt != "") {
                                val dialog = OptionDialog(context,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    newAt,
                                    positiveBtnText = R.string.accept,
                                    negativeBtnText = R.string.cancel,
                                    positiveClick = {
                                        // Hit Event
                                        try {
                                            val bundle = Bundle()
                                            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                                            bundle.putString("var_book_movie_disclaimer","accept")
                                            GoogleAnalytics.hitEvent(context, "book_movie_disclaimer", bundle)
                                        }catch (e:Exception){
                                            e.printStackTrace()
                                        }
                                        context.startActivity(intent)
                                    },
                                    negativeClick = {
                                        // Hit Event
                                        try {
                                            val bundle = Bundle()
                                            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                                            bundle.putString("var_book_movie_disclaimer","cancel")
                                            GoogleAnalytics.hitEvent(context, "book_movie_disclaimer", bundle)
                                        }catch (e:Exception){
                                            e.printStackTrace()
                                        }
                                    })
                                dialog.show()
                            } else {
                                context.startActivity(intent)
                            }
                        }
                    },
                    negativeClick = {
                        // Hit Event
                        try {
                            val bundle = Bundle()
                            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                            bundle.putString("var_book_movie_disclaimer","cancel")
                            GoogleAnalytics.hitEvent(context, "book_movie_disclaimer", bundle)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    })
                dialog.show()

            } else {
                if (at != "") {
                    val dialog = OptionDialog(context,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        at,
                        positiveBtnText = R.string.accept,
                        negativeBtnText = R.string.cancel,
                        positiveClick = {
                            if (newAt != "") {
                                val dialog = OptionDialog(context,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    newAt,
                                    positiveBtnText = R.string.accept,
                                    negativeBtnText = R.string.cancel,
                                    positiveClick = {
                                        // Hit Event
                                        try {
                                            val bundle = Bundle()
                                            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                                            bundle.putString("var_book_movie_disclaimer","accept")
                                            GoogleAnalytics.hitEvent(context, "book_movie_disclaimer", bundle)
                                        }catch (e:Exception){
                                            e.printStackTrace()
                                        }
                                        context.startActivity(intent)
                                    },
                                    negativeClick = {
                                        // Hit Event
                                        try {
                                            val bundle = Bundle()
                                            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                                            bundle.putString("var_book_movie_disclaimer","cancel")
                                            GoogleAnalytics.hitEvent(context, "book_movie_disclaimer", bundle)
                                        }catch (e:Exception){
                                            e.printStackTrace()
                                        }
                                    })
                                dialog.show()
                            } else {
                                context.startActivity(intent)
                            }
                        },
                        negativeClick = {
                            // Hit Event
                            try {
                                val bundle = Bundle()
                                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                                bundle.putString("var_book_movie_disclaimer","cancel")
                                GoogleAnalytics.hitEvent(context, "book_movie_disclaimer", bundle)
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        })
                    dialog.show()
                } else {


                    if (newAt != "") {
                        val dialog = OptionDialog(context,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            newAt,
                            positiveBtnText = R.string.accept,
                            negativeBtnText = R.string.cancel,
                            positiveClick = {
                                // Hit Event
                                try {
                                    val bundle = Bundle()
                                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                                    bundle.putString("var_book_movie_disclaimer","accept")
                                    GoogleAnalytics.hitEvent(context, "book_movie_disclaimer", bundle)
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                                context.startActivity(intent)
                            },
                            negativeClick = {
                                // Hit Event
                                try {
                                    val bundle = Bundle()
                                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                                    bundle.putString("var_book_movie_disclaimer","cancel")
                                    GoogleAnalytics.hitEvent(context, "book_movie_disclaimer", bundle)
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            })
                        dialog.show()
                    } else {
                        context.startActivity(intent)
                    }
                }
            }

        }

        applyOffer.setOnClickListener {
            if (discountPrice > 0){
                dialog.dismiss()
                sidText = sid.toString()
                ccText = cc

                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
//                                bundle.putString("var_FnB_food_type","veg")
                    GoogleAnalytics.hitEvent(context, "movie_show_time", bundle)
                    GoogleAnalytics.hitEvent(context, "book_apply_offer", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                val intent = Intent(context, SeatLayoutActivity::class.java)
                intent.putExtra("clickPosition", position.toString())
                intent.putExtra("shows", nowShowingList)
                intent.putExtra("skip", "false")
                intent.putExtra("from", "cinema")
                intent.putExtra("discountPrice", discountPrice.toString())
                if (adlt) {
                    val dialog = OptionDialog(context,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        context.getString(R.string.adult_msz),
                        positiveBtnText = R.string.accept,
                        negativeBtnText = R.string.cancel,
                        positiveClick = {
                            if (at != "") {
                                val dialog = OptionDialog(context,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    at,
                                    positiveBtnText = R.string.accept,
                                    negativeBtnText = R.string.cancel,
                                    positiveClick = {
                                        if (newAt != "") {
                                            val dialog = OptionDialog(context,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                newAt,
                                                positiveBtnText = R.string.accept,
                                                negativeBtnText = R.string.cancel,
                                                positiveClick = {
                                                    context.startActivity(intent)
                                                },
                                                negativeClick = {})
                                            dialog.show()
                                        } else {
                                            context.startActivity(intent)
                                        }
                                    },
                                    negativeClick = {})
                                dialog.show()
                            } else {
                                if (newAt != "") {
                                    val dialog = OptionDialog(context,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        newAt,
                                        positiveBtnText = R.string.accept,
                                        negativeBtnText = R.string.cancel,
                                        positiveClick = {
                                            context.startActivity(intent)
                                        },
                                        negativeClick = {})
                                    dialog.show()
                                } else {
                                    context.startActivity(intent)
                                }
                            }
                        },
                        negativeClick = {})
                    dialog.show()

                } else {
                    if (at != "") {
                        val dialog = OptionDialog(context,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            at,
                            positiveBtnText = R.string.accept,
                            negativeBtnText = R.string.cancel,
                            positiveClick = {
                                if (newAt != "") {
                                    val dialog = OptionDialog(context,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        newAt,
                                        positiveBtnText = R.string.accept,
                                        negativeBtnText = R.string.cancel,
                                        positiveClick = {
                                            context.startActivity(intent)
                                        },
                                        negativeClick = {})
                                    dialog.show()
                                } else {
                                    context.startActivity(intent)
                                }
                            },
                            negativeClick = {})
                        dialog.show()
                    } else {
                        if (newAt != "") {
                            val dialog = OptionDialog(context,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                newAt,
                                positiveBtnText = R.string.accept,
                                negativeBtnText = R.string.cancel,
                                positiveClick = {
                                    context.startActivity(intent)
                                },
                                negativeClick = {})
                            dialog.show()
                        } else {
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }

}