package com.nasserkhosravi.appcomponent

import android.app.Activity
import android.graphics.PorterDuff
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.nasserkhosravi.appcomponent.utils.DimensUtils
import com.nasserkhosravi.appcomponent.view.NoConnectionView
import com.nasserkhosravi.appcomponent.view.ProgressBuilder
import com.nasserkhosravi.appcomponent.view.ProgressController

class BaseViewAccessor internal constructor(
    private val component: AppComponent,
    private val viewGroupManager: BaseViewGroupManager,
    private val config: ViewComponentConfig
) {
    private var tvTitle: TextView? = null
        private set

    lateinit var progress: ProgressController
        private set

    private var noConnectionView: NoConnectionView? = null
    private var banView: View? = null

    val toolbar: ViewGroup
        get() = viewGroupManager.getViewGroup().findViewById(R.id.toolbarLayout)

    fun constructLayout() {
        if (config.isEnableToolBar) {
            val viewGroup = viewGroupManager.getViewGroup()
            val toolBarLayout = viewGroup.findViewById<ViewGroup>(R.id.toolbarLayout)
            if (toolBarLayout == null) {
                throw IllegalArgumentException("enable toolbar is true but you did not supply toolbar")
            } else {
                tvTitle = toolBarLayout.findViewById(R.id.tvTitleToolbar)
                if (config.isEnableBackButton) {
                    setupBackButton(toolBarLayout)
                }
            }
        }
        buildProgress()
    }

    private fun setupBackButton(toolBarLayout: ViewGroup) {
        val imgArrowBack = ImageView(toolBarLayout.context)
        imgArrowBack.setImageResource(R.drawable.ic_arrow_back_black_24dp)
        imgArrowBack.setColorFilter(component.getColorRes(R.color.toolbar_child_color), PorterDuff.Mode.SRC_IN)
        val imgLayoutParams =
            RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        imgLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
        imgLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
        val px = DimensUtils.dpToPx(10)
        imgArrowBack.setPadding(px, px, px, px)
        imgArrowBack.layoutParams = imgLayoutParams
        imgArrowBack.setOnClickListener { (component.ctx as Activity).onBackPressed() }
        toolBarLayout.addView(imgArrowBack)
    }

    fun setTitleToolBar(titleToolBar: String) {
        tvTitle!!.text = titleToolBar
    }

    fun setTitleToolBar(@StringRes res: Int) {
        tvTitle!!.setText(res)
    }

    fun buildProgress() {
        if (viewGroupManager.getViewGroup().findViewById<View>(R.id.progressView) == null) {
            val view: View = ProgressBuilder.createFrom(component.ctx, config)
            progress = ProgressController(view, viewGroupManager.getViewGroup())
        }
    }

    /**
     * use this for banning a layout
     * this method dedicated for showing one layout over root layout that provided by main progressView group.
     * use case: use want see a page and there is no network, you show a dedicated page, and everytime network is available you hide
     * or remove ban progressView
     */
    fun createAndShowBanViewTo(@LayoutRes layoutRes: Int, to: View) {
        val toParent = to.parent as ViewGroup
        banView = View.inflate(to.context, layoutRes, null)
        banView!!.id = R.id.layoutBanView

        val lpToView = to.layoutParams
        banView!!.layoutParams = lpToView
        val indexOfChild = toParent.indexOfChild(to)
        toParent.addView(banView, indexOfChild)

        to.visibility = View.GONE
        banView!!.visibility = View.VISIBLE
    }

    fun removeBanView(to: View) {
        val toParent = to.parent as ViewGroup
        to.visibility = View.VISIBLE
        if (banView != null) {
            toParent.removeView(banView)
        }
    }

    fun getNoConnectionView(): NoConnectionView {
        if (noConnectionView == null) {
            noConnectionView = NoConnectionView(viewGroupManager.getViewGroup())
        }
        return noConnectionView!!
    }
}