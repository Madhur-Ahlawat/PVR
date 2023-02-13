package com.net.pvr1.utils.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.opengl.ETC1.getHeight
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.Nullable


class TextViewDrawable(context: Context?, @Nullable attrs: AttributeSet?) :
    AppCompatTextView(context!!, attrs) {
    /**
     * Support Only Drawable Left or Right
     */
    protected override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        val heightTextView = height
        val centerTextView = heightTextView / 2
        val r = Rect()
        getLineBounds(0, r)
        val lineHeight: Int = r.bottom - r.top + 1
        val centerLine = lineHeight / 2
        val topDrawable = centerLine - centerTextView
        val compoundDrawables: Array<Drawable> = compoundDrawables
        val drawableLeft = compoundDrawables[0]
        if (drawableLeft != null) {
            drawableLeft.setBounds(
                0,
                topDrawable,
                drawableLeft.intrinsicWidth,
                drawableLeft.intrinsicHeight + topDrawable
            )
        }
        val drawableRight = compoundDrawables[2]
        if (drawableRight != null) {
            drawableRight.setBounds(
                0,
                topDrawable,
                drawableRight.intrinsicWidth,
                drawableRight.intrinsicHeight + topDrawable
            )
        }
    }
}