package com.net.pvr1.utils.view

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.TextView


abstract class RightDrawableOnTouchListener(view: TextView) : OnTouchListener {
    var drawable: Drawable? = null
    private val fuzz = 10

    /**
     * @param keyword
     */
    init {
        val drawables = view.compoundDrawables
        if (drawables != null && drawables.size == 4) drawable = drawables[2]
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && drawable != null) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val bounds: Rect = drawable!!.bounds
            if ((x >= (v.right - bounds.width() - fuzz) && x <= (v.right - v.paddingRight) + fuzz) && y >= v.paddingTop - fuzz && y <= v.height - v.paddingBottom + fuzz) {
                return onDrawableTouch(event)
            }
        }
        return false
    }

    abstract fun onDrawableTouch(event: MotionEvent?): Boolean
}