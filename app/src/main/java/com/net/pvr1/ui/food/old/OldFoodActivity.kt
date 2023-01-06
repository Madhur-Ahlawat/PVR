package com.net.pvr1.ui.food.old

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityOldFoodBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.CartModel
import com.net.pvr1.ui.food.adapter.CartAdapter
import com.net.pvr1.ui.food.old.adapter.OldAllFoodAdapter
import com.net.pvr1.ui.food.old.adapter.OldCategoryAdapter
import com.net.pvr1.ui.food.old.reponse.OldFoodResponse
import com.net.pvr1.ui.food.old.viewModel.OldFoodViewModel
import com.net.pvr1.ui.summery.SummeryActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class OldFoodActivity : AppCompatActivity(), OldCategoryAdapter.RecycleViewItemClickListener,
    OldAllFoodAdapter.RecycleViewItemClickListener, CartAdapter.RecycleViewItemClickListenerCity {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityOldFoodBinding? = null
    private val authViewModel: OldFoodViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var catFilter = ArrayList<OldFoodResponse.Output.R>()
    private val cartModel: ArrayList<CartModel> = arrayListOf()
    private var oldAllFoodAdapter: OldAllFoodAdapter? = null
    private var cartShow = false

    //foodLimit
    private var foodLimit: Int = 10
    private var itemCheckPriceCart: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOldFoodBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
//        authViewModel.food(
//            preferences.getUserId(),
//            "LOGG",
//            "LOGG220007711942",
//            "",
//            "",
//            "",
//            "",
//            "",
//            "",
//            "",
//            "",
//            "no"
//        )

        authViewModel.food(
            preferences.getUserId(),
            Constant.CINEMA_ID,
            Constant.BOOKING_ID,
            "",
            "",
            "",
            "",
            "",
            "",
            "no",
            "no",
            "no"
        )
        foodDataApi()
        movedNext()
    }

    private fun movedNext() {
        //search
        binding?.searchView?.setOnClickListener {
            binding?.searchUi?.show()
            binding?.toolbarUi?.hide()
        }
        //cancel Search
        binding?.include35?.cancelBtn?.setOnClickListener {
            binding?.searchUi?.hide()
            binding?.toolbarUi?.show()
        }
        //search Data
        binding?.include35?.editTextTextPersonName?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {

//                field1.setText("")
            }
        })
        //filter
        binding?.imageView17?.setOnClickListener {
            val popupMenu = PopupMenu(this, binding?.imageView17!!)
            // Inflating popup menu from popup_menu.xml file
            popupMenu.menuInflater.inflate(R.menu.old_food_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                // Toast message on menu item clicked
                val layoutManagerCrew2 =
                    GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                oldAllFoodAdapter =
                    OldAllFoodAdapter(getFilterAllMfl(menuItem.title.toString()), this, this)
                binding?.recyclerView58?.adapter = oldAllFoodAdapter
                binding?.recyclerView58?.setHasFixedSize(true)
                binding?.recyclerView58?.layoutManager = layoutManagerCrew2
                OldAllFoodAdapter(getFilterAllMfl(menuItem.title.toString()), this, this)

                true
            }
            // Showing the popup menu
            popupMenu.show()
        }

        //without Food
        binding?.textView381?.setOnClickListener {
            val intent = Intent(this, SummeryActivity::class.java)
            intent.putExtra(Constant.BOOK_TYPE, "BOOKING")
            intent.putExtra("food", cartModel)
            startActivity(intent)
        }
        //with Food
        binding?.include24?.textView5?.setOnClickListener {
            val intent = Intent(this, SummeryActivity::class.java)
            intent.putExtra(Constant.BOOK_TYPE, "BOOKING")
            intent.putExtra("food", cartModel)
            startActivity(intent)
        }
    }

    private fun foodDataApi() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        catFilter = it.data.output.r
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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: OldFoodResponse.Output) {
        //Category
        val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val categoryAdapter = OldCategoryAdapter(output.cat, this, this)
        binding?.recyclerView57?.layoutManager = layoutManager
        binding?.recyclerView57?.adapter = categoryAdapter
        binding?.recyclerView57?.setHasFixedSize(true)


        val layoutManagerCrew2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        oldAllFoodAdapter = OldAllFoodAdapter(getFilterAllMfl("ALL"), this, this)
        binding?.recyclerView58?.adapter = oldAllFoodAdapter
        binding?.recyclerView58?.setHasFixedSize(true)
        binding?.recyclerView58?.layoutManager = layoutManagerCrew2
    }

    override fun categoryClick(string: String) {
        val layoutManagerCrew2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        oldAllFoodAdapter = OldAllFoodAdapter(getFilterAllMfl(string), this, this)
        binding?.recyclerView58?.adapter = oldAllFoodAdapter
        binding?.recyclerView58?.setHasFixedSize(true)
        binding?.recyclerView58?.layoutManager = layoutManagerCrew2
    }

    private fun getFilterAllMfl(name: String): ArrayList<OldFoodResponse.Output.R> {
        val categoryFilterNew = ArrayList<OldFoodResponse.Output.R>()
        when (name) {
            "ALL" -> {
                for (data in catFilter) {
                    categoryFilterNew.add(data)
                }
            }
            "Non Veg" -> {
                for (data in catFilter) {
                    if (!data.veg) categoryFilterNew.add(data)

                }
            }
            "Veg" -> {
                for (data in catFilter) {
                    if (data.veg) categoryFilterNew.add(data)
                }
            }
            else -> {
                for (data in catFilter) {
                    if (data.ct == name) {
                        categoryFilterNew.add(data)
                    }
                }
            }
        }
        return categoryFilterNew
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun foodClick(comingSoonItem: OldFoodResponse.Output.R) {
        var num = comingSoonItem.qt
        num += 1
        comingSoonItem.qt = num
        updateFood(comingSoonItem)
        oldAllFoodAdapter?.notifyDataSetChanged()
        cartData()
    }

    override fun foodAddClick(comingSoonItem: OldFoodResponse.Output.R) {
        var num = comingSoonItem.qt
        if (num > foodLimit || num == foodLimit) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.max_item_msz) + " " + foodLimit + " " + getString(R.string.items_a_time),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num += 1
            comingSoonItem.qt = num
            updateFood(comingSoonItem)
            cartData()
        }

    }

    override fun foodSubtractClick(comingSoonItem: OldFoodResponse.Output.R) {
        var num = comingSoonItem.qt
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
            comingSoonItem.qt = num
            updateFood(comingSoonItem)
            cartData()
        }
    }

    private fun updateFood(comingSoonItem: OldFoodResponse.Output.R) {
        if (cartModel.size == 0) {
            cartModel.add(
                CartModel(
                    comingSoonItem.id.toInt(),
                    comingSoonItem.h,
                    comingSoonItem.i,
                    comingSoonItem.qt,
                    comingSoonItem.dp,
                    comingSoonItem.veg,
                    comingSoonItem.ho,
                    ""
                )
            )
        } else {
            if (updateCategoryItemExist(comingSoonItem)) {
                for (item in cartModel) {
                    if (item.id == comingSoonItem.id.toInt()) {
                        if (comingSoonItem.qt == 0) {
                            cartModel.remove(item)
                        } else {
                            item.quantity = comingSoonItem.qt

                        }
                        break
                    }
                }
            } else {
                cartModel.add(
                    CartModel(
                        comingSoonItem.id.toInt(),
                        comingSoonItem.h,
                        comingSoonItem.i,
                        comingSoonItem.qt,
                        comingSoonItem.dp,
                        comingSoonItem.veg,
                        comingSoonItem.ho,
                        ""
                    )
                )
            }
        }
    }

    private fun updateCategoryItemExist(foodItem: OldFoodResponse.Output.R): Boolean {
        for (item in cartModel) {
            if (item.id == foodItem.id.toInt()) {
                return true
            }
        }
        return false
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables", "NotifyDataSetChanged")
    private fun cartData() {

        oldAllFoodAdapter?.notifyDataSetChanged()
        if (cartModel.isEmpty()) {
            binding?.constraintLayout30?.hide()
            binding?.constraintLayout35?.hide()
        } else {
            binding?.constraintLayout35?.show()
            binding?.imageView74?.setOnClickListener {
                if (binding?.constraintLayout30?.visibility == View.VISIBLE) {
                    binding?.imageView74?.setImageDrawable(this.getDrawable(R.drawable.arrow_down))
                    binding?.constraintLayout30?.hide()
                } else {
                    binding?.imageView74?.setImageDrawable(this.getDrawable(R.drawable.arrow_up))
                    binding?.constraintLayout30?.show()
                }

            }

            //total Price
            itemCheckPriceCart = calculateTotal()
            binding?.textView148?.text =
                getString(R.string.currency) + Constant.DECIFORMAT.format(itemCheckPriceCart / 100.0)
            binding?.textView149?.text = cartModel.size.toString() + " " + getString(R.string.items)
            val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val cartAdapter = CartAdapter(cartModel, this, this)
            binding?.recyclerView41?.layoutManager = layoutManager
            binding?.recyclerView41?.adapter = cartAdapter

        }
    }

    //Calculate Total Item Price
    private fun calculateTotal(): Int {
        var totalPrice = 0
        for (data in cartModel) {
            totalPrice += data.price * data.quantity
        }
        return totalPrice
    }

    //    Cart
    override fun cartFoodPlus(comingSoonItem: CartModel, position: Int) {
        var num = comingSoonItem.quantity
        if (num > foodLimit || num == foodLimit) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.max_item_msz) + " " + foodLimit + " " + getString(R.string.items_a_time),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num += 1
            comingSoonItem.quantity = num
            updateCartFoodCartList(comingSoonItem)
            cartData()
        }
    }

    override fun cartFoodMinus(comingSoonItem: CartModel, position: Int) {
        var num = comingSoonItem.quantity
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
            comingSoonItem.quantity = num
            updateCartFoodCartList(comingSoonItem)
            cartData()
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
            updateCartMainList(recyclerData)
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

    private fun updateCartMainList(recyclerData: CartModel) {
        for (item in catFilter) {
            if (recyclerData.id == item.id.toInt()) {
                item.qt = recyclerData.quantity
            }
        }
        cartData()
    }

}