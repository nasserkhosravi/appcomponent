package com.nasserkhosravi.appcomponent.example

import android.app.Application
import com.nasserkhosravi.appcomponent.AppContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.build(this)
    }
}