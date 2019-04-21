package com.nasserkhosravi.appcomponent

import android.util.Log

/**
 * a tiny wrapper to Log
 */
object AppLog {
    var isLogEnable = true
    private val noTag = "AppLog"

    fun d(tag: String, m: String) {
        runLog {
            Log.d(tag, m)
        }
    }

    fun d(m: String) {
        runLog {
            Log.d(noTag, m)
        }
    }

    fun d(tag: Any, m: String) {
        runLog {
            val name = tag::javaClass.name
            Log.d(name, m)
        }
    }

    private fun runLog(action: () -> Unit) {
        if (isLogEnable) {
            action()
        }
    }

}