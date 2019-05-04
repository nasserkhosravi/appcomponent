package com.nasserkhosravi.appcomponent.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.tabs.TabLayout
import com.nasserkhosravi.appcomponent.ResHelper
import java.util.*

object UIUtils {

    /**
     * really useful for showing a view and and there is no back way to showing old view
     * use case: no order available or no favorite
     */
    fun replaceViewWith(context: Context, from: View, @LayoutRes toLayout: Int): View? {
        val parent = from.parent as ViewGroup
        val lpFrom = from.layoutParams
        val newV = View.inflate(context, toLayout, null)
        newV.layoutParams = lpFrom
        parent.removeView(from)
        parent.addView(newV)
        return newV
    }

    fun layoutDirectionToRTL(window: Window) {
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
    }

    fun filterColor(img: ImageView, @ColorRes color: Int) {
        img.setColorFilter(ResHelper.getColorRes(color), PorterDuff.Mode.SRC_IN)
    }

    fun filterColor(drawable: Drawable, @ColorRes id: Int) {
        filterColorInt(drawable,ResHelper.getColorRes(id))
    }

    fun filterColorInt(drawable: Drawable, @ColorInt color: Int) {
        when (drawable) {
            is ShapeDrawable -> drawable.paint.color = color
            is GradientDrawable -> drawable.setColor(color)
            is ColorDrawable -> drawable.color = color
        }
    }

    /**
     * @link https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
     * /tested on API 27 when we have focus left from edit text to another UI like imageview and activity passed from activity class.
     */
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * I did not test it.
     *
     * @link https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
     * this intended for hiding keyboard from fregment.
     */
    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun activityChooserSafe(activity: Activity, intent: Intent) {
        try {
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "You don'resultBack have GoogleHelper Play installed", Toast.LENGTH_LONG).show()
        }

    }

    fun changeFontTabs(tabLayout: TabLayout, typeface: Typeface) {
        val vg = tabLayout.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildCount = vgTab.childCount
            for (i in 0 until tabChildCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = typeface
                }
            }
        }
    }

    private fun getParentColor(view: View): Int {
        if (view.parent != null) {
            val viewGroup = view.parent as ViewGroup
            val background = viewGroup.background
            if (background is ColorDrawable) {
                return background.color
            }
        }
        return 0
    }

    @SuppressLint("ObsoleteSdkInt")
    fun makefullScreen(activity: Activity) {
        if (Build.VERSION.SDK_INT in 12..18) {
            val v = activity.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView = activity.window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarColor(activity: Activity, @ColorRes color: Int) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor = ResHelper.getColorRes(color)
    }

    fun layoutDirectionToLTR(window: Window) {
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
    }

    /**
     * you can change color of bottom line edit text by backgroundTint attribute or you can remove or hide it with background=@null
     */
    fun changeBottomLineEditText(editText: EditText, @ColorRes colorRes: Int) {
        editText.background.mutate().setColorFilter(
            ResHelper.getColorRes(colorRes), PorterDuff.Mode
                .SRC_ATOP
        )
    }

    fun drawLineOn(tv: TextView) {
        tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun setNavigationBarColor(activity: Activity, @ColorRes colorRes: Int) {
        activity.window.navigationBarColor = ContextCompat.getColor(activity, colorRes)
    }

    fun isRTL(locale: Locale): Boolean {
        val directionality = Character.getDirectionality(locale.displayName[0]).toInt()
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT.toInt() || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC.toInt()
    }

    fun isRTL(activity: Activity): Boolean {
        val config = activity.resources.configuration
        return config.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    fun isRTLCompat(view: View): Boolean {
        return (ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_RTL)
    }

    fun isRTLCompatByLocal(): Boolean {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL
    }
}