package com.nasserkhosravi.appcomponent.view.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseComponentDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (layoutRes() != 0) {
            return inflater.inflate(layoutRes(), container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @LayoutRes
    protected abstract fun layoutRes(): Int

}