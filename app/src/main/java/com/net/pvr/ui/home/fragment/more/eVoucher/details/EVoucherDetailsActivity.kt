package com.net.pvr.ui.home.fragment.more.eVoucher.details

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ActivityEvoucherDetailsBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.more.eVoucher.adapter.VoucherListAdapter
import com.net.pvr.ui.home.fragment.more.eVoucher.details.adapter.VoucherAddAdapter
import com.net.pvr.ui.home.fragment.more.eVoucher.details.model.EVoucherCart
import com.net.pvr.ui.home.fragment.more.eVoucher.response.VoucherListResponse
import com.net.pvr.ui.home.fragment.more.eVoucher.viewModel.EVoucherViewModel
import com.net.pvr.ui.location.selectCity.SelectCityActivity
import com.net.pvr.utils.Constant
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import com.net.pvr.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class EVoucherDetailsActivity : AppCompatActivity(),
    VoucherAddAdapter.RecycleViewItemClickListener {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityEvoucherDetailsBinding? = null

    private var loader: LoaderDialog? = null
    private val authViewModel: EVoucherViewModel by viewModels()

    private var voucherDetShow = 0
    private var howWorkShow = 0
    private  var category =""

    private var voucherListResponse: ArrayList<VoucherListResponse.Output.Ev> = ArrayList()
    private var cartModel: ArrayList<EVoucherCart> = arrayListOf()

    private var voucherAddAdapter: VoucherAddAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvoucherDetailsBinding.inflate(layoutInflater, null, false)
        setContentView(binding?.root)

        manageFunction()
    }

    @SuppressLint("SetTextI18n")
    private fun manageFunction() {
        category = intent.getStringExtra("category").toString()
        voucherListResponse =
            intent.getSerializableExtra("list") as ArrayList<VoucherListResponse.Output.Ev>

        //title
        binding?.textView408?.text = intent.getStringExtra("name").toString()

        //validDate
        binding?.textView409?.text =
            getString(R.string.valid_till) + " " + Constant().dateFormatter(
                intent.getStringExtra("endDate").toString()
            )

        //Image
        Glide.with(this).load(intent.getStringExtra("image"))
            .error(R.drawable.placeholder_horizental).into(binding?.imageView133!!)

        //voucherDetails
        binding?.imageView179?.setOnClickListener {
            if (voucherDetShow == 0) {
                voucherDetShow = 1
                binding?.imageView179?.setImageResource(R.drawable.arrow_down)
                binding?.textView413?.show()
                //  details Txt
                binding?.textView414?.hide()
            } else {
                binding?.imageView179?.setImageResource(R.drawable.arrow_up)
                binding?.textView413?.hide()
                //  details Txt
                binding?.textView414?.show()
                binding?.textView414?.text = Html.fromHtml(
                    intent.getStringExtra("shortDesc").toString(), Html.FROM_HTML_MODE_LEGACY
                )
                voucherDetShow = 0
            }
        }

        //How It Work
        binding?.imageView181?.setOnClickListener {
            if (howWorkShow == 0) {
                howWorkShow = 1
                binding?.imageView181?.setImageResource(R.drawable.arrow_down)
                binding?.textView416?.show()
                binding?.imageView182?.hide()

            } else {
                howWorkShow = 0
                binding?.imageView181?.setImageResource(R.drawable.arrow_up)
                binding?.textView416?.hide()
                binding?.imageView182?.show()

            }
        }

        movedNext()
        categoryData()
        addGiftCard()
    }

    private fun addGiftCard() {


    }

    private fun categoryData() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView63?.layoutManager = layoutManager
        voucherAddAdapter = VoucherAddAdapter(this,filter(category) , this)
        binding?.recyclerView63?.adapter = voucherAddAdapter


        println("------------->${filter(category)}")
    }
    private fun filter(text: String): ArrayList<VoucherListResponse.Output.Ev> {
        val filtered: ArrayList<VoucherListResponse.Output.Ev> = java.util.ArrayList()
        val filtered1: ArrayList<VoucherListResponse.Output.Ev> = java.util.ArrayList()
        for (item in voucherListResponse) {
            if (item.voucherCategory.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filtered.add(item)
            }
        }
        return filtered
//            voucherAddAdapter?.filterVoucherSearchList(filtered1)
//        voucherListAdapter?.filterCinemaList(filtered1)

    }

    private fun movedNext() {
        binding?.include52?.textView108?.text = "Purchase Voucher"

        binding?.include52?.imageView58?.setOnClickListener {
            finish()
        }

        binding?.checkedTextView?.setOnCheckedChangeListener { _, isChecked ->
            // checkbox status is changed from uncheck to checked.
            if (isChecked) {
                binding?.checkedTextView?.buttonTintList =
                    ColorStateList.valueOf(this.getColor(R.color.black))
            } else {
                binding?.checkedTextView?.buttonTintList =
                    ColorStateList.valueOf(this.getColor(R.color.black))
            }
        }


        binding?.textView31?.setOnClickListener {
            if (binding?.checkedTextView?.isChecked == false) {

                binding?.checkedTextView?.buttonTintList =
                    ColorStateList.valueOf(this.getColor(R.color.red))
                val shake: Animation =
                    AnimationUtils.loadAnimation(this@EVoucherDetailsActivity, R.anim.shake)
                binding?.textView418?.startAnimation(shake)
                Constant().vibrateDevice(this)

            } else {
                val intent = Intent(this@EVoucherDetailsActivity, SelectCityActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun addItem(ev: VoucherListResponse.Output.Ev) {
        var num = ev.quantity
        num += 1
        ev.quantity = num
        updateCartList(ev)
    }

    override fun increaseItem(ev: VoucherListResponse.Output.Ev) {
        var num = ev.quantity
        if (num < 0 || num == ev.sellAllowedCount) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.min_item_msz),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num += 1
            ev.quantity = num
            updateCartList(ev)
        }
    }

    override fun decreaseItem(ev: VoucherListResponse.Output.Ev) {
        var num = ev.quantity
        if (num < 0 || num == 0) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.min_item_msz),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num -= 1
            ev.quantity = num
            updateCartList(ev)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculatePrice() {
        if (cartModel.size > 0) {
            binding?.constraintLayout182?.show()

            //totalPrice
            val itemCheckPrice = updatePrice()
            binding?.textView32?.text =
                getString(R.string.currency) + Constant.DECIFORMAT.format(itemCheckPrice / 100.0)
        } else {
            binding?.constraintLayout182?.hide()
        }
    }

    //Calculate Total Item Price
    private fun updatePrice(): Int {
        var totalPrice = 0
        for (data in cartModel) {
            totalPrice += data.price * data.quantity
        }
        return totalPrice
    }

    private fun updateCartList(comingSoonItem: VoucherListResponse.Output.Ev) {
        if (cartModel.size == 0) {
            cartModel.add(
                EVoucherCart(
                    comingSoonItem.pkBinDiscountId,
                    comingSoonItem.binDiscountName,
                    comingSoonItem.quantity,
                    comingSoonItem.sellAllowedValue,
                )
            )
        } else {
            if (itemExist(comingSoonItem)) {
                for (item in cartModel) {
                    if (item.id == comingSoonItem.pkBinDiscountId) {
                        if (comingSoonItem.quantity == 0) {
                            cartModel.remove(item)
                        } else {
                            item.quantity = comingSoonItem.quantity
                        }
                        break
                    }
                }
            } else {
                cartModel.add(
                    EVoucherCart(
                        comingSoonItem.pkBinDiscountId,
                        comingSoonItem.binDiscountName,
                        comingSoonItem.quantity,
                        comingSoonItem.sellAllowedValue,
                    )
                )
            }
        }
        calculatePrice()
    }

    private fun itemExist(foodItem: VoucherListResponse.Output.Ev): Boolean {
        for (item in cartModel) {
            if (item.id == foodItem.pkBinDiscountId) {
                return true
            }
        }
        return false
    }

}