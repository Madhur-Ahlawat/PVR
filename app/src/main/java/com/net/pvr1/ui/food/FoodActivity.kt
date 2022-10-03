package com.net.pvr1.ui.food

import android.app.Dialog
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityFoodBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.adapter.*
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.ui.food.viewModel.FoodViewModel
import com.net.pvr1.ui.summery.SummeryActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodActivity : AppCompatActivity(),
    FoodBestSellerAdapter.RecycleViewItemClickListenerCity,
    CategoryAdapter.RecycleViewItemClickListenerCity,
    FilterAdapter.RecycleViewItemClickListenerCity,
    FilterBottomAdapter.RecycleViewItemClickListenerCity,
    CartAdapter.RecycleViewItemClickListenerCity {


//    private lateinit var preferences: AppPreferences
    private var binding: ActivityFoodBinding? = null
    private val authViewModel: FoodViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private val cartModel: ArrayList<CartModel> = arrayListOf()
    private var filterResponse: ArrayList<FoodResponse.Output.Mfl>? = null
    private var foodResponse: FoodResponse.Output? = null
    private var up: Boolean = false

    private var foodBestSellerAdapter: FoodBestSellerAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var filterAdapter: FilterAdapter? = null
    private var filterBottomAdapter: FilterBottomAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        authViewModel.food(
            "pGnnlj1MEjb0MOKBx1EH5w==",
            "GAUR",
            "",
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
        foodDetails()
    }

    private fun foodDetails() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        foodResponse = it.data.output
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
        categoryAdapter = CategoryAdapter(output.cat, this, this)
        binding?.recyclerView20?.layoutManager = layoutManager
        binding?.recyclerView20?.adapter = categoryAdapter
        binding?.recyclerView20?.setHasFixedSize(true)

    }

    override fun foodBestImageClick(comingSoonItem: FoodResponse.Output.Bestseller) {

    }

    override fun addFood(comingSoonItem: FoodResponse.Output.Bestseller, position: Int) {
        var num = comingSoonItem.qt
        num += 1
        comingSoonItem.qt = num
        updateBestSellerCartList(comingSoonItem)
        cartData()
        movedNext()
    }

    private fun movedNext() {
        binding?.textView148?.setOnClickListener {
            val intent = Intent(this@FoodActivity, SummeryActivity::class.java)
            startActivity(intent)
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
                    comingSoonItem.veg
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
                        comingSoonItem.veg
                    )
                )
            }
        }
    }

    // Chek Item Ability
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

    private fun updateHomeCartList(comingSoonItem: FoodResponse.Output.Bestseller) {
        if (cartModel.size == 0) {
            cartModel.add(
                CartModel(
                    comingSoonItem.cid,
                    comingSoonItem.nm,
                    comingSoonItem.mi,
                    comingSoonItem.qt,
                    comingSoonItem.dp,
                    comingSoonItem.veg
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
                        comingSoonItem.veg
                    )
                )
            }
        }
//        foodHomePrice (cartModel)
    }

    override fun categoryClick(comingSoonItem: FoodResponse.Output.Cat) {
        filterAdapter = FilterAdapter(getFilterCategoryList(comingSoonItem.name), this, this)
        binding?.recyclerView21?.adapter = filterAdapter
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView21?.setHasFixedSize(true)
        binding?.recyclerView21?.layoutManager = layoutManager
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
                    comingSoonItem.veg
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
                        comingSoonItem.veg
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
                    comingSoonItem.veg
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
                        comingSoonItem.veg
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

            binding?.textView151?.text = cartModel.size.toString()
            binding?.constraintLayout30?.show()
            val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val cartAdapter = CartAdapter(cartModel, this, this)
            binding?.recyclerView23?.layoutManager = layoutManager
            binding?.recyclerView23?.adapter = cartAdapter
        }


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

        }

    }

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

}