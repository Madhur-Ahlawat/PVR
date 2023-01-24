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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.more.offer.adapter.PHAdapter.OpenRedirection
import com.net.pvr1.ui.home.fragment.more.offer.list.OfferListActivity
import com.net.pvr1.ui.home.fragment.more.offer.response.MOfferResponse
import com.net.pvr1.ui.home.fragment.more.offer.response.OfferLocalData
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.printLog
import com.net.pvr1.utils.toast
import java.util.*

@Suppress("DEPRECATION")
class OffersAdapter(
    private val context: Activity,
    private var offerList: ArrayList<OfferLocalData>?,
    private val offerListAll: ArrayList<OfferLocalData>,
    private val listener: RecycleViewItemClickListener,
) : RecyclerView.Adapter<OffersAdapter.ViewHolder>(), View.OnClickListener,
    OfferFilterAdapter.RecycleViewItemClickListener, OpenRedirection {
    private val handler = Handler()
    private var phList: ArrayList<MOfferResponse.Output.Pu>? = ArrayList()
    private var speedScroll = 3000
    private var rowIndex = -1
    private var runnable: Runnable? = null
    private var displayMetrics = DisplayMetrics()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val itemLayoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_offer_item, null)
        return ViewHolder(itemLayoutView)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (cat, offerList1) = offerList!![position]
        holder.titlePcTextView.text = cat
        if (position == offerList!!.size - 1) {
            holder.view.visibility = View.GONE
        } else {
            holder.view.visibility = View.VISIBLE
        }
        if (position == 0) {
            if (cat.equals("TRENDING", ignoreCase = true)) {
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
        if (cat.equals("", ignoreCase = true)) {

            val list: List<String> =
                offerList!![0].offerList[0].catp.replace("Trending,", "",ignoreCase = true).replace("TREND,", "",ignoreCase = true).split(",")

            printLog("catList---->$list")


            val adapter1 = OfferFilterAdapter(list , context, this, rowIndex, holder.offerRecList)
            holder.offerRecList.adapter = adapter1
            if (rowIndex >= 0 && rowIndex < list.size)
                holder.offerRecList.smoothScrollToPosition(
                rowIndex + 1
            )
            holder.titlePcTextView.hide()
            holder.headerView.hide()
            holder.seeAll.hide()
            if (phList != null && phList!!.size > 0) {
                holder.upperView.visibility = View.VISIBLE
                phList = phList
                val layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val snapHelper: SnapHelper = PagerSnapHelper()
                holder.demoSlider.onFlingListener = null
                snapHelper.attachToRecyclerView(holder.demoSlider)
                holder.demoSlider.layoutManager = layoutManager
                val recyclerAdapter = PHAdapter(context, phList, this, 1)
                holder.demoSlider.adapter = recyclerAdapter
                if (phList!!.size > 1) {
                    addTimer(recyclerAdapter, holder.demoSlider)
                }
            } else {
                holder.upperView.visibility = View.GONE
            }
        } else {
            holder.upperView.visibility = View.GONE
            if (offerList1.size > 1) {
                if (position == 0) {
                    holder.seeAll.visibility = View.GONE
                } else {
                    holder.seeAll.visibility = View.VISIBLE
                }
            } else {
                holder.seeAll.visibility = View.GONE
            }
            val adapter1 = OffersFAdapter(context, offerList1, "F")
            holder.offerRecList.adapter = adapter1
            holder.titlePcTextView.visibility = View.VISIBLE
            holder.headerView.visibility = View.VISIBLE
        }

        holder.seeAll.setOnClickListener {
            val intent = Intent(context, OfferListActivity::class.java)
            intent.putExtra("title",cat)
            intent.putExtra("list",offerList1)
            context.startActivity(intent)
        }
    }

    private fun addTimer(recyclerAdapter: PHAdapter, demoSlider: RecyclerView) {
        runnable = object : Runnable {
            var count = 0
            var flag = true
            override fun run() {
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

    override fun onFilterClick(
        offer: String?, type: Int, rowIndex: Int, recyclerView: RecyclerView?
    ) {
        try {
            if (runnable != null) handler.removeCallbacksAndMessages(null)
            this.rowIndex = rowIndex
            val offers = ArrayList<OfferLocalData>()
            for (data in offerListAll) {
                if (data.cat.equals(offer, ignoreCase = true)) {
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
                    offerList = offers
                } else {
                    context.toast("No offer available for selected category!")
                }
            }
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun listHas(offerListAll: List<OfferLocalData>): Boolean {
        for ((cat) in offerListAll) {
            if (cat.equals("TRENDING", ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    override fun onOpen(exmovieVO: MOfferResponse.Output.Pu?) {

    }

    override fun getItemCount(): Int {
        return if (offerList != null) offerList!!.size else 0
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(offer: MOfferResponse.Output.Offer?)
    }

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
            headerView = itemView.findViewById(R.id.headerView)
            view = itemView.findViewById(R.id.view)
            upperView = itemView.findViewById(R.id.upperView)
            mainView = itemView.findViewById(R.id.mainView)
            offerRecList = itemView.findViewById(R.id.offerList)
            demoSlider = itemView.findViewById(R.id.slider)
            titlePcTextView = itemView.findViewById(R.id.text)
            seeAll = itemView.findViewById(R.id.seeAll)
        }
    }


    override fun onClick(view: View) {

    }

    companion object {
        var allData: List<MOfferResponse.Output.Offer> = ArrayList()
    }
}