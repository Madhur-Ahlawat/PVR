package com.net.pvr1.ui.food

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityFoodBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.adapter.*
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.ui.food.viewModel.FoodViewModel
import com.net.pvr1.ui.summery.SummeryActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FoodActivity : AppCompatActivity(),
    FoodBestSellerAdapter.RecycleViewItemClickListenerCity,
    CategoryAdapter.RecycleViewItemClickListenerCity,
    FilterAdapter.RecycleViewItemClickListenerCity,
    FilterBottomAdapter.RecycleViewItemClickListenerCity,
    CartAdapter.RecycleViewItemClickListenerCity, AllFoodAdapter.RecycleViewItemClickListenerCity {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityFoodBinding? = null
    private val authViewModel: FoodViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private val cartModel: ArrayList<CartModel> = arrayListOf()
    private var filterResponse: ArrayList<FoodResponse.Output.Mfl>? = null
    private var foodResponse: FoodResponse.Output? = null
    private var foodResponseCategory: ArrayList<FoodResponse.Output.Cat> = arrayListOf()
    private var up: Boolean = false
    private var foodBestSellerAdapter: FoodBestSellerAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var filterAdapter: FilterAdapter? = null
    private var filterBottomAdapter: FilterBottomAdapter? = null
    private var masterId: String? = "0"

    private var itemCheckPriceCart: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
//        authViewModel.food(
//            preferences.getUserId().toString(),
//            CINEMA_ID,
//            BOOKING_ID,
//            "",
//            "",
//            "",
//            "",
//            "",
//            "",
//            "no",
//            "no",
//            "no"
//        )


        authViewModel.food(
            preferences.getUserId().toString(),
            "GURM",
            "GURM220009921660",
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

        movedNext()
        foodDetails()
    }

    private fun foodDetails() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        foodResponse = it.data.output
                        foodResponseCategory.add(FoodResponse.Output.Cat("", "ALL", 0))
                        foodResponseCategory.addAll(it.data.output.cat)
                        filterResponse = it.data.output.mfl
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: FoodResponse.Output) {
        //Food
        val layoutManagerCrew = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        foodBestSellerAdapter = FoodBestSellerAdapter(output.bestsellers, this, this)
        binding?.recyclerView19?.layoutManager = layoutManagerCrew
        binding?.recyclerView19?.adapter = foodBestSellerAdapter
        binding?.recyclerView19?.setHasFixedSize(true)
        //Category
        val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(foodResponseCategory, this, this)

        binding?.recyclerView20?.layoutManager = layoutManager
        binding?.recyclerView20?.adapter = categoryAdapter
        binding?.recyclerView20?.setHasFixedSize(true)

        //Sub Category
        val layoutManagerCrew2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
//        filterAdapter = FilterAdapter(getFilterCategoryList(output.cat[0].name), this, this)
//        binding?.recyclerView21?.adapter = filterAdapter
//        binding?.recyclerView21?.setHasFixedSize(true)
//        binding?.recyclerView21?.layoutManager = layoutManagerCrew2
//        val layoutManagerCrew = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val allFoodAdapter = foodResponse?.mfl?.let { AllFoodAdapter(it, this, this) }
        binding?.recyclerView21?.adapter = allFoodAdapter
        binding?.recyclerView21?.setHasFixedSize(true)
        binding?.recyclerView21?.layoutManager = layoutManagerCrew2
    }

    override fun foodBestImageClick(comingSoonItem: FoodResponse.Output.Bestseller) {

    }

    override fun addFood(comingSoonItem: FoodResponse.Output.Bestseller, position: Int) {
        masterId = comingSoonItem.mid.toString()
        var num = comingSoonItem.qt
        num += 1
        comingSoonItem.qt = num
        updateBestSellerCartList(comingSoonItem)
        cartData()
    }

    private fun movedNext() {
        binding?.toolbar?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()

        }
        binding?.toolbar?.textView108?.text=getString(R.string.order_snack)
        binding?.include24?.textView5?.text=getString(R.string.submit)
        binding?.include24?.textView5?.setOnClickListener {
            val intent = Intent(this@FoodActivity, SummeryActivity::class.java)
            intent.putExtra("food", cartModel)
            startActivity(intent)
        }

        //veg
        binding?.switch2?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The switch enabled
//                binding?.switch2?.

            } else {
                // The switch disabled
//                text_view.text = "Switch off"

            }
        }
        //Nonveg
        binding?.switch3?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The switch enabled
//                text_view.text = "Switch on"

            } else {
                // The switch disabled
//                text_view.text = "Switch off"

            }
        }
    }

    private fun updateBestSellerCartList(comingSoonItem: FoodResponse.Output.Bestseller) {
        if (cartModel.size == 0) {
            cartModel.add(
                CartModel(
                    comingSoonItem.cid,
                    comingSoonItem.nm,
                    comingSoonItem.mi,
                    comingSoonItem.qt,
                    comingSoonItem.dp,
                    comingSoonItem.veg,
                    comingSoonItem.r[0].ho,
                    comingSoonItem.mid.toString(),
                )
            )
        } else {
            if (itemExist(comingSoonItem)) {
                for (item in cartModel) {
                    if (item.id == comingSoonItem.cid) {
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
                        comingSoonItem.cid,
                        comingSoonItem.nm,
                        comingSoonItem.mi,
                        comingSoonItem.qt,
                        comingSoonItem.dp,
                        comingSoonItem.veg,
                        comingSoonItem.r[0].ho,
                        comingSoonItem.mid.toString()
                    )
                )
            }
        }
    }

    // CheckItem Ability
    private fun itemExist(foodItem: FoodResponse.Output.Bestseller): Boolean {
        for (item in cartModel) {
            if (item.id == foodItem.cid) {
                return true
            }
        }
        return false
    }

    override fun addFoodPlus(comingSoonItem: FoodResponse.Output.Bestseller, position: Int) {
        var num = comingSoonItem.qt
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
            comingSoonItem.qt = num
            cartData()
            updateHomeCartList(comingSoonItem)
        }
    }

    override fun addFoodMinus(comingSoonItem: FoodResponse.Output.Bestseller, position: Int) {
        var num = comingSoonItem.qt
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
            Toast.makeText(this, "sorry", Toast.LENGTH_LONG).show()
        } else {
            num -= 1
            comingSoonItem.qt = num
            cartData()
            updateHomeCartList(comingSoonItem)
        }
    }

    override fun bestSellerDialog( comingSoonItem: List<FoodResponse.Output.Bestseller.R>, position: String){
        bottomDialog(comingSoonItem, position)
    }

    private fun updateHomeCartList(comingSoonItem: FoodResponse.Output.Bestseller) {
        if (cartModel.size == 0) {
            cartModel.add(
                CartModel(
                    comingSoonItem.cid,
                    comingSoonItem.nm,
                    comingSoonItem.mi,
                    comingSoonItem.qt,
                    comingSoonItem.dp,
                    comingSoonItem.veg,
                    comingSoonItem.r[0].ho,
                    comingSoonItem.mid.toString()
                )
            )
        } else {
            if (itemExist(comingSoonItem)) {
                for (item in cartModel) {
                    if (item.id == comingSoonItem.cid) {
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
                        comingSoonItem.cid,
                        comingSoonItem.nm,
                        comingSoonItem.mi,
                        comingSoonItem.qt,
                        comingSoonItem.dp,
                        comingSoonItem.veg,
                        comingSoonItem.r[0].ho,
                        comingSoonItem.mid.toString()
                    )
                )
            }
        }
    }

    override fun categoryClick(comingSoonItem: FoodResponse.Output.Cat) {
        if (comingSoonItem.name == "ALL") {
            val layoutManagerCrew = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            val allFoodAdapter = foodResponse?.mfl?.let { AllFoodAdapter(it, this, this) }
            binding?.recyclerView21?.adapter = allFoodAdapter
            binding?.recyclerView21?.setHasFixedSize(true)
            binding?.recyclerView21?.layoutManager = layoutManagerCrew
        } else {
            val layoutManagerCrew = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            filterAdapter = FilterAdapter(getFilterCategoryList(comingSoonItem.name), this, this)
            binding?.recyclerView21?.adapter = filterAdapter
            binding?.recyclerView21?.setHasFixedSize(true)
            binding?.recyclerView21?.layoutManager = layoutManagerCrew
        }
    }

    private fun getFilterCategoryList(
        category: String
    ): ArrayList<FoodResponse.Output.Mfl> {
        val categoryFilterNew = ArrayList<FoodResponse.Output.Mfl>()
        for (data in filterResponse!!) {
            if (data.ctn == category) {
                categoryFilterNew.add(data)
            }
        }
        return categoryFilterNew
    }

    override fun categoryFoodClick(comingSoonItem: FoodResponse.Output.Mfl) {
        var num = comingSoonItem.qt
        num += 1
        comingSoonItem.qt = num
        updateCategoryFoodCartList(comingSoonItem)
        cartData()
    }

    override fun categoryFoodPlus(comingSoonItem: FoodResponse.Output.Mfl, position: Int) {
        var num = comingSoonItem.qt
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
            comingSoonItem.qt = num
            cartData()
            updateCategoryFoodCartList(comingSoonItem)
        }
    }

    override fun categoryFoodMinus(comingSoonItem: FoodResponse.Output.Mfl, position: Int) {
        var num = comingSoonItem.qt
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
            comingSoonItem.qt = num
            cartData()
            updateCategoryFoodCartList(comingSoonItem)
        }
    }

    override fun categoryFoodDialog(
        comingSoonItem: List<FoodResponse.Output.Bestseller.R>,
        title: String
    ) {
        bottomDialog(comingSoonItem, title)
    }

    private fun updateCategoryFoodCartList(comingSoonItem: FoodResponse.Output.Mfl) {
        if (cartModel.size == 0) {
            cartModel.add(
                CartModel(
                    comingSoonItem.cid,
                    comingSoonItem.nm,
                    comingSoonItem.mi,
                    comingSoonItem.qt,
                    comingSoonItem.dp,
                    comingSoonItem.veg,
                    comingSoonItem.r[0].ho,
                    comingSoonItem.mid.toString()
                )
            )
        } else {
            if (updateCategoryItemExist(comingSoonItem)) {
                for (item in cartModel) {
                    if (item.id == comingSoonItem.cid) {
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
                        comingSoonItem.cid,
                        comingSoonItem.nm,
                        comingSoonItem.mi,
                        comingSoonItem.qt,
                        comingSoonItem.dp,
                        comingSoonItem.veg,
                        comingSoonItem.r[0].ho,
                        comingSoonItem.mid.toString()
                    )
                )
            }
        }
    }

    private fun updateCategoryItemExist(foodItem: FoodResponse.Output.Mfl): Boolean {
        for (item in cartModel) {
            if (item.id == foodItem.cid) {
                return true
            }
        }
        return false
    }

    private fun bottomDialog(
        comingSoonItem: List<FoodResponse.Output.Bestseller.R>, titleTxt: String
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.food_bottom_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView22)
        val cancel = dialog.findViewById<ImageView>(R.id.imageView71)
        val proceed = dialog.findViewById<TextView>(R.id.textView5)
        val title = dialog.findViewById<TextView>(R.id.textView143)

        val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        filterBottomAdapter = FilterBottomAdapter(comingSoonItem, this, this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = filterBottomAdapter

        title.text = titleTxt
        proceed.text = getString(R.string.proceed)
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        proceed.setOnClickListener {
            cartData()
            dialog.dismiss()
        }
    }

    override fun filterBtFoodClick(comingSoonItem: FoodResponse.Output.Bestseller.R) {
        var num = comingSoonItem.qt
        num += 1
        comingSoonItem.qt = num
        cartData()
        updateBottomFoodCartList(comingSoonItem)
    }

    override fun filterBtFoodPlus(comingSoonItem: FoodResponse.Output.Bestseller.R) {
        var num = comingSoonItem.qt
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
            comingSoonItem.qt = num
            cartData()
            updateBottomFoodCartList(comingSoonItem)
        }
    }

    override fun filterBtFoodMinus(comingSoonItem: FoodResponse.Output.Bestseller.R) {
        var num = comingSoonItem.qt
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
            comingSoonItem.qt = num
            cartData()
            updateBottomFoodCartList(comingSoonItem)
        }
    }

    private fun updateBottomFoodCartList(comingSoonItem: FoodResponse.Output.Bestseller.R) {
        if (cartModel.size == 0) {
            cartModel.add(
                CartModel(
                    comingSoonItem.id,
                    comingSoonItem.h,
                    comingSoonItem.i,
                    comingSoonItem.qt,
                    comingSoonItem.dp,
                    comingSoonItem.veg,
                    comingSoonItem.ho,
                    masterId!!
                )
            )
        } else {
            if (updateBottomItemExist(comingSoonItem)) {
                for (item in cartModel) {
                    if (item.id == comingSoonItem.id) {
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
                        comingSoonItem.id,
                        comingSoonItem.h,
                        comingSoonItem.i,
                        comingSoonItem.qt,
                        comingSoonItem.dp,
                        comingSoonItem.veg,
                        comingSoonItem.ho,
                        masterId!!
                    )
                )
            }
        }
    }

    private fun updateBottomItemExist(foodItem: FoodResponse.Output.Bestseller.R): Boolean {
        for (item in cartModel) {
            if (item.id == foodItem.id) {
                return true
            }
        }
        return false
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n", "NotifyDataSetChanged")
    private fun cartData() {
        printLog("cartUpdate--->")
        foodBestSellerAdapter?.notifyDataSetChanged()
        categoryAdapter?.notifyDataSetChanged()
        filterAdapter?.notifyDataSetChanged()
        filterBottomAdapter?.notifyDataSetChanged()
        if (cartModel.isEmpty()) {
            binding?.constraintLayout30?.hide()
        } else {
            binding?.imageView74?.setOnClickListener {
                if (up) {
                    up = false
                    binding?.imageView74?.setImageDrawable(this.getDrawable(R.drawable.arrow_up))
                    binding?.recyclerView23?.show()
                } else {
                    up = true
                    binding?.imageView74?.setImageDrawable(this.getDrawable(R.drawable.arrow_down))
                    binding?.recyclerView23?.hide()
                }
            }

            //calculate Price
            binding?.textView148?.text=""

            //total Price
            itemCheckPriceCart = calculateTotal()
            binding?.textView148?.text = "₹ " + Constant.DECIFORMAT.format(itemCheckPriceCart / 100.0)

            binding?.textView149?.text = cartModel.size.toString()+" "+getString(R.string.items)
            binding?.constraintLayout30?.show()
            val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val cartAdapter = CartAdapter(cartModel, this, this)
            binding?.recyclerView23?.layoutManager = layoutManager
            binding?.recyclerView23?.adapter = cartAdapter
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


    override fun cartFoodPlus(comingSoonItem: CartModel, position: Int) {
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
            cartData()

            updateCartFoodCartList(comingSoonItem)
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
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
        } else {
            num -= 1
            comingSoonItem.quantity = num
            cartData()
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

            updateCartMainList(recyclerData)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCartMainList(recyclerData: CartModel) {
        for (item in foodResponse!!.bestsellers) {
            if (item.cid == recyclerData.id) {
                if (recyclerData.id == item.cid) {
                    item.qt = recyclerData.quantity
                }
            }
        }
        cartData()
        //Update Home List
        foodBestSellerAdapter?.notifyDataSetChanged()
    }

    private fun removeCartItem(item: CartModel) {
        for (data in cartModel) {
            if (data.id == item.id) {
                cartModel.remove(data)
            }
        }
    }

    override fun allFoodClick(comingSoonItem: FoodResponse.Output.Mfl) {
        var num = comingSoonItem.qt
        num += 1
        comingSoonItem.qt = num
        updateCategoryFoodCartList(comingSoonItem)
        cartData()
    }

    override fun allFoodPlus(comingSoonItem: FoodResponse.Output.Mfl, position: Int) {
        var num = comingSoonItem.qt
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
            comingSoonItem.qt = num
            cartData()
            updateCategoryFoodCartList(comingSoonItem)
        }
    }

    override fun allFoodMinus(comingSoonItem: FoodResponse.Output.Mfl, position: Int) {

        var num = comingSoonItem.qt
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
            comingSoonItem.qt = num
            cartData()
            updateCategoryFoodCartList(comingSoonItem)
        }
    }

    override fun allFoodDialog(
        comingSoonItem: List<FoodResponse.Output.Bestseller.R>,
        title: String
    ) {
        bottomDialog(comingSoonItem, title)

    }

}