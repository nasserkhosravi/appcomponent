package com.nasserkhosravi.appcomponent

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.NonNull

/**
 * Main entry point to accessing components
 * Instruction:
 * 1- [this construct]
 * 2- do your config [this getConfig]
 * 3- set your view group by [this getViewGroupManager]
 * 4- build component [getViewAccessor constructLayout]
 * you'r ready to use components
 */
class AppComponent(override var ctx: Context) : ResourceComponent {

    private lateinit var config: ViewComponentConfig
    private lateinit var viewGroupManager: BaseViewGroupManager
    private lateinit var viewManger: BaseViewAccessor

    fun construct() {
        config = ViewComponentConfig()
        viewGroupManager = BaseViewGroupManager()
        viewManger = BaseViewAccessor(this, viewGroupManager, config)
    }

    fun getConfig(): ViewComponentConfig {
        return config
    }

    fun getViewAccessor(): BaseViewAccessor {
        return viewManger
    }

    fun getViewGroupManager(): BaseViewGroupManager {
        return viewGroupManager
    }

    @NonNull
    fun getViewGroup(): ViewGroup? {
        return viewGroupManager.getViewGroup()
    }
}