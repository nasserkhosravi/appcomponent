package com.nasserkhosravi.appcomponent

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat

/**
 * different resource type
 */
object Res {
    const val WEB = 0
    const val ASSET = 1
    const val DRAWABLE = 2
    const val FILE = 3
}

@IntDef(Res.WEB, Res.ASSET, Res.DRAWABLE, Res.FILE)
annotation class ResType

/**
 * easy access to inherent resource methods
 */
interface ResourceComponent {
    val ctx: Context

    fun getStringRes(@StringRes resId: Int): String {
        return ctx.getString(resId)
    }

    @ColorInt
    fun getColorRes(@ColorRes res: Int): Int {
        return ContextCompat.getColor(ctx, res)
    }

    fun getResName(int: Int): String {
        return ResHelper.getResName(int)
    }
}

/**
 * static easy access to resource
 */
object ResHelper {

    /**
     * please take notice how this function need context. In multi language or localized app If you change localize of app
     * and pass old context, android will return previous strings resource
     */
    fun getString(context: Context, @StringRes resId: Int): String {
        return context.getString(resId)
    }

    fun getResName(id: Int): String {
        return AppContext.get().resources.getResourceEntryName(id)
    }

    @ColorInt
    fun getColorRes(@ColorRes res: Int): Int {
        return ContextCompat.getColor(AppContext.get(), res)
    }

    fun getDrawable(@DrawableRes res: Int): Drawable? {
        return ContextCompat.getDrawable(AppContext.get(), res)
    }

}