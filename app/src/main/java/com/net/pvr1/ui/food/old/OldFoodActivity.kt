package com.net.pvr1.ui.food.old

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityOldFoodBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.CartModel
import com.net.pvr1.ui.food.old.adapter.OldAllFoodAdapter
import com.net.pvr1.ui.food.old.adapter.OldCategoryAdapter
import com.net.pvr1.ui.food.old.reponse.OldFoodResponse
import com.net.pvr1.ui.food.old.viewModel.OldFoodViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OldFoodActivity : AppCompatActivity(),OldCategoryAdapter.RecycleViewItemClickListener ,OldAllFoodAdapter.RecycleViewItemClickListener{
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityOldFoodBinding? = null
    private val authViewModel: OldFoodViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private val catList: ArrayList<String> = ArrayList()
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
        authViewModel.food(preferences.getUserId(),"LOGG","LOGG220007711942","","","","","","","","","no")
        foodDataApi()
    }

    private fun foodDataApi() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        catFilter =it.data.output.r

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

    private fun retrieveData(output: OldFoodResponse.Output) {
        //Category
        val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val categoryAdapter = OldCategoryAdapter(output.cat, this, this)
        binding?.recyclerView57?.layoutManager = layoutManager
        binding?.recyclerView57?.adapter = categoryAdapter
        binding?.recyclerView57?.setHasFixedSize(true)


        val layoutManagerCrew2 =
            GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        oldAllFoodAdapter =
            OldAllFoodAdapter(getFilterAllMfl( "ALL"), this, this)
        binding?.recyclerView58?.adapter = oldAllFoodAdapter
        binding?.recyclerView58?.setHasFixedSize(true)
        binding?.recyclerView58?.layoutManager = layoutManagerCrew2
    }

    override fun categoryClick(string: String) {
        val layoutManagerCrew2 =
            GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        oldAllFoodAdapter=
            OldAllFoodAdapter(getFilterAllMfl( string), this, this)
        binding?.recyclerView58?.adapter = oldAllFoodAdapter
        binding?.recyclerView58?.setHasFixedSize(true)
        binding?.recyclerView58?.layoutManager = layoutManagerCrew2
    }

    private fun getFilterAllMfl(name: String): ArrayList<OldFoodResponse.Output.R> {
        val categoryFilterNew = ArrayList<OldFoodResponse.Output.R>()
        if (name == "ALL") {
            for (data in catFilter) {
                categoryFilterNew.add(data)
            }
        } else {
            for (data in catFilter) {
                if (data.ct == name) {
                    categoryFilterNew.add(data)
                }
            }
        }
        return categoryFilterNew
    }


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
                positiveClick = {
                },
                negativeClick = {
                })
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
                positiveClick = {
                },
                negativeClick = {
                })
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


    private fun cartData() {
        oldAllFoodAdapter?.notifyDataSetChanged()
//
//        if (cartModel.isEmpty()) {
//            binding?.constraintLayout30?.hide()
//        } else {
//            binding?.constraintLayout30?.show()
//            binding?.imageView74?.setOnClickListener {
//                cartShow = if (cartShow){
//                    binding?.imageView74?.setImageDrawable(this.getDrawable(R.drawable.arrow_up))
//                    binding?.constraintLayout112?.show()
//                    false
//                }else{
//                    binding?.imageView74?.setImageDrawable(this.getDrawable(R.drawable.arrow_down))
//                    binding?.constraintLayout112?.hide()
//                    true
//                }
//
//            }

            //total Price
            itemCheckPriceCart = calculateTotal()
//            binding?.textView148?.text =
//                getString(R.string.currency) + Constant.DECIFORMAT.format(itemCheckPriceCart / 100.0)
//            binding?.textView149?.text = cartModel.size.toString() + " " + getString(R.string.items)
//            binding?.constraintLayout30?.show()
//            val layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
//            val cartAdapter = CartAdapter(cartModel, this, this)
//            binding?.recyclerView41?.layoutManager = layoutManager
//            binding?.recyclerView41?.adapter = cartAdapter


    }

    //Calculate Total Item Price
    private fun calculateTotal(): Int {
        var totalPrice = 0
        for (data in cartModel) {
            totalPrice += data.price * data.quantity
        }
        return totalPrice
    }
}