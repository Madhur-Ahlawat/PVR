package com.net.pvr.ui.giftCard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityGiftCardBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.giftCard.activateGiftCard.ActivateGiftCardActivity
import com.net.pvr.ui.giftCard.activateGiftCard.AddGiftCardActivity
import com.net.pvr.ui.giftCard.activateGiftCard.CreateGiftCardActivity
import com.net.pvr.ui.giftCard.adapter.GiftCardMainAdapter
import com.net.pvr.ui.giftCard.adapter.GiftFilterAdapter
import com.net.pvr.ui.giftCard.response.GiftCardListResponse
import com.net.pvr.ui.giftCard.response.GiftCardsFilter
import com.net.pvr.ui.giftCard.viewModel.GiftCardViewModel
import com.net.pvr.utils.*
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class GiftCardActivity : AppCompatActivity() ,
    GiftCardMainAdapter.RecycleViewItemClickListener,
    GiftFilterAdapter.RecycleViewItemClickListener{

    private var binding: ActivityGiftCardBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: GiftCardViewModel by viewModels()
    private var giftCartList = ArrayList<GiftCardListResponse.Output.GiftCard>()
    private var gcFilterList = ArrayList<GiftCardsFilter>()
    private var giftCardListFilter = ArrayList<GiftCardListResponse.Output.GiftCard>()
    private var genricFound = false
    private var limit = 0
    private var customImage = false
    @Inject
    lateinit var preferences: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGiftCardBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)


        manageFunction()
    }

    private fun manageFunction() {
        if (preferences.getIsLogin()){
            binding?.llActiveCard?.show()
        }else{
            binding?.llActiveCard?.hide()

        }
        if (intent.hasExtra("from")){
            showDialogLoyalty(
                this,
                intent.getStringExtra("amt").toString(),
                intent.getStringExtra("id").toString(),
                intent.getStringExtra("date").toString()
            )
        }

        //title
        binding?.include6?.titleCommonToolbar?.text = "Gift Cards"

        //Screen Width
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        authViewModel.giftCards(width.toString(), "")
        //button title
        binding?.include37?.textView5?.text = "Make your own Gift Card"

        giftCard()
        movedNext()
    }

    private fun movedNext() {
        binding?.llActiveCard?.setOnClickListener {
            startActivity(Intent(this,ActivateGiftCardActivity::class.java))
        }

        binding?.include6?.btnBack?.setOnClickListener {
            finish()
        }

        binding?.include37?.textView5?.setOnClickListener {
            val createIntent = Intent(this@GiftCardActivity, CreateGiftCardActivity::class.java)
            createIntent.putExtra("custom", customImage)
            createIntent.putExtra("from", "upload")
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
                createIntent.putExtra("limit", limit.toString())
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
        GoogleAnalytics.hitItemListEventGC(this,"GIFTCARD",output.giftCards)

        if (output.limit!=null && output.limit!="")
        limit = output.limit.toInt()
        customImage = output.customCardFlag
        if (output.imageUpload) {
            binding?.include37?.constraintLayout10?.show()
        } else {
            binding?.include37?.constraintLayout10?.hide()
        }

        giftCartList = output.giftCards
        gcFilterList = ArrayList<GiftCardsFilter>()
        gcFilterList.add(GiftCardsFilter("ALL OCCASIONS","",true))
        for (data in output.giftCards){
            if (!checkExist(gcFilterList,data.type) && data.type != "GENERIC")
            gcFilterList.add(GiftCardsFilter(data.type,data.newImageUrl?:"",false))
            if (data.type == "GENERIC"){
                Glide.with(this)
                    .load(data.newImageUrl)
                    .error(R.drawable.gift_card_placeholder)
                    .into(binding?.ivImageGeneric!!)
                genricFound = true
                val vto: ViewTreeObserver = binding?.ivImageGeneric?.viewTreeObserver!!
                vto.addOnPreDrawListener {
                    println("binding?.ivImageGeneric?.measuredHeight-->" + binding?.ivImageGeneric?.measuredHeight)
                    println("binding?.ivImageGeneric?.measuredHeight123-->" + binding?.ivImageGeneric?.measuredWidth)
                    true
                }
                // break
            }
        }

        val filterLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val filterAdapter = GiftFilterAdapter(gcFilterList,  this, this)
        binding?.rvFilterGiftCards?.layoutManager = filterLayout
        binding?.rvFilterGiftCards?.adapter = filterAdapter

        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val giftCardMainAdapter2 = GiftCardMainAdapter(
            getNewList(output.giftCards),
            this,
            this,
            "ALL OCCASIONS"
        )
        binding?.rvGiftCards?.layoutManager = gridLayout2
        binding?.rvGiftCards?.adapter = giftCardMainAdapter2

        binding?.ivImageGeneric?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Gift Card")
                 bundle.putString("var_gift_card_banner","")
                GoogleAnalytics.hitEvent(this, "gift_card_banner", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            giftCardListFilter = ArrayList()
            for (i in giftCartList.indices) {
                if (giftCartList[i].type == ("GENERIC") && (giftCartList[i].giftValue/100) % 50 == 0) {
                    giftCardListFilter.add(giftCartList[i])
                }
            }

            if (giftCardListFilter.size > 0) {
                GoogleAnalytics.hitViewItemEvent(this,"GiftCard",giftCardListFilter[0].pkGiftId.toString(),"${giftCardListFilter[0].display}")

                val intent = Intent(this, AddGiftCardActivity::class.java)
                intent.putExtra("genericList", giftCardListFilter)
                intent.putExtra("custom", customImage)
                intent.putExtra("imageValue", giftCardListFilter[0].newImageUrl)
                intent.putExtra("key", giftCardListFilter[0].channel)
                intent.putExtra("limit", limit)
                startActivity(intent)
            }
        }

    }

    private fun getNewList(giftCards: ArrayList<GiftCardListResponse.Output.GiftCard>): List<GiftCardListResponse.Output.GiftCard> {
        var newList = ArrayList<GiftCardListResponse.Output.GiftCard>()
        for (data in giftCards){
            if (newList.size == 0 && data.type != "GENERIC"){
                newList.add(data)
            }else{
                if (!checkGCExist(newList,data.type) && data.type != "GENERIC"){
                    newList.add(data)
                }
            }
        }

        return newList
    }

    private fun checkExist(gcFilterList: ArrayList<GiftCardsFilter>, type: String): Boolean {
        for (data in gcFilterList){
            if (data.filterText == (type)){
                return true
            }
        }
        return false
    }

    private fun checkGCExist(gcFilterList: ArrayList<GiftCardListResponse.Output.GiftCard>, type: String): Boolean {
        for (data in gcFilterList){
            if (data.type == (type) ){
                return true
            }
        }
        return false
    }

    override fun giftCardClick(comingSoonItem: GiftCardListResponse.Output.GiftCard) {
        printLog("ticket---->${comingSoonItem}")
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Gift Card")
            bundle.putString("var_gift_card_banner","")
            GoogleAnalytics.hitEvent(this, "gift_card_banner", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }

        GoogleAnalytics.hitViewItemEvent(this,"GiftCard",comingSoonItem.pkGiftId.toString(),"${comingSoonItem.display}")

        giftCardListFilter = ArrayList()
        for (i in gcFilterList.indices) {
            if (gcFilterList[i].filterText == comingSoonItem.type) {
                for (j in giftCartList.indices) {
                    if (giftCartList[j].type == (comingSoonItem.type)) {
                        if (giftCartList[j].giftValue/100 % 50 == 0) //to add only multiples of 50
                            giftCardListFilter.add(giftCartList[j])
                    }
                }
            }
        }
        println("giftCardListFilter--->"+giftCardListFilter.size)

        if (giftCardListFilter.size > 0) {
            val intent = Intent(this, AddGiftCardActivity::class.java)
            intent.putExtra("genericList", giftCardListFilter)
            intent.putExtra("custom", customImage)
            intent.putExtra("imageValue", giftCardListFilter[0].newImageUrl)
            intent.putExtra("key", giftCardListFilter[0].channel)
            intent.putExtra("limit", limit)
            startActivity(intent)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun gcFilterClick(comingSoonItem: GiftCardsFilter, position: Int) {
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)

        if (position<binding?.rvFilterGiftCards?.adapter?.itemCount!! && position>0)
        binding?.rvFilterGiftCards?.scrollToPosition(position+1)
        var gcNewList = ArrayList<GiftCardListResponse.Output.GiftCard>()
        if (comingSoonItem.filterText == "ALL OCCASIONS"){
            gcNewList = giftCartList
            giftCardListFilter = giftCartList
            val giftCardMainAdapter2 = GiftCardMainAdapter(getNewList(gcNewList),  this, this,comingSoonItem.filterText)
            binding?.rvGiftCards?.layoutManager = gridLayout2
            binding?.rvGiftCards?.adapter = giftCardMainAdapter2
        }else {
            for (data in giftCartList) {
                if (comingSoonItem.filterText == data.type) {
                    gcNewList.add(data)
                }
            }
            giftCardListFilter = gcNewList
            val giftCardMainAdapter2 = GiftCardMainAdapter(gcNewList,  this, this,comingSoonItem.filterText)
            binding?.rvGiftCards?.layoutManager = gridLayout2
            binding?.rvGiftCards?.adapter = giftCardMainAdapter2
        }


    }

    private fun showDialogLoyalty(mContext: Context?, price1: String, id: String, date: String?) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Gift Card")
//            bundle.putString("var_gift_card_add_popup","")
            GoogleAnalytics.hitEvent(this, "gift_card_purchase", bundle)
            GoogleAnalytics.hitPurchaseEvent(this,
                id,price1,"Gift Card", Constant.GC_COUNT)
        }catch (e:Exception){
            e.printStackTrace()
        }

        val dialog = BottomSheetDialog(mContext!!, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.payment_success_gc_pp)
        dialog.setCancelable(true)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        val referenceNo = dialog.findViewById<View>(R.id.refrenceNo) as TextView?
        val price = dialog.findViewById<View>(R.id.price) as TextView?
        val paidDT = dialog.findViewById<View>(R.id.paidDT) as TextView?
        val titleText = dialog.findViewById<View>(R.id.titleText) as TextView?
        val dic = dialog.findViewById<View>(R.id.dic) as TextView?
        val btn = dialog.findViewById<View>(R.id.btn) as CardView?
        val logo = dialog.findViewById<View>(R.id.logo) as ImageView?
        val c = Calendar.getInstance().time
        println("Current time => $c")
        titleText?.text = "Thanks for purchasing your PVR Gift Card!"
        dic?.text = "Redirecting you to your active gift card page now! We shall be reviewing the uploaded image & will update you in the next 24 hours!"
        val df = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val formattedDate = df.format(c)
        price!!.text = "₹$price1"
        paidDT!!.text = "Paid on: $formattedDate"
        referenceNo!!.text = Html.fromHtml("Order ID:<br></br>$id")
        btn?.show()
        btn?.setOnClickListener {
            startActivity(Intent(this,ActivateGiftCardActivity::class.java))
        }
        dialog.show()
    }


}