package com.nasserkhosravi.appcomponent

import android.view.ViewGroup

class BaseViewGroupManager internal constructor() {
    private var viewGroup: ViewGroup? = null

    fun getViewGroup(): ViewGroup {
        if (viewGroup == null) {
            throw NullPointerException(" On view group. set your view group: [this setViewGroup]")
        }
        return viewGroup!!
    }

    fun setViewGroup(newViewGroup: ViewGroup) {
        this.viewGroup = newViewGroup
    }


    fun destroy() {
        viewGroup = null
    }
}
