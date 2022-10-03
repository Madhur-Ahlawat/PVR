package com.net.pvr1.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px

import androidx.recyclerview.widget.RecyclerView
open class RVPagerSnapFancyDecorator(
    @Px totalWidth: Float?,
    @Px itemWidth: Float,
    itemPeekingPercent: Float = .5f
) : RecyclerView.ItemDecoration() {
    private val mInterItemsGap: Int
    private val mNetOneSidedGap: Int

    init {
        val cardPeekingWidth = (itemWidth * itemPeekingPercent + .5f).toInt()
        mInterItemsGap = (totalWidth!!.toInt() - itemWidth.toInt()) / 2
        mNetOneSidedGap = mInterItemsGap / 2 - cardPeekingWidth
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = parent.getChildAdapterPosition(view)
        val isFirstItem = isFirstItem(index)
        val isLastItem = isLastItem(index, parent)

        val leftInset = if (isFirstItem) mInterItemsGap else mNetOneSidedGap
        val rightInset = if (isLastItem) mInterItemsGap else mNetOneSidedGap

        outRect.set(leftInset, 0, rightInset, 0)
    }


    private fun isFirstItem(index: Int) = index == 0
    private fun isLastItem(index: Int, recyclerView: RecyclerView) = index == recyclerView.adapter!!.itemCount - 1

}