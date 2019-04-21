package com.nasserkhosravi.appcomponent.utils

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.nasserkhosravi.appcomponent.AppContext

/**
 * Dimension and measurement methods and values.
 */

object DimensUtils {

    fun spToPx(sps: Int): Int {
        return Math.round(AppContext.get().resources.displayMetrics.scaledDensity * sps)
    }

    fun dpToPx(dps: Int): Int {
        return Math.round(AppContext.get().resources.displayMetrics.density * dps)
    }

    fun spToPx(sps: Float): Float {
        val dm = AppContext.get().resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sps, dm)
    }

    fun dpToPx(dps: Float): Float {
        val dm = AppContext.get().resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, dm)
    }

    /**
     * return estimated height of text view in px based on text and it's size
     */
    fun getHeightOfTextView(tv: TextView, textSize: Float, deviceWidth: Int): Int {
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        tv.measure(widthMeasureSpec, heightMeasureSpec)
        return tv.measuredHeight
    }
}
