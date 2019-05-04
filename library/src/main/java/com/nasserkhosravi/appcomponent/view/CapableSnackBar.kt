package com.nasserkhosravi.appcomponent.view

import android.annotation.SuppressLint
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.nasserkhosravi.appcomponent.R
import com.nasserkhosravi.appcomponent.ResHelper

/**
 * Capable snack bar is created to adding some feature and having general control to create different state and theme
 * for snack bar.
 * You can override resource used
 */
class CapableSnackBar(parent: ViewGroup, content: View, callback: CapableContentViewCallback) :
    BaseTransientBottomBar<CapableSnackBar>(
        parent, content
        , callback
    ) {
    companion object {
        const val SHORT_DURATION_MS = 1500
        const val LONG_DURATION_MS = 2750
        const val LENGTH_INDEFINITE_MS = 100000

        @SuppressLint("WrongConstant")
        fun make(coordinateLayout: ViewGroup): CapableSnackBar {
            val inflater = LayoutInflater.from(coordinateLayout.context)
            val content = inflater.inflate(R.layout.layout_capable_snack_bar, coordinateLayout, false)
            val viewCallback = CapableContentViewCallback(content)
            val customSandbar = CapableSnackBar(coordinateLayout, content, viewCallback)
            customSandbar.setDuration(LONG_DURATION_MS)
            coordinateLayout.setOnClickListener { dismissSnackBarIfIsOpen(customSandbar) }
            customSandbar.view.setOnClickListener { dismissSnackBarIfIsOpen(customSandbar) }
            return customSandbar
        }

        private fun dismissSnackBarIfIsOpen(customSnackBar: CapableSnackBar) {
            if (customSnackBar.isShown) {
                customSnackBar.dismiss()
            }
        }
    }

    private var duration: Int = 0
    private var onEndListener: EndListener? = null

    fun setText(text: String): CapableSnackBar {
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.text = text
        return this
    }

    fun setText(@StringRes id: Int): CapableSnackBar {
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.text = context.getString(id)
        return this
    }

    fun setTextColor(color: Int): CapableSnackBar {
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(color)
        return this
    }

    fun setActionTextColor(color: Int): CapableSnackBar {
        val textView = view.findViewById<TextView>(R.id.snackbar_action)
        textView.setTextColor(color)
        return this
    }

    fun setActionTextColorRes(@ColorRes colorId: Int): CapableSnackBar {
        setActionTextColor(getColorRes(colorId))
        return this
    }

    fun setAction(text: CharSequence, action: () -> Any): CapableSnackBar {
        val actionView = view.findViewById<TextView>(R.id.snackbar_action)
        actionView.text = text
        actionView.visibility = View.VISIBLE
        actionView.setOnClickListener {
            action()
            dismiss()
        }
        return this
    }

    override fun show() {
        super.show()
        if (onEndListener != null) {
            Handler().postDelayed({ onEndListener!!.onEnd() }, duration.toLong())
        }
    }

    fun setOnEndListener(onEndListener: EndListener): CapableSnackBar {
        this.onEndListener = onEndListener
        return this
    }

    @SuppressLint("SwitchIntDef")
    override fun setDuration(@SnackDuration duration: Int): CapableSnackBar {
        when (duration) {
            LONG_DURATION_MS -> {
                super.setDuration(BaseTransientBottomBar.LENGTH_LONG)
            }
            SHORT_DURATION_MS -> {
                super.setDuration(BaseTransientBottomBar.LENGTH_SHORT)
            }
            LENGTH_INDEFINITE_MS -> {
                super.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE)
            }
        }
        this.duration = duration
        return this
    }

    override fun getDuration(): Int {
        return duration
    }

    fun setBackgroundColor(color: Int): CapableSnackBar {
        getView().setBackgroundColor(color)
        return this
    }

    fun withNormalDefaultTheme(): CapableSnackBar {
        setBackgroundColor(getColorRes(R.color.normal_capable_snackbar_background_color))
        setTextColor(getColorRes(R.color.normal_capable_snackbar_text_color))
        setActionTextColorRes(R.color.normal_capable_snackbar_action_text_color)
        return this
    }

    fun withErrorDefaultTheme(): CapableSnackBar {
        setBackgroundColor(getColorRes(R.color.error_capable_snackbar_background_color))
        setTextColor(getColorRes(R.color.error_capable_snackbar_text_color))
        setActionTextColorRes(R.color.error_capable_snackbar_action_text_color)
        return this
    }

    private fun getColorRes(@ColorRes id: Int): Int {
        return ResHelper.getColorRes(id)
    }

    class CapableContentViewCallback(private val content: View) : BaseTransientBottomBar.ContentViewCallback {

        override fun animateContentIn(delay: Int, duration: Int) {
            content.scaleY = 0f
            ViewCompat.animate(content).scaleY(1f).setDuration(duration.toLong()).startDelay = delay.toLong()
        }

        override fun animateContentOut(delay: Int, duration: Int) {
            content.scaleY = 1f
            ViewCompat.animate(content).scaleY(0f).setDuration(duration.toLong()).startDelay = delay.toLong()
        }
    }

    interface EndListener {
        fun onEnd()
    }

    @IntDef(LENGTH_INDEFINITE_MS, SHORT_DURATION_MS, LONG_DURATION_MS)
    annotation class SnackDuration
}