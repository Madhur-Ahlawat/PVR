package com.net.pvr1.ui.giftCard

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityGiftCardBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.giftCard.activateGiftCard.ActivateGiftCardActivity
import com.net.pvr1.ui.giftCard.activateGiftCard.CreateGiftCardActivity
import com.net.pvr1.ui.giftCard.adapter.GiftCardMainAdapter
import com.net.pvr1.ui.giftCard.adapter.GiftFilterAdapter
import com.net.pvr1.ui.giftCard.response.GiftCardListResponse
import com.net.pvr1.ui.giftCard.response.GiftCardsFilter
import com.net.pvr1.ui.giftCard.viewModel.GiftCardViewModel
import com.net.pvr1.ui.myBookings.response.GiftCardResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class GiftCardActivity : AppCompatActivity() ,GiftCardMainAdapter.RecycleViewItemClickListener,GiftFilterAdapter.RecycleViewItemClickListener{
    private var binding: ActivityGiftCardBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: GiftCardViewModel by viewModels()
    private var giftCartList = ArrayList<GiftCardListResponse.Output.GiftCard>()
    private var giftCardListFilter = ArrayList<GiftCardListResponse.Output.GiftCard>()
    private var genricFound = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGiftCardBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include6?.titleCommonToolbar?.text = "Gift Cards"

        //Screen Width
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        authViewModel.giftCards(width.toString(), "")
        binding?.include37?.textView5?.text = "Make your own Gift Card"
        giftCard()
        movedNext()
    }

    private fun movedNext() {
        binding?.view26?.setOnClickListener {
            startActivity(Intent(this,ActivateGiftCardActivity::class.java))
        }

        binding?.include6?.btnBack?.setOnClickListener {
            finish()
        }
        binding?.include37?.textView5?.setOnClickListener {
            val createIntent = Intent(this@GiftCardActivity, CreateGiftCardActivity::class.java)
            giftCardListFilter = ArrayList<GiftCardListResponse.Output.GiftCard>()
            for (i in giftCartList.indices) {
                println("amtval....>"+(giftCartList[i].giftValue/100).toString())
                if (giftCartList[i].type == ("GENERIC") && (giftCartList[i].giftValue/100) % 50 == 0) {
                    giftCardListFilter.add(giftCartList[i])
                }
            }
            if (giftCardListFilter.size > 0) {
                createIntent.putExtra("genericList", giftCardListFilter)
                createIntent.putExtra("key", giftCardListFilter[0].channel)
                createIntent.putExtra("limit", "500")
            }
            startActivity(createIntent)
        }
    }


    private fun giftCard() {
        authViewModel.userResponseLiveData.observe(this) {
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: GiftCardListResponse.Output) {
        giftCartList = output.giftCards
        val gcFilterList = ArrayList<GiftCardsFilter>()
        gcFilterList.add(GiftCardsFilter("All Occasions","",true))
        for (data in output.giftCards){
            if (!checkExist(gcFilterList,data.type))
            gcFilterList.add(GiftCardsFilter(data.type,data.newImageUrl?:"",false))
            if (data.type == "GENERIC" && !genricFound){
                Glide.with(this)
                    .load(data.newImageUrl)
                    .error(R.drawable.app_icon)
                    .into(binding?.ivImageGeneric!!)
                genricFound = true
               // break
            }
        }

        val filterLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val filterAdapter = GiftFilterAdapter(gcFilterList,  this, this)
        binding?.recyclerView2?.layoutManager = filterLayout
        binding?.recyclerView2?.adapter = filterAdapter

        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val giftCardMainAdapter2 = GiftCardMainAdapter(output.giftCards,  this, this)
        binding?.recyclerView3?.layoutManager = gridLayout2
        binding?.recyclerView3?.adapter = giftCardMainAdapter2

    }

    private fun checkExist(gcFilterList: ArrayList<GiftCardsFilter>, type: String): Boolean {
        for (data in gcFilterList){
            if (data.filterText == (type)){
                return true
            }
        }
        return false
    }

    override fun giftCardClick(comingSoonItem: GiftCardListResponse.Output.GiftCard) {
//        startActivity(Intent(this,ActivateGiftCardActivity::class.java))
    }

    override fun gcFilterClick(comingSoonItem: GiftCardsFilter, position: Int) {
        if (position<binding?.recyclerView2?.adapter?.itemCount!! && position>0)
        binding?.recyclerView2?.scrollToPosition(position+1)
        var gcNewList = ArrayList<GiftCardListResponse.Output.GiftCard>()
        if (comingSoonItem.filterText == "All Occasions"){
            gcNewList = giftCartList
            giftCardListFilter = giftCartList
        }else {
            for (data in giftCartList) {
                if (comingSoonItem.filterText == data.type) {
                    gcNewList.add(data)
                }
            }
            giftCardListFilter = gcNewList
        }
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val giftCardMainAdapter2 = GiftCardMainAdapter(gcNewList,  this, this)
        binding?.recyclerView3?.layoutManager = gridLayout2
        binding?.recyclerView3?.adapter = giftCardMainAdapter2
    }

}