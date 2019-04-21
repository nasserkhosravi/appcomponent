package com.nasserkhosravi.appcomponent

import android.app.Application

/**
 * Application context holder for easy access to application features
 */
object AppContext {
    private lateinit var application: Application

    fun build(application: Application) {
        AppContext.application = application
    }

    fun get(): Application {
        return application
    }
}
