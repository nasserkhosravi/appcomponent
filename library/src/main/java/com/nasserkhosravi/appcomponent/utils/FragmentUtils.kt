package com.nasserkhosravi.appcomponent.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


/**
 * Created by Nasser on 11/9/2017.
 */
object FragmentUtils {
    fun replace(frameLayoutId: Int, fragment: Fragment, activity: AppCompatActivity) {
        activity.supportFragmentManager.beginTransaction().replace(frameLayoutId, fragment).commit()
    }
}