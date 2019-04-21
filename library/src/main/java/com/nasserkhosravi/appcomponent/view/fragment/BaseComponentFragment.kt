package com.nasserkhosravi.appcomponent.view.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nasserkhosravi.appcomponent.AppComponent
import com.nasserkhosravi.appcomponent.BaseViewAccessor
import com.nasserkhosravi.appcomponent.BaseViewGroupManager
import com.nasserkhosravi.appcomponent.ViewComponentConfig
import com.nasserkhosravi.appcomponent.view.BaseComponentActivity

abstract class BaseComponentFragment : Fragment() {

    private lateinit var appDelegate: AppComponent

    @LayoutRes
    protected abstract fun layoutRes(): Int

    fun getBaseActivity(): BaseComponentActivity = activity as BaseComponentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDelegate = AppComponent(getBaseActivity())
        appDelegate.construct()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (layoutRes() != 0) {
            return inflater.inflate(layoutRes(), container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun getConfig(): ViewComponentConfig {
        return appDelegate.getConfig()
    }

    fun getViewsComponent(): BaseViewAccessor {
        return appDelegate.getViewAccessor()
    }

    fun getViewGroupManager(): BaseViewGroupManager {
        return appDelegate.getViewGroupManager()
    }

    fun appComponent(): AppComponent {
        return appDelegate
    }

}