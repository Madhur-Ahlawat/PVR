package com.net.pvr1.ui.home.fragment.more.offer.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.more.offer.OfferList
import com.net.pvr1.ui.home.fragment.more.offer.PHAdapter
import com.net.pvr1.ui.home.fragment.more.offer.offerDetails.OfferDetailsActivity
import com.net.pvr1.ui.home.fragment.more.offer.response.MOfferResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import java.util.*

@Suppress("DEPRECATION")
class OffersAdapter(
    private var context: Activity,
    private var offerList: ArrayList<OfferList>,
    private var offerListAll: ArrayList<OfferList>,
    private var type: String,
    private var phList: ArrayList<MOfferResponse.Output.Ph>
) : RecyclerView.Adapter<OffersAdapter.ViewHolder>(), View.OnClickListener {
    private var speedScroll = 3000
    private var allData: List<MOfferResponse.Output.Offer> = ArrayList<MOfferResponse.Output.Offer>()

    private var rowIndex = -1
    private val handler = Handler()
    private var runnable: Runnable? = null
    private var displayMetrics = DisplayMetrics()

    override fun onClick(view: View) {
//        val datum: Datum = view.tag as Datum
//        if (datum != null && listener != null) listener.onItemClick(datum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayoutView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_offer_item, null)
        return ViewHolder(itemLayoutView)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obj: OfferList = offerList[position]
        holder.titlePcTextView.text = obj.cat

        try {
            println("offers----Adapter>${offerList.size}--->${phList.size}")
            if (position == offerList.size - 1) {
                holder.view.hide()
            } else {
                holder.view.show()
            }

            if (position == 0) {
                if (obj.cat == "TRENDING") {
                    holder.mainView.setBackgroundColor(Color.parseColor("#EDE8E9"))
                } else {
                    holder.mainView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            } else {
                holder.mainView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            val layoutParams: ViewGroup.LayoutParams = LinearLayout.LayoutParams(
                displayMetrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            holder.mainView.layoutParams = layoutParams
            val snapHelper1: SnapHelper = PagerSnapHelper()
            holder.offerRecList.onFlingListener = null
            snapHelper1.attachToRecyclerView(holder.offerRecList)
            holder.offerRecList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            if (obj.cat!!.contentEquals("")) {
                try {
                    val list: List<String> = listOf(
                        listOf(
                            offerList[0].list[0].catp.replace("Trending,", "").replace("TREND,", "")
                                .split("\\s*,\\s*")
                        ).toString()
                    )

                    val adapter1 =
                        OfferFilterAdapter(list, context, this, rowIndex, holder.offerRecList)
                    holder.offerRecList.adapter = adapter1
                    if (rowIndex >= 0 && rowIndex < list.size) holder.offerRecList.smoothScrollToPosition(
                        rowIndex + 1
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("exceptionAdapter---->${e.message}")
                }
                holder.titlePcTextView.hide()
                holder.headerView.hide()
                holder.seeAll.hide()
                if (phList.size > 0) {
                    holder.upperView.show()
                    val layoutManager = LinearLayoutManager(
                        context, LinearLayoutManager.HORIZONTAL, false
                    )
                    val snapHelper: SnapHelper = PagerSnapHelper()
                    holder.demoSlider.onFlingListener = null
                    snapHelper.attachToRecyclerView(holder.demoSlider)
                    holder.demoSlider.layoutManager = layoutManager
                    val recyclerAdapter = PHAdapter(context, phList, 1)
                    holder.demoSlider.adapter = recyclerAdapter
                    if (phList.size > 1) {
                        addTimer(recyclerAdapter, holder.demoSlider)
                    }
                } else {
                    holder.upperView.hide()
                }

            } else {
                holder.upperView.hide()
                if (obj.list.size > 1) {
                    if (position == 0) {
                        holder.seeAll.hide()
                    } else {
                        holder.seeAll.show()
                    }
                } else {
                    holder.seeAll.hide()
                }
                val adapter1 = OffersFAdapter(
                    context, obj.list as ArrayList<MOfferResponse.Output.Offer>, "F"
                )
                holder.offerRecList.adapter = adapter1
                holder.titlePcTextView.visibility = View.VISIBLE
                holder.headerView.visibility = View.VISIBLE
            }
            holder.seeAll.setOnClickListener {
            val intent = Intent(context, OfferDetailsActivity::class.java)
            intent.putExtra("TITLE", obj.cat)
            allData = obj.list
            context.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun addTimer(recyclerAdapter: PHAdapter, demoSlider: RecyclerView) {
        runnable = object : Runnable {
            var count = 0
            var flag = true
            override fun run() {
                println("position--->" + count + flag + recyclerAdapter.itemCount)
                if (count < recyclerAdapter.itemCount) {
                    if (count == recyclerAdapter.itemCount - 1) {
                        flag = false
                    } else if (count == 0) {
                        flag = true
                    }
                    if (flag) count++ else count--
                    demoSlider.smoothScrollToPosition(count)
                    handler.postDelayed(this, speedScroll.toLong())
                }
            }
        }
        handler.postDelayed(runnable as Runnable, speedScroll.toLong())
    }

    //
    @SuppressLint("NotifyDataSetChanged")
    fun onFilterClick(offer: String?, type: Int, rowIndex: Int, offerRecList: RecyclerView) {
        try {
            if (runnable != null) handler.removeCallbacksAndMessages(null)
            println("offerListAll--->" + offerListAll.size)
            this.rowIndex = rowIndex
            val offers: MutableList<OfferList> = ArrayList<OfferList>()
            for (data in offerListAll) {
                if (data.cat == offer) {
                    if (listHas(offerListAll)) {
                        offers.add(offerListAll[0])
                        offers.add(offerListAll[1])
                    } else {
                        offers.add(offerListAll[0])
                    }
                    if (offerListAll.size > 2) offers.add(data)
                }
            }
            if (type == 0) {
                offerList = offerListAll
            } else {
                if (offers.size > 0) {
                    offerList = offers as ArrayList<OfferList>
                } else {
                    Toast.makeText(
                        context, "No offer available for selected category!", Toast.LENGTH_SHORT
                    ).show()
                }
            }
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun listHas(offerListAll: ArrayList<OfferList>): Boolean {
        for (offerData in offerListAll) {
            if (offerData.cat == "TRENDING") {
                return true
            }
        }
        return false
    }

    //    fun onOpen(exmovieVO: Pu?) {}
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titlePcTextView: TextView
        var seeAll: TextView
        var offerRecList: RecyclerView
        var demoSlider: RecyclerView
        var mainView: CardView
        var headerView: RelativeLayout
        var view: LinearLayout
        var upperView: LinearLayout

        init {
            headerView = itemView.findViewById<View>(R.id.headerView) as RelativeLayout
            view = itemView.findViewById<View>(R.id.view) as LinearLayout
            upperView = itemView.findViewById<View>(R.id.upperView) as LinearLayout
            mainView = itemView.findViewById<View>(R.id.mainView) as CardView
            offerRecList = itemView.findViewById<View>(R.id.offerList) as RecyclerView
            demoSlider = itemView.findViewById<View>(R.id.slider) as RecyclerView
            titlePcTextView = itemView.findViewById<View>(R.id.text) as TextView
            seeAll = itemView.findViewById<View>(R.id.seeAll) as TextView
        }
    }

    override fun getItemCount(): Int {
        return if (offerList != null) offerList.size else 0
    }

    interface RecycleViewItemClickListener

    companion object
}