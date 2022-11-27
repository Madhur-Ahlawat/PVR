package com.net.pvr1.ui.food

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityFoodBinding
import com.net.pvr1.databinding.FoodBottomAddFoodBinding
import com.net.pvr1.databinding.FoodCartDialogBinding
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
    SubFoodBestSellerAdapter.RecycleViewItemClickListenerCity,
    CategoryAdapter.RecycleViewItemClickListenerCity,
    AllFoodAdapter.RecycleViewItemClickListenerCity,
    SubAllFoodAdapter.RecycleViewItemClickListenerCity,
    CartAdapter.RecycleViewItemClickListenerCity{
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityFoodBinding? = null
    private val authViewModel: FoodViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private val cartModel: ArrayList<CartModel> = arrayListOf()
    private var filterResponse: ArrayList<FoodResponse.Output.Mfl>? = null
    private var catFilter = ArrayList<FoodResponse.Output.Mfl>()
    private var catFilterBestSeller = ArrayList<FoodResponse.Output.Bestseller>()
    private var foodResponse: FoodResponse.Output? = null
    private var foodResponseCategory: ArrayList<FoodResponse.Output.Cat> = arrayListOf()
    private var subFoodBestSellerAdapter: SubFoodBestSellerAdapter? = null
    private var subAllFoodAdapter: SubAllFoodAdapter? = null
    private var allFoodAdapter: AllFoodAdapter? = null
    private var masterId: String? = "0"
    private var categoryName: String = "ALL"
    private var itemCheckPriceCart: Int = 0
    private var menuType = 0
    private var bestSellerType: String = "ALL"

    //foodLimit
    private var foodLimit: Int = 0
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
                        foodLimit = it.data.output.aqt
                        foodResponse = it.data.output
                        foodResponseCategory.add(FoodResponse.Output.Cat("", "ALL", 0))
                        foodResponseCategory.addAll(it.data.output.cat)
                        filterResponse = it.data.output.mfl
                        catFilter = it.data.output.mfl
                        catFilterBestSeller = it.data.output.bestsellers
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
        printLog("bestseller--->${output.bestsellers.size}")
        //Food
        val layoutManagerCrew = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        subFoodBestSellerAdapter = SubFoodBestSellerAdapter(output.bestsellers, this, this)
        binding?.recyclerView19?.layoutManager = layoutManagerCrew
        binding?.recyclerView19?.adapter = subFoodBestSellerAdapter
        binding?.recyclerView19?.setHasFixedSize(true)

        //Category
        val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val categoryAdapter = CategoryAdapter(foodResponseCategory, this, this)
        binding?.recyclerView20?.layoutManager = layoutManager
        binding?.recyclerView20?.adapter = categoryAdapter
        binding?.recyclerView20?.setHasFixedSize(true)

        //All Food
        val layoutManagerCrew2 =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        allFoodAdapter =
            AllFoodAdapter(getFilterAllMfl(true, menuType, "ALL"), this, this)
        binding?.recyclerView21?.adapter = allFoodAdapter
        binding?.recyclerView21?.setHasFixedSize(true)
        binding?.recyclerView21?.layoutManager = layoutManagerCrew2
    }

    private fun movedNext() {
        binding?.toolbar?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.toolbar?.textView108?.text = getString(R.string.order_snack)
        binding?.include24?.textView5?.text = getString(R.string.submit)
        binding?.include24?.textView5?.text = getString(R.string.proceed)
        binding?.include24?.textView5?.setOnClickListener {
            val intent = Intent(this@FoodActivity, SummeryActivity::class.java)
            intent.putExtra("food", cartModel)
            startActivity(intent)
        }

        //veg
        binding?.switch2?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                menuType = 1
                binding?.switch3?.isChecked = false
                bestSellerType = "Veg"
                val layoutManagerCrew2 =
                    GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                allFoodAdapter =
                    AllFoodAdapter(getFilterAllMfl(true, menuType, categoryName), this, this)
                binding?.recyclerView21?.adapter = allFoodAdapter
                binding?.recyclerView21?.setHasFixedSize(true)
                binding?.recyclerView21?.layoutManager = layoutManagerCrew2

//                catFilterBestSeller
                val layoutManagerCrew =
                    GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                subFoodBestSellerAdapter =
                    SubFoodBestSellerAdapter(
                        getFilterBestSellerList(true, menuType, bestSellerType),
                        this,
                        this
                    )
                binding?.recyclerView19?.layoutManager = layoutManagerCrew
                binding?.recyclerView19?.adapter = subFoodBestSellerAdapter
                binding?.recyclerView19?.setHasFixedSize(true)

            } else {
                if (binding?.switch2?.isChecked == false && binding?.switch3?.isChecked == false) {
                    bestSellerType = "ALL"
                    menuType = 0
                    val layoutManagerCrew2 =
                        GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                    allFoodAdapter =
                        AllFoodAdapter(getFilterAllMfl(true, menuType, categoryName), this, this)
                    binding?.recyclerView21?.adapter = allFoodAdapter
                    binding?.recyclerView21?.setHasFixedSize(true)
                    binding?.recyclerView21?.layoutManager = layoutManagerCrew2

//                catFilterBestSeller
                    val layoutManagerCrew =
                        GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                    subFoodBestSellerAdapter =
                        SubFoodBestSellerAdapter(
                            getFilterBestSellerList(
                                true,
                                menuType,
                                bestSellerType
                            ), this, this
                        )
                    binding?.recyclerView19?.layoutManager = layoutManagerCrew
                    binding?.recyclerView19?.adapter = subFoodBestSellerAdapter
                    binding?.recyclerView19?.setHasFixedSize(true)

                }
            }
        }
        //Non veg
        binding?.switch3?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                menuType = 2
                binding?.switch2?.isChecked = false
                bestSellerType = "Veg"

                val layoutManagerCrew2 =
                    GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                allFoodAdapter =
                    AllFoodAdapter(getFilterAllMfl(true, menuType, categoryName), this, this)
                binding?.recyclerView21?.adapter = allFoodAdapter
                binding?.recyclerView21?.setHasFixedSize(true)
                binding?.recyclerView21?.layoutManager = layoutManagerCrew2

                //catFilterBestSeller
                val layoutManagerCrew =
                    GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                subFoodBestSellerAdapter =
                    SubFoodBestSellerAdapter(
                        getFilterBestSellerList(true, menuType, bestSellerType),
                        this,
                        this
                    )
                binding?.recyclerView19?.layoutManager = layoutManagerCrew
                binding?.recyclerView19?.adapter = subFoodBestSellerAdapter
                binding?.recyclerView19?.setHasFixedSize(true)


            } else {
                if (binding?.switch2?.isChecked == false && binding?.switch3?.isChecked == false) {
                    bestSellerType = "ALL"

                    menuType = 0
                    val layoutManagerCrew2 =
                        GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                    allFoodAdapter =
                        AllFoodAdapter(getFilterAllMfl(true, menuType, categoryName), this, this)
                    binding?.recyclerView21?.adapter = allFoodAdapter
                    binding?.recyclerView21?.setHasFixedSize(true)
                    binding?.recyclerView21?.layoutManager = layoutManagerCrew2

//                catFilterBestSeller
                    val layoutManagerCrew =
                        GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                    subFoodBestSellerAdapter =
                        SubFoodBestSellerAdapter(
                            getFilterBestSellerList(
                                true,
                                menuType,
                                bestSellerType
                            ), this, this
                        )
                    binding?.recyclerView19?.layoutManager = layoutManagerCrew
                    binding?.recyclerView19?.adapter = subFoodBestSellerAdapter
                    binding?.recyclerView19?.setHasFixedSize(true)

                }
            }
        }
    }

    //Category By Name
    override fun categoryClick(comingSoonItem: FoodResponse.Output.Cat) {
        categoryName = comingSoonItem.name
        if (comingSoonItem.name == "ALL") {
            val layoutManagerCrew =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            allFoodAdapter =
                AllFoodAdapter(getFilterAllMfl(true, menuType, comingSoonItem.name), this, this)
            binding?.recyclerView21?.adapter = allFoodAdapter
            binding?.recyclerView21?.setHasFixedSize(true)
            binding?.recyclerView21?.layoutManager = layoutManagerCrew
        } else {
            val layoutManagerCrew =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            allFoodAdapter =
                AllFoodAdapter(getFilterAllMfl(true, menuType, comingSoonItem.name), this, this)
            binding?.recyclerView21?.adapter = allFoodAdapter
            binding?.recyclerView21?.setHasFixedSize(true)
            binding?.recyclerView21?.layoutManager = layoutManagerCrew
        }
    }

    // Add Food  BestSeller Action Manage
    override fun foodBestImageClick(comingSoonItem: FoodResponse.Output.Bestseller) {
        bottomDialogAddFood(comingSoonItem)
    }

    override fun addFood(comingSoonItem: FoodResponse.Output.Bestseller, position: Int) {
        masterId = comingSoonItem.mid.toString()
        var num = comingSoonItem.qt
        num += 1
        comingSoonItem.qt = num
        updateBestSellerCartList(comingSoonItem)
        cartData()
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
        if (num > foodLimit || num == foodLimit) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.max_item_msz) + " " + foodLimit + " " + getString(R.string.items_a_time),
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

    @SuppressLint("SetTextI18n")
    private fun bottomDialogAddFood(comingSoonItem: FoodResponse.Output.Bestseller) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.food_bottom_add_food)
        val inflater = LayoutInflater.from(this)
        val bindingBottom = FoodBottomAddFoodBinding.inflate(inflater)
        dialog.setContentView(bindingBottom.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
//Mange Veg Non-Veg
        if (comingSoonItem.veg) {
            bindingBottom.imageView69.setImageDrawable(this.getDrawable(R.drawable.veg_ic))
        } else {
            bindingBottom.imageView69.setImageDrawable(this.getDrawable(R.drawable.nonveg_ic))
        }
        //Image View
        Glide.with(this)
            .load(comingSoonItem.mi)
            .error(R.drawable.app_icon)
            .into(bindingBottom.imageView64)

//        //title
        bindingBottom.textView137.text = comingSoonItem.nm
//        //price
        bindingBottom.textView150.text =
            getString(R.string.currency) + " " + Constant.DECIFORMAT.format(comingSoonItem.dp / 100.0)
//manageUI
        if (comingSoonItem.qt > 0) {
            bindingBottom.textView309.hide()
            bindingBottom.consAddUi.show()
            bindingBottom.uiPlusMinus.foodCount.text = comingSoonItem.qt.toString()
        } else {
            bindingBottom.textView309.show()
            bindingBottom.consAddUi.hide()
        }

        bindingBottom.textView309.setOnClickListener {
            if (comingSoonItem.r.size > 1) {
                dialog.dismiss()
                bottomDialogCart(comingSoonItem.r, comingSoonItem.nm)
            } else {
                masterId = comingSoonItem.mid.toString()
                var num = comingSoonItem.qt
                num += 1
                comingSoonItem.qt = num
                updateBestSellerCartList(comingSoonItem)
                cartData()
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()

                bindingBottom.textView309.hide()
                bindingBottom.consAddUi.show()
            }
        }

        //Add Food
        bindingBottom.uiPlusMinus.plus.setOnClickListener {
            var num = comingSoonItem.qt
            if (num > foodLimit || num == foodLimit) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.max_item_msz) + " " + foodLimit + " " + getString(R.string.items_a_time),
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

                bindingBottom.uiPlusMinus.foodCount.text = num.toString()
                cartData()
                updateHomeCartList(comingSoonItem)
            }
        }
        // Subtract Food
        bindingBottom.uiPlusMinus.minus.setOnClickListener {
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
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()
                if (comingSoonItem.qt == 0) {
                    bindingBottom.textView309.show()
                    bindingBottom.consAddUi.hide()
                }
                cartData()
                updateHomeCartList(comingSoonItem)
            }
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

    private fun getFilterBestSellerList(
        category: Boolean,
        menuType: Int,
        bestSellerType: String
    ): List<FoodResponse.Output.Bestseller> {
        val categoryFilterNew = ArrayList<FoodResponse.Output.Bestseller>()
        when (menuType) {
            0 -> {
                if (bestSellerType == "ALL") {
                    for (data in catFilterBestSeller) {
                        categoryFilterNew.add(data)
                    }
                } else {
                    for (data in catFilterBestSeller) {
                        if (data.veg == category)
                            categoryFilterNew.add(data)
                    }
                }

            }
            1 -> {
                if (bestSellerType == "ALL") {
                    for (data in catFilterBestSeller) {
//                        if (data.veg == category && data.veg) {
                        categoryFilterNew.add(data)
//                        }
                    }
                } else {
                    for (data in catFilterBestSeller) {
                        if (data.veg == category && data.veg) {
                            categoryFilterNew.add(data)
                        }
                    }
                }


            }
            2 -> {
                if (bestSellerType == "ALL") {
                    for (data in catFilterBestSeller) {
//                        if (!data.veg == category && !data.veg) {
                        categoryFilterNew.add(data)
//                        }
                    }
                } else {
                    for (data in catFilterBestSeller) {
                        if (!data.veg == category && !data.veg) {
                            categoryFilterNew.add(data)
                        }
                    }
                }

            }
            else -> {
                if (bestSellerType == "ALL") {
                    for (data in catFilterBestSeller) {
//                        if (data.veg == category)
                        categoryFilterNew.add(data)
                    }
                } else {
                    for (data in catFilterBestSeller) {
                        if (data.veg == category)
                            categoryFilterNew.add(data)
                    }
                }


            }

        }
        return categoryFilterNew
    }


    override fun bestSellerDialogAddFood(
        comingSoonItem: List<FoodResponse.Output.Bestseller.R>,
        position: String
    ) {
        bottomDialogCart(comingSoonItem, position)
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

    override fun categoryFoodDialog(
        comingSoonItem: List<FoodResponse.Output.Bestseller.R>,
        title: String
    ) {
        bottomDialogCart(comingSoonItem, title)
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
        if (num > foodLimit || num == foodLimit) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.max_item_msz) + " " + foodLimit + " " + getString(R.string.items_a_time),
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

    //Calculate Total Item Price
    private fun calculateTotal(): Int {
        var totalPrice = 0
        for (data in cartModel) {
            totalPrice += data.price * data.quantity
        }
        return totalPrice
    }

    //Cart Action Manage
    override fun cartFoodPlus(comingSoonItem: CartModel, position: Int) {
        var num = comingSoonItem.quantity
        if (num > foodLimit || num == foodLimit) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.max_item_msz) + " " + foodLimit + " " + getString(R.string.items_a_time),
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
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
        } else {
            num -= 1
            comingSoonItem.quantity = num
            updateCartFoodCartList(comingSoonItem)
            cartData()
        }
    }

    private fun removeCartItem(item: CartModel) {
        for (data in cartModel) {
            if (data.id == item.id) {
                cartModel.remove(data)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "UseCompatLoadingForDrawables", "SetTextI18n")
    private fun cartData() {
        subFoodBestSellerAdapter?.notifyDataSetChanged()
        subAllFoodAdapter?.notifyDataSetChanged()
        allFoodAdapter?.notifyDataSetChanged()
        binding?.imageView74?.setImageDrawable(this.getDrawable(R.drawable.arrow_up))

        if (cartModel.isEmpty()) {
            binding?.constraintLayout30?.hide()
        } else {
            binding?.constraintLayout30?.show()
            binding?.imageView74?.setOnClickListener {
                cartDialog()
            }

            //total Price
            itemCheckPriceCart = calculateTotal()
            binding?.textView148?.text =
                getString(R.string.currency) + Constant.DECIFORMAT.format(itemCheckPriceCart / 100.0)

            binding?.textView149?.text = cartModel.size.toString() + " " + getString(R.string.items)
            binding?.constraintLayout30?.show()

        }

    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun cartDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val bindingBottom = FoodCartDialogBinding.inflate(inflater)
        dialog.setContentView(bindingBottom.root)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        bindingBottom.include26.textView5.text = getString(R.string.proceed)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()
        //total Items
        bindingBottom.textView149.text = cartModel.size.toString() + " " + getString(R.string.items)
        //total Price
        itemCheckPriceCart = calculateTotal()
        bindingBottom.textView148.text =
            getString(R.string.currency) + Constant.DECIFORMAT.format(itemCheckPriceCart / 100.0)

        //dismiss
        bindingBottom.imageView74.setImageDrawable(this.getDrawable(R.drawable.arrow_down))

        bindingBottom.imageView74.setOnClickListener {
            dialog.dismiss()
        }

        bindingBottom.include26.textView5.setOnClickListener {
            val intent = Intent(this@FoodActivity, SummeryActivity::class.java)
            intent.putExtra("food", cartModel)
            startActivity(intent)
        }

        val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val cartAdapter = CartAdapter(cartModel, this, this)
        bindingBottom.recyclerView41.layoutManager = layoutManager
        bindingBottom.recyclerView41.adapter = cartAdapter
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
    }

    private fun bottomDialogCart(
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
        subAllFoodAdapter = SubAllFoodAdapter(comingSoonItem, this, this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = subAllFoodAdapter

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

    // All  Food Mfl Action Manage

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

    //Mfl All Food
    private fun getFilterAllMfl(
        category: Boolean,
        menuType: Int,
        name: String
    ): ArrayList<FoodResponse.Output.Mfl> {
        val categoryFilterNew = ArrayList<FoodResponse.Output.Mfl>()
        when (menuType) {
            0 -> {
                if (name == "ALL") {
                    for (data in catFilter) {
                        categoryFilterNew.add(data)
                    }
                } else {
                    for (data in catFilter) {
                        if (data.ctn == name) {
                            categoryFilterNew.add(data)
                        }
                    }
                }

            }
            1 -> {
                for (data in catFilter) {
                    if (name == "ALL") {
                        if (data.veg == category && data.veg) {
                            categoryFilterNew.add(data)
                        }
                    } else {
                        if (data.ctn == name) {
                            if (data.veg == category && data.veg) {
                                categoryFilterNew.add(data)
                            }
                        }
                    }
                }
            }
            2 -> {
                for (data in catFilter) {
                    if (name == "ALL") {
                        if (!data.veg == category && !data.veg) {
                            categoryFilterNew.add(data)
                        }
                    } else {
                        if (data.ctn == name) {
                            if (!data.veg == category && !data.veg) {
                                categoryFilterNew.add(data)
                            }
                        }
                    }

                }
            }
            else -> {
                for (data in catFilter) {
                    if (name == "ALL") {
                        if (data.veg == category)
                            categoryFilterNew.add(data)
                    }
                    if (data.ctn == name) {
                        if (data.veg == category)
                            categoryFilterNew.add(data)
                    }

                }

            }

        }
        return categoryFilterNew
    }

    //Mfl All Food
    override fun categoryFoodClick(comingSoonItem: FoodResponse.Output.Mfl) {
        var num = comingSoonItem.qt
        num += 1
        comingSoonItem.qt = num
        cartData()
        updateCategoryFoodCartList(comingSoonItem)
    }

    override fun categoryFoodImageClick(comingSoonItem: FoodResponse.Output.Mfl) {
        bottomDialogAllFood(comingSoonItem)
    }

    override fun categoryFoodPlus(comingSoonItem: FoodResponse.Output.Mfl, position: Int) {
        var num = comingSoonItem.qt
        if (num > foodLimit || num == foodLimit) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.max_item_msz) + " " + foodLimit + " " + getString(R.string.items_a_time),
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

    @SuppressLint("SetTextI18n")
    private fun bottomDialogAllFood(comingSoonItem: FoodResponse.Output.Mfl) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val bindingBottom = FoodBottomAddFoodBinding.inflate(inflater)
        dialog.setContentView(bindingBottom.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
//Manage Veg-NonVeg
        if (comingSoonItem.veg) {
            bindingBottom.imageView69.setImageDrawable(this.getDrawable(R.drawable.veg_ic))
        } else {
            bindingBottom.imageView69.setImageDrawable(this.getDrawable(R.drawable.nonveg_ic))
        }
        //Image View
        Glide.with(this)
            .load(comingSoonItem.mi)
            .error(R.drawable.app_icon)
            .into(bindingBottom.imageView64)

//        //title
        bindingBottom.textView137.text = comingSoonItem.nm
//        //price
        bindingBottom.textView150.text =
            getString(R.string.currency) + " " + Constant.DECIFORMAT.format(comingSoonItem.dp / 100.0)
//manageUI
        if (comingSoonItem.qt != 0) {
            bindingBottom.textView309.hide()
            bindingBottom.consAddUi.show()
            bindingBottom.uiPlusMinus.foodCount.text = comingSoonItem.qt.toString()
        } else {
            bindingBottom.textView309.show()
            bindingBottom.consAddUi.hide()
        }

        bindingBottom.textView309.setOnClickListener {
            if (comingSoonItem.r.size > 1) {
                dialog.dismiss()
                bottomDialogCart(comingSoonItem.r, comingSoonItem.nm)
            } else {
                var num = comingSoonItem.qt
                num += 1
                comingSoonItem.qt = num
                updateCategoryFoodCartList(comingSoonItem)
                cartData()
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()

                bindingBottom.textView309.hide()
                bindingBottom.consAddUi.show()
            }
        }

        //Add Food
        bindingBottom.uiPlusMinus.plus.setOnClickListener {
            var num = comingSoonItem.qt
            if (num > foodLimit || num == foodLimit) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.max_item_msz) + " " + foodLimit + " " + getString(R.string.items_a_time),
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
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()
                cartData()
                updateCategoryFoodCartList(comingSoonItem)
            }
        }

        // Subtract Food
        bindingBottom.uiPlusMinus.minus.setOnClickListener {
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
                bindingBottom.uiPlusMinus.foodCount.text = num.toString()

                if (comingSoonItem.qt == 0) {
                    bindingBottom.textView309.show()
                    bindingBottom.consAddUi.hide()
                }
                cartData()
                updateCategoryFoodCartList(comingSoonItem)
            }
        }

    }

}