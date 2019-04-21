package com.nasserkhosravi.appcomponent.view.widget

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.view.ViewCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.nasserkhosravi.appcomponent.R

/**
 * main source at https://github.com/TheLuckyCoder/Expandable-Card-View/blob/master/expandablecardview/src/main/java/net/theluckycoder/expandablecardview/ExpandableCardView.kt
 * A CardView that can be expanded
 **/
open class ExpandableCardView : CardView {
    fun tag() = "ExpandableCardView"

    private val ivArrow by bind<ImageView>(R.id.iv_card_expand)
    val tvTitle by bind<TextView>(R.id.tv_card_title)
    val layoutContent by bind<View>(R.id.layout_content)

    var tvDescription: TextView? = null
    var rv: RecyclerView? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        inflate(context, R.layout.view_expandable_card, this)
        rv = findViewById(R.id.rv)
        tvDescription = findViewById(R.id.tv_card_desc)
        var expanded = false
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.ExpandableCardView
            )
            cardTitle = typedArray.getString(R.styleable.ExpandableCardView_ecvTitle) ?: ""
            expanded = typedArray.getBoolean(R.styleable.ExpandableCardView_ecvExpanded, false)
            expandDuration = typedArray.getInt(R.styleable.ExpandableCardView_ecvExpand_duration, 400).toLong()
            typedArray.recycle()
        }

        post {
            if (expanded) {
                expand(false)
            } else {
                setHeightToZero(false)
            }
        }

        setOnClickListener {
            if (isExpanded) {
                collapse(true)
            } else {
                expand(true)
            }
        }
    }

    private fun <T : View> View.bind(@IdRes res: Int): Lazy<T> =
        lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }

    private fun rotateArrow(rotation: Float, animate: Boolean) {
        ViewCompat.animate(ivArrow)
            .rotation(rotation)
            .withLayer()
            .setDuration(if (animate) expandDuration else 0)
            .start()
    }

    private fun setHeightToZero(animate: Boolean) {
        if (animate) {
            animate(layoutContent.height, 0)
        } else {
            setContentHeight(0)
        }
    }

    private fun setHeightToContentHeight(animate: Boolean) {
        measureContentView()
        var targetHeight = 0


        //computing height of recycleView
//        if (rv != null && rv!!.visibility == View.VISIBLE ) {
//            val adapter = rv!!.adapter as AttributeAdapter
//            targetHeight = adapter.estimatedHeightTV * adapter.items!!.size
//        } else {
//            targetHeight = layoutContent.measuredHeight
//        }
        targetHeight = layoutContent.measuredHeight
        Log.d(tag(), targetHeight.toString())

        if (animate) {
            animate(0, targetHeight)
        } else {
            setContentHeight(targetHeight)
        }
    }

    private fun setContentHeight(height: Int) {
        layoutContent.layoutParams.height = height
        layoutContent.requestLayout()
    }

    private fun measureContentView() {
        val widthMS = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST)
        val heightMS = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        layoutContent.measure(widthMS, heightMS)
    }

    private fun animate(from: Int, to: Int) {
        val valuesHolder: PropertyValuesHolder = PropertyValuesHolder.ofInt("prop", from, to)

        val animator = ValueAnimator.ofPropertyValuesHolder(valuesHolder)
        animator.duration = expandDuration
        animator.addUpdateListener {
            val value = animator.getAnimatedValue("prop") as Int? ?: 0
            layoutContent.layoutParams.height = value
            layoutContent.requestLayout()
            invalidate()
        }
        animator.start()
    }

    /**
     * Check if the card is expanded
     */
    var isExpanded = false
        private set

    /**
     * Expand the Card
     */
    open fun expand(animate: Boolean) {
        if (isExpanded) return

        setHeightToContentHeight(animate)
        rotateArrow(180f, animate)
        isExpanded = true
    }

    /**
     * Collapse the Card
     */
    open fun collapse(animate: Boolean) {
        if (!isExpanded) return

        setHeightToZero(animate)
        rotateArrow(0f, animate)
        isExpanded = false
    }

    /**
     * @property cardTitle The title of the card
     */
    open var cardTitle: CharSequence
        get() = tvTitle.text
        set(title) {
            tvTitle.text = title
        }

    /**
     * Sets the title of the card
     * @param resId String resource to display as title
     * @see cardTitle
     */
    open fun setCardTitle(@StringRes resId: Int) {
        cardTitle = context.getString(resId)
    }

    /**
     * @property expandDuration The duration of the expand animation
     * @throws IllegalArgumentException if the duration is <= 0
     */
    open var expandDuration: Long = 400
        set(duration) {
            if (duration > 0) {
                field = duration
            } else {
                throw IllegalArgumentException("Card Expand Duration can not be smaller than or equal to 0")
            }
        }

}
