package com.nasserkhosravi.appcomponent.view

import android.content.Context
import android.graphics.Typeface
import android.support.annotation.IntDef
import android.support.design.widget.CoordinatorLayout
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.nasserkhosravi.appcomponent.Device
import com.nasserkhosravi.appcomponent.R
import com.nasserkhosravi.appcomponent.ResHelper
import com.nasserkhosravi.appcomponent.ViewComponentConfig
import com.nasserkhosravi.appcomponent.utils.DimensUtils

/**
 *
 */
class ProgressController constructor(view: View, layout: ViewGroup) {
    val progressView: View = view

    init {
        if (view.parent == null) {
            layout.addView(view)
        } else {
            val oldLayout = view.parent as ViewGroup
            oldLayout.removeView(view)
            layout.addView(view)
        }
    }

    fun show() {
        progressView.visibility = View.VISIBLE
    }

    fun hide() {
        progressView.visibility = View.GONE
    }
}

/**
 * A special class for focus on options of progress to building
 */
class ProgressBuilder(private var context: Context) {

    private var width = 0
    private var height = 0
    private var layoutGravity = Gravity.CENTER

    fun inCenter(): ProgressBuilder {
        layoutGravity = Gravity.CENTER
        return this
    }

    fun withSizeDP(width: Int, height: Int): ProgressBuilder {
        this.width = DimensUtils.dpToPx(width)
        this.height = DimensUtils.dpToPx(height)
        return this
    }

    fun buildCircleProgressView(): View {
        val view = ProgressBar(context, null, android.R.attr.progressBarStyleInverse)
        view.isIndeterminate = true
        view.id = R.id.progressView
        view.visibility = View.GONE
        view.isIndeterminate = true
        if (height == 0 && width == 0) {
            withSizeDP(40, 40)
        }
        val layoutParams = CoordinatorLayout.LayoutParams(width, height)
        if (layoutGravity == Gravity.CENTER) {
            layoutParams.gravity = Gravity.CENTER
        }
        view.layoutParams = layoutParams
        return view
    }

    fun buildLinearProgressView(title: String): View {
        val tvTitle = TextView(context)
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)

        tvTitle.setTypeface(null, Typeface.BOLD)
        tvTitle.setTextColor(ResHelper.getColorRes(R.color.secondary_text))
        tvTitle.text = title

        val lpTV = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lpTV.gravity = Gravity.CENTER
        tvTitle.layoutParams = lpTV

        val progress = View.inflate(context, R.layout.widget_linear_progress_bar, null) as ProgressBar

        if (width == 0) {
            width = Device.get().width / 2
        }
        if (height == 0) {
            height = context.resources.getDimension(R.dimen.default_linear_progress_height).toInt()
        }
        val lpProgress = LinearLayout.LayoutParams(width, height)
        lpProgress.gravity = Gravity.CENTER
        progress.layoutParams = lpProgress

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(tvTitle)
        layout.addView(progress)

        val layoutParams = CoordinatorLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (layoutGravity == Gravity.CENTER) {
            layoutParams.gravity = Gravity.CENTER
        }
        layout.layoutParams = layoutParams
        layout.visibility = View.GONE
        return layout
    }

    //did not test
    fun buildCustom(): View {
        val progress = View.inflate(context, R.layout.widget_custom_progress_bar, null)
        val layoutParams = CoordinatorLayout.LayoutParams(width, height)
        if (layoutGravity == Gravity.CENTER) {
            layoutParams.gravity = Gravity.CENTER
        }
        progress.layoutParams = layoutParams
        return progress
    }

    companion object {
        const val CIRCLE_PROGRESS = 0
        const val LINEAR_PROGRESS = 1
        const val CUSTOM_PROGRESS = 2

        fun createFrom(context: Context, config: ViewComponentConfig): View {
            return when (config.progressType) {
                CIRCLE_PROGRESS -> {
                    ProgressBuilder(context).inCenter().buildCircleProgressView()
                }
                LINEAR_PROGRESS -> {
                    val progressTitle: String = if (config.progressTitle == null) {
                        ResHelper.getString(context, R.string.linear_progress_text)
                    } else {
                        config.progressTitle!!
                    }
                    ProgressBuilder(context).inCenter().buildLinearProgressView(progressTitle)
                }
                CUSTOM_PROGRESS -> {
                    ProgressBuilder(context).inCenter().buildCustom()
                }
                else -> throw IllegalArgumentException()
            }
        }
    }
}

@IntDef(ProgressBuilder.CIRCLE_PROGRESS, ProgressBuilder.LINEAR_PROGRESS, ProgressBuilder.CUSTOM_PROGRESS)
annotation class ProgressType