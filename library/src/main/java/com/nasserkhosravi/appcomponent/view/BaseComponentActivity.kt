package com.nasserkhosravi.appcomponent.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nasserkhosravi.appcomponent.AppComponent
import com.nasserkhosravi.appcomponent.BaseViewAccessor
import com.nasserkhosravi.appcomponent.BaseViewGroupManager
import com.nasserkhosravi.appcomponent.ViewComponentConfig
import org.greenrobot.eventbus.EventBus

abstract class BaseComponentActivity : AppCompatActivity() {
    lateinit var appComponent: AppComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent = AppComponent(this)
        appComponent.construct()
    }

    fun registerForEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    fun unRegisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    fun getConfig(): ViewComponentConfig {
        return appComponent.getConfig()
    }

    fun getViewComponent(): BaseViewAccessor {
        return appComponent.getViewAccessor()
    }

    fun getViewGroupManager(): BaseViewGroupManager {
        return appComponent.getViewGroupManager()
    }

}