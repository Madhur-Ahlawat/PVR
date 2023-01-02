package com.net.pvr1.ui.summery

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySummeryBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.CartModel
import com.net.pvr1.ui.payment.PaymentActivity
import com.net.pvr1.ui.seatLayout.adapter.AddFoodCartAdapter
import com.net.pvr1.ui.summery.adapter.SeatListAdapter
import com.net.pvr1.ui.summery.response.SummeryResponse
import com.net.pvr1.ui.summery.viewModel.SummeryViewModel
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.BOOKING_ID
import com.net.pvr1.utils.Constant.Companion.CINEMA_ID
import com.net.pvr1.utils.Constant.Companion.TRANSACTION_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SummeryActivity : AppCompatActivity(), AddFoodCartAdapter.RecycleViewItemClickListenerCity {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivitySummeryBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: SummeryViewModel by viewModels()
    private var cartModel: ArrayList<CartModel> = arrayListOf()
    private var itemDescription: String = ""
    private var showTaxes = false
    private var paidAmount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummeryBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        try {
            cartModel = intent.getSerializableExtra("food") as ArrayList<CartModel>
            printLog("exception--->${cartModel}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        authViewModel.summery(
            TRANSACTION_ID,
            CINEMA_ID,
            preferences.getUserId(),
            BOOKING_ID
        )
        movedNext()
        foodCart()
        summeryDetails()
        saveFood()
        setDonation()
    }

    //CartFood
    private fun foodCart() {
        if (cartModel.isNotEmpty()) {
            binding?.recyclerView32?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val foodBestSellerAdapter = AddFoodCartAdapter(cartModel, this, this)
            binding?.recyclerView32?.layoutManager = layoutManagerCrew
            binding?.recyclerView32?.adapter = foodBestSellerAdapter
            binding?.recyclerView32?.setHasFixedSize(true)
        } else {
            binding?.recyclerView32?.hide()
        }
    }

    private fun movedNext() {
        binding?.include7?.textView108?.text = getString(R.string.checkout)
        binding?.include7?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    //saveFood
    private fun saveFood() {
        authViewModel.seatWithFoodDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

                        authViewModel.setDonation(
                            BOOKING_ID,
                            TRANSACTION_ID, true, false, "NO"
                        )
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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    //Set Donation
    private fun setDonation() {
        authViewModel.setDonationDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

                        val intent = Intent(this@SummeryActivity, PaymentActivity::class.java)
                        intent.putExtra("paidAmount",paidAmount)
                        startActivity(intent)

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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    //SummeryDetails
    private fun summeryDetails() {
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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun retrieveData(output: SummeryResponse.Output) {
        //movie Details
        binding?.textView111?.text =
            output.cen + getString(R.string.dots) + output.lg + getString(R.string.dots) + output.fmt
        //audi
        binding?.textView115?.text = output.audi + "-" + output.st
        //ticket
        binding?.textView119?.text = output.f[0].it[0].n
        //price
        binding?.textView120?.text = getString(R.string.currency) + output.f[0].it[0].v
        //sub total
        binding?.textView168?.text = ""
        //coupon
        binding?.textView169?.text = ""

        var itList : List<SummeryResponse.Output.F.It> = ArrayList()
        try {

//           for (item in output.f){
//
//               if (item.n == "Taxes & Fees "){
//
//                   //taxes fee
//                   binding?.textView170?.text =getString(R.string.currency) + item.v
//                   itList = item.it
//                   for (item in itList){
//                       //Convenience fee
//                       binding?.textView272?.text =getString(R.string.currency) + item.v
//
//                       //Convenience Heading
//                       binding?.textView270?.text = item.n
//                       //gst
//                       binding?.textView273?.text = getString(R.string.currency) + item.v
//                       //gst Heading
//                       binding?.textView271?.text = item.n
//                   }
//               }
//           }

            //taxes fee
            binding?.textView170?.text =getString(R.string.currency) + output.f[1].v
            //Convenience fee
            binding?.textView272?.text = output.f[1].it[0].v
            //Convenience Heading
            binding?.textView270?.text = output.f[1].it[0].n
            //gst
            binding?.textView273?.text = output.f[1].it[1].v
            //gst Heading
            binding?.textView271?.text = output.f[1].it[1].n
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //caver calling
        binding?.textView171?.text = ""
        //2/ticket calling
//        binding?.textView172?.text = ""
//        grand total
        paidAmount=output.a
        binding?.textView173?.text = getString(R.string.currency) + output.a
//       pay
        binding?.textView174?.text =
            getString(R.string.pay) + " " + getString(R.string.currency) + output.a + " |"
        //Image
        Glide.with(this)
            .load(output.imh)
            .into(binding?.imageView59!!)

        //title
        binding?.textView110?.text = output.m
        //shows
        binding?.textView112?.text = output.md + ", " + output.t
        //location
        binding?.textView113?.text = output.c
        binding?.imageView60?.setOnClickListener {
            Constant().openMap(this, preferences.getLatitudeData(), preferences.getLongitudeData())
        }
        //total seat
        val ticketCount = output.seat.size * 2
        binding?.textView166?.text = ticketCount.toString() + "/" + getString(R.string.ticket)

        //audi
        binding?.textView115?.text = output.audi + "-" + output.st

        //seat
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        layoutManager.alignItems = AlignItems.STRETCH
        val adapter = SeatListAdapter(output.seat)
        binding?.recyclerView31?.setHasFixedSize(true)
        binding?.recyclerView31?.layoutManager = layoutManager
        binding?.recyclerView31?.adapter = adapter

        //taxes UnderLine
        binding?.textView164?.paintFlags =
            binding?.textView164!!.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //Show taxes
        binding?.textView164?.setOnClickListener {
            binding?.taxes?.show()
        }


        //manage taxes Shows
        binding?.imageView77?.setOnClickListener {
            showTaxes = if (!showTaxes) {
                binding?.imageView77?.setImageResource(R.drawable.arrow_down)
                binding?.taxes?.hide()
                true
            } else {
                binding?.imageView77?.setImageResource(R.drawable.arrow_up)
                binding?.taxes?.show()
                false
            }
        }

        //Cavery Dialog
        binding?.imageView147?.setOnClickListener {
            carveryDialog(output.don_stext)
        }

        //seatWithFood
        binding?.textView175?.setOnClickListener {
            cartModel.forEachIndexed { index, food ->
                itemDescription = if (index == 0) {
                    food.title + "|" + food.id + "|" + food.quantity +
                            "|" + food.price + "|" + food.ho + "|" + food.mid
                } else {
                    itemDescription + "#" + food.title + "|" + food.id + "|" + food.quantity +
                            "|" + food.price + "|" + food.ho + "|" + food.mid
                }
            }
//            printLog("seat--->${itemDescription},${TRANSACTION_ID},${CINEMA_ID},${ preferences.getUserId().toString()},${output.seats},${output.audi}")
            printLog(
                "seat--->${itemDescription},${TRANSACTION_ID},${CINEMA_ID},${
                    preferences.getUserId()
                },${output.seats},${output.audi}"
            )

            if (cartModel.isNotEmpty()) {
                authViewModel.seatWithFood(
                    itemDescription,
                    TRANSACTION_ID,
                    CINEMA_ID,
                    preferences.getUserId(),
                    "",
                    "",
                    "no",
                    output.seats,
                    output.audi
                )
            } else {
                val intent = Intent(this@SummeryActivity, PaymentActivity::class.java)
                intent.putExtra("paidAmount",output.a)
                startActivity(intent)
            }


        }
    }

    private fun carveryDialog(donStext: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.carvery_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()
        val terms = dialog.findViewById<TextView>(R.id.textView310)
        val carveryText = dialog.findViewById<TextView>(R.id.textView154)
        carveryText?.text = Html.fromHtml(donStext, Html.FROM_HTML_MODE_LEGACY)
        terms.paintFlags = terms.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    }

    override fun increaseFoodClick(comingSoonItem: CartModel) {
        var num = comingSoonItem.quantity
        if (num > 10 || num == 10) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.max_item_msz),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
        } else {
            num += 1
            comingSoonItem.quantity = num

            updateCartFoodCartList(comingSoonItem)
        }
    }

    override fun decreaseFoodClick(comingSoonItem: CartModel) {
        var num = comingSoonItem.quantity
        if (num < 0 || num == 0) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.min_item_msz),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
        } else {
            num -= 1
            comingSoonItem.quantity = num
            updateCartFoodCartList(comingSoonItem)
        }

    }

    private fun updateCartFoodCartList(recyclerData: CartModel) {
        try {
            for (item in cartModel) {
                if (item.id == recyclerData.id) {
                    if (recyclerData.quantity == 0) {
                        removeCartItem(item)
                    } else {
                        item.quantity = recyclerData.quantity
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun removeCartItem(item: CartModel) {
        for (data in cartModel) {
            if (data.id == item.id) {
                cartModel.remove(data)
            }
        }
    }

}